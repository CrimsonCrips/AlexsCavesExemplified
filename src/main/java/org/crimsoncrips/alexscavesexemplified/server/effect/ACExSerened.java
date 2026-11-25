package org.crimsoncrips.alexscavesexemplified.server.effect;


import org.crimsoncrips.alexscavesexemplified.backport.ACExCompat;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import javax.annotation.Nullable;

public class ACExSerened extends MobEffect {

    public ACExSerened() {
        super(MobEffectCategory.NEUTRAL, 0X1cff59);
        this.addAttributeModifier(Attributes.MAX_HEALTH, "ba8a7227-7386-4b39-b5ce-11dcbff92d23", 1.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "92636ebe-9779-4dc7-b788-6d58a8301a2a", -0.020000000, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ACExCompat.uuid(AlexsCavesExemplified.prefix("effect.serened.attack_damage")).toString(), -1.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR, ACExCompat.uuid(AlexsCavesExemplified.prefix("effect.serened.armor")).toString(), 2, AttributeModifier.Operation.ADDITION);

    }

    public String getDescriptionId() {
        if (AlexsCavesExemplified.COMMON_CONFIG.SERENED_ENABLED.get()) {
            return "effect.alexscavesexemplified.serened.title";
        } else {
            return "misc.alexscavesexemplified.feature_disabled";
        }
    }

    public static boolean calms(LivingEntity calmed, @Nullable LivingEntity target) {
        return target != null && AlexsCavesExemplified.COMMON_CONFIG.SERENED_ENABLED.get() && calmed.hasEffect(ACExEffects.SERENED.get()) && target != calmed.getLastHurtByMob();
    }

    private static void dropTargets(Mob mob) {
        if (calms(mob, mob.getTarget())) {
            mob.setTarget(null);
        }
        Brain<?> brain = mob.getBrain();
        if (brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && calms(mob, brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null))) {
            brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
        }
    }

    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel serverLevel){
            if (entity instanceof Mob mob) {
                dropTargets(mob);
            }
            RandomSource random = entity.getRandom();
            Vec3 particlePos = entity.getEyePosition().add((random.nextFloat() - 0.5F) * 2.0F * entity.getScale(), random.nextFloat() * 2.0F * entity.getScale(), (random.nextFloat() - 0.5F) * 2.0F * entity.getScale());
            serverLevel.sendParticles(ACParticleRegistry.HAPPINESS.get(), particlePos.x, particlePos.y - 1, particlePos.z, 1,((double) random.nextFloat() - 0.5D) * 0.1D, ((double) random.nextFloat() - 0.5D) * 0.1D, ((double) random.nextFloat() - 0.5D) * 0.1D,0.1);
        }
        entity.heal(0.2F);
    }


    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }

}