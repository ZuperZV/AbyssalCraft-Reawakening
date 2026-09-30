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
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.EssenceBoilerBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import org.jetbrains.annotations.Nullable;

public class EssenceBoilerBlockEntityRenderer
        implements BlockEntityRenderer<
        EssenceBoilerBlockEntity,
        EssenceBoilerBlockEntityRenderer.RenderState> {

    private final ItemModelResolver itemModelResolver;

    private static final float ITEM_SCALE = 0.37F;
    private static final float ITEM_CENTER_Y = 0.70F;
    private static final float ITEM_RADIUS = 0.55F;
    private static final float ITEM_SPIN_SPEED = 4.0F;
    private static final float ITEM_SPIN_MULTIPLIER = 1.5F;
    private static final float BOB_AMOUNT = 0.05F;
    private static final float JITTER_AMOUNT = 0.05F;
    private static final float JITTER_SPEED = 0.70F;

    private static final float FLUID_X_MIN = 0.10F;
    private static final float FLUID_X_MAX = 0.90F;
    private static final float FLUID_Z_MIN = 0.10F;
    private static final float FLUID_Z_MAX = 0.90F;
    private static final float FLUID_BASE_Y = 0.50F;
    private static final float FLUID_HEIGHT = 0.50F;

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
        state.rotation = (state.gameTime + partialTicks) * ITEM_SPIN_SPEED;

        state.itemCount = Math.min(
                blockEntity.inventory.getSlots(),
                3
        );

        for (int i = 0; i < 3; i++) {
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

        state.fluidAmountNormalized = state.fluidVisible
                ? Mth.clamp(
                (float) fluid.amount()
                        / (float) blockEntity.getFluidTankCapacity(),
                0.0F,
                1.0F
        )
                : 0.0F;

        if (state.fluidVisible) {
            FluidModel fluidModel = Minecraft.getInstance()
                    .getModelManager()
                    .getFluidStateModelSet()
                    .get(fluid.fluid().defaultFluidState());

            Material.Baked stillMaterial = fluidModel.stillMaterial();

            if (stillMaterial != null) {
                state.fluidSprite = stillMaterial.sprite();
            } else {
                state.fluidSprite = null;
                state.fluidVisible = false;
                return;
            }

            state.fluidColor = getFluidTint(
                    fluidModel,
                    fluid,
                    level,
                    blockEntity
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
                    ) / wobbleStyle.duration;

            state.wobbleStyle = wobbleStyle.ordinal();
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
                        + state.fluidAmountNormalized * FLUID_HEIGHT;

        float itemCenterY =
                Math.max(
                        FLUID_BASE_Y + 0.04F,
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

        for (int i = 0; i < state.itemCount; i++) {
            ItemStackRenderState itemState =
                    state.items[i];

            if (itemState == null) {
                continue;
            }

            poseStack.pushPose();

            float angle =
                    (
                            state.rotation
                                    + (360.0F / state.itemCount) * i
                    ) * Mth.DEG_TO_RAD;

            float baseX =
                    ITEM_RADIUS * Mth.cos(angle);

            float baseZ =
                    ITEM_RADIUS * Mth.sin(angle);

            float slotSeed =
                    i * 31.7F;

            float time =
                    (
                            state.gameTime
                                    + state.partialTick
                                    + slotSeed
                    ) * 0.1F;

            float bobbingY =
                    Mth.sin(time) * BOB_AMOUNT;

            float jitterX =
                    Mth.sin(time * JITTER_SPEED)
                            * JITTER_AMOUNT;

            float jitterZ =
                    Mth.cos(time * JITTER_SPEED)
                            * JITTER_AMOUNT;

            poseStack.translate(
                    baseX + jitterX,
                    bobbingY,
                    baseZ + jitterZ
            );

            poseStack.mulPose(
                    Axis.YP.rotationDegrees(
                            state.rotation
                                    * ITEM_SPIN_MULTIPLIER
                    )
            );

            poseStack.mulPose(
                    Axis.YP.rotationDegrees(90.0F)
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

        float y =
                FLUID_BASE_Y
                        + state.fluidAmountNormalized
                        * FLUID_HEIGHT;

        RenderType renderType =
                RenderTypes.translucentMovingBlock();

        collector.submitCustomGeometry(
                poseStack,
                renderType,
                (pose, builder) -> drawQuad(
                        builder,
                        pose,
                        FLUID_X_MIN,
                        y,
                        FLUID_Z_MIN,
                        FLUID_X_MAX,
                        y,
                        FLUID_Z_MAX,
                        sprite.getU0(),
                        sprite.getV0(),
                        sprite.getU1(),
                        sprite.getV1(),
                        getLightLevel(state),
                        color
                )
        );
    }

    private void applyWobble(
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
                            ) + 0.5F
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
                            ) * 0.015625F
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
                    ) * 0.125F;

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

    private static void drawQuad(
            VertexConsumer builder,
            PoseStack.Pose pose,
            float x0,
            float y0,
            float z0,
            float x1,
            float y1,
            float z1,
            float u0,
            float v0,
            float u1,
            float v1,
            int light,
            int color
    ) {
        builder.addVertex(
                        pose,
                        x0,
                        y0,
                        z0
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
                        x0,
                        y1,
                        z1
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
                        x1,
                        y1,
                        z1
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
                        x1,
                        y0,
                        z0
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

        public float rotation;

        public boolean fluidVisible;

        public float fluidAmountNormalized;

        public int fluidColor;

        @Nullable
        public TextureAtlasSprite fluidSprite;

        public float wobbleProgress = -1.0F;

        public int wobbleStyle = -1;
    }

    private static final int NO_TINT = 0xFFFFFFFF;
    private static final float POTION_TINT_BLEND = 0.5F;

    private static int getFluidTint(
            FluidModel fluidModel,
            EssenceBoilerFluid fluid,
            Level level,
            EssenceBoilerBlockEntity blockEntity
    ) {
        int fluidTint = NO_TINT;
        boolean hasFluidTint = false;

        if (fluidModel.tintSource() != null) {
            fluidTint = fluidModel.tintSource().colorInWorld(
                    fluid.fluid()
                            .defaultFluidState()
                            .createLegacyBlock(),
                    (BlockAndTintGetter) level,
                    blockEntity.getBlockPos()
            );

            hasFluidTint = fluidTint != NO_TINT;
        }

        PotionContents potionContents =
                fluid.potionContents();

        boolean hasPotionTint =
                potionContents != null
                        && potionContents != PotionContents.EMPTY;

        if (!hasPotionTint) {
            return hasFluidTint
                    ? fluidTint
                    : NO_TINT;
        }

        int potionTint = potionContents.getColor();

        if (!hasFluidTint) {
            return potionTint;
        }

        return interpolateColor(
                fluidTint,
                potionTint,
                POTION_TINT_BLEND
        );
    }

    private static int interpolateColor(
            int colorA,
            int colorB,
            float amount
    ) {
        amount = Mth.clamp(amount, 0.0F, 1.0F);

        int aR = (colorA >> 16) & 0xFF;
        int aG = (colorA >> 8) & 0xFF;
        int aB = colorA & 0xFF;

        int bR = (colorB >> 16) & 0xFF;
        int bG = (colorB >> 8) & 0xFF;
        int bB = colorB & 0xFF;

        int r = Mth.lerpInt(amount, aR, bR);
        int g = Mth.lerpInt(amount, aG, bG);
        int b = Mth.lerpInt(amount, aB, bB);

        return 0xFF000000
                | (r << 16)
                | (g << 8)
                | b;
    }
}