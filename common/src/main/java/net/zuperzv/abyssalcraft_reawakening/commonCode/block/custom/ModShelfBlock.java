package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.UseEffects;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.ModShelfBlockEntity;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

public class ModShelfBlock extends BaseEntityBlock implements SelectableSlotContainer, SideChainPartBlock, SimpleWaterloggedBlock {
    public static final MapCodec<ModShelfBlock> CODEC = simpleCodec(ModShelfBlock::new);
    public static final BooleanProperty POWERED;
    public static final EnumProperty<Direction> FACING;
    public static final EnumProperty<SideChainPart> SIDE_CHAIN_PART;
    public static final BooleanProperty WATERLOGGED;
    private static final Map<Direction, VoxelShape> SHAPES;

    public MapCodec<ModShelfBlock> codec() {
        return CODEC;
    }

    public ModShelfBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(POWERED, false)).setValue(SIDE_CHAIN_PART, SideChainPart.UNCONNECTED)).setValue(WATERLOGGED, false));
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (VoxelShape)SHAPES.get(state.getValue(FACING));
    }

    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return type == PathComputationType.WATER && state.getFluidState().is(FluidTags.WATER);
    }

    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new ModShelfBlockEntity(worldPosition, blockState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, POWERED, SIDE_CHAIN_PART, WATERLOGGED});
    }

    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
        this.updateNeighborsAfterPoweringDown(level, pos, state);
    }

    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide()) {
            boolean signal = level.hasNeighborSignal(pos);
            if ((Boolean)state.getValue(POWERED) != signal) {
                BlockState newState = (BlockState)state.setValue(POWERED, signal);
                if (!signal) {
                    newState = (BlockState)newState.setValue(SIDE_CHAIN_PART, SideChainPart.UNCONNECTED);
                }

                level.setBlock(pos, newState, 3);
                this.playSound(level, pos, signal ? SoundEvents.SHELF_ACTIVATE : SoundEvents.SHELF_DEACTIVATE);
                level.gameEvent(signal ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos, GameEvent.Context.of(newState));
            }
        }

    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
        return (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()))).setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER));
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }

    public int getRows() {
        return 1;
    }

    public int getColumns() {
        return 3;
    }

    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof ModShelfBlockEntity shelfBlockEntity) {

            if (!hand.equals(InteractionHand.OFF_HAND)) {

                OptionalInt hitSlot = this.getHitSlot(
                        hitResult,
                        state.getValue(FACING)
                );

                if (hitSlot.isEmpty()) {
                    return InteractionResult.PASS;
                }

                Inventory inventory = player.getInventory();

                if (level.isClientSide()) {
                    return inventory.getSelectedItem().isEmpty()
                            ? InteractionResult.PASS
                            : InteractionResult.SUCCESS;
                }

                if (!state.getValue(POWERED)) {

                    boolean itemRemoved = swapSingleItem(
                            itemStack,
                            player,
                            shelfBlockEntity,
                            hitSlot.getAsInt(),
                            inventory
                    );

                    if (itemRemoved) {
                        playSound(
                                level,
                                pos,
                                itemStack.isEmpty()
                                        ? SoundEvents.SHELF_TAKE_ITEM
                                        : SoundEvents.SHELF_SINGLE_SWAP
                        );
                    }
                    else {
                        if (itemStack.isEmpty()) {
                            return InteractionResult.PASS;
                        }

                        playSound(level, pos, SoundEvents.SHELF_PLACE_ITEM);
                    }

                    return InteractionResult.SUCCESS;
                }

                ItemStack previousItem = inventory.getSelectedItem();

                boolean anySwapped = swapHotbar(level, pos, inventory);

                if (!anySwapped) {
                    return InteractionResult.CONSUME;
                }

                playSound(level, pos, SoundEvents.SHELF_MULTI_SWAP);

                return previousItem == inventory.getSelectedItem()
                        ? InteractionResult.SUCCESS
                        : InteractionResult.SUCCESS.heldItemTransformedTo(
                        inventory.getSelectedItem()
                );
            }
        }

        return InteractionResult.PASS;
    }

    private static boolean swapSingleItem(ItemStack itemStack, Player player, @UnknownNullability ModShelfBlockEntity shelfBlockEntity, int hitSlot, Inventory inventory) {
        ItemStack removedItem = shelfBlockEntity.swapItemNoUpdate(hitSlot, itemStack);
        ItemStack newInventoryItem = player.hasInfiniteMaterials() && removedItem.isEmpty() ? itemStack.copy() : removedItem;
        inventory.setItem(inventory.getSelectedSlot(), newInventoryItem);
        inventory.setChanged();
        shelfBlockEntity.setChanged(newInventoryItem.has(DataComponents.USE_EFFECTS) && !((UseEffects)newInventoryItem.get(DataComponents.USE_EFFECTS)).interactVibrations() ? null : GameEvent.ITEM_INTERACT_FINISH);
        return !removedItem.isEmpty();
    }

    private boolean swapHotbar(Level level, BlockPos pos, Inventory inventory) {
        List<BlockPos> connectedBlocks = getAllBlocksConnectedTo(level, pos);

        if (connectedBlocks.isEmpty()) {
            return false;
        }

        boolean anySwapped = false;

        for (int shelfPartIndex = 0; shelfPartIndex < connectedBlocks.size(); shelfPartIndex++) {

            BlockEntity be = level.getBlockEntity(
                    connectedBlocks.get(shelfPartIndex)
            );

            if (!(be instanceof ModShelfBlockEntity shelfPart)) {
                continue;
            }

            for (int slot = 0; slot < shelfPart.getContainerSize(); slot++) {

                int inventorySlot =
                        9 - (connectedBlocks.size() - shelfPartIndex)
                                * shelfPart.getContainerSize()
                                + slot;

                if (inventorySlot >= 0 &&
                        inventorySlot <= inventory.getContainerSize()) {

                    ItemStack placedInventoryItem =
                            inventory.removeItemNoUpdate(inventorySlot);

                    ItemStack removedShelfItem =
                            shelfPart.swapItemNoUpdate(
                                    slot,
                                    placedInventoryItem
                            );

                    if (!placedInventoryItem.isEmpty()
                            || !removedShelfItem.isEmpty()) {

                        inventory.setItem(
                                inventorySlot,
                                removedShelfItem
                        );

                        anySwapped = true;
                    }
                }
            }
            inventory.setChanged();
            shelfPart.setChanged(GameEvent.ENTITY_INTERACT);
        }

        return anySwapped;
    }

    public SideChainPart getSideChainPart(BlockState state) {
        return (SideChainPart)state.getValue(SIDE_CHAIN_PART);
    }

    public BlockState setSideChainPart(BlockState state, SideChainPart newPart) {
        return (BlockState)state.setValue(SIDE_CHAIN_PART, newPart);
    }

    public Direction getFacing(BlockState state) {
        return (Direction)state.getValue(FACING);
    }

    public boolean isConnectable(BlockState state) {
        return state.is(BlockTags.WOODEN_SHELVES) && state.hasProperty(POWERED) && (Boolean)state.getValue(POWERED);
    }

    public int getMaxChainLength() {
        return 3;
    }

    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if ((Boolean)state.getValue(POWERED)) {
            this.updateSelfAndNeighborsOnPoweringUp(level, pos, state, oldState);
        } else {
            this.updateNeighborsAfterPoweringDown(level, pos, state);
        }

    }

    private void playSound(LevelAccessor level, BlockPos pos, SoundEvent sound) {
        level.playSound((Entity)null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    protected FluidState getFluidState(BlockState state) {
        return (Boolean)state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if ((Boolean)state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        if (level.isClientSide()) {
            return 0;
        } else if (direction != ((Direction)state.getValue(FACING)).getOpposite()) {
            return 0;
        } else {
            BlockEntity var6 = level.getBlockEntity(pos);
            if (var6 instanceof ModShelfBlockEntity) {
                ModShelfBlockEntity blockEntity = (ModShelfBlockEntity)var6;
                int item1Bit = blockEntity.getItem(0).isEmpty() ? 0 : 1;
                int item2Bit = blockEntity.getItem(1).isEmpty() ? 0 : 1;
                int item3Bit = blockEntity.getItem(2).isEmpty() ? 0 : 1;
                return item1Bit | item2Bit << 1 | item3Bit << 2;
            } else {
                return 0;
            }
        }
    }

    static {
        POWERED = BlockStateProperties.POWERED;
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        SIDE_CHAIN_PART = BlockStateProperties.SIDE_CHAIN_PART;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        SHAPES = Shapes.rotateHorizontal(Shapes.or(Block.box((double)0.0F, (double)12.0F, (double)11.0F, (double)16.0F, (double)16.0F, (double)13.0F), new VoxelShape[]{Block.box((double)0.0F, (double)0.0F, (double)13.0F, (double)16.0F, (double)16.0F, (double)16.0F), Block.box((double)0.0F, (double)0.0F, (double)11.0F, (double)16.0F, (double)4.0F, (double)13.0F)}));
    }
}
