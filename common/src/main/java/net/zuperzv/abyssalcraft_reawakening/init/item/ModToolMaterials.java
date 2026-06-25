package net.zuperzv.abyssalcraft_reawakening.init.item;

import net.minecraft.world.item.ToolMaterial;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlockTags;

public final class ModToolMaterials {
    private ModToolMaterials() {
    }

    public static final ToolMaterial ABYSSALNITE =
            new ToolMaterial(
                    ModBlockTags.INCORRECT_FOR_ABYSSALNITE_TOOL, //Ignore
                    1300,
                    10.0F,
                    4.0F,
                    15,
                    ModItemTags.ABYSSALNITE_MATERIALS //repairItems
            );

    public static final ToolMaterial REFINED_CORALIUM =
            new ToolMaterial(
                    ModBlockTags.INCORRECT_FOR_REFINED_CORALIUM_TOOL, //Ignore
                    1800,
                    12.0F,
                    5.0F,
                    13,
                    ModItemTags.REFINED_CORALIUM_MATERIALS //repairItems
            );

    public static final ToolMaterial DREADIUM =
            new ToolMaterial(
                    ModBlockTags.INCORRECT_FOR_DREADIUM_TOOL, //Ignore
                    2300,
                    14.0F,
                    6.0F,
                    14,
                    ModItemTags.DREADIUM_MATERIALS //repairItems
            );

    public static final ToolMaterial ETHAXIUM =
            new ToolMaterial(
                    ModBlockTags.INCORRECT_FOR_ETHAXIUM_TOOL, //Ignore
                    2800,
                    16.0F,
                    8.0F,
                    20,
                    ModItemTags.ETHAXIUM_MATERIALS //repairItems
            );
}