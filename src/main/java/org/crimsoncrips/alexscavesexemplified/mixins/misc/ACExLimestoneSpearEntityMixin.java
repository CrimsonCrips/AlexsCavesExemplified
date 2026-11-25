package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.LimestoneSpearEntity;
import org.crimsoncrips.alexscavesexemplified.misc.ACExPrimordialProwess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LimestoneSpearEntity.class)
public abstract class ACExLimestoneSpearEntityMixin {

    @ModifyArg(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), index = 1)
    private float alexsCavesExemplified$onHitEntity(float damage) {
        return ACExPrimordialProwess.boostedDamage(((LimestoneSpearEntity) (Object) this).getOwner(), damage);
    }
}
