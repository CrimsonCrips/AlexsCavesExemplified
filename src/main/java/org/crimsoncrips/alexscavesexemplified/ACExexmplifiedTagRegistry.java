package org.crimsoncrips.alexscavesexemplified;//



import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;


public class ACExexmplifiedTagRegistry {
    public static final TagKey<Block> BURST_BLOCKS = registerBlockTag("burst_blocks");
    public static final TagKey<Block> CONSUMABLE_BLOCKS = registerBlockTag("consumable_blocks");
    public static final TagKey<Item> LIGHT = registerItemTag("light");


    public ACExexmplifiedTagRegistry() {
    }

    private static TagKey<Item> registerItemTag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("alexscavesexemplified", name));
    }
    private static TagKey<EntityType<?>> registerEntityTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("alexscavesexemplified", name));
    }

    private static TagKey<Block> registerBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation("alexscavesexemplified", name));
    }


}