package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.item.SeekingArrowEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.LaunchedSeeking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SeekingArrowEntity.class)
public abstract class ACExSeekingArrowEntityMixin extends AbstractArrow implements LaunchedSeeking {


    @Shadow private boolean stopSeeking;

    @Shadow protected abstract void setArcTowardsID(int id);



    int launchDelay = 0;
    protected ACExSeekingArrowEntityMixin(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        int test = getLaunchedTargetID();
        if (test != -1 && launchDelay <= 10){
            launchDelay++;
        }
        if (launchDelay >= 9){
            this.playSound(ACSoundRegistry.SEEKING_ARROW_LOCKON.get(), 5.0F, 1.0F);
            inGround = false;
            setStopSeeking(false);
            setArcTowardsID(getLaunchedTargetID());
            setLaunchedTargetID(-1);
            launchDelay = 0;
        }
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/SeekingArrowEntity;setArcTowardsID(I)V"))
    private boolean alexsCavesExemplified$tick1(SeekingArrowEntity instance, int id) {
        return getLaunchedTargetID() == -1;
    }

    private static final EntityDataAccessor<Integer> LAUNCHED_TARGET_ID = SynchedEntityData.defineId(SeekingArrowEntity.class, EntityDataSerializers.INT);;
    private static final EntityDataAccessor<Float> SPIN_ANGLE = SynchedEntityData.defineId(SeekingArrowEntity.class, EntityDataSerializers.FLOAT);;

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynched(CallbackInfo ci){
        this.entityData.define(LAUNCHED_TARGET_ID, -1);
        this.entityData.define(SPIN_ANGLE, (float)this.random.nextInt(0,361));
    }

    @Override
    public void setLaunchedTargetID(int var) {
        this.entityData.set(LAUNCHED_TARGET_ID, var);
    }

    @Override
    public void setSpinAngle(float var) {
        this.entityData.set(SPIN_ANGLE, var);
    }

    @Override
    public float getSpinAngle() {
        return this.entityData.get(SPIN_ANGLE);
    }

    @Override
    public int getLaunchedTargetID() {
        return this.entityData.get(LAUNCHED_TARGET_ID);
    }

    @Override
    public void setStopSeeking(boolean stopSeeking) {
        this.stopSeeking = stopSeeking;
    }

}
