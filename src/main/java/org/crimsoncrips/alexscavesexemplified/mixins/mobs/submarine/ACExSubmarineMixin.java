package org.crimsoncrips.alexscavesexemplified.mixins.mobs.submarine;

import net.minecraft.util.Mth;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SubmarineEntity.class)
public abstract class ACExSubmarineMixin extends Entity {

    @Shadow private int controlUpTicks;
    @Shadow private int controlDownTicks;
    @Shadow private int turnLeftTicks;
    @Shadow private int turnRightTicks;

    @Shadow public abstract float getAcceleration();
    @Shadow public abstract void setAcceleration(float acceleration);


    @Shadow public abstract void setDamageLevel(int level);

    @Shadow public abstract int getDamageLevel();

    public ACExSubmarineMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (this.level().isClientSide || this.isVehicle() || !this.hasData(ACExAttachments.SUBMARINE_REMOTE_INPUT))
            return;
        ACExSubmarineRemote.RemoteInput input = this.getData(ACExAttachments.SUBMARINE_REMOTE_INPUT);
        if (this.level().getGameTime() - input.time() > 3) {
            this.removeData(ACExAttachments.SUBMARINE_REMOTE_INPUT);
            return;
        }
        if (input.strafe() != 0.0F) {
            float turn = -Math.signum(input.strafe());
            if (turn > 0.0F) {
                this.turnLeftTicks = ACExUtils.seconds(0.25F);
            } else {
                this.turnRightTicks = ACExUtils.seconds(0.25F);
            }
            this.setYRot(this.getYRot() + turn * 2.5F);
        }
        if (input.forward() != 0.0F) {
            float back = -Math.signum(input.forward());
            this.setAcceleration(Mth.approach(this.getAcceleration(), back < 0.0F ? 1.0F : -0.5F, 0.02F));
        }
        if (input.up() && this.controlUpTicks < 2) {
            this.controlUpTicks = ACExUtils.seconds(0.5F);
        }
        if (input.down() && this.controlDownTicks < 2) {
            this.controlDownTicks = ACExUtils.seconds(0.5F);
        }
    }

    @Inject(method = "tickController", at = @At("TAIL"),remap = false)
    private void alexsCavesExemplified$tickController(Player passenger, CallbackInfo ci) {
        SubmarineEntity submarineEntity = (SubmarineEntity)(Object)this;
        if (passenger.xxa != 0 || passenger.zza != 0){
            for (LivingEntity entity : submarineEntity.level().getEntitiesOfClass(LivingEntity.class, submarineEntity.getBoundingBox().inflate(1.6))) {
                if (entity != passenger && entity.getBbHeight() <= 1.6F && AlexsCavesExemplified.COMMON_CONFIG.SUBMARINE_BUMP_ENABLED.get()) {
                    entity.hurt(submarineEntity.damageSources().mobAttack(entity), 6.0F);
                    entity.knockback(1.5D, submarineEntity.getX() - entity.getX(), submarineEntity.getZ() - entity.getZ());
                    ACExUtils.awardAdvancement(submarineEntity.getFirstPassenger(),"submarine_bump","bump");
                }
            }
        }
    }
}