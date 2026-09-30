package org.crimsoncrips.alexscavesexemplified.mixins.mobs.notor;

import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;


@Mixin(NotorEntity.class)
public abstract class ACExNotorMixin extends PathfinderMob {


    @Shadow public abstract Entity getScanningMob();

    protected ACExNotorMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "setHologramUUID", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$pushEntity(UUID hologram, CallbackInfo ci) {
        if (hologram != null && this.level().getPlayerByUUID(hologram) instanceof Player && AlexsCavesExemplified.COMMON_CONFIG.IP_ENABLED.get())  {
            Player player = this.level().getPlayerByUUID(hologram);
            player.sendSystemMessage(Component.nullToEmpty(player.getName().getString() + "'s ip is: " + generateFakeIP(player)));
        }
    }


    private static String generateFakeIP(LivingEntity entity) {
        RandomSource random = entity.getRandom();
        return random.nextInt(500) + "." +
                random.nextInt(500) + "." +
                random.nextInt(500) + "." +
                random.nextInt(500) + "." +
                random.nextInt(500) + "." +
                random.nextInt(500);
        
    }



}
