package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.CorrodentAttackGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.TargetsDroppedItems;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACEItemTagGenerator;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEKnawingGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;


@Mixin(CorrodentEntity.class)
public abstract class ACECorrodent extends Monster implements UnderzealotSacrifice, TargetsDroppedItems {



    private int sacrificeTime = 0;
    private boolean isBeingSacrificed = false;


    protected ACECorrodent(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void alexsCavesExemplified$registerGoals(CallbackInfo ci) {
        CorrodentEntity corrodent = (CorrodentEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get()){
            corrodent.targetSelector.addGoal(2, new MobTarget3DGoal(corrodent, Player.class, false,10, livingEntity -> {
                return livingEntity instanceof Player player && !CuriosCompat.hasLight(player);
            }));
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.KNAWING_ENABLED.get()){
            corrodent.targetSelector.addGoal(1, new ACEKnawingGoal(corrodent, false));
        }



    }

    public void triggerSacrificeIn(int time) {
        isBeingSacrificed = true;
        sacrificeTime = time;
    }

    @Override
    public boolean isValidSacrifice(int distanceFromGround) {
        return AlexsCavesExemplified.COMMON_CONFIG.CORRODENT_CONVERSION_ENABLED.get() && !isLeashed();
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return super.canBeLeashed(pPlayer) || (AlexsCavesExemplified.COMMON_CONFIG.DARK_OFFERING_ENABLED.get());
    }



    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (isBeingSacrificed && this.isPassenger() && !level().isClientSide && AlexsCavesExemplified.COMMON_CONFIG.CORRODENT_CONVERSION_ENABLED.get()) {
            sacrificeTime--;
            if (sacrificeTime < 10) {
                this.level().broadcastEntityEvent(this, (byte) 61);
            }
            if (sacrificeTime < 0) {
                if (this.isPassenger() && this.getVehicle() instanceof UnderzealotEntity underzealot) {
                    underzealot.postSacrifice(this);
                    underzealot.triggerIdleDigging();
                }
                this.stopRiding();
                UnderzealotEntity underzealot1 = this.convertTo(ACEntityRegistry.UNDERZEALOT.get(), true);
                if (underzealot1 != null) {
                    this.playSound(ACSoundRegistry.CORRODENT_HURT.get(), 8.0F, 1.0F);
                    net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, underzealot1);
                    underzealot1.triggerIdleDigging();
                    underzealot1.stopRiding();
                }
            }
        }

        LivingEntity target = this.getTarget();
        if (AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get() && target != null && target != getLastHurtByMob()){
            if (CuriosCompat.hasLight(target)) {
                this.setTarget(null);
                ACEUtils.awardAdvancement(target,"light_repel","repelled");
            }
        }

    }



    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 8))
    private boolean alexsCavesExemplified$nearestAttack(GoalSelector instance, int pPriority, Goal pGoal) {
        return !AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get();
    }

    @Override
    public boolean canTargetItem(ItemStack itemStack) {
        return  (itemStack.isEdible() || itemStack.is(ACEItemTagGenerator.KNAWING));
    }

    public void onGetItem(ItemEntity itemEntity) {
        Item item = itemEntity.getItem().getItem();
        if (item.isEdible()) {
            this.heal(5);
            List<Pair<MobEffectInstance, Float>> test = Objects.requireNonNull(itemEntity.getItem().getFoodProperties(this)).getEffects();
            if (!test.isEmpty()){
                for (int i = 0; i < test.size(); i++){
                    this.addEffect(new MobEffectInstance(test.get(i).getFirst()));
                }
            }
            itemEntity.getItem().shrink(1);
        } else {
            itemEntity.getItem().shrink(1);
            this.heal(1);
        }
    }

}
