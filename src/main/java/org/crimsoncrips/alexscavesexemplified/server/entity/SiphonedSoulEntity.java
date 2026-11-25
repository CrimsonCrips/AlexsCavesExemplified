package org.crimsoncrips.alexscavesexemplified.server.entity;

import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSectumsempra;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class SiphonedSoulEntity extends Entity {

    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(SiphonedSoulEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<CompoundTag> MOB_DATA = SynchedEntityData.defineId(SiphonedSoulEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<Float> MOB_YAW = SynchedEntityData.defineId(SiphonedSoulEntity.class, EntityDataSerializers.FLOAT);
    private static final String[] STRIPPED_KEYS = {"Brain", "Passengers", "Leash", "CustomName", "CustomNameVisible", "DeathTime", "HurtTime", "HurtByTimestamp", "ActiveEffects", "UUID"};
    private static final double SHRINK_DISTANCE = 3.0D;
    private static final float MIN_SCALE = 0.15F;
    private static final int RISE_TICKS = ACExUtils.seconds(0.4F);
    private static final int MAX_LIFE = ACExUtils.seconds(6.0F);
    private static final double RISE_SPEED = 0.06D;
    private static final double MAX_SPEED = 0.9D;
    private static final double STEER = 0.18D;
    private static final double ARRIVE_DISTANCE = 0.6D;
    private static final double LOST_DISTANCE = 64.0D;
    private static final DustParticleOptions SOUL_DUST = new DustParticleOptions(new Vector3f(0.7F, 0.04F, 0.08F), 1.1F);
    private static final DustParticleOptions SOUL_CORE = new DustParticleOptions(new Vector3f(1.0F, 0.55F, 0.55F), 0.6F);

    private float charge;
    @Nullable
    private Entity replica;
    private boolean replicaFailed;

    public SiphonedSoulEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public static SiphonedSoulEntity create(Level level, Vec3 position, Player owner, float charge, @Nullable LivingEntity source) {
        SiphonedSoulEntity soul = new SiphonedSoulEntity(ACExEntityRegistry.SIPHONED_SOUL.get(), level);
        soul.setPos(position);
        soul.setOwnerId(owner.getId());
        soul.charge = charge;
        if (source != null && !(source instanceof Player)) {
            CompoundTag tag = new CompoundTag();
            if (source.saveAsPassenger(tag)) {
                for (String key : STRIPPED_KEYS) {
                    tag.remove(key);
                }
                tag.putFloat("Health", source.getMaxHealth());
                soul.entityData.set(MOB_DATA, tag);
                soul.entityData.set(MOB_YAW, source.yBodyRot);
            }
        }
        return soul;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_ID, -1);
        this.entityData.define(MOB_DATA, new CompoundTag());
        this.entityData.define(MOB_YAW, 0.0F);
    }

    private void setOwnerId(int id) {
        this.entityData.set(OWNER_ID, id);
    }

    @Nullable
    private Player getOwner() {
        return this.level().getEntity(this.entityData.get(OWNER_ID)) instanceof Player player ? player : null;
    }

    @Nullable
    public Entity getReplica() {
        if (this.replica == null && !this.replicaFailed) {
            CompoundTag tag = this.entityData.get(MOB_DATA);
            this.replica = tag.isEmpty() ? null : EntityType.create(tag, this.level()).orElse(null);
            this.replicaFailed = this.replica == null;
        }
        return this.replica;
    }

    public void dropReplica() {
        this.replica = null;
        this.replicaFailed = true;
    }

    public float getReplicaYaw() {
        return this.entityData.get(MOB_YAW);
    }

    public float getReplicaScale(float partialTick) {
        Player owner = this.getOwner();
        if (owner == null || this.tickCount <= RISE_TICKS)
            return 1.0F;
        double distance = handPosition(owner).distanceTo(this.getPosition(partialTick));
        return (float) Mth.clamp(distance / SHRINK_DISTANCE, MIN_SCALE, 1.0D);
    }

    private static Vec3 handPosition(Player player) {
        float side = player.getMainArm() == HumanoidArm.RIGHT ? -1.0F : 1.0F;
        Vec3 sideways = Vec3.directionFromRotation(0.0F, player.yBodyRot + 90.0F * side).scale(0.35D);
        return player.position().add(0.0D, player.getBbHeight() * 0.55D, 0.0D).add(sideways);
    }

    @Override
    public void tick() {
        super.tick();
        Player owner = this.getOwner();
        if (this.level().isClientSide) {
            this.level().addParticle(SOUL_DUST, this.getX() + (this.random.nextDouble() - 0.5D) * 0.2D, this.getY() + (this.random.nextDouble() - 0.5D) * 0.2D, this.getZ() + (this.random.nextDouble() - 0.5D) * 0.2D, 0.0D, 0.0D, 0.0D);
            this.level().addParticle(SOUL_CORE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            if (this.random.nextInt(3) == 0) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.01D, 0.0D);
            }
        } else if (owner == null || !owner.isAlive() || this.tickCount > MAX_LIFE || owner.distanceToSqr(this) > LOST_DISTANCE * LOST_DISTANCE) {
            this.discard();
            return;
        }
        if (owner != null) {
            if (this.tickCount <= RISE_TICKS) {
                float swirl = this.tickCount * 0.6F;
                this.setDeltaMovement(Mth.cos(swirl) * 0.03D, RISE_SPEED, Mth.sin(swirl) * 0.03D);
            } else {
                Vec3 toHand = handPosition(owner).subtract(this.position());
                double distance = toHand.length();
                if (!this.level().isClientSide && distance < ARRIVE_DISTANCE) {
                    this.absorb(owner);
                    return;
                }
                double speed = Math.min(MAX_SPEED, 0.15D + (this.tickCount - RISE_TICKS) * 0.03D);
                Vec3 wanted = toHand.normalize().scale(Math.min(speed, distance));
                this.setDeltaMovement(this.getDeltaMovement().add(wanted.subtract(this.getDeltaMovement()).scale(STEER + (this.tickCount - RISE_TICKS) * 0.02D)));
            }
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    private void absorb(Player owner) {
        ACExSectumsempra.addCharge(owner, this.charge);
        this.level().playSound(null, owner.getX(), owner.getY(), owner.getZ(), ACSoundRegistry.DESOLATE_DAGGER_SUMMON.get(), SoundSource.PLAYERS, 0.6F, 1.6F);
        this.discard();
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }
}
