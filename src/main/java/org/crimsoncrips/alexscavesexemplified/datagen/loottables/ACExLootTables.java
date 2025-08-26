package org.crimsoncrips.alexscavesexemplified.datagen.loottables;

import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;


import java.util.Collections;
import java.util.Set;

public class ACExLootTables {
	//Props to Drull and TF for assistance//
	private static final Set<ResourceLocation> ACE_LOOT_TABLES = Sets.newHashSet();



	public static final ResourceLocation VESPER_TRADE = register("entities/vesper_trade");
	public static final ResourceLocation GLOOMOTH_TRADE = register("entities/gloomoth_trade");
	public static final ResourceLocation CORRODENT_TRADE = register("entities/corrodent_trade");

	public static final ResourceLocation NUCLEEPER_DEFUSION = register("entities/nucleeper_defusion");


	public final ResourceLocation lootTable;

	private ACExLootTables(String path) {
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
