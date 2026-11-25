package org.crimsoncrips.alexscavesexemplified.mixins.misc.render;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(RenderStateShard.EmptyTextureStateShard.class)
public interface ACExTextureStateShardAccessor {

    @Invoker("cutoutTexture")
    Optional<ResourceLocation> invokeCutoutTexture();
}
