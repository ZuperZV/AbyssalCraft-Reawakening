package net.zuperzv.abyssalcraft_reawakening.init.api.jei.custom.category;

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

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.api.jei.custom.ModJEIRecipeTypes;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.component.PotentialEnergyData;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.StoneRitualAltarRecipe;

import net.zuperzv.abyssalcraft_reawakening.init.recipe.helper.TimeOfDay;
import org.jetbrains.annotations.NotNull;


public class RitualAltarRecipeCategory implements IRecipeCategory<RecipeHolder<StoneRitualAltarRecipe>> {
    public static final Identifier SLOT_TEX =
            Identifier.fromNamespaceAndPath(
                    Constants.MOD_ID,
                    "textures/gui/magic_slot.png"
            );

    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableStatic dayIcon;
    private final IDrawableStatic nightIcon;
    private final IDrawableStatic bothIcon;

    private final IDrawableAnimated progress;

    private final IDrawableStatic slotDrawable;

    private final int width = 115 + 16;
    private final int height = 55 + 16;

    int slotSize = 16;

    int centerX = 28;
    int centerY = height / 2 - 7;

    private static final int[][] SLOT_POSITIONS = new int[][]{
            {-19 - 8, 0},   // venstre
            {0, -19 - 8},   // op
            {19 + 8, 0},    // højre
            {0, 19 + 8},    // ned
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

        this.dayIcon =
                helper.drawableBuilder(
                                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/icon_day.png"),
                                0,
                                0,
                                16,
                                16
                        )
                        .setTextureSize(16,16)
                        .build();

        this.nightIcon =
                helper.drawableBuilder(
                                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/icon_night.png"),
                                0,
                                0,
                                16,
                                16
                        )
                        .setTextureSize(16,16)
                        .build();

        this.bothIcon =
                helper.drawableBuilder(
                                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/icon_both.png"),
                                0,
                                0,
                                16,
                                16
                        )
                        .setTextureSize(16,16)
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
    public void getTooltip(
            ITooltipBuilder tooltip,
            RecipeHolder<StoneRitualAltarRecipe> recipe,
            IRecipeSlotsView slots,
            double mouseX,
            double mouseY
    ) {
        //Arrow
        if (isMouseOver(mouseX, mouseY, 79, centerY, 23, 15)) {
            tooltip.add(
                    Component.translatable("recipe_mods.abyssalcraft_reawakening.time")
                            .append(Component.literal(": " + recipe.value().getRecipeTime() / 20 + "s"))
            );
        }

        //Time of day
        int iconX = width / 2 + 4;
        int iconY = 3;

        if (recipe.value().timeOfDay().get() != TimeOfDay.BOTH) {
            if (isMouseOver(mouseX, mouseY, iconX, iconY, 16, 16)) {
                tooltip.add(
                        Component.translatable("recipe_mods.abyssalcraft_reawakening.works_at")
                                .append(Component.literal(": ")
                                        .append(Component.translatable("recipe_mods.abyssalcraft_reawakening." + recipe.value().timeOfDay().get().name().toLowerCase())))
                );
            }

            iconX += 19;
        }

        //Dimension
        if (recipe.value().dimension().isPresent()) {
            if (isMouseOver(mouseX, mouseY, iconX, iconY, 16, 16)) {
                tooltip.add(
                        Component.translatable("recipe_mods.abyssalcraft_reawakening.works_in_dimension")
                                .append(Component.literal(": " +
                                        formatDimensionName(recipe.value().dimension().get().toString())
                                ))
                );
            }

            iconX += 19;
        }
    }

    private String formatDimensionName(String id) {
        if (id.contains(":")) {
            int colon = id.lastIndexOf(':');
            if (colon != -1) {
                id = id.substring(colon + 1);
            }
        }

        if (id.contains("/")) {
            id = id.substring(id.lastIndexOf("/") + 1).trim();
        }

        id = id.replace("_", " ");
        id = id.replace("]", "");

        StringBuilder result = new StringBuilder();
        for (String word : id.split(" ")) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return result.toString().trim();
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
                width - 4 - slotSize,
                centerY-1
        );

        //Necronomicon
        slotDrawable.draw(
                guiGraphics,
                width / 2 + 3,
                height - 5 - slotSize
        );

        //Arrow
        progress.draw(
                guiGraphics,
                79,
                centerY
        );


        // Time of Day
        int iconX = width / 2 + 3;
        int iconY = 3;

        if (recipe.timeOfDay().get() != TimeOfDay.BOTH) {
            slotDrawable.draw(
                    guiGraphics,
                    iconX,
                    iconY
            );

            switch (recipe.timeOfDay().get()) {
                case DAY -> dayIcon.draw(guiGraphics, iconX + 1, iconY + 1);
                case NIGHT -> nightIcon.draw(guiGraphics, iconX + 1, iconY + 1);
                case BOTH -> bothIcon.draw(guiGraphics, iconX + 1, iconY + 1); // Not in use
            }

            iconX += 19;
        }

        //Dimension
        if (recipe.dimension().isPresent()) {
            slotDrawable.draw(
                    guiGraphics,
                    iconX,
                    iconY
            );

            iconX += 19;
        }
    }


    @Override
    public void setRecipe(
            @NotNull IRecipeLayoutBuilder builder,
            RecipeHolder<StoneRitualAltarRecipe> holder,
            @NotNull IFocusGroup focuses
    ) {

        StoneRitualAltarRecipe recipe = holder.value();

        //Necronomicon
        ItemStack necronomicon = ModItems.NECRONOMICON.get().getDefaultInstance();
        necronomicon.set(
                ModDataComponentTypes.POTENTIAL_ENERGY.get(),
                new PotentialEnergyData(recipe.potentialEnergy())
        );
        builder.addSlot(
                RecipeIngredientRole.INPUT,
                width / 2 + 4,
                height - 4 - slotSize
        ).add(necronomicon);

        int iconX = width / 2 + 4;
        int iconY = 4;

        if (recipe.timeOfDay().get() != TimeOfDay.BOTH) {
            iconX += 19;
        }

        // Dimension
        if (recipe.dimension().isPresent()) {
            ItemStack dimensionIcon = ItemStack.EMPTY;

            var dimension = recipe.dimension().get();

            if (dimension == Level.OVERWORLD) {
                dimensionIcon = new ItemStack(Blocks.GRASS_BLOCK);
            } else if (dimension == Level.NETHER) {
                dimensionIcon = new ItemStack(Blocks.NETHERRACK);
            } else if (dimension == Level.END) {
                dimensionIcon = new ItemStack(Blocks.END_STONE);
            }

            if (!dimensionIcon.isEmpty()) {
                builder.addSlot(
                        RecipeIngredientRole.RENDER_ONLY,
                                iconX,
                                iconY
                        )
                        .add(dimensionIcon)
                        .addRichTooltipCallback((view, tooltip) -> tooltip.clear())
                        .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                                Component.translatable("recipe_mods.abyssalcraft_reawakening.works_in_dimension")
                                        .append(Component.literal(": " +
                                                formatDimensionName(recipe.dimension().get().toString())
                                        ))
                        ));
            }

            iconX += 19;
        }

        //Inputs
        builder.addSlot(
                        RecipeIngredientRole.INPUT,
                        centerX,
                        centerY
                )
                .add(recipe.moldIngredient());

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

        builder.addSlot(
                RecipeIngredientRole.OUTPUT,
                width - 3 - slotSize,
                centerY
        ).add(recipe.output().create());
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    private static boolean isMouseOver(
            double mouseX,
            double mouseY,
            int x,
            int y,
            int width,
            int height
    ) {
        return mouseX >= x
                && mouseX < x + width
                && mouseY >= y
                && mouseY < y + height;
    }
}