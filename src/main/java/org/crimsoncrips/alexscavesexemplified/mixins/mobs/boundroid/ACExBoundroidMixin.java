package org.crimsoncrips.alexscavesexemplified.mixins.mobs.boundroid;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.spongepowered.asm.mixin.Unique;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidWinchEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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

    @Unique
    private int activateDelay;

    @Unique
    private static final double PULL_RANGE = 2.2D;
    @Unique
    private static final double PULL_POINT_DROP = 1.8D;
    @Unique
    private static final float STRENGTH_PER_ITEM = 0.08F;
    @Unique
    private static final int FULL_MAGNET_ITEMS = 4;
    @Unique
    private static final double WINCH_SHORT_RANGE = 3.0D;

    @Unique
    private static int magneticItems(Entity entity) {
        if (!(entity instanceof LivingEntity living) || living.hasEffect(ACEffectRegistry.MAGNETIZING.get()))
            return FULL_MAGNET_ITEMS;
        int items = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (living.getItemBySlot(slot).is(ACTagRegistry.MAGNETIC_ITEMS)) {
                items++;
            }
        }
        return items > 0 ? items : FULL_MAGNET_ITEMS;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        BoundroidEntity boundroidEntity = (BoundroidEntity) (Object) this;
        if (ACExData.get(this, ACExAttachments.MAGNETIZING) && boundroidEntity.getRandom().nextDouble() < 0.01){
            for (int i = 0;i < 10;i++){
                Vec3 vec3 = new Vec3((boundroidEntity.getRandom().nextFloat() - 0.5) * 0.3F, (boundroidEntity.getRandom().nextFloat() - 0.5) * 0.3F + boundroidEntity.getRandom().nextInt(-2,3), 2 * 0.5F + 2 * 0.5F * boundroidEntity.getRandom().nextFloat()).yRot((float) ((i / 10F) * Math.PI * 2)).add(boundroidEntity.position());
                boundroidEntity.level().addParticle(ACParticleRegistry.SCARLET_SHIELD_LIGHTNING.get(), vec3.x, vec3.y, vec3.z, boundroidEntity.getX(), boundroidEntity.getY(), boundroidEntity.getZ());
            }
        }


        if (AlexsCavesExemplified.COMMON_CONFIG.BOUNDED_MAGNETISM_ENABLED.get()) {

            LivingEntity target = boundroidEntity.getTarget();
            BoundroidWinchEntity boundroidWinch = boundroidEntity.getWinch() instanceof BoundroidWinchEntity winch ? winch : null;
            if (ACExData.get(this, ACExAttachments.MAGNETIZING) && target != null && boundroidWinch != null && MagnetUtil.isPulledByMagnets(target) && boundroidWinch.distanceTo(target) < WINCH_SHORT_RANGE && boundroidWinch.isLatched()) {
                activateDelay = ACExUtils.seconds(5.0F);
                ACExAttachments.set(this, ACExAttachments.MAGNETIZING, false);
                stopSlammingFor = ACExUtils.seconds(5.5F);
            }
            if (activateDelay > 0 && !ACExData.get(this, ACExAttachments.MAGNETIZING)) {
                activateDelay--;
            }
            if (activateDelay <= 0 && !ACExData.get(this, ACExAttachments.MAGNETIZING) && !boundroidEntity.level().isClientSide()) {
                ACExAttachments.set(this, ACExAttachments.MAGNETIZING, true);
            }


            for (Entity entity1 : boundroidEntity.level().getEntitiesOfClass(Entity.class, boundroidEntity.getBoundingBox().inflate(PULL_RANGE))) {
                //Copy of how AC handles magnetism
                Vec3 pull = boundroidEntity.position().subtract(0, PULL_POINT_DROP, 0).subtract(entity1.position());
                if (pull.length() > 1F) {
                    pull = pull.normalize();
                }
                if (MagnetUtil.isPulledByMagnets(entity1)) {
                    float strength = STRENGTH_PER_ITEM * magneticItems(entity1);
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
                    if (!MagnetUtil.isEntityOnMovingMetal(entity1) && ACExData.get(this, ACExAttachments.MAGNETIZING)) {
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
