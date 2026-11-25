package org.crimsoncrips.alexscavesexemplified.client.monolith;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.misc.ACExAmberMonolith;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.mixins.misc.render.ACExVoidBeingCloudParticleAccessor;
import org.crimsoncrips.alexscavesexemplified.server.entity.DarkRitualEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class ACExDarkRitualClient {

    private static final double CLOUD_HEIGHT = 4.5D;
    private static final double CLOUD_CLEARANCE = 2.0D;
    private static final double MIN_CLOUD_HEIGHT = 2.0D;
    private static final int CLOUD_SIZE = 1;
    private static final float EYE_SPREAD = 1.1F;
    private static final int MIN_EYES = 3;
    private static final int MAX_EYES = 4;
    private static final int MIN_TENDRILS = 5;
    private static final int MAX_TENDRILS = 8;
    private static final int TENDRIL_SEEK_TICKS = 200;
    private static final int GATHER_PRAYER_INTERVAL = 2;
    private static final int PRAYER_INTERVAL = 10;
    private static final int PRAYER_STREAMS = 4;
    private static final double PRAYER_RADIUS = 0.5D;
    private static final double GATHER_RADIUS = 1.5D;
    private static final double GATHER_PULL = 0.08D;
    private static final int PLUNGE_TICKS = ACExUtils.seconds(1.0F);
    private static final double PLUNGE_ACCELERATION = 0.04D;
    private static final int BURST_TICKS = 10;
    private static final int PLUNGE_START = ACExAmberMonolith.RITUAL_TICKS - PLUNGE_TICKS;

    private static final Map<DarkRitualEntity, Ritual> RITUALS = new WeakHashMap<>();

    public static void tick(DarkRitualEntity entity) {
        RITUALS.computeIfAbsent(entity, Ritual::new).tick(entity);
    }

    private static class Ritual {
        private final Vec3 cloud;
        private final List<Particle> cloudParts = new ArrayList<>();
        private boolean summonedCloud;

        private Ritual(DarkRitualEntity entity) {
            this.cloud = new Vec3(entity.getX(), monolithTop(entity) + cloudHeight(entity), entity.getZ());
        }

        private void tick(DarkRitualEntity entity) {
            int age = entity.getRitualAge();
            if (age < PLUNGE_START && age % (age < ACExAmberMonolith.GATHER_TICKS ? GATHER_PRAYER_INTERVAL : PRAYER_INTERVAL) == 0) {
                this.pray(entity, age);
            }
            if (age < ACExAmberMonolith.GATHER_TICKS) {
                this.gather(entity, age / (float) ACExAmberMonolith.GATHER_TICKS);
            } else if (!this.summonedCloud && age < PLUNGE_START) {
                this.summonCloud(entity);
            }
            if (age >= PLUNGE_START) {
                this.plunge(age - PLUNGE_START + 1);
            }
            if (age > ACExAmberMonolith.RITUAL_TICKS - BURST_TICKS && age <= ACExAmberMonolith.RITUAL_TICKS) {
                burst(entity);
            }
        }

        private void pray(DarkRitualEntity entity, int age) {
            for (int i = 0; i < PRAYER_STREAMS; i++) {
                Vec3 from = entity.position().add(new Vec3(PRAYER_RADIUS, 0.0D, 0.0D).yRot((float) i / PRAYER_STREAMS * Mth.TWO_PI + age * 0.05F));
                entity.level().addParticle(ACParticleRegistry.UNDERZEALOT_MAGIC.get(), from.x, from.y, from.z, this.cloud.x, this.cloud.y, this.cloud.z);
            }
        }

        private void gather(DarkRitualEntity entity, float progress) {
            RandomSource random = entity.level().getRandom();
            int count = 1 + Mth.floor(progress * 3.0F);
            for (int i = 0; i < count; i++) {
                Vec3 direction = new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize();
                Vec3 from = this.cloud.add(direction.scale(GATHER_RADIUS * (1.0F - progress * 0.5F)));
                Vec3 pull = this.cloud.subtract(from).scale(GATHER_PULL);
                entity.level().addParticle(ParticleTypes.SQUID_INK, from.x, from.y, from.z, pull.x, pull.y, pull.z);
            }
        }

        private void summonCloud(DarkRitualEntity entity) {
            this.summonedCloud = true;
            RandomSource random = entity.level().getRandom();
            Particle cloudParticle = this.add(ACParticleRegistry.VOID_BEING_CLOUD.get(), CLOUD_SIZE, entity.getId(), 0);
            if (cloudParticle instanceof ACExVoidBeingCloudParticleAccessor accessor) {
                accessor.setSpawnedExtras(true);
            }
            float circleOffset = random.nextInt(360);
            float size = CLOUD_SIZE + 1;
            int eyes = Mth.nextInt(random, MIN_EYES, MAX_EYES);
            for (int i = 0; i < eyes; i++) {
                Vec3 offset = new Vec3((0.5F + random.nextFloat() * 0.7F) * size * EYE_SPREAD, 0.0D, 0.0D).yRot(circleOffset + (float) i / eyes * Mth.PI);
                this.add(ACParticleRegistry.VOID_BEING_EYE.get(), offset.x, offset.z, 0.0D);
            }
            int tendrils = Mth.nextInt(random, MIN_TENDRILS, MAX_TENDRILS);
            for (int i = 0; i < tendrils; i++) {
                this.add(ACParticleRegistry.VOID_BEING_TENDRIL.get(), entity.getId(), TENDRIL_SEEK_TICKS / tendrils * (i + 1), 0.0D);
            }
            entity.level().playLocalSound(this.cloud.x, this.cloud.y, this.cloud.z, ACSoundRegistry.DARK_CLOUD_APPEAR.get(), SoundSource.BLOCKS, 2.0F, 1.0F, false);
        }

        private Particle add(ParticleOptions options, double xData, double yData, double zData) {
            ParticleEngine engine = Minecraft.getInstance().particleEngine;
            Particle particle = engine.createParticle(options, this.cloud.x, this.cloud.y, this.cloud.z, xData, yData, zData);
            if (particle != null) {
                this.cloudParts.add(particle);
            }
            return particle;
        }

        private void plunge(int plungeTicks) {
            for (Particle part : this.cloudParts) {
                if (part.isAlive()) {
                    part.setParticleSpeed(0.0D, -PLUNGE_ACCELERATION * plungeTicks, 0.0D);
                }
            }
        }
    }

    private static void burst(DarkRitualEntity entity) {
        RandomSource random = entity.level().getRandom();
        double bottom = entity.getY() - DarkRitualEntity.RITUAL_HEIGHT;
        for (int i = 0; i < 1 + random.nextInt(4); i++) {
            entity.level().addParticle(ACParticleRegistry.UNDERZEALOT_EXPLOSION.get(), entity.getX() + random.nextFloat() - 0.5F, bottom + random.nextFloat(), entity.getZ() + random.nextFloat() - 0.5F, 0.0D, 0.0D, 0.0D);
        }
    }

    private static double monolithTop(DarkRitualEntity entity) {
        return entity.getY() - DarkRitualEntity.RITUAL_HEIGHT + 1.0D;
    }

    private static double cloudHeight(DarkRitualEntity entity) {
        Level level = entity.level();
        BlockPos.MutableBlockPos pos = entity.blockPosition().mutable();
        int limit = Mth.ceil(CLOUD_HEIGHT + CLOUD_CLEARANCE);
        for (int i = 1; i <= limit; i++) {
            pos.move(Direction.UP);
            if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty())
                return Mth.clamp(i - 1 - CLOUD_CLEARANCE, MIN_CLOUD_HEIGHT, CLOUD_HEIGHT);
        }
        return CLOUD_HEIGHT;
    }
}
