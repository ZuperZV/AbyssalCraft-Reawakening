package net.zuperzv.abyssalcraft_reawakening.commonCode.api.multiblock;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record MultiblockDisplay(
        Identifier id,
        Identifier structure,
        Optional<Identifier> secondStructure,
        ItemStack icon,
        Component title,
        int switchTimeSec
) {

    public static final List<MultiblockDisplay> ALL = List.of(

            new MultiblockDisplay(
                    id("stone_alter"),
                    structure("stone_alter"),
                    Optional.of(
                            structure("stone_alter_done")
                    ),
                    new ItemStack(
                            ModItems.NECRONOMICON.get()
                    ),
                    Component.translatable(
                            "api.arcane_chemistry.multiblock"
                    ),
                    3
            ),

            new MultiblockDisplay(
                    id("test"),
                    structure("test"),
                    Optional.empty(),
                    new ItemStack(
                            ModBlocks.ABYSSAL_STONE.block().get()
                    ),
                    Component.translatable(
                            "api.arcane_chemistry.multiblock"
                    ),
                    5
            ),
            new MultiblockDisplay(
                    id("test2"),
                    structure("test2"),
                    Optional.empty(),
                    new ItemStack(
                            Blocks.IRON_BLOCK
                    ),
                    Component.translatable(
                            "api.arcane_chemistry.multiblock"
                    ),
                    6
            )
    );

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(
                Constants.MOD_ID,
                path
        );
    }

    private static Identifier structure(String path) {
        return Identifier.fromNamespaceAndPath(
                Constants.MOD_ID,
                path
        );
    }

    public static List<ItemStack> getUniqueIcons() {
        List<ItemStack> icons = new ArrayList<>();

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