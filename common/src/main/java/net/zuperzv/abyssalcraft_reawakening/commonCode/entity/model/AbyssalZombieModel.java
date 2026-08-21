package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom.AbyssalZombieEntity;
import org.intellij.lang.annotations.Identifier;

public class AbyssalZombieModel extends EntityModel<LivingEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Constants.id("abyssal_zombie"), "main");

	private final ModelPart LeftLeg;
	private final ModelPart RightLeg;
	private final ModelPart Waist;
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;

	public AbyssalZombieModel(ModelPart root) {
		super(root);
        this.LeftLeg = root.getChild("Left Leg");
		this.RightLeg = root.getChild("Right Leg");
		this.Waist = root.getChild("Waist");
		this.Head = this.Waist.getChild("Head");
		this.Body = this.Waist.getChild("Body");
		this.RightArm = this.Waist.getChild("Right Arm");
		this.LeftArm = this.Waist.getChild("Left Arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("Left Leg", CubeListBuilder.create().texOffs(44, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(20, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("Right Leg", CubeListBuilder.create().texOffs(36, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(52, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition Waist = partdefinition.addOrReplaceChild("Waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition Head = Waist.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 36).addBox(0.0F, -12.0F, -1.0F, 0.0F, 12.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(7, 68).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(32, 20).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition Body = Waist.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(20, 36).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(64, 0).addBox(0.0F, 0.0F, 2.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition RightArm = Waist.addOrReplaceChild("Right Arm", CubeListBuilder.create().texOffs(0, 58).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, -10.0F, 0.0F));

		PartDefinition LeftArm = Waist.addOrReplaceChild("Left Arm", CubeListBuilder.create().texOffs(60, 36).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, -10.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(LivingEntityRenderState state) {
		super.setupAnim(state);
		this.Head.xRot = state.xRot * (float) (Math.PI / 180.0);
		this.Head.yRot = state.yRot * (float) (Math.PI / 180.0);

		float walk = state.walkAnimationPos;
		float speed = state.walkAnimationSpeed;
		this.RightLeg.xRot = Mth.cos(walk * 0.6662F) * 1.4F * speed;
		this.LeftLeg.xRot = Mth.cos(walk * 0.6662F + (float) Math.PI) * 1.4F * speed;
	}
}