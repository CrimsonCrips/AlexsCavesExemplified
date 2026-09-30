package org.crimsoncrips.alexscavesexemplified.server.effect;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExSerened extends MobEffect {

    public ACExSerened() {
        super(MobEffectCategory.NEUTRAL, 0X1cff59);
        this.addAttributeModifier(Attributes.MAX_HEALTH, AlexsCavesExemplified.prefix("effect.serened.max_health"), 1.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, AlexsCavesExemplified.prefix("effect.serened.movement_speed"), -0.020000000, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, AlexsCavesExemplified.prefix("effect.serened.attack_damage"), -1.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR, AlexsCavesExemplified.prefix("effect.serened.armor"), 2, AttributeModifier.Operation.ADD_VALUE);

    }

    public String getDescriptionId() {
        if (AlexsCavesExemplified.COMMON_CONFIG.SERENED_ENABLED.get()) {
            return "effect.alexscavesexemplified.serened.title";
        } else {
            return "alexscavesexemplified.feature_disabled";
        }
    }

    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel serverLevel){
            RandomSource random = entity.getRandom();
            Vec3 particlePos = entity.getEyePosition().add((random.nextFloat() - 0.5F) * 2.0F * entity.getScale(), random.nextFloat() * 2.0F * entity.getScale(), (random.nextFloat() - 0.5F) * 2.0F * entity.getScale());
            serverLevel.sendParticles(ACParticleRegistry.HAPPINESS.get(), particlePos.x, particlePos.y - 1, particlePos.z, 1,((double) random.nextFloat() - 0.5D) * 0.1D, ((double) random.nextFloat() - 0.5D) * 0.1D, ((double) random.nextFloat() - 0.5D) * 0.1D,0.1);
        }
        entity.heal(0.2F);
        return true;
    }


    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration > 0;
    }

}