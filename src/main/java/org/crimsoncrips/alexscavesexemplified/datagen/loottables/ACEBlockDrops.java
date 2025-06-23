package org.crimsoncrips.alexscavesexemplified.datagen.loottables;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACEBlockRegistry;

import java.util.Set;
import java.util.stream.Collectors;

public class ACEBlockDrops extends BlockLootSubProvider {

	public ACEBlockDrops() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected void generate() {
		dropSelf(ACEBlockRegistry.METAL_CAULDRON.get());
		add(ACEBlockRegistry.ACID_CAULDRON.get(), createSingleItemTable(ACEBlockRegistry.METAL_CAULDRON.get()));
		add(ACEBlockRegistry.PURPLE_SODA_CAULDRON.get(), createSingleItemTable(ACEBlockRegistry.METAL_CAULDRON.get()));

		otherWhenSilkTouch(ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_1.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_2.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_3.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_4.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_5.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_6.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_7.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_8.get(),ACBlockRegistry.LIMESTONE.get());
		otherWhenSilkTouch(ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_9.get(),ACBlockRegistry.LIMESTONE.get());


	}



	@Override
	protected Iterable<Block> getKnownBlocks() {
		return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> ForgeRegistries.BLOCKS.getKey(block).getNamespace().equals(AlexsCavesExemplified.MODID)).collect(Collectors.toList());
	}
}
