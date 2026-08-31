package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.animations.GroundlingAnimation;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.state.GroundlingRenderState;
import org.jspecify.annotations.NonNull;

public class GroundlingModel extends EntityModel<GroundlingRenderState> {
	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(
					Constants.id("groundling"),
					"main"
			);

	private final ModelPart Entity;
	private final ModelPart Waist;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart Body;
	private final ModelPart Head;
	private final ModelPart Eyes;
	private final ModelPart RightFins;
	private final ModelPart TopFin;
	private final ModelPart MediumFin;
	private final ModelPart LowerFin;
	private final ModelPart LeftFins;
	private final ModelPart TopFin2;
	private final ModelPart MediumFin2;
	private final ModelPart LowerFin2;
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;

	private final KeyframeAnimation idleAnimation;
	private final KeyframeAnimation attackAnimation;
	private final KeyframeAnimation walkAnimation;
	private final KeyframeAnimation crawl_to_walkAnimation;
	private final KeyframeAnimation wake_ip_to_crawlAnimation;
	private final KeyframeAnimation crawlAnimation;
	private final KeyframeAnimation hideAnimation;
	private final KeyframeAnimation hiddenAnimation;
	private final KeyframeAnimation wake_upAnimation;

	public GroundlingModel(ModelPart root) {
		super(root);
		this.Entity = root.getChild("Entity");
		this.Waist = this.Entity.getChild("Waist");
		this.RightArm = this.Waist.getChild("RightArm");
		this.LeftArm = this.Waist.getChild("LeftArm");
		this.Body = this.Waist.getChild("Body");
		this.Head = this.Waist.getChild("Head");
		this.Eyes = this.Head.getChild("Eyes");
		this.RightFins = this.Head.getChild("RightFins");
		this.TopFin = this.RightFins.getChild("TopFin");
		this.MediumFin = this.RightFins.getChild("MediumFin");
		this.LowerFin = this.RightFins.getChild("LowerFin");
		this.LeftFins = this.Head.getChild("LeftFins");
		this.TopFin2 = this.LeftFins.getChild("TopFin2");
		this.MediumFin2 = this.LeftFins.getChild("MediumFin2");
		this.LowerFin2 = this.LeftFins.getChild("LowerFin2");
		this.RightLeg = this.Entity.getChild("RightLeg");
		this.LeftLeg = this.Entity.getChild("LeftLeg");

		this.idleAnimation = GroundlingAnimation.idle.bake(root);
		this.attackAnimation = GroundlingAnimation.attack.bake(root);
		this.walkAnimation = GroundlingAnimation.walk.bake(root);
		this.crawl_to_walkAnimation = GroundlingAnimation.crawl_to_walk.bake(root);
		this.wake_ip_to_crawlAnimation = GroundlingAnimation.wake_ip_to_crawl.bake(root);
		this.crawlAnimation = GroundlingAnimation.crawl.bake(root);
		this.hideAnimation = GroundlingAnimation.hide.bake(root);
		this.hiddenAnimation = GroundlingAnimation.hidden.bake(root);
		this.wake_upAnimation = GroundlingAnimation.wake_up.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Entity = partdefinition.addOrReplaceChild("Entity", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Waist = Entity.addOrReplaceChild("Waist", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, 0.0F));

		PartDefinition RightArm = Waist.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(28, 20).addBox(-2.0F, -1.25F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 9).mirror().addBox(-2.0F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(-2.0F, -3.75F, 0.0F));

		PartDefinition LeftArm = Waist.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(28, 20).mirror().addBox(-0.25F, -1.25F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(36, 9).addBox(-0.25F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(2.25F, -3.75F, 0.0F));

		PartDefinition Body = Waist.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 28).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(28, 0).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Head = Waist.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -7.1F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-3.5F, -7.1F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -4.9F, 0.0F));

		PartDefinition Eyes = Head.addOrReplaceChild("Eyes", CubeListBuilder.create().texOffs(36, 20).addBox(-3.5F, -4.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.26F))
		.texOffs(34, 34).addBox(-3.5F, -4.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -2.6F, 0.0F));

		PartDefinition RightFins = Head.addOrReplaceChild("RightFins", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.5F, -3.85F, 0.0F, 0.0873F, -0.0435F, -0.0038F));

		PartDefinition TopFin = RightFins.addOrReplaceChild("TopFin", CubeListBuilder.create(), PartPose.offset(0.0F, -2.75F, 0.0F));

		PartDefinition cube_r1 = TopFin.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, 0.0F, -3.5F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition MediumFin = RightFins.addOrReplaceChild("MediumFin", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, 0.0F));

		PartDefinition cube_r2 = MediumFin.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, 0.0F, -3.5F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition LowerFin = RightFins.addOrReplaceChild("LowerFin", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 0.0F));

		PartDefinition cube_r3 = LowerFin.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, 0.0F, -3.5F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition LeftFins = Head.addOrReplaceChild("LeftFins", CubeListBuilder.create(), PartPose.offsetAndRotation(3.5F, -3.85F, 0.0F, 0.0873F, 0.0435F, 0.0038F));

		PartDefinition TopFin2 = LeftFins.addOrReplaceChild("TopFin2", CubeListBuilder.create(), PartPose.offset(0.0F, -2.75F, 0.0F));

		PartDefinition cube_r4 = TopFin2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, 0.0F, -3.5F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition MediumFin2 = LeftFins.addOrReplaceChild("MediumFin2", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, 0.0F));

		PartDefinition cube_r5 = MediumFin2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, 0.0F, -3.5F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition LowerFin2 = LeftFins.addOrReplaceChild("LowerFin2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 0.0F));

		PartDefinition cube_r6 = LowerFin2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, 0.0F, -3.5F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition RightLeg = Entity.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(28, 9).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 20).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.0F, -9.0F, 0.0F));

		PartDefinition LeftLeg = Entity.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(28, 9).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(36, 20).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offset(1.0F, -9.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(@NonNull GroundlingRenderState state) {
		super.setupAnim(state);

		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (state.hideAnimationState.isStarted()) {

			this.hideAnimation.apply(
					state.hideAnimationState,
					state.ageInTicks
			);

			return;
		}

		if (state.hiddenAnimationState.isStarted()) {

			this.hiddenAnimation.apply(
					state.hiddenAnimationState,
					state.ageInTicks
			);

			return;
		}

		if (state.wakeUpAnimationState.isStarted()) {

			this.wake_upAnimation.apply(
					state.wakeUpAnimationState,
					state.ageInTicks
			);

			return;
		}

		this.idleAnimation.apply(
				state.idleAnimationState,
				state.ageInTicks
		);

		this.walkAnimation.apply(
				state.walkAnimationState,
				state.ageInTicks
		);

		this.attackAnimation.apply(
				state.attackAnimationState,
				state.ageInTicks
		);

		this.Head.xRot +=
				state.xRot * ((float) Math.PI / 180.0F);

		this.Head.yRot +=
				state.yRot * ((float) Math.PI / 180.0F);

		if (state.walkAnimationState.isStarted()) {

			float walk = state.walkAnimationPos;
			float speed = state.walkAnimationSpeed;

			this.RightLeg.xRot +=
					Mth.cos(walk * 0.5662F)
							* 1.3F
							* speed;

			this.LeftLeg.xRot +=
					Mth.cos(
							walk * 0.5662F + (float) Math.PI
					)
							* 1.3F
							* speed;
		}
	}
}