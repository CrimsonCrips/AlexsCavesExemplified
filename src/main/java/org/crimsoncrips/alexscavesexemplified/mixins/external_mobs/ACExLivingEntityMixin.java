package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import net.minecraft.world.entity.Mob;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.world.effect.MobEffectInstance;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;
import org.spongepowered.asm.mixin.Unique;
import org.crimsoncrips.alexscavesexemplified.server.entity.GumwirmEntity;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(LivingEntity.class)
public abstract class ACExLivingEntityMixin extends Entity {


    public ACExLivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Inject(method = "onEffectAdded", at = @At("HEAD"))
    private void alexsCavesExemplified$onEffectAdded(MobEffectInstance effect, Entity source, CallbackInfo ci, @Share("healthRatio") LocalFloatRef healthRatio) {
        healthRatio.set(this.healthRatio());
    }

    @Inject(method = "onEffectAdded", at = @At("TAIL"))
    private void alexsCavesExemplified$onEffectAdded1(MobEffectInstance effect, Entity source, CallbackInfo ci, @Share("healthRatio") LocalFloatRef healthRatio) {
        this.keepHealthRatio(effect, healthRatio.get());
    }

    @Inject(method = "onEffectRemoved", at = @At("HEAD"))
    private void alexsCavesExemplified$onEffectRemoved(MobEffectInstance effect, CallbackInfo ci, @Share("healthRatio") LocalFloatRef healthRatio) {
        healthRatio.set(this.healthRatio());
    }

    @Inject(method = "onEffectRemoved", at = @At("TAIL"))
    private void alexsCavesExemplified$onEffectRemoved1(MobEffectInstance effect, CallbackInfo ci, @Share("healthRatio") LocalFloatRef healthRatio) {
        this.keepHealthRatio(effect, healthRatio.get());
    }

    @Unique
    private float healthRatio() {
        LivingEntity living = (LivingEntity) (Object) this;
        return living.getHealth() / living.getMaxHealth();
    }

    @Unique
    private void keepHealthRatio(MobEffectInstance effect, float ratio) {
        LivingEntity living = (LivingEntity) (Object) this;
        if (!living.level().isClientSide && ratio > 0.0F && effect.getEffect() == ACExEffects.SERENED.get()) {
            living.setHealth(ratio * living.getMaxHealth());
        }
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;aiStep()V"))
    private boolean alexsCavesExemplified$tick(LivingEntity instance) {
        return instance.level().isClientSide || !ACExData.has(instance, ACExAttachments.SWEET_RELEASE);
    }

    @WrapWithCondition(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"))
    private boolean alexsCavesExemplified$hurtKnockback(LivingEntity instance, double strength, double x, double z, @Local(argsOnly = true) DamageSource source) {
        return !(source.getDirectEntity() instanceof GumwirmEntity) && !(source.getEntity() instanceof GumwirmEntity);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void alexsCavesExemplified$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (ACExSugarStaffAbilities.interceptSweetReleaseHurt((LivingEntity) (Object) this, source, amount)) {
            cir.setReturnValue(false);
        }
    }
}
