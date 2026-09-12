package net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.custom.subtypeInterpreter;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.CodexTierData;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.CoraliumGemsData;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;

public class NecronomiconSubtypeInterpreter implements ISubtypeInterpreter {

    @Override
    public Object getSubtypeData(Object ingredient, UidContext context) {
        if (!(ingredient instanceof ItemStack stack)) {
            return null;
        }

        CodexTierData data = stack.get(ModDataComponentTypes.CODEX_TIER.get());
        if (data == null) {
            return null;
        }

        return "tier:" + data.getTier();
    }
}