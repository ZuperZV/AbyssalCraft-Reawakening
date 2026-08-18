package net.zuperzv.abyssalcraft_reawakening.init.api.jei.custom.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.init.api.jei.ModJEIRecipeTypes;
import net.zuperzv.abyssalcraft_reawakening.init.api.multiblock.MultiblockDisplay;
import net.zuperzv.abyssalcraft_reawakening.init.api.multiblock.MultiblockPreviewRenderer;
import org.jetbrains.annotations.NotNull;

public final class MultiblockRecipeCategory
        implements IRecipeCategory<MultiblockDisplay> {

    private final IDrawable background;
    private final IDrawable icon;

    public MultiblockRecipeCategory(
            IGuiHelper helper
    ) {
        this.background =
                helper.createBlankDrawable(
                        MultiblockPreviewRenderer.WIDTH,
                        MultiblockPreviewRenderer.HEIGHT
                );

        ItemStack iconStack =
                MultiblockDisplay.getUniqueIcons()
                        .stream()
                        .findFirst()
                        .orElse(ItemStack.EMPTY);

        this.icon =
                helper.createDrawableIngredient(
                        VanillaTypes.ITEM_STACK,
                        iconStack
                );
    }

    @Override
    public @NotNull mezz.jei.api.recipe.types.IRecipeType<MultiblockDisplay>
    getRecipeType() {
        return ModJEIRecipeTypes.MULTIBLOCK;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable(
                "api.abyssalcraft_reawakening.multiblock"
        );
    }

    @Override
    public int getWidth() {
        return MultiblockPreviewRenderer.WIDTH;
    }

    @Override
    public int getHeight() {
        return MultiblockPreviewRenderer.HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(
            MultiblockDisplay recipe,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphicsExtractor guiGraphics,
            double mouseX,
            double mouseY
    ) {
        MultiblockPreviewRenderer.render(
                guiGraphics,
                0,
                0,
                MultiblockPreviewRenderer.WIDTH,
                MultiblockPreviewRenderer.HEIGHT,
                recipe.structure(),
                mouseX,
                mouseY
        );
    }

    @Override
    public void setRecipe(
            @NotNull IRecipeLayoutBuilder builder,
            MultiblockDisplay recipe,
            @NotNull IFocusGroup focuses
    ) {
        // No ingredient slots.
        // This category displays a multiblock structure.
    }
}
