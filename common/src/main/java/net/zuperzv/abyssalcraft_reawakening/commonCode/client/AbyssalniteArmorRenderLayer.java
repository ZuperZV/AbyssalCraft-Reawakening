package net.zuperzv.abyssalcraft_reawakening.commonCode.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModArmorMaterials;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.commonCode.mixin.HumanoidArmorLayerAccessor;
import net.zuperzv.abyssalcraft_reawakening.commonCode.mixin.LivingEntityRendererAccessor;

public final class AbyssalniteArmorRenderLayer<
        S extends HumanoidRenderState,
        M extends HumanoidModel<S>
        > extends RenderLayer<S, M> {

    private final AbyssalniteArmorModel<S> helmetModel = createArmorModel(true, false, false, false);
    private final AbyssalniteArmorModel<S> chestplateModel = createArmorModel(false, true, true, false);
    private final EthaxiumArmorModel<S> ethaxiumHelmetModel = createEthaxiumArmorModel(true, false, false, false);
    private final EthaxiumArmorModel<S> ethaxiumChestplateModel = createEthaxiumArmorModel(false, true, true, false);
    private final EthaxiumArmorModel<S> ethaxiumBootsModel = createEthaxiumArmorModel(false, false, false, true);
    private final EquipmentLayerRenderer equipmentRenderer;

    private AbyssalniteArmorRenderLayer(LivingEntityRenderer<?, S, M> renderer) {
        super(renderer);
        this.equipmentRenderer = findEquipmentRenderer(renderer);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static RenderLayer create(LivingEntityRenderer renderer) {
        return new AbyssalniteArmorRenderLayer(renderer);
    }

    private static EquipmentLayerRenderer findEquipmentRenderer(LivingEntityRenderer<?, ?, ?> renderer) {
        for (RenderLayer<?, ?> layer : ((LivingEntityRendererAccessor)(Object)renderer).abyssalcraft$getLayers()) {
            if (layer instanceof HumanoidArmorLayer<?, ?, ?> armorLayer) {
                return ((HumanoidArmorLayerAccessor)(Object)armorLayer).abyssalcraft$getEquipmentRenderer();
            }
        }
        return null;
    }

    private static <S extends HumanoidRenderState> AbyssalniteArmorModel<S> createArmorModel(
            boolean showHead,
            boolean showBody,
            boolean showArms,
            boolean showLegs
    ) {
        AbyssalniteArmorModel<S> model =
                new AbyssalniteArmorModel<>(AbyssalniteArmorModel.createBodyLayer().bakeRoot());
        model.setArmorPartsVisible(showHead, showBody, showArms, showLegs);
        return model;
    }

    private static <S extends HumanoidRenderState> EthaxiumArmorModel<S> createEthaxiumArmorModel(
            boolean showHead,
            boolean showBody,
            boolean showArms,
            boolean showLegs
    ) {
        EthaxiumArmorModel<S> model =
                new EthaxiumArmorModel<>(EthaxiumArmorModel.createBodyLayer().bakeRoot());
        model.setArmorPartsVisible(showHead, showBody, showArms, showLegs);
        return model;
    }

    @Override
    public void submit(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            S state,
            float yRot,
            float xRot
    ) {
        if (state instanceof ArmorStandRenderState) {
            if (this.equipmentRenderer == null) {
                return;
            }

            ItemStack helmet = state.headEquipment;
            if (isEthaxiumHelmet(helmet)) {
                ethaxiumHelmetModel.setArmorPartsVisible(true, false, false, false);
                ethaxiumHelmetModel.setupAnim(state);
                this.renderArmorLayer(
                        ethaxiumHelmetModel,
                        EquipmentClientInfo.LayerType.HUMANOID,
                        ModArmorMaterials.ETHAXIUM_ASSET,
                        helmet,
                        poseStack,
                        submitNodeCollector,
                        lightCoords,
                        state
                );
            }

            ItemStack chestplate = state.chestEquipment;
            ResourceKey<EquipmentAsset> asset = getEquipmentAsset(chestplate);
            if (isCustomChestplate(chestplate) && asset != null) {
                if (chestplate.is(ModItems.ETHAXIUM_CHESTPLATE.get())) {
                    ethaxiumChestplateModel.setArmorPartsVisible(false, true, true, false);
                    ethaxiumChestplateModel.setupAnim(state);
                    this.renderArmorLayer(
                            ethaxiumChestplateModel,
                            EquipmentClientInfo.LayerType.HUMANOID,
                            asset,
                            chestplate,
                            poseStack,
                            submitNodeCollector,
                            lightCoords,
                            state
                    );
                } else {
                    setArmorPartsVisible(chestplateModel, false, true, true, false);
                    chestplateModel.rightArm.skipDraw = false;
                    chestplateModel.leftArm.skipDraw = false;
                    chestplateModel.copyArmorStandPoseFrom(this.getParentModel());
                    this.renderArmorLayer(
                            chestplateModel,
                            EquipmentClientInfo.LayerType.HUMANOID,
                            asset,
                            chestplate,
                            poseStack,
                            submitNodeCollector,
                            lightCoords,
                            state
                    );
                }
            }

            ItemStack boots = state.feetEquipment;
            if (isCustomBoots(boots)) {
                ethaxiumBootsModel.setArmorPartsVisible(false, false, false, true);
                ethaxiumBootsModel.setupAnim(state);
                this.renderArmorLayer(
                        ethaxiumBootsModel,
                        EquipmentClientInfo.LayerType.HUMANOID,
                        ModArmorMaterials.ETHAXIUM_ASSET,
                        boots,
                        poseStack,
                        submitNodeCollector,
                        lightCoords,
                        state
                );
            }
            return;
        }

        if (state.isBaby) {
            return;
        }

        if (this.equipmentRenderer == null) {
            return;
        }

        HumanoidModel<S> parentModel = this.getParentModel();
        EquipmentClientInfo.LayerType humanoidLayer = EquipmentClientInfo.LayerType.HUMANOID;

        ItemStack helmet = state.headEquipment;
        ResourceKey<EquipmentAsset> helmetAsset = getEquipmentAsset(helmet);
        if (isCustomHelmet(helmet) && helmetAsset != null) {
            HumanoidModel<S> model = helmet.is(ModItems.ETHAXIUM_HELMET.get())
                    ? ethaxiumHelmetModel
                    : helmetModel;
            setArmorPartsVisible(model, true, false, false, false);
            copyPose(parentModel, model);
            this.renderArmorLayer(model, humanoidLayer, helmetAsset, helmet, poseStack, submitNodeCollector, lightCoords, state);
        }

        ItemStack chestplate = state.chestEquipment;
        ResourceKey<EquipmentAsset> chestplateAsset = getEquipmentAsset(chestplate);
        if (isCustomChestplate(chestplate) && chestplateAsset != null) {
            HumanoidModel<S> model = chestplate.is(ModItems.ETHAXIUM_CHESTPLATE.get())
                    ? ethaxiumChestplateModel
                    : chestplateModel;
            setArmorPartsVisible(model, false, true, true, false);
            copyPose(parentModel, model);
            this.renderArmorLayer(model, humanoidLayer, chestplateAsset, chestplate, poseStack, submitNodeCollector, lightCoords, state);
        }

        ItemStack boots = state.feetEquipment;
        ResourceKey<EquipmentAsset> bootsAsset = getEquipmentAsset(boots);
        if (isCustomBoots(boots) && bootsAsset != null) {
            setArmorPartsVisible(ethaxiumBootsModel, false, false, false, true);
            copyPose(parentModel, ethaxiumBootsModel);
            this.renderArmorLayer(ethaxiumBootsModel, humanoidLayer, bootsAsset, boots, poseStack, submitNodeCollector, lightCoords, state);
        }
    }

    public static boolean isCustomHelmet(ItemStack item) {
        return item.is(ModItems.ABYSSALNITE_HELMET.get())
                || item.is(ModItems.CORALIUM_HELMET.get())
                || item.is(ModItems.DREADIUM_HELMET.get())
                || item.is(ModItems.ETHAXIUM_HELMET.get());
    }

    public static boolean isCustomChestplate(ItemStack item) {
        return item.is(ModItems.ABYSSALNITE_CHESTPLATE.get())
                || item.is(ModItems.CORALIUM_CHESTPLATE.get())
                || item.is(ModItems.DREADIUM_CHESTPLATE.get())
                || item.is(ModItems.ETHAXIUM_CHESTPLATE.get());
    }

    public static boolean isCustomBoots(ItemStack item) {
        return item.is(ModItems.ETHAXIUM_BOOTS.get());
    }

    public static boolean isEthaxiumHelmet(ItemStack item) {
        return item.is(ModItems.ETHAXIUM_HELMET.get());
    }

    private static ResourceKey<EquipmentAsset> getEquipmentAsset(ItemStack item) {
        if (item.is(ModItems.ABYSSALNITE_HELMET.get()) || item.is(ModItems.ABYSSALNITE_CHESTPLATE.get())) {
            return ModArmorMaterials.ABYSSALNITE_ASSET;
        }
        if (item.is(ModItems.CORALIUM_HELMET.get()) || item.is(ModItems.CORALIUM_CHESTPLATE.get())) {
            return ModArmorMaterials.REFINED_CORALIUM_ASSET;
        }
        if (item.is(ModItems.DREADIUM_HELMET.get()) || item.is(ModItems.DREADIUM_CHESTPLATE.get())) {
            return ModArmorMaterials.DREADIUM_ASSET;
        }
        if (item.is(ModItems.ETHAXIUM_HELMET.get())
                || item.is(ModItems.ETHAXIUM_CHESTPLATE.get())
                || item.is(ModItems.ETHAXIUM_BOOTS.get())) {
            return ModArmorMaterials.ETHAXIUM_ASSET;
        }
        return null;
    }

    private static <S extends HumanoidRenderState> void setArmorPartsVisible(
            HumanoidModel<S> model,
            boolean showHead,
            boolean showBody,
            boolean showArms,
            boolean showLegs
    ) {
        model.head.visible = showHead;
        model.body.visible = showBody;
        model.rightArm.visible = showArms;
        model.leftArm.visible = showArms;
        model.rightLeg.visible = showLegs;
        model.leftLeg.visible = showLegs;
    }

    private static <S extends HumanoidRenderState> void copyPose(
            HumanoidModel<S> source,
            HumanoidModel<S> target
    ) {
        copyRotation(source.head, target.head);
        copyRotation(source.body, target.body);
        copyRotation(source.rightArm, target.rightArm);
        copyRotation(source.leftArm, target.leftArm);
        copyRotation(source.rightLeg, target.rightLeg);
        copyRotation(source.leftLeg, target.leftLeg);
    }

    private static void copyRotation(
            net.minecraft.client.model.geom.ModelPart source,
            net.minecraft.client.model.geom.ModelPart target
    ) {
        target.xRot = source.xRot;
        target.yRot = source.yRot;
        target.zRot = source.zRot;
    }

    private void renderArmorLayer(
            HumanoidModel<S> model,
            EquipmentClientInfo.LayerType layerType,
            ResourceKey<EquipmentAsset> asset,
            ItemStack item,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            S state
    ) {
        this.equipmentRenderer.renderLayers(
                layerType,
                asset,
                model,
                state,
                item,
                poseStack,
                submitNodeCollector,
                lightCoords,
                state.outlineColor
        );
    }
}
