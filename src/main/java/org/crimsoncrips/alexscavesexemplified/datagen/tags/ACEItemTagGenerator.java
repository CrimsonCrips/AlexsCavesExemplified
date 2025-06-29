package org.crimsoncrips.alexscavesexemplified.datagen.tags;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
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
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.compat.CreateCompat;
import org.crimsoncrips.alexscavesexemplified.server.item.ACEItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ACEItemTagGenerator extends ItemTagsProvider {
	public static final TagKey<Item> COLD_FOOD = create("cold_food");
	public static final TagKey<Item> GELATINABLE = create("gelatinable");
	public static final TagKey<Item> KNAWING = create("knawing");
	public static final TagKey<Item> LIGHT = create("light");
	public static final TagKey<Item> SWEETS = create("sweets");
	public static final TagKey<Item> GELATIN = create("gelatin");

    public ACEItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> provider, ExistingFileHelper helper) {
        super(output, future, provider, AlexsCavesExemplified.MODID, helper);
    }

	@SuppressWarnings("unchecked")
    @Override
	protected void addTags(HolderLookup.Provider provider) {

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
				ACEItemRegistry.ICE_CREAM_CONE.get()
		);

		tag(GELATINABLE).add(
				Items.BONE_BLOCK,
				Items.BONE,
				ACItemRegistry.HEAVY_BONE.get(),
				ACBlockRegistry.SMOOTH_BONE.get().asItem(),
				ACBlockRegistry.SMOOTH_BONE_STAIRS.get().asItem(),
				ACBlockRegistry.SMOOTH_BONE_SLAB.get().asItem(),
				ACBlockRegistry.SMOOTH_BONE_WALL.get().asItem(),
				ACBlockRegistry.HOLLOW_BONE.get().asItem(),
				ACBlockRegistry.THIN_BONE.get().asItem(),
				ACBlockRegistry.BONE_NODULE.get().asItem(),
				ACBlockRegistry.BONE_RIBS.get().asItem(),
				ACBlockRegistry.BALEEN_BONE.get().asItem()
		)
				.addOptional(new ResourceLocation("alexsmobs:fish_bones"))
				.addOptional(new ResourceLocation("alexsmobs:skelewag_sword"));

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

		tag(SWEETS).add(
				Items.HONEY_BOTTLE,
				ACItemRegistry.BIOME_TREAT.get(),
				ACItemRegistry.CARAMEL.get(),
				ACItemRegistry.PURPLE_SODA_BOTTLE.get(),
				ACItemRegistry.SWEETISH_FISH_BLUE.get(),
				ACItemRegistry.SWEETISH_FISH_GREEN.get(),
				ACItemRegistry.SWEETISH_FISH_PINK.get(),
				ACItemRegistry.SWEETISH_FISH_RED.get(),
				ACItemRegistry.SWEETISH_FISH_YELLOW.get(),
				ACItemRegistry.GELATIN_RED.get(),
				ACItemRegistry.GELATIN_YELLOW.get(),
				ACItemRegistry.GELATIN_PINK.get(),
				ACItemRegistry.GELATIN_GREEN.get(),
				ACItemRegistry.GELATIN_BLUE.get(),
				ACItemRegistry.SWEETISH_FISH_BLUE.get(),
				ACItemRegistry.PEPPERMINT_POWDER.get(),
				ACItemRegistry.GUMBALL_PILE.get(),
				ACItemRegistry.SHARPENED_CANDY_CANE.get(),
				ACItemRegistry.HOT_CHOCOLATE_BOTTLE.get(),
				ACItemRegistry.CARAMEL_APPLE.get(),
				ACItemRegistry.GINGERBREAD_CRUMBS.get(),
				ACItemRegistry.JELLY_BEAN.get(),
				ACBlockRegistry.BLOCK_OF_CHOCOLATE.get().asItem(),
				ACBlockRegistry.BLOCK_OF_POLISHED_CHOCOLATE.get().asItem(),
				ACBlockRegistry.BLOCK_OF_CHISELED_CHOCOLATE.get().asItem(),
				ACBlockRegistry.BLOCK_OF_FROSTED_CHOCOLATE.get().asItem(),
				ACBlockRegistry.BLOCK_OF_FROSTING.get().asItem(),
				ACBlockRegistry.BLOCK_OF_VANILLA_FROSTING.get().asItem(),
				ACBlockRegistry.BLOCK_OF_CHOCOLATE_FROSTING.get().asItem(),
				ACBlockRegistry.SWEET_PUFF.get().asItem(),
				ACBlockRegistry.CAKE_LAYER.get().asItem(),
				ACBlockRegistry.DOUGH_BLOCK.get().asItem(),
				ACBlockRegistry.COOKIE_BLOCK.get().asItem(),
				ACBlockRegistry.WAFER_COOKIE_WALL.get().asItem(),
				ACBlockRegistry.WAFER_COOKIE_BLOCK.get().asItem(),
				ACBlockRegistry.WAFER_COOKIE_SLAB.get().asItem(),
				ACBlockRegistry.WAFER_COOKIE_STAIRS.get().asItem(),
				ACBlockRegistry.LICOROOT.get().asItem(),
				ACBlockRegistry.LICOROOT_SPROUT.get().asItem(),
				ACBlockRegistry.LICOROOT_VINE.get().asItem(),
				ACBlockRegistry.SMALL_PEPPERMINT.get().asItem(),
				ACBlockRegistry.LARGE_PEPPERMINT.get().asItem(),
				ACBlockRegistry.VANILLA_ICE_CREAM.get().asItem(),
				ACBlockRegistry.CHOCOLATE_ICE_CREAM.get().asItem(),
				ACBlockRegistry.SWEETBERRY_ICE_CREAM.get().asItem(),
				ACBlockRegistry.SPRINKLES.get().asItem(),
				ACBlockRegistry.GIANT_SWEETBERRY.get().asItem(),
				ACBlockRegistry.CANDY_CANE.get().asItem(),
				ACBlockRegistry.CANDY_CANE_BLOCK.get().asItem(),
				ACBlockRegistry.CHISELED_CANDY_CANE_BLOCK.get().asItem(),
				ACBlockRegistry.STRIPPED_CANDY_CANE_POLE.get().asItem(),
				ACBlockRegistry.LOLLIPOP_BUNCH.get().asItem(),
				ACBlockRegistry.FROSTMINT.get().asItem(),
				ACBlockRegistry.SUGAR_GLASS.get().asItem(),
				ACBlockRegistry.SUNDROP.get().asItem(),
				ACBlockRegistry.GUMMY_RING_BLUE.get().asItem(),
				ACBlockRegistry.GUMMY_RING_GREEN.get().asItem(),
				ACBlockRegistry.GUMMY_RING_PINK.get().asItem(),
				ACBlockRegistry.GUMMY_RING_RED.get().asItem(),
				ACBlockRegistry.GUMMY_RING_YELLOW.get().asItem(),
				ACBlockRegistry.WHITE_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.ORANGE_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.MAGENTA_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.LIGHT_BLUE_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.YELLOW_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.LIME_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.PINK_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.GRAY_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.LIGHT_GRAY_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.CYAN_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.PURPLE_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.BLUE_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.BROWN_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.GREEN_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.RED_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.BLACK_ROCK_CANDY.get().asItem(),
				ACBlockRegistry.GINGERBREAD_BLOCK.get().asItem(),
				ACBlockRegistry.GINGERBREAD_STAIRS.get().asItem(),
				ACBlockRegistry.GINGERBREAD_WALL.get().asItem(),
				ACBlockRegistry.GINGERBREAD_BRICKS.get().asItem(),
				ACBlockRegistry.GINGERBREAD_BRICK_STAIRS.get().asItem(),
				ACBlockRegistry.GINGERBREAD_BRICK_SLAB.get().asItem(),
				ACBlockRegistry.GINGERBREAD_BRICK_WALL.get().asItem(),
				ACBlockRegistry.GINGERBARREL.get().asItem(),
				ACBlockRegistry.FROSTED_GINGERBREAD_BLOCK.get().asItem(),
				ACBlockRegistry.FROSTED_GINGERBREAD_STAIRS.get().asItem(),
				ACBlockRegistry.FROSTED_GINGERBREAD_SLAB.get().asItem(),
				ACBlockRegistry.FROSTED_GINGERBREAD_WALL.get().asItem(),
				ACBlockRegistry.FROSTED_GINGERBREAD_DOOR.get().asItem(),
				ACBlockRegistry.FROSTED_GINGERBREAD_BRICKS.get().asItem(),
				ACBlockRegistry.FROSTED_GINGERBREAD_BRICK_STAIRS.get().asItem(),
				ACBlockRegistry.FROSTED_GINGERBREAD_BRICK_SLAB.get().asItem(),
				ACBlockRegistry.FROSTED_GINGERBREAD_BRICK_WALL.get().asItem()
		).addTag(COLD_FOOD);
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
