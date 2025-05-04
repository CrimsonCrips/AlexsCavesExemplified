package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
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
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

import static org.crimsoncrips.alexscavesexemplified.compat.AMCompat.amberReset;

@Mixin(LivingEntity.class)
public abstract class ACELivingEntity extends Entity {


    @Shadow public abstract boolean isDeadOrDying();

    @Shadow public abstract boolean isSensitiveToWater();

    @Shadow public abstract boolean hasEffect(MobEffect pEffect);

    @Shadow @Nullable public abstract MobEffectInstance getEffect(MobEffect pEffect);

    public ACELivingEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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
                frog.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 1,false,false));
            }
            if (livingEntity instanceof Tadpole tadpole && tadpole.isNoAi()) {
                tadpole.setNoAi(false);
                tadpole.setInvulnerable(false);
                tadpole.setSilent(false);
                tadpole.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 1,false,false));
            }
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void alexsCavesExemplified$hurt(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        if (pSource.getEntity() instanceof LivingEntity && livingEntity.hasEffect(ACEEffects.SERENED.get())){
            MobEffectInstance serene = livingEntity.getEffect(ACEEffects.SERENED.get());
            if (serene != null) {
                livingEntity.removeEffect(serene.getEffect());
                if (serene.getDuration() > 100) {
                    livingEntity.addEffect(new MobEffectInstance(serene.getEffect(), serene.getDuration() - 100, serene.getAmplifier()));
                }
            }
        }
    }

    //Props to Drullkus for assistance

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        Block block = livingEntity.level().getBlockState(livingEntity.blockPosition()).getBlock();
        if (block != ACBlockRegistry.AMBER.get()) {
            if (ModList.get().isLoaded("alexsmobs")) {
                amberReset(livingEntity);
            }
            if (livingEntity instanceof Frog frog && frog.isNoAi()) {
                frog.setNoAi(false);
                frog.setInvulnerable(false);
                frog.setSilent(false);
                frog.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 1,false,false));
            }
            if (livingEntity instanceof Tadpole tadpole && tadpole.isNoAi()) {
                tadpole.setNoAi(false);
                tadpole.setInvulnerable(false);
                tadpole.setSilent(false);
                tadpole.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 1,false,false));
            }
        }
    }

    @ModifyReturnValue(method = "isSensitiveToWater", at = @At("RETURN"))
    private boolean alexsCavesExemplified$isValidSacrifice(boolean original) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        return original || (!(livingEntity instanceof WaterAnimal) && livingEntity.hasEffect(ACEEffects.RABIAL.get()) && AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get());
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isDeadOrDying()Z"))
    private boolean alexsCavesExemplified$aiStep(boolean original) {
        return AlexsCavesExemplified.COMMON_CONFIG.CRYONIC_CAVITY_ENABLED.get();
    }

    @Inject(method = "aiStep", at = @At(value = "TAIL"))
    private void alexsCavesExemplified$aiStep2(CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.CRYONIC_CAVITY_ENABLED.get()){
            if (!this.level().isClientSide && !this.isDeadOrDying()) {
                int i = this.getTicksFrozen();
                if (this.isInPowderSnow && this.canFreeze()) {
                    this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), i + 1));
                } else if (!this.level().getBiome(this.blockPosition()).is(ACBiomeRegistry.CANDY_CAVITY)) {
                    this.setTicksFrozen(Math.max(0, i - 2));
                }
                if (this.level().getBiome(this.blockPosition()).is(ACBiomeRegistry.CANDY_CAVITY) && this.level().random.nextDouble() < 0.01 && !this.isOnFire() && !this.getType().is(ACTagRegistry.CANDY_MOBS)){
                    this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), getTicksFrozen() + 1));
                }
            }
        }
    }

}
