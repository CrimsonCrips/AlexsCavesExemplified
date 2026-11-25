package org.crimsoncrips.alexscavesexemplified.mixins.misc.render;

import com.github.alexmodguy.alexscaves.client.particle.VoidBeingCloudParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VoidBeingCloudParticle.class)
public interface ACExVoidBeingCloudParticleAccessor {

    @Accessor("spawnedExtras")
    void setSpawnedExtras(boolean spawnedExtras);
}
