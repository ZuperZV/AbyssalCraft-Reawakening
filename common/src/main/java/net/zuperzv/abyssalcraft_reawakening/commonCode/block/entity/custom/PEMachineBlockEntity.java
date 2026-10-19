package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom.PEMachineBlock;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom.PlaceOfPowerStructure;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.PotentialEnergyData;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.NecronomiconItem;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.ModEntityTypes;

public class PEMachineBlockEntity extends BlockEntity {
    private static final int TRANSFER_RATE = 10;
    private static final int BASE_RANGE = 3;
    private static final int MAX_PE = 100_000;
    private int pe;
    private int rangeUpgrade;
    private int powerUpgrade;
    private int durationUpgrade;

    public PEMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PE_MACHINE_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PEMachineBlockEntity machine) {
        if (level.isClientSide() || !(state.getBlock() instanceof PEMachineBlock block)) return;
        long time = level.getGameTime();
        PEMachineBlock.Kind kind = block.getKind();

        if (kind == PEMachineBlock.Kind.STATUE && time % Math.max(5, 20 - machine.durationUpgrade * 2) == 0) {
            boolean protectedByStructure = machine.hasPlaceOfPower();
            machine.pe = Math.min(machine.capacity(), machine.pe + 1 + machine.powerUpgrade + (protectedByStructure ? 2 : 0));
            machine.sync();
            machine.transferFromStatue();
            if (machine.pe > 0 && time % 200 == 0 && !protectedByStructure
                    && level.getRandom().nextInt(200) == 0) {
                machine.triggerDisruption();
            }
        } else if (kind == PEMachineBlock.Kind.COLLECTOR || kind == PEMachineBlock.Kind.PEDESTAL) {
            if (time % TRANSFER_RATE == 0) machine.collectNearbyStatues();
        } else if (kind == PEMachineBlock.Kind.RELAY && time % TRANSFER_RATE == 0) {
            machine.relayEnergy();
        }
    }

    public InteractionResult interact(Player player, ItemStack held, InteractionHand hand) {
        if (level == null || level.isClientSide()) return InteractionResult.SUCCESS;
        PEMachineBlock.Kind kind = getKind();

        if (kind == PEMachineBlock.Kind.STATUE && applyCharm(held)) {
            held.shrink(1);
            sync();
            return InteractionResult.SUCCESS;
        }

        if (kind == PEMachineBlock.Kind.PEDESTAL && held.is(ModItems.STONE_TABLET.get()) && pe > 0) {
            PotentialEnergyData tabletEnergy = held.get(ModDataComponentTypes.POTENTIAL_ENERGY.get());
            int stored = tabletEnergy == null ? 0 : tabletEnergy.getPotentialEnergy();
            int amount = Math.min(pe, 1000 - stored);
            if (amount > 0) {
                held.set(ModDataComponentTypes.POTENTIAL_ENERGY.get(), new PotentialEnergyData(stored + amount));
                extract(amount);
                return InteractionResult.SUCCESS;
            }
        }

        if (kind == PEMachineBlock.Kind.DEPOSITIONER && held.is(ModItems.STONE_TABLET.get())) {
            PotentialEnergyData tabletEnergy = held.get(ModDataComponentTypes.POTENTIAL_ENERGY.get());
            if (tabletEnergy != null && tabletEnergy.getPotentialEnergy() > 0) {
                int accepted = insert(tabletEnergy.getPotentialEnergy());
                if (accepted > 0) {
                    tabletEnergy.decrease(accepted);
                    held.set(ModDataComponentTypes.POTENTIAL_ENERGY.get(), tabletEnergy);
                    level.playSound(null, worldPosition, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.8F, 1.2F);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if (kind == PEMachineBlock.Kind.SACRIFICIAL_ALTAR && held.is(ModItems.LIFE_CRYSTAL.get())) {
            int accepted = insert(100);
            if (accepted > 0) {
                held.shrink(1);
                level.playSound(null, worldPosition, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 0.8F, 0.8F);
                return InteractionResult.SUCCESS;
            }
        }

        if (held.getItem() instanceof NecronomiconItem) {
            int accepted = transferToBook(held);
            if (accepted > 0) {
                level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.8F, 1.1F);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public int getStoredPE() { return pe; }
    public int getCapacityPE() { return capacity(); }

    private void sync() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    private PEMachineBlock.Kind getKind() {
        return getBlockState().getBlock() instanceof PEMachineBlock block ? block.getKind() : PEMachineBlock.Kind.PEDESTAL;
    }

    private int capacity() {
        return switch (getKind()) {
            case STATUE -> 1000;
            case RELAY -> 500;
            case PEDESTAL, COLLECTOR -> 10_000;
            case CONTAINER, SACRIFICIAL_ALTAR, DEPOSITIONER -> MAX_PE;
        };
    }

    private int insert(int amount) {
        int inserted = Math.min(Math.max(0, amount), capacity() - pe);
        if (inserted > 0) {
            pe += inserted;
            sync();
        }
        return inserted;
    }

    public static void onMobDeath(ServerLevel level, net.minecraft.world.entity.LivingEntity mob) {
        if (mob instanceof Player) return;
        int energy = Math.max(10, Math.round(mob.getMaxHealth() * 2.0F));
        BlockPos.betweenClosedStream(mob.blockPosition().offset(-6, -3, -6), mob.blockPosition().offset(6, 3, 6))
                .map(pos -> level.getBlockEntity(pos))
                .filter(PEMachineBlockEntity.class::isInstance)
                .map(PEMachineBlockEntity.class::cast)
                .filter(altar -> altar.getKind() == PEMachineBlock.Kind.SACRIFICIAL_ALTAR)
                .findFirst()
                .ifPresent(altar -> altar.insert(energy));
    }

    private int extract(int amount) {
        int extracted = Math.min(Math.max(0, amount), pe);
        if (extracted > 0) {
            pe -= extracted;
            sync();
        }
        return extracted;
    }

    private int transferToBook(ItemStack book) {
        PotentialEnergyData current = book.get(ModDataComponentTypes.POTENTIAL_ENERGY.get());
        int stored = current == null ? 0 : current.getPotentialEnergy();
        int max = NecronomiconItem.getMaxPotentialEnergy(book);
        int amount = Math.min(pe, max - stored);
        if (amount <= 0) return 0;
        book.set(ModDataComponentTypes.POTENTIAL_ENERGY.get(), new PotentialEnergyData(stored + amount));
        extract(amount);
        return amount;
    }

    private boolean applyCharm(ItemStack stack) {
        String charm = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        String statue = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock()).getPath();
        if (charm.equals("ritual_charm")) {
            rangeUpgrade = durationUpgrade = powerUpgrade = 0;
            return true;
        }

        String upgrade = "";
        String deity = "";
        if (charm.startsWith("range_")) {
            upgrade = "range";
            deity = charm.substring("range_".length(), charm.length() - "_charm".length());
        } else if (charm.startsWith("duration_")) {
            upgrade = "duration";
            deity = charm.substring("duration_".length(), charm.length() - "_charm".length());
        } else if (charm.startsWith("power_")) {
            upgrade = "power";
            deity = charm.substring("power_".length(), charm.length() - "_charm".length());
        } else if (charm.endsWith("_charm")) {
            deity = charm.substring(0, charm.length() - "_charm".length());
        } else {
            return false;
        }

        boolean matching = deity.equals("ritual") || statue.equals(deity + "_statue");
        if (!matching) return false;
        if (upgrade.equals("range")) {
            rangeUpgrade = Math.min(5, rangeUpgrade + 2);
            return true;
        }
        if (upgrade.equals("duration")) {
            durationUpgrade = Math.min(5, durationUpgrade + 1);
            return true;
        }
        if (upgrade.equals("power") || upgrade.isEmpty()) {
            powerUpgrade = Math.min(5, powerUpgrade + 1);
            return true;
        }
        return false;
    }

    private void transferFromStatue() {
        int range = BASE_RANGE + rangeUpgrade;
        int offer = Math.min(pe, 1 + powerUpgrade + (hasPlaceOfPower() ? 2 : 0));
        BlockPos.betweenClosedStream(worldPosition.offset(-range, -2, -range), worldPosition.offset(range, 0, range))
                .filter(pos -> !pos.equals(worldPosition))
                .map(pos -> level.getBlockEntity(pos))
                .filter(PEMachineBlockEntity.class::isInstance)
                .map(PEMachineBlockEntity.class::cast)
                .filter(target -> target.getKind() == PEMachineBlock.Kind.PEDESTAL
                        || target.getKind() == PEMachineBlock.Kind.COLLECTOR)
                .findFirst()
                .ifPresent(target -> {
                    int accepted = target.insert(offer);
                    if (extract(accepted) > 0) emitTransferParticles(target.worldPosition);
                });

        if (pe > 0) {
            AABB box = new AABB(worldPosition).inflate(range, 2, range);
            for (Player player : level.getEntitiesOfClass(Player.class, box)) {
                for (InteractionHand hand : InteractionHand.values()) {
                    ItemStack held = player.getItemInHand(hand);
                    if (held.getItem() instanceof NecronomiconItem && transferToBook(held) > 0) return;
                }
            }
        }
    }

    private void collectNearbyStatues() {
        if (pe >= capacity()) return;
        int range = BASE_RANGE + rangeUpgrade;
        BlockPos.betweenClosedStream(worldPosition.offset(-range, -range, -range), worldPosition.offset(range, range, range))
                .map(pos -> level.getBlockEntity(pos))
                .filter(PEMachineBlockEntity.class::isInstance)
                .map(PEMachineBlockEntity.class::cast)
                .filter(source -> source != this && source.getKind() == PEMachineBlock.Kind.STATUE && source.pe > 0)
                .findFirst()
                .ifPresent(source -> {
                    int offer = Math.min(source.pe, 10 + powerUpgrade * 5);
                    int accepted = insert(offer);
                    if (source.extract(accepted) > 0) source.emitTransferParticles(worldPosition);
                });
    }

    private void relayEnergy() {
        if (pe <= 0) return;
        for (Direction direction : Direction.values()) {
            BlockEntity adjacent = level.getBlockEntity(worldPosition.relative(direction));
            if (!(adjacent instanceof PEMachineBlockEntity target) || target == this) continue;
            int amount = Math.min(pe, 10 + powerUpgrade * 5);
            int moved = target.insert(amount);
            if (moved > 0) {
                extract(moved);
                emitTransferParticles(target.worldPosition);
                return;
            }
        }
    }

    private void emitTransferParticles(BlockPos target) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        Vec3 from = Vec3.atCenterOf(worldPosition).add(0, 0.25, 0);
        Vec3 to = Vec3.atCenterOf(target).add(0, 0.25, 0);
        Vec3 velocity = to.subtract(from).normalize().scale(0.08);
        // Eight motes form the corners of a small cube, making the PE stream read as a 3D shape.
        for (int x : new int[]{-1, 1}) for (int y : new int[]{-1, 1}) for (int z : new int[]{-1, 1}) {
            serverLevel.sendParticles(ParticleTypes.END_ROD,
                    from.x + x * 0.045, from.y + y * 0.045, from.z + z * 0.045,
                    0, velocity.x, velocity.y, velocity.z, 1.0);
        }
    }

    private boolean hasPlaceOfPower() {
        return PlaceOfPowerStructure.containsStatue(level, worldPosition);
    }

    private void triggerDisruption() {
        if (!(level instanceof ServerLevel serverLevel)) return;
        var random = level.getRandom();
        int event = random.nextInt(10);
        AABB area = new AABB(worldPosition).inflate(6, 3, 6);
        switch (event) {
            case 0 -> spawnHostile(serverLevel, worldPosition.offset(random.nextInt(7) - 3, 0, random.nextInt(7) - 3));
            case 1 -> {
                for (int i = 0; i < 4; i++) spawnHostile(serverLevel, worldPosition.offset(random.nextInt(9) - 4, 0, random.nextInt(9) - 4));
            }
            case 2 -> {
                for (int i = 0; i < 4; i++) {
                    BlockPos monolith = worldPosition.offset(random.nextInt(9) - 4, 0, random.nextInt(9) - 4);
                    if (level.getBlockState(monolith).isAir()) level.setBlock(monolith, ModBlocks.MONOLITH_STONE.block().get().defaultBlockState(), 3);
                }
            }
            case 3 -> {
                for (int i = 0; i < 5; i++) {
                    BlockPos ooze = worldPosition.offset(random.nextInt(9) - 4, -1, random.nextInt(9) - 4);
                    if (level.getBlockState(ooze).isAir()) level.setBlock(ooze, Blocks.SCULK.defaultBlockState(), 3);
                }
            }
            case 4 -> {
                for (Player player : level.getEntitiesOfClass(Player.class, area)) {
                    EntityType.LIGHTNING_BOLT.spawn(serverLevel, player.blockPosition(), EntitySpawnReason.TRIGGERED);
                }
            }
            case 5 -> {
                for (Player player : level.getEntitiesOfClass(Player.class, area)) {
                    player.setRemainingFireTicks(80);
                }
            }
            case 6 -> {
                for (int i = 0; i < 8; i++) {
                    BlockPos fire = worldPosition.offset(random.nextInt(13) - 6, 0, random.nextInt(13) - 6);
                    fire = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, fire);
                    if (level.getBlockState(fire).isAir() && level.getBlockState(fire.below()).isSolid()) {
                        level.setBlock(fire, Blocks.FIRE.defaultBlockState(), 3);
                    }
                }
            }
            case 7 -> {
                for (int x = -8; x <= 8; x++) for (int z = -8; z <= 8; z++) {
                    BlockPos ground = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                            worldPosition.offset(x, 0, z)).below();
                    if (level.getBlockState(ground).isSolid() && !level.getBlockState(ground).is(ModBlocks.MONOLITH_STONE.block().get())) {
                        level.setBlock(ground, ModBlocks.GRIMESTONE.block().get().defaultBlockState(), 3);
                    }
                }
            }
            case 8 -> {
                for (Animal animal : level.getEntitiesOfClass(Animal.class, area)) {
                    ModEntityTypes.ABYSSAL_ZOMBIE.get().spawn(serverLevel, animal.blockPosition(), EntitySpawnReason.TRIGGERED);
                    animal.discard();
                }
            }
            case 9 -> {
                for (Player player : level.getEntitiesOfClass(Player.class, area)) {
                    Vec3 destination = player.position().add(random.nextInt(17) - 8, 0, random.nextInt(17) - 8);
                    player.randomTeleport(destination.x, destination.y, destination.z, true);
                }
                extract(Math.max(1, pe / 4));
            }
        }
        level.playSound(null, worldPosition, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.BLOCKS, 0.7F, 1.3F);
    }

    private void spawnZombie(ServerLevel level, BlockPos pos) {
        BlockPos spawnPos = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);
        EntityType.ZOMBIE.spawn(level, spawnPos, EntitySpawnReason.TRIGGERED);
    }

    private void spawnHostile(ServerLevel level, BlockPos pos) {
        BlockPos spawnPos = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);
        switch (level.getRandom().nextInt(3)) {
            case 0 -> EntityType.ZOMBIE.spawn(level, spawnPos, EntitySpawnReason.TRIGGERED);
            case 1 -> EntityType.SKELETON.spawn(level, spawnPos, EntitySpawnReason.TRIGGERED);
            default -> EntityType.SPIDER.spawn(level, spawnPos, EntitySpawnReason.TRIGGERED);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("pe", pe);
        output.putInt("range_upgrade", rangeUpgrade);
        output.putInt("power_upgrade", powerUpgrade);
        output.putInt("duration_upgrade", durationUpgrade);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        pe = Math.max(0, Math.min(capacity(), input.getIntOr("pe", 0)));
        rangeUpgrade = Math.max(0, Math.min(5, input.getIntOr("range_upgrade", 0)));
        powerUpgrade = Math.max(0, Math.min(5, input.getIntOr("power_upgrade", 0)));
        durationUpgrade = Math.max(0, Math.min(5, input.getIntOr("duration_upgrade", 0)));
    }
}
