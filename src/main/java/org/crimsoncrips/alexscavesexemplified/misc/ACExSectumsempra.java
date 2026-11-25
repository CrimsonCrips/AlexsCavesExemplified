package org.crimsoncrips.alexscavesexemplified.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.DesolateDaggerEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import org.crimsoncrips.alexscavesexemplified.backport.EntityTickEvent;
import org.crimsoncrips.alexscavesexemplified.backport.ServerTickEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.entity.SiphonedSoulEntity;

import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ACExSectumsempra {

    public static final float CHARGE_PER_KILL = 0.1F;
    public static final float INCARNATE_CHARGE_PER_KILL = 0.2F;
    public static final float REFUND = 0.2F;
    public static final float INCARNATE_REFUND = 0.4F;
    public static final int DURATION = ACExUtils.seconds(4.0F);
    private static final int SUMMON_TICKS = ACExUtils.seconds(3.0F);
    private static final int STAB_TICKS = 5;
    private static final int DAGGERS = 40;
    private static final float DAMAGE = 40.0F;
    private static final double MIN_DISTANCE = 1.0D;
    private static final double MAX_DISTANCE = 4.0D;
    private static final double WALL_MARGIN = 0.4D;
    private static final int PLACEMENT_ATTEMPTS = 12;
    public static final int SETTLE_TICKS = ACExUtils.seconds(0.5F);
    public static final float SETTLE_SPIN_DEGREES = 720.0F;
    private static final int BURST_PARTICLES = 28;
    private static final double BURST_SPEED = 0.35D;
    private static final DustParticleOptions BURST_DUST = new DustParticleOptions(new Vector3f(0.75F, 0.02F, 0.05F), 1.3F);

    private static final List<Cast> CASTS = new ArrayList<>();
    private static boolean applyingDamage;

    private static class Cast {
        private final ServerLevel level;
        private final int targetId;
        private final UUID casterId;
        private final Vec3 lockPos;
        private final ItemStack dagger;
        private int ticks;
        private int spawned;
        private boolean capturing = true;
        private float storedDamage;

        private Cast(ServerLevel level, LivingEntity target, Player caster, ItemStack dagger) {
            this.level = level;
            this.targetId = target.getId();
            this.casterId = caster.getUUID();
            this.lockPos = target.position();
            this.dagger = dagger.copy();
        }
    }

    public static boolean enabled() {
        return AlexsCavesExemplified.COMMON_CONFIG.SECTUMSEMPRA_ENABLED.get();
    }

    public static boolean wearsDarkness(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.HOOD_OF_DARKNESS.get()) && player.getItemBySlot(EquipmentSlot.CHEST).is(ACItemRegistry.CLOAK_OF_DARKNESS.get());
    }

    public static float getCharge(Player player) {
        return ACExData.get(player, ACExAttachments.SECTUMSEMPRA_CHARGE);
    }

    public static boolean isEmpowered(Player player) {
        return getCharge(player) >= 1.0F;
    }

    public static void addCharge(Player player, float amount) {
        ACExAttachments.set(player, ACExAttachments.SECTUMSEMPRA_CHARGE, Mth.clamp(getCharge(player) + amount, 0.0F, 1.0F));
    }

    public static boolean isLocked(Entity entity) {
        return ACExData.get(entity, ACExAttachments.SECTUMSEMPRA_LOCK);
    }

    public static void onKill(LivingDeathEvent event) {
        if (applyingDamage || !enabled() || event.getEntity().level().isClientSide)
            return;
        if (event.getSource().getEntity() instanceof Player player && event.getSource().getDirectEntity() == player && player.getMainHandItem().is(ACItemRegistry.DESOLATE_DAGGER.get()) && wearsDarkness(player)) {
            LivingEntity dead = event.getEntity();
            float charge = player.hasEffect(ACEffectRegistry.DARKNESS_INCARNATE.get()) ? INCARNATE_CHARGE_PER_KILL : CHARGE_PER_KILL;
            dead.level().addFreshEntity(SiphonedSoulEntity.create(dead.level(), center(dead), player, charge, dead));
        }
    }

    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (!enabled() || player.level().isClientSide || !(player.level() instanceof ServerLevel level) || !(event.getTarget() instanceof LivingEntity target))
            return;
        if (!target.isAlive() || isLocked(target) || !isEmpowered(player) || !player.getMainHandItem().is(ACItemRegistry.DESOLATE_DAGGER.get()))
            return;
        ACExAttachments.set(player, ACExAttachments.SECTUMSEMPRA_CHARGE, 0.0F);
        ACExAttachments.set(target, ACExAttachments.SECTUMSEMPRA_LOCK, true);
        target.setDeltaMovement(Vec3.ZERO);
        CASTS.add(new Cast(level, target, player, player.getMainHandItem()));
        burst(level, center(target), target.getRandom());
        level.playSound(null, target.getX(), target.getY(), target.getZ(), ACSoundRegistry.DESOLATE_DAGGER_SUMMON.get(), SoundSource.PLAYERS, 1.5F, 0.6F);
    }

    public static void onIncomingDamage(LivingAttackEvent event) {
        if (event.getEntity().level().isClientSide)
            return;
        for (Cast cast : CASTS) {
            if (cast.capturing && cast.targetId == event.getEntity().getId() && event.getSource().getEntity() instanceof Player caster && caster.getUUID().equals(cast.casterId) && event.getSource().getDirectEntity() == caster) {
                cast.storedDamage += event.getAmount();
                cast.capturing = false;
                event.setCanceled(true);
                return;
            }
        }
    }

    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof Player) && event.getEntity() instanceof LivingEntity living && isLocked(living) && !living.isDeadOrDying()) {
            event.setCanceled(true);
        }
    }

    public static void tick(ServerTickEvent.Post event) {
        Iterator<Cast> iterator = CASTS.iterator();
        while (iterator.hasNext()) {
            Cast cast = iterator.next();
            Entity entity = cast.level.getEntity(cast.targetId);
            if (!(entity instanceof LivingEntity target) || target.isRemoved() || target.isDeadOrDying()) {
                if (entity != null) {
                    release(entity);
                }
                if (entity instanceof LivingEntity dead && dead.isDeadOrDying()) {
                    refund(cast, dead);
                }
                iterator.remove();
                continue;
            }
            cast.capturing = false;
            cast.ticks++;
            hold(target, cast.lockPos);
            summon(cast, target);
            if (cast.ticks >= DURATION) {
                release(target);
                strike(cast, target);
                iterator.remove();
            }
        }
    }

    public static void clear(ServerStoppedEvent event) {
        CASTS.clear();
    }

    private static void hold(LivingEntity target, Vec3 lockPos) {
        target.setDeltaMovement(Vec3.ZERO);
        target.fallDistance = 0.0F;
        if (target instanceof ServerPlayer player) {
            player.connection.teleport(lockPos.x, lockPos.y, lockPos.z, player.getYRot(), player.getXRot());
        } else {
            target.setPos(lockPos);
        }
    }

    private static void summon(Cast cast, LivingEntity target) {
        int due = Math.min(DAGGERS, Mth.ceil(DAGGERS * Math.min(1.0F, cast.ticks / (float) SUMMON_TICKS)));
        RandomSource random = target.getRandom();
        while (cast.spawned < due) {
            DesolateDaggerEntity dagger = ACEntityRegistry.DESOLATE_DAGGER.get().create(cast.level);
            if (dagger == null)
                return;
            Vec3 center = center(target);
            Vec3 offset = placement(cast.level, center, random);
            dagger.setPos(center);
            dagger.setTargetId(target.getId());
            dagger.setItemStack(cast.dagger);
            dagger.orbitFor = Math.max(0, DURATION - STAB_TICKS - cast.ticks);
            ACExData.set(dagger, ACExAttachments.SECTUMSEMPRA_OFFSET, offset);
            cast.level.addFreshEntity(dagger);
            cast.spawned++;
        }
    }

    private static void burst(ServerLevel level, Vec3 center, RandomSource random) {
        for (int i = 0; i < BURST_PARTICLES; i++) {
            Vec3 direction = new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize();
            level.sendParticles(BURST_DUST, center.x, center.y, center.z, 0, direction.x, direction.y, direction.z, BURST_SPEED * (0.5D + random.nextDouble()));
        }
    }

    public static float settle(Entity dagger, float partialTick) {
        float progress = Mth.clamp((dagger.tickCount + partialTick) / SETTLE_TICKS, 0.0F, 1.0F);
        float remaining = 1.0F - progress;
        return 1.0F - remaining * remaining * remaining;
    }

    public static Vec3 center(Entity target) {
        return target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D);
    }

    private static Vec3 placement(ServerLevel level, Vec3 center, RandomSource random) {
        Vec3 best = null;
        for (int i = 0; i < PLACEMENT_ATTEMPTS; i++) {
            Vec3 direction = new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
            if (direction.lengthSqr() < 1.0E-4D)
                continue;
            direction = direction.normalize();
            double distance = MIN_DISTANCE + random.nextDouble() * (MAX_DISTANCE - MIN_DISTANCE);
            BlockHitResult hit = level.clip(new ClipContext(center, center.add(direction.scale(distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
            if (hit.getType() == HitResult.Type.BLOCK) {
                distance = hit.getLocation().distanceTo(center) - WALL_MARGIN;
            }
            Vec3 offset = direction.scale(Math.max(distance, 0.0D));
            if (distance >= MIN_DISTANCE)
                return offset;
            if (best == null || offset.lengthSqr() > best.lengthSqr()) {
                best = offset;
            }
        }
        return best == null ? new Vec3(0.0D, MIN_DISTANCE, 0.0D) : best;
    }

    private static void strike(Cast cast, LivingEntity target) {
        Player caster = cast.level.getPlayerByUUID(cast.casterId);
        applyingDamage = true;
        target.invulnerableTime = 0;
        target.hurt(ACDamageTypes.causeDesolateDaggerDamage(cast.level.registryAccess(), caster != null ? caster : target), DAMAGE + cast.storedDamage);
        applyingDamage = false;
        cast.level.playSound(null, target.getX(), target.getY(), target.getZ(), ACSoundRegistry.DESOLATE_DAGGER_HIT.get(), SoundSource.PLAYERS, 2.0F, 0.8F);
        if (target.isDeadOrDying()) {
            refund(cast, target);
        }
    }

    private static void refund(Cast cast, LivingEntity target) {
        Player caster = cast.level.getPlayerByUUID(cast.casterId);
        if (caster != null) {
            float refund = caster.hasEffect(ACEffectRegistry.DARKNESS_INCARNATE.get()) ? INCARNATE_REFUND : REFUND;
            cast.level.addFreshEntity(SiphonedSoulEntity.create(cast.level, center(target), caster, refund, target));
        }
    }

    private static void release(Entity entity) {
        ACExAttachments.set(entity, ACExAttachments.SECTUMSEMPRA_LOCK, false);
    }

    public static boolean isSectumsempraDagger(@Nullable Entity dagger) {
        return dagger != null && ACExData.has(dagger, ACExAttachments.SECTUMSEMPRA_OFFSET);
    }
}
