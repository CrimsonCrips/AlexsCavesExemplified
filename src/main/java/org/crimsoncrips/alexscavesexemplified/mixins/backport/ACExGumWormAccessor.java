package org.crimsoncrips.alexscavesexemplified.mixins.backport;

import com.github.alexmodguy.alexscaves.server.entity.living.GumWormEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = GumWormEntity.class, remap = false)
public interface ACExGumWormAccessor {

    @Accessor("surfaceY")
    double getSurfaceY();
}
