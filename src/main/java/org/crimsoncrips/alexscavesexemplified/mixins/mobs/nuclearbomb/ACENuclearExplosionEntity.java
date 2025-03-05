package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nuclearbomb;

import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GammaroachEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.alexmodguy.alexscaves.server.item.SackOfSatingItem.*;


@Mixin(NuclearExplosionEntity.class)
public abstract class ACENuclearExplosionEntity extends Entity implements Gammafied {


    public ACENuclearExplosionEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final EntityDataAccessor<Boolean> GAMMA = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.BOOLEAN);

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
        if(entity instanceof TremorzillaEntity tremorzilla && AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get()){
            ((Gammafied) tremorzilla).setGamma(isGamma());
            tremorzilla.getAttribute(Attributes.MAX_HEALTH).setBaseValue(750F);
            tremorzilla.getAttribute(Attributes.ARMOR).setBaseValue(18F);
            tremorzilla.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(50F);
            for (Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(100))) {
                ACEUtils.awardAdvancement(player,"gamma_tremorzilla","gamma");
            }
        }
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V"),index = 0)
    private ParticleOptions alexsCavesExemplified$tick(ParticleOptions pParticleData) {
        return isGamma() ? ACEParticleRegistry.GAMMA_MUSHROOM_CLOUD.get() : pParticleData;
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
