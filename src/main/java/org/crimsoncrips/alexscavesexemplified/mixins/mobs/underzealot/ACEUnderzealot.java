package org.crimsoncrips.alexscavesexemplified.mixins.mobs.underzealot;

import com.github.alexmodguy.alexscaves.server.entity.ai.UnderzealotCaptureSacrificeGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GloomothEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEMobTargetClosePlayers;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEUnderzealotExtinguishCampfires;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(UnderzealotEntity.class)
public abstract class ACEUnderzealot extends Monster {


    protected ACEUnderzealot(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void alexsCavesExemplified$registerGoals(CallbackInfo ci) {
        UnderzealotEntity underzealot = (UnderzealotEntity)(Object)this;

        underzealot.targetSelector.addGoal(2, new ACEMobTargetClosePlayers(underzealot, 40, 12.0F,livingEntity -> {
            boolean light = (!CuriosCompat.hasLight(livingEntity)) || !AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get();
            boolean respect = (!livingEntity.getItemBySlot(EquipmentSlot.CHEST).is(ACItemRegistry.CLOAK_OF_DARKNESS.get()) && !livingEntity.getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.HOOD_OF_DARKNESS.get())) || !AlexsCavesExemplified.COMMON_CONFIG.UNDERZEALOT_RESPECT_ENABLED.get() ;
            return light && respect;
        }) {
            public boolean canUse() {
                return !underzealot.isTargetingBlocked() && super.canUse();
            }
        });

        if (AlexsCavesExemplified.COMMON_CONFIG.EXTINGUISH_CAMPFIRES_ENABLED.get()){
            this.goalSelector.addGoal(7, new ACEUnderzealotExtinguishCampfires(underzealot, 32));
        }



    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        LivingEntity target = this.getTarget();
        if (AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get() && target != null && target != getLastHurtByMob()){
            if (CuriosCompat.hasLight(target)) {
                this.setTarget(null);
                ACEUtils.awardAdvancement(target,"light_repel","repelled");
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand pHand) {
        UnderzealotEntity underzealot = (UnderzealotEntity)(Object)this;
        Level level = level();

        boolean respect = (player.getItemBySlot(EquipmentSlot.CHEST).is(ACItemRegistry.CLOAK_OF_DARKNESS.get()) && player.getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.HOOD_OF_DARKNESS.get()));


        ACEUtils.awardAdvancement(player,"dark_respect","respect");



        if (underzealot.getPassengers().isEmpty() && !underzealot.isPraying() && AlexsCavesExemplified.COMMON_CONFIG.DARK_OFFERING_ENABLED.get()) {

            for (Mob leashedEntities : level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(10))) {
                boolean lasso = ModList.get().isLoaded("alexsmobs") && AMCompat.isLeashed(leashedEntities,player);
                if ((leashedEntities.getLeashHolder() == player || lasso) && leashedEntities instanceof UnderzealotSacrifice) {
                    if (leashedEntities instanceof GloomothEntity) {
                        ACEUtils.awardAdvancement(player, "gloomoth_trade", "gloomoth");
                    } else if (leashedEntities instanceof CorrodentEntity) {
                        ACEUtils.awardAdvancement(player, "corrodent_trade", "corrodent");
                    } else if (leashedEntities instanceof VesperEntity) {
                        ACEUtils.awardAdvancement(player, "vesper_trade", "vesper");
                    }
                    if (lasso){
                        leashedEntities.spawnAtLocation(new ItemStack(AMCompat.amItemRegistry(3)));
                        AMCompat.vineLassoTo(null,leashedEntities);
                    } else {
                        leashedEntities.dropLeash(true,true);
                    }
                    leashedEntities.startRiding(underzealot);



                    boolean happy;
                    if (AlexsCavesExemplified.COMMON_CONFIG.UNDERZEALOT_RESPECT_ENABLED.get() && respect){
                        if (level instanceof ServerLevel serverLevel){
                            ResourceLocation sacrificeLocation = new ResourceLocation(AlexsCavesExemplified.MODID, "entities/underzealot_trade");
                            LootParams ctx = new LootParams.Builder(serverLevel).withParameter(LootContextParams.THIS_ENTITY, underzealot).create(LootContextParamSets.EMPTY);
                            ObjectArrayList<ItemStack> rewards = level.getServer().getLootData().getLootTable(sacrificeLocation).getRandomItems(ctx);

                            rewards.forEach(stack -> BehaviorUtils.throwItem(underzealot, rewards.get(0), player.position().add(0.0D, 1.0D, 0.0D)));
                        }
                        happy = true;
                    } else {
                        happy = false;
                    }
                    for(int i = 0; i < 5; ++i) {
                        double d0 = underzealot.getRandom().nextGaussian() * 0.02D;
                        double d1 = underzealot.getRandom().nextGaussian() * 0.02D;
                        double d2 = underzealot.getRandom().nextGaussian() * 0.02D;
                        underzealot.level().addParticle((happy ? ParticleTypes.HAPPY_VILLAGER : ParticleTypes.ANGRY_VILLAGER), underzealot.getRandomX(1.0D), underzealot.getRandomY() + 1.0D, underzealot.getRandomZ(1.0D), d0, d1, d2);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.mobInteract(player, pHand);
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 12))
    private boolean alexsCavesExemplified$nearestAttack(GoalSelector instance, int pPriority, Goal pGoal) {
        return !AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get() && !AlexsCavesExemplified.COMMON_CONFIG.UNDERZEALOT_RESPECT_ENABLED.get();
    }


}
