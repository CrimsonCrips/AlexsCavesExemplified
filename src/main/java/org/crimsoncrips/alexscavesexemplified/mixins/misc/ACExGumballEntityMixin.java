package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.GumballEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.crimsoncrips.alexscavesexemplified.misc.ACExBuckshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GumballEntity.class)
public abstract class ACExGumballEntityMixin {

    @WrapOperation(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean alexsCavesExemplified$onHitEntity(Entity target, DamageSource source, float damage, Operation<Boolean> original) {
        GumballEntity gumball = (GumballEntity) (Object) this;
        if (!ACExBuckshot.isPellet(gumball))
            return original.call(target, source, damage);
        target.invulnerableTime = 0;
        return original.call(target, source, ACExBuckshot.falloffDamage(gumball, target));
    }
}
