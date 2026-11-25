package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.DinosaurSpiritEntity;
import org.crimsoncrips.alexscavesexemplified.misc.ACExPrimordialProwess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DinosaurSpiritEntity.class)
public abstract class ACExDinosaurSpiritEntityMixin {

    @ModifyArg(method = "tickTremorsaurus", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), index = 1)
    private float alexsCavesExemplified$tickTremorsaurus(float damage) {
        return ACExPrimordialProwess.boostedDamage(((DinosaurSpiritEntity) (Object) this).getUsingPlayer(), damage);
    }
}
