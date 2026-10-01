package net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.custom.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.ModJEIRecipeTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.EssenceBoilerRecipe;
import org.jetbrains.annotations.NotNull;

public final class EssenceBoilerRecipeCategory
        implements IRecipeCategory<RecipeHolder<EssenceBoilerRecipe>> {

    private static final int WIDTH = 252;
    private static final int HEIGHT = 84;
    private static final int[] ITEM_SLOT_X = {8, 52, 96};
    private static final int[] ITEM_SLOT_Y = {22, 2, 22};
    private static final int OUTPUT_OFFSET_X = 136;

    private final mezz.jei.api.gui.drawable.IDrawable icon;
    private final IDrawableAnimated progress;

    public EssenceBoilerRecipeCategory(IGuiHelper helper) {
        icon = helper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.ESSENCE_BOILER.item().get())
        );
        Identifier arrowTexture = Identifier.fromNamespaceAndPath(
                Constants.MOD_ID,
                "textures/gui/arrow.png"
        );
        IDrawableStatic arrow = helper.drawableBuilder(arrowTexture, 0, 0, 23, 15)
                .setTextureSize(23, 15)
                .build();
        progress = helper.createAnimatedDrawable(
                arrow,
                200,
                IDrawableAnimated.StartDirection.LEFT,
                false
        );
    }

    @Override
    public @NotNull IRecipeType<RecipeHolder<EssenceBoilerRecipe>> getRecipeType() {
        return ModJEIRecipeTypes.ESSENCE_BOILER;
    }

    @Override
    public @NotNull net.minecraft.network.chat.Component getTitle() {
        return net.minecraft.network.chat.Component.translatable(
                "recipe_mods.abyssalcraft_reawakening.essence_boiler"
        );
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public mezz.jei.api.gui.drawable.IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(
            RecipeHolder<EssenceBoilerRecipe> recipe,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphicsExtractor guiGraphics,
            double mouseX,
            double mouseY
    ) {
        progress.draw(guiGraphics, 115, 34);
    }

    @Override
    public void getTooltip(
            ITooltipBuilder tooltip,
            RecipeHolder<EssenceBoilerRecipe> recipe,
            IRecipeSlotsView slots,
            double mouseX,
            double mouseY
    ) {
        if (mouseX >= 115 && mouseX < 138 && mouseY >= 34 && mouseY < 49) {
            tooltip.add(Component.translatable(
                    "recipe_mods.abyssalcraft_reawakening.time"
            ).append(Component.literal(": "
                    + recipe.value().recipeTime() / 20.0F + " s")));
        }
    }

    @Override
    public void setRecipe(
            @NotNull IRecipeLayoutBuilder builder,
            RecipeHolder<EssenceBoilerRecipe> holder,
            @NotNull IFocusGroup focuses
    ) {
        EssenceBoilerRecipe recipe = holder.value();

        for (int i = 0; i < recipe.ingredients().size(); i++) {
            builder.addSlot(
                            RecipeIngredientRole.INPUT,
                            ITEM_SLOT_X[i],
                            ITEM_SLOT_Y[i]
                    )
                    .add(recipe.ingredients().get(i));
        }

        recipe.inputFluid().ifPresent(fluid -> {
            ItemStack bucket = new ItemStack(fluid.fluid().getBucket());
            if (!bucket.is(Items.AIR)) {
                builder.addSlot(RecipeIngredientRole.INPUT, 52, 60)
                        .add(bucket)
                        .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                                net.minecraft.network.chat.Component.literal(
                                        "Input fluid: " + fluid.amount() + " mB"
                                )
                        ));
            }
        });

        for (int i = 0; i < recipe.results().size(); i++) {
            builder.addSlot(
                            RecipeIngredientRole.OUTPUT,
                            ITEM_SLOT_X[i] + OUTPUT_OFFSET_X,
                            ITEM_SLOT_Y[i]
                    )
                    .add(recipe.results().get(i).create());
        }

        recipe.outputFluid().ifPresent(fluid -> {
            ItemStack bucket = new ItemStack(fluid.fluid().getBucket());
            if (!bucket.is(Items.AIR)) {
                builder.addSlot(RecipeIngredientRole.OUTPUT, 188, 60)
                        .add(bucket)
                        .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                                net.minecraft.network.chat.Component.literal(recipe.preserveFluidAmount()
                                        ? "Output fluid: same amount as input"
                                        : "Output fluid: " + fluid.amount() + " mB")
                        ));
            }
        });
    }
}
