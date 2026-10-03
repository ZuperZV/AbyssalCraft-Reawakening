package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.EssenceBoilerBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.BoilerTipBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class BoilerTipBlock extends Block implements EntityBlock {
    public static final MapCodec<BoilerTipBlock> CODEC = simpleCodec(BoilerTipBlock::new);
    public static final int MAX_ATTACHMENTS = 4;

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH_ON = BooleanProperty.create("north_on");
    public static final BooleanProperty SOUTH_ON = BooleanProperty.create("south_on");
    public static final BooleanProperty EAST_ON = BooleanProperty.create("east_on");
    public static final BooleanProperty WEST_ON = BooleanProperty.create("west_on");
    public static final BooleanProperty UP_ON = BooleanProperty.create("up_on");
    public static final BooleanProperty DOWN_ON = BooleanProperty.create("down_on");

    private static final Map<Direction, BooleanProperty> ATTACHMENTS = new EnumMap<>(Direction.class);
    private static final Map<Direction, BooleanProperty> CONNECTED = new EnumMap<>(Direction.class);
    private static final VoxelShape SHAPE_NORTH = Shapes.or(
            box(5.5, 10, 14, 6.5, 13, 17),
            box(9.5, 10, 14, 10.5, 13, 17),
            box(6.5, 10, 14, 9.5, 11, 17)
    );

    static {
        ATTACHMENTS.put(Direction.NORTH, NORTH);
        ATTACHMENTS.put(Direction.SOUTH, SOUTH);
        ATTACHMENTS.put(Direction.EAST, EAST);
        ATTACHMENTS.put(Direction.WEST, WEST);
        ATTACHMENTS.put(Direction.UP, UP);
        ATTACHMENTS.put(Direction.DOWN, DOWN);
        CONNECTED.put(Direction.NORTH, NORTH_ON);
        CONNECTED.put(Direction.SOUTH, SOUTH_ON);
        CONNECTED.put(Direction.EAST, EAST_ON);
        CONNECTED.put(Direction.WEST, WEST_ON);
        CONNECTED.put(Direction.UP, UP_ON);
        CONNECTED.put(Direction.DOWN, DOWN_ON);
    }

    public BoilerTipBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(NORTH, false).setValue(SOUTH, false)
                .setValue(EAST, false).setValue(WEST, false)
                .setValue(UP, false).setValue(DOWN, false)
                .setValue(NORTH_ON, false).setValue(SOUTH_ON, false)
                .setValue(EAST_ON, false).setValue(WEST_ON, false)
                .setValue(UP_ON, false).setValue(DOWN_ON, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public static BooleanProperty attachmentProperty(Direction direction) {
        return ATTACHMENTS.get(direction);
    }

    public static BooleanProperty connectedProperty(Direction direction) {
        return CONNECTED.get(direction);
    }

    public static boolean hasAttachment(BlockState state, Direction direction) {
        return state.getValue(attachmentProperty(direction));
    }

    public static int attachmentCount(BlockState state) {
        int count = 0;
        for (Direction direction : Direction.values()) {
            if (hasAttachment(state, direction)) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        VoxelShape shape = Shapes.empty();
        for (Direction direction : Direction.values()) {
            if (hasAttachment(state, direction)) {
                shape = Shapes.or(shape, rotateShape(Direction.SOUTH, direction.getOpposite(), SHAPE_NORTH));
            }
        }
        return shape;
    }

    @Override
    protected VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return getShape(state, level, pos, context);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        return toggleTargetedAttachment(state, level, pos, player, hitResult);
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack itemStack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        if (itemStack.is(ModBlocks.BOILER_TIP.item().get())) {
            return InteractionResult.PASS;
        }
        return toggleTargetedAttachment(state, level, pos, player, hitResult);
    }

    private InteractionResult toggleTargetedAttachment(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        Direction direction = getTargetedAttachment(state, pos, hitResult.getLocation());
        if (direction == null) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (!(level.getBlockEntity(pos) instanceof BoilerTipBlockEntity tip)) {
            return InteractionResult.PASS;
        }
        if (!tip.isExtracting(direction)) {
            BlockPos sourcePos = pos.relative(direction.getOpposite());
            if (!state.getValue(connectedProperty(direction))
                    || tip.findBoilerBelow(level, pos) == null
                    || !(level.getBlockEntity(sourcePos) instanceof EssenceBoilerBlockEntity source)
                    || source.getTank().isEmpty()) {
                return InteractionResult.FAIL;
            }
        }

        tip.setExtracting(direction, !tip.isExtracting(direction));
        return InteractionResult.SUCCESS;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if (!context.getItemInHand().is(ModBlocks.BOILER_TIP.item().get())) {
            return super.canBeReplaced(state, context);
        }
        return attachmentCount(state) < MAX_ATTACHMENTS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos clickedPos = context.getClickedPos();
        BlockPos pos = context.getLevel().getBlockState(clickedPos).is(this)
                ? clickedPos
                : clickedPos.relative(direction);
        BlockPos sourcePos = pos.relative(direction.getOpposite());
        boolean connected = context.getLevel().getBlockState(sourcePos)
                .is(ModBlocks.ESSENCE_BOILER.block().get());
        BlockState state = context.getLevel().getBlockState(pos);
        if (state.is(this)) {
            if (attachmentCount(state) >= MAX_ATTACHMENTS) {
                return null;
            }
            if (hasAttachment(state, direction)) {
                return null;
            }
        } else {
            state = defaultBlockState();
        }
        return state.setValue(attachmentProperty(direction), true)
                .setValue(connectedProperty(direction), connected);
    }

    @Override
    protected void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block block,
            @Nullable net.minecraft.world.level.redstone.Orientation orientation,
            boolean movedByPiston
    ) {
        if (level.isClientSide()) {
            return;
        }

        BlockState updatedState = state;
        BoilerTipBlockEntity tip = level.getBlockEntity(pos) instanceof BoilerTipBlockEntity blockEntity
                ? blockEntity
                : null;
        for (Direction direction : Direction.values()) {
            if (!hasAttachment(state, direction)) {
                continue;
            }
            BlockPos sourcePos = pos.relative(direction.getOpposite());
            boolean connected = level.getBlockState(sourcePos).is(ModBlocks.ESSENCE_BOILER.block().get());
            if (updatedState.getValue(connectedProperty(direction)) != connected) {
                updatedState = updatedState.setValue(connectedProperty(direction), connected);
            }
            if (!connected && tip != null) {
                tip.clearConnection(direction);
            }
        }
        if (updatedState != state) {
            level.setBlock(pos, updatedState, Block.UPDATE_ALL);
        }
    }

    @Override
    public void playerDestroy(
            Level level,
            Player player,
            BlockPos pos,
            BlockState state,
            @Nullable BlockEntity blockEntity,
            ItemStack tool
    ) {
        if (attachmentCount(state) > 1) {
            Direction direction = getLookedAtAttachment(state, level, pos, player);
            if (direction != null) {
                BlockState remainingState = state
                        .setValue(attachmentProperty(direction), false)
                        .setValue(connectedProperty(direction), false);
                if (level.setBlock(pos, remainingState, Block.UPDATE_ALL)) {
                    if (blockEntity instanceof BoilerTipBlockEntity oldTip
                            && level.getBlockEntity(pos) instanceof BoilerTipBlockEntity newTip) {
                        oldTip.copyConnectionsTo(newTip, direction);
                    }
                    if (!player.getAbilities().instabuild) {
                        popResource(level, pos, new ItemStack(this));
                    }
                    return;
                }
            }
        }
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    @Nullable
    private static Direction getLookedAtAttachment(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player
    ) {
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getViewVector(1.0F).scale(player.blockInteractionRange() + 1.0));
        VoxelShape shape = state.getShape(level, pos, CollisionContext.empty());
        BlockHitResult hit = shape.clip(start, end, pos);
        return hit == null ? null : getTargetedAttachment(state, pos, hit.getLocation());
    }

    @Nullable
    private static Direction getTargetedAttachment(BlockState state, BlockPos pos, Vec3 hitLocation) {
        double x = hitLocation.x - (pos.getX() + 0.5);
        double y = hitLocation.y - (pos.getY() + 0.5);
        double z = hitLocation.z - (pos.getZ() + 0.5);
        Direction nearest = null;
        double nearestDot = -Double.MAX_VALUE;
        for (Direction candidate : Direction.values()) {
            if (!hasAttachment(state, candidate)) {
                continue;
            }
            Direction visibleSide = candidate == Direction.EAST || candidate == Direction.WEST
                    ? candidate
                    : candidate.getOpposite();
            double dot = x * visibleSide.getStepX()
                    + y * visibleSide.getStepY()
                    + z * visibleSide.getStepZ();
            if (dot > nearestDot) {
                nearest = candidate;
                nearestDot = dot;
            }
        }
        return nearest;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(
                NORTH, SOUTH, EAST, WEST, UP, DOWN,
                NORTH_ON, SOUTH_ON, EAST_ON, WEST_ON, UP_ON, DOWN_ON
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BoilerTipBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        if (level.isClientSide()) {
            return null;
        }

        return (lvl, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof BoilerTipBlockEntity tip) {
                BoilerTipBlockEntity.tick(lvl, pos, blockState, tip);
            }
        };
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        if (from == to) {
            return shape;
        }

        VoxelShape[] result = {Shapes.empty()};
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            double x0;
            double y0;
            double z0;
            double x1;
            double y1;
            double z1;
            switch (to) {
                case NORTH -> {
                    x0 = 1.0 - maxX; y0 = minY; z0 = 1.0 - maxZ;
                    x1 = 1.0 - minX; y1 = maxY; z1 = 1.0 - minZ;
                }
                case EAST -> {
                    x0 = minZ; y0 = minY; z0 = 1.0 - maxX;
                    x1 = maxZ; y1 = maxY; z1 = 1.0 - minX;
                }
                case SOUTH -> {
                    x0 = minX; y0 = minY; z0 = minZ;
                    x1 = maxX; y1 = maxY; z1 = maxZ;
                }
                case WEST -> {
                    x0 = 1.0 - maxZ; y0 = minY; z0 = minX;
                    x1 = 1.0 - minZ; y1 = maxY; z1 = maxX;
                }
                case UP -> {
                    x0 = minX; y0 = minZ; z0 = 1.0 - maxY;
                    x1 = maxX; y1 = maxZ; z1 = 1.0 - minY;
                }
                case DOWN -> {
                    x0 = minX; y0 = 1.0 - maxZ; z0 = minY;
                    x1 = maxX; y1 = 1.0 - minZ; z1 = maxY;
                }
                default -> {
                    x0 = minX; y0 = minY; z0 = minZ;
                    x1 = maxX; y1 = maxY; z1 = maxZ;
                }
            }
            result[0] = Shapes.or(result[0], Shapes.box(x0, y0, z0, x1, y1, z1));
        });
        return result[0];
    }
}
