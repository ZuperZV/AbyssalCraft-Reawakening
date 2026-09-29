package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.zuperzv.abyssalcraft_reawakening.services.types.FluidDefinition;
import net.zuperzv.abyssalcraft_reawakening.services.types.IFluidFactory;

import java.util.Optional;

public final class FabricFluidFactory implements IFluidFactory {

    @Override
    public FlowingFluid createSource(FluidDefinition definition) {
        return new FabricSourceFluid(definition);
    }

    @Override
    public FlowingFluid createFlowing(FluidDefinition definition) {
        return new FabricFlowingFluid(definition);
    }


    private abstract static class FabricBaseFluid extends FlowingFluid {

        protected final FluidDefinition definition;

        protected FabricBaseFluid(FluidDefinition definition) {
            this.definition = definition;
        }

        @Override
        public Fluid getFlowing() {
            return definition.flowing().get();
        }

        @Override
        public Fluid getSource() {
            return definition.source().get();
        }

        @Override
        public Item getBucket() {
            return definition.bucket().get();
        }

        @Override
        protected int getSlopeFindDistance(LevelReader level) {
            return definition.slopeFindDistance();
        }

        @Override
        public int getDropOff(LevelReader level) {
            return definition.levelDecreasePerBlock();
        }

        @Override
        public int getTickDelay(LevelReader level) {
            return definition.tickRate();
        }

        @Override
        protected float getExplosionResistance() {
            return definition.explosionResistance();
        }

        @Override
        protected boolean canConvertToSource(ServerLevel level) {
            return false;
        }

        @Override
        protected void beforeDestroyingBlock(
                LevelAccessor level,
                BlockPos pos,
                BlockState state
        ) {
            BlockEntityDropHelper.drop(level, pos, state);
        }

        @Override
        protected BlockState createLegacyBlock(FluidState state) {
            return definition.block()
                    .get()
                    .defaultBlockState()
                    .setValue(
                            LiquidBlock.LEVEL,
                            getLegacyLevel(state)
                    );
        }

        @Override
        protected boolean canBeReplacedWith(
                FluidState state,
                BlockGetter level,
                BlockPos pos,
                Fluid fluid,
                Direction direction
        ) {
            return direction == Direction.DOWN
                    && !fluid.isSame(this);
        }

        @Override
        public boolean isSame(Fluid fluid) {
            return fluid == definition.source().get()
                    || fluid == definition.flowing().get();
        }

        @Override
        public Optional<net.minecraft.sounds.SoundEvent> getPickupSound() {
            return Optional.of(SoundEvents.BUCKET_FILL);
        }

        @Override
        protected ParticleOptions getDripParticle() {
            return ParticleTypes.DRIPPING_WATER;
        }
    }


    private static final class FabricSourceFluid
            extends FabricBaseFluid {

        private FabricSourceFluid(FluidDefinition definition) {
            super(definition);
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }


    private static final class FabricFlowingFluid
            extends FabricBaseFluid {

        private FabricFlowingFluid(FluidDefinition definition) {
            super(definition);
        }

        @Override
        protected void createFluidStateDefinition(
                StateDefinition.Builder<Fluid, FluidState> builder
        ) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }


    private static final class BlockEntityDropHelper {

        private static void drop(
                LevelAccessor level,
                BlockPos pos,
                BlockState state
        ) {
            net.minecraft.world.level.block.Block.dropResources(
                    state,
                    (Level) level,
                    pos
            );
        }
    }
}