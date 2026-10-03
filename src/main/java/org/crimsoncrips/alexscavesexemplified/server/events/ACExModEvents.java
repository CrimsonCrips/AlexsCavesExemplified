package org.crimsoncrips.alexscavesexemplified.server.events;

import com.github.alexmodguy.alexscaves.server.misc.ACCreativeTabRegistry;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;
import org.crimsoncrips.alexscavesexemplified.server.entity.GumwirmEntity;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;


public class ACExModEvents {


    @SubscribeEvent
    public void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ACCreativeTabRegistry.CANDY_CAVITY.getKey())){
            event.accept(ACExItemRegistry.ICE_CREAM_CONE.get());
            event.accept(ACExItemRegistry.GUMWIRM_SPAWN_EGG.get());
        }
        if (event.getTabKey().equals(ACCreativeTabRegistry.TOXIC_CAVES.getKey())){
            event.accept(ACExItemRegistry.GAMMA_TREMORZILLA_SPAWN_EGG.get());
        }
        if (event.getTabKey().equals(ACCreativeTabRegistry.PRIMORDIAL_CAVES.getKey())){
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_1.get());
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_2.get());
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_3.get());
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_4.get());
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_5.get());
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_6.get());
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_7.get());
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_8.get());
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_9.get());
        }
        if (event.getTabKey().equals(ACCreativeTabRegistry.MAGNETIC_CAVES.getKey())){
            event.accept(ACExBlockRegistry.METAL_CAULDRON.get());
        }
        if (event.getTabKey().equals(ACCreativeTabRegistry.TOXIC_CAVES.getKey())){
            event.accept(ACExBlockRegistry.GAMMA_NUCLEAR_BOMB.get());
        }
    }

    @SubscribeEvent
    public void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ACExEntityRegistry.GUMWIRM.get(), GumwirmEntity.createAttributes().build());
    }

}

