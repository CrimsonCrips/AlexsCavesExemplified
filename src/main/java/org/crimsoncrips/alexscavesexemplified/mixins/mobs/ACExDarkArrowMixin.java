package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.item.DarkArrowEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(DarkArrowEntity.class)
public abstract class ACExDarkArrowMixin extends AbstractArrow {


    protected ACExDarkArrowMixin(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void hitEntity(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {

            if (AlexsCavesExemplified.COMMON_CONFIG.DREAD_ADDAPTIONS_ENABLED.get()){
                if (this.isOnFire()) {
                    livingEntity.setSecondsOnFire(5);
                }

                if (this.getKnockback() > 0) {
                    double d0 = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale((double) this.getKnockback() * 0.6 * d0);
                    if (vec3.lengthSqr() > 0.0) {
                        livingEntity.push(vec3.x, 0.1, vec3.z);
                    }
                }
            }
        }

    }


}
