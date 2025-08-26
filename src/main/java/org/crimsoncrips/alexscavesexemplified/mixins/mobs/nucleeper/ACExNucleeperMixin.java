package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nucleeper;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.loottables.ACExLootTables;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.NucleeperXtra;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExNucleeperMelee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(NucleeperEntity.class)
public abstract class ACExNucleeperMixin extends Monster implements NucleeperXtra {

    @Shadow public abstract void setTriggered(boolean triggered);

    protected ACExNucleeperMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void alexsCavesExemplified$registerGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(2, new ACExNucleeperMelee((NucleeperEntity)(Object)this));
    }


    private static final EntityDataAccessor<Boolean> DEFUSED = SynchedEntityData.defineId(NucleeperEntity.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void alexsCavesExemplified$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(DEFUSED, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Defused", this.isDefused());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        this.setDefused(compound.getBoolean("Defused"));
    }


    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void alexsCavesExemplified$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (player.getItemInHand(hand).is(Items.SHEARS) && !isDefused() && AlexsCavesExemplified.COMMON_CONFIG.DEFUSION_ENABLED.get()){
            ACExUtils.awardAdvancement(player,"defusing","defuse");
            this.setTriggered(false);
            ACExUtils.spawnLoot(ACExLootTables.NUCLEEPER_DEFUSION,this,player,1);
            this.playSound(ACSoundRegistry.NUCLEAR_BOMB_DEFUSE.get());
            setDefused(true);
            player.swing(hand);
            if (!player.isCreative()) {
                player.getItemInHand(hand).hurtAndBreak(1, player, (p_233654_0_) -> {});
            }

        }
    }

    @Inject(method = "explode", at = @At("HEAD"),remap = false)
    private void alexsCavesExemplified$explode(CallbackInfo ci) {
        Level level = this.level();
        int amount = 0;
        for (NucleeperEntity nucleepers : level.getEntitiesOfClass(NucleeperEntity.class, this.getBoundingBox().inflate(13))) {
            if (AlexsCavesExemplified.COMMON_CONFIG.NUCLEAR_CHAIN_ENABLED.get() && !((NucleeperXtra)nucleepers).isDefused()){
                amount++;
                NuclearExplosionEntity explosion = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(level);
                explosion.copyPosition(nucleepers);
                explosion.setSize(nucleepers.isCharged() ? 1.75F : 1F);
                if(!level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)){
                    explosion.setNoGriefing(true);
                }
                level.addFreshEntity(explosion);

            }
        }
        for (Player players : level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(50))) {
            if (amount >= 10){
                ACExUtils.awardAdvancement(players,"nucleeper_annhilation","nuke_chained");
            }
            if (amount != 0){
                ACExUtils.awardAdvancement(players,"chain_reaction","chain");
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.DEFUSION_ENABLED.get() && isDefused()){
            setDefused(false);
        }
    }

    @Override
    public void setDefused(boolean val) {
        this.entityData.set(DEFUSED, Boolean.valueOf(val));
    }

    @Override
    public boolean isDefused() {
        return this.entityData.get(DEFUSED);
    }

    @WrapWithCondition(method = "mobInteract", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/NucleeperEntity;setTriggered(Z)V"))
    private boolean alexsCavesExemplified$tick(NucleeperEntity instance, boolean triggered) {
        return !isDefused();
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 2))
    private boolean nearestAttack(GoalSelector instance, int pPriority, Goal pGoal) {
        return !AlexsCavesExemplified.COMMON_CONFIG.DEFUSION_ENABLED.get();
    }
}
