package org.crimsoncrips.alexscavesexemplified.mixins.backport;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.crimsoncrips.alexscavesexemplified.backport.EntityTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerLevel.class)
public abstract class ACExServerLevelTickMixin {

    @WrapOperation(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void alexsCavesExemplified$tickNonPassenger(Entity entity, Operation<Void> original) {
        EntityTickEvent.tick(entity, () -> original.call(entity));
    }

    @WrapOperation(method = "tickPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;rideTick()V"))
    private void alexsCavesExemplified$tickPassenger(Entity entity, Operation<Void> original) {
        EntityTickEvent.tick(entity, () -> original.call(entity));
    }
}
