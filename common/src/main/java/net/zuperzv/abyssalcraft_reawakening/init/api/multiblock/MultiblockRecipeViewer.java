package net.zuperzv.abyssalcraft_reawakening.init.api.multiblock;

import net.minecraft.world.item.ItemStack;

public final class MultiblockRecipeViewer {

    private MultiblockRecipeViewer() {
    }

    public static void showRecipes(
            ItemStack stack
    ) {
        if (stack == null || stack.isEmpty()) {
            return;
        }

        /*
        if (isEmiLoaded()) {
            if (showEmi(stack)) {
                return;
            }
        }
         */

        MultiblockJeiCompat.showRecipes(
                stack
        );
    }

    /*
    private static boolean isEmiLoaded() {
        return ModList
                .get()
                .isLoaded("emi");
    }

    private static boolean showEmi(
            ItemStack stack
    ) {
        try {
            Class<?> emiApi =
                    Class.forName(
                            "dev.emi.emi.api.EmiApi"
                    );

            Class<?> emiStack =
                    Class.forName(
                            "dev.emi.emi.api.stack.EmiStack"
                    );

            Method of =
                    emiStack.getMethod(
                            "of",
                            ItemStack.class
                    );

            Object emiIngredient =
                    of.invoke(
                            null,
                            stack
                    );

            Method displayRecipes =
                    emiApi.getMethod(
                            "displayRecipes",
                            emiIngredient.getClass()
                    );

            displayRecipes.invoke(
                    null,
                    emiIngredient
            );

            return true;

        } catch (
                ReflectiveOperationException |
                RuntimeException exception
        ) {
            return false;
        }
    }
     */
}