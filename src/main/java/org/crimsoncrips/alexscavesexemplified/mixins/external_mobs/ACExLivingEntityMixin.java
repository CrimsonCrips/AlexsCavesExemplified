package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import com.llamalad7.mixinextras.sugar.Local;
import org.crimsoncrips.alexscavesexemplified.server.entity.GumwirmEntity;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import static org.crimsoncrips.alexscavesexemplified.compat.AMCompat.amberReset;

@Mixin(LivingEntity.class)
public abstract class ACExLivingEntityMixin extends Entity {


    public ACExLivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;aiStep()V"))
    private boolean alexsCavesExemplified$tick(LivingEntity instance) {
        return instance.level().isClientSide || !instance.hasData(ACExAttachments.SWEET_RELEASE);
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

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAirSupply()I"))
    private void alexsCavesExemplified$baseTick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        Block block = livingEntity.level().getBlockState(livingEntity.blockPosition()).getBlock();
        if (AlexsCavesExemplified.COMMON_CONFIG.PRESERVED_AMBER_ENABLED.get() && block != ACBlockRegistry.AMBER.get()) {
            if (ModList.get().isLoaded("alexsmobs")) {
                amberReset(livingEntity);
            }
            if (livingEntity instanceof Frog frog && frog.isNoAi()) {
                frog.setNoAi(false);
                frog.setInvulnerable(false);
                frog.setSilent(false);
                frog.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ACExUtils.seconds(300.0F), 1,false,false));
            }
            if (livingEntity instanceof Tadpole tadpole && tadpole.isNoAi()) {
                tadpole.setNoAi(false);
                tadpole.setInvulnerable(false);
                tadpole.setSilent(false);
                tadpole.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ACExUtils.seconds(300.0F), 1,false,false));
            }
        }
    }

    @ModifyReturnValue(method = "isSensitiveToWater", at = @At("RETURN"))
    private boolean alexsCavesExemplified$isValidSacrifice(boolean original) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        return original || (!(livingEntity instanceof WaterAnimal) && livingEntity.hasEffect(ACExEffects.RABIAL) && AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get());
    }


}
