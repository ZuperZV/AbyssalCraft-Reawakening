package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HumanoidArmorLayer.class)
public interface HumanoidArmorLayerAccessor {

    @Accessor("equipmentRenderer")
    EquipmentLayerRenderer abyssalcraft$getEquipmentRenderer();
}
