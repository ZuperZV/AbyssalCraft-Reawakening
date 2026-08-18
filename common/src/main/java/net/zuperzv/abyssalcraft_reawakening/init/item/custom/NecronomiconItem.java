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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.component.PotentialEnergyData;
import net.zuperzv.abyssalcraft_reawakening.init.data.tooltip.NecronomiconTooltipComponent;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.screen.NecronomiconMenu;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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

        Identifier inputId = Identifier.fromNamespaceAndPath(
                "abyssalcraft_reawakening",
                "stone_alter"
        );

        Optional<StructureTemplate> inputOptional =
                structureManager.get(inputId);

        if (inputOptional.isEmpty()) {
            return InteractionResult.PASS;
        }

        StructureTemplate inputStructure = inputOptional.get();

        BlockPos structureOrigin = findStructureOrigin(
                serverLevel,
                inputStructure,
                clickedPos
        );

        if (structureOrigin == null) {
            return InteractionResult.PASS;
        }

        Identifier outputId = Identifier.fromNamespaceAndPath(
                "abyssalcraft_reawakening",
                "stone_alter_done"
        );

        Optional<StructureTemplate> outputOptional =
                structureManager.get(outputId);

        if (outputOptional.isEmpty()) {
            return InteractionResult.PASS;
        }

        StructureTemplate outputStructure = outputOptional.get();

        StructurePlaceSettings settings =
                new StructurePlaceSettings();

        boolean placed = outputStructure.placeInWorld(
                serverLevel,
                structureOrigin,
                structureOrigin,
                settings,
                serverLevel.getRandom(),
                2
        );

        if (!placed) {
            return InteractionResult.PASS;
        }

        double centerX =
                structureOrigin.getX()
                        + outputStructure.getSize().getX() / 2.0;

        double centerY =
                structureOrigin.getY()
                        + outputStructure.getSize().getY() / 2.0;

        double centerZ =
                structureOrigin.getZ()
                        + outputStructure.getSize().getZ() / 2.0;

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
                centerX,
                structureOrigin.getY() + 1.0,
                centerZ,
                40,
                outputStructure.getSize().getX() / 3.0,
                outputStructure.getSize().getY() / 3.0,
                outputStructure.getSize().getZ() / 3.0,
                0.05
        );

        serverLevel.sendParticles(
                ParticleTypes.SMOKE,
                centerX,
                structureOrigin.getY() + 0.5,
                centerZ,
                25,
                outputStructure.getSize().getX() / 4.0,
                0.5,
                outputStructure.getSize().getZ() / 4.0,
                0.02
        );

        return InteractionResult.SUCCESS;
    }

    private boolean isStructureCorrect(
            ServerLevel level,
            StructureTemplate structure,
            BlockPos origin
    ) {
        List<StructureTemplate.StructureBlockInfo> blocks =
                getAllStructureBlocks(structure);

        if (blocks.isEmpty()) {
            return false;
        }

        for (StructureTemplate.StructureBlockInfo info : blocks) {

            BlockPos worldPos = origin.offset(info.pos());

            BlockState expectedState = info.state();
            BlockState actualState = level.getBlockState(worldPos);

            if (!actualState.equals(expectedState)) {
                return false;
            }
        }

        return true;
    }

    private BlockPos findStructureOrigin(
            ServerLevel level,
            StructureTemplate structure,
            BlockPos clickedPos
    ) {
        List<StructureTemplate.StructureBlockInfo> blocks =
                getAllStructureBlocks(structure);

        if (blocks.isEmpty()) {
            return null;
        }

        for (StructureTemplate.StructureBlockInfo info : blocks) {

            BlockPos possibleOrigin = clickedPos.offset(
                    -info.pos().getX(),
                    -info.pos().getY(),
                    -info.pos().getZ()
            );

            if (isStructureCorrect(
                    level,
                    structure,
                    possibleOrigin
            )) {
                return possibleOrigin;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private List<StructureTemplate.StructureBlockInfo> getAllStructureBlocks(
            StructureTemplate structure
    ) {
        List<StructureTemplate.StructureBlockInfo> result =
                new ArrayList<>();

        try {
            Field palettesField =
                    StructureTemplate.class.getDeclaredField("palettes");

            palettesField.setAccessible(true);

            List<StructureTemplate.Palette> palettes =
                    (List<StructureTemplate.Palette>)
                            palettesField.get(structure);

            for (StructureTemplate.Palette palette : palettes) {
                result.addAll(palette.blocks());
            }

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        return result;
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