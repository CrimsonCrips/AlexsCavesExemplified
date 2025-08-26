package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.GammaroachEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.hellomouse.alexscavesenriched.entity.NuclearExplosion2Entity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACExParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(NuclearExplosion2Entity.class)
public abstract class ACExNuclearExplosion2EntityMixin extends Entity implements Gammafied {


    public ACExNuclearExplosion2EntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final EntityDataAccessor<Boolean> GAMMA = SynchedEntityData.defineId(NuclearExplosion2Entity.class, EntityDataSerializers.BOOLEAN);

    public boolean isGamma() {
        return this.entityData.get(GAMMA);
    }

    public void setGamma(boolean val) {
        this.entityData.set(GAMMA, Boolean.valueOf(val));
    }


    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void define(CallbackInfo ci) {
        this.entityData.define(GAMMA, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void add(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Gamma", this.isGamma());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private void alexsCavesExemplified$tick(CallbackInfo ci,@Local LivingEntity entity) {
        if(entity instanceof TremorzillaEntity tremorzilla && AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && !((Gammafied) tremorzilla).isGamma() && isGamma()){
            ((Gammafied) tremorzilla).setGamma(isGamma());
            for (Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(100))) {
                ACEUtils.awardAdvancement(player,"gamma_tremorzilla","gamma");
            }
        }
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;<init>(Lnet/minecraft/world/effect/MobEffect;IIZZZ)V"),index = 2)
    private int alexsCavesExemplified$tick1(int amplifier) {
        return isGamma() ? amplifier * 2 : amplifier;
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V",ordinal = 0),index = 0)
    private ParticleOptions alexsCavesExemplified$tick(ParticleOptions pParticleData) {
        return isGamma() ? ACExParticleRegistry.GAMMA_MUSHROOM_CLOUD.get() : pParticleData;
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean alexsCavesExemplified$tick(LivingEntity instance, DamageSource entity, float ev) {
        if (AlexsCavesExemplified.COMMON_CONFIG.TOUGH_ROACHES_ENABLED.get()){
            return !(instance instanceof GammaroachEntity) && !AMCompat.cockroach(instance);
        }
        return true;
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private boolean alexsCavesExemplified$tick2(LivingEntity instance, Vec3 vec3) {
        if (AlexsCavesExemplified.COMMON_CONFIG.TOUGH_ROACHES_ENABLED.get()){
            return !(instance instanceof GammaroachEntity) && !AMCompat.cockroach(instance);
        }
        return true;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void read(CompoundTag compound, CallbackInfo ci) {
        this.setGamma(compound.getBoolean("Gamma"));
    }
}
