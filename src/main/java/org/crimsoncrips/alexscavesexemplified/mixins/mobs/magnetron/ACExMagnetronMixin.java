package org.crimsoncrips.alexscavesexemplified.mixins.mobs.magnetron;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.MagnetronEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MagnetronPartEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MagnetronMagneticism;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MagnetronEntity.class)
public abstract class ACExMagnetronMixin extends Monster implements MagnetronMagneticism {

    @Shadow public abstract boolean isFormed();

    @Shadow @Final public MagnetronPartEntity[] allParts;
    int delay = 0;

    private static final EntityDataAccessor<Integer> RIPPED_HEART = SynchedEntityData.defineId(MagnetronEntity.class, EntityDataSerializers.INT);

    protected ACExMagnetronMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public int getRippedHeart() {
        return this.entityData.get(RIPPED_HEART);
    }

    public void setRippedHeart(int value) {
        delay = 15;
        this.entityData.set(RIPPED_HEART, Integer.valueOf(value));
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void alexsCavesExemplified$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(RIPPED_HEART, 0);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("RippedHeart", this.getRippedHeart());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        this.setRippedHeart(compound.getInt("RippedHeart"));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        delay--;
        if (delay <= 0 && getRippedHeart() > 0) {
            setRippedHeart(0);
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        if (getRippedHeart() >= 100 && AlexsCavesExemplified.COMMON_CONFIG.MAGNETICISM_ENABLED.get() ){
            float range = 5F;
            Level level = this.level();

            Vec3 particlesFrom = this.position().add(0, 0.2, 0);
            float particleMax = 10 + this.getRandom().nextInt(5);

            for (int particles = 0; particles < particleMax; particles++) {
                Vec3 vec3 = new Vec3((this.getRandom().nextFloat() - 0.5) * 0.3F, (this.getRandom().nextFloat() - 0.5) * 0.3F, range * 0.5F + range * 0.5F * this.getRandom().nextFloat()).yRot((float) ((particles / particleMax) * Math.PI * 2)).add(particlesFrom);
                if (this.getRandom().nextBoolean()) {
                    level.addParticle(ACParticleRegistry.SCARLET_SHIELD_LIGHTNING.get(), vec3.x, vec3.y, vec3.z, particlesFrom.x, particlesFrom.y, particlesFrom.z);
                } else {
                    level.addParticle(ACParticleRegistry.AZURE_SHIELD_LIGHTNING.get(), particlesFrom.x, particlesFrom.y, particlesFrom.z, vec3.x, vec3.y, vec3.z);
                }
            }
            level.explode(null, this.blockPosition().getX() + 0.5D, this.blockPosition().getY() + 0.5D, this.blockPosition().getZ() + 0.5D, 1, false, Level.ExplosionInteraction.NONE);


            if (this.isFormed()) {
                for (MagnetronPartEntity part : allParts) {
                    if (part.getBlockState() != null && level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        BlockPos placeAt = part.blockPosition();
                        while (!level().getBlockState(placeAt).isAir() && placeAt.getY() < level().getMaxBuildHeight()) {
                            placeAt = placeAt.above();
                        }
                        FallingBlockEntity.fall(level(), placeAt, part.getBlockState());
                        part.setBlockState(null);
                    }
                }
            }
        }
        super.die(pDamageSource);
    }
}
