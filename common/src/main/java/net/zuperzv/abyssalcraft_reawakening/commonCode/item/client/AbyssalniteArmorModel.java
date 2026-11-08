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

public final class AbyssalniteArmorModel<S extends HumanoidRenderState> extends HumanoidModel<S> {

    public AbyssalniteArmorModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.9F))
                        .texOffs(32, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.5F)),
                PartPose.ZERO
        );
        head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.95F)),
                PartPose.ZERO
        );

        PartDefinition rightArm = root.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create()
                        .texOffs(41, 25)
                        .addBox(-3.0F, -0.501F, -1.9564F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.5F)),
                PartPose.offsetAndRotation(-6.25F, 1.0F, -0.25F, -0.0436F, 0.0F, 0.0F)
        );
        rightArm.addOrReplaceChild(
                "shoulder_armor",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.55F)),
                PartPose.offsetAndRotation(-1.25F, -2.001F, 0.0436F, 0.0F, 0.0F, 0.0873F)
        );

        PartDefinition leftArm = root.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create()
                        .texOffs(41, 25)
                        .mirror()
                        .addBox(0.0F, -0.501F, -1.9564F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.5F))
                        .mirror(false),
                PartPose.offsetAndRotation(6.25F, 1.0F, -0.25F, -0.0436F, 0.0F, 0.0F)
        );
        leftArm.addOrReplaceChild(
                "shoulder_armor",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .mirror()
                        .addBox(-1.3899F, -0.9734F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.55F))
                        .mirror(false),
                PartPose.offsetAndRotation(0.5F, -2.001F, 0.0436F, 0.0F, 0.0F, -0.0873F)
        );

        root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.8F)),
                PartPose.offset(-1.9F, 12.0F, 0.0F)
        );
        root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .mirror()
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.79F))
                        .mirror(false),
                PartPose.offset(1.9F, 12.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    public void setArmorPartsVisible(boolean showHead, boolean showBody, boolean showArms, boolean showLegs) {
        this.head.visible = showHead;
        this.body.visible = showBody;
        this.rightArm.visible = showArms;
        this.leftArm.visible = showArms;
        this.setArmCubesVisible(true);
        this.rightLeg.visible = showLegs;
        this.leftLeg.visible = showLegs;
    }

    public void setArmCubesVisible(boolean visible) {
        this.rightArm.skipDraw = !visible;
        this.leftArm.skipDraw = !visible;
    }

    public void copyPoseFrom(HumanoidModel<S> source) {
        copyRotation(source.head, this.head);
        copyRotation(source.body, this.body);
        copyRotation(source.rightArm, this.rightArm);
        copyRotation(source.leftArm, this.leftArm);
        copyRotation(source.rightLeg, this.rightLeg);
        copyRotation(source.leftLeg, this.leftLeg);
    }

    public void copyArmorStandPoseFrom(HumanoidModel<S> source) {
        copyRotation(source.head, this.head);
        copyRotation(source.body, this.body);
        copyRotation(source.rightLeg, this.rightLeg);
        copyRotation(source.leftLeg, this.leftLeg);
        this.rightArm.setRotation(-0.0436F, 0.0F, 0.0F);
        this.leftArm.setRotation(-0.0436F, 0.0F, 0.0F);
    }

    private static void copyRotation(ModelPart source, ModelPart target) {
        target.xRot = source.xRot;
        target.yRot = source.yRot;
        target.zRot = source.zRot;
    }
}
