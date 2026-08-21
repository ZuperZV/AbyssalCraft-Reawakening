package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model;// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.zuperzv.abyssalcraft_reawakening.Constants;

public class AbyssalZombieBabyModel extends AbyssalZombieModel {
	public AbyssalZombieBabyModel(ModelPart root) {
		super(root);
	}

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Constants.id("abyssal_zombie_baby"), "main");

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();

		root.addOrReplaceChild(
				"Left Leg",
				CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(
								-1.0F, 0.0F, -1.0F,
								2.0F, 4.0F, 2.0F,
								new CubeDeformation(0.0F)
						),
				PartPose.offset(1.0F, 20.0F, 0.0F)
		);

		root.addOrReplaceChild(
				"Right Leg",
				CubeListBuilder.create()
						.texOffs(8, 16)
						.addBox(
								-1.0F, 0.0F, -1.0F,
								2.0F, 4.0F, 2.0F,
								new CubeDeformation(0.0F)
						),
				PartPose.offset(-1.0F, 20.0F, 0.0F)
		);

		PartDefinition waist = root.addOrReplaceChild(
				"Waist",
				CubeListBuilder.create(),
				PartPose.ZERO
		);

		// HEAD
		waist.addOrReplaceChild(
				"Head",
				CubeListBuilder.create()
						.texOffs(3, 3)
						.addBox(
								-3.0F, -6.25F, -3.0F,
								6.0F, 6.0F, 6.0F,
								new CubeDeformation(0.0F)
						)
						.texOffs(35, 3)
						.addBox(
								-3.0F, -6.15F, -3.0F,
								6.0F, 6.0F, 6.0F,
								new CubeDeformation(0.25F)
						),
				PartPose.offset(0.0F, 15.25F, 0.0F)
		);

		// BODY
		waist.addOrReplaceChild(
				"Body",
				CubeListBuilder.create()
						.texOffs(16, 16)
						.addBox(
								-2.0F, -2.5F, -1.0F,
								4.0F, 5.0F, 2.0F,
								new CubeDeformation(0.0F)
						)
						.texOffs(0, 18)
						.addBox(
								0.0F, -2.5F, 1.0F,
								0.0F, 5.0F, 4.0F,
								new CubeDeformation(0.0F)
						),
				PartPose.offset(0.0F, 17.5F, 0.0F)
		);

		// RIGHT ARM
		waist.addOrReplaceChild(
				"Right Arm",
				CubeListBuilder.create()
						.texOffs(36, 16)
						.addBox(
								-1.0F, -0.5F, -1.0F,
								2.0F, 5.0F, 2.0F,
								new CubeDeformation(0.0F)
						)
						.texOffs(36, 23)
						.addBox(
								-1.0F, -0.5F, -1.0F,
								2.0F, 5.0F, 2.0F,
								new CubeDeformation(0.25F)
						),
				PartPose.offset(-3.0F, 15.5F, 0.0F)
		);

		// LEFT ARM
		waist.addOrReplaceChild(
				"Left Arm",
				CubeListBuilder.create()
						.texOffs(28, 16)
						.addBox(
								-1.0F, -0.5F, -1.0F,
								2.0F, 5.0F, 2.0F,
								new CubeDeformation(0.0F)
						)
						.texOffs(28, 23)
						.addBox(
								-1.0F, -0.5F, -1.0F,
								2.0F, 5.0F, 2.0F,
								new CubeDeformation(0.25F)
						),
				PartPose.offset(3.0F, 15.5F, 0.0F)
		);

		ModelPart dummy = null;

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}