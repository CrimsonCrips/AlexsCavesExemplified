package org.crimsoncrips.alexscavesexemplified.datagen.advancement;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.AdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ACExAdvancementProvider extends AdvancementProvider {

	public ACExAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
		super(output, registries, existingFileHelper, List.of(new ACExAdvancements()));
	}

}