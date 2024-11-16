package org.crimsoncrips.alexscavesexemplified.event;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityDataRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ACExemplifiedEvents {


    @SubscribeEvent
    public void mobSpawn(MobSpawnEvent.PositionCheck spawnPlacementCheck){
        EntityType<?> entityType = spawnPlacementCheck.getEntity().getType();
        Holder<Biome> biome = spawnPlacementCheck.getLevel().getBiome(spawnPlacementCheck.getEntity().blockPosition());
        long time = spawnPlacementCheck.getLevel().dayTime();

        String namespace = EntityType.getKey(entityType).getNamespace();

        if(!namespace.equals("alexscaves")){
            spawnPlacementCheck.setResult(Event.Result.DENY);
        }
    }

}
