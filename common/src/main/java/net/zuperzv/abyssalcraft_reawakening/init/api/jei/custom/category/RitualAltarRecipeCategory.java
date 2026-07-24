package net.zuperzv.abyssalcraft_reawakening.init.api.jei.custom.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.api.jei.custom.ModJEIRecipeTypes;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.StoneRitualAltarRecipe;

import org.jetbrains.annotations.NotNull;


public class RitualAltarRecipeCategory implements IRecipeCategory<RecipeHolder<StoneRitualAltarRecipe>> {
    public static final Identifier SLOT_TEX =
            Identifier.fromNamespaceAndPath(
                    Constants.MOD_ID,
                    "textures/gui/magic_slot.png"
            );

    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableAnimated progress;

    private final IDrawableStatic slotDrawable;

    private final int width = 115;
    private final int height = 55;

    int centerX = 20;
    int centerY = height / 2 - 7;

    private static final int[][] SLOT_POSITIONS = new int[][]{
            {-19, 0},   // venstre
            {0, -19},   // op
            {19, 0},    // højre
            {0, 19},    // ned
            {-19, -19}, // øverste venstre hjørne
            {19, -19},  // øverste højre hjørne
            {19, 19},   // nederste højre hjørne
            {-19, 19}   // nederste venstre hjørne
    };

    public RitualAltarRecipeCategory(IGuiHelper helper) {

        Identifier ARROW =
                Identifier.fromNamespaceAndPath(
                        Constants.MOD_ID,
                        "textures/gui/arrow.png"
                );

        this.background =
                helper.createBlankDrawable(width,height);

        this.icon =
                helper.createDrawableIngredient(
                        VanillaTypes.ITEM_STACK,
                        new ItemStack(ModBlocks.STONE_RITUAL_ALTAR.item().get())
                );

        IDrawableStatic progressDrawable =
                helper.drawableBuilder(
                                ARROW,
                                0,
                                0,
                                23,
                                15
                        )
                        .setTextureSize(23,15)
                        .build();

        this.progress =
                helper.createAnimatedDrawable(
                        progressDrawable,
                        200,
                        IDrawableAnimated.StartDirection.LEFT,
                        false
                );

        this.slotDrawable =
                helper.drawableBuilder(
                                SLOT_TEX,
                                0,
                                0,
                                18,
                                18
                        )
                        .setTextureSize(18,18)
                        .build();

    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public IRecipeType<RecipeHolder<StoneRitualAltarRecipe>> getRecipeType() {
        return ModJEIRecipeTypes.RITUAL_ALTAR;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(
                "recipe_mods.abyssalcraft_reawakening.ritual_altar"
        );
    }

    @Override
    public void draw(
            RecipeHolder<StoneRitualAltarRecipe> holder,
            IRecipeSlotsView recipeSlotsView,
            net.minecraft.client.gui.GuiGraphicsExtractor guiGraphics,
            double mouseX,
            double mouseY
    ) {
        StoneRitualAltarRecipe recipe = holder.value();

        slotDrawable.draw(
                guiGraphics,
                centerX - 1,
                centerY - 1
        );

        for(int i = 1; i < recipe.additionalIngredients().size(); i++) {
            if(i > SLOT_POSITIONS.length)
                break;

            int offsetX =
                    SLOT_POSITIONS[i-1][0];

            int offsetY =
                    SLOT_POSITIONS[i-1][1];

            slotDrawable.draw(
                    guiGraphics,
                    centerX + offsetX -1,
                    centerY + offsetY -1
            );
        }

        slotDrawable.draw(
                guiGraphics,
                97-1,
                centerY-1
        );

        progress.draw(
                guiGraphics,
                63,
                centerY
        );
    }


    @Override
    public void setRecipe(
            @NotNull IRecipeLayoutBuilder builder,
            RecipeHolder<StoneRitualAltarRecipe> holder,
            @NotNull IFocusGroup focuses
    ) {

        StoneRitualAltarRecipe recipe = holder.value();

        // Main ingredient (center)
        builder.addSlot(
                        RecipeIngredientRole.INPUT,
                        centerX,
                        centerY
                )
                .add(recipe.moldIngredient());

        // Pedestal ingredients
        for (int i = 0; i < recipe.additionalIngredients().size(); i++) {

            if (i >= SLOT_POSITIONS.length)
                break;

            int offsetX = SLOT_POSITIONS[i][0];
            int offsetY = SLOT_POSITIONS[i][1];

            builder.addSlot(
                    RecipeIngredientRole.INPUT,
                    centerX + offsetX,
                    centerY + offsetY
            ).add(recipe.additionalIngredients().get(i));
        }

        // Output
        builder.addSlot(
                RecipeIngredientRole.OUTPUT,
                97,
                centerY
        ).add(recipe.output().create());
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }
}