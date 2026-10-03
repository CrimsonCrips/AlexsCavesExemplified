package org.crimsoncrips.alexscavesexemplified.server.entity;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class SpikesEntity extends Entity {

    public static final int VARIANTS = 3;
    public static final int RISE_TICKS = ACExUtils.seconds(0.25F);
    private static final float DAMAGE = 8.0F;
    private static final float HURT_PROGRESS = 0.6F;

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SpikesEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(SpikesEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TOGGLE_INTERVAL = SynchedEntityData.defineId(SpikesEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> STARTS_OUT = SynchedEntityData.defineId(SpikesEntity.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;

    public SpikesEntity(EntityType<? extends SpikesEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(VARIANT, 0);
        builder.define(LIFESPAN, ACExUtils.seconds(3.0F));
        builder.define(TOGGLE_INTERVAL, 0);
        builder.define(STARTS_OUT, true);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Mth.clamp(variant, 0, VARIANTS - 1));
    }

    public int getLifespan() {
        return this.entityData.get(LIFESPAN);
    }

    public void setLifespan(int lifespan) {
        this.entityData.set(LIFESPAN, lifespan);
    }

    public void setToggling(int interval, boolean startsOut) {
        this.entityData.set(TOGGLE_INTERVAL, interval);
        this.entityData.set(STARTS_OUT, startsOut);
    }

    public void setOwner(@Nullable Entity owner) {
        this.cachedOwner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner == null && this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            this.cachedOwner = serverLevel.getEntity(this.ownerUUID);
        }
        return this.cachedOwner;
    }

    public float heightProgress(float partialTicks) {
        float age = this.tickCount + partialTicks;
        float rising = age / RISE_TICKS;
        float retracting = (this.getLifespan() - age) / RISE_TICKS;
        float envelope = Mth.clamp(Math.min(rising, retracting), 0.0F, 1.0F);
        int interval = this.entityData.get(TOGGLE_INTERVAL);
        if (interval <= 0)
            return envelope;
        int cycle = (int) (age / interval);
        boolean out = (cycle % 2 == 0) == this.entityData.get(STARTS_OUT);
        if (cycle == 0)
            return out ? envelope : 0.0F;
        float shift = Mth.clamp((age - cycle * interval) / RISE_TICKS, 0.0F, 1.0F);
        return Math.min(out ? shift : 1.0F - shift, Mth.clamp(retracting, 0.0F, 1.0F));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide)
            return;
        if (this.tickCount >= this.getLifespan()) {
            this.discard();
            return;
        }
        if (this.heightProgress(1.0F) < HURT_PROGRESS)
            return;
        Entity owner = this.getOwner();
        DamageSource source = this.damageSources().mobProjectile(this, owner instanceof LivingEntity living ? living : null);
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())) {
            if (!living.isAlive() || living.isSpectator() || (owner != null && ACExGammaAttacks.isFriendly(owner, living)))
                continue;
            living.hurt(source, DAMAGE);
        }
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
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }
}
