package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.ACEDamageTypes;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ThrownPotion.class)
public abstract class ACEThrownPotion extends ThrowableItemProjectile {


    public ACEThrownPotion(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "applyWater", at = @At("TAIL"))
    private void applyWater(CallbackInfo ci) {

        for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D))) {
            double d0 = this.distanceToSqr(livingentity);
            if (d0 < 16.0D) {

                if (AlexsCavesExemplified.COMMON_CONFIG.IRRADIATION_WASHOFF_ENABLED.get()){
                    MobEffectInstance irradiated = livingentity.getEffect(ACEffectRegistry.IRRADIATED.get());
                    if (irradiated != null) {
                        livingentity.removeEffect(irradiated.getEffect());
                        livingentity.addEffect(new MobEffectInstance(irradiated.getEffect(), irradiated.getDuration() - 100, irradiated.getAmplifier()));
                    }
                }
            }
        }

    }


}
