package net.zuperzv.abyssalcraft_reawakening.commonCode.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
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
                        .addBox(-3.0F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.5F)),
                PartPose.offset(-6.25F, 1.75F, -0.25F)
        );
        rightArm.addOrReplaceChild(
                "shoulder_armor",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.65F)),
                PartPose.offsetAndRotation(-1.25F, -3.0F, 0.0F, 0.0F, 0.0F, 0.0873F)
        );

        PartDefinition leftArm = root.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create()
                        .texOffs(41, 25)
                        .mirror()
                        .addBox(0.0F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.5F))
                        .mirror(false),
                PartPose.offsetAndRotation(6.25F, 1.75F, -0.25F, -0.0436F, 0.0F, 0.0F)
        );
        leftArm.addOrReplaceChild(
                "shoulder_armor",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .mirror()
                        .addBox(-1.3899F, -0.9734F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.65F))
                        .mirror(false),
                PartPose.offsetAndRotation(0.5F, -3.0F, 0.0F, 0.0F, 0.0F, -0.0873F)
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

    @Override
    public void setupAnim(S state) {
        if (!(state instanceof ArmorStandRenderState armorStandState)) {
            super.setupAnim(state);
            return;
        }

        setRotation(this.head, armorStandState.headPose.x(), armorStandState.headPose.y(), armorStandState.headPose.z());
        setRotation(this.body, armorStandState.bodyPose.x(), armorStandState.bodyPose.y(), armorStandState.bodyPose.z());
        setRotation(this.rightArm, armorStandState.rightArmPose.x(), armorStandState.rightArmPose.y(), armorStandState.rightArmPose.z());
        setRotation(this.leftArm, armorStandState.leftArmPose.x(), armorStandState.leftArmPose.y(), armorStandState.leftArmPose.z());
        setRotation(this.rightLeg, armorStandState.rightLegPose.x(), armorStandState.rightLegPose.y(), armorStandState.rightLegPose.z());
        setRotation(this.leftLeg, armorStandState.leftLegPose.x(), armorStandState.leftLegPose.y(), armorStandState.leftLegPose.z());
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

    private static void setRotation(ModelPart part, float xDegrees, float yDegrees, float zDegrees) {
        part.setRotation(
                xDegrees * 0.017453292F,
                yDegrees * 0.017453292F,
                zDegrees * 0.017453292F
        );
    }
}
