package net.zuperzv.abyssalcraft_reawakening.init.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.zuperzv.abyssalcraft_reawakening.init.component.EnergyData;
import net.zuperzv.abyssalcraft_reawakening.init.component.EnergyEntry;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.component.StaffTargetData;
import net.zuperzv.abyssalcraft_reawakening.init.data.EnergyType;
import net.zuperzv.abyssalcraft_reawakening.init.data.tooltip.StaffTooltipComponent;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

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

        EnergyData energyData = itemStack.get(ModDataComponentTypes.ENERGY.get());

        if (energyData == null) {
            energyData = EnergyData.createEmpty();
            itemStack.set(ModDataComponentTypes.ENERGY.get(), energyData);
        }

        EnergyEntry shadow = energyData.get(EnergyType.SHADOW);
        EnergyEntry coralium = energyData.get(EnergyType.CORALIUM);
        EnergyEntry dread = energyData.get(EnergyType.DREAD);
        EnergyEntry omothol = energyData.get(EnergyType.OMOTHOL);

        int maxAmount = Math.max(
                Math.max(shadow.getAmount(), coralium.getAmount()),
                Math.max(dread.getAmount(), omothol.getAmount())
        );

        float percentage = Math.min(
                1.0f,
                Math.max(0.0f, maxAmount / (float) MaxEnergy)
        );

        int baseRed = 0x80;
        int baseGreen = 0x80;
        int baseBlue = 0x80;

        int targetRed = 0xff;
        int targetGreen = 0x3c;
        int targetBlue = 0x5b;


        int red = (int)(baseRed + (targetRed - baseRed) * percentage);
        int green = (int)(baseGreen + (targetGreen - baseGreen) * percentage);
        int blue = (int)(baseBlue + (targetBlue - baseBlue) * percentage);


        int color = (red << 16) | (green << 8) | blue;

        itemStack.set(
                DataComponents.DYED_COLOR,
                new DyedItemColor(color)
        );
    }

    private void increaseEnergy(ItemStack stack, EnergyType type, int amount) {
        EnergyData energyData =
                stack.get(ModDataComponentTypes.ENERGY.get());

        if (energyData == null) {
            energyData = EnergyData.createEmpty();
        }

        EnergyEntry energy =
                energyData.get(type);

        int newAmount = Math.min(
                MaxEnergy,
                energy.getAmount() + amount
        );

        energy.setAmount(newAmount);

        stack.set(
                ModDataComponentTypes.ENERGY.get(),
                energyData
        );
    }

    public int getMaxEnergy() {
        return MaxEnergy;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int remainingUseDuration) {
        if (!(living instanceof Player player))
            return;

        if (level.isClientSide())
            return;

        if (remainingUseDuration % 20 != 0)
            return;

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

            if (entityBox.clip(start, end).isPresent() && entity.isAlive() && entity.getType() != EntityType.PLAYER) {
                double distance = player.distanceTo(entity);

                if (distance < closestDistance ) {
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

            EnergyType type = EnergyType.SHADOW;
            ResourceKey<Level> dim = player.level().dimension();

            if (dim.equals(Level.OVERWORLD)) {
                type = EnergyType.CORALIUM;

            } else if (dim.equals(Level.NETHER)) {
                type = EnergyType.OMOTHOL;

            } else if (dim.equals(Level.END)) {
                type = EnergyType.DREAD;
            }

            int damage = Math.min(5, Math.max(1, amount / 2));

            closest.hurt(level.damageSources().playerAttack(player), damage);

            increaseEnergy(stack, type, amount);
        }

        EnergyData energyData = stack.get(ModDataComponentTypes.ENERGY.get());
        if (energyData != null) {
            EnergyEntry shadow = energyData.get(EnergyType.SHADOW);
            EnergyEntry coralium = energyData.get(EnergyType.CORALIUM);
            EnergyEntry dread = energyData.get(EnergyType.DREAD);
            EnergyEntry omothol = energyData.get(EnergyType.OMOTHOL);

            EnergyEntry maxEnergyEntry = Stream.of(shadow, coralium, dread, omothol)
                    .max(Comparator.comparingInt(EnergyEntry::getAmount))
                    .orElse(null);

            if (energyData.get(maxEnergyEntry.getType()).getAmount() >= MaxEnergy) {
                level.playLocalSound(
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.TOTEM_USE,
                        SoundSource.PLAYERS,
                        0.5f,
                        0.3f,
                        false
                );

                ItemStack reward = new ItemStack(ModItems.SHADOW_GEM.get());

                if (maxEnergyEntry.getType() == EnergyType.SHADOW) {
                    reward = new ItemStack(ModItems.SHADOW_GEM.get());

                } else if (maxEnergyEntry.getType() == EnergyType.CORALIUM) {
                    reward = new ItemStack(ModItems.ABYSSAL_WASTELAND_ESSENCE.get());

                } else if (maxEnergyEntry.getType() == EnergyType.DREAD) {
                    reward = new ItemStack(ModItems.DREADLANDS_ESSENCE.get());

                } else if (maxEnergyEntry.getType() == EnergyType.OMOTHOL) {
                    reward = new ItemStack(ModItems.OMOTHOL_ESSENCE.get());
                }

                if (!player.addItem(reward)) {
                    player.drop(reward, false);
                }

                maxEnergyEntry.setAmount(0);
            }
        }

        return;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {

        EnergyData energy =
                stack.get(ModDataComponentTypes.ENERGY.get());

        return Optional.of(
                new StaffTooltipComponent(
                        energy,
                        stack
                )
        );
    }
}
