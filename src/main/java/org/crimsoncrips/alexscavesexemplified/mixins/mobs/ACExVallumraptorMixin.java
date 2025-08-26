package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExVallumraptorEatBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(VallumraptorEntity.class)
public abstract class ACExVallumraptorMixin extends DinosaurEntity {

    protected ACExVallumraptorMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        VallumraptorEntity vallumraptor = (VallumraptorEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.EGG_ANGER_ENABLED.get()){
            vallumraptor.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(vallumraptor, LivingEntity.class, 100, true, false,livingEntity -> {
                return livingEntity.isHolding(Ingredient.of(ACBlockRegistry.VALLUMRAPTOR_EGG.get()));
            }){
                @Override
                public boolean canContinueToUse() {
                    return super.canContinueToUse() && !vallumraptor.isTame();
                }
            });
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.SCAVENGING_ENABLED.get()){
            vallumraptor.goalSelector.addGoal(6, new ACExVallumraptorEatBlock(vallumraptor, 1.4, 20));

        }
    }

}
