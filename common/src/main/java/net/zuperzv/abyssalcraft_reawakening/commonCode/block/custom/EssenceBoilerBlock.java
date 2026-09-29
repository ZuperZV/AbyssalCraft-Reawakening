package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.UseRemainder;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.EssenceBoilerBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerPotionFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.services.EssenceBoilerPlatformAccess;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class EssenceBoilerBlock extends BaseEntityBlock {

    public static final MapCodec<EssenceBoilerBlock> CODEC =
            simpleCodec(EssenceBoilerBlock::new);

    public static final EnumProperty<Direction.Axis> FACING =
            BlockStateProperties.HORIZONTAL_AXIS;

    public static final BooleanProperty LIT =
            BlockStateProperties.LIT;

    public static final BooleanProperty DONE =
            BooleanProperty.create("done");

    public static final BooleanProperty SYLPH_EMBER =
            BooleanProperty.create("sylph_ember");

    private static final VoxelShape SHAPE_Z = Shapes.or(
            box(0, 0.5, 10, 16, 4.5, 14),
            box(10, 0, 0, 14, 4, 16),
            box(2, 0, 0, 6, 4, 16),
            box(0, 0.5, 2, 16, 4.5, 6),
            box(1, 6.5, 1, 15, 8.5, 15),
            box(1, 8.5, 1, 3, 16.5, 13),
            box(13, 8.5, 1, 15, 16.5, 15),
            box(3, 8.5, 1, 13, 16.5, 3),
            box(1, 8.5, 13, 13, 16.5, 15),
            box(2, 15.5, 0, 14, 18.5, 2),
            box(14, 15.5, 0, 16, 18.5, 16),
            box(0, 15.5, 14, 14, 18.5, 16),
            box(0, 15.5, 0, 2, 18.5, 14)
    );

    private static final VoxelShape SHAPE_X =
            rotateShape(
                    Direction.Axis.Z,
                    Direction.Axis.X,
                    SHAPE_Z
            );

    public EssenceBoilerBlock(Properties properties) {
        super(properties);

        registerDefaultState(
                stateDefinition.any()
                        .setValue(FACING, Direction.Axis.X)
                        .setValue(LIT, false)
                        .setValue(DONE, false)
                        .setValue(SYLPH_EMBER, false)
        );
    }

    @Override
    public VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return switch (state.getValue(FACING)) {
            case X -> SHAPE_X;
            case Z -> SHAPE_Z;
            case Y -> SHAPE_Z;
        };
    }

    @Override
    public BlockState rotate(
            BlockState state,
            Rotation rotation
    ) {
        Direction.Axis axis = state.getValue(FACING);

        if (rotation == Rotation.CLOCKWISE_90
                || rotation == Rotation.COUNTERCLOCKWISE_90) {

            axis = switch (axis) {
                case X -> Direction.Axis.Z;
                case Z -> Direction.Axis.X;
                case Y -> Direction.Axis.Y;
            };
        }

        return state.setValue(FACING, axis);
    }

    @Override
    public BlockState mirror(
            BlockState state,
            Mirror mirror
    ) {
        return state;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(
            BlockPlaceContext context
    ) {
        Direction direction =
                context.getHorizontalDirection().getOpposite();

        Direction.Axis axis = direction.getAxis();

        return defaultBlockState()
                .setValue(FACING, axis)
                .setValue(LIT, true)
                .setValue(DONE, false)
                .setValue(SYLPH_EMBER, false);
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(
                FACING,
                LIT,
                DONE,
                SYLPH_EMBER
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        return new EssenceBoilerBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
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
        if (blockEntity instanceof EssenceBoilerBlockEntity boiler) {
            boiler.drops();
        }

        super.playerDestroy(
                level,
                player,
                pos,
                state,
                blockEntity,
                tool
        );
    }

    @Override
    protected void affectNeighborsAfterRemoval(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            boolean movedByPiston
    ) {
        level.updateNeighbourForOutputSignal(pos, this);

        super.affectNeighborsAfterRemoval(
                state,
                level,
                pos,
                movedByPiston
        );
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> blockEntityType
    ) {
        if (level.isClientSide()) {
            return null;
        }

        return (lvl, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof EssenceBoilerBlockEntity boiler) {
                EssenceBoilerBlockEntity.tick(
                        lvl,
                        pos,
                        blockState,
                        boiler
                );
            }
        };
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        if (!(level.getBlockEntity(pos)
                instanceof EssenceBoilerBlockEntity boiler)) {
            return InteractionResult.PASS;
        }

        if (!stack.isEmpty()
                && stack.is(Items.FLINT_AND_STEEL)
                && !state.getValue(LIT)) {

            level.setBlock(
                    pos,
                    state.setValue(LIT, true),
                    3
            );

            damageOrShrink(
                    player,
                    hand,
                    stack
            );

            level.playSound(
                    null,
                    pos,
                    SoundEvents.FLINTANDSTEEL_USE,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty()
                && stack.is(ItemTags.SHOVELS)
                && state.getValue(LIT)) {

            level.setBlock(
                    pos,
                    state
                            .setValue(LIT, false)
                            .setValue(SYLPH_EMBER, false),
                    3
            );

            damageOrShrink(
                    player,
                    hand,
                    stack
            );

            level.playSound(
                    null,
                    pos,
                    SoundEvents.FIRE_EXTINGUISH,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            return InteractionResult.SUCCESS;
        }

        if (handlePotionInteraction(
                boiler,
                stack,
                player,
                hand
        )) {
            return InteractionResult.SUCCESS;
        }

        if (boiler.progress > 0) {
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty()
                && EssenceBoilerPlatformAccess.get().tryEmptyFluidContainer(
                boiler,
                player,
                hand
        )) {
            level.playSound(
                    null,
                    pos,
                    SoundEvents.BUCKET_EMPTY,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty()
                && EssenceBoilerPlatformAccess.get().tryFillFluidContainer(
                boiler,
                player,
                hand
        )) {
            level.playSound(
                    null,
                    pos,
                    SoundEvents.BUCKET_FILL,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            level.playSound(
                    null,
                    pos,
                    SoundEvents.GENERIC_SPLASH,
                    SoundSource.BLOCKS,
                    0.5F,
                    1.0F
            );

            boiler.wobble(
                    EssenceBoilerBlockEntity.WobbleStyle.POSITIVE
            );

            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty()) {
            if ((stack.is(Items.GLASS_BOTTLE)
                    || stack.is(Items.GLASS_BOTTLE))
                    && boiler.inventory
                    .getStackInSlot(EssenceBoilerBlockEntity.SLOT_CONTAINER)
                    .isEmpty()) {

                boiler.inventory.insertItem(
                        EssenceBoilerBlockEntity.SLOT_CONTAINER,
                        stack.copyWithCount(1),
                        false
                );

                playInsertEffects(
                        level,
                        pos,
                        boiler,
                        true
                );

                stack.shrink(1);

                level.playSound(
                        null,
                        pos,
                        SoundEvents.ITEM_PICKUP,
                        SoundSource.BLOCKS,
                        1.0F,
                        2.0F
                );

                return InteractionResult.SUCCESS;
            }

            for (int slot =
                 EssenceBoilerBlockEntity.SLOT_INGREDIENT_1;
                 slot <= EssenceBoilerBlockEntity.SLOT_INGREDIENT_3;
                 slot++) {

                if (!boiler.inventory
                        .getStackInSlot(slot)
                        .isEmpty()) {
                    continue;
                }

                boiler.inventory.insertItem(
                        slot,
                        stack.copyWithCount(1),
                        false
                );

                playInsertEffects(
                        level,
                        pos,
                        boiler,
                        false
                );

                stack.shrink(1);

                level.playSound(
                        null,
                        pos,
                        SoundEvents.ITEM_PICKUP,
                        SoundSource.BLOCKS,
                        1.0F,
                        2.0F
                );

                return InteractionResult.SUCCESS;
            }
        }

        int[] extractionOrder = {
                EssenceBoilerBlockEntity.SLOT_OUTPUT,
                EssenceBoilerBlockEntity.SLOT_INGREDIENT_1,
                EssenceBoilerBlockEntity.SLOT_INGREDIENT_2,
                EssenceBoilerBlockEntity.SLOT_INGREDIENT_3,
                EssenceBoilerBlockEntity.SLOT_CONTAINER
        };

        for (int slot : extractionOrder) {
            ItemStack extracted =
                    boiler.inventory.getStackInSlot(slot);

            if (extracted.isEmpty()) {
                continue;
            }

            boolean addedToInventory = false;

            for (ItemStack playerStack :
                    player.getInventory().getNonEquipmentItems()) {

                if (!playerStack.isEmpty()
                        && ItemStack.isSameItemSameComponents(
                        playerStack,
                        extracted
                )
                        && playerStack.getCount()
                        < playerStack.getMaxStackSize()) {

                    playerStack.grow(1);
                    addedToInventory = true;
                    break;
                }
            }

            if (!addedToInventory && stack.isEmpty()) {
                player.setItemInHand(
                        hand,
                        extracted.copyWithCount(1)
                );
                addedToInventory = true;
            }

            if (addedToInventory) {
                boiler.inventory.extractItem(
                        slot,
                        1,
                        false
                );

                level.playSound(
                        null,
                        pos,
                        SoundEvents.ITEM_PICKUP,
                        SoundSource.BLOCKS,
                        1.0F,
                        1.0F
                );

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.SUCCESS;
    }

    private static boolean handlePotionInteraction(
            EssenceBoilerBlockEntity boiler,
            ItemStack stack,
            Player player,
            InteractionHand hand
    ) {
        if (!EssenceBoilerPotionFluid.isConfigured()) {
            return false;
        }

        Level level = boiler.getLevel();

        if (level == null || stack.isEmpty()) {
            return false;
        }

        PotionContents contents =
                stack.get(DataComponents.POTION_CONTENTS);

        if (contents == null
                || contents == PotionContents.EMPTY) {
            return false;
        }

        int potionAmount =
                EssenceBoilerPotionFluid.amountPerPotion();

        int freeSpace =
                boiler.getFluidTankCapacity()
                        - boiler.getFluidTankAmount();

        if (freeSpace < potionAmount) {
            return false;
        }

        EssenceBoilerFluid incoming =
                new EssenceBoilerFluid(
                        EssenceBoilerPotionFluid.fluid(),
                        potionAmount,
                        contents
                );

        if (!boiler.getFluidTank().isEmpty()
                && !boiler.getFluidTank().isSame(incoming)) {
            return false;
        }

        ItemStack remainder =
                getUseRemainder(stack);

        int accepted =
                boiler.fillFluidTank(incoming);

        if (accepted != potionAmount) {
            return false;
        }

        stack.shrink(1);

        if (stack.isEmpty()
                && !remainder.isEmpty()) {

            player.setItemInHand(
                    hand,
                    remainder.copy()
            );
        }

        level.playSound(
                null,
                boiler.getBlockPos(),
                SoundEvents.BOTTLE_EMPTY,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
        );

        boiler.wobble(
                EssenceBoilerBlockEntity.WobbleStyle.POSITIVE
        );

        return true;
    }

    private static ItemStack getUseRemainder(
            ItemStack stack
    ) {
        UseRemainder useRemainder =
                stack.get(DataComponents.USE_REMAINDER);

        if (useRemainder == null) {
            return ItemStack.EMPTY;
        }

        return useRemainder.convertInto().create();
    }

    private static void damageOrShrink(
            Player player,
            InteractionHand hand,
            ItemStack stack
    ) {
        if (!stack.isDamageableItem()) {
            stack.shrink(1);
            return;
        }

        stack.hurtAndBreak(
                1,
                player,
                hand
        );
    }

    private static void playInsertEffects(
            Level level,
            BlockPos pos,
            EssenceBoilerBlockEntity boiler,
            boolean containerSlot
    ) {
        if (boiler.getFluidTankAmount() <= 0) {
            return;
        }

        level.playSound(
                null,
                pos,
                SoundEvents.GENERIC_SPLASH,
                SoundSource.BLOCKS,
                containerSlot ? 1.2F : 1.0F,
                1.0F
        );

        boiler.wobble(
                containerSlot
                        ? EssenceBoilerBlockEntity.WobbleStyle.NEGATIVE
                        : EssenceBoilerBlockEntity.WobbleStyle.POSITIVE
        );

        level.playSound(
                null,
                pos,
                SoundEvents.DECORATED_POT_INSERT,
                SoundSource.BLOCKS,
                1.0F,
                1.2F
        );

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.DUST_PLUME,
                    pos.getX() + 0.5D,
                    pos.getY() + 1.0D,
                    pos.getZ() + 0.5D,
                    7,
                    0.0D,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }

    @Override
    public void animateTick(
            BlockState state,
            Level level,
            BlockPos pos,
            RandomSource random
    ) {
        double xPos = pos.getX() + 0.5D;
        double yPos = pos.getY() + 1.2D;
        double zPos = pos.getZ() + 0.5D;

        if (random.nextDouble() < 0.1D) {
            level.playLocalSound(
                    xPos,
                    yPos,
                    zPos,
                    SoundEvents.FURNACE_FIRE_CRACKLE,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F,
                    false
            );

            if (level.getBlockEntity(pos)
                    instanceof EssenceBoilerBlockEntity boiler
                    && boiler.getFluidTank().isSame(Fluids.WATER)) {

                ItemStack first = boiler.inventory
                        .getStackInSlot(EssenceBoilerBlockEntity.SLOT_INGREDIENT_1);
                ItemStack second = boiler.inventory
                        .getStackInSlot(EssenceBoilerBlockEntity.SLOT_INGREDIENT_2);
                ItemStack third = boiler.inventory
                        .getStackInSlot(EssenceBoilerBlockEntity.SLOT_INGREDIENT_3);

                if (!first.isEmpty()) {
                    level.addParticle(
                            new ItemParticleOption(
                                    ParticleTypes.ITEM,
                                    first.getItem()
                            ),
                            xPos,
                            yPos,
                            zPos,
                            0.2D,
                            -0.4D,
                            0.1D
                    );
                }

                if (!second.isEmpty()) {
                    level.addParticle(
                            new ItemParticleOption(
                                    ParticleTypes.ITEM,
                                    second.getItem()
                            ),
                            xPos,
                            yPos,
                            zPos,
                            0.2D,
                            -0.2D,
                            0.3D
                    );
                }

                if (!third.isEmpty()) {
                    level.addParticle(
                            new ItemParticleOption(
                                    ParticleTypes.ITEM,
                                    third.getItem()
                            ),
                            xPos,
                            yPos,
                            zPos,
                            0.4D,
                            -0.1D,
                            0.4D
                    );
                }
            }
        }

        if (!state.getValue(LIT)) {
            return;
        }

        if (random.nextInt(10) == 0) {
            level.playLocalSound(
                    pos.getX() + 0.5D,
                    pos.getY() + 0.5D,
                    pos.getZ() + 0.5D,
                    SoundEvents.CAMPFIRE_CRACKLE,
                    SoundSource.BLOCKS,
                    0.5F + random.nextFloat(),
                    random.nextFloat() * 0.7F + 0.6F,
                    false
            );
        }

        if (random.nextInt(5) == 0) {
            for (int i = 0; i < random.nextInt(1) + 1; i++) {
                level.addParticle(
                        ParticleTypes.LAVA,
                        pos.getX() + 0.5D,
                        pos.getY() + 0.5D,
                        pos.getZ() + 0.5D,
                        random.nextFloat() / 2.0D,
                        5.0E-5D,
                        random.nextFloat() / 2.0D
                );
            }
        }

    }
    public static VoxelShape rotateShape(
            Direction.Axis from,
            Direction.Axis to,
            VoxelShape shape
    ) {
        if (from == to) {
            return shape;
        }

        VoxelShape[] buffer = {
                shape,
                Shapes.empty()
        };

        int times = 1;

        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes(
                    (minX, minY, minZ, maxX, maxY, maxZ) ->
                            buffer[1] = Shapes.or(
                                    buffer[1],
                                    Shapes.box(
                                            1 - maxZ,
                                            minY,
                                            minX,
                                            1 - minZ,
                                            maxY,
                                            maxX
                                    )
                            )
            );

            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }
}