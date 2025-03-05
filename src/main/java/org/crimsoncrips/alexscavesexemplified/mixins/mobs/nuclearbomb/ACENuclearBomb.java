package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nuclearbomb;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearSirenBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.stream.Stream;


@Mixin(NuclearBombEntity.class)
public abstract class ACENuclearBomb extends Entity implements Gammafied, TraceableEntity {


    @Shadow protected abstract void explode();

    private static final EntityDataAccessor<Boolean> GAMMA = SynchedEntityData.defineId(NuclearBombEntity.class, EntityDataSerializers.BOOLEAN);

    public ACENuclearBomb(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean isGamma() {
        return this.entityData.get(GAMMA);
    }

    @Override
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

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void read(CompoundTag compound, CallbackInfo ci) {
        this.setGamma(compound.getBoolean("Gamma"));
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),index = 0)
    private ParticleOptions alexsCavesExemplified$tick(ParticleOptions pParticleData) {
        return isGamma() ? ACEParticleRegistry.GAMMA_PROTON.get() : pParticleData;
    }


    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/NuclearBombEntity;setTime(I)V"))
    private boolean alexsCavesExemplified$tick1(NuclearBombEntity instance, int time) {
        if (AlexsCavesExemplified.COMMON_CONFIG.GROUNDED_NUKE_ENABLED.get()){
            return this.onGround();
        }
        return true;
    }



    @Inject(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private void alexsCavesExemplified$explode(CallbackInfo ci, @Local NuclearExplosionEntity explosion) {
        if (isGamma()){
            ((Gammafied)explosion).setGamma(true);
            explosion.setSize((AlexsCaves.COMMON_CONFIG.nukeExplosionSizeModifier.get()).floatValue() * 1.5F);
        }
    }


}
