package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.EssenceBoilerBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluidColor;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import org.jetbrains.annotations.Nullable;

public class EssenceBoilerBlockEntityRenderer
        implements BlockEntityRenderer<
        EssenceBoilerBlockEntity,
        EssenceBoilerBlockEntityRenderer.RenderState> {

    private final ItemModelResolver itemModelResolver;
    private static final float ITEM_SCALE = 0.42F;
    private static final float ITEM_MIN_Y = 0.56F;

    private static final float[] ITEM_BASE_RADIUS = {
            0.46F,
            0.34F,
            0.19F
    };

    private static final float[] ITEM_RADIUS_DRIFT = {
            0.040F,
            0.035F,
            0.030F
    };

    private static final float[] ITEM_DRIFT_SPEED = {
            0.008F,
            0.007F,
            0.009F
    };

    private static final float[] ITEM_ORBIT_SPEED = {
            1.0F,
            0.8F,
            1.2F
    };

    private static final float[] ITEM_ORBIT_PHASE = {
            0.0F,
            120.0F,
            240.0F
    };

    private static final float[] ITEM_SPIN_SPEED = {
            0.08F,
            0.06F,
            0.10F
    };

    private static final float[] ITEM_SPIN_PHASE = {
            0.0F,
            140.0F,
            260.0F
    };

    private static final float[] ITEM_BOB_AMOUNT = {
            0.030F,
            0.040F,
            0.035F
    };

    private static final float[] ITEM_BOB_SPEED = {
            0.055F,
            0.045F,
            0.065F
    };

    private static final float[] ITEM_BOB_PHASE = {
            0.0F,
            2.0F,
            4.0F
    };

    private static final float[] ITEM_SWAY_AMOUNT = {
            0.012F,
            0.018F,
            0.015F
    };

    private static final float[] ITEM_SWAY_SPEED = {
            0.035F,
            0.028F,
            0.042F
    };

    private static final float[] ITEM_SWAY_PHASE = {
            0.0F,
            1.7F,
            3.4F
    };

    private static final float FLUID_X_MIN = 0.10F;
    private static final float FLUID_X_MAX = 0.90F;
    private static final float FLUID_Z_MIN = 0.10F;
    private static final float FLUID_Z_MAX = 0.90F;

    private static final float FLUID_BASE_Y = 0.50F;
    private static final float FLUID_HEIGHT = 0.50F;

    private static final float FLUID_WAVE_AMOUNT = 0.0025F;

    private static final float FLUID_WAVE_SPEED = 0.008F;

    private static final float FLUID_WAVE_FREQUENCY = 4.0F;

    private static final int MAX_ITEMS = 3;

    public EssenceBoilerBlockEntityRenderer(
            BlockEntityRendererProvider.Context context
    ) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
            EssenceBoilerBlockEntity blockEntity,
            RenderState state,
            float partialTicks,
            net.minecraft.world.phys.Vec3 cameraPosition,
            @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(
                blockEntity,
                state,
                partialTicks,
                cameraPosition,
                breakProgress
        );

        Level level = blockEntity.getLevel();

        if (level == null) {
            state.itemCount = 0;
            state.fluidVisible = false;
            state.fluidSprite = null;
            return;
        }

        state.partialTick = partialTicks;
        state.gameTime = level.getGameTime();
        state.craftingProgress = blockEntity.maxProgress <= 0
                ? 0.0F
                : Mth.clamp(
                        (blockEntity.progress + partialTicks) / blockEntity.maxProgress,
                        0.0F,
                        1.0F
                );
        state.crafting = blockEntity.progress > 0;
        state.craftingIngredientMask = blockEntity.getCraftingIngredientMask();
        double animationTime = state.gameTime + state.partialTick;
        if (state.animationClockInitialized) {
            double elapsed = Math.max(0.0D, animationTime - state.lastAnimationTime);
            float craftIntensity = smoothProgress(state.craftingProgress);
            state.animationClock += elapsed;
            state.rotationClock += elapsed * Mth.lerp(craftIntensity, 1.0F, 1.15F);
        } else {
            state.animationClock = animationTime;
            state.rotationClock = animationTime;
            state.animationClockInitialized = true;
        }
        state.lastAnimationTime = animationTime;

        state.itemCount = Math.min(
                blockEntity.inventory.getSlots(),
                MAX_ITEMS
        );

        for (int i = 0; i < MAX_ITEMS; i++) {
            state.items[i] = new ItemStackRenderState();

            if (i >= state.itemCount) {
                continue;
            }

            ItemStack stack =
                    blockEntity.inventory.getStackInSlot(i);

            if (!stack.isEmpty()) {
                itemModelResolver.updateForTopItem(
                        state.items[i],
                        stack,
                        ItemDisplayContext.GROUND,
                        level,
                        null,
                        i
                );
            }
        }

        EssenceBoilerFluid fluid =
                blockEntity.getFluidTank();

        EssenceBoilerFluid craftingOutput =
                blockEntity.getCraftingOutputFluid();
        float fluidTransition = state.crafting && !craftingOutput.isEmpty()
                ? state.craftingProgress
                : 0.0F;

        state.fluidVisible = !fluid.isEmpty()
                || fluidTransition > 0.0F;
        state.fluidSprite = null;
        state.fluidColor = 0xFFFFFFFF;

        state.fluidAmountNormalized =
                !fluid.isEmpty()
                        ? Mth.clamp(
                        (float) fluid.amount()
                                / (float) blockEntity.getFluidTankCapacity(),
                        0.0F,
                        1.0F
                )
                        : 0.0F;

        state.transitionFluidSprite = null;
        state.transitionFluidProgress = 0.0F;

        if (!fluid.isEmpty()) {
            FluidModel fluidModel =
                    Minecraft.getInstance()
                            .getModelManager()
                            .getFluidStateModelSet()
                            .get(
                                    fluid.fluid()
                                            .defaultFluidState()
                            );

            Material.Baked stillMaterial =
                    fluidModel.stillMaterial();

            if (stillMaterial != null) {
                state.fluidSprite =
                        stillMaterial.sprite();
            } else {
                state.fluidSprite = null;
                state.fluidVisible = false;
                return;
            }

            state.fluidColor =
                    EssenceBoilerFluidColor.getTint(
                            fluidModel,
                            fluid,
                            level,
                            blockEntity.getBlockPos()
                    );
        }

        if (fluidTransition > 0.0F) {
            FluidModel outputModel =
                    Minecraft.getInstance()
                            .getModelManager()
                            .getFluidStateModelSet()
                            .get(craftingOutput.fluid().defaultFluidState());
            Material.Baked outputMaterial = outputModel.stillMaterial();
            if (outputMaterial == null) {
                state.transitionFluidSprite = null;
                state.fluidVisible = !fluid.isEmpty();
                return;
            }
            TextureAtlasSprite outputSprite = outputMaterial.sprite();
            int outputColor = EssenceBoilerFluidColor.getTint(
                    outputModel,
                    craftingOutput,
                    level,
                    blockEntity.getBlockPos()
            );
            state.fluidAmountNormalized = Mth.lerp(
                    fluidTransition,
                    state.fluidAmountNormalized,
                    Mth.clamp(
                            (float) craftingOutput.amount() / blockEntity.getFluidTankCapacity(),
                            0.0F,
                            1.0F
                    )
            );

            if (!fluid.isEmpty() && fluid.fluid() == craftingOutput.fluid()) {
                state.fluidColor = interpolateColor(
                        state.fluidColor,
                        outputColor,
                        fluidTransition
                );
            } else {
                state.transitionFluidSprite = outputSprite;
                state.transitionFluidColor = outputColor;
                state.transitionFluidProgress = fluidTransition;
                if (fluid.isEmpty()) {
                    state.fluidSprite = outputSprite;
                    state.fluidColor = withAlpha(outputColor, fluidTransition);
                    state.transitionFluidSprite = null;
                    state.transitionFluidProgress = 0.0F;
                } else {
                    state.fluidColor = withAlpha(state.fluidColor, 1.0F - fluidTransition);
                    state.transitionFluidColor = withAlpha(outputColor, fluidTransition);
                }
            }
        }

        EssenceBoilerBlockEntity.WobbleStyle wobbleStyle =
                blockEntity.lastWobbleStyle;

        if (wobbleStyle == null) {
            state.wobbleProgress = -1.0F;
            state.wobbleStyle = -1;
        } else {
            state.wobbleProgress =
                    (
                            (float) (
                                    level.getGameTime()
                                            - blockEntity.wobbleStartedAtTick
                            )
                                    + partialTicks
                    )
                            / wobbleStyle.duration;

            state.wobbleStyle =
                    wobbleStyle.ordinal();
        }
    }

    @Override
    public void submit(
            RenderState state,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            CameraRenderState camera
    ) {
        poseStack.pushPose();

        applyWobble(
                state,
                poseStack
        );

        submitItems(
                state,
                poseStack,
                collector
        );

        submitProgressBar(
                state,
                poseStack,
                collector
        );

        submitFluid(
                state,
                poseStack,
                collector
        );

        poseStack.popPose();
    }

    private void submitItems(
            RenderState state,
            PoseStack poseStack,
            SubmitNodeCollector collector
    ) {
        if (state.itemCount <= 0) {
            return;
        }

        poseStack.pushPose();

        float fluidSurfaceY =
                FLUID_BASE_Y
                        + state.fluidAmountNormalized
                        * FLUID_HEIGHT;

        float itemCenterY =
                Math.max(
                        ITEM_MIN_Y,
                        fluidSurfaceY - 0.12F
                );

        poseStack.translate(
                0.5F,
                itemCenterY,
                0.5F
        );

        poseStack.scale(
                ITEM_SCALE,
                ITEM_SCALE,
                ITEM_SCALE
        );

        float time = (float) state.animationClock;
        float rotationTime = (float) state.rotationClock;
        float craftIntensity = smoothProgress(state.craftingProgress);
        for (int i = 0; i < state.itemCount; i++) {
            ItemStackRenderState itemState =
                    state.items[i];

            if (itemState == null) {
                continue;
            }

            float shrinkProgress = (state.craftingIngredientMask & (1 << i)) != 0
                    ? Mth.clamp((state.craftingProgress - 0.78F) / 0.22F, 0.0F, 1.0F)
                    : 0.0F;
            shrinkProgress = shrinkProgress * shrinkProgress
                    * (3.0F - 2.0F * shrinkProgress);
            float itemScale = 1.0F - shrinkProgress * 0.985F;

            poseStack.pushPose();

            float orbitAngle =
                    (
                            rotationTime
                                    * ITEM_ORBIT_SPEED[i]
                                    + ITEM_ORBIT_PHASE[i]
                    )
                            * Mth.DEG_TO_RAD;

            float radius =
                    ITEM_BASE_RADIUS[i]
                            + Mth.sin(
                            time
                                    * ITEM_DRIFT_SPEED[i]
                                    + ITEM_ORBIT_PHASE[i]
                    )
                            * ITEM_RADIUS_DRIFT[i];

            float baseX =
                    radius
                            * Mth.cos(orbitAngle);

            float baseZ =
                    radius
                            * Mth.sin(orbitAngle);

            float swayTime =
                    time
                            * ITEM_SWAY_SPEED[i]
                            + ITEM_SWAY_PHASE[i];

            float swayX =
                    Mth.sin(swayTime)
                            * ITEM_SWAY_AMOUNT[i]
                            * (1.0F + craftIntensity * 1.5F);

            float swayZ =
                    Mth.cos(
                            swayTime * 0.83F
                    )
                            * ITEM_SWAY_AMOUNT[i]
                            * (1.0F + craftIntensity * 1.5F);

            float bobTime =
                    time
                            * ITEM_BOB_SPEED[i]
                            + ITEM_BOB_PHASE[i];

            float bobbingY =
                    Mth.sin(bobTime)
                            * ITEM_BOB_AMOUNT[i]
                            * (1.0F + craftIntensity);

            float itemY =
                    Math.max(
                            ITEM_MIN_Y - itemCenterY,
                            bobbingY
                    );

            poseStack.translate(
                    baseX + swayX,
                    itemY,
                    baseZ + swayZ
            );

            float spinDegrees =
                    rotationTime
                            * ITEM_SPIN_SPEED[i]
                            + ITEM_SPIN_PHASE[i];

            poseStack.scale(itemScale, itemScale, itemScale);
            float wiggleTime =
                    time * 0.20F
                            + ITEM_SPIN_PHASE[i] * Mth.DEG_TO_RAD;
            float wiggleAmount = craftIntensity * 0.16F;
            poseStack.mulPose(Axis.XP.rotation(
                    Mth.sin(wiggleTime) * wiggleAmount
            ));
            poseStack.mulPose(Axis.ZP.rotation(
                    Mth.cos(wiggleTime * 0.83F) * wiggleAmount
            ));
            poseStack.mulPose(
                    Axis.YP.rotationDegrees(
                            spinDegrees
                    )
            );

            poseStack.mulPose(
                    Axis.YP.rotationDegrees(
                            90.0F
                    )
            );

            itemState.submit(
                    poseStack,
                    collector,
                    getLightLevel(state),
                    0,
                    0
            );

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private void submitProgressBar(
            RenderState state,
            PoseStack poseStack,
            SubmitNodeCollector collector
    ) {
        if (!state.crafting) {
            return;
        }

        float left = 0.18F;
        float right = 0.82F;
        float bottom = 0.91F;
        float top = 0.95F;
        float depth = 0.055F;
        int background = 0xD0201728;
        int fill = 0xF0A6DDD8;
        float filledRight = Mth.lerp(state.craftingProgress, left, right);

        collector.submitCustomGeometry(
                poseStack,
                RenderTypes.translucentMovingBlock(),
                (pose, builder) -> {
                    drawProgressQuad(builder, pose, left, bottom, right, top, depth, background, state.lightCoords);
                    if (filledRight > left) {
                        drawProgressQuad(builder, pose, left, bottom, filledRight, top, depth - 0.001F, fill, state.lightCoords);
                    }
                }
        );
    }

    private static void drawProgressQuad(
            VertexConsumer builder,
            PoseStack.Pose pose,
            float left,
            float bottom,
            float right,
            float top,
            float depth,
            int color,
            int light
    ) {
        builder.addVertex(pose, left, bottom, depth)
                .setColor(color).setUv(0.0F, 1.0F).setOverlay(0).setLight(light).setNormal(pose, 0.0F, 0.0F, -1.0F);
        builder.addVertex(pose, left, top, depth)
                .setColor(color).setUv(0.0F, 0.0F).setOverlay(0).setLight(light).setNormal(pose, 0.0F, 0.0F, -1.0F);
        builder.addVertex(pose, right, top, depth)
                .setColor(color).setUv(1.0F, 0.0F).setOverlay(0).setLight(light).setNormal(pose, 0.0F, 0.0F, -1.0F);
        builder.addVertex(pose, right, bottom, depth)
                .setColor(color).setUv(1.0F, 1.0F).setOverlay(0).setLight(light).setNormal(pose, 0.0F, 0.0F, -1.0F);
    }

    private void submitFluid(
            RenderState state,
            PoseStack poseStack,
            SubmitNodeCollector collector
    ) {
        if (!state.fluidVisible
                || (state.fluidSprite == null && state.transitionFluidSprite == null)) {
            return;
        }

        float baseY =
                FLUID_BASE_Y
                        + state.fluidAmountNormalized
                        * FLUID_HEIGHT;

        float waveTime =
                (state.gameTime + state.partialTick)
                        * FLUID_WAVE_SPEED
                        * (1.0F + state.craftingProgress * 0.5F);
        float waveAmplitude = FLUID_WAVE_AMOUNT
                * (1.0F + state.craftingProgress * 1.0F);

        if (state.fluidSprite != null) {
            submitFluidLayer(
                        state,
                        poseStack,
                        collector,
                        state.fluidSprite,
                        state.fluidColor,
                        baseY,
                        waveTime,
                        waveAmplitude,
                        state.craftingProgress
            );
        }
        if (state.transitionFluidSprite != null) {
            submitFluidLayer(
                            state,
                            poseStack,
                            collector,
                            state.transitionFluidSprite,
                            state.transitionFluidColor,
                            baseY + 0.001F,
                            waveTime,
                            waveAmplitude,
                            state.craftingProgress
            );
        }
    }

    private void submitFluidLayer(
            RenderState state,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            TextureAtlasSprite sprite,
            int color,
            float baseY,
            float waveTime,
            float waveAmplitude,
            float craftingProgress
    ) {
        if (sprite == null) {
            return;
        }

        float y00 =
                        baseY
                        + calculateFluidWave(
                        FLUID_X_MIN,
                        FLUID_Z_MIN,
                        waveTime,
                        waveAmplitude,
                        craftingProgress
                );

        float y01 =
                baseY
                        + calculateFluidWave(
                        FLUID_X_MIN,
                        FLUID_Z_MAX,
                        waveTime,
                        waveAmplitude,
                        craftingProgress
                );

        float y11 =
                baseY
                        + calculateFluidWave(
                        FLUID_X_MAX,
                        FLUID_Z_MAX,
                        waveTime,
                        waveAmplitude,
                        craftingProgress
                );

        float y10 =
                baseY
                        + calculateFluidWave(
                        FLUID_X_MAX,
                        FLUID_Z_MIN,
                        waveTime,
                        waveAmplitude,
                        craftingProgress
                );

        RenderType renderType =
                RenderTypes.translucentMovingBlock();

        collector.submitCustomGeometry(
                poseStack,
                renderType,
                (pose, builder) -> drawFluidQuad(
                        builder,
                        pose,

                        FLUID_X_MIN,
                        y00,
                        FLUID_Z_MIN,

                        FLUID_X_MIN,
                        y01,
                        FLUID_Z_MAX,

                        FLUID_X_MAX,
                        y11,
                        FLUID_Z_MAX,

                        FLUID_X_MAX,
                        y10,
                        FLUID_Z_MIN,

                        sprite.getU0(),
                        sprite.getV0(),
                        sprite.getU1(),
                        sprite.getV1(),

                        getLightLevel(state),
                        color
                )
        );
    }

    private static int withAlpha(int color, float alpha) {
        int scaledAlpha = Mth.clamp(Math.round(((color >>> 24) & 0xFF) * alpha), 0, 255);
        return (color & 0x00FFFFFF) | (scaledAlpha << 24);
    }

    private static int interpolateColor(int from, int to, float amount) {
        int alpha = Mth.lerpInt(amount, (from >>> 24) & 0xFF, (to >>> 24) & 0xFF);
        int red = Mth.lerpInt(amount, (from >> 16) & 0xFF, (to >> 16) & 0xFF);
        int green = Mth.lerpInt(amount, (from >> 8) & 0xFF, (to >> 8) & 0xFF);
        int blue = Mth.lerpInt(amount, from & 0xFF, to & 0xFF);
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private static float calculateFluidWave(
            float x,
            float z,
            float time,
            float amplitude,
            float craftingProgress
    ) {
        float craftingWiggle = smoothProgress(craftingProgress);
        float wave1 =
                Mth.sin(
                        time
                                + x * FLUID_WAVE_FREQUENCY
                                + z * 1.5F
                );

        float wave2 =
                Mth.cos(
                        time * 0.72F
                                + z * FLUID_WAVE_FREQUENCY
                                - x
                );
        float craftingSlosh = Mth.sin(time * 0.55F + x * 2.0F - z * 1.7F)
                * amplitude
                * craftingWiggle
                * 1.0F;

        return (
                wave1 * 0.65F
                        + wave2 * 0.35F
        )
                * amplitude
                + craftingSlosh;
    }

    private static float smoothProgress(float progress) {
        float clampedProgress = Mth.clamp(progress, 0.0F, 1.0F);
        return clampedProgress * clampedProgress
                * (3.0F - 2.0F * clampedProgress);
    }

    private static void drawFluidQuad(
            VertexConsumer builder,
            PoseStack.Pose pose,

            float x00,
            float y00,
            float z00,

            float x01,
            float y01,
            float z01,

            float x11,
            float y11,
            float z11,

            float x10,
            float y10,
            float z10,

            float u0,
            float v0,
            float u1,
            float v1,

            int light,
            int color
    ) {
        builder.addVertex(
                        pose,
                        x00,
                        y00,
                        z00
                )
                .setColor(color)
                .setUv(u0, v0)
                .setOverlay(0)
                .setLight(light)
                .setNormal(
                        pose,
                        0.0F,
                        1.0F,
                        0.0F
                );

        builder.addVertex(
                        pose,
                        x01,
                        y01,
                        z01
                )
                .setColor(color)
                .setUv(u0, v1)
                .setOverlay(0)
                .setLight(light)
                .setNormal(
                        pose,
                        0.0F,
                        1.0F,
                        0.0F
                );

        builder.addVertex(
                        pose,
                        x11,
                        y11,
                        z11
                )
                .setColor(color)
                .setUv(u1, v1)
                .setOverlay(0)
                .setLight(light)
                .setNormal(
                        pose,
                        0.0F,
                        1.0F,
                        0.0F
                );

        builder.addVertex(
                        pose,
                        x10,
                        y10,
                        z10
                )
                .setColor(color)
                .setUv(u1, v0)
                .setOverlay(0)
                .setLight(light)
                .setNormal(
                        pose,
                        0.0F,
                        1.0F,
                        0.0F
                );
    }

    private static void applyWobble(
            RenderState state,
            PoseStack poseStack
    ) {
        float progress =
                state.wobbleProgress;

        if (progress < 0.0F
                || progress > 1.0F) {
            return;
        }

        if (state.wobbleStyle
                == EssenceBoilerBlockEntity.WobbleStyle.POSITIVE.ordinal()) {

            float angle =
                    -1.5F
                            * (
                            Mth.cos(
                                    progress * Mth.TWO_PI
                            )
                                    + 0.5F
                    )
                            * Mth.sin(
                            progress * Mth.PI
                    );

            poseStack.rotateAround(
                    Axis.XP.rotation(
                            angle * 0.015625F
                    ),
                    0.5F,
                    0.0F,
                    0.5F
            );

            poseStack.rotateAround(
                    Axis.ZP.rotation(
                            Mth.sin(
                                    progress * Mth.TWO_PI
                            )
                                    * 0.015625F
                    ),
                    0.5F,
                    0.0F,
                    0.5F
            );

        } else {

            float rotY =
                    Mth.sin(
                            -progress
                                    * 3.0F
                                    * Mth.PI
                    )
                            * 0.125F;

            poseStack.rotateAround(
                    Axis.YP.rotation(
                            rotY
                                    * (1.0F - progress)
                    ),
                    0.5F,
                    0.0F,
                    0.5F
            );
        }
    }

    private int getLightLevel(
            RenderState state
    ) {
        return state.lightCoords;
    }

    public static final class RenderState
            extends BlockEntityRenderState {

        public final ItemStackRenderState[] items = {
                new ItemStackRenderState(),
                new ItemStackRenderState(),
                new ItemStackRenderState()
        };

        public int itemCount;

        public long gameTime;

        public float partialTick;

        public double animationClock;

        public double rotationClock;

        public double lastAnimationTime;

        public boolean animationClockInitialized;

        public boolean crafting;

        public float craftingProgress;

        public int craftingIngredientMask;

        public boolean fluidVisible;

        public float fluidAmountNormalized;

        public int fluidColor;

        public float transitionFluidProgress;

        public int transitionFluidColor;

        @Nullable
        public TextureAtlasSprite transitionFluidSprite;

        @Nullable
        public TextureAtlasSprite fluidSprite;

        public float wobbleProgress = -1.0F;

        public int wobbleStyle = -1;
    }

}