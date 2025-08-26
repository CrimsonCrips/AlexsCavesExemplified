package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ThrownPotion.class)
public abstract class ACExThrownPotionMixin extends ThrowableItemProjectile {


    public ACExThrownPotionMixin(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "applyWater", at = @At("TAIL"))
    private void applyWater(CallbackInfo ci) {

        for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D))) {
            double d0 = this.distanceToSqr(livingentity);
            if (d0 < 16.0D) {

                ACExUtils.irradiationWash(livingentity,80);
            }
        }

    }


}
