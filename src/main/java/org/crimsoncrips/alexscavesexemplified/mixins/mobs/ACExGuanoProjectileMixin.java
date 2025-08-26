package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.item.GuanoEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GuanoEntity.class)
public class ACExGuanoProjectileMixin {

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void getMaxLoadTime(EntityHitResult hitResult, CallbackInfo ci) {
        if (hitResult.getEntity() instanceof LivingEntity living && AlexsCavesExemplified.COMMON_CONFIG.GUASLOWPOKE_ENABLED.get()){
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
        }
    }


}
