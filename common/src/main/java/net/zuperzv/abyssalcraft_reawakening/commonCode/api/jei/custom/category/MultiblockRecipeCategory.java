package net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.custom.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.ModJEIRecipeTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.api.multiblock.MultiblockDisplay;
import net.zuperzv.abyssalcraft_reawakening.commonCode.api.multiblock.MultiblockPreviewRenderer;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.PotentialEnergyData;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import org.jetbrains.annotations.NotNull;

public final class MultiblockRecipeCategory
        implements IRecipeCategory<MultiblockDisplay> {

    private final IDrawable background;
    private final IDrawable icon;

    int slotSize = 16;

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
        Identifier structureId = recipe.structure();
        Identifier secondStructureId =
                recipe.secondStructure().orElse(null);

        int switchTimeSec = recipe.switchTimeSec();

        int switchTicks = Math.max(1, switchTimeSec * 20);

        long time = System.currentTimeMillis() / 50L;

        Identifier structureToRender = structureId;

        if (secondStructureId != null) {
            long cycleLength = (long) switchTicks * 2;
            long cycleTime = time % cycleLength;

            if (cycleTime >= switchTicks) {
                structureToRender = secondStructureId;
            }
        }

        MultiblockPreviewRenderer.render(
                guiGraphics,
                0,
                0,
                MultiblockPreviewRenderer.WIDTH,
                MultiblockPreviewRenderer.HEIGHT,
                structureToRender,
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

        //Icon
        ItemStack icon = recipe.icon();

        if (!icon.isEmpty()) {
            builder.addSlot(
                    RecipeIngredientRole.INPUT,
                    MultiblockPreviewRenderer.WIDTH / 16 - 6,
                    MultiblockPreviewRenderer.HEIGHT / 4 - 6 - slotSize
            ).add(icon);
        }
    }
}
