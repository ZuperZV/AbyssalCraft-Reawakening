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
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.material.Fluid;
import org.joml.Vector3f;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.CrystalGrowthBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom.CrystalProductBlock;
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
        public float rotation;
        public boolean hasSeed;
        public boolean blockProduct;
        public int crystalTint;
        public float crystalMorphProgress = 1.0F;
        public float seedScale = 1.0F;
        public float fluidAmount;
        public int fluidColor = 0xFFFFFFFF;
        @Nullable
        public TextureAtlasSprite fluidSprite;
        @Nullable
        public BlockStateModel crystalModel;
        @Nullable
        public BlockStateModel crystalFromModel;
        public final ItemStackRenderState seed = new ItemStackRenderState();
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
        state.rotation = level == null ? 0 : (level.getGameTime() + partialTicks) * 0.5F;
        state.hasSeed = false;
        state.blockProduct = false;
        state.crystalModel = null;
        state.crystalFromModel = null;
        state.fluidSprite = null;
        state.crystalMorphProgress = 1.0F;
        state.seedScale = 1.0F;

        float progress = Mth.clamp(blockEntity.getGrowthProgress(), 0.0F, 1.0F);
        float eased = progress * progress * (3.0F - 2.0F * progress);

        int capacity = Math.max(1, blockEntity.getFluidTankCapacity());
        EssenceBoilerFluid tankFluid = blockEntity.getFluidTank();
        EssenceBoilerFluid outputFluid = blockEntity.getCraftingOutputFluid();
        float tankAmount = tankFluid.isEmpty()
                ? 0.0F
                : Mth.clamp((float) tankFluid.amount() / capacity, 0.0F, 1.0F);
        float outputAmount = outputFluid.isEmpty()
                ? 0.0F
                : Mth.clamp((float) outputFluid.amount() / capacity, 0.0F, 1.0F);
        state.fluidAmount = level == null ? 0.0F : Mth.lerp(progress, tankAmount, outputAmount);
        if (level == null) {
            return;
        }

        FluidTint tank = resolveFluid(level, tankFluid, blockEntity.getBlockPos());
        FluidTint output = resolveFluid(level, outputFluid, blockEntity.getBlockPos());
        if (tank != null) {
            state.fluidSprite = tank.sprite();
            state.fluidColor = tank.color();
        }
        if (output != null) {
            if (tank == null) {
                state.fluidSprite = output.sprite();
                state.fluidColor = output.color();
            } else {
                state.fluidColor = lerpColor(tank.color(), output.color(), progress);
            }
        }

        ItemStack stack = blockEntity.getGrowingResult();
        ItemStack morphTarget = blockEntity.getCraftingOutputItem();
        boolean morphing = progress > 0.0F
                && !morphTarget.isEmpty()
                && !ItemStack.isSameItemSameComponents(stack, morphTarget);
        ItemStack display = morphing ? morphTarget : stack;
        if (display.isEmpty()) {
            return;
        }
        state.hasSeed = true;

        CrystalProductBlock renderCrystal = crystalFor(display);
        if (renderCrystal != null) {
            state.crystalModel = Minecraft.getInstance().getModelManager()
                    .getBlockStateModelSet().get(renderCrystal.defaultBlockState());
            state.blockProduct = state.crystalModel != null;
        }

        if (state.blockProduct) {
            CrystalProductBlock fromCrystal = crystalFor(stack);
            CrystalProductBlock toCrystal = crystalFor(morphTarget);
            if (morphing && fromCrystal != null && toCrystal != null) {
                state.crystalFromModel = modelFor(fromCrystal);
                state.crystalTint = lerpColor(fromCrystal.getTint(), toCrystal.getTint(), eased);
                state.crystalMorphProgress = eased;
            } else {
                state.crystalTint = renderCrystal.getTint();
            }
            return;
        }

        if (morphing) {
            state.seedScale = Mth.lerp(eased, 0.4F, 1.0F);
        }
        itemModelResolver.updateForTopItem(
                state.seed, display, ItemDisplayContext.FIXED, level, null, 0
        );
    }

    @Nullable
    private static CrystalProductBlock crystalFor(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        Identifier itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        var crystalBlockHandle = ModBlocks.CRYSTAL_BLOCKS.get(itemId);
        return crystalBlockHandle == null ? null : crystalBlockHandle.get();
    }

    @Nullable
    private static BlockStateModel modelFor(CrystalProductBlock crystal) {
        return Minecraft.getInstance().getModelManager()
                .getBlockStateModelSet().get(crystal.defaultBlockState());
    }

    @Nullable
    private static FluidTint resolveFluid(Level level, EssenceBoilerFluid fluid, BlockPos pos) {
        if (fluid.isEmpty()) {
            return null;
        }
        FluidModel model = Minecraft.getInstance().getModelManager()
                .getFluidStateModelSet().get(fluid.fluid().defaultFluidState());
        if (model == null) {
            return null;
        }
        Material.Baked still = model.stillMaterial();
        if (still == null) {
            return null;
        }
        return new FluidTint(
                still.sprite(),
                EssenceBoilerFluidColor.getTint(model, fluid, level, pos)
        );
    }

    private static int lerpColor(int from, int to, float t) {
        int a = Mth.lerpInt(t, (from >>> 24) & 0xFF, (to >>> 24) & 0xFF);
        int r = Mth.lerpInt(t, (from >> 16) & 0xFF, (to >> 16) & 0xFF);
        int g = Mth.lerpInt(t, (from >> 8) & 0xFF, (to >> 8) & 0xFF);
        int b = Mth.lerpInt(t, from & 0xFF, to & 0xFF);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private record FluidTint(TextureAtlasSprite sprite, int color) {}

    @Override
    public void submit(GrowthState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.level == null) {
            return;
        }
        submitFluid(state, poseStack, collector);
        if (!state.hasSeed) {
            return;
        }

        if (state.blockProduct) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 2.0F / 16.0F, 0.0F);
            if (state.crystalFromModel != null && state.crystalModel != null) {
                submitMorphedCrystal(state, poseStack, collector);
            } else if (state.crystalModel != null) {
                submitCrystalModel(state, state.crystalModel, state.crystalTint, poseStack, collector);
            }
            poseStack.popPose();
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));
        float scale = 0.25F * state.seedScale;
        poseStack.scale(scale, scale, scale);
        state.seed.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    private void submitCrystalModel(GrowthState state, BlockStateModel model, int tint,
                                    PoseStack poseStack, SubmitNodeCollector collector) {
        List<BlockStateModelPart> parts = new ArrayList<>();
        model.collectParts(RandomSource.create(0L), parts);
        collector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(), (pose, builder) -> {
            QuadInstance quad = new QuadInstance();
            quad.setColor(0xFF000000 | tint);
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

    private void submitMorphedCrystal(GrowthState state, PoseStack poseStack,
                                      SubmitNodeCollector collector) {
        List<BlockStateModelPart> fromParts = new ArrayList<>();
        List<BlockStateModelPart> toParts = new ArrayList<>();
        state.crystalFromModel.collectParts(RandomSource.create(0L), fromParts);
        state.crystalModel.collectParts(RandomSource.create(0L), toParts);
        float progress = state.crystalMorphProgress;
        int color = 0xFF000000 | state.crystalTint;

        collector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(), (pose, builder) -> {
            QuadInstance quad = new QuadInstance();
            quad.setColor(color);
            quad.setLightCoords(state.lightCoords);
            quad.setOverlayCoords(OverlayTexture.NO_OVERLAY);
            int partCount = Math.min(fromParts.size(), toParts.size());
            for (int partIndex = 0; partIndex < partCount; partIndex++) {
                BlockStateModelPart fromPart = fromParts.get(partIndex);
                BlockStateModelPart toPart = toParts.get(partIndex);
                for (net.minecraft.core.Direction direction : net.minecraft.core.Direction.values()) {
                    submitMorphedQuads(builder, pose, quad, fromPart.getQuads(direction),
                            toPart.getQuads(direction), progress);
                }
                submitMorphedQuads(builder, pose, quad, fromPart.getQuads(null),
                        toPart.getQuads(null), progress);
            }
        });
    }

    private static void submitMorphedQuads(VertexConsumer builder, PoseStack.Pose pose,
                                           QuadInstance quad, List<BakedQuad> fromQuads,
                                           List<BakedQuad> toQuads, float progress) {
        int count = Math.max(fromQuads.size(), toQuads.size());
        for (int i = 0; i < count; i++) {
            if (i >= fromQuads.size()) {
                builder.putBakedQuad(pose, toQuads.get(i), quad);
            } else if (i >= toQuads.size()) {
                builder.putBakedQuad(pose, fromQuads.get(i), quad);
            } else {
                builder.putBakedQuad(pose, interpolateQuad(fromQuads.get(i), toQuads.get(i), progress), quad);
            }
        }
    }

    private static BakedQuad interpolateQuad(BakedQuad from, BakedQuad to, float progress) {
        return new BakedQuad(
                interpolatePosition(from.position(0), to.position(0), progress),
                interpolatePosition(from.position(1), to.position(1), progress),
                interpolatePosition(from.position(2), to.position(2), progress),
                interpolatePosition(from.position(3), to.position(3), progress),
                interpolateUV(from.packedUV0(), to.packedUV0(), progress),
                interpolateUV(from.packedUV1(), to.packedUV1(), progress),
                interpolateUV(from.packedUV2(), to.packedUV2(), progress),
                interpolateUV(from.packedUV3(), to.packedUV3(), progress),
                to.direction(), to.materialInfo()
        );
    }

    private static long interpolateUV(long from, long to, float progress) {
        return UVPair.pack(
                Mth.lerp(progress, UVPair.unpackU(from), UVPair.unpackU(to)),
                Mth.lerp(progress, UVPair.unpackV(from), UVPair.unpackV(to))
        );
    }

    private static Vector3f interpolatePosition(org.joml.Vector3fc from, org.joml.Vector3fc to,
                                                float progress) {
        return new Vector3f(
                Mth.lerp(progress, from.x(), to.x()),
                Mth.lerp(progress, from.y(), to.y()),
                Mth.lerp(progress, from.z(), to.z())
        );
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
