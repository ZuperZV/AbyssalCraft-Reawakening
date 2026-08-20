package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.StoneRitualAltarBlockEntity;
import org.jetbrains.annotations.Nullable;

public class StoneRitualAltarBlock extends BaseEntityBlock {
    public static final MapCodec<StoneRitualAltarBlock> CODEC = simpleCodec(StoneRitualAltarBlock::new);
    public static BooleanProperty CRAFTING = BooleanProperty.create("crafting");
    public static BooleanProperty DONE = BooleanProperty.create("done");

    private static final VoxelShape SHAPE = Shapes.or(
            // Bottom
            box(2, 0, 2, 14, 2, 14),

            // Center
            box(4, 2, 4, 12, 10, 12),

            // Top
            box(2, 10, 2, 14, 13, 14),

            // Top corner blocks
            box(12, 13, 2, 14, 14, 4),
            box(12, 12, 12, 14, 14, 14),
            box(2, 13, 12, 4, 14, 14),
            box(2, 13, 2, 4, 14, 4),

            // Small details
            box(12, 9, 2, 14, 10, 4),
            box(12, 9, 12, 14, 10, 14),
            box(2, 9, 2, 4, 10, 4),
            box(2, 9, 12, 4, 10, 14),

            // Four outer pillars
            box(11, 0, 3, 13, 10, 5),
            box(3, 0, 3, 5, 10, 5),
            box(11, 0, 11, 13, 10, 13),
            box(3, 0, 11, 5, 10, 13)
    );

    public StoneRitualAltarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CRAFTING, false).setValue(DONE, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(CRAFTING, false)
                .setValue(DONE, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(CRAFTING);
        pBuilder.add(DONE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState pState) {
        return new StoneRitualAltarBlockEntity(pos, pState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
            if(level.getBlockEntity(pos) instanceof StoneRitualAltarBlockEntity StoneRitualAltarBlockEntity) {
                StoneRitualAltarBlockEntity.drops();
                level.updateNeighborsAt(pos, this);
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;

        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof StoneRitualAltarBlockEntity tile) {
                StoneRitualAltarBlockEntity.tick(level, pos, state, tile);
            }
        };
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof StoneRitualAltarBlockEntity altar) {

            if (altar.inventory.getStackInSlot(0).isEmpty() && !itemStack.isEmpty()) {

                ItemStack toInsert = itemStack.copy();
                toInsert.setCount(1);

                ItemStack remaining = altar.inventory.insertItem(0, toInsert, false);

                if (remaining.isEmpty()) {
                    itemStack.shrink(1);
                }

                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                return InteractionResult.SUCCESS;
            }

            else if (itemStack.isEmpty() || !itemStack.isEmpty() && !altar.inventory.getStackInSlot(0).isEmpty()) {

                int amount = altar.inventory.getStackInSlot(0).getCount();

                ItemStack extracted = altar.inventory.extractItem(0, amount, true);

                if (!extracted.isEmpty()) {

                    boolean addedToInventory = false;

                    for (int i = 0; i < player.getInventory().getNonEquipmentItems().size(); i++) {
                        ItemStack playerStack = player.getInventory().getNonEquipmentItems().get(i);

                        if (!playerStack.isEmpty()
                                && ItemStack.isSameItem(playerStack, extracted)
                                && playerStack.getCount() < playerStack.getMaxStackSize()) {

                            playerStack.grow(amount);
                            addedToInventory = true;
                            break;
                        }
                    }

                    if (!addedToInventory && itemStack.isEmpty()) {
                        ItemStack handStack = extracted.copy();
                        player.setItemInHand(InteractionHand.MAIN_HAND, handStack);
                        addedToInventory = true;
                    }

                    if (addedToInventory) {
                        altar.clearContents();
                        altar.inventory.extractItem(0, amount, false);

                        level.playSound(player, pos,
                                SoundEvents.ITEM_PICKUP,
                                SoundSource.BLOCKS,
                                1f, 1f);
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double xPos = (double)pos.getX() + 0.5;
        double yPos = pos.getY() + 1.2;
        double zPos = (double)pos.getZ() + 0.5;
        if (random.nextDouble() < 0.1) {
            level.playLocalSound(xPos, yPos, zPos, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            if(level.getBlockEntity(pos) instanceof StoneRitualAltarBlockEntity StoneRitualAltarBlockEntity && !StoneRitualAltarBlockEntity.getInputItems().getStackInSlot(0).isEmpty()) {
                level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, StoneRitualAltarBlockEntity.getInputItems().getStackInSlot(0).getItem()),
                        xPos, yPos, zPos , 0.0, 0.0, 0.0);
            }
        }
    }
}