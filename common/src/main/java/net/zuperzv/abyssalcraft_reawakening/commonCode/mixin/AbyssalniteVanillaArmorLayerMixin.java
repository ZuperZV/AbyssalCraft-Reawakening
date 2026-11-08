package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class AbyssalniteVanillaArmorLayerMixin {

    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void abyssalcraft$skipVanillaAbyssalniteArmor(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            ItemStack item,
            EquipmentSlot slot,
            int lightCoords,
            HumanoidRenderState state,
            CallbackInfo ci
    ) {
        boolean abyssalniteChestplate = item.is(ModItems.ABYSSALNITE_CHESTPLATE.get());
        boolean abyssalniteHelmet = item.is(ModItems.ABYSSALNITE_HELMET.get())
                && !(state instanceof ArmorStandRenderState);
        if (abyssalniteChestplate || abyssalniteHelmet) {
            ci.cancel();
        }
    }
}
