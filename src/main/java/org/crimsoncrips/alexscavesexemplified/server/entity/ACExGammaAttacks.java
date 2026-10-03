package org.crimsoncrips.alexscavesexemplified.server.entity;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.phys.AABB;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACExParticleRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import net.minecraft.world.level.levelgen.Heightmap;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;

import javax.annotation.Nullable;
import java.util.function.DoublePredicate;

public class ACExGammaAttacks {

    public static final int WASTE_SPIT = 0;
    public static final int SPIKE_LASER = 1;
    public static final int TOXIC_BARREL = 2;
    public static final int NUKE_BEAM = 3;
    public static final int SPIKE_STORM = 4;
    public static final int CONTROLLED_LASER = 5;
    public static final int ABILITY_COUNT = 6;
    public static final int LEAP = 6;
    public static final int CONTROLLED_LASER_TIME = ACExUtils.seconds(10.0F);
    private static final double RIDER_AIM_RANGE = 128.0D;
    public static final float GAMMA_MODEL_SCALE = 1.5F;
    private static final double MOUTH_UP = 9.4D;
    private static final double MOUTH_FORWARD = 3.5D;
    private static final double BEAM_UP = 10.04D;
    private static final double BEAM_FORWARD = 1.495D;
    private static final double EYE_UP = 10.7D;
    private static final double EYE_FORWARD = 2.4D;
    private static final double SWIM_EYE_DOWN = 6.5D;
    private static final double SWIM_EYE_FORWARD = 6.0D;
    public static final float RIDER_LIFT = 1.0F;

    public static final int WASTE_CHANCE = 20;
    public static final double WASTE_MIN_RANGE = 8.0D;
    public static final double WASTE_MAX_RANGE = 64.0D;
    public static final int WASTE_SPIT_TICK = ACExUtils.seconds(0.5F);
    private static final int WASTE_COUNT = 5;
    private static final double WASTE_SPREAD = 3.0D;
    private static final int WASTE_FLIGHT = ACExUtils.seconds(1.75F);
    private static final int WASTE_FLIGHT_EXTRA = ACExUtils.seconds(0.75F);
    private static final int WASTE_URANIUM_CHANCE = 3;

    public static final int SPIKE_CHANCE = 30;
    public static final double SPIKE_TRIGGER_RANGE = 30.0D;
    public static final double SPIKE_NEAR_RANGE = 12.0D;
    public static final int LASER_WINDUP = ACExUtils.seconds(1.0F);
    public static final int LASER_DURATION = ACExUtils.seconds(1.5F);
    public static final double LASER_REACH = 8.0D;
    public static final int RING_INTERVAL = ACExUtils.seconds(0.1F);
    public static final int RING_COUNT = 8;
    private static final double RING_START = 2.0D;
    private static final double RING_SPACING = 3.0D;
    private static final double SPIKE_SPACING = 3.5D;
    private static final int SPIKE_LIFESPAN = ACExUtils.seconds(2.5F);
    private static final int GROUND_SCAN = 8;
    private static final double STOMP_SPIKE_RADIUS = 3.0D;
    private static final int STOMP_SPIKE_LIFESPAN = ACExUtils.seconds(2.0F);

    public static final int SPIKE_STORM_CHANCE = 40;

    public static final int LEAP_CHANCE = 20;
    public static final double LEAP_TRIGGER_RANGE = 10.0D;
    public static final int LEAP_TIME = ACExUtils.seconds(4.0F);
    public static final int LEAP_LANDING_TIMEOUT = ACExUtils.seconds(3.0F);
    private static final double LEAP_DISTANCE = 100.0D;
    private static final double LEAP_HEIGHT = 40.0D;
    private static final double LEAP_GRAVITY = 8.0D * LEAP_HEIGHT / ((double) LEAP_TIME * LEAP_TIME);
    private static final double RIDER_LEAP_DISTANCE = 70.0D;
    private static final double RIDER_LEAP_HEIGHT = LEAP_HEIGHT * 1.3D;
    private static final float RIDER_LEAP_MIN_CHARGE = 0.25F;
    private static final double RIDER_LEAP_MIN_DISTANCE = 0.2D;
    public static final double SPIKE_STORM_RANGE = 40.0D;
    public static final int SPIKE_STORM_DURATION = ACExUtils.seconds(10.0F);
    public static final int SPIKE_STORM_TOGGLE = ACExUtils.seconds(2.0F);
    private static final int SPIKE_STORM_RINGS = 20;
    private static final double SPIKE_STORM_RING_START = 2.0D;
    private static final double SPIKE_STORM_RING_SPACING = 3.0D;
    private static final double SPIKE_STORM_LASER_RADIUS = 10.0D;
    private static final double SPIKE_STORM_LASER_TURN = Math.PI * 2.0D / ACExUtils.seconds(3.0F);
    private static final double SPIKE_STORM_WEDGE = Math.PI * 0.10D;

    public static final int BARREL_CHANCE = 30;
    public static final double BARREL_MIN_RANGE = 16.0D;
    public static final double BARREL_MAX_RANGE = 80.0D;
    public static final int BARREL_RISE = ACExUtils.seconds(1.0F);
    public static final int BARREL_FUSE = ACExUtils.seconds(2.0F);
    private static final double BARREL_HEIGHT = 18.0D;
    private static final double BARREL_FORWARD = 0.35D;

    public static final int NUKE_BEAM_CHANCE = 40;
    public static final double NUKE_BEAM_MIN_RANGE = 30.0D;
    public static final double NUKE_BEAM_MAX_RANGE = 120.0D;
    public static final int NUKE_BEAM_SWEEP = ACExUtils.seconds(2.0F);
    public static final int NUKE_BEAM_HURT_INTERVAL = ACExUtils.seconds(0.15F);
    public static final float NUKE_BEAM_HURT_RADIUS = 4.0F;
    public static final float NUKE_BEAM_DAMAGE = 10.0F;
    private static final double NUKE_BEAM_START = 4.0D;
    private static final double NUKE_BEAM_REACH = 100.0D;
    private static final float MINI_NUKE_SIZE = 1.4F;
    private static final float MINI_NUKE_POWER = 6.0F * 1.5F * MINI_NUKE_SIZE;
    private static final double MINI_NUKE_RADIUS = 10.0D * MINI_NUKE_SIZE;
    private static final float MINI_NUKE_DAMAGE = 60.0F;
    private static final float MINI_NUKE_FLING = 0.165F;
    private static final int MINI_NUKE_IRRADIATED_DURATION = ACExUtils.seconds(30.0F);
    private static final int MINI_NUKE_IRRADIATED_AMPLIFIER = 1;
    private static final double MINI_NUKE_CLOUD_SCALE = 0.6D * MINI_NUKE_SIZE;
    public static final float CONTROLLED_NUKE_SIZE = 1.2F;
    public static final float CONTROLLED_NUKE_DURATION = 1.1F;
    private static final ExplosionDamageCalculator BLOCKS_ONLY = new ExplosionDamageCalculator() {
        @Override
        public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
            return false;
        }

        @Override
        public float getKnockbackMultiplier(Entity entity) {
            return 0.0F;
        }
    };

    public record BeamMerge(long start, int duration) {
        public static final BeamMerge NONE = new BeamMerge(0L, 0);
        public static final StreamCodec<ByteBuf, BeamMerge> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_LONG, BeamMerge::start,
                ByteBufCodecs.VAR_INT, BeamMerge::duration,
                BeamMerge::new);
    }

    public static void startBeamMerge(TremorzillaEntity tremorzilla, int duration) {
        tremorzilla.setData(ACExAttachments.GAMMA_BEAM_MERGE, new BeamMerge(tremorzilla.level().getGameTime(), duration));
    }

    public static void stopBeamMerge(TremorzillaEntity tremorzilla) {
        ACExAttachments.set(tremorzilla, ACExAttachments.GAMMA_BEAM_MERGE, BeamMerge.NONE);
    }

    public static float beamMerge(TremorzillaEntity tremorzilla, float partialTicks) {
        BeamMerge merge = tremorzilla.getData(ACExAttachments.GAMMA_BEAM_MERGE);
        if (merge.duration() <= 0)
            return 0.0F;
        float progress = Mth.clamp((tremorzilla.level().getGameTime() - merge.start() + partialTicks) / merge.duration(), 0.0F, 1.0F);
        return progress * progress * (3.0F - 2.0F * progress);
    }

    public static int cooldownLength(int ability) {
        return ACExUtils.seconds(switch (ability) {
            case WASTE_SPIT -> 3;
            case SPIKE_LASER -> 7;
            case TOXIC_BARREL -> 15;
            case NUKE_BEAM -> 25;
            case SPIKE_STORM -> 30;
            case LEAP -> 60;
            default -> 0;
        });
    }

    public static int cooldownRemaining(TremorzillaEntity tremorzilla, int ability) {
        return (int) Math.max(0L, tremorzilla.getData(ACExAttachments.GAMMA_COOLDOWNS).end(ability) - tremorzilla.level().getGameTime());
    }

    public static float cooldownProgress(TremorzillaEntity tremorzilla, int ability) {
        int length = tremorzilla.getData(ACExAttachments.GAMMA_COOLDOWNS).length(ability);
        return length <= 0 ? 0.0F : Mth.clamp(cooldownRemaining(tremorzilla, ability) / (float) length, 0.0F, 1.0F);
    }

    public static void startCooldown(TremorzillaEntity tremorzilla, int ability) {
        int length = cooldownLength(ability);
        tremorzilla.setData(ACExAttachments.GAMMA_COOLDOWNS, tremorzilla.getData(ACExAttachments.GAMMA_COOLDOWNS).with(ability, tremorzilla.level().getGameTime() + length, length));
    }

    public static boolean beamCharged(TremorzillaEntity tremorzilla) {
        return tremorzilla.getMeterAmount() >= 1.0F;
    }

    public static float modelScale(TremorzillaEntity tremorzilla) {
        return tremorzilla.getScale() * (tremorzilla.getData(ACExAttachments.GAMMA) ? GAMMA_MODEL_SCALE : 1.0F);
    }

    public static Vec3 mouthPosition(TremorzillaEntity tremorzilla) {
        double scale = modelScale(tremorzilla);
        return tremorzilla.position().add(Vec3.directionFromRotation(0.0F, tremorzilla.getYHeadRot()).scale(MOUTH_FORWARD * scale)).add(0.0D, MOUTH_UP * scale, 0.0D);
    }

    public static Vec3 beamOrigin(TremorzillaEntity tremorzilla, float partialTicks) {
        double scale = modelScale(tremorzilla);
        float bodyYaw = Mth.rotLerp(partialTicks, tremorzilla.yBodyRotO, tremorzilla.yBodyRot);
        return tremorzilla.getPosition(partialTicks).add(Vec3.directionFromRotation(0.0F, bodyYaw).scale(BEAM_FORWARD * scale)).add(0.0D, BEAM_UP * scale, 0.0D);
    }

    public static Vec3 riderCrosshair(TremorzillaEntity tremorzilla, Player rider) {
        return riderBlockAim(tremorzilla, rider);
    }

    public static Vec3 eyePosition(TremorzillaEntity tremorzilla, float swimAmount) {
        double scale = modelScale(tremorzilla);
        double forward = (EYE_FORWARD + SWIM_EYE_FORWARD * swimAmount) * scale;
        double up = (EYE_UP - SWIM_EYE_DOWN * swimAmount) * scale;
        return tremorzilla.position().add(Vec3.directionFromRotation(0.0F, tremorzilla.yBodyRot).scale(forward)).add(0.0D, up, 0.0D);
    }

    public static boolean canRiderUse(TremorzillaEntity tremorzilla, Player rider) {
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && tremorzilla.getData(ACExAttachments.GAMMA) && tremorzilla.isTame() && tremorzilla.isOwnedBy(rider) && !tremorzilla.isBaby() && rider.getVehicle() == tremorzilla;
    }

    @Nullable
    public static LivingEntity riderTarget(TremorzillaEntity tremorzilla, Player rider) {
        Vec3 eye = rider.getEyePosition();
        Vec3 end = riderBlockAim(tremorzilla, rider);
        EntityHitResult hit = ProjectileUtil.getEntityHitResult(tremorzilla.level(), rider, eye, end, new AABB(eye, end).inflate(1.0D), entity -> entity instanceof LivingEntity living && living.isAlive() && !living.isSpectator() && living != tremorzilla && living.getVehicle() != tremorzilla && !tremorzilla.isAlliedTo(living));
        return hit != null ? (LivingEntity) hit.getEntity() : null;
    }

    public static Vec3 riderAim(TremorzillaEntity tremorzilla, Player rider, @Nullable LivingEntity target) {
        return target != null ? target.position() : riderBlockAim(tremorzilla, rider);
    }

    private static Vec3 riderBlockAim(TremorzillaEntity tremorzilla, Player rider) {
        Vec3 eye = rider.getEyePosition();
        Vec3 end = eye.add(rider.getViewVector(1.0F).scale(RIDER_AIM_RANGE));
        BlockHitResult hit = tremorzilla.level().clip(new ClipContext(eye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, rider));
        return hit.getType() == HitResult.Type.MISS ? end : hit.getLocation();
    }

    public static boolean isFriendly(Entity owner, LivingEntity living) {
        return living == owner || living.getVehicle() == owner || owner.isAlliedTo(living) || living.isAlliedTo(owner);
    }

    public static void spitWaste(TremorzillaEntity tremorzilla, Vec3 target, @Nullable LivingEntity homing) {
        Level level = tremorzilla.level();
        RandomSource random = tremorzilla.getRandom();
        Vec3 mouth = mouthPosition(tremorzilla);
        for (int i = 0; i < WASTE_COUNT; i++) {
            ToxicWasteEntity waste = ACExEntityRegistry.TOXIC_WASTE.get().create(level);
            if (waste == null)
                continue;
            Vec3 aim = target.add((random.nextDouble() * 2.0D - 1.0D) * WASTE_SPREAD, 0.0D, (random.nextDouble() * 2.0D - 1.0D) * WASTE_SPREAD);
            waste.setOwner(tremorzilla);
            waste.setTargetId(homing == null ? -1 : homing.getId());
            waste.setSpillsAcid(false);
            waste.setVariant(random.nextInt(WASTE_URANIUM_CHANCE) == 0 ? ToxicWasteEntity.URANIUM : ToxicWasteEntity.WASTE_DRUM);
            waste.setPos(mouth);
            waste.setDeltaMovement(ToxicWasteEntity.arcTo(mouth, aim, WASTE_FLIGHT + random.nextInt(WASTE_FLIGHT_EXTRA)));
            level.addFreshEntity(waste);
        }
        level.playSound(null, BlockPos.containing(mouth), SoundEvents.LLAMA_SPIT, SoundSource.HOSTILE, 6.0F, 0.4F);
    }

    @Nullable
    public static ToxicBarrelEntity spitBarrel(TremorzillaEntity tremorzilla, Vec3 target, @Nullable LivingEntity homing, boolean aboveTarget) {
        Level level = tremorzilla.level();
        ToxicBarrelEntity barrel = ACExEntityRegistry.TOXIC_BARREL.get().create(level);
        if (barrel == null)
            return null;
        Vec3 mouth = mouthPosition(tremorzilla);
        Vec3 apex = (aboveTarget ? target : mouth.lerp(target, BARREL_FORWARD)).add(0.0D, BARREL_HEIGHT, 0.0D);
        barrel.setOwner(tremorzilla);
        barrel.aimAt(target, homing);
        barrel.setPos(mouth.subtract(0.0D, barrel.getBbHeight() * 0.5D, 0.0D));
        barrel.setDeltaMovement(ToxicWasteEntity.arcTo(mouth, apex, BARREL_RISE, ToxicBarrelEntity.GRAVITY, ToxicBarrelEntity.DRAG));
        level.addFreshEntity(barrel);
        level.playSound(null, BlockPos.containing(mouth), SoundEvents.LLAMA_SPIT, SoundSource.HOSTILE, 8.0F, 0.2F);
        return barrel;
    }

    public static Vec3 laserPoint(TremorzillaEntity tremorzilla, Vec3 origin) {
        Vec3 forward = Vec3.directionFromRotation(0.0F, tremorzilla.yBodyRot).scale(LASER_REACH * tremorzilla.getScale());
        Vec3 ground = groundAt(tremorzilla.level(), origin.add(forward));
        return ground != null ? ground : origin.add(forward);
    }

    public static void spawnSpikeRing(TremorzillaEntity tremorzilla, Vec3 origin, int ring) {
        double radius = tremorzilla.getBbWidth() * 0.5D + RING_START + ring * RING_SPACING;
        spawnSpikeCircle(tremorzilla, origin, radius, SPIKE_LIFESPAN + ring * RING_INTERVAL, 0.5F + ring * 0.05F, 0, angle -> true);
    }

    public static void spawnStompSpikes(TremorzillaEntity tremorzilla, Vec3 foot) {
        spawnSpikeCircle(tremorzilla, foot, STOMP_SPIKE_RADIUS * tremorzilla.getScale(), STOMP_SPIKE_LIFESPAN, 0.6F, 0, angle -> true);
    }

    public static void spawnSpikeStorm(TremorzillaEntity tremorzilla, Vec3 origin) {
        Vec3 forward = Vec3.directionFromRotation(0.0F, tremorzilla.yBodyRot);
        double forwardAngle = Math.atan2(forward.z, forward.x);
        DoublePredicate frontAndBack = angle -> ((int) Math.floor((angle - forwardAngle + SPIKE_STORM_WEDGE * 0.5D) / SPIKE_STORM_WEDGE) & 1) == 0;
        for (int ring = 0; ring < SPIKE_STORM_RINGS; ring++) {
            double radius = tremorzilla.getBbWidth() * 0.5D + SPIKE_STORM_RING_START + ring * SPIKE_STORM_RING_SPACING;
            spawnSpikeCircle(tremorzilla, origin, radius, SPIKE_STORM_DURATION, 0.5F + ring * 0.05F, SPIKE_STORM_TOGGLE, frontAndBack);
        }
    }

    public static Vec3 spikeStormLaserPoint(TremorzillaEntity tremorzilla, Vec3 origin, int ticks) {
        double angle = ticks * SPIKE_STORM_LASER_TURN;
        double radius = tremorzilla.getBbWidth() * 0.5D + SPIKE_STORM_LASER_RADIUS;
        Vec3 flat = origin.add(Math.cos(angle) * radius, 0.0D, Math.sin(angle) * radius);
        Vec3 ground = groundAt(tremorzilla.level(), flat);
        return ground != null ? ground : flat;
    }

    private static void spawnSpikeCircle(TremorzillaEntity tremorzilla, Vec3 origin, double radius, int lifespan, float pitch, int toggleInterval, DoublePredicate startsOut) {
        Level level = tremorzilla.level();
        RandomSource random = tremorzilla.getRandom();
        int count = Math.max(6, (int) Math.round(Math.PI * 2.0D * radius / SPIKE_SPACING));
        double offset = random.nextDouble() * Math.PI * 2.0D;
        for (int i = 0; i < count; i++) {
            double angle = offset + Math.PI * 2.0D * (i + (random.nextDouble() - 0.5D) * 0.4D) / count;
            Vec3 ground = groundAt(level, origin.add(Math.cos(angle) * radius, 0.0D, Math.sin(angle) * radius));
            if (ground == null)
                continue;
            SpikesEntity spikes = ACExEntityRegistry.SPIKES.get().create(level);
            if (spikes == null)
                continue;
            spikes.setPos(ground);
            spikes.setYRot(random.nextFloat() * 360.0F);
            spikes.setVariant(random.nextInt(SpikesEntity.VARIANTS));
            spikes.setLifespan(lifespan);
            if (toggleInterval > 0) {
                spikes.setToggling(toggleInterval, startsOut.test(angle));
            }
            spikes.setOwner(tremorzilla);
            level.addFreshEntity(spikes);
        }
        level.playSound(null, BlockPos.containing(origin), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.HOSTILE, 6.0F, pitch);
    }

    public static Vec3 leapLanding(TremorzillaEntity tremorzilla, LivingEntity target) {
        double angle = tremorzilla.getRandom().nextDouble() * Math.PI * 2.0D;
        double x = target.getX() + Math.cos(angle) * LEAP_DISTANCE;
        double z = target.getZ() + Math.sin(angle) * LEAP_DISTANCE;
        return groundColumn(tremorzilla, x, z);
    }

    public static Vec3 riderLeapLanding(TremorzillaEntity tremorzilla, Player rider, int power) {
        double distance = RIDER_LEAP_DISTANCE * Mth.lerp(riderLeapCharge(power), RIDER_LEAP_MIN_DISTANCE, 1.0D);
        Vec3 flat = tremorzilla.position().add(Vec3.directionFromRotation(0.0F, rider.getYRot()).scale(distance));
        return groundColumn(tremorzilla, flat.x, flat.z);
    }

    public static double riderLeapHeight(int power) {
        return RIDER_LEAP_HEIGHT * riderLeapCharge(power);
    }

    private static float riderLeapCharge(int power) {
        return Mth.clamp(power / 100.0F, RIDER_LEAP_MIN_CHARGE, 1.0F);
    }

    public static int leapTime(double height) {
        return Math.max(1, (int) Math.round(Math.sqrt(8.0D * height / LEAP_GRAVITY)));
    }

    public static boolean canRiderLeap(TremorzillaEntity tremorzilla) {
        return tremorzilla.onGround() && !tremorzilla.isTremorzillaSwimming() && !tremorzilla.isFiring();
    }

    private static Vec3 groundColumn(TremorzillaEntity tremorzilla, double x, double z) {
        Level level = tremorzilla.level();
        BlockPos column = BlockPos.containing(x, 0.0D, z);
        double y = level.hasChunkAt(column) ? level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, column.getX(), column.getZ()) : tremorzilla.getY();
        return new Vec3(x, y, z);
    }

    public static double leapGravity() {
        return LEAP_GRAVITY;
    }

    public static Vec3 leapVelocity(Vec3 from, Vec3 landing, int time) {
        Vec3 horizontal = new Vec3(landing.x - from.x, 0.0D, landing.z - from.z).scale(1.0D / time);
        double vertical = (landing.y - from.y + 0.5D * LEAP_GRAVITY * time * time) / time;
        return new Vec3(horizontal.x, vertical, horizontal.z);
    }

    public static Vec3 nukeBeamPoint(TremorzillaEntity tremorzilla, Vec3 origin, Vec3 target, float progress) {
        Vec3 toTarget = target.subtract(origin).multiply(1.0D, 0.0D, 1.0D);
        Vec3 direction = toTarget.lengthSqr() < 1.0E-4D ? Vec3.directionFromRotation(0.0F, tremorzilla.yBodyRot) : toTarget.normalize();
        double startDistance = NUKE_BEAM_START * tremorzilla.getScale();
        double endDistance = Math.max(startDistance, NUKE_BEAM_REACH);
        Vec3 flat = origin.add(direction.scale(Mth.lerp(progress, startDistance, endDistance)));
        double height = Mth.lerp(progress, origin.y, target.y);
        Vec3 ground = groundAt(tremorzilla.level(), new Vec3(flat.x, height, flat.z));
        return ground != null ? ground : new Vec3(flat.x, height, flat.z);
    }

    public static void miniNuke(TremorzillaEntity tremorzilla, Vec3 at) {
        miniNuke(tremorzilla, at, 1.0F, 1.0F);
    }

    public static void miniNuke(TremorzillaEntity tremorzilla, Vec3 at, float size, float duration) {
        if (!(tremorzilla.level() instanceof ServerLevel level))
            return;
        double radius = MINI_NUKE_RADIUS * size;
        level.explode(tremorzilla, null, BLOCKS_ONLY, at.x, at.y, at.z, MINI_NUKE_POWER * size, false, Level.ExplosionInteraction.MOB);
        DamageSource source = ACDamageTypes.causeNukeDamage(level.registryAccess());
        for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, new AABB(at, at).inflate(radius))) {
            if (living instanceof TremorzillaEntity || living.isSpectator() || !living.isAlive() || isFriendly(tremorzilla, living))
                continue;
            double distance = living.position().distanceTo(at);
            if (distance > radius)
                continue;
            float damage = MINI_NUKE_DAMAGE * (float) (1.0D - distance / radius);
            float fling = living instanceof Player ? MINI_NUKE_FLING * 0.5F : MINI_NUKE_FLING;
            if (living.getType().is(ACTagRegistry.RESISTS_RADIATION)) {
                damage *= 0.25F;
                fling *= 0.1F;
            }
            living.hurt(source, damage);
            living.setDeltaMovement(living.position().subtract(at).add(0.0D, 0.3D, 0.0D).normalize().scale(damage * 0.1F * fling));
            living.hurtMarked = true;
            living.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED, MINI_NUKE_IRRADIATED_DURATION, MINI_NUKE_IRRADIATED_AMPLIFIER));
        }
        for (ServerPlayer player : level.players()) {
            level.sendParticles(player, ACExParticleRegistry.MINI_GAMMA_MUSHROOM_CLOUD.get(), true, at.x, at.y + 1.0D, at.z, 0, MINI_NUKE_CLOUD_SCALE * size, duration, 0.0D, 1.0D);
        }
    }

    @Nullable
    private static Vec3 groundAt(Level level, Vec3 pos) {
        BlockPos.MutableBlockPos cursor = BlockPos.containing(pos.x, pos.y + GROUND_SCAN, pos.z).mutable();
        for (int i = 0; i <= GROUND_SCAN * 2; i++) {
            BlockPos below = cursor.below();
            if (!level.getBlockState(below).getCollisionShape(level, below).isEmpty() && level.getBlockState(cursor).getCollisionShape(level, cursor).isEmpty() && level.getFluidState(cursor).isEmpty())
                return new Vec3(pos.x, cursor.getY(), pos.z);
            cursor.move(0, -1, 0);
        }
        return null;
    }
}
