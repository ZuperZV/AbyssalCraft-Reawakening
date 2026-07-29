package net.zuperzv.abyssalcraft_reawakening.init.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.BlockUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.block.custom.BasePortalBlock;

import java.util.Comparator;
import java.util.Optional;

public class BasePortalForcer {
    public static final int TICKET_RADIUS = 3;
    private static final int NETHER_PORTAL_RADIUS = 16;
    private static final int OVERWORLD_PORTAL_RADIUS = 128;
    private static final int FRAME_HEIGHT = 5;
    private static final int FRAME_WIDTH = 5;
    private static final int FRAME_BOX = 3;
    private static final int FRAME_HEIGHT_START = -1;
    private static final int FRAME_HEIGHT_END = 4;
    private static final int FRAME_WIDTH_START = -1;
    private static final int FRAME_WIDTH_END = 3;
    private static final int FRAME_BOX_START = -1;
    private static final int FRAME_BOX_END = 3;
    private static final int NOTHING_FOUND = -1;
    private final ServerLevel level;

    public BasePortalForcer(ServerLevel level) {
        this.level = level;
    }

    public Optional<BlockPos> findClosestPortalPosition(
            BlockPos approximateExitPos,
            boolean toNether,
            WorldBorder worldBorder
    ) {
        int radius = toNether ? 16 : 128;

        Optional<BlockPos> result = BlockPos.betweenClosedStream(
                        approximateExitPos.offset(-radius, -radius, -radius),
                        approximateExitPos.offset(radius, radius, radius))
                .filter(worldBorder::isWithinBounds)
                .filter(pos -> level.getBlockState(pos)
                        .is(ModBlocks.ABYSSAL_WASTELAND_PORTAL_BLOCK.block().get()))
                .map(BlockPos::immutable)
                .min(
                        Comparator
                                .comparingDouble((BlockPos p) -> p.distSqr(approximateExitPos))
                                .thenComparingInt(BlockPos::getY)
                );

        result.ifPresent(pos -> System.out.println("Returning portal: " + pos));

        return result;
    }

    public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos origin, Direction.Axis portalAxis) {
        Direction direction = Direction.get(AxisDirection.POSITIVE, portalAxis);
        double closestFullDistanceSqr = (double)-1.0F;
        BlockPos closestFullPosition = null;
        double closestPartialDistanceSqr = (double)-1.0F;
        BlockPos closestPartialPosition = null;
        WorldBorder worldBorder = this.level.getWorldBorder();
        int maxPlaceableY = Math.min(this.level.getMaxY(), this.level.getMinY() + this.level.getLogicalHeight() - 1);
        int edgeDistance = 1;
        BlockPos.MutableBlockPos mutable = origin.mutable();

        for(BlockPos.MutableBlockPos columnPos : BlockPos.spiralAround(origin, 16, Direction.EAST, Direction.SOUTH)) {
            int height = Math.min(maxPlaceableY, this.level.getHeight(Types.MOTION_BLOCKING, columnPos.getX(), columnPos.getZ()));
            if (worldBorder.isWithinBounds(columnPos) && worldBorder.isWithinBounds(columnPos.move(direction, 1))) {
                columnPos.move(direction.getOpposite(), 1);

                for(int y = height; y >= this.level.getMinY(); --y) {
                    columnPos.setY(y);
                    if (this.canPortalReplaceBlock(columnPos)) {
                        int firstEmptyY;
                        for(firstEmptyY = y; y > this.level.getMinY() && this.canPortalReplaceBlock(columnPos.move(Direction.DOWN)); --y) {
                        }

                        if (y + 4 <= maxPlaceableY) {
                            int deltaY = firstEmptyY - y;
                            if (deltaY <= 0 || deltaY >= 3) {
                                columnPos.setY(y);
                                if (this.canHostFrame(columnPos, mutable, direction, 0)) {
                                    double distance = origin.distSqr(columnPos);
                                    if (this.canHostFrame(columnPos, mutable, direction, -1) && this.canHostFrame(columnPos, mutable, direction, 1) && (closestFullDistanceSqr == (double)-1.0F || closestFullDistanceSqr > distance)) {
                                        closestFullDistanceSqr = distance;
                                        closestFullPosition = columnPos.immutable();
                                    }

                                    if (closestFullDistanceSqr == (double)-1.0F && (closestPartialDistanceSqr == (double)-1.0F || closestPartialDistanceSqr > distance)) {
                                        closestPartialDistanceSqr = distance;
                                        closestPartialPosition = columnPos.immutable();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (closestFullDistanceSqr == (double)-1.0F && closestPartialDistanceSqr != (double)-1.0F) {
            closestFullPosition = closestPartialPosition;
            closestFullDistanceSqr = closestPartialDistanceSqr;
        }

        if (closestFullDistanceSqr == (double)-1.0F) {
            int minStartY = Math.max(this.level.getMinY() - -1, 70);
            int maxStartY = maxPlaceableY - 9;
            if (maxStartY < minStartY) {
                return Optional.empty();
            }

            closestFullPosition = (new BlockPos(origin.getX() - direction.getStepX() * 1, Mth.clamp(origin.getY(), minStartY, maxStartY), origin.getZ() - direction.getStepZ() * 1)).immutable();
            closestFullPosition = worldBorder.clampToBounds(closestFullPosition);
            Direction clockWise = direction.getClockWise();

            for(int box = -1; box < 3; ++box) {
                for(int width = 0; width < 3; ++width) {
                    for(int height = -1; height < 3; ++height) {
                        BlockState blockState = height < 0 ? ModBlocks.ABYSSAL_STONE.block().get().defaultBlockState() : Blocks.AIR.defaultBlockState();
                        mutable.setWithOffset(closestFullPosition, width * direction.getStepX() + box * clockWise.getStepX(), height, width * direction.getStepZ() + box * clockWise.getStepZ());
                        this.level.setBlockAndUpdate(mutable, blockState);
                    }
                }
            }
        }

        for (int width = -2; width <= 2; width++) {
            for (int height = -1; height <= 3; height++) {

                boolean frame =
                        width == -2 ||
                                width == 2 ||
                                height == -1 ||
                                height == 3;

                mutable.setWithOffset(
                        closestFullPosition,
                        width * direction.getStepX(),
                        height,
                        width * direction.getStepZ()
                );

                if (frame) {
                    if (width == 0 && height == -1) {
                        this.level.setBlock(
                                mutable,
                                ModBlocks.ABYSSAL_WASTELAND_ACTIVATOR.block().get().defaultBlockState(), //Activator Block
                                3
                        );
                    } else {
                        this.level.setBlock(
                                mutable,
                                ModBlocks.ABYSSAL_STONE.block().get().defaultBlockState(),
                                3
                        );
                    }
                }
            }
        }

        BlockState portalBlockState =
                ModBlocks.ABYSSAL_WASTELAND_PORTAL_BLOCK.block()
                        .get()
                        .defaultBlockState()
                        .setValue(BasePortalBlock.AXIS, portalAxis);

        for (int width = -1; width <= 1; width++) {
            for (int height = 0; height < 3; height++) {

                mutable.setWithOffset(
                        closestFullPosition,
                        width * direction.getStepX(),
                        height,
                        width * direction.getStepZ()
                );

                this.level.setBlock(
                        mutable,
                        portalBlockState,
                        18
                );
            }
        }

        return Optional.of(new BlockUtil.FoundRectangle(closestFullPosition.above().immutable(), 3, 3));
    }

    private boolean canPortalReplaceBlock(BlockPos.MutableBlockPos pos) {
        BlockState blockState = this.level.getBlockState(pos);
        return blockState.canBeReplaced() && blockState.getFluidState().isEmpty();
    }

    private boolean canHostFrame(BlockPos origin, BlockPos.MutableBlockPos mutable, Direction direction, int offset) {
        Direction clockWise = direction.getClockWise();

        for(int width = -1; width < 3; ++width) {
            for(int height = -1; height < 4; ++height) {
                mutable.setWithOffset(origin, direction.getStepX() * width + clockWise.getStepX() * offset, height, direction.getStepZ() * width + clockWise.getStepZ() * offset);
                if (height < 0 && !this.level.getBlockState(mutable).isSolid()) {
                    return false;
                }

                if (height >= 0 && !this.canPortalReplaceBlock(mutable)) {
                    return false;
                }
            }
        }

        return true;
    }
}
