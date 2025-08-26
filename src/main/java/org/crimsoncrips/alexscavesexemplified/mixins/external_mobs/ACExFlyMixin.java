package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexthe666.alexsmobs.entity.EntityFly;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExFlyToTrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EntityFly.class)
public abstract class ACExFlyMixin extends Entity implements FlyingAnimal {

    public ACExFlyMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void alexsCavesExemplified$registerGoals(CallbackInfo ci) {
        EntityFly fly = (EntityFly)(Object)this;

        if(AlexsCavesExemplified.COMMON_CONFIG.FLY_TRAPPED_ENABLED.get()) {
            fly.goalSelector.addGoal(4, new ACExFlyToTrap(fly, 0.7, 10));
        }

    }
}
