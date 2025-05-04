package org.crimsoncrips.alexscavesexemplified.server.effect;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.ACEDamageTypes;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;

public class ACESerened extends MobEffect {

    public ACESerened() {
        super(MobEffectCategory.NEUTRAL, 0X1cff59);
        this.addAttributeModifier(Attributes.MAX_HEALTH, "ba8a7227-7386-4b39-b5ce-11dcbff92d23", 1.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "92636ebe-9779-4dc7-b788-6d58a8301a2a", -0.020000000, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "52c22265-593c-4007-a838-91593d75c690", -1.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR, "7405588f-6b0b-43b9-8acf-c4f765ceccbd", 2, AttributeModifier.Operation.ADDITION);

    }

    public String getDescriptionId() {
        if (AlexsCavesExemplified.COMMON_CONFIG.SERENED_ENABLED.get()) {
            return "effect.alexscavesexemplified.serened.title";
        } else {
            return "alexscavesexemplified.feature_disabled";
        }
    }

    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel serverLevel){
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