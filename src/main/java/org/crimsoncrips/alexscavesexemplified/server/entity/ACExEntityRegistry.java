package org.crimsoncrips.alexscavesexemplified.server.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExEntityRegistry {

    public static final DeferredRegister<EntityType<?>> DEF_REG = DeferredRegister.create(Registries.ENTITY_TYPE, AlexsCavesExemplified.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<GammaNuclearBombEntity>> GAMMA_NUCLEAR_BOMB = DEF_REG.register("gamma_nuclear_bomb", () ->
            EntityType.Builder.<GammaNuclearBombEntity>of(GammaNuclearBombEntity::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .setUpdateInterval(1).setShouldReceiveVelocityUpdates(true)
                    .updateInterval(10).clientTrackingRange(20).build("gamma_nuclear_bomb"));

}

