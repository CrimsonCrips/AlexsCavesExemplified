package org.crimsoncrips.alexscavesexemplified.server.events;





import net.minecraft.world.level.block.Block;
import java.util.Set;
import java.util.HashSet;
import org.crimsoncrips.alexscavesexemplified.mixins.backport.ACExBlockEntityTypeAccessor;
import com.github.alexmodguy.alexscaves.server.block.blockentity.ACBlockEntityRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACCreativeTabRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.blocks.PreservedCreature;
import org.crimsoncrips.alexscavesexemplified.server.item.PreservedAmberItem;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;
import org.crimsoncrips.alexscavesexemplified.server.entity.GumwirmEntity;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;


public class ACExModEvents {


    @SubscribeEvent
    public void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ACCreativeTabRegistry.CANDY_CAVITY.getKey())){
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
            for (PreservedCreature creature : PreservedCreature.values()) {
                if (creature.isAvailable()) {
                    event.accept(PreservedAmberItem.withCreature(new ItemStack(ACExBlockRegistry.PRESERVED_AMBER.get()), creature));
                }
            }
        }
        if (event.getTabKey().equals(ACCreativeTabRegistry.FORLORN_HOLLOWS.getKey())){
            event.accept(ACExBlockRegistry.DARK_MONOLITH.get());
        }
        if (event.getTabKey().equals(ACCreativeTabRegistry.MAGNETIC_CAVES.getKey())){
            event.accept(ACExBlockRegistry.METAL_CAULDRON.get());
        }
        if (event.getTabKey().equals(ACCreativeTabRegistry.TOXIC_CAVES.getKey())){
            event.accept(ACExBlockRegistry.GAMMA_NUCLEAR_BOMB.get());
        }
    }

    public static void addBlockEntityBlocks() {
        ACExBlockEntityTypeAccessor accessor = (ACExBlockEntityTypeAccessor) ACBlockEntityRegistry.AMBER_MONOLITH.get();
        Set<Block> blocks = new HashSet<>(accessor.getValidBlocks());
        blocks.add(ACExBlockRegistry.DARK_MONOLITH.get());
        accessor.setValidBlocks(blocks);
    }

    @SubscribeEvent
    public void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ACExEntityRegistry.GUMWIRM.get(), GumwirmEntity.createAttributes().build());
    }

}

