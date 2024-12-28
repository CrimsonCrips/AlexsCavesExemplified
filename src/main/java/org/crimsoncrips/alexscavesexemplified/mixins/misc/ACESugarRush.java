package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.potion.SugarRushEffect;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;
import org.crimsoncrips.alexscavesexemplified.misc.ACEDamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SugarRushEffect.class)
public class ACESugarRush extends MobEffect {

    @Unique
    private int lastDuration = -1;


    public ACESugarRush(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Inject(method = "applyEffectTick", at = @At("HEAD"))
    private void getMaxLoadTime(LivingEntity entity, int amplifier, CallbackInfo ci) {
        if (lastDuration <= 1 && ACExemplifiedConfig.SUGAR_CRASH_ENABLED) {
            int sugarcrashLevel = amplifier + 1;
            entity.addEffect(new MobEffectInstance(ACEEffects.SUGAR_CRASH.get(), 400, amplifier));
            entity.hurt(ACEDamageTypes.causeSugarCrash(entity.level().registryAccess()), sugarcrashLevel * 2);
            entity.playSound(SoundEvents.GENERIC_EXPLODE, 5F, 1F);

            if (sugarcrashLevel > 3){
                entity.level().explode(entity,entity.getX(),entity.getY(),entity.getZ(),2, Level.ExplosionInteraction.MOB);
            }
        }
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        lastDuration = duration;
        return duration > 0;
    }


}
