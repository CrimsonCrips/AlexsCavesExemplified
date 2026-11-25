package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(DeepOneBaseEntity.class)
public interface ACExDeepOneAccessor {

    @Accessor(value = "summonerUUID", remap = false)
    UUID getSummonerUUID();
}
