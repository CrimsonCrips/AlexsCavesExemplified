package org.crimsoncrips.alexscavesexemplified.mixins.backport;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface ACExCameraAccessor {

    @Invoker("move")
    void invokeMove(double x, double y, double z);

    @Invoker("getMaxZoom")
    double invokeGetMaxZoom(double distance);
}
