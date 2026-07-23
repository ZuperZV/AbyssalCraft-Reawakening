package net.zuperzv.abyssalcraft_reawakening.init.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.zuperzv.abyssalcraft_reawakening.init.component.EnergyData;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.component.StaffTargetData;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class StaffOfRendingItem extends Item {
    private final int Range;
    private final int MaxEnergy;

    public StaffOfRendingItem(Properties properties, int range, int maxEnergy) {
        super(properties);
        this.Range = range;
        this.MaxEnergy = maxEnergy;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(itemStack, level, owner, slot);

        if (itemStack.get(ModDataComponentTypes.ENERGY.get()) == null) {
            itemStack.set(ModDataComponentTypes.ENERGY.get(), new EnergyData(0));
        }

        EnergyData energyData = itemStack.get(ModDataComponentTypes.ENERGY.get());

        if (energyData != null && energyData.getAmount() >= 0) {
            float percentage = Math.min(1.0f, Math.max(0.0f,
                    (float) energyData.getAmount() / MaxEnergy
            ));

            int baseRed = 0x80;
            int baseGreen = 0x80;
            int baseBlue = 0x80;

            int targetRed = 0xff;
            int targetGreen = 0x3c;
            int targetBlue = 0x5b;

            int red = (int) (baseRed + (targetRed - baseRed) * percentage);
            int green = (int) (baseGreen + (targetGreen - baseGreen) * percentage);
            int blue = (int) (baseBlue + (targetBlue - baseBlue) * percentage);

            int color = (red << 16) | (green << 8) | blue;

            itemStack.set(DataComponents.DYED_COLOR, new DyedItemColor(color));
        }
    }

    private void increaseEnergy(LivingEntity entity, ItemStack stack, int amount) {
        EnergyData energyData = stack.get(ModDataComponentTypes.ENERGY.get());

        if (energyData == null) {
            energyData = new EnergyData(0);
        }

        int newAmount = Math.min(MaxEnergy, energyData.getAmount() + amount);

        energyData.setAmount(newAmount);
        stack.set(ModDataComponentTypes.ENERGY.get(), energyData);
    }

    public int getDrainAmount(ItemStack stack) {
        EnergyData energyData = stack.get(ModDataComponentTypes.ENERGY.get());

        return energyData != null ? energyData.getAmount() : 0;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        EnergyData energyData = stack.get(ModDataComponentTypes.ENERGY.get());

        Vec3 start = player.getEyePosition();
        Vec3 look = player.getViewVector(1.0F);
        Vec3 end = start.add(look.scale(Range));

        AABB box = player.getBoundingBox()
                .expandTowards(look.scale(Range))
                .inflate(2);

        LivingEntity closest = null;
        double closestDistance = Range;

        List<LivingEntity> entities = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                entity -> entity != player && entity.isAlive()
        );

        for (LivingEntity entity : entities) {
            AABB entityBox = entity.getBoundingBox().inflate(0.3);

            if (entityBox.clip(start, end).isPresent()) {
                double distance = player.distanceTo(entity);

                if (distance < closestDistance) {
                    closest = entity;
                    closestDistance = distance;
                }
            }
        }

        if (closest != null) {

            //System.out.println("looking at: " + closest.getName().getString());

            StaffTargetData data = stack.get(ModDataComponentTypes.STAFF_TARGET.get());

            if (data == null) {
                data = new StaffTargetData(new HashMap<>());
            }

            UUID id = closest.getUUID();

            int repeat = data.getRepeatCount(id);

            int base = Math.max(1, (int) closest.getHealth() / 4);
            int amount = Math.max(1, base >> repeat);

            stack.set(
                    ModDataComponentTypes.STAFF_TARGET.get(),
                    data.increase(id)
            );

            increaseEnergy(player, stack, amount);
        } else {
            //System.out.println("No mob found");
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if(itemStack.get(ModDataComponentTypes.ENERGY.get()) != null) {
            builder.accept(Component.literal("Energy: " + itemStack.get(ModDataComponentTypes.ENERGY.get()).getAmount() + " / " + MaxEnergy));
        }
    }
}
