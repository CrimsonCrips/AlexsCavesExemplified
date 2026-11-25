package org.crimsoncrips.alexscavesexemplified.datagen.tags;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ACExItemTagGenerator extends ItemTagsProvider {
	public static final TagKey<Item> COLD_FOOD = create("cold_food");
	public static final TagKey<Item> KNAWING = create("knawing");
	public static final TagKey<Item> LIGHT = create("light");
	public static final TagKey<Item> GELATIN = create("gelatin");
	public static final TagKey<Item> MAGNETICISM_ENCHANTABLE = create("magneticism_enchantable");

    public ACExItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> provider, ExistingFileHelper helper) {
        super(output, future, provider, AlexsCavesExemplified.MODID, helper);
    }

	@SuppressWarnings("unchecked")
    @Override
	protected void addTags(HolderLookup.Provider provider) {

		tag(MAGNETICISM_ENCHANTABLE).add(
				ACItemRegistry.GALENA_GAUNTLET.get(),
				ACItemRegistry.RESISTOR_SHIELD.get()
		);

		tag(GELATIN).add(
				ACItemRegistry.GELATIN_BLUE.get(),
				ACItemRegistry.GELATIN_GREEN.get(),
				ACItemRegistry.GELATIN_RED.get(),
				ACItemRegistry.GELATIN_PINK.get(),
				ACItemRegistry.GELATIN_YELLOW.get()
		);

		tag(COLD_FOOD).add(
				ACBlockRegistry.CHOCOLATE_ICE_CREAM.get().asItem(),
				ACBlockRegistry.SWEETBERRY_ICE_CREAM.get().asItem(),
				ACBlockRegistry.VANILLA_ICE_CREAM.get().asItem(),
				ACBlockRegistry.FROSTMINT.get().asItem(),
				ACExItemRegistry.ICE_CREAM_CONE.get()
		);

		tag(KNAWING).add(
				Items.LEATHER,
				Items.LEATHER_BOOTS,
				Items.LEATHER_CHESTPLATE,
				Items.LEATHER_LEGGINGS,
				Items.LEATHER_HORSE_ARMOR,
				Items.LEATHER_HELMET,
				Items.BONE,
				Items.BONE_BLOCK,
				Items.BOOK,
				Items.WRITABLE_BOOK,
				Items.WRITTEN_BOOK,
				Items.BOOKSHELF,
				Items.CHISELED_BOOKSHELF,
				Items.BRICK,
				Items.BRICKS,
				Items.BRICK_SLAB,
				Items.BRICK_STAIRS,
				Items.BRICK_WALL,
				Items.HAY_BLOCK,
				Items.PAPER,
				Items.PUMPKIN,
				Items.BAMBOO
		).addTags(ItemTags.PLANKS,
                ItemTags.WOODEN_TRAPDOORS,
                ItemTags.WOOL_CARPETS,
                ItemTags.WOOL,
                ItemTags.BANNERS,
                ItemTags.CANDLES,
                ItemTags.HANGING_SIGNS,
                ItemTags.LOGS,
                ItemTags.SAPLINGS,
                ItemTags.SIGNS,
                ItemTags.LEAVES,
                ItemTags.BAMBOO_BLOCKS,
                ItemTags.SMALL_FLOWERS
        )
				.addOptional(new ResourceLocation("create:fluid_pipe"))
				.addOptional(new ResourceLocation("create:smart_fluid_pipe"));


		tag(LIGHT).add(
				Items.SHROOMLIGHT,
				Items.JACK_O_LANTERN,
				Items.LANTERN,
				Items.OCHRE_FROGLIGHT,
				Items.VERDANT_FROGLIGHT,
				Items.PEARLESCENT_FROGLIGHT,
				Items.SEA_LANTERN,
				Items.GLOWSTONE,
				Items.SOUL_LANTERN,
				Items.TORCH,
				Items.SOUL_TORCH
		);

	}

	@Override
	public String getName() {
		return "ACE Item Tags";
	}


	public static TagKey<Item> create(String tagName) {
		return ItemTags.create(AlexsCavesExemplified.prefix(tagName));
	}

	public static TagKey<Item> makeForgeTag(String tagName) {
		return ItemTags.create(new ResourceLocation("forge", tagName));
	}
}
