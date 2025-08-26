package org.crimsoncrips.alexscavesexemplified.server.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ACExEntityRegistry {

    public static final DeferredRegister<EntityType<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AlexsCavesExemplified.MODID);

    public static final RegistryObject<EntityType<GammaNuclearBombEntity>> GAMMA_NUCLEAR_BOMB = DEF_REG.register("gamma_nuclear_bomb", () ->
            (EntityType) EntityType.Builder.of(GammaNuclearBombEntity::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .setCustomClientFactory(GammaNuclearBombEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true)
                    .updateInterval(10).clientTrackingRange(20).build("gamma_nuclear_bomb"));

}

