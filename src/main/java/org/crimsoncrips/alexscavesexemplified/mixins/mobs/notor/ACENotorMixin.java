package org.crimsoncrips.alexscavesexemplified.mixins.mobs.notor;

import com.github.alexmodguy.alexscaves.server.entity.ai.CorrodentAttackGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.NotorHologramGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.NotorScanGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GammaroachEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEBaseInterface;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACESelfDestruct;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;


@Mixin(NotorEntity.class)
public abstract class ACENotorMixin extends PathfinderMob implements ACEBaseInterface {


    @Shadow public abstract Entity getScanningMob();

    protected ACENotorMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "setHologramUUID", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$pushEntity(UUID hologram, CallbackInfo ci) {
        if (hologram != null && this.level().getPlayerByUUID(hologram) instanceof Player && AlexsCavesExemplified.COMMON_CONFIG.IP_ENABLED.get())  {
            Player player = this.level().getPlayerByUUID(hologram);
            player.sendSystemMessage(Component.nullToEmpty(player.getName().getString() + "'s ip is: " + generateFakeIP(player)));
        }
    }

    @Inject(method = "registerGoals", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$registerGoals(CallbackInfo ci) {
        NotorEntity notor = (NotorEntity)(Object)this;

        if (AlexsCavesExemplified.COMMON_CONFIG.SELF_DESTRUCT_ENABLED.get()){
            this.goalSelector.addGoal(1, new NotorHologramGoal(notor){
                @Override
                public boolean canUse() {
                    return super.canUse() && getLastHurtByMob() == null;
                }
            });
            this.goalSelector.addGoal(2, new NotorScanGoal(notor){
                @Override
                public boolean canUse() {
                    return super.canUse() && getLastHurtByMob() == null;
                }
            });
            this.goalSelector.addGoal(2, new ACESelfDestruct(notor));

        }
    }


    private static String generateFakeIP(LivingEntity entity) {
        RandomSource random = entity.getRandom();
        return random.nextInt(500) + "." +
                random.nextInt(500) + "." +
                random.nextInt(500) + "." +
                random.nextInt(500) + "." +
                random.nextInt(500) + "." +
                random.nextInt(500);
        
    }

    private static final EntityDataAccessor<Integer> SELF_DESTRUCT_TIME = SynchedEntityData.defineId(NotorEntity.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void alexsCavesExemplified$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(SELF_DESTRUCT_TIME, 0);
    }



    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("SelfDestructTime", this.getSelfDestructTime());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        this.setSelfDestructTime(compound.getInt("SelfDestructTime"));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        int destructTime = getSelfDestructTime();
        if (destructTime > 0) {
            int newI = destructTime - Math.max(1, destructTime / 20);
            setSelfDestructTime(newI);

            //Thank you Reimnop and ChatGPT for the math, shame im too retarded to know how shit works
            setSelfDestructTime(destructTime - Math.min((destructTime / 5) + 1, Math.max(1, (int) Math.pow(2, Math.log(200 / destructTime) / Math.log(2)))));
        }
        if (destructTime == 1){
            ACEUtils.awardAdvancement(getLastHurtByMob(),"self_destruct","explode");
            this.level().explode(this,this.getX(),this.getY(),this.getZ(),2, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (AlexsCavesExemplified.COMMON_CONFIG.SELF_DESTRUCT_ENABLED.get()) {
            if (getSelfDestructTime() <= 0) {
                setSelfDestructTime(200);
            }
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public int getSelfDestructTime() {
        return this.entityData.get(SELF_DESTRUCT_TIME);
    }

    public void setSelfDestructTime(int destructTime){
        this.entityData.set(SELF_DESTRUCT_TIME, Integer.valueOf(destructTime));
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 1))
    private boolean alexsCavesExemplified$registerGoals1(GoalSelector instance, int pPriority, Goal pGoal) {
        return !AlexsCavesExemplified.COMMON_CONFIG.SELF_DESTRUCT_ENABLED.get();
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 2))
    private boolean alexsCavesExemplified$registerGoals2(GoalSelector instance, int pPriority, Goal pGoal) {
        return !AlexsCavesExemplified.COMMON_CONFIG.SELF_DESTRUCT_ENABLED.get();
    }


}
