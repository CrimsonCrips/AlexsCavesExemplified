package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import net.minecraft.world.entity.LivingEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(FrostmintExplosion.class)
public class ACExFrostmintExplosionMixin {

    @WrapWithCondition(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private boolean alexsCavesExemplified$explode(Entity entity, Vec3 motion) {
        return !ACExSugarStaffAbilities.SPEAR_HITTING.get();
    }

    @WrapWithCondition(method = "explode", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
    private boolean alexsCavesExemplified$explode1(Map<Object, Object> map, Object key, Object value) {
        return !ACExSugarStaffAbilities.SPEAR_HITTING.get();
    }

    @WrapOperation(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setTicksFrozen(I)V"))
    private void alexsCavesExemplified$explode2(LivingEntity living, int ticks, Operation<Void> original) {
        float freeze = ACExSugarStaffAbilities.SPEAR_FREEZE.get();
        int required = living.getTicksRequiredToFreeze();
        original.call(living, freeze == 1.0F ? ticks : required + Math.round((ticks - required) * freeze));
    }
}
