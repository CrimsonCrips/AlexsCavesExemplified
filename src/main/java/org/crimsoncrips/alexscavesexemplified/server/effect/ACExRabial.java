package org.crimsoncrips.alexscavesexemplified.server.effect;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.ACExDamageTypes;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;

public class ACExRabial extends MobEffect {

    public ACExRabial() {
        super(MobEffectCategory.HARMFUL, 0Xe6b0ac);
        
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "52c22265-593c-4007-a838-91593d75c690", 0.1000000596046448, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR, "7405588f-6b0b-43b9-8acf-c4f765ceccbd", -2, AttributeModifier.Operation.ADDITION);


    }

    public String getDescriptionId() {
        if (AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get()) {
            return "effect.alexscavesexemplified.rabial.title";
        } else {
            return "alexscavesexemplified.feature_disabled";
        }
    }

    private int lastDuration = -1;

    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (lastDuration <= 1) {
            int rabialLevel = amplifier + 1;
            entity.hurt(ACExDamageTypes.getDamageSource(entity.level(), ACExDamageTypes.RABIAL_END), rabialLevel * 10);
        }
        if (entity instanceof ServerPlayer serverPlayer){
            ACExUtils.awardAdvancement(serverPlayer,"rabial","has_rabies");
        }

    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        lastDuration = duration;
        return duration > 0;
    }


}