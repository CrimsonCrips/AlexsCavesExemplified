package org.crimsoncrips.alexscavesexemplified.datagen.tags;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import java.util.concurrent.CompletableFuture;

public class ACEBlockTagGenerator extends IntrinsicHolderTagsProvider<Block> {
	public static final TagKey<Block> ABYSSAL_ECOSYSTEM = BlockTags.create(AlexsCavesExemplified.prefix("abyssal_ecosystem"));
	public static final TagKey<Block> BURST_BLOCKS = BlockTags.create(AlexsCavesExemplified.prefix("burst_blocks"));
	public static final TagKey<Block> CONSUMABLE_BLOCKS = BlockTags.create(AlexsCavesExemplified.prefix("consumable_blocks"));
	public static final TagKey<Block> GELATIN_FIRE = BlockTags.create(AlexsCavesExemplified.prefix("gelatin_fire"));
	public static final TagKey<Block> RADIOACTIVE = BlockTags.create(AlexsCavesExemplified.prefix("radioactive"));
	public static final TagKey<Block> DINO_SCAVENGE = BlockTags.create(AlexsCavesExemplified.prefix("dino_scavenge"));

	public ACEBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
		super(output, Registries.BLOCK, future, block -> block.builtInRegistryHolder().key(), AlexsCavesExemplified.MODID, helper);
	}

	@Override
	public String getName() {
		return "AMI Block Tags";
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		tag(ABYSSAL_ECOSYSTEM).add(
				ACBlockRegistry.GEOTHERMAL_VENT.get(),
				ACBlockRegistry.GEOTHERMAL_VENT_MEDIUM.get(),
				ACBlockRegistry.GEOTHERMAL_VENT_THIN.get(),
				ACBlockRegistry.TUBE_WORM.get(),
				ACBlockRegistry.MUSSEL.get(),
				ACBlockRegistry.DUSK_ANEMONE.get(),
				ACBlockRegistry.BONE_WORMS.get(),
				ACBlockRegistry.PING_PONG_SPONGE.get(),
				ACBlockRegistry.MIDNIGHT_ANEMONE.get(),
				ACBlockRegistry.TWILIGHT_ANEMONE.get()
		);

		tag(BURST_BLOCKS).add(
				Blocks.PACKED_MUD,
				Blocks.DIRT,
				ACBlockRegistry.GUANOSTONE.get(),
				ACBlockRegistry.COPROLITH.get(),
				ACBlockRegistry.COPROLITH_COAL_ORE.get(),
				ACBlockRegistry.GUANOSTONE_REDSTONE_ORE.get()
		);

		tag(CONSUMABLE_BLOCKS).add(
				ACBlockRegistry.BLOCK_OF_CHOCOLATE.get(),
				ACBlockRegistry.BLOCK_OF_POLISHED_CHOCOLATE.get(),
				ACBlockRegistry.BLOCK_OF_CHISELED_CHOCOLATE.get(),
				ACBlockRegistry.BLOCK_OF_FROSTED_CHOCOLATE.get(),
				ACBlockRegistry.BLOCK_OF_FROSTING.get(),
				ACBlockRegistry.BLOCK_OF_VANILLA_FROSTING.get(),
				ACBlockRegistry.BLOCK_OF_CHOCOLATE_FROSTING.get(),
				ACBlockRegistry.SWEET_PUFF.get(),
				ACBlockRegistry.CAKE_LAYER.get(),
				ACBlockRegistry.DOUGH_BLOCK.get(),
				ACBlockRegistry.COOKIE_BLOCK.get(),
				ACBlockRegistry.WAFER_COOKIE_WALL.get(),
				ACBlockRegistry.WAFER_COOKIE_BLOCK.get(),
				ACBlockRegistry.WAFER_COOKIE_SLAB.get(),
				ACBlockRegistry.WAFER_COOKIE_STAIRS.get(),
				ACBlockRegistry.LICOROOT.get(),
				ACBlockRegistry.LICOROOT_SPROUT.get(),
				ACBlockRegistry.LICOROOT_VINE.get(),
				ACBlockRegistry.SMALL_PEPPERMINT.get(),
				ACBlockRegistry.LARGE_PEPPERMINT.get(),
				ACBlockRegistry.VANILLA_ICE_CREAM.get(),
				ACBlockRegistry.CHOCOLATE_ICE_CREAM.get(),
				ACBlockRegistry.SWEETBERRY_ICE_CREAM.get(),
				ACBlockRegistry.SPRINKLES.get(),
				ACBlockRegistry.GIANT_SWEETBERRY.get(),
				ACBlockRegistry.CANDY_CANE.get(),
				ACBlockRegistry.CANDY_CANE_BLOCK.get(),
				ACBlockRegistry.CHISELED_CANDY_CANE_BLOCK.get(),
				ACBlockRegistry.STRIPPED_CANDY_CANE_POLE.get(),
				ACBlockRegistry.LOLLIPOP_BUNCH.get(),
				ACBlockRegistry.FROSTMINT.get(),
				ACBlockRegistry.SUGAR_GLASS.get(),
				ACBlockRegistry.SUNDROP.get(),
				ACBlockRegistry.GUMMY_RING_BLUE.get(),
				ACBlockRegistry.GUMMY_RING_GREEN.get(),
				ACBlockRegistry.GUMMY_RING_PINK.get(),
				ACBlockRegistry.GUMMY_RING_RED.get(),
				ACBlockRegistry.GUMMY_RING_YELLOW.get(),
				ACBlockRegistry.WHITE_ROCK_CANDY.get(),
				ACBlockRegistry.ORANGE_ROCK_CANDY.get(),
				ACBlockRegistry.MAGENTA_ROCK_CANDY.get(),
				ACBlockRegistry.LIGHT_BLUE_ROCK_CANDY.get(),
				ACBlockRegistry.YELLOW_ROCK_CANDY.get(),
				ACBlockRegistry.LIME_ROCK_CANDY.get(),
				ACBlockRegistry.PINK_ROCK_CANDY.get(),
				ACBlockRegistry.GRAY_ROCK_CANDY.get(),
				ACBlockRegistry.LIGHT_GRAY_ROCK_CANDY.get(),
				ACBlockRegistry.CYAN_ROCK_CANDY.get(),
				ACBlockRegistry.PURPLE_ROCK_CANDY.get(),
				ACBlockRegistry.BLUE_ROCK_CANDY.get(),
				ACBlockRegistry.BROWN_ROCK_CANDY.get(),
				ACBlockRegistry.GREEN_ROCK_CANDY.get(),
				ACBlockRegistry.RED_ROCK_CANDY.get(),
				ACBlockRegistry.BLACK_ROCK_CANDY.get(),
				ACBlockRegistry.GINGERBREAD_BLOCK.get(),
				ACBlockRegistry.GINGERBREAD_STAIRS.get(),
				ACBlockRegistry.GINGERBREAD_WALL.get(),
				ACBlockRegistry.GINGERBREAD_BRICKS.get(),
				ACBlockRegistry.GINGERBREAD_BRICK_STAIRS.get(),
				ACBlockRegistry.GINGERBREAD_BRICK_SLAB.get(),
				ACBlockRegistry.GINGERBREAD_BRICK_WALL.get(),
				ACBlockRegistry.GINGERBARREL.get(),
				ACBlockRegistry.FROSTED_GINGERBREAD_BLOCK.get(),
				ACBlockRegistry.FROSTED_GINGERBREAD_STAIRS.get(),
				ACBlockRegistry.FROSTED_GINGERBREAD_SLAB.get(),
				ACBlockRegistry.FROSTED_GINGERBREAD_WALL.get(),
				ACBlockRegistry.FROSTED_GINGERBREAD_DOOR.get(),
				ACBlockRegistry.FROSTED_GINGERBREAD_BRICKS.get(),
				ACBlockRegistry.FROSTED_GINGERBREAD_BRICK_STAIRS.get(),
				ACBlockRegistry.FROSTED_GINGERBREAD_BRICK_SLAB.get(),
				ACBlockRegistry.FROSTED_GINGERBREAD_BRICK_WALL.get()

		);

		tag(GELATIN_FIRE).add(
				Blocks.FIRE,
				Blocks.SOUL_CAMPFIRE,
				Blocks.SOUL_FIRE,
				Blocks.CAMPFIRE,
				Blocks.MAGMA_BLOCK
		);

		tag(DINO_SCAVENGE).add(
				ACBlockRegistry.COOKED_DINOSAUR_CHOP.get(),
				ACBlockRegistry.DINOSAUR_CHOP.get()
		)
				.addOptional(new ResourceLocation("cavedelight:roasted_dino_chop"));

		tag(RADIOACTIVE).add(
				ACBlockRegistry.RADROCK_URANIUM_ORE.get(),
				ACBlockRegistry.ACIDIC_RADROCK.get(),
				ACBlockRegistry.WASTE_DRUM.get(),
				ACBlockRegistry.UNREFINED_WASTE.get(),
				ACBlockRegistry.BLOCK_OF_URANIUM.get(),
				ACBlockRegistry.URANIUM_ROD.get()
		);

	}
}
