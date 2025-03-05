package org.crimsoncrips.alexscavesexemplified.mixins.mobs.mine_guardian;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEMineGuardianHurtBy;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Debug (export = true)
@Mixin(MineGuardianEntity.class)
public abstract class ACEMineGuardian extends Monster implements MineGuardianXtra {

    @Shadow public abstract boolean isExploding();

    @Shadow private float explodeProgress;
    private static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.INT);

    protected ACEMineGuardian(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void alexsCavesExemplified$registerGoals(CallbackInfo ci) {
        MineGuardianEntity mineGuardian = (MineGuardianEntity)(Object)this;
        mineGuardian.targetSelector.addGoal(1, new ACEMineGuardianHurtBy(mineGuardian, new Class[0]));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.REMINEDING_ENABLED.get() && !Objects.equals(alexsCavesExemplified$getOwner(), "-1")){
            alexsCavesExemplified$setOwner("-1");
        }
        if (alexsCavesExemplified$getVariant() >= 1){
            if (this.getName().getString().equals("Ae")){
                alexsCavesExemplified$setVariant(1);
            }
            if (this.getName().getString().equals("Jesse")){
                alexsCavesExemplified$setVariant(2);
            }
        }
        if (alexsCavesExemplified$getVariant() > 0){
            if (this.random.nextInt(80) == 0) {
                this.level().playLocalSound(this.getX() + 0.5D, this.getY() + 0.5D, this.getZ() + 0.5D, ACSoundRegistry.URANIUM_HUM.get(), SoundSource.BLOCKS, 0.5F, this.random.nextFloat() * 0.4F + 0.8F, false);
            }
            if (this.random.nextInt(10) == 0) {
                this.level().addParticle(ACParticleRegistry.PROTON.get(), this.getX(), this.getY() + 0.5, this.getZ(), this.getX(), this.getY(), this.getZ());
            }

            if (!AlexsCavesExemplified.COMMON_CONFIG.NAVAL_NUCLEARITY_ENABLED.get()){
                alexsCavesExemplified$setVariant(0);
            }
        }
        if (AlexsCavesExemplified.COMMON_CONFIG.NOON_GUARDIAN_ENABLED.get()){
            if (this.getName().getString().equals("Noon") && alexsCavesExemplified$getVariant() == 0){
                this.alexsCavesExemplified$setVariant(-1);
                for (Player deepOne : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(10))) {
                    ACEUtils.awardAdvancement(deepOne,"noon_guardian","noon");
                }
            }
        } else if (alexsCavesExemplified$getVariant() == -1){
            alexsCavesExemplified$setVariant(1);
        }
    }

    @Override
    public int alexsCavesExemplified$getVariant() {
        return this.entityData.get(VARIANT);
    }

    public String alexsCavesExemplified$getOwner() {
        return this.entityData.get(OWNER);
    }

    @Override
    public void alexsCavesExemplified$setVariant(int variant) {
        this.entityData.set(VARIANT, Integer.valueOf(variant));
    }

    @Override
    public void alexsCavesExemplified$setOwner(String playerUUID) {
        this.entityData.set(OWNER, String.valueOf(playerUUID));
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void alexsCavesExemplified$define(CallbackInfo ci) {
        this.entityData.define(OWNER, "-1");
        this.entityData.define(VARIANT, 0);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$add(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("Variant", this.alexsCavesExemplified$getVariant());
        compound.putString("MineOwner", this.alexsCavesExemplified$getOwner());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$read(CompoundTag compound, CallbackInfo ci) {
        this.alexsCavesExemplified$setVariant(compound.getInt("Variant"));
        this.alexsCavesExemplified$setOwner(compound.getString("MineOwner"));
    }

    @Inject(method = "isValidTarget", at = @At("HEAD"),cancellable = true,remap = false)
    private void alexsCavesExemplified$isValidTarget(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(AlexsCavesExemplified.COMMON_CONFIG.REMINEDING_ENABLED.get() && entity instanceof Player player){
            cir.setReturnValue(canAttack(player) && !Objects.equals(player.getUUID().toString(), alexsCavesExemplified$getOwner()));
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/MineGuardianEntity;isExploding()Z",ordinal = 2))
    private boolean alexsCavesExemplified$tick(MineGuardianEntity instance, Operation<Boolean> original) {
        return original.call(instance) && alexsCavesExemplified$getVariant() < 1;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick1(CallbackInfo ci) {
        if(alexsCavesExemplified$getVariant() > 0){
            if (this.isExploding()) {
                if (this.explodeProgress >= 10.0F) {
                    this.remove(RemovalReason.KILLED);
                    this.nukeExplode();
                }

                this.setDeltaMovement(this.getDeltaMovement().multiply((double)0.3F, (double)1.0F, (double)0.3F));
            }
        }
    }

    private void nukeExplode() {
        NuclearExplosionEntity explosion = (NuclearExplosionEntity)((EntityType) ACEntityRegistry.NUCLEAR_EXPLOSION.get()).create(this.level());
        explosion.copyPosition(this);
        explosion.setSize((float) (AlexsCaves.COMMON_CONFIG.nukeExplosionSizeModifier.get().floatValue() * 0.8));
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            explosion.setNoGriefing(true);
        }

        this.level().addFreshEntity(explosion);
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 2))
    private boolean alexsCavesExemplified$registerGoals(GoalSelector instance, int pPriority, Goal pGoal) {
        return !AlexsCavesExemplified.COMMON_CONFIG.REMINEDING_ENABLED.get();
    }


    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemHeld = pPlayer.getItemInHand(pHand);
        if (AlexsCavesExemplified.COMMON_CONFIG.NAVAL_NUCLEARITY_ENABLED.get() && itemHeld.is(ACBlockRegistry.NUCLEAR_BOMB.get().asItem()) && alexsCavesExemplified$getVariant() == 0){
            alexsCavesExemplified$setVariant(random.nextBoolean() ? 1 : 2);
            if (!pPlayer.isCreative()){
                itemHeld.shrink(1);
            }
            playSound(SoundEvents.SMITHING_TABLE_USE, 8.0F, 1.0F);
            ACEUtils.awardAdvancement(pPlayer,"nuclear_mines","nuke_mine");

            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(pPlayer, pHand);
    }


}
