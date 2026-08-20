package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModArmorMaterials;
import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;

public class ModEquipmentAssetProvider extends EquipmentAssetProvider {
    public ModEquipmentAssetProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void registerModels(@NonNull BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        output.accept(
                ModArmorMaterials.ABYSSALNITE_ASSET,
                EquipmentClientInfo.builder()
                        .addHumanoidLayers(Constants.id("abyssalnite"))
                        .build()
        );
        output.accept(
                ModArmorMaterials.REFINED_CORALIUM_ASSET,
                EquipmentClientInfo.builder()
                        .addHumanoidLayers(Constants.id("refined_coralium"))
                        .build()
        );
        output.accept(
                ModArmorMaterials.PLATED_CORALIUM_ASSET,
                EquipmentClientInfo.builder()
                        .addHumanoidLayers(Constants.id("plated_coralium"))
                        .build()
        );
        output.accept(
                ModArmorMaterials.OF_THE_DEPTHS_ASSET,
                EquipmentClientInfo.builder()
                        .addHumanoidLayers(Constants.id("of_the_depths"))
                        .build()
        );
        output.accept(
                ModArmorMaterials.DREADIUM_ASSET,
                EquipmentClientInfo.builder()
                        .addHumanoidLayers(Constants.id("dreadium"))
                        .build()
        );
        output.accept(
                ModArmorMaterials.DREADIUM_SAMURAI_ASSET,
                EquipmentClientInfo.builder()
                        .addHumanoidLayers(Constants.id("dreadium_samurai"))
                        .build()
        );
        output.accept(
                ModArmorMaterials.ETHAXIUM_ASSET,
                EquipmentClientInfo.builder()
                        .addHumanoidLayers(Constants.id("ethaxium"))
                        .build()
        );
    }
}