package org.crimsoncrips.alexscavesexemplified.mixins.mobs.submarine;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.misc.ACExTrackedSubmarine;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.AlexsCaves;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import net.minecraft.util.Mth;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineTelegraph;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SubmarineEntity.class)
public abstract class ACExSubmarineMixin extends Entity implements ACExTrackedSubmarine {

    @Unique
    private static final float SPEED_SMOOTHING = 0.3F;

    @Unique
    private float remoteTurn;

    @Unique
    private int remoteCreakTime;

    @Unique
    private Vec3 lastTrackedPos;

    @Unique
    private float trackedSpeed;

    @Override
    public float getTrackedSpeed() {
        return this.trackedSpeed;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick1(CallbackInfo ci) {
        Vec3 now = this.position();
        float moved = this.lastTrackedPos == null ? 0.0F : (float) now.distanceTo(this.lastTrackedPos);
        this.trackedSpeed = Mth.lerp(SPEED_SMOOTHING, this.trackedSpeed, moved);
        this.lastTrackedPos = now;
    }

    @Shadow private int controlUpTicks;
    @Shadow private int controlDownTicks;
    @Shadow private int turnLeftTicks;
    @Shadow private int turnRightTicks;

    @Shadow public abstract float getAcceleration();
    @Shadow public abstract void setAcceleration(float acceleration);


    @Shadow public abstract void setDamageLevel(int level);

    @Shadow public abstract int getDamageLevel();

    @Shadow public abstract int getDangerAlertTicks();

    public ACExSubmarineMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean isControlledByLocalInstance() {
        if (ACExSubmarineRemote.isRemoteControlled(this))
            return this.level().isClientSide;
        return super.isControlledByLocalInstance();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = At.Shift.AFTER))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (!this.level().isClientSide && ACExData.has(this, ACExAttachments.SUBMARINE_REMOTE_TIME) && !ACExSubmarineRemote.isRemoteControlled(this)) {
            ACExData.remove(this, ACExAttachments.SUBMARINE_REMOTE_TIME);
            ACExAttachments.set(this, ACExAttachments.SUBMARINE_DRIVER, -1);
            this.setAcceleration(0.0F);
            this.setDeltaMovement(Vec3.ZERO);
        }
        if (!this.level().isClientSide && !this.isVehicle() && ACExSubmarineRemote.isRemoteControlled(this)) {
            this.tickRemoteCabinSounds();
        }
        if (!this.level().isClientSide || !ACExSubmarineRemote.isRemoteControlled(this)) {
            this.remoteTurn = 0.0F;
            return;
        }
        if (this.isInWaterOrBubble() && this.isAlive()) {
            AlexsCaves.PROXY.playWorldSound(this, (byte) 15);
        }
        ACExSubmarineRemote.LocalControl input = ACExSubmarineRemote.LOCAL;
        this.remoteTurn = Mth.approach(this.remoteTurn, -Math.signum(input.strafe) * ACExSubmarineRemote.TURN_SPEED, ACExSubmarineRemote.TURN_ACCELERATION);
        if (this.remoteTurn != 0.0F) {
            if (this.remoteTurn > 0.0F) {
                this.turnLeftTicks = ACExUtils.seconds(0.25F);
            } else {
                this.turnRightTicks = ACExUtils.seconds(0.25F);
            }
            this.setYRot(this.getYRot() + this.remoteTurn);
        }
        if (ACExSubmarineTelegraph.engaged(this)) {
            this.setAcceleration(ACExSubmarineTelegraph.throttle(this.getAcceleration(), this.trackedSpeed));
        } else if (input.forward != 0.0F) {
            this.setAcceleration(Mth.approach(this.getAcceleration(), input.forward > 0.0F ? 1.0F : -0.5F, 0.02F));
        }
        if (input.up && this.controlUpTicks < 2) {
            this.controlUpTicks = ACExUtils.seconds(0.5F);
        }
        if (input.down && this.controlDownTicks < 2) {
            this.controlDownTicks = ACExUtils.seconds(0.5F);
        }
    }

    @Inject(method = "tickController", at = @At("TAIL"),remap = false)
    private void alexsCavesExemplified$tickController(Player passenger, CallbackInfo ci) {
        SubmarineEntity submarineEntity = (SubmarineEntity)(Object)this;
        boolean telegraph = ACExSubmarineTelegraph.engaged(this);
        if (telegraph) {
            this.setAcceleration(ACExSubmarineTelegraph.throttle(this.getAcceleration(), this.trackedSpeed));
        }
        if (passenger.xxa != 0 || passenger.zza != 0 || telegraph && ACExSubmarineTelegraph.order != ACExSubmarineTelegraph.Order.ALL_STOP){
            for (LivingEntity entity : submarineEntity.level().getEntitiesOfClass(LivingEntity.class, submarineEntity.getBoundingBox().inflate(1.6))) {
                if (entity != passenger && entity.getBbHeight() <= 1.6F && AlexsCavesExemplified.COMMON_CONFIG.SUBMARINE_CHANGES_ENABLED.get()) {
                    entity.hurt(submarineEntity.damageSources().mobAttack(entity), 6.0F);
                    entity.knockback(1.5D, submarineEntity.getX() - entity.getX(), submarineEntity.getZ() - entity.getZ());
                    ACExUtils.awardAdvancement(submarineEntity.getFirstPassenger(),"submarine_bump","bump");
                }
            }
        }
    }

    @Unique
    private void tickRemoteCabinSounds() {
        if (this.getDamageLevel() > 0 && this.remoteCreakTime-- <= 0) {
            this.remoteCreakTime = 500 - this.getDamageLevel() * 120 + this.random.nextInt(60);
            this.playSound(ACSoundRegistry.SUBMARINE_CREAK.get());
        }
        if (this.getDangerAlertTicks() > 0 && this.getDamageLevel() <= 3 && this.tickCount % ACExUtils.seconds(1.0F) == 0) {
            this.playSound(ACSoundRegistry.SUBMARINE_SONAR.get());
        }
    }
}
