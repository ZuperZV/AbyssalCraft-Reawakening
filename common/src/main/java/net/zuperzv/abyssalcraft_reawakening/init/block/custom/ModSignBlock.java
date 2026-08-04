package net.zuperzv.abyssalcraft_reawakening.init.block.custom;

import com.mojang.serialization.MapCodec;
import java.util.Arrays;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignApplicator;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.ModSignBlockEntity;
import org.jspecify.annotations.Nullable;

public abstract class ModSignBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED;
    private static final VoxelShape SHAPE;
    private final WoodType type;

    protected ModSignBlock(WoodType type, BlockBehaviour.Properties properties) {
        super(properties);
        this.type = type;
    }

    protected abstract MapCodec<? extends ModSignBlock> codec();

    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if ((Boolean)state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public boolean isPossibleToRespawnInThis(BlockState state) {
        return true;
    }

    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new ModSignBlockEntity(worldPosition, blockState);
    }

    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity var9 = level.getBlockEntity(pos);
        if (var9 instanceof ModSignBlockEntity sign) {
            Item var11 = itemStack.getItem();
            SignApplicator var10000;
            if (var11 instanceof SignApplicator applicator) {
                var10000 = applicator;
            } else {
                var10000 = null;
            }

            SignApplicator signApplicator = var10000;
            boolean hasApplicatorToUse = signApplicator != null && player.mayBuild();
            if (level instanceof ServerLevel serverLevel) {
                if (hasApplicatorToUse && !sign.isWaxed() && !this.otherPlayerIsEditingSign(player, sign)) {
                    boolean isFrontText = sign.isFacingFrontText(player);
                    if (signApplicator.canApplyToSign(sign.getText(isFrontText), itemStack, player) && signApplicator.tryApplyToSign(serverLevel, sign, isFrontText, itemStack, player)) {
                        sign.executeClickCommandsIfPresent(serverLevel, player, pos, isFrontText);
                        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                        serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, sign.getBlockPos(), Context.of(player, sign.getBlockState()));
                        itemStack.consume(1, player);
                        return InteractionResult.SUCCESS;
                    } else {
                        return InteractionResult.TRY_WITH_EMPTY_HAND;
                    }
                } else {
                    return InteractionResult.TRY_WITH_EMPTY_HAND;
                }
            } else {
                return !hasApplicatorToUse && !sign.isWaxed() ? InteractionResult.CONSUME : InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity var7 = level.getBlockEntity(pos);
        if (var7 instanceof ModSignBlockEntity sign) {
            if (level instanceof ServerLevel serverLevel) {
                boolean isFrontText = sign.isFacingFrontText(player);
                boolean executedClickCommand = sign.executeClickCommandsIfPresent(serverLevel, player, pos, isFrontText);
                if (sign.isWaxed()) {
                    serverLevel.playSound((Entity)null, sign.getBlockPos(), sign.getSignInteractionFailedSoundEvent(), SoundSource.BLOCKS);
                    return InteractionResult.SUCCESS_SERVER;
                } else if (executedClickCommand) {
                    return InteractionResult.SUCCESS_SERVER;
                } else if (!this.otherPlayerIsEditingSign(player, sign) && player.mayBuild() && this.hasEditableText(player, sign, isFrontText)) {
                    this.openTextEdit(player, sign, isFrontText);
                    return InteractionResult.SUCCESS_SERVER;
                } else {
                    return InteractionResult.PASS;
                }
            } else {
                Util.pauseInIde(new IllegalStateException("Expected to only call this on server"));
                return InteractionResult.CONSUME;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    private boolean hasEditableText(Player player, ModSignBlockEntity sign, boolean isFrontText) {
        SignText text = sign.getText(isFrontText);
        return Arrays.stream(text.getMessages(player.isTextFilteringEnabled())).allMatch((message) -> message.equals(CommonComponents.EMPTY) || message.getContents() instanceof PlainTextContents);
    }

    public abstract float getYRotationDegrees(BlockState var1);

    public Vec3 getSignHitboxCenterPosition(BlockState state) {
        return new Vec3((double)0.5F, (double)0.5F, (double)0.5F);
    }

    protected FluidState getFluidState(BlockState state) {
        return (Boolean)state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public WoodType type() {
        return this.type;
    }

    public static WoodType getWoodType(Block block) {
        WoodType var10000;
        if (block instanceof ModSignBlock signBlock) {
            var10000 = signBlock.type();
        } else {
            var10000 = WoodType.OAK;
        }

        return var10000;
    }

    public void openTextEdit(Player player, ModSignBlockEntity sign, boolean isFrontText) {
        sign.setAllowedPlayerEditor(player.getUUID());
        player.openTextEdit(sign, isFrontText);
    }

    private boolean otherPlayerIsEditingSign(Player player, ModSignBlockEntity sign) {
        UUID playerWhoMayEdit = sign.getPlayerWhoMayEdit();
        return playerWhoMayEdit != null && !playerWhoMayEdit.equals(player.getUUID());
    }

    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.MOD_SIGN_BE.get(), ModSignBlockEntity::tick);
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        SHAPE = Block.column((double)8.0F, (double)0.0F, (double)16.0F);
    }
}
