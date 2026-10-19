package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.PEMachineBlockEntity;
import org.jspecify.annotations.Nullable;

/** Renders the statue's single currently displayed charm above its plinth. */
public final class PEMachineBlockEntityRenderer
        implements BlockEntityRenderer<PEMachineBlockEntity, PEMachineBlockEntityRenderer.RenderState> {
    private final ItemModelResolver itemModelResolver;

    public PEMachineBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static final class RenderState extends BlockEntityRenderState {
        public boolean hasCharm;
        public float rotation;
        public final ItemStackRenderState charm = new ItemStackRenderState();
        public boolean showJarFluid;
        public float fluidLevel;
        public TextureAtlasSprite fluidSprite;
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
            PEMachineBlockEntity blockEntity,
            RenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.showJarFluid = false;
        state.fluidSprite = null;
        ItemStack displayedCharm = blockEntity.getDisplayedCharm();
        state.hasCharm = !displayedCharm.isEmpty();
        if (blockEntity.getLevel() == null) return;
        if (state.hasCharm) {
            state.rotation = (blockEntity.getLevel().getGameTime() + partialTicks) * 1.5F % 360.0F;
            itemModelResolver.updateForTopItem(
                    state.charm,
                    displayedCharm,
                    ItemDisplayContext.FIXED,
                    blockEntity.getLevel(),
                    null,
                    0
            );
        }
        state.showJarFluid = blockEntity.getBlockState().is(net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks.LUNAR_JAR.block().get())
                && blockEntity.getStoredPE() > 0;
        state.fluidLevel = blockEntity.getCapacityPE() <= 0 ? 0.0F
                : (float) blockEntity.getStoredPE() / blockEntity.getCapacityPE();
        if (state.showJarFluid) {
            FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet()
                    .get(Blocks.WATER.defaultBlockState().getFluidState());
            state.fluidSprite = fluidModel.stillMaterial() == null ? null : fluidModel.stillMaterial().sprite();
        }
    }

    @Override
    public void submit(
            RenderState state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState cameraRenderState
    ) {
        if (state.hasCharm) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 1.12F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));
            poseStack.scale(0.35F, 0.35F, 0.35F);
            state.charm.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
        if (state.showJarFluid && state.fluidSprite != null) submitJarFluid(state, poseStack, submitNodeCollector);
    }

    private static void submitJarFluid(RenderState state, PoseStack poseStack, SubmitNodeCollector collector) {
        float bottom = 3.08F / 16.0F;
        float top = bottom + (10.0F / 16.0F) * Math.max(0.0F, Math.min(1.0F, state.fluidLevel));
        float min = 3.12F / 16.0F;
        float max = 12.88F / 16.0F;
        TextureAtlasSprite sprite = state.fluidSprite;
        float u0 = sprite.getU0(), u1 = sprite.getU1(), v0 = sprite.getV0(), v1 = sprite.getV1();
        int color = 0xE8651422;
        collector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(), (pose, out) -> {
            quad(out, pose, min,bottom,min, min,top,min, max,top,min, max,bottom,min, u0,v0,u1,v1, color,state.lightCoords, 0,0,-1);
            quad(out, pose, max,bottom,max, max,top,max, min,top,max, min,bottom,max, u0,v0,u1,v1, color,state.lightCoords, 0,0,1);
            quad(out, pose, min,bottom,max, min,top,max, min,top,min, min,bottom,min, u0,v0,u1,v1, color,state.lightCoords, -1,0,0);
            quad(out, pose, max,bottom,min, max,top,min, max,top,max, max,bottom,max, u0,v0,u1,v1, color,state.lightCoords, 1,0,0);
            quad(out, pose, min,top,min, min,top,max, max,top,max, max,top,min, u0,v0,u1,v1, color,state.lightCoords, 0,1,0);
        });
    }

    private static void quad(VertexConsumer out, PoseStack.Pose pose,
            float x0,float y0,float z0, float x1,float y1,float z1,
            float x2,float y2,float z2, float x3,float y3,float z3,
            float u0,float v0,float u1,float v1,int color,int light,float nx,float ny,float nz) {
        vertex(out,pose,x0,y0,z0,u0,v1,color,light,nx,ny,nz);
        vertex(out,pose,x1,y1,z1,u0,v0,color,light,nx,ny,nz);
        vertex(out,pose,x2,y2,z2,u1,v0,color,light,nx,ny,nz);
        vertex(out,pose,x3,y3,z3,u1,v1,color,light,nx,ny,nz);
    }

    private static void vertex(VertexConsumer out, PoseStack.Pose pose,float x,float y,float z,float u,float v,int color,int light,float nx,float ny,float nz) {
        out.addVertex(pose,x,y,z).setColor(color).setUv(u,v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,nx,ny,nz);
    }
}
