package org.crimsoncrips.alexscavesexemplified.mixins.mobs.boundroid;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidWinchEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoundroidEntity.class)
public abstract class ACExBoundroidMixin extends Monster {

    @Shadow public int stopSlammingFor;


    protected ACExBoundroidMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    int activateDelay;

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        BoundroidEntity boundroidEntity = (BoundroidEntity) (Object) this;
        if (this.getData(ACExAttachments.MAGNETIZING) && boundroidEntity.getRandom().nextDouble() < 0.01){
            for (int i = 0;i < 10;i++){
                Vec3 vec3 = new Vec3((boundroidEntity.getRandom().nextFloat() - 0.5) * 0.3F, (boundroidEntity.getRandom().nextFloat() - 0.5) * 0.3F + boundroidEntity.getRandom().nextInt(-2,3), 2 * 0.5F + 2 * 0.5F * boundroidEntity.getRandom().nextFloat()).yRot((float) ((i / 10F) * Math.PI * 2)).add(boundroidEntity.position());
                boundroidEntity.level().addParticle(ACParticleRegistry.SCARLET_SHIELD_LIGHTNING.get(), vec3.x, vec3.y, vec3.z, boundroidEntity.getX(), boundroidEntity.getY(), boundroidEntity.getZ());
            }
        }


        if (AlexsCavesExemplified.COMMON_CONFIG.BOUNDED_MAGNETISM_ENABLED.get()) {

            LivingEntity target = boundroidEntity.getTarget();
            BoundroidWinchEntity boundroidWinch = (BoundroidWinchEntity) boundroidEntity.getWinch();
            if (this.getData(ACExAttachments.MAGNETIZING) && target != null && MagnetUtil.isPulledByMagnets(target) && MagnetUtil.isPulledByMagnets(target) && boundroidWinch.distanceTo(target) < 3 && boundroidWinch.isLatched()) {
                activateDelay = ACExUtils.seconds(5.0F);
                ACExAttachments.set(this, ACExAttachments.MAGNETIZING, false);
                stopSlammingFor = ACExUtils.seconds(5.5F);
            }
            if (activateDelay > 0 && !this.getData(ACExAttachments.MAGNETIZING)) {
                activateDelay--;
            }
            if (activateDelay <= 0 && !this.getData(ACExAttachments.MAGNETIZING) && !boundroidEntity.level().isClientSide()) {
                ACExAttachments.set(this, ACExAttachments.MAGNETIZING, true);
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
                    if (!MagnetUtil.isEntityOnMovingMetal(entity1) && this.getData(ACExAttachments.MAGNETIZING)) {
                        if (entity1 == boundroidEntity.getTarget()) {
                            stopSlammingFor = ACExUtils.seconds(1.5F) + random.nextInt(ACExUtils.seconds(1.0F));
                        }
                        Vec3 center = entity1.position();
                        entity1.level().addParticle(ACParticleRegistry.SCARLET_MAGNETIC_ORBIT.get(), center.x, center.y + 1, center.z, center.x, center.y, center.z);
                        entity1.setDeltaMovement(entity1.getDeltaMovement().add(strength * pull.x, strength * pull.y, strength * pull.z));

                    }
                }
            }
        }
    }


}
