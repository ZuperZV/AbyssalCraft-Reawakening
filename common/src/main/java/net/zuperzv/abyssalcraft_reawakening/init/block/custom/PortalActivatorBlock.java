package net.zuperzv.abyssalcraft_reawakening.init.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.portal.AbyssalWastelandPortalShape;
import net.zuperzv.abyssalcraft_reawakening.init.portal.BasePortalShape;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension.ModDimensions;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class PortalActivatorBlock extends Block {
    public static BooleanProperty ON = BooleanProperty.create("on");

    public PortalActivatorBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(ON, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ON);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (itemStack.getItem() == ModItems.GATEWAY_KEY.get()) {

            if (inPortalDimension(level)) {
                Optional<BasePortalShape> optionalShape =
                        AbyssalWastelandPortalShape.findEmptyPortalShape(level, pos.above(), Direction.Axis.X);

                if (optionalShape.isPresent()) {
                    optionalShape.get().createPortalBlocks(level);

                    setAndCheckPortalState(level, pos, state);

                    return InteractionResult.SUCCESS;
                }
            }

        }
        return InteractionResult.FAIL;
    }

    private static void setAndCheckPortalState(Level level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(ON, level.getBlockState(pos.above()).is(ModBlocks.ABYSSAL_WASTELAND_PORTAL_BLOCK.block().get())), 3);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        setAndCheckPortalState(level, pos, state);

        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
    }

    private static boolean inPortalDimension(Level level) {
        return level.dimension() == Level.OVERWORLD || level.dimension() == ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY;
    }
}
