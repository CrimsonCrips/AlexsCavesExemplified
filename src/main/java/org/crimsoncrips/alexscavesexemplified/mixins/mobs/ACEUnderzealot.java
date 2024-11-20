package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetClosePlayers;
import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GingerbreadManEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.goals.ACEHurtByTargetGoal;
import org.crimsoncrips.alexscavesexemplified.goals.ACEMobTargetClosePlayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;


@Mixin(UnderzealotEntity.class)
public abstract class ACEUnderzealot extends Monster {


    protected ACEUnderzealot(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        UnderzealotEntity underzealot = (UnderzealotEntity)(Object)this;
        if (ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED){

            underzealot.targetSelector.addGoal(2, new ACEMobTargetClosePlayers(underzealot, 40, 12.0F,livingEntity -> {
                return !livingEntity.isHolding(Ingredient.of(ACExexmplifiedTagRegistry.LIGHT)) && !(livingEntity instanceof Player player && curiosLight(player));
            }) {
                public boolean canUse() {
                    return !underzealot.isTargetingBlocked() && super.canUse();
                }
            });

            underzealot.goalSelector.addGoal(1, new AvoidEntityGoal<>(underzealot, LivingEntity.class, 4.0F, 1.5, 2, (livingEntity) -> {
                return underzealot.getLastAttacker() != livingEntity && (livingEntity.isHolding(Ingredient.of(ACExexmplifiedTagRegistry.LIGHT)) || (livingEntity instanceof Player player && curiosLight(player))) ;
            }));

        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (!ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED)
            return;
        LivingEntity target = this.getTarget();
        if (target == null)
            return;
        if (this.getLastHurtByMob() == target)
            return;
        if (target.isHolding(Ingredient.of(ACExexmplifiedTagRegistry.LIGHT)) || target instanceof Player player && curiosLight(player)) {
            this.setTarget(null);
        }
    }

    public boolean curiosLight(Player player){
        if (ModList.get().isLoaded("curiouslanterns")) {
            ICuriosItemHandler handler = CuriosApi.getCuriosInventory(player).orElseThrow(() -> new IllegalStateException("Player " + player.getName() + " has no curios inventory!"));
            return handler.getStacksHandler("belt").orElseThrow().getStacks().getStackInSlot(0).is(ACExexmplifiedTagRegistry.LIGHT);
        } else return false;
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 12))
    private boolean nearestAttack(GoalSelector instance, int pPriority, Goal pGoal) {
        return !ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED;
    }

}
