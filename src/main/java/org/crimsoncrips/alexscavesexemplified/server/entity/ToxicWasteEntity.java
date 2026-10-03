package org.crimsoncrips.alexscavesexemplified.server.entity;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import org.joml.Vector3f;

public class ToxicWasteEntity extends Projectile {

    public static final double GRAVITY = 0.06D;
    public static final double DRAG = 0.99D;
    private static final float DAMAGE = 12.0F;
    private static final double SPLASH_RADIUS = 2.5D;
    private static final double HOMING = 0.06D;
    private static final int IRRADIATED_DURATION = ACExUtils.seconds(15.0F);
    private static final int IRRADIATED_AMPLIFIER = 1;
    private static final double RADIATION_RADIUS = 5.0D;
    private static final int RADIATION_PARTICLES = 40;
    private static final int ACID_LEVEL = 1;
    private static final int ACID_SOURCE_LEVEL = 0;
    private static final int MAX_AGE = ACExUtils.seconds(10.0F);
    private static final DustParticleOptions TRAIL = new DustParticleOptions(new Vector3f(0.45F, 0.9F, 0.15F), 1.5F);

    public static final int WASTE_DRUM = 0;
    public static final int URANIUM = 1;
    public static final int UNREFINED_WASTE = 2;

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(ToxicWasteEntity.class, EntityDataSerializers.INT);

    private int targetId = -1;
    private boolean spillsAcid = true;
    private float acidSourceChance;

    public ToxicWasteEntity(EntityType<? extends ToxicWasteEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(VARIANT, WASTE_DRUM);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public BlockState displayState() {
        return switch (this.entityData.get(VARIANT)) {
            case URANIUM -> ACBlockRegistry.BLOCK_OF_URANIUM.get().defaultBlockState();
            case UNREFINED_WASTE -> ACBlockRegistry.UNREFINED_WASTE.get().defaultBlockState();
            default -> ACBlockRegistry.WASTE_DRUM.get().defaultBlockState();
        };
    }

    public void setSpillsAcid(boolean spillsAcid) {
        this.spillsAcid = spillsAcid;
    }

    public void setAcidSourceChance(float acidSourceChance) {
        this.acidSourceChance = acidSourceChance;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public static Vec3 arcTo(Vec3 from, Vec3 to, int flight) {
        return arcTo(from, to, flight, GRAVITY, DRAG);
    }

    public static Vec3 arcTo(Vec3 from, Vec3 to, int flight, double gravity, double drag) {
        Vec3 delta = to.subtract(from);
        double dragFactor = (1.0D - Math.pow(drag, flight)) / (flight * (1.0D - drag));
        double vy = (delta.y + 0.5D * gravity * flight * flight) / flight;
        return new Vec3(delta.x / flight / dragFactor, vy / dragFactor, delta.z / flight / dragFactor);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = this.getDeltaMovement();
        if (!this.level().isClientSide) {
            HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hit.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hit)) {
                this.onHit(hit);
                return;
            }
            if (motion.y < 0.0D && this.level().getEntity(this.targetId) instanceof LivingEntity target && target.isAlive()) {
                Vec3 toward = target.position().subtract(this.position());
                Vec3 horizontal = new Vec3(toward.x, 0.0D, toward.z);
                if (horizontal.lengthSqr() > 1.0E-4D) {
                    Vec3 desired = horizontal.normalize().scale(Math.max(motion.horizontalDistance(), 0.2D));
                    motion = new Vec3(Mth.lerp(HOMING, motion.x, desired.x), motion.y, Mth.lerp(HOMING, motion.z, desired.z));
                }
            }
        } else {
            this.level().addParticle(TRAIL, this.getRandomX(0.6D), this.getRandomY(), this.getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
        }
        this.setPos(this.position().add(motion));
        this.setDeltaMovement(motion.scale(DRAG).subtract(0.0D, GRAVITY, 0.0D));
        this.updateRotation();
        if (!this.level().isClientSide && this.tickCount > MAX_AGE) {
            this.discard();
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        Entity owner = this.getOwner();
        if (entity instanceof ToxicWasteEntity || (owner != null && entity instanceof PartEntity<?> part && part.getParent() == owner))
            return false;
        return super.canHitEntity(entity);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        this.land(result.getLocation(), result.getEntity().blockPosition());
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.land(result.getLocation(), result.getBlockPos().relative(result.getDirection()));
    }

    private void land(Vec3 at, BlockPos acidPos) {
        Entity owner = this.getOwner();
        DamageSource source = this.damageSources().mobProjectile(this, owner instanceof LivingEntity living ? living : null);
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, new AABB(at, at).inflate(RADIATION_RADIUS))) {
            if (!living.isAlive() || (owner != null && ACExGammaAttacks.isFriendly(owner, living)))
                continue;
            if (living.distanceToSqr(at) <= SPLASH_RADIUS * SPLASH_RADIUS) {
                living.hurt(source, DAMAGE);
            }
            if (living.distanceToSqr(at) <= RADIATION_RADIUS * RADIATION_RADIUS) {
                living.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED, IRRADIATED_DURATION, IRRADIATED_AMPLIFIER));
            }
        }
        if (this.spillsAcid && EventHooks.canEntityGrief(this.level(), owner != null ? owner : this) && this.level().getBlockState(acidPos).canBeReplaced() && this.level().getFluidState(acidPos).isEmpty()) {
            this.level().setBlockAndUpdate(acidPos, ACBlockRegistry.ACID.get().defaultBlockState().setValue(LiquidBlock.LEVEL, this.random.nextFloat() < this.acidSourceChance ? ACID_SOURCE_LEVEL : ACID_LEVEL));
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(TRAIL, at.x, at.y + 0.5D, at.z, RADIATION_PARTICLES, RADIATION_RADIUS * 0.5D, 0.5D, RADIATION_RADIUS * 0.5D, 0.0D);
        }
        this.level().levelEvent(2001, BlockPos.containing(at), Block.getId(this.displayState()));
        this.discard();
    }
}
