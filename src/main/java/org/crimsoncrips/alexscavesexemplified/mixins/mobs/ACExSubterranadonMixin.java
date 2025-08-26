package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExDinosaurEggAttack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SubterranodonEntity.class)
public abstract class ACExSubterranadonMixin extends DinosaurEntity {

    protected ACExSubterranadonMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        SubterranodonEntity subterranodon = (SubterranodonEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.EGG_ANGER_ENABLED.get()){
            subterranodon.targetSelector.addGoal(4, new ACExDinosaurEggAttack<>(subterranodon, LivingEntity.class, true));
        }
    }
}
