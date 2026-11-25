package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.ExtinctionSpearEntity;
import org.crimsoncrips.alexscavesexemplified.misc.ACExPrimordialProwess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ExtinctionSpearEntity.class)
public abstract class ACExExtinctionSpearEntityMixin {

    @ModifyArg(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), index = 1)
    private float alexsCavesExemplified$onHitEntity(float damage) {
        return ACExPrimordialProwess.boostedDamage(((ExtinctionSpearEntity) (Object) this).getOwner(), damage);
    }
}
