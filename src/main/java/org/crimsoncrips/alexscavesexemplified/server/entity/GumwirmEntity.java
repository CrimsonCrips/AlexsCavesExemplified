package org.crimsoncrips.alexscavesexemplified.server.entity;

import com.github.alexmodguy.alexscaves.server.entity.ai.GumWormAttackGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.GumWormDestroyGobthumperGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.GumWormDigRandomlyGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.GumWormLeapRandomlyGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.GumWormEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GumwirmEntity extends GumWormEntity {

    public static final float SCALE = 0.1F;

    public GumwirmEntity(EntityType<? extends GumwirmEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.GRAVITY, 0.15)
                .add(Attributes.MAX_HEALTH, 15.0)
                .add(Attributes.ARMOR, 1.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new GumWormAttackGoal(this));
        this.goalSelector.addGoal(2, new GumWormDestroyGobthumperGoal(this));
        this.goalSelector.addGoal(3, new GumWormLeapRandomlyGoal(this));
        this.goalSelector.addGoal(4, new GumWormDigRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public float getScale() {
        return super.getScale() * SCALE;
    }

    @Override
    public boolean attackAllAroundMouth(float damageAmount, float knockbackAmount) {
        boolean attackedMainTarget = false;
        AABB hurtBox = this.getBoundingBox().inflate(this.isLeaping() ? 3.0 * SCALE : SCALE);
        Entity target = this.getTarget();
        DamageSource damageSource = this.damageSources().mobAttack(this);
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, hurtBox, EntitySelector.NO_CREATIVE_OR_SPECTATOR)) {
            if (!living.is(this) && !living.isAlliedTo(this) && !this.isRidingPlayer(living) && living.getType() != this.getType()) {
                living.hurt(damageSource, damageAmount);
                if (target != null && living.is(target)) {
                    attackedMainTarget = true;
                }
            }
        }
        return attackedMainTarget;
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public Vec3 getHookPosition(int i) {
        Vec3 offset = new Vec3(i * -SCALE, -0.5F * SCALE, -1.15F * SCALE)
                .xRot((float) -Math.toRadians(this.getXRot()))
                .yRot((float) -Math.toRadians(this.yBodyRot));
        return this.position().add(0.0, 0.5F * this.getBbWidth(), 0.0).add(offset);
    }

    @Override
    public void spawnDustParticles(boolean surface) {
        spawnTinyDust(this, this.getSurfaceY());
    }

    public static void spawnTinyDust(Entity entity, double surfaceY) {
        if (surfaceY < entity.getY(1.0) && entity.getY(1.0) - surfaceY < 0.4 && entity.getRandom().nextInt(3) == 0) {
            BlockState state = entity.level().getBlockState(BlockPos.containing(entity.getX(), surfaceY - 1.0, entity.getZ()));
            if (!state.isAir()) {
                entity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), entity.getRandomX(0.8F), surfaceY + entity.getRandom().nextFloat() * SCALE, entity.getRandomZ(0.8F), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public float getScreenShakeAmount(float partialTicks) {
        return 0.0F;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 1.8F;
    }
}
