package org.crimsoncrips.alexscavesexemplified.server.entity;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class ToxicBarrelEntity extends Projectile {

    public static final float SCALE = 5.0F;
    public static final double GRAVITY = 0.04D;
    public static final double DRAG = 0.98D;
    private static final double HOLD_DAMPING = 0.8D;
    private static final int MAX_AGE = ACExUtils.seconds(15.0F);
    private static final int FRAGMENTS = 40;
    private static final float FRAGMENT_ACID_SOURCE_CHANCE = 0.35F;
    private static final int AIMED_FRAGMENTS = 20;
    private static final double AIM_SPREAD = 9.0D;
    private static final int AIM_FLIGHT = ACExUtils.seconds(1.25F);
    private static final int AIM_FLIGHT_EXTRA = ACExUtils.seconds(0.75F);
    private static final double SCATTER_SPEED = 1.0D;
    private static final double SCATTER_LIFT = 0.5D;
    private static final DustParticleOptions DRIP = new DustParticleOptions(new Vector3f(0.45F, 0.9F, 0.15F), 3.0F);

    private static final EntityDataAccessor<Boolean> HELD = SynchedEntityData.defineId(ToxicBarrelEntity.class, EntityDataSerializers.BOOLEAN);

    private int targetId = -1;
    @Nullable
    private Vec3 aimPos;

    public ToxicBarrelEntity(EntityType<? extends ToxicBarrelEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(HELD, false);
    }

    public boolean isHeld() {
        return this.entityData.get(HELD);
    }

    public void setHeld(boolean held) {
        this.entityData.set(HELD, held);
    }

    public void aimAt(Vec3 aimPos, @Nullable LivingEntity homing) {
        this.aimPos = aimPos;
        this.targetId = homing == null ? -1 : homing.getId();
    }

    public static BlockState displayState() {
        return ACBlockRegistry.WASTE_DRUM.get().defaultBlockState();
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = this.getDeltaMovement();
        if (!this.level().isClientSide) {
            if (!this.isHeld()) {
                HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, entity -> false);
                if (hit.getType() == HitResult.Type.BLOCK) {
                    this.burst();
                    return;
                }
            }
            if (this.tickCount > MAX_AGE) {
                this.burst();
                return;
            }
        } else {
            this.level().addParticle(DRIP, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 0.0D, -0.1D, 0.0D);
        }
        motion = this.isHeld() ? motion.scale(HOLD_DAMPING) : motion.scale(DRAG).subtract(0.0D, GRAVITY, 0.0D);
        this.setPos(this.position().add(motion));
        this.setDeltaMovement(motion);
    }

    public void burst() {
        if (this.isRemoved())
            return;
        if (this.level() instanceof ServerLevel level) {
            RandomSource random = this.getRandom();
            Vec3 center = this.getBoundingBox().getCenter();
            LivingEntity target = level.getEntity(this.targetId) instanceof LivingEntity living && living.isAlive() ? living : null;
            Vec3 aimCenter = target != null ? target.position() : this.aimPos;
            for (int i = 0; i < FRAGMENTS; i++) {
                ToxicWasteEntity waste = ACExEntityRegistry.TOXIC_WASTE.get().create(level);
                if (waste == null)
                    continue;
                waste.setOwner(this.getOwner());
                waste.setVariant(ToxicWasteEntity.UNREFINED_WASTE);
                waste.setAcidSourceChance(FRAGMENT_ACID_SOURCE_CHANCE);
                waste.setPos(center);
                if (i < AIMED_FRAGMENTS && aimCenter != null) {
                    waste.setTargetId(target == null ? -1 : target.getId());
                    Vec3 aim = aimCenter.add((random.nextDouble() * 2.0D - 1.0D) * AIM_SPREAD, 0.0D, (random.nextDouble() * 2.0D - 1.0D) * AIM_SPREAD);
                    waste.setDeltaMovement(ToxicWasteEntity.arcTo(center, aim, AIM_FLIGHT + random.nextInt(AIM_FLIGHT_EXTRA)));
                } else {
                    Vec3 out = Vec3.directionFromRotation(0.0F, random.nextFloat() * 360.0F).scale(SCATTER_SPEED * (0.5D + random.nextDouble()));
                    waste.setDeltaMovement(out.x, SCATTER_LIFT * (0.5D + random.nextDouble()), out.z);
                }
                level.addFreshEntity(waste);
            }
            level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, center.x, center.y, center.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            level.levelEvent(2001, BlockPos.containing(center), Block.getId(displayState()));
            level.playSound(null, BlockPos.containing(center), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 8.0F, 0.7F);
        }
        this.discard();
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return false;
    }
}
