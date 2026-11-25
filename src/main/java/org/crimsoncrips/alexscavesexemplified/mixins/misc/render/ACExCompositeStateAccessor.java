package org.crimsoncrips.alexscavesexemplified.mixins.misc.render;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderType.CompositeState.class)
public interface ACExCompositeStateAccessor {

    @Accessor("textureState")
    RenderStateShard.EmptyTextureStateShard getTextureState();
}
