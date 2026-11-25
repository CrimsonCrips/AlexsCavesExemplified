package org.crimsoncrips.alexscavesexemplified.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.TeslaBulbBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.ACExSoundRegistry;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import javax.annotation.Nullable;

public class ACExTeslaShock {

    public static final int SPARK_TICKS = ACExUtils.seconds(1.5F);
    public static final int POWERUP_TICKS = ACExUtils.seconds(1.75F);
    public static final int FIRE_TICKS = ACExUtils.seconds(3.1F);
    public static final int[] GLOW_TICKS = {ACExUtils.seconds(2.0F), ACExUtils.seconds(2.25F), ACExUtils.seconds(2.5F), ACExUtils.seconds(2.75F)};
    private static final double RANGE = 5.0D;
    private static final float DAMAGE = 7.0F;
    private static final int BURN_TICKS = ACExUtils.seconds(1.5F);

    public static boolean enabled() {
        return AlexsCavesExemplified.COMMON_CONFIG.SHOCKING_THERAPY_ENABLED.get();
    }

    public static long elapsed(TeslaBulbBlockEntity bulb, Level level) {
        long start = ACExData.get(bulb, ACExAttachments.TESLA_CHARGE_START);
        return start < 0 ? -1 : level.getGameTime() - start;
    }

    public static int glowStage(TeslaBulbBlockEntity bulb, Level level) {
        long elapsed = elapsed(bulb, level);
        int stage = 0;
        for (int threshold : GLOW_TICKS) {
            if (elapsed > threshold) {
                stage++;
            }
        }
        return stage;
    }

    public static void tick(Level level, BlockPos pos, TeslaBulbBlockEntity bulb) {
        if (level.isClientSide) {
            long elapsed = elapsed(bulb, level);
            if (elapsed >= SPARK_TICKS) {
                ParticleUtils.spawnParticlesAlongAxis(Direction.UP.getAxis(), level, pos, 0.125D, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
            }
            if (elapsed == POWERUP_TICKS) {
                level.playLocalSound(pos, ACExSoundRegistry.TESLA_POWERUP.get(), SoundSource.BLOCKS, 2.0F, 1.0F, false);
            }
            return;
        }
        Player target = findTarget(level, pos);
        if (target == null) {
            ACExAttachments.set(bulb, ACExAttachments.TESLA_CHARGE_START, -1L);
            return;
        }
        long start = ACExData.get(bulb, ACExAttachments.TESLA_CHARGE_START);
        if (start < 0) {
            ACExAttachments.set(bulb, ACExAttachments.TESLA_CHARGE_START, level.getGameTime());
            return;
        }
        if (level.getGameTime() - start >= FIRE_TICKS) {
            shock((ServerLevel) level, pos, target);
            ACExAttachments.set(bulb, ACExAttachments.TESLA_CHARGE_START, level.getGameTime());
        }
    }

    @Nullable
    private static Player findTarget(Level level, BlockPos pos) {
        Vec3 center = Vec3.atCenterOf(pos);
        Player closest = null;
        double closestDistance = RANGE * RANGE;
        for (Player player : level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(RANGE))) {
            double distance = player.distanceToSqr(center);
            if (distance <= closestDistance && player.isAlive() && !player.isCreative() && !player.isSpectator() && canSee(level, pos, center, player)) {
                closest = player;
                closestDistance = distance;
            }
        }
        return closest;
    }

    private static boolean canSee(Level level, BlockPos pos, Vec3 center, Player player) {
        Vec3 eyes = player.getEyePosition();
        Vec3 from = center.add(eyes.subtract(center).normalize().scale(0.6D));
        HitResult hit = level.clip(new ClipContext(from, eyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        return hit.getType() == HitResult.Type.MISS;
    }

    private static void shock(ServerLevel level, BlockPos pos, Player target) {
        Vec3 from = Vec3.atCenterOf(pos);
        Vec3 offset = from.subtract(target.position());
        level.sendParticles(ACParticleRegistry.TESLA_BULB_LIGHTNING.get(), from.x, from.y, from.z, 0, -offset.x, -offset.y + 1, -offset.z, 1.3D);
        LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
        lightningBolt.setDamage(DAMAGE);
        target.thunderHit(level, lightningBolt);
        target.setRemainingFireTicks(BURN_TICKS);
        level.playSound(null, target.getX(), target.getY(), target.getZ(), ACExSoundRegistry.TESLA_FIRE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        ACExUtils.awardAdvancement(target, "tesla_shock", "shock");
    }
}
