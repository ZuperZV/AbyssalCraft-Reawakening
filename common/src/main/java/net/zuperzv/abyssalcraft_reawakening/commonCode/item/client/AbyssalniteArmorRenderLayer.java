package net.zuperzv.abyssalcraft_reawakening.commonCode.item.client;

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
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModArmorMaterials;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.commonCode.mixin.HumanoidArmorLayerAccessor;
import net.zuperzv.abyssalcraft_reawakening.commonCode.mixin.LivingEntityRendererAccessor;

public final class AbyssalniteArmorRenderLayer<
        S extends HumanoidRenderState,
        M extends HumanoidModel<S>
        > extends RenderLayer<S, M> {

    private final AbyssalniteArmorModel<S> helmetModel = createArmorModel(false, true, false, false, false);
    private final AbyssalniteArmorModel<S> chestplateModel = createArmorModel(false, false, true, true, false);
    private final AbyssalniteArmorModel<S> babyHelmetModel = createArmorModel(true, true, false, false, false);
    private final AbyssalniteArmorModel<S> babyChestplateModel = createArmorModel(true, false, true, true, false);
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
            boolean baby,
            boolean showHead,
            boolean showBody,
            boolean showArms,
            boolean showLegs
    ) {
        var layer = AbyssalniteArmorModel.createBodyLayer();
        if (baby) {
            layer = layer.apply(HumanoidModel.BABY_TRANSFORMER);
        }
        AbyssalniteArmorModel<S> model = new AbyssalniteArmorModel<>(layer.bakeRoot());
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
            ItemStack chestplate = state.chestEquipment;
            if (chestplate.is(ModItems.ABYSSALNITE_CHESTPLATE.get()) && this.equipmentRenderer != null) {
                AbyssalniteArmorModel<S> armorStandChestplateModel = this.chestplateModel;
                armorStandChestplateModel.setArmorPartsVisible(false, true, true, false);
                armorStandChestplateModel.setArmCubesVisible(true);
                armorStandChestplateModel.copyArmorStandPoseFrom(this.getParentModel());
                this.renderArmorLayer(
                        armorStandChestplateModel,
                        EquipmentClientInfo.LayerType.HUMANOID,
                        chestplate,
                        poseStack,
                        submitNodeCollector,
                        lightCoords,
                        state
                );
            }
            return;
        }

        if (this.equipmentRenderer == null) {
            return;
        }

        HumanoidModel<S> parentModel = this.getParentModel();
        AbyssalniteArmorModel<S> helmetModel = state.isBaby ? this.babyHelmetModel : this.helmetModel;
        AbyssalniteArmorModel<S> chestplateModel = state.isBaby ? this.babyChestplateModel : this.chestplateModel;
        EquipmentClientInfo.LayerType humanoidLayer = state.isBaby
                ? EquipmentClientInfo.LayerType.HUMANOID_BABY
                : EquipmentClientInfo.LayerType.HUMANOID;

        ItemStack helmet = state.headEquipment;
        if (helmet.is(ModItems.ABYSSALNITE_HELMET.get())) {
            helmetModel.setArmorPartsVisible(true, false, false, false);
            helmetModel.copyPoseFrom(parentModel);
            this.renderArmorLayer(helmetModel, humanoidLayer, helmet, poseStack, submitNodeCollector, lightCoords, state);
        }

        ItemStack chestplate = state.chestEquipment;
        if (chestplate.is(ModItems.ABYSSALNITE_CHESTPLATE.get())) {
            chestplateModel.setArmorPartsVisible(false, true, true, false);
            chestplateModel.copyPoseFrom(parentModel);
            this.renderArmorLayer(chestplateModel, humanoidLayer, chestplate, poseStack, submitNodeCollector, lightCoords, state);
        }
    }

    private void renderArmorLayer(
            HumanoidModel<S> model,
            EquipmentClientInfo.LayerType layerType,
            ItemStack item,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            S state
    ) {
        this.equipmentRenderer.renderLayers(
                layerType,
                ModArmorMaterials.ABYSSALNITE_ASSET,
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
