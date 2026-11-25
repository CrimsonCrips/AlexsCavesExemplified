package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.blockentity.AmberMonolithBlockEntity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AmberMonolithBlockEntity.class)
public interface ACExAmberMonolithAccessor {

    @Accessor("spawnsMobIn")
    void setSpawnsMobIn(int spawnsMobIn);

    @Accessor("spawnType")
    EntityType<?> getSpawnType();

    @Accessor("spawnType")
    void setSpawnType(EntityType<?> spawnType);

    @Accessor("spawnCount")
    int getSpawnCount();

    @Accessor("spawnCount")
    void setSpawnCount(int spawnCount);

    @Invoker("generateSpawnData")
    void invokeGenerateSpawnData();
}
