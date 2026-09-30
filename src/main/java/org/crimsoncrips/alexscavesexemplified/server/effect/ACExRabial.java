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
        
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, AlexsCavesExemplified.prefix("effect.rabial.attack_damage"), 0.1000000596046448, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR, AlexsCavesExemplified.prefix("effect.rabial.armor"), -2, AttributeModifier.Operation.ADD_VALUE);


    }

    public String getDescriptionId() {
        if (AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get()) {
            return "effect.alexscavesexemplified.rabial.title";
        } else {
            return "alexscavesexemplified.feature_disabled";
        }
    }

    private int lastDuration = -1;

    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (lastDuration <= 1) {
            int rabialLevel = amplifier + 1;
            entity.hurt(ACExDamageTypes.getDamageSource(entity.level(), ACExDamageTypes.RABIAL_END), rabialLevel * 10);
        }
        if (entity instanceof ServerPlayer serverPlayer){
            ACExUtils.awardAdvancement(serverPlayer,"rabial","has_rabies");
        }
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        lastDuration = duration;
        return duration > 0;
    }


}