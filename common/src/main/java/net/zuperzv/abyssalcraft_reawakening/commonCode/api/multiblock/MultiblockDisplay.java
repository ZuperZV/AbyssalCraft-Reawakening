package net.zuperzv.abyssalcraft_reawakening.commonCode.api.multiblock;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.zuperzv.abyssalcraft_reawakening.Constants;

import java.util.List;

public record MultiblockDisplay(
        Identifier id,
        Identifier structure,
        ItemStack icon,
        Component title
) {
    public static final List<MultiblockDisplay> ALL = List.of(

            new MultiblockDisplay(
                    id("stone_alter"),
                    structure("stone_alter"),
                    new ItemStack(
                            Blocks.COBBLESTONE
                    ),
                    Component.translatable(
                            "api.arcane_chemistry.multiblock"
                    )
            )
    );

    private static Identifier id(
            String path
    ) {
        return Identifier.fromNamespaceAndPath(
                Constants.MOD_ID,
                path
        );
    }

    private static Identifier structure(
            String path
    ) {
        return Identifier.fromNamespaceAndPath(
                Constants.MOD_ID,
                path
        );
    }

    public static List<ItemStack> getUniqueIcons() {
        List<ItemStack> icons = new java.util.ArrayList<>();

        for (MultiblockDisplay display : ALL) {
            ItemStack icon = display.icon();

            if (icon.isEmpty()) {
                continue;
            }

            boolean alreadyExists = false;

            for (ItemStack existing : icons) {
                if (ItemStack.isSameItemSameComponents(existing, icon)) {
                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists) {
                icons.add(icon.copy());
            }
        }

        return icons;
    }
}