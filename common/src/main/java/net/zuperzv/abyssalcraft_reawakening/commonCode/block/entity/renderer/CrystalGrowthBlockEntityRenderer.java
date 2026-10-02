package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.material.Fluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.CrystalGrowthBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluidColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CrystalGrowthBlockEntityRenderer implements
        BlockEntityRenderer<CrystalGrowthBlockEntity, CrystalGrowthBlockEntityRenderer.GrowthState> {
    private static final float FLUID_X_MIN = 0.12F;
    private static final float FLUID_X_MAX = 0.88F;
    private static final float FLUID_Z_MIN = 0.12F;
    private static final float FLUID_Z_MAX = 0.88F;
    private final ItemModelResolver itemModelResolver;

    public CrystalGrowthBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    public static class GrowthState extends BlockEntityRenderState {
        public Level level;
        public float progress;
        public float rotation;
        public boolean hasSeed;
        public boolean fullyGrown;
        public boolean crystal;
        public int crystalTint;
        public float fluidAmount;
        public int fluidColor = 0xFFFFFFFF;
        @Nullable
        public TextureAtlasSprite fluidSprite;
        @Nullable
        public BlockStateModel crystalModel;
        public final ItemStackRenderState seed = new ItemStackRenderState();
        public final ItemStackRenderState catalyst = new ItemStackRenderState();
    }

    @Override
    public GrowthState createRenderState() {
        return new GrowthState();
    }

    @Override
    public void extractRenderState(CrystalGrowthBlockEntity blockEntity, GrowthState state,
                                   float partialTicks, Vec3 cameraPosition,
                                   @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        Level level = blockEntity.getLevel();
        state.level = level;
        state.progress = blockEntity.getGrowthProgress();
        state.rotation = level == null ? 0 : (level.getGameTime() + partialTicks) * 1.5F;
        state.fullyGrown = blockEntity.progress == 0
                && !blockEntity.inventory.getStackInSlot(CrystalGrowthBlockEntity.SLOT_OUTPUT).isEmpty();
        state.hasSeed = false;
        state.crystal = false;
        state.crystalModel = null;
        state.fluidSprite = null;
        state.fluidAmount = level == null ? 0.0F
                : Mth.clamp((float) blockEntity.getFluidTankAmount()
                / blockEntity.getFluidTankCapacity(), 0.0F, 1.0F);
        if (level == null) {
            return;
        }

        EssenceBoilerFluid fluid = blockEntity.getFluidTank();
        if (!fluid.isEmpty()) {
            FluidModel model = Minecraft.getInstance().getModelManager()
                    .getFluidStateModelSet().get(fluid.fluid().defaultFluidState());
            Material.Baked still = model.stillMaterial();
            if (still != null) {
                state.fluidSprite = still.sprite();
                state.fluidColor = EssenceBoilerFluidColor.getTint(
                        model, fluid, level, blockEntity.getBlockPos()
                );
            }
        }

        ItemStack stack = blockEntity.getGrowingResult();
        ItemStack catalyst = blockEntity.inventory.getStackInSlot(CrystalGrowthBlockEntity.SLOT_CATALYST);
        itemModelResolver.updateForTopItem(
                state.catalyst, catalyst, ItemDisplayContext.GROUND, level, null, 1
        );
        if (stack.isEmpty()) {
            return;
        }
        state.hasSeed = true;
        String itemName = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        state.crystal = itemName.endsWith("_crystal");
        if (state.crystal) {
            state.crystalTint = Mth.hsvToRgb((itemName.hashCode() & 0xFFFF) / 65535.0F, 0.68F, 1.0F);
            state.crystalModel = Minecraft.getInstance().getModelManager()
                    .getBlockStateModelSet().get(Blocks.AMETHYST_CLUSTER.defaultBlockState());
        } else {
            itemModelResolver.updateForTopItem(
                    state.seed, stack, ItemDisplayContext.FIXED, level, null, 0
            );
        }
    }

    @Override
    public void submit(GrowthState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.level == null) {
            return;
        }
        submitFluid(state, poseStack, collector);
        if (!state.catalyst.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.28F, 0.34F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-state.rotation));
            poseStack.scale(0.2F, 0.2F, 0.2F);
            state.catalyst.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
        if (!state.hasSeed) {
            return;
        }

        poseStack.pushPose();
        float growth = state.fullyGrown ? 1.0F : state.progress;
        float scale = 0.16F + growth * 0.38F;
        poseStack.translate(0.5F, 0.45F + growth * 0.18F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));
        poseStack.scale(scale, scale, scale);
        if (state.crystal && state.crystalModel != null) {
            submitCrystalModel(state, poseStack, collector);
        } else {
            state.seed.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }
        poseStack.popPose();
    }

    private void submitCrystalModel(GrowthState state, PoseStack poseStack, SubmitNodeCollector collector) {
        List<BlockStateModelPart> parts = new ArrayList<>();
        state.crystalModel.collectParts(RandomSource.create(0L), parts);
        collector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(), (pose, builder) -> {
            QuadInstance quad = new QuadInstance();
            quad.setColor(0xFF000000 | state.crystalTint);
            quad.setLightCoords(state.lightCoords);
            quad.setOverlayCoords(OverlayTexture.NO_OVERLAY);
            for (BlockStateModelPart part : parts) {
                for (var direction : net.minecraft.core.Direction.values()) {
                    for (var bakedQuad : part.getQuads(direction)) {
                        builder.putBakedQuad(pose, bakedQuad, quad);
                    }
                }
                for (var bakedQuad : part.getQuads(null)) {
                    builder.putBakedQuad(pose, bakedQuad, quad);
                }
            }
        });
    }

    private void submitFluid(GrowthState state, PoseStack poseStack, SubmitNodeCollector collector) {
        TextureAtlasSprite sprite = state.fluidSprite;
        if (sprite == null || state.fluidAmount <= 0.0F) {
            return;
        }
        float y = 0.16F + state.fluidAmount * 0.30F;
        float waveTime = (state.level.getGameTime() % 24000L) * 0.025F;
        collector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(),
                (pose, builder) -> drawFluid(builder, pose, sprite, state.fluidColor, y, waveTime,
                        state.lightCoords));
    }

    private static void drawFluid(VertexConsumer builder, PoseStack.Pose pose, TextureAtlasSprite sprite,
                                  int color, float y, float time, int light) {
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();
        float x0 = FLUID_X_MIN;
        float x1 = FLUID_X_MAX;
        float z0 = FLUID_Z_MIN;
        float z1 = FLUID_Z_MAX;
        float y00 = y + Mth.sin(time + x0 * 9.0F + z0 * 5.0F) * 0.008F;
        float y01 = y + Mth.sin(time + x0 * 9.0F + z1 * 5.0F) * 0.008F;
        float y11 = y + Mth.sin(time + x1 * 9.0F + z1 * 5.0F) * 0.008F;
        float y10 = y + Mth.sin(time + x1 * 9.0F + z0 * 5.0F) * 0.008F;
        fluidVertex(builder, pose, x0, y00, z0, u0, v0, color, light);
        fluidVertex(builder, pose, x0, y01, z1, u0, v1, color, light);
        fluidVertex(builder, pose, x1, y11, z1, u1, v1, color, light);
        fluidVertex(builder, pose, x1, y10, z0, u1, v0, color, light);
    }

    private static void fluidVertex(VertexConsumer builder, PoseStack.Pose pose, float x, float y, float z,
                                    float u, float v, int color, int light) {
        builder.addVertex(pose, x, y, z)
                .setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
