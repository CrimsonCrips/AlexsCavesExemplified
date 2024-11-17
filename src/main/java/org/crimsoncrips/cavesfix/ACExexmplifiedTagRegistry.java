package org.crimsoncrips.cavesfix;//



import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;


public class ACExexmplifiedTagRegistry {

    public static final TagKey<Biome> ACBiomes = registerBiomeTag("ac_biomes");

    public ACExexmplifiedTagRegistry() {
    }


    private static TagKey<Biome> registerBiomeTag(String name) {
        return TagKey.create(Registries.BIOME, new ResourceLocation("cavesfix", name));
    }


}