package org.crimsoncrips.alexscavesexemplified.server.goals;



import net.minecraftforge.event.ForgeEventFactory;
import net.minecraft.tags.DamageTypeTags;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class ACExSelfDestruct extends ACExFollowNearestGoal {



    public ACExSelfDestruct(NotorEntity mob) {
        super(mob, LivingEntity.class, 15, 1.3,living -> {
            return mob.getLastHurtByMob() == living;
        });
    }

    @Override
    public boolean canUse() {
        return super.canUse() && mob.getLastHurtByMob() instanceof LivingEntity;
    }

    @Override
    public void tick() {
        if (getTarget() != null) {
            mob.setLastHurtByMob(getTarget());
        }
        var target = getTarget();
        if (target != null && !mob.isLeashed()) {
            mob.getLookControl().setLookAt(target, 10.0F, (float) this.mob.getMaxHeadXRot());
            navigation.moveTo(target, speedModifier);
        } else {
            navigation.stop();
        }
    }

    private static final int SHARDS = 24;
    private static final int SCRAPS = 18;
    private static final int SPARKS = 12;
    private static final float DAMAGE_MULTIPLIER = 1.0F / 3.0F;
    private static final ExplosionDamageCalculator WEAKENED_BLAST = new ExplosionDamageCalculator();
    private static boolean weakening;

    public static float weakenedDamage(DamageSource source, float amount) {
        return weakening && source.is(DamageTypeTags.IS_EXPLOSION) ? amount * DAMAGE_MULTIPLIER : amount;
    }

    public static void shatter(ServerLevel level, NotorEntity notor) {
        Vec3 center = notor.position().add(0, notor.getBbHeight() * 0.5F, 0);
        RandomSource random = notor.getRandom();
        burst(level, new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(ACItemRegistry.NOTOR_COMPONENT.get())), center, random, SHARDS, 0.35D);
        burst(level, new BlockParticleOption(ParticleTypes.BLOCK, ACBlockRegistry.SCRAP_METAL.get().defaultBlockState()), center, random, SCRAPS, 0.3D);
        burst(level, ParticleTypes.ELECTRIC_SPARK, center, random, SPARKS, 0.5D);
        level.playSound(null, center.x, center.y, center.z, SoundEvents.GLASS_BREAK, SoundSource.HOSTILE, 1.0F, 0.8F + random.nextFloat() * 0.3F);
        level.playSound(null, center.x, center.y, center.z, ACSoundRegistry.NOTOR_DEATH.get(), SoundSource.HOSTILE, 1.0F, 1.2F);
        dropRemains(level, notor);
        notor.discard();
        weakening = true;
        try {
            level.explode(notor, null, WEAKENED_BLAST, center.x, center.y, center.z, 2, false, Level.ExplosionInteraction.NONE);
        } finally {
            weakening = false;
        }
    }

    private static void burst(ServerLevel level, ParticleOptions particle, Vec3 center, RandomSource random, int amount, double speed) {
        for (int i = 0; i < amount; i++) {
            Vec3 direction = new Vec3(random.nextDouble() - 0.5D, random.nextDouble() - 0.3D, random.nextDouble() - 0.5D).normalize();
            level.sendParticles(particle, center.x, center.y, center.z, 0, direction.x, direction.y, direction.z, speed * (0.6D + random.nextDouble() * 0.8D));
        }
    }

    private static void dropRemains(ServerLevel level, NotorEntity notor) {
        LivingEntity attacker = notor.getLastHurtByMob();
        DamageSource source = notor.damageSources().explosion(notor, attacker);
        LootParams.Builder params = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, notor)
                .withParameter(LootContextParams.ORIGIN, notor.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, source)
                .withOptionalParameter(LootContextParams.KILLER_ENTITY, attacker)
                .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, notor);
        if (attacker instanceof Player player) {
            params.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
            ExperienceOrb.award(level, notor.position(), ForgeEventFactory.getExperienceDrop(notor, player, notor.getExperienceReward()));
        }
        LootTable table = level.getServer().getLootData().getLootTable(notor.getLootTable());
        table.getRandomItems(params.create(LootContextParamSets.ENTITY), notor.getLootTableSeed(), notor::spawnAtLocation);
    }
}
