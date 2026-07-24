package net.zuperzv.abyssalcraft_reawakening.init.data.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.init.component.EnergyData;

public record StaffTooltipComponent(
        EnergyData energy,
        ItemStack item
) implements TooltipComponent {

}