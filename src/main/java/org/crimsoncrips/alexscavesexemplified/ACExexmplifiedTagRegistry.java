package org.crimsoncrips.alexscavesexemplified;//



import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;


public class ACExexmplifiedTagRegistry {

    public static final TagKey<Biome> ACBiomes = registerBiomeTag("ac_biomes");

    public ACExexmplifiedTagRegistry() {
    }


    private static TagKey<Biome> registerBiomeTag(String name) {
        return TagKey.create(Registries.BIOME, new ResourceLocation("alexscavesexemplified", name));
    }


}