package org.crimsoncrips.alexscavesexemplified.mixins.misc.render;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "net.minecraft.client.renderer.RenderType$CompositeRenderType")
public interface ACExCompositeRenderTypeAccessor {

    @Invoker("state")
    RenderType.CompositeState invokeState();
}
