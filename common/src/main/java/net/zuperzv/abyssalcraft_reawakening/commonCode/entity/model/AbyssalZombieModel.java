package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.util.Mth;
import net.zuperzv.abyssalcraft_reawakening.Constants;

public class AbyssalZombieModel extends ZombieModel<ZombieRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(
					Constants.id("abyssal_zombie"),
					"main"
			);

	private final ModelPart abyssalLeftLeg;
	private final ModelPart abyssalRightLeg;
	private final ModelPart abyssalHead;
	private final ModelPart abyssalBody;
	private final ModelPart abyssalRightArm;
	private final ModelPart abyssalLeftArm;

	public AbyssalZombieModel(ModelPart root) {
		super(root);

		this.abyssalLeftLeg = root.getChild("left_leg");
		this.abyssalRightLeg = root.getChild("right_leg");
		this.abyssalHead = root.getChild("head");
		this.abyssalBody = root.getChild("body");
		this.abyssalRightArm = root.getChild("right_arm");
		this.abyssalLeftArm = root.getChild("left_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition head = root.addOrReplaceChild(
				"head",
				CubeListBuilder.create()
						.texOffs(0, 20)
						.addBox(
								-4.0F, -8.0F, -4.0F,
								8.0F, 8.0F, 8.0F,
								CubeDeformation.NONE
						)

						.texOffs(0, 36)
						.addBox(
								0.0F, -12.0F, -1.0F,
								0.0F, 12.0F, 10.0F,
								CubeDeformation.NONE
						)

						.texOffs(7, 68)
						.addBox(
								-5.0F, -9.0F, -5.0F,
								10.0F, 10.0F, 10.0F,
								CubeDeformation.NONE
						)

						.texOffs(32, 20)
						.addBox(
								-4.0F, -8.0F, -4.0F,
								8.0F, 8.0F, 8.0F,
								new CubeDeformation(0.5F)
						),

				PartPose.offset(0.0F, 0.0F, 0.0F)
		);

		head.addOrReplaceChild(
				"hat",
				CubeListBuilder.create(),
				PartPose.ZERO
		);

		root.addOrReplaceChild(
				"body",
				CubeListBuilder.create()

						.texOffs(20, 36)
						.addBox(
								-4.0F, 0.0F, -2.0F,
								8.0F, 12.0F, 4.0F,
								CubeDeformation.NONE
						)

						.texOffs(64, 0)
						.addBox(
								0.0F, 0.0F, 2.0F,
								0.0F, 12.0F, 4.0F,
								CubeDeformation.NONE
						)

						.texOffs(40, 0)
						.addBox(
								-4.0F, 0.0F, -2.0F,
								8.0F, 12.0F, 4.0F,
								new CubeDeformation(0.25F)
						),

				PartPose.ZERO
		);

		root.addOrReplaceChild(
				"right_arm",
				CubeListBuilder.create()

						.texOffs(0, 58)
						.addBox(
								-3.0F, -2.0F, -2.0F,
								4.0F, 12.0F, 4.0F,
								CubeDeformation.NONE
						)

						.texOffs(16, 0)
						.addBox(
								-3.0F, -2.0F, -2.0F,
								4.0F, 12.0F, 4.0F,
								new CubeDeformation(0.25F)
						),

				PartPose.offset(-5.0F, 2.0F, 0.0F)
		);

		root.addOrReplaceChild(
				"left_arm",
				CubeListBuilder.create()

						.texOffs(60, 36)
						.addBox(
								-1.0F, -2.0F, -2.0F,
								4.0F, 12.0F, 4.0F,
								CubeDeformation.NONE
						)

						// Custom outer arm
						.texOffs(0, 0)
						.addBox(
								-1.0F, -2.0F, -2.0F,
								4.0F, 12.0F, 4.0F,
								new CubeDeformation(0.25F)
						),

				PartPose.offset(5.0F, 2.0F, 0.0F)
		);

		root.addOrReplaceChild(
				"right_leg",
				CubeListBuilder.create()

						.texOffs(36, 52)
						.addBox(
								-2.0F, 0.0F, -2.0F,
								4.0F, 12.0F, 4.0F,
								CubeDeformation.NONE
						)

						// Custom outer leg
						.texOffs(52, 52)
						.addBox(
								-2.0F, 0.0F, -2.0F,
								4.0F, 12.0F, 4.0F,
								new CubeDeformation(0.25F)
						),

				PartPose.offset(-1.9F, 12.0F, 0.0F)
		);

		root.addOrReplaceChild(
				"left_leg",
				CubeListBuilder.create()

						.texOffs(44, 36)
						.addBox(
								-2.0F, 0.0F, -2.0F,
								4.0F, 12.0F, 4.0F,
								CubeDeformation.NONE
						)

						.texOffs(20, 52)
						.addBox(
								-2.0F, 0.0F, -2.0F,
								4.0F, 12.0F, 4.0F,
								new CubeDeformation(0.25F)
						),

				PartPose.offset(1.9F, 12.0F, 0.0F)
		);

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(ZombieRenderState state) {
		super.setupAnim(state);

		this.abyssalHead.xRot =
				state.xRot * ((float) Math.PI / 180.0F);

		this.abyssalHead.yRot =
				state.yRot * ((float) Math.PI / 180.0F);

		float walk = state.walkAnimationPos;
		float speed = state.walkAnimationSpeed;

		this.abyssalRightLeg.xRot =
				Mth.cos(walk * 0.6662F) * 1.4F * speed;

		this.abyssalLeftLeg.xRot =
				Mth.cos(
						walk * 0.6662F + (float) Math.PI
				) * 1.4F * speed;

		AnimationUtils.animateZombieArms(
				this.abyssalLeftArm,
				this.abyssalRightArm,
				state.isAggressive,
				state
		);
	}
}