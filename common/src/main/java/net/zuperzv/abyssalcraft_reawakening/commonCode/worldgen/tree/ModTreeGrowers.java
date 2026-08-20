package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.tree;

import net.minecraft.world.level.block.grower.TreeGrower;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.ModWorldgen;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower WITHERWOOD = new TreeGrower(Constants.MOD_ID + ":witherwood",
            Optional.empty(), Optional.of(ModWorldgen.LEAVES_WITHERWOOD_TREE), Optional.empty());
}
