package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEDinosaurEggAttack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AtlatitanEntity.class)
public abstract class ACEAtlatitan extends SauropodBaseEntity {

    protected ACEAtlatitan(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "onStep", at = @At("TAIL"),remap = false)
    private void onStep(CallbackInfo ci) {
        AtlatitanEntity atlatitan = (AtlatitanEntity)(Object)this;
        for (LivingEntity entity : atlatitan.level().getEntitiesOfClass(LivingEntity.class, atlatitan.getBoundingBox().expandTowards(1, -2, 1))) {
            if (entity != atlatitan && !atlatitan.isBaby() && entity.getBbHeight() <= 2.2F && AlexsCavesExemplified.COMMON_CONFIG.STOMP_DAMAGE_ENABLED.get()) {
                entity.hurt(atlatitan.damageSources().mobAttack(atlatitan), 6.0F);
                ACEUtils.awardAdvancement(entity,"splat","stepped");
            }
        }
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        AtlatitanEntity atlatitan = (AtlatitanEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.DINOSAUR_EGG_ANGER_ENABLED.get()){
            atlatitan.targetSelector.addGoal(4, new ACEDinosaurEggAttack<>(atlatitan, LivingEntity.class, true){
                @Override
                public boolean canContinueToUse() {
                    return super.canContinueToUse() && atlatitan.getRideableFor() <= 0 ;
                }
            });
        }
    }
}
