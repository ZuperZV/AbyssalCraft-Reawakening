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
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.CrystalGrowthRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class CrystalGrowthRecipeCategory
        implements IRecipeCategory<RecipeHolder<CrystalGrowthRecipe>> {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 38;
    private static final int INPUT_X = 2;
    private static final int OUTPUT_X = 60;
    private static final int ITEM_Y = 0;
    private static final int FLUID_Y = 20;
    private static final int SINGLE_SLOT_Y = 10;
    private static final int ARROW_X = 23;
    private static final int ARROW_Y = 11;

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
        CrystalGrowthRecipe recipe = holder.value();
        boolean hasInputFluid = hasBucket(recipe.inputFluid());
        boolean hasOutputFluid = hasBucket(recipe.outputFluid());
        if (recipe.hasItemInput()) {
            drawSlot(graphics, INPUT_X, hasInputFluid ? ITEM_Y : SINGLE_SLOT_Y);
        }
        if (hasInputFluid) {
            drawSlot(graphics, INPUT_X, recipe.hasItemInput() ? FLUID_Y : SINGLE_SLOT_Y);
        }
        if (recipe.hasItemResult()) {
            drawSlot(graphics, OUTPUT_X, hasOutputFluid ? ITEM_Y : SINGLE_SLOT_Y);
        }
        if (hasOutputFluid) {
            drawSlot(graphics, OUTPUT_X, recipe.hasItemResult() ? FLUID_Y : SINGLE_SLOT_Y);
        }
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

        boolean hasInputFluid = hasBucket(recipe.inputFluid());
        boolean hasOutputFluid = hasBucket(recipe.outputFluid());
        if (recipe.hasItemInput()) {
            builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X,
                            hasInputFluid ? ITEM_Y : SINGLE_SLOT_Y)
                    .add(recipe.ingredients().getFirst());
        }
        if (hasInputFluid) {
            addFluidSlot(builder, recipe, recipe.inputFluid(), INPUT_X,
                    recipe.hasItemInput() ? FLUID_Y : SINGLE_SLOT_Y, false);
        }

        if (recipe.hasItemResult()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X,
                            hasOutputFluid ? ITEM_Y : SINGLE_SLOT_Y)
                    .add(recipe.result().get().create());
        }
        if (hasOutputFluid) {
            addFluidSlot(builder, recipe, recipe.outputFluid(), OUTPUT_X,
                    recipe.hasItemResult() ? FLUID_Y : SINGLE_SLOT_Y, true);
        }
    }

    private static void addFluidSlot(IRecipeLayoutBuilder builder, CrystalGrowthRecipe recipe,
                                     Optional<EssenceBoilerFluid> fluid, int x, int y, boolean output) {
        if (!hasBucket(fluid)) {
            return;
        }

        EssenceBoilerFluid value = fluid.get();
        builder.addSlot(output ? RecipeIngredientRole.OUTPUT : RecipeIngredientRole.INPUT, x, y)
                .add(new ItemStack(value.fluid().getBucket()))
                .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                        Component.literal((output ? "Output fluid: " : "Input fluid: ")
                                + (output && recipe.preserveFluidAmount()
                                ? "same amount as input"
                                : value.amount() + " mB"))));
    }

    private static boolean hasBucket(Optional<EssenceBoilerFluid> fluid) {
        return fluid.isPresent() && !fluid.get().fluid().getBucket().equals(Items.AIR);
    }
}
