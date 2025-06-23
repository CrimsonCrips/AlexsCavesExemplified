package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GrottoceratopsEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEDinosaurEggAttack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GrottoceratopsEntity.class)
public abstract class ACEGrottoceratops extends DinosaurEntity {

    protected ACEGrottoceratops(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        GrottoceratopsEntity grottoceratops = (GrottoceratopsEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.EGG_ANGER_ENABLED.get()){
            grottoceratops.targetSelector.addGoal(4, new ACEDinosaurEggAttack<>(grottoceratops, LivingEntity.class, true));
        }
        if (AlexsCavesExemplified.COMMON_CONFIG.ADD_TARGETS_ENABLED.get()){
            grottoceratops.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(grottoceratops, LivingEntity.class, 150, true, false, livingEntity -> {
                return livingEntity.isHolding(Ingredient.of(ACItemRegistry.LIMESTONE_SPEAR.get()));
            }){
                @Override
                public boolean canContinueToUse() {
                    return super.canContinueToUse() && !grottoceratops.isTame();
                }
            });
        }
    }
}
