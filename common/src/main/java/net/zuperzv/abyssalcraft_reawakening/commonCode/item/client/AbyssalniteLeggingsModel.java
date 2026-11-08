package net.zuperzv.abyssalcraft_reawakening.commonCode.item.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public final class AbyssalniteLeggingsModel<S extends HumanoidRenderState> extends HumanoidModel<S> {

    public AbyssalniteLeggingsModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.51F)),
                PartPose.ZERO
        );
        body.addOrReplaceChild(
                "left_flare",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-1.0F, -2.0F, -3.0F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.1F))
                        .mirror(false),
                PartPose.offsetAndRotation(-4.25F, 11.5F, 0.0F, 0.0F, 0.0F, 0.1745F)
        );
        body.addOrReplaceChild(
                "right_flare",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, -2.0F, -3.0F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.1F)),
                PartPose.offsetAndRotation(4.25F, 11.5F, 0.0F, 0.0F, 0.0F, -0.1745F)
        );

        root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.251F)),
                PartPose.offset(-1.9F, 12.0F, 0.0F)
        );
        root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .mirror()
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
                        .mirror(false),
                PartPose.offset(1.9F, 12.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    public void setLeggingsVisible(boolean visible) {
        this.body.visible = visible;
        this.rightLeg.visible = visible;
        this.leftLeg.visible = visible;
    }

    public void copyPoseFrom(HumanoidModel<S> source) {
        this.body.xRot = source.body.xRot;
        this.body.yRot = source.body.yRot;
        this.body.zRot = source.body.zRot;
        this.rightLeg.xRot = source.rightLeg.xRot;
        this.rightLeg.yRot = source.rightLeg.yRot;
        this.rightLeg.zRot = source.rightLeg.zRot;
        this.leftLeg.xRot = source.leftLeg.xRot;
        this.leftLeg.yRot = source.leftLeg.yRot;
        this.leftLeg.zRot = source.leftLeg.zRot;
    }
}
