package org.crimsoncrips.alexscavesexemplified.datagen.loottables;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.crimsoncrips.alexscavesexemplified.server.blocks.PreservedAmberBlock;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;

import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.stream.Collectors;

public class ACExBlockDrops extends BlockLootSubProvider {

	public ACExBlockDrops() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected void generate() {
		dropSelf(ACExBlockRegistry.METAL_CAULDRON.get());
		dropSelf(ACExBlockRegistry.GAMMA_NUCLEAR_BOMB.get());
		dropSelf(ACExBlockRegistry.DARK_MONOLITH.get());
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

		add(ACExBlockRegistry.PRESERVED_AMBER.get(), this::preservedAmber);


	}



	private LootTable.Builder preservedAmber(Block block) {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(block).when(HAS_SILK_TOUCH).apply(CopyBlockState.copyState(block).copy(PreservedAmberBlock.CREATURE))
						.otherwise(this.applyExplosionCondition(block, LootItem.lootTableItem(ACItemRegistry.AMBER_CURIOSITY.get()).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.015F, 0.025F, 0.035F, 0.05F))
								.otherwise(LootItem.lootTableItem(ACBlockRegistry.AMBER.get()))))));
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return ACExBlockRegistry.DEF_REG.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
	}
}
