package net.zuperzv.abyssalcraft_reawakening.init.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.StoneRitualPedestalBlockEntity;
import org.jetbrains.annotations.Nullable;

public class StoneRitualPedestalBlock extends BaseEntityBlock {
    public static final BooleanProperty CRAFTING = BooleanProperty.create("crafting");
    public static final MapCodec<StoneRitualPedestalBlock> CODEC = simpleCodec(StoneRitualPedestalBlock::new);

    private static final VoxelShape SHAPE = Shapes.or(
            box(4, 0, 4, 12, 13, 12),

            box(11, 0, 3, 13, 14, 5),
            box(11 - 8, 0, 3, 13 - 8, 14, 5),
            box(11, 0, 3 + 8, 13, 14, 5 + 8),
            box(11 - 8, 0, 3 + 8, 13 - 8, 14, 5 + 8),

            box(3.25, -0.001, 3.25, 12.75, 1.999, 12.75),
            box(3.25, 11.01, 3.25, 12.75, 13.01, 12.75)
    );

    public StoneRitualPedestalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CRAFTING, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new StoneRitualPedestalBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(CRAFTING, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(CRAFTING);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if(level.getBlockEntity(pos) instanceof StoneRitualPedestalBlockEntity StoneRitualPedestalBlockEntity) {
            StoneRitualPedestalBlockEntity.drops();
            level.updateNeighborsAt(pos, this);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                          Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pLevel.getBlockEntity(pPos) instanceof StoneRitualPedestalBlockEntity nexus) {

            if (nexus.inventory.getStackInSlot(0).isEmpty() && !pStack.isEmpty()) {
                ItemStack toInsert = pStack.copy();
                toInsert.setCount(1);
                nexus.inventory.insertItem(0, toInsert, false);
                pStack.shrink(1);
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                return InteractionResult.SUCCESS;
            }

            else if (pStack.isEmpty() || !pStack.isEmpty() && !nexus.inventory.getStackInSlot(0).isEmpty()) {
                ItemStack extracted = nexus.inventory.extractItem(0, 1, true);

                if (!extracted.isEmpty()) {
                    boolean addedToInventory = false;

                    for (int i = 0; i < pPlayer.getInventory().getNonEquipmentItems().size(); i++) {
                        ItemStack playerStack = pPlayer.getInventory().getNonEquipmentItems().get(i);

                        if (!playerStack.isEmpty()
                                && ItemStack.isSameItem(playerStack, extracted)
                                && playerStack.getCount() < playerStack.getMaxStackSize()) {

                            playerStack.grow(1);
                            addedToInventory = true;
                            break;
                        }
                    }

                    if (!addedToInventory && pStack.isEmpty()) {
                        pPlayer.setItemInHand(InteractionHand.MAIN_HAND, extracted);
                        addedToInventory = true;
                    }

                    if (addedToInventory) {
                        nexus.clearContents();
                        nexus.inventory.extractItem(0, 1, false);
                        pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (blockEntityType != ModBlockEntities.STONE_RITUAL_PEDESTAL_BE.get()) return null;

        if (level.isClientSide()) {
            return (lvl, pos, st, be) -> {
                if (be instanceof StoneRitualPedestalBlockEntity tile) {
                    StoneRitualPedestalBlockEntity.tickClient(lvl, pos, st, tile);
                }
            };
        } else {
            return (lvl, pos, st, be) -> {
                if (be instanceof StoneRitualPedestalBlockEntity tile) {
                    StoneRitualPedestalBlockEntity.tickServer(lvl, pos, st, tile);
                }
            };
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!(level.getBlockEntity(pos) instanceof StoneRitualPedestalBlockEntity nexus)) return;

        ItemStack stack = nexus.getInputItems().getStackInSlot(0);
        if (stack.isEmpty()) return;

        nexus.getFlyingItemPosition(0.0f).ifPresent(currentPos -> {
            BlockPos altarPos = nexus.getSavedPos();
            if (altarPos == null) return;

            Vec3 targetPos = new Vec3(altarPos.getX() + 0.5, altarPos.getY() + 1.15, altarPos.getZ() + 0.5);
            Vec3 direction = targetPos.subtract(currentPos).normalize().scale(0.05);

            level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack.getItem()),
                    currentPos.x, currentPos.y, currentPos.z,
                    direction.x, direction.y + 0.10, direction.z);
        });
    }
}