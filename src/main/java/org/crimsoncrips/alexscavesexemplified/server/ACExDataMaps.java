package org.crimsoncrips.alexscavesexemplified.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.crimsoncrips.alexscavesexemplified.backport.ACExDataMapLoader;
import org.crimsoncrips.alexscavesexemplified.backport.DataMapType;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExDataMaps {

    public static final DataMapType<Item, DepthResistance> DEPTH_RESISTANCE = DataMapType.builder(AlexsCavesExemplified.prefix("depth_resistance"), Registries.ITEM, DepthResistance.CODEC).synced(DepthResistance.CODEC, false).build();

    public static final DataMapType<Fluid, ResourceLocation> FROSTMINT_FREEZE = DataMapType.builder(AlexsCavesExemplified.prefix("frostmint_freeze"), Registries.FLUID, ResourceLocation.CODEC).build();

    public static void register() {
        ACExDataMapLoader.init();
    }

    public record DepthResistance(int depth) {
        public static final Codec<DepthResistance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("depth").forGetter(DepthResistance::depth)
        ).apply(instance, DepthResistance::new));
    }
}
