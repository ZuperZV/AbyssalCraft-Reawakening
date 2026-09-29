package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import com.llamalad7.mixinextras.sugar.Cancellable;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.FluidStateModelSet;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(FluidStateModelSet.class)
public abstract class FluidStateModelSetMixin {

    @Inject(
            method = "bake",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void abyssalcraft$addPotionFluidModel(
            MaterialBaker materials,
            CallbackInfoReturnable<Map<Fluid, FluidModel>> cir
    ) {
        Map<Fluid, FluidModel> models =
                new HashMap<>(cir.getReturnValue());

        FluidModel potionModel =
                new FluidModel.Unbaked(
                        new Material(
                                Identifier.fromNamespaceAndPath(
                                        "minecraft",
                                        "block/water_still"
                                )
                        ),
                        new Material(
                                Identifier.fromNamespaceAndPath(
                                        "minecraft",
                                        "block/water_flow"
                                )
                        ),
                        null,
                        null
                ).bake(
                        materials,
                        () -> "Potion"
                );

        models.put(
                ModFluids.SOURCE_POTION.get(),
                potionModel
        );

        models.put(
                ModFluids.FLOWING_POTION.get(),
                potionModel
        );

        cir.setReturnValue(
                Map.copyOf(models)
        );
    }
}