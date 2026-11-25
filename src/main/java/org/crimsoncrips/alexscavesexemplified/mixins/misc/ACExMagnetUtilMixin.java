package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.misc.ACExMagnerip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil.*;


@Mixin(MagnetUtil.class)
public class ACExMagnetUtilMixin {

    @Unique
    private static final double PULL_THRESHOLD = 1.0E-6D;


    @Inject(method = "tickMagnetism", at = @At(value = "TAIL"), remap = false)
    private static void alexsCavesExemplified$tickMagnetism(Entity entity, CallbackInfo ci) {
        if (entity instanceof Player player && getEntityMagneticDelta(entity).lengthSqr() > PULL_THRESHOLD) {
            ACExMagnerip.pulled(player);
        }
    }

}
