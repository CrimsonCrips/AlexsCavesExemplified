package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetClosePlayers;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import com.github.alexmodguy.alexscaves.server.entity.util.TargetsDroppedItems;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.TremorConsumption;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEDinosaurEggAttack;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACETremorDroppedEatBlock;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACETremorEatBlock;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACETremorTempt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TremorsaurusEntity.class)
public abstract class ACETremorsaurus extends DinosaurEntity implements TargetsDroppedItems, TremorConsumption {


    protected ACETremorsaurus(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void registerGoals(CallbackInfo ci) {
        TremorsaurusEntity tremorsaurus = (TremorsaurusEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.DINOSAUR_EGG_ANGER_ENABLED.get()){
            tremorsaurus.targetSelector.addGoal(4, new ACEDinosaurEggAttack<>(tremorsaurus, LivingEntity.class, true){
                @Override
                public boolean canContinueToUse() {
                    return super.canContinueToUse() && !isSeethed();
                }
            });
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.TREMOR_V_TREMOR_ENABLED.get()){
            this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.SEETHED_TAMING_ENABLED.get()) {
            this.goalSelector.addGoal(2, new ACETremorTempt(tremorsaurus, 1.1, Ingredient.of(new ItemLike[]{(ItemLike) ACBlockRegistry.COOKED_DINOSAUR_CHOP.get(), ACBlockRegistry.DINOSAUR_CHOP.get()}), false){
                @Override
                public boolean canUse() {
                    return super.canUse() && isSeethed();
                }
            });


            this.targetSelector.addGoal(2, new MobTargetClosePlayers(tremorsaurus, 50, 8.0F) {
                @Override
                public boolean canContinueToUse() {
                    return super.canContinueToUse() && !isSeethed();
                }
            });

        }


        if (AlexsCavesExemplified.COMMON_CONFIG.SCAVENGING_ENABLED.get()){
            tremorsaurus.goalSelector.addGoal(3, new ACETremorEatBlock(tremorsaurus, 1, 30,3));

            tremorsaurus.targetSelector.addGoal(2, new ACETremorDroppedEatBlock(tremorsaurus,true,true,200 + tremorsaurus.getRandom().nextInt(150),30));

        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorsaurusEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void stomp(CallbackInfo ci) {
        TremorsaurusEntity tremorsaurus = (TremorsaurusEntity)(Object)this;
        for (LivingEntity entity : tremorsaurus.level().getEntitiesOfClass(LivingEntity.class, tremorsaurus.getBoundingBox().expandTowards(1, -2, 1))) {
            if (entity != tremorsaurus && entity.getBbHeight() <= 0.8F) {
                entity.hurt(tremorsaurus.damageSources().mobAttack(tremorsaurus), 3.0F);
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo ci) {
        TremorsaurusEntity tremorsaurus = (TremorsaurusEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.SEETHED_TAMING_ENABLED.get()){
            if (isSeethed()){
                if (tremorsaurus.level().isClientSide){
                    tremorsaurus.level().addParticle(ACParticleRegistry.HAPPINESS.get(), tremorsaurus.getX(), tremorsaurus.getEyeY() - (tremorsaurus.isOrderedToSit() ? 2 : 0), tremorsaurus.getZ(), ((double) this.random.nextFloat() - (double) 0.5F) * 0.2, ((double) this.random.nextFloat() - (double) 0.5F) * 0.2, ((double) this.random.nextFloat() - (double) 0.5F) * 0.2);
                }
                if (tremorsaurus.getPersistentData().getInt("LoseSeethe") < 2400){
                    tremorsaurus.getPersistentData().putInt("LoseSeethe", tremorsaurus.getPersistentData().getInt("LoseSeethe") + 1);
                } else {
                    setSeethed(false);
                    tremorsaurus.getPersistentData().putInt("LoseSeethe", 0);
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof LivingEntity){
            setSeethed(false);
        }
        return super.hurt(pSource, pAmount);
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 6))
    private boolean tempt(GoalSelector instance, int pPriority, Goal pGoal) {
        return !AlexsCavesExemplified.COMMON_CONFIG.SEETHED_TAMING_ENABLED.get();
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 10))
    private boolean hurtBy(GoalSelector instance, int pPriority, Goal pGoal) {
        return !AlexsCavesExemplified.COMMON_CONFIG.TREMOR_V_TREMOR_ENABLED.get();
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 11))
    private boolean targetPlayer(GoalSelector instance, int pPriority, Goal pGoal) {
        return !AlexsCavesExemplified.COMMON_CONFIG.SEETHED_TAMING_ENABLED.get();
    }



    public boolean canTargetItem(ItemStack itemStack) {
        return itemStack.is(ACBlockRegistry.DINOSAUR_CHOP.get().asItem()) || itemStack.is(ACBlockRegistry.COOKED_DINOSAUR_CHOP.get().asItem());
    }





    private static final EntityDataAccessor<Boolean> SEETHED = SynchedEntityData.defineId(TremorsaurusEntity.class, EntityDataSerializers.BOOLEAN);;
    private static final EntityDataAccessor<Boolean> SNIFFED = SynchedEntityData.defineId(TremorsaurusEntity.class, EntityDataSerializers.BOOLEAN);;

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynched(CallbackInfo ci){
        this.entityData.define(SEETHED, false);
        this.entityData.define(SNIFFED, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditional(CompoundTag compound, CallbackInfo ci){
        compound.putBoolean("Seethed", this.isSeethed());
        compound.putBoolean("Sniffed", this.isSniffed());
    }
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditional(CompoundTag compound, CallbackInfo ci){
        this.setSniffed(compound.getBoolean("Seethed"));
        this.setSniffed(compound.getBoolean("Sniffed"));
    }


    @Override
    public boolean isSeethed() {
        return this.entityData.get(SEETHED);
    }

    @Override
    public boolean isSniffed() {
        return this.entityData.get(SNIFFED);
    }

    @Override
    public void setSeethed(boolean value) {
        this.entityData.set(SEETHED, value);
    }

    @Override
    public void setSniffed(boolean value) {
        this.entityData.set(SNIFFED,value);
    }




}
