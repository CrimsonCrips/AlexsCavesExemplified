package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GammaroachEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.particles.ParticleOptions;
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
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(NuclearExplosionEntity.class)
public abstract class ACExNuclearExplosionEntityMixin extends Entity {


    public ACExNuclearExplosionEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private void alexsCavesExemplified$tick(CallbackInfo ci,@Local LivingEntity entity) {
        if(entity instanceof TremorzillaEntity tremorzilla && AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && !tremorzilla.getData(ACExAttachments.GAMMA) && this.getData(ACExAttachments.GAMMA)){
            ACExUtils.setGamma(tremorzilla, this.getData(ACExAttachments.GAMMA));
            for (Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(100))) {
                ACExUtils.awardAdvancement(player,"gamma_tremorzilla","gamma");
            }
        }
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;<init>(Lnet/minecraft/core/Holder;IIZZZ)V"),index = 2)
    private int alexsCavesExemplified$tick1(int amplifier) {
        return this.getData(ACExAttachments.GAMMA) ? amplifier * 2 : amplifier;
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V"),index = 0)
    private ParticleOptions alexsCavesExemplified$tick(ParticleOptions pParticleData) {
        return this.getData(ACExAttachments.GAMMA) ? ACExParticleRegistry.GAMMA_MUSHROOM_CLOUD.get() : pParticleData;
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

}
