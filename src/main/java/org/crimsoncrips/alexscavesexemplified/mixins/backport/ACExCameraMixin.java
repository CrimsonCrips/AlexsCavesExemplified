package org.crimsoncrips.alexscavesexemplified.mixins.backport;

import net.minecraft.client.Camera;
import org.crimsoncrips.alexscavesexemplified.client.submarine.ACExSubmarineRemoteClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public abstract class ACExCameraMixin {

    @ModifyArg(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(D)D"))
    private double alexsCavesExemplified$setup(double distance) {
        return ACExSubmarineRemoteClient.cameraDistance((Camera) (Object) this, distance);
    }
}
