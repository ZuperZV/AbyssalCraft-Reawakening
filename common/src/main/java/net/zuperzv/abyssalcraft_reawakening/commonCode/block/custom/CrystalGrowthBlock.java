package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.CrystalGrowthBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.services.EssenceBoilerPlatformAccess;
import org.jetbrains.annotations.Nullable;

public class CrystalGrowthBlock extends BaseEntityBlock {
    public static final MapCodec<CrystalGrowthBlock> CODEC = simpleCodec(CrystalGrowthBlock::new);
    private static final VoxelShape SHAPE = Shapes.or(
            box(1, 0, 1, 15, 2, 15),
            box(1, 2, 1, 3, 10, 13),
            box(13, 2, 1, 15, 10, 15),
            box(3, 2, 1, 13, 10, 3),
            box(1, 2, 13, 13, 10, 15),
            box(0, 9, 14, 14, 12, 16),
            box(14, 9, 0, 16, 12, 16),
            box(2, 9, 0, 14, 12, 2),
            box(0, 9, 0, 2, 12, 14)
    );

    public CrystalGrowthBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level,
                                  BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrystalGrowthBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type
    ) {
        if (level.isClientSide()) {
            return null;
        }
        return (world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof CrystalGrowthBlockEntity growth) {
                CrystalGrowthBlockEntity.tick(world, pos, blockState, growth);
            }
        };
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (blockEntity instanceof CrystalGrowthBlockEntity growth) {
            growth.drops();
        }
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                           BlockPos pos, Player player, InteractionHand hand,
                                           BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (!(level.getBlockEntity(pos) instanceof CrystalGrowthBlockEntity growth)) {
            return InteractionResult.PASS;
        }

        if (!stack.isEmpty()
                && EssenceBoilerPlatformAccess.get().tryEmptyFluidContainer(growth, player, hand)) {
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        if (!stack.isEmpty()
                && EssenceBoilerPlatformAccess.get().tryFillFluidContainer(growth, player, hand)) {
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        if (stack.isEmpty()) {
            return takeStoredItem(growth, level, pos, player, hand);
        }
        if (growth.inventory.getStackInSlot(CrystalGrowthBlockEntity.SLOT_ITEM).isEmpty()
                && growth.inventory.insertItem(CrystalGrowthBlockEntity.SLOT_ITEM,
                stack.copyWithCount(1), false).isEmpty()) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.7F, 1.15F);
            return InteractionResult.SUCCESS;
        }
        return takeStoredItem(growth, level, pos, player, hand);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (!(level.getBlockEntity(pos) instanceof CrystalGrowthBlockEntity growth)) {
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown() && !growth.getFluidTank().isEmpty()) {
            growth.drainFluidTank(growth.getFluidTankAmount());
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 0.8F, 0.85F);
            return InteractionResult.SUCCESS;
        }

        return takeStoredItem(growth, level, pos, player, InteractionHand.MAIN_HAND);
    }

    private InteractionResult takeStoredItem(
            CrystalGrowthBlockEntity growth,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand
    ) {
        int visibleSlot = growth.getGrowingResultSlot();
        if (visibleSlot < 0) {
            return InteractionResult.PASS;
        }

        ItemStack stored = growth.inventory.getStackInSlot(visibleSlot);
        ItemStack held = player.getItemInHand(hand);
        if (!held.isEmpty()
                && ItemStack.isSameItemSameComponents(held, stored)
                && held.getCount() < held.getMaxStackSize()) {
            held.grow(1);
            growth.inventory.extractItem(visibleSlot, 1, false);
        } else {
            ItemStack taken = growth.inventory.extractItem(visibleSlot, 1, false);
            if (held.isEmpty()) {
                player.setItemInHand(hand, taken);
            } else if (!player.getInventory().add(taken)) {
                player.drop(taken, false);
            }
        }
        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.7F, 1.0F);
        return InteractionResult.SUCCESS;
    }
}
