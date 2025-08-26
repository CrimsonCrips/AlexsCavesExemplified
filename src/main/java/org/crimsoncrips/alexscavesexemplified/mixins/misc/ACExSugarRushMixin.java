package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.potion.SugarRushEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.ACExDamageTypes;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SugarRushEffect.class)
public class ACExSugarRushMixin extends MobEffect {

    @Unique
    private int lastDuration = -1;


    public ACExSugarRushMixin(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Inject(method = "applyEffectTick", at = @At("HEAD"))
    private void alexsCavesExemplified$applyEffectTick(LivingEntity entity, int amplifier, CallbackInfo ci) {
        if (lastDuration <= 2 && AlexsCavesExemplified.COMMON_CONFIG.SUGAR_CRASH_ENABLED.get() && (entity.getRandom().nextDouble() < 0.3 || amplifier >= 2)) {
            int sugarcrashLevel = amplifier + 1;
            entity.addEffect(new MobEffectInstance(ACExEffects.SUGAR_CRASH.get(), 400, amplifier));
            entity.hurt(ACExDamageTypes.getDamageSource(entity.level(), ACExDamageTypes.SUGAR_CRASH), sugarcrashLevel * 2);

            if (sugarcrashLevel > 3){
                entity.level().explode(entity,entity.getX(),entity.getY(),entity.getZ(),2, Level.ExplosionInteraction.MOB);
            }
            ACExUtils.awardAdvancement(entity,"sugar_crashed","crashed");
        }
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        lastDuration = duration;
        return duration > 0;
    }


}
