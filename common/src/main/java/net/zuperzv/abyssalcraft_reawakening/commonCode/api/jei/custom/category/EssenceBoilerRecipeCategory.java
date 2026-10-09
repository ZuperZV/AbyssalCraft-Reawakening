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
import net.minecraft.core.component.DataComponents;
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

    private static final int WIDTH = 105;
    private static final int HEIGHT = 50;
    private static final int INPUT_CENTER_X = 29;
    private static final int OUTPUT_CENTER_X = 89;
    private static final int ITEM_TOP_Y = 4;
    private static final int ITEM_SIDE_Y = 22 - (16/2);
    private static final int ITEM_SIDE_OFFSET = 19;
    private static final int FLUID_INPUT_X = INPUT_CENTER_X - 8;
    private static final int FLUID_INPUT_Y = 40 - (16/2) - 2;
    private static final int FLUID_OUTPUT_X = OUTPUT_CENTER_X - 8;
    private static int FLUID_OUTPUT_Y = FLUID_INPUT_Y;
    private static final int ARROW_X = 42;
    private static int ARROW_Y = 1;

    private final mezz.jei.api.gui.drawable.IDrawable icon;
    private final IDrawableAnimated progress;
    private final IDrawableStatic slotDrawable;

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
        Identifier slotTexture = Identifier.fromNamespaceAndPath(
                Constants.MOD_ID,
                "textures/gui/magic_slot.png"
        );
        slotDrawable = helper.drawableBuilder(slotTexture, 0, 0, 18, 18)
                .setTextureSize(18, 18)
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
        EssenceBoilerRecipe value = recipe.value();

        int arrowY = getArrowY(value);
        int outputFluidY = getOutputFluidY(value);

        for (int i = 0; i < value.ingredients().size(); i++) {
            drawSlot(
                    guiGraphics,
                    itemSlotX(INPUT_CENTER_X, i),
                    itemSlotY(i)
            );
        }

        for (int i = 0; i < value.results().size(); i++) {
            drawSlot(
                    guiGraphics,
                    itemSlotX(OUTPUT_CENTER_X, i),
                    outputItemSlotY(value, i)
            );
        }

        value.inputFluid().ifPresent(fluid -> {
            if (!fluid.fluid().getBucket().equals(Items.AIR)) {
                drawSlot(
                        guiGraphics,
                        FLUID_INPUT_X,
                        FLUID_INPUT_Y
                );
            }
        });

        value.outputFluid().ifPresent(fluid -> {
            if (!fluid.fluid().getBucket().equals(Items.AIR)) {
                drawSlot(
                        guiGraphics,
                        FLUID_OUTPUT_X,
                        outputFluidY
                );
            }
        });

        progress.draw(guiGraphics, ARROW_X, arrowY);
    }

    private static int getArrowY(EssenceBoilerRecipe recipe) {
        if (recipe.ingredients().size() >= 3) {
            return 1;
        }

        return HEIGHT / 2 - 22 / 2 + 4;
    }

    private static int getOutputFluidY(EssenceBoilerRecipe recipe) {
        if (!recipe.results().isEmpty()) {
            return FLUID_INPUT_Y;
        }

        return HEIGHT / 2 - 16 / 2 - 2;
    }

    @Override
    public void getTooltip(
            ITooltipBuilder tooltip,
            RecipeHolder<EssenceBoilerRecipe> recipe,
            IRecipeSlotsView slots,
            double mouseX,
            double mouseY
    ) {
        int arrowY = getArrowY(recipe.value());

        if (mouseX >= ARROW_X && mouseX < ARROW_X + 23
                && mouseY >= arrowY && mouseY < arrowY + 15) {

            tooltip.add(Component.translatable(
                    "recipe_mods.abyssalcraft_reawakening.time"
            ).append(Component.literal(": "
                    + recipe.value().recipeTime() / 20.0F + " s")));
        }
    }

    private void drawSlot(GuiGraphicsExtractor guiGraphics, int x, int y) {
        slotDrawable.draw(guiGraphics, x - 1, y - 1);
      }

    private static int itemSlotX(int centerX, int index) {
        return switch (index) {
            case 0 -> centerX - 8;
            case 1 -> centerX - ITEM_SIDE_OFFSET - 8;
            case 2 -> centerX + ITEM_SIDE_OFFSET - 8;
            default -> throw new IndexOutOfBoundsException("Boiler recipes support at most three item slots");
        };
    }

    private static int outputItemSlotY(EssenceBoilerRecipe recipe, int index) {
        if (recipe.outputFluid().isEmpty()) {
            return HEIGHT / 2 - 8;
        }

        return itemSlotY(index);
    }

    private static int itemSlotY(int index) {
        return index == 0 ? ITEM_TOP_Y : ITEM_SIDE_Y;
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
                            itemSlotX(INPUT_CENTER_X, i),
                            itemSlotY(i)
                    )
                    .add(recipe.ingredients().get(i));
        }

        recipe.inputFluid().ifPresent(fluid -> {
            ItemStack bucket = createFluidItem(fluid);
            if (!bucket.is(Items.AIR)) {
                builder.addSlot(RecipeIngredientRole.INPUT, FLUID_INPUT_X, FLUID_INPUT_Y)
                        .add(bucket)
                        .addRichTooltipCallback((view, tooltip) -> {
                            tooltip.add(Component.literal("Input fluid: " + fluid.amount() + " mB"));
                            if (fluid.hasPotionContents()) {
                                tooltip.add(Component.translatable(
                                        "tooltip.abyssalcraft_reawakening.potion",
                                        fluid.potionContents().getName("item.minecraft.potion.effect.")));
                            }
                        });
            }
        });

        for (int i = 0; i < recipe.results().size(); i++) {
            builder.addSlot(
                            RecipeIngredientRole.OUTPUT,
                            itemSlotX(OUTPUT_CENTER_X, i),
                            outputItemSlotY(recipe, i)
                    )
                    .add(recipe.results().get(i).create());
        }

        recipe.outputFluid().ifPresent(fluid -> {
            ItemStack bucket = createFluidItem(fluid);
            if (!bucket.is(Items.AIR)) {
                builder.addSlot(RecipeIngredientRole.OUTPUT, FLUID_OUTPUT_X, FLUID_OUTPUT_Y)
                        .add(bucket)
                        .addRichTooltipCallback((view, tooltip) -> {
                            tooltip.add(Component.literal(recipe.preserveFluidAmount()
                                    ? "Output fluid: same amount as input"
                                    : "Output fluid: " + fluid.amount() + " mB"));
                            if (fluid.hasPotionContents()) {
                                tooltip.add(Component.translatable(
                                        "tooltip.abyssalcraft_reawakening.potion",
                                        fluid.potionContents().getName("item.minecraft.potion.effect.")));
                            }
                        });
            }
        });
    }

    private static ItemStack createFluidItem(net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid fluid) {
        ItemStack stack = new ItemStack(fluid.fluid().getBucket());
        if (fluid.hasPotionContents()) {
            stack.set(DataComponents.POTION_CONTENTS, fluid.potionContents());
        }
        return stack;
    }
}
