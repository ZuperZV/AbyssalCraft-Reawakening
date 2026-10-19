package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.PEMachineBlockEntity;
import org.jetbrains.annotations.Nullable;

public class PEMachineBlock extends BaseEntityBlock {
    public enum Kind {
        STATUE, PEDESTAL, COLLECTOR, RELAY, CONTAINER, LUNAR_JAR, SACRIFICIAL_ALTAR, DEPOSITIONER
    }

    public static final MapCodec<PEMachineBlock> CODEC = simpleCodec(properties -> new PEMachineBlock(properties, Kind.PEDESTAL));
    private final Kind kind;

    public PEMachineBlock(Properties properties, Kind kind) {
        super(properties);
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PEMachineBlockEntity(pos, state);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState();
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                           Player player, net.minecraft.world.InteractionHand hand,
                                           BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof PEMachineBlockEntity machine) {
            return machine.interact(player, stack, hand);
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (type != ModBlockEntities.PE_MACHINE_BE.get()) return null;
        return (world, pos, blockState, entity) -> {
            if (entity instanceof PEMachineBlockEntity machine) {
                PEMachineBlockEntity.tick(world, pos, blockState, machine);
            }
        };
    }
}
