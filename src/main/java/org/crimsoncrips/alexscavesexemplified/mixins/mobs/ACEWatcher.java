package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.WatcherEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMICompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(WatcherEntity.class)
public abstract class ACEWatcher {

    @WrapOperation(method = "attemptPossession", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/WatcherEntity;canPossessTargetEntity(Lnet/minecraft/world/entity/Entity;)Z"),remap = false)
    private boolean attemptPossesion(WatcherEntity instance, Entity playerData, Operation<Boolean> original) {
        boolean returning = true;
        if (ModList.get().isLoaded("alexsmobsinteraction")){
            if (AlexsCavesExemplified.COMMON_CONFIG.STABILIZER_COMPATIBILITY_ENABLED.get() && playerData instanceof Player player && AMICompat.watcherBoolean(player)) {
                returning = false;
            }
        }

        return returning;
    }


}
