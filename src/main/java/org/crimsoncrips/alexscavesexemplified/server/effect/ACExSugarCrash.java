package org.crimsoncrips.alexscavesexemplified.server.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExSugarCrash extends MobEffect {

    public ACExSugarCrash() {
        super(MobEffectCategory.HARMFUL, 0Xfc3df9);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "973637ce-ed32-404b-b2f3-e4b6264a181a", -0.020000000, AttributeModifier.Operation.MULTIPLY_TOTAL);

    }

    public String getDescriptionId() {
        if (AlexsCavesExemplified.COMMON_CONFIG.SUGAR_CRASH_ENABLED.get()) {
            return "alexscavesexemplified.potion.sugar_crash";
        } else {
            return "alexscavesexemplified.feature_disabled";
        }
    }


}