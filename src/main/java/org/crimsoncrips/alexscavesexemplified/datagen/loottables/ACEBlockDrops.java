package org.crimsoncrips.alexscavesexemplified.datagen.loottables;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;

import java.util.Set;
import java.util.stream.Collectors;

public class ACEBlockDrops extends BlockLootSubProvider {

	public ACEBlockDrops() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected void generate() {
		dropSelf(ACExBlockRegistry.METAL_CAULDRON.get());
		dropSelf(ACExBlockRegistry.GAMMA_NUCLEAR_BOMB.get());
		add(ACExBlockRegistry.ACID_CAULDRON.get(), createSingleItemTable(ACExBlockRegistry.METAL_CAULDRON.get()));
		add(ACExBlockRegistry.PURPLE_SODA_CAULDRON.get(), createSingleItemTable(ACExBlockRegistry.METAL_CAULDRON.get()));

		otherWhenSilkTouch(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_1.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_2.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_3.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_4.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_5.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_6.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_7.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_8.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_9.get(),ACBlockRegistry.LIMESTONE.get());


	}



	@Override
	protected Iterable<Block> getKnownBlocks() {
		return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> ForgeRegistries.BLOCKS.getKey(block).getNamespace().equals(AlexsCavesExemplified.MODID)).collect(Collectors.toList());
	}
}
