package net.zuperzv.abyssalcraft_reawakening.init.block.entity.renderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import java.util.Map;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.WallAndGroundTransformations;
import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import net.minecraft.client.renderer.blockentity.state.StandingSignRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.PlainSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.PlainSignBlock.Attachment;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import net.zuperzv.abyssalcraft_reawakening.init.block.custom.ModWoodTypes;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public class ModStandingSignRenderer extends ModAbstractSignRenderer<StandingSignRenderState> {
    private static final float RENDER_SCALE = 0.6666667F;
    private static final Vector3fc TEXT_OFFSET = new Vector3f(0.0F, 0.33333334F, 0.046666667F);
    public static final WallAndGroundTransformations<SignRenderState.SignTransformations> TRANSFORMATIONS =
            new WallAndGroundTransformations<SignRenderState.SignTransformations>(
                    ModStandingSignRenderer::createWallTransformation,
                    ModStandingSignRenderer::createGroundTransformation,
                    16
            );
    private final Map<WoodType, ModStandingSignRenderer.Models> signModels;

    public ModStandingSignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);

        this.signModels = ImmutableMap.<WoodType, Models>builder()
                .putAll(
                        WoodType.values()
                                .collect(ImmutableMap.toImmutableMap(
                                        type -> type,
                                        type -> Models.create(context, type)
                                ))
                )
                .put(ModWoodTypes.WITHERWOOD, Models.create(context, ModWoodTypes.WITHERWOOD))
                .build();
    }

    public StandingSignRenderState createRenderState() {
        return new StandingSignRenderState();
    }

    @Override
    public void extractRenderState(
            SignBlockEntity blockEntity,
            StandingSignRenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockState blockState = blockEntity.getBlockState();
        state.attachmentType = PlainSignBlock.getAttachmentPoint(blockState);

        if (blockState.getBlock() instanceof WallSignBlock) {
            Direction direction = (Direction) blockState.getValue(WallSignBlock.FACING);
            state.transformations = TRANSFORMATIONS.wallTransformation(direction);
        } else {
            int rotation = (Integer) blockState.getValue(StandingSignBlock.ROTATION);
            state.transformations = TRANSFORMATIONS.freeTransformations(rotation);
        }
    }

    protected Model.Simple getSignModel(StandingSignRenderState state) {
        return ((ModStandingSignRenderer.Models)this.signModels.get(state.woodType)).get(state.attachmentType);
    }

    @Override
    protected SpriteId getSignSprite(WoodType type) {
        System.out.println("WoodType: " + type);
        System.out.println("Equals WITHERWOOD: " + (type == ModWoodTypes.WITHERWOOD));

        SpriteId sprite = Sheets.getSignSprite(type);
        System.out.println("Sprite: " + sprite);

        return sprite;
    }

    private static Matrix4f baseTransformation(float angle, PlainSignBlock.Attachment attachmentType) {
        Matrix4f result = (new Matrix4f()).translate(0.5F, 0.5F, 0.5F).rotate(Axis.YP.rotationDegrees(-angle));
        if (attachmentType == Attachment.WALL) {
            result.translate(0.0F, -0.3125F, -0.4375F);
        }

        return result;
    }

    private static Transformation bodyTransformation(PlainSignBlock.Attachment attachmentType, float angle) {
        return new Transformation(baseTransformation(angle, attachmentType).scale(0.6666667F, -0.6666667F, -0.6666667F));
    }

    private static Transformation textTransformation(PlainSignBlock.Attachment attachmentType, float angle, boolean isFrontText) {
        Matrix4f result = baseTransformation(angle, attachmentType);
        if (!isFrontText) {
            result.rotate(Axis.YP.rotationDegrees(180.0F));
        }

        float s = 0.010416667F;
        return new Transformation(result.translate(TEXT_OFFSET).scale(0.010416667F, -0.010416667F, 0.010416667F));
    }

    private static SignRenderState.SignTransformations createTransformations(PlainSignBlock.Attachment attachmentType, float angle) {
        return new SignRenderState.SignTransformations(bodyTransformation(attachmentType, angle), textTransformation(attachmentType, angle, true), textTransformation(attachmentType, angle, false));
    }

    private static SignRenderState.SignTransformations createGroundTransformation(int segment) {
        return createTransformations(Attachment.GROUND, RotationSegment.convertToDegrees(segment));
    }

    private static SignRenderState.SignTransformations createWallTransformation(Direction direction) {
        return createTransformations(Attachment.WALL, direction.toYRot());
    }

    public static void submitSpecial(SpriteGetter sprites, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Model.Simple model, SpriteId sprite) {
        submitNodeCollector.submitModel(model, Unit.INSTANCE, poseStack, lightCoords, overlayCoords, -1, sprite, sprites, 0, (ModelFeatureRenderer.CrumblingOverlay)null);
    }

    public static Model.Simple createSignModel(EntityModelSet entityModelSet, WoodType woodType, PlainSignBlock.Attachment attachment) {
        ModelLayerLocation var10000;
        switch (attachment) {
            case GROUND -> {
                if (woodType == ModWoodTypes.WITHERWOOD) {
                    var10000 = ModModelLayers.WITHERWOOD_SIGN;
                } else {
                    var10000 = ModelLayers.createStandingSignModelName(woodType);
                }
            }

            case WALL -> {
                if (woodType == ModWoodTypes.WITHERWOOD) {
                    var10000 = ModModelLayers.WITHERWOOD_WALL_SIGN;
                } else {
                    var10000 = ModelLayers.createWallSignModelName(woodType);
                }
            }

            default -> throw new MatchException((String)null, (Throwable)null);
        }

        ModelLayerLocation layer = var10000;
        return new Model.Simple(entityModelSet.bakeLayer(layer), RenderTypes::entityCutout);
    }

    public static LayerDefinition createSignLayer(boolean standing) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("sign", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -14.0F, -1.0F, 24.0F, 12.0F, 2.0F), PartPose.ZERO);
        if (standing) {
            root.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F), PartPose.ZERO);
        }

        return LayerDefinition.create(mesh, 64, 32);
    }

    private static record Models(Model.Simple standing, Model.Simple wall) {
        public static ModStandingSignRenderer.Models create(BlockEntityRendererProvider.Context context, WoodType type) {
            return new ModStandingSignRenderer.Models(ModStandingSignRenderer.createSignModel(context.entityModelSet(), type, Attachment.GROUND), ModStandingSignRenderer.createSignModel(context.entityModelSet(), type, Attachment.WALL));
        }

        public Model.Simple get(PlainSignBlock.Attachment attachmentType) {
            Model.Simple var10000;
            switch (attachmentType) {
                case GROUND -> var10000 = this.standing;
                case WALL -> var10000 = this.wall;
                default -> throw new MatchException((String)null, (Throwable)null);
            }

            return var10000;
        }
    }
}
