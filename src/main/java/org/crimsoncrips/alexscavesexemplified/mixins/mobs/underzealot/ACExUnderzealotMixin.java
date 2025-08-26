package org.crimsoncrips.alexscavesexemplified.mixins.mobs.underzealot;

import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GloomothEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.crimsoncrips.alexscavesexemplified.datagen.loottables.ACExLootTables;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExMobTargetClosePlayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(UnderzealotEntity.class)
public abstract class ACExUnderzealotMixin extends Monster {


    protected ACExUnderzealotMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void alexsCavesExemplified$registerGoals(CallbackInfo ci) {
        UnderzealotEntity underzealot = (UnderzealotEntity)(Object)this;

        underzealot.targetSelector.addGoal(2, new ACExMobTargetClosePlayers(underzealot, 40, 12.0F, livingEntity -> {
            boolean light = (!CuriosCompat.hasLight(livingEntity)) || !AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get();
            boolean respect = (!livingEntity.getItemBySlot(EquipmentSlot.CHEST).is(ACItemRegistry.CLOAK_OF_DARKNESS.get()) && !livingEntity.getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.HOOD_OF_DARKNESS.get())) || !AlexsCavesExemplified.COMMON_CONFIG.UNDERZEALOT_RESPECT_ENABLED.get() ;
            return light && respect;
        }) {
            public boolean canUse() {
                return !underzealot.isTargetingBlocked() && super.canUse();
            }
        });



    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        LivingEntity target = this.getTarget();
        if (AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get() && target != null && target != getLastHurtByMob()){
            if (CuriosCompat.hasLight(target)) {
                this.setTarget(null);
                ACExUtils.awardAdvancement(target,"light_repel","repelled");
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand pHand) {
        UnderzealotEntity underzealot = (UnderzealotEntity)(Object)this;
        Level level = level();

        boolean respect = (player.getItemBySlot(EquipmentSlot.CHEST).is(ACItemRegistry.CLOAK_OF_DARKNESS.get()) && player.getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.HOOD_OF_DARKNESS.get()));




        if (underzealot.getPassengers().isEmpty() && !underzealot.isPraying() && AlexsCavesExemplified.COMMON_CONFIG.DARK_OFFERING_ENABLED.get()) {

            for (Mob leashedEntities : level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(10))) {
                boolean lasso = ModList.get().isLoaded("alexsmobs") && AMCompat.isLeashed(leashedEntities,player);
                if ((leashedEntities.getLeashHolder() == player || lasso)  && leashedEntities instanceof UnderzealotSacrifice && underzealot.getTarget() != player && !leashedEntities.getPersistentData().getBoolean("SacrificeGiven")) {
                    int tradeDeterminer = 0;
                    if (leashedEntities instanceof GloomothEntity) {
                        tradeDeterminer = 1;
                        ACExUtils.awardAdvancement(player, "gloomoth_trade", "gloomoth");
                    } else if (leashedEntities instanceof CorrodentEntity) {
                        tradeDeterminer = 2;
                        ACExUtils.awardAdvancement(player, "corrodent_trade", "corrodent");
                    } else if (leashedEntities instanceof VesperEntity) {
                        tradeDeterminer = 3;
                        ACExUtils.awardAdvancement(player, "vesper_trade", "vesper");
                    }
                    if (lasso){
                        leashedEntities.spawnAtLocation(new ItemStack(AMCompat.amItemRegistry(1)));
                        AMCompat.vineLassoTo(null,leashedEntities);
                    } else {
                        leashedEntities.dropLeash(true,true);
                    }

                    leashedEntities.getPersistentData().putBoolean("SacrificeGiven", true);

                    if (!leashedEntities.level().isClientSide()) {
                        leashedEntities.startRiding(underzealot);
                    }



                    boolean happy;
                    if (AlexsCavesExemplified.COMMON_CONFIG.UNDERZEALOT_RESPECT_ENABLED.get() && respect){
                        ACExUtils.awardAdvancement(player,"dark_respect","respect");

                        ResourceLocation resourceLocation = switch (tradeDeterminer) {
                            case 1 -> ACExLootTables.GLOOMOTH_TRADE;
                            case 2 -> ACExLootTables.CORRODENT_TRADE;
                            default -> ACExLootTables.VESPER_TRADE;
                        };
                        ACExUtils.spawnLoot(resourceLocation,underzealot,player,0);
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
