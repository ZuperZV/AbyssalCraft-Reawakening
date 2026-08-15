package net.zuperzv.abyssalcraft_reawakening.init.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.component.PotentialEnergyData;
import net.zuperzv.abyssalcraft_reawakening.init.data.tooltip.NecronomiconTooltipComponent;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.screen.NecronomiconMenu;

import java.util.Optional;

public class NecronomiconItem extends Item {

    public NecronomiconItem(Properties properties) {
        super(properties);
    }

    /*
    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(itemStack, level, owner, slot);

        System.out.println("Potential Energy: " + itemStack.get(ModDataComponentTypes.POTENTIAL_ENERGY.get()).getPotentialEnergy());
    }
     */

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        }

        BlockPos clickedPos = context.getClickedPos();

        StructureTemplateManager structureManager =
                serverLevel.getStructureManager();

        Identifier structureId = Identifier.fromNamespaceAndPath(
                "abyssalcraft_reawakening",
                "stone_alter_done"
        );

        Optional<StructureTemplate> optional =
                structureManager.get(structureId);

        if (optional.isEmpty()) {
            return InteractionResult.PASS;
        }

        StructureTemplate structure = optional.get();

        StructurePlaceSettings settings =
                new StructurePlaceSettings();

        BlockPos placementPos = clickedPos.offset(
                -structure.getSize().getX() / 2,
                -structure.getSize().getY() / 2,
                -structure.getSize().getZ() / 2
        );

        boolean placed = structure.placeInWorld(
                serverLevel,
                placementPos,
                placementPos,
                settings,
                serverLevel.getRandom(),
                2
        );

        if (!placed) {
            return InteractionResult.PASS;
        }

        double centerX = placementPos.getX() + structure.getSize().getX() / 2.0;
        double centerY = placementPos.getY() + structure.getSize().getY() / 2.0;
        double centerZ = placementPos.getZ() + structure.getSize().getZ() / 2.0;

        serverLevel.playSound(
                null,
                centerX,
                centerY,
                centerZ,
                SoundEvents.SOUL_ESCAPE,
                SoundSource.BLOCKS,
                1.0F,
                0.6F
        );

        serverLevel.sendParticles(
                ParticleTypes.SOUL,
                placementPos.getX() + structure.getSize().getX() / 2.0,
                placementPos.getY() + 1.0,
                placementPos.getZ() + structure.getSize().getZ() / 2.0,
                40,
                structure.getSize().getX() / 3.0,
                structure.getSize().getY() / 3.0,
                structure.getSize().getZ() / 3.0,
                0.05
        );

        serverLevel.sendParticles(
                ParticleTypes.SMOKE,
                placementPos.getX() + structure.getSize().getX() / 2.0,
                placementPos.getY() + 0.5,
                placementPos.getZ() + structure.getSize().getZ() / 2.0,
                25,
                structure.getSize().getX() / 4.0,
                0.5,
                structure.getSize().getZ() / 4.0,
                0.02
        );

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        initializeEnergy(stack);

        if (player.isShiftKeyDown()) {
            increaseEnergy(stack, 10);

            return level.isClientSide()
                    ? InteractionResult.SUCCESS
                    : InteractionResult.CONSUME;
        }

        if (!level.isClientSide()) {
            player.openMenu(this.getMenuProvider(stack));
        }

        return level.isClientSide()
                ? InteractionResult.SUCCESS
                : InteractionResult.CONSUME;
    }

    private MenuProvider getMenuProvider(ItemStack itemStack) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return itemStack.getHoverName();
            }

            @Override
            public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new NecronomiconMenu(i, inventory);
            }
        };
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        initializeEnergy(stack);

        PotentialEnergyData pe =
                stack.get(ModDataComponentTypes.POTENTIAL_ENERGY.get());

        //System.out.println("Potential Energy getTooltipImage: " + pe.getPotentialEnergy());

        return Optional.of(
                new NecronomiconTooltipComponent(
                        pe,
                        stack
                )
        );
    }

    private void increaseEnergy(ItemStack stack, int amount) {
        PotentialEnergyData energyData =
                stack.get(ModDataComponentTypes.POTENTIAL_ENERGY.get());

        if (energyData == null) {
            energyData = PotentialEnergyData.createEmpty();
            stack.set(ModDataComponentTypes.POTENTIAL_ENERGY.get(), energyData);
        }

        int max = getMaxPotentialEnergy(stack);

        int newAmount = Math.min(
                energyData.getPotentialEnergy() + amount,
                max
        );

        if (newAmount > max) newAmount = max;

        energyData.setPotentialEnergy(newAmount);

        stack.set(
                ModDataComponentTypes.POTENTIAL_ENERGY.get(),
                energyData
        );
    }

    public static int getMaxPotentialEnergy(ItemStack stack) {
        var tier = stack.get(
                ModDataComponentTypes.CODEX_TIER.get()
        );

        if (tier == null) {
            return 5000;
        }

        return getMaxPotentialEnergy(tier.getTier());
    }

    public static int getMaxPotentialEnergy(int tier) {
        return switch (tier) {
            //case 1 -> 5000;
            case 2 -> 10000;
            case 3 -> 20000;
            case 4 -> 40000;
            case 5 -> 100000;
            default -> 5000;
        };
    }

    private void initializeEnergy(ItemStack stack) {
        if (!stack.has(ModDataComponentTypes.POTENTIAL_ENERGY.get())) {
            stack.set(
                    ModDataComponentTypes.POTENTIAL_ENERGY.get(),
                    new PotentialEnergyData(0)
            );
        }
    }
}