package net.zuperzv.abyssalcraft_reawakening.commonCode.api.multiblock;

import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class MultiblockJeiCompat {

    private static IJeiRuntime runtime;

    private MultiblockJeiCompat() {
    }

    public static void setRuntime(
            IJeiRuntime runtime
    ) {
        MultiblockJeiCompat.runtime = runtime;
    }

    public static boolean showRecipes(ItemStack stack) {
        if (runtime == null || stack.isEmpty()) {
            return false;
        }

        Optional<IIngredientType<ItemStack>> optionalType =
                runtime.getIngredientManager()
                        .getIngredientTypeChecked(ItemStack.class);

        if (optionalType.isEmpty()) {
            return false;
        }

        IIngredientType<ItemStack> itemType = optionalType.get();

        IFocusFactory focusFactory =
                runtime.getJeiHelpers().getFocusFactory();

        IFocus<ItemStack> focus =
                focusFactory.createFocus(
                        RecipeIngredientRole.OUTPUT,
                        itemType,
                        stack
                );

        runtime.getRecipesGui().show(focus);

        return true;
    }
}