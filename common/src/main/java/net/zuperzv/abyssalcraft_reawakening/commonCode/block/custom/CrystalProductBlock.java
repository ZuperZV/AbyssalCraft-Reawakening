package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrystalProductBlock extends Block {
    private static final VoxelShape SHAPE = Shapes.or(
            box(1, 0, 3, 13, 1, 13),
            box(4, 0, 4, 12, 6, 12)
    );

    private final int tint;
    private final String product;

    public CrystalProductBlock(BlockBehaviour.Properties properties, int tint, String product) {
        super(properties);
        this.tint = tint;
        this.product = product;
    }

    public int getTint() {
        return tint;
    }

    public String getProduct() {
        return product;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
