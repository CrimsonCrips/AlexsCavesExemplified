package org.crimsoncrips.alexscavesexemplified.mixins.misc;

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

    @WrapWithCondition(method = "explode", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private boolean alexsCavesExemplified$explode1(Map<Object, Object> map, Object key, Object value) {
        return !ACExSugarStaffAbilities.SPEAR_HITTING.get();
    }
}
