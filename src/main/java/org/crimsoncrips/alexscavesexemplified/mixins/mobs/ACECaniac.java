package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.CaniacEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACECaniacBedwars;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACECaniacExplode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;


@Mixin(CaniacEntity.class)
public abstract class ACECaniac extends Monster {

    protected ACECaniac(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        CaniacEntity caniac = (CaniacEntity)(Object)this;

        if (AlexsCavesExemplified.COMMON_CONFIG.CANIAC_MANIAC_ENABLED.get()){
            caniac.goalSelector.addGoal(2, new NearestAttackableTargetGoal<>(caniac, LivingEntity.class,5000,false,false, Objects::nonNull){
                @Override
                public void start() {
                    caniac.playSound(ACSoundRegistry.CANIAC_HURT.get(), 2.0F, 0.0F);
                    super.start();
                }
            });
            caniac.goalSelector.addGoal(2, new ACECaniacBedwars(caniac, 24));
            caniac.goalSelector.addGoal(2, new ACECaniacExplode(caniac, 24));
        }
    }

    @ModifyExpressionValue(method = "hurtMobsFromArmSwing", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/CaniacEntity;getType()Lnet/minecraft/world/entity/EntityType;"))
    private EntityType alexsCavesExemplified$hurtMobsFromArmSwing(EntityType original) {
        return AlexsCavesExemplified.COMMON_CONFIG.CANIAC_MANIAC_ENABLED.get() ? null : original;
    }
}
