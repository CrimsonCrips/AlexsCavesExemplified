package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetClosePlayers;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import com.github.alexmodguy.alexscaves.server.entity.util.TargetsDroppedItems;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.TremorConsumption;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;
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
            tremorsaurus.targetSelector.addGoal(4, new ACEDinosaurEggAttack<>(tremorsaurus, LivingEntity.class, true));
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.TREMOR_V_TREMOR_ENABLED.get()){
            this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.SEETHED_TAMING_ENABLED.get()) {
            this.goalSelector.addGoal(2, new ACETremorTempt(tremorsaurus, 1.1, Ingredient.of(new ItemLike[]{ACBlockRegistry.COOKED_DINOSAUR_CHOP.get(), ACBlockRegistry.DINOSAUR_CHOP.get()}), false));
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

        if (isAlive() && (isVehicle() || tremorsaurus.getTarget() != null) && AlexsCavesExemplified.COMMON_CONFIG.RAVAGING_TREMOR_ENABLED.get() && getRandom().nextDouble() < 0.3){
            AABB aabb = this.getBoundingBox().inflate(0.2D);

            for(BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
                BlockState blockstate = this.level().getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (block instanceof LeavesBlock) {
                    this.level().destroyBlock(blockpos, true, this);
                }
            }
        }
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 6))
    private boolean tempt(GoalSelector instance, int pPriority, Goal pGoal) {
        return !AlexsCavesExemplified.COMMON_CONFIG.SEETHED_TAMING_ENABLED.get();
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 10))
    private boolean hurtBy(GoalSelector instance, int pPriority, Goal pGoal) {
        return !AlexsCavesExemplified.COMMON_CONFIG.TREMOR_V_TREMOR_ENABLED.get();
    }




    public boolean canTargetItem(ItemStack itemStack) {
        return itemStack.is(ACBlockRegistry.DINOSAUR_CHOP.get().asItem()) || itemStack.is(ACBlockRegistry.COOKED_DINOSAUR_CHOP.get().asItem());
    }




    private static final EntityDataAccessor<Boolean> SNIFFED = SynchedEntityData.defineId(TremorsaurusEntity.class, EntityDataSerializers.BOOLEAN);;

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynched(CallbackInfo ci){
        this.entityData.define(SNIFFED, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditional(CompoundTag compound, CallbackInfo ci){
        compound.putBoolean("Sniffed", this.isSniffed());
    }
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditional(CompoundTag compound, CallbackInfo ci){
        this.setSniffed(compound.getBoolean("Sniffed"));
    }



    @Override
    public boolean isSniffed() {
        return this.entityData.get(SNIFFED);
    }


    @Override
    public void setSniffed(boolean value) {
        this.entityData.set(SNIFFED,value);
    }

    public boolean isSeethed(TremorsaurusEntity tremorsaurus){
        return tremorsaurus.hasEffect(ACEEffects.SERENED.get());
    }




}
