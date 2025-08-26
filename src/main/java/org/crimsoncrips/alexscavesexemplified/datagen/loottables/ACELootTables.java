package org.crimsoncrips.alexscavesexemplified.datagen.loottables;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;


import java.util.Collections;
import java.util.Set;

public class ACELootTables {
	//Props to Drull and TF for assistance//
	private static final Set<ResourceLocation> ACE_LOOT_TABLES = Sets.newHashSet();



	public static final ResourceLocation VESPER_TRADE = register("entities/vesper_trade");
	public static final ResourceLocation GLOOMOTH_TRADE = register("entities/gloomoth_trade");
	public static final ResourceLocation CORRODENT_TRADE = register("entities/corrodent_trade");

	public static final ResourceLocation NUCLEEPER_DEFUSION = register("entities/nucleeper_defusion");


	public final ResourceLocation lootTable;

	private ACELootTables(String path) {
		this.lootTable = AlexsCavesExemplified.prefix(String.format("chests/%s", path));
	}

	private static ResourceLocation register(String id) {
		return register(AlexsCavesExemplified.prefix(id));
	}

	private static ResourceLocation register(ResourceLocation id) {
		if (ACE_LOOT_TABLES.add(id)) {
			return id;
		} else {
			throw new IllegalArgumentException(id + " loot table already registered");
		}
	}

	public static Set<ResourceLocation> allBuiltin() {
		return Collections.unmodifiableSet(ACE_LOOT_TABLES);
	}


}
