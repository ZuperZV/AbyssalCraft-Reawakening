package net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.custom.subtypeInterpreter;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.CoraliumGemsData;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;

public class CoraliumGemSubtypeInterpreter implements ISubtypeInterpreter {

    @Override
    public Object getSubtypeData(Object ingredient, UidContext context) {
        if (!(ingredient instanceof ItemStack stack)) {
            return null;
        }

        CoraliumGemsData data = stack.get(ModDataComponentTypes.CORALIUM_GEMS.get());
        if (data == null) {
            return null;
        }

        return "gems:" + data.getCorealiumGems();
    }
}