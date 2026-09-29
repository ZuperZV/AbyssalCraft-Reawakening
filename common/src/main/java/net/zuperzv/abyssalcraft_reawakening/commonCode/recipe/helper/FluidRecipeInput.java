package net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.helper;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.EssenceBoilerBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;

public final class FluidRecipeInput implements RecipeInput {
    private final SimpleContainer inventory;
    private final EssenceBoilerFluid fluid;

    public FluidRecipeInput(
            SimpleContainer inventory,
            EssenceBoilerFluid fluid
    ) {
        this.inventory = inventory;
        this.fluid = fluid == null
                ? EssenceBoilerFluid.EMPTY
                : fluid;
    }

    @Override
    public ItemStack getItem(int index) {
        return inventory.getItem(index).copy();
    }

    @Override
    public int size() {
        return inventory.getContainerSize();
    }

    public EssenceBoilerFluid fluid() {
        return fluid;
    }
}
