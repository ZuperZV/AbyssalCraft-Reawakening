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
    private static final VoxelShape CRYSTAL_SHAPE = Shapes.or(
            box(6.37, 0, 3.8, 12.16, 7.28, 10.74),
            box(5.62, 0, 7.69, 10.18, 6.08, 13.84),
            box(2.37, 0, 5.08, 9.74, 8.44, 10.82)
    );
    private static final VoxelShape SHARD_SHAPE = Shapes.or(
            box(6.37, 0, 4.53, 11.89, 4.38, 10.74),
            box(5.79, 0, 7.69, 10.18, 3.3, 12.73),
            box(3.08, 0, 5.12, 9.74, 5.53, 10.82)
    );
    private static final VoxelShape FRAGMENT_SHAPE = Shapes.or(
            box(7.89, 0, 6.26, 10.7, 2.19, 9.49),
            box(6.62, 0, 7.5, 9.55, 2.1, 10.86),
            box(5.41, 0, 6.32, 8.62, 2.28, 9.16)
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
        return switch (product) {
            case "crystal" -> CRYSTAL_SHAPE;
            case "shard" -> SHARD_SHAPE;
            case "fragment" -> FRAGMENT_SHAPE;
            default -> Shapes.empty();
        };
    }
}
