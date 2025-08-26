package org.crimsoncrips.alexscavesexemplified.mixins.mobs.boundroid;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidWinchEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACExBaseInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoundroidEntity.class)
public abstract class ACExBoundroidMixin extends Monster implements ACExBaseInterface {

    @Shadow public int stopSlammingFor;

    private static final EntityDataAccessor<Boolean> MAGNETIZING = SynchedEntityData.defineId(BoundroidEntity.class, EntityDataSerializers.BOOLEAN);

    protected ACExBoundroidMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    int activateDelay;

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        BoundroidEntity boundroidEntity = (BoundroidEntity) (Object) this;
        if (isMagnetizing() && boundroidEntity.getRandom().nextDouble() < 0.01){
            for (int i = 0;i < 10;i++){
                Vec3 vec3 = new Vec3((boundroidEntity.getRandom().nextFloat() - 0.5) * 0.3F, (boundroidEntity.getRandom().nextFloat() - 0.5) * 0.3F + boundroidEntity.getRandom().nextInt(-2,3), 2 * 0.5F + 2 * 0.5F * boundroidEntity.getRandom().nextFloat()).yRot((float) ((i / 10F) * Math.PI * 2)).add(boundroidEntity.position());
                boundroidEntity.level().addParticle(ACParticleRegistry.SCARLET_SHIELD_LIGHTNING.get(), vec3.x, vec3.y, vec3.z, boundroidEntity.getX(), boundroidEntity.getY(), boundroidEntity.getZ());
            }
        }


        if (AlexsCavesExemplified.COMMON_CONFIG.BOUNDED_MAGNETISM_ENABLED.get()) {

            LivingEntity target = boundroidEntity.getTarget();
            BoundroidWinchEntity boundroidWinch = (BoundroidWinchEntity) boundroidEntity.getWinch();
            if (isMagnetizing() && target != null && MagnetUtil.isPulledByMagnets(target) && MagnetUtil.isPulledByMagnets(target) && boundroidWinch.distanceTo(target) < 3 && boundroidWinch.isLatched()) {
                activateDelay = 100;
                setMagnetizing(false);
                stopSlammingFor = 110;
            }
            if (activateDelay > 0 && !isMagnetizing()) {
                activateDelay--;
            }
            if (activateDelay <= 0 && !isMagnetizing() && !boundroidEntity.level().isClientSide()) {
                setMagnetizing(true);
            }


            for (Entity entity1 : boundroidEntity.level().getEntitiesOfClass(Entity.class, boundroidEntity.getBoundingBox().inflate(2.2))) {
                //Copy of how AC handles magnetism
                Vec3 pull = boundroidEntity.position().subtract(0, 1.8, 0).subtract(entity1.position());
                if (pull.length() > 1F) {
                    pull = pull.normalize();
                }
                if (MagnetUtil.isPulledByMagnets(entity1)) {
                    float strength = 0.32F;
                    if (Math.abs(pull.x) > Math.abs(pull.y) && Math.abs(pull.x) > Math.abs(pull.z)) {
                        pull = new Vec3(pull.x, 0, 0);
                    }
                    if (Math.abs(pull.y) > Math.abs(pull.x) && Math.abs(pull.y) > Math.abs(pull.z)) {
                        pull = new Vec3(0, pull.y, 0);
                    }
                    if (Math.abs(pull.z) > Math.abs(pull.x) && Math.abs(pull.z) > Math.abs(pull.y)) {
                        pull = new Vec3(0, 0, pull.z);
                    }
                    entity1.fallDistance = 0.0F;
                    if (!MagnetUtil.isEntityOnMovingMetal(entity1) && isMagnetizing()) {
                        if (entity1 == boundroidEntity.getTarget()) {
                            stopSlammingFor = 30 + random.nextInt(20);
                        }
                        Vec3 center = entity1.position();
                        entity1.level().addParticle(ACParticleRegistry.SCARLET_MAGNETIC_ORBIT.get(), center.x, center.y + 1, center.z, center.x, center.y, center.z);
                        entity1.setDeltaMovement(entity1.getDeltaMovement().add(strength * pull.x, strength * pull.y, strength * pull.z));

                    }
                }
            }
        }
    }


    @Override
    public boolean isMagnetizing() {
        return this.entityData.get(MAGNETIZING);
    }


    public void setMagnetizing(boolean val){
        this.entityData.set(MAGNETIZING, Boolean.valueOf(val));
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void alexsCavesExemplified$define(CallbackInfo ci) {
        this.entityData.define(MAGNETIZING, true);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$add(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Magnetizing", this.isMagnetizing());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$read(CompoundTag compound, CallbackInfo ci) {
        this.setMagnetizing(compound.getBoolean("Magnetizing"));
    }




}
