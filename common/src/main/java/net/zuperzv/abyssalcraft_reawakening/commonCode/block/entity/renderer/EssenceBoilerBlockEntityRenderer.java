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
    private static final float ITEM_SCALE = 0.37F;
    private static final float ITEM_MIN_Y = 0.56F;

    private static final float[] ITEM_BASE_RADIUS = {
            0.46F,
            0.34F,
            0.19F
    };

    private static final float[] ITEM_RADIUS_DRIFT = {
            0.095F,
            0.085F,
            0.070F
    };

    private static final float[] ITEM_DRIFT_SPEED = {
            0.020F,
            0.0175F,
            0.023F
    };

    private static final float[] ITEM_ORBIT_SPEED = {
            1.10F,
            0.85F,
            1.35F
    };

    private static final float[] ITEM_ORBIT_PHASE = {
            0.0F,
            120.0F,
            240.0F
    };

    private static final float[] ITEM_SPIN_SPEED = {
            2.0F,
            2.7F,
            1.6F
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

    private static final float FLUID_WAVE_AMOUNT = 0.006F;

    private static final float FLUID_WAVE_SPEED = 0.025F;

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

        state.fluidVisible = !fluid.isEmpty();

        state.fluidAmountNormalized =
                state.fluidVisible
                        ? Mth.clamp(
                        (float) fluid.amount()
                                / (float) blockEntity.getFluidTankCapacity(),
                        0.0F,
                        1.0F
                )
                        : 0.0F;

        if (state.fluidVisible) {
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

        float time =
                state.gameTime
                        + state.partialTick;

        for (int i = 0; i < state.itemCount; i++) {
            ItemStackRenderState itemState =
                    state.items[i];

            if (itemState == null) {
                continue;
            }

            poseStack.pushPose();

            float orbitAngle =
                    (
                            time
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
                            * ITEM_SWAY_AMOUNT[i];

            float swayZ =
                    Mth.cos(
                            swayTime * 0.83F
                    )
                            * ITEM_SWAY_AMOUNT[i];

            float bobTime =
                    time
                            * ITEM_BOB_SPEED[i]
                            + ITEM_BOB_PHASE[i];

            float bobbingY =
                    Mth.sin(bobTime)
                            * ITEM_BOB_AMOUNT[i];

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
                    time
                            * ITEM_SPIN_SPEED[i]
                            + ITEM_SPIN_PHASE[i];

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

    private void submitFluid(
            RenderState state,
            PoseStack poseStack,
            SubmitNodeCollector collector
    ) {
        if (!state.fluidVisible
                || state.fluidSprite == null) {
            return;
        }

        TextureAtlasSprite sprite =
                state.fluidSprite;

        int color =
                state.fluidColor;

        float baseY =
                FLUID_BASE_Y
                        + state.fluidAmountNormalized
                        * FLUID_HEIGHT;

        float waveTime =
                (state.gameTime + state.partialTick)
                        * FLUID_WAVE_SPEED;

        float y00 =
                baseY
                        + calculateFluidWave(
                        FLUID_X_MIN,
                        FLUID_Z_MIN,
                        waveTime
                );

        float y01 =
                baseY
                        + calculateFluidWave(
                        FLUID_X_MIN,
                        FLUID_Z_MAX,
                        waveTime
                );

        float y11 =
                baseY
                        + calculateFluidWave(
                        FLUID_X_MAX,
                        FLUID_Z_MAX,
                        waveTime
                );

        float y10 =
                baseY
                        + calculateFluidWave(
                        FLUID_X_MAX,
                        FLUID_Z_MIN,
                        waveTime
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

    private static float calculateFluidWave(
            float x,
            float z,
            float time
    ) {
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

        return (
                wave1 * 0.65F
                        + wave2 * 0.35F
        )
                * FLUID_WAVE_AMOUNT;
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

        public boolean fluidVisible;

        public float fluidAmountNormalized;

        public int fluidColor;

        @Nullable
        public TextureAtlasSprite fluidSprite;

        public float wobbleProgress = -1.0F;

        public int wobbleStyle = -1;
    }

}