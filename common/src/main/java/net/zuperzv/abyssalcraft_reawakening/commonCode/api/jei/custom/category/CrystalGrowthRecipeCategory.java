package net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.custom.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
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
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.CrystalGrowthRecipe;
import org.jetbrains.annotations.NotNull;

public final class CrystalGrowthRecipeCategory
        implements IRecipeCategory<RecipeHolder<CrystalGrowthRecipe>> {
    private static final int WIDTH = 146;
    private static final int HEIGHT = 64;
    private static final int INPUT_X = 20;
    private static final int OUTPUT_X = 118;
    private static final int FIRST_INPUT_Y = 5;
    private static final int SECOND_INPUT_Y = 25;
    private static final int FLUID_X = 20;
    private static final int FLUID_Y = 45;
    private static final int ARROW_X = 65;
    private static final int ARROW_Y = 23;

    private final IDrawable icon;
    private final IDrawableAnimated progress;
    private final IDrawableStatic slot;

    public CrystalGrowthRecipeCategory(IGuiHelper helper) {
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.CRYSTAL_GROWTH_CHAMBER.item().get()));
        Identifier arrowTexture = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/arrow.png");
        IDrawableStatic arrow = helper.drawableBuilder(arrowTexture, 0, 0, 23, 15)
                .setTextureSize(23, 15).build();
        Identifier slotTexture = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/magic_slot.png");
        slot = helper.drawableBuilder(slotTexture, 0, 0, 18, 18).setTextureSize(18, 18).build();
        progress = helper.createAnimatedDrawable(arrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public @NotNull IRecipeType<RecipeHolder<CrystalGrowthRecipe>> getRecipeType() {
        return ModJEIRecipeTypes.CRYSTAL_GROWTH;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe_mods.abyssalcraft_reawakening.crystal_growth");
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
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(RecipeHolder<CrystalGrowthRecipe> holder, IRecipeSlotsView slots,
                     GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        drawSlot(graphics, INPUT_X, FIRST_INPUT_Y);
        if (holder.value().ingredients().size() > 1) {
            drawSlot(graphics, INPUT_X, SECOND_INPUT_Y);
        }
        if (!holder.value().inputFluid().fluid().getBucket().equals(Items.AIR)) {
            drawSlot(graphics, FLUID_X, FLUID_Y);
        }
        drawSlot(graphics, OUTPUT_X, 21);
        progress.draw(graphics, ARROW_X, ARROW_Y);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<CrystalGrowthRecipe> recipe,
                           IRecipeSlotsView slots, double mouseX, double mouseY) {
        if (mouseX >= ARROW_X && mouseX < ARROW_X + 23
                && mouseY >= ARROW_Y && mouseY < ARROW_Y + 15) {
            tooltip.add(Component.translatable("recipe_mods.abyssalcraft_reawakening.time")
                    .append(Component.literal(": " + recipe.value().time() / 20.0F + " s")));
        }
    }

    private void drawSlot(GuiGraphicsExtractor graphics, int x, int y) {
        slot.draw(graphics, x - 1, y - 1);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CrystalGrowthRecipe> holder,
                          IFocusGroup focuses) {
        CrystalGrowthRecipe recipe = holder.value();
        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, FIRST_INPUT_Y)
                .add(recipe.ingredients().getFirst());
        if (recipe.ingredients().size() > 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, SECOND_INPUT_Y)
                    .add(recipe.ingredients().get(1));
        }

        ItemStack bucket = new ItemStack(recipe.inputFluid().fluid().getBucket());
        if (!bucket.is(Items.AIR)) {
            builder.addSlot(RecipeIngredientRole.INPUT, FLUID_X, FLUID_Y)
                    .add(bucket)
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                            Component.literal("Input fluid: " + recipe.inputFluid().amount() + " mB")));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, 21)
                .add(recipe.result().create());
    }
}
