package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.EnumSet;

public class GroundlingEntity extends Monster {

    private static final EntityDataAccessor<Boolean> DATA_HIDING =
            SynchedEntityData.defineId(GroundlingEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_WAKE_UP_ID =
            SynchedEntityData.defineId(GroundlingEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_ATTACK_ID =
            SynchedEntityData.defineId(GroundlingEntity.class, EntityDataSerializers.INT);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();

    public final AnimationState hideAnimationState = new AnimationState();
    public final AnimationState hiddenAnimationState = new AnimationState();
    public final AnimationState wakeUpAnimationState = new AnimationState();

    private int clientAttackId = 0;
    private int clientWakeUpId  = 0;
    private int wakeUpTicks = 0;
    private int hideCooldown = 0;
    private int fearCooldown = 0;

    private int hideTicks = 0;

    public GroundlingEntity(EntityType<? extends GroundlingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(DATA_HIDING, false);
        builder.define(DATA_WAKE_UP_ID, 0);
        builder.define(DATA_ATTACK_ID, 0);
    }

    public boolean isHidden() {
        return this.entityData.get(DATA_HIDING);
    }

    public void setHidden(boolean hidden) {
        if (this.isHidden() == hidden) {
            return;
        }

        this.entityData.set(DATA_HIDING, hidden);

        if (hidden) {
            this.setTarget(null);
            this.getNavigation().stop();

            this.setDeltaMovement(0, 0, 0);
        }
    }

    public boolean isHiding() {
        return this.entityData.get(DATA_HIDING);
    }

    public void setHiding(boolean hiding) {
        this.entityData.set(DATA_HIDING, hiding);
    }

    public void startHide() {
        if (isHidden() || isHiding() || hideCooldown > 0) {
            return;
        }

        setHiding(true);

        hideTicks = 0;

        this.setTarget(null);
        this.getNavigation().stop();
        this.setDeltaMovement(0, 0, 0);
    }

    public void finishHide() {
        setHiding(false);

        hideTicks = 0;

        setHidden(true);
    }

    public void wakeUp() {
        if (!isHidden()) {
            return;
        }

        setHidden(false);

        triggerWakeUpAnimation();

        hideCooldown = 60;
        fearCooldown = 140;
    }

    private void triggerWakeUpAnimation() {
        int id = this.entityData.get(DATA_WAKE_UP_ID);
        this.entityData.set(DATA_WAKE_UP_ID, id + 1);
    }

    private void triggerAttackAnimation() {
        int id = this.entityData.get(DATA_ATTACK_ID);
        this.entityData.set(DATA_ATTACK_ID, id + 1);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(1, new GroundlingHideGoal(this));

        this.goalSelector.addGoal(
                2,
                new MeleeAttackGoal(this, 1.15D, false)
        );

        this.goalSelector.addGoal(
                4,
                new RandomLookAroundGoal(this)
        );

        this.goalSelector.addGoal(
                5,
                new LookAtPlayerGoal(this, Player.class, 8.0F)
        );

        this.goalSelector.addGoal(
                8,
                new GroundlingRandomStrollGoal(this, 0.75D)
        );

        this.targetSelector.addGoal(
                1,
                new GroundlingHurtByTargetGoal(this)
        );

        this.targetSelector.addGoal(
                2,
                new GroundlingPlayerTargetGoal(this)
        );

        this.targetSelector.addGoal(
                3,
                new GroundlingMobTargetGoal(this, Zombie.class)
        );

        this.targetSelector.addGoal(
                4,
                new NearestAttackableTargetGoal<>(
                        this,
                        AbstractVillager.class,
                        false
                )
        );

        this.targetSelector.addGoal(
                4,
                new NearestAttackableTargetGoal<>(
                        this,
                        IronGolem.class,
                        true
                )
        );

        this.targetSelector.addGoal(
                5,
                new NearestAttackableTargetGoal<>(
                        this,
                        Turtle.class,
                        10,
                        true,
                        false,
                        Turtle.BABY_ON_LAND_SELECTOR
                )
        );
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 18.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 2.5D)
                .add(Attributes.FOLLOW_RANGE, 23.0D);
    }

    @Override
    public void tick() {

        if (!level().isClientSide()) {

            if (hideCooldown > 0) {
                hideCooldown--;
            }

            if (fearCooldown > 0) {
                fearCooldown--;
            }

            if (isHiding()) {

                hideTicks++;

                if (hideTicks >= 104) {
                    finishHide();
                }
            }

            if (isHidden()) {

                LivingEntity nearbyTarget =
                        level().getNearestPlayer(this, 8.0D);

                if (nearbyTarget instanceof Player player) {

                    double distance = this.distanceToSqr(player);

                    boolean tooClose =
                            distance <= 2.3D * 2.3D;

                    boolean sprinting =
                            player.isSprinting() && distance <= 8.0D * 8.0D;

                    if (tooClose || sprinting) {
                        wakeUp();
                    }
                }
            }

            if (!isHidden()
                    && !isHiding()
                    && fearCooldown <= 0
                    && getLastHurtByMob() != null
                    && random.nextFloat() < 0.30F) {

                if (getTarget() == null) {
                    startHide();
                }

                fearCooldown = 80;
            }

            if (wakeUpTicks > 0) {
                wakeUpTicks++;

                if (wakeUpTicks >= 20.855) {
                    wakeUpTicks = 0;
                }
            }
        }

        if (level().isClientSide()) {

            int wakeUpId =
                    this.entityData.get(DATA_WAKE_UP_ID);

            if (wakeUpId != clientWakeUpId) {

                clientWakeUpId = wakeUpId;

                wakeUpTicks = 0;

                wakeUpAnimationState.start(tickCount);

                hideAnimationState.stop();
                hiddenAnimationState.stop();
                idleAnimationState.stop();
                walkAnimationState.stop();
            }

            if (isHiding()) {

                hideAnimationState.startIfStopped(tickCount);

                hiddenAnimationState.stop();
                wakeUpAnimationState.stop();
                idleAnimationState.stop();
                walkAnimationState.stop();
            }

            else if (isHidden()) {

                hideAnimationState.stop();

                hiddenAnimationState.startIfStopped(tickCount);

                wakeUpAnimationState.stop();
                idleAnimationState.stop();
                walkAnimationState.stop();
            }


            else if (wakeUpAnimationState.isStarted()) {

                wakeUpTicks++;

                idleAnimationState.stop();
                walkAnimationState.stop();

                if (wakeUpTicks >= 21) {

                    wakeUpAnimationState.stop();
                    wakeUpTicks = 0;
                }
            }

            else {

                hiddenAnimationState.stop();

                boolean moving =
                        this.walkAnimation.isMoving();

                idleAnimationState.animateWhen(
                        !moving,
                        tickCount
                );

                walkAnimationState.animateWhen(
                        moving,
                        tickCount
                );
            }

            int attackId =
                    this.entityData.get(DATA_ATTACK_ID);

            if (attackId != clientAttackId) {

                clientAttackId = attackId;

                attackAnimationState.start(tickCount);
            }
        }

        super.tick();
    }

    private void stopNormalAnimations() {
        idleAnimationState.stop();
        walkAnimationState.stop();
        attackAnimationState.stop();
        wakeUpAnimationState.stop();
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (isHidden()) {
            return false;
        }

        return super.hurtServer(level, source, amount);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {

        boolean result = super.doHurtTarget(level, target);

        if (result) {

            if (isHidden()) {
                wakeUp();
            }

            triggerAttackAnimation();

            if (target instanceof LivingEntity living) {

                float difficulty =
                        level.getCurrentDifficultyAt(
                                blockPosition()
                        ).getEffectiveDifficulty();

                if (random.nextFloat() < difficulty * 0.10F) {

                    living.addEffect(
                            new MobEffectInstance(
                                    MobEffects.DARKNESS,
                                    Mth.floor(
                                            10.0F + difficulty * 10.0F
                                    ),
                                    0,
                                    false,
                                    true,
                                    true
                            )
                    );
                }
            }
        }

        return result;
    }

    private static class GroundlingHideGoal extends Goal {

        private final GroundlingEntity groundling;

        public GroundlingHideGoal(GroundlingEntity groundling) {
            this.groundling = groundling;

            this.setFlags(
                    EnumSet.of(
                            Flag.MOVE,
                            Flag.LOOK
                    )
            );
        }

        @Override
        public boolean canUse() {

            if (groundling.isHidden()) {
                return false;
            }

            if (groundling.isHiding()) {
                return false;
            }

            if (groundling.hideCooldown > 0) {
                return false;
            }

            if (groundling.getTarget() != null) {
                return false;
            }

            return groundling.getRandom().nextInt(140) == 0;
        }

        @Override
        public boolean canContinueToUse() {

            return groundling.isHiding()
                    || groundling.isHidden();
        }

        @Override
        public void start() {

            groundling.startHide();
        }

        @Override
        public void tick() {

            groundling.getNavigation().stop();
            groundling.setDeltaMovement(0, 0, 0);
        }

        @Override
        public void stop() {
            //The entity itself decides when to wake.
        }
    }

    private static class GroundlingRandomStrollGoal
            extends WaterAvoidingRandomStrollGoal {

        private final GroundlingEntity groundling;

        public GroundlingRandomStrollGoal(
                GroundlingEntity groundling,
                double speed
        ) {
            super(groundling, speed);
            this.groundling = groundling;
        }

        @Override
        public boolean canUse() {

            if (groundling.isHidden()) {
                return false;
            }

            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {

            if (groundling.isHidden()) {
                return false;
            }

            return super.canContinueToUse();
        }
    }

    private static class GroundlingPlayerTargetGoal
            extends NearestAttackableTargetGoal<Player> {

        private final GroundlingEntity groundling;

        public GroundlingPlayerTargetGoal(
                GroundlingEntity groundling
        ) {
            super(
                    groundling,
                    Player.class,
                    true
            );

            this.groundling = groundling;
        }

        @Override
        public boolean canUse() {

            if (groundling.isHidden()) {
                return false;
            }

            return super.canUse();
        }
    }

    private static class GroundlingMobTargetGoal<T extends LivingEntity>
            extends NearestAttackableTargetGoal<T> {

        private final GroundlingEntity groundling;

        public GroundlingMobTargetGoal(
                GroundlingEntity groundling,
                Class<T> targetClass
        ) {
            super(
                    groundling,
                    targetClass,
                    true
            );

            this.groundling = groundling;
        }

        @Override
        public boolean canUse() {

            if (groundling.isHidden()) {
                return false;
            }

            return super.canUse();
        }
    }

    private static class GroundlingHurtByTargetGoal
            extends HurtByTargetGoal {

        private final GroundlingEntity groundling;

        public GroundlingHurtByTargetGoal(
                GroundlingEntity groundling
        ) {
            super(groundling);

            this.groundling = groundling;
        }

        @Override
        public boolean canUse() {

            if (groundling.isHidden()) {
                return false;
            }

            return super.canUse();
        }
    }
}