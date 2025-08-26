package org.crimsoncrips.alexscavesexemplified.server.events;

import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACCreativeTabRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.enchantment.ACEEnchants;
import org.crimsoncrips.alexscavesexemplified.server.item.ACEItemRegistry;


@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ACEModEvents {


    @SubscribeEvent
    public void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ACCreativeTabRegistry.CANDY_CAVITY.getKey())){
            event.accept(ACEItemRegistry.ICE_CREAM_CONE);
        }
        if (event.getTabKey().equals(ACCreativeTabRegistry.PRIMORDIAL_CAVES.getKey())){
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_1);
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_2);
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_3);
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_4);
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_5);
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_6);
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_7);
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_8);
            event.accept(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_9);
        }
        if (event.getTabKey().equals(ACCreativeTabRegistry.MAGNETIC_CAVES.getKey())){
            event.accept(ACExBlockRegistry.METAL_CAULDRON);
        }
        if (event.getTabKey().equals(ACCreativeTabRegistry.TOXIC_CAVES.getKey())){
            event.accept(ACExBlockRegistry.GAMMA_NUCLEAR_BOMB);
        }
    }



}

