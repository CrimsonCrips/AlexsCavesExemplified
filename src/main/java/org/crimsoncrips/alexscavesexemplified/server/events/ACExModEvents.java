package org.crimsoncrips.alexscavesexemplified.server.events;

import com.github.alexmodguy.alexscaves.server.misc.ACCreativeTabRegistry;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;


public class ACExModEvents {


    @SubscribeEvent
    public void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ACCreativeTabRegistry.CANDY_CAVITY.getKey())){
            event.accept(ACExItemRegistry.ICE_CREAM_CONE.get());
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



}

