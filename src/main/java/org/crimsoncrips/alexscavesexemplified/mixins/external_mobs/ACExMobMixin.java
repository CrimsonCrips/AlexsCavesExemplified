package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(Mob.class)
public abstract class ACExMobMixin extends LivingEntity {


    protected ACExMobMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @WrapOperation(method = "setTarget", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/event/entity/living/LivingChangeTargetEvent;getNewAboutToBeSetTarget()Lnet/minecraft/world/entity/LivingEntity;"))
    private LivingEntity alexsCavesExemplified$setTarget(LivingChangeTargetEvent instance, Operation<LivingEntity> original) {
        if (this.hasEffect(ACExEffects.SERENED) && this.getLastHurtByMob() != instance.getNewAboutToBeSetTarget() && AlexsCavesExemplified.COMMON_CONFIG.SERENED_ENABLED.get()) {
            return null;
        } else {
            return original.call(instance);
        }
    }
}
