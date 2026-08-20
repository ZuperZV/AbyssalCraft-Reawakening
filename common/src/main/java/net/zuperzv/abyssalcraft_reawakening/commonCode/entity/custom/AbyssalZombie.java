package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;

import java.util.Objects;

public class AbyssalZombie extends Zombie {

    public AbyssalZombie(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new ZombieAttackTurtleEggGoal(this, (double)1.0F, 3));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(12, new RandomStrollGoal(this, 0.7));
        this.addBehaviourGoals();
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new SpearUseGoal(this, (double)1.0F, (double)1.0F, 10.0F, 2.0F));
        this.goalSelector.addGoal(3, new ZombieAttackGoal(this, (double)1.0F, false));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, (double)1.0F, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[]{ZombifiedPiglin.class}));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Zombie.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE,
                (double)30.0F).add(Attributes.MAX_HEALTH,
                (double)33.5F).add(Attributes.MOVEMENT_SPEED,
                (double)0.23F).add(Attributes.ATTACK_DAMAGE,
                (double)3.0F).add(Attributes.ARMOR,
                (double)2.0F).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean result = super.doHurtTarget(level, target);
        if (result) {
            float difficulty = level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < difficulty * 0.3F) {
                target.igniteForSeconds((float)(2 * (int)difficulty));
            }
            if (this.getMainHandItem().isEmpty() && this.random.nextFloat() < difficulty * 0.1F) {
                ((LivingEntity) target).addEffect(
                        new MobEffectInstance(
                                MobEffects.POISON,
                                (Mth.floor((float)(2 * (int)difficulty) * 20.0F)),
                                0,
                                false,
                                true,
                                true
                        )
                );
            }
        }

        return result;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        if (random.nextFloat() < (this.level().getDifficulty() == Difficulty.HARD ? 0.05F : 0.01F)) {
            int rand = random.nextInt(6);
            if (rand == 0) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ABYSSALNITE_SWORD.get()));
            } else if (rand == 1) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ABYSSALNITE_SPEAR.get()));
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ABYSSALNITE_SHOVEL.get()));
            }
        }

    }

    private class ZombieAttackTurtleEggGoal extends RemoveBlockGoal {
        ZombieAttackTurtleEggGoal(PathfinderMob mob, double speedModifier, int verticalSearchRange) {
            Objects.requireNonNull(AbyssalZombie.this);
            Objects.requireNonNull(AbyssalZombie.this);
            super(Blocks.TURTLE_EGG, mob, speedModifier, verticalSearchRange);
        }

        public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
            level.playSound((Entity)null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + AbyssalZombie.this.random.nextFloat() * 0.2F);
        }

        public void playBreakSound(Level level, BlockPos pos) {
            level.playSound((Entity)null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.getRandom().nextFloat() * 0.2F);
        }

        public double acceptedDistance() {
            return 1.14;
        }
    }
}
