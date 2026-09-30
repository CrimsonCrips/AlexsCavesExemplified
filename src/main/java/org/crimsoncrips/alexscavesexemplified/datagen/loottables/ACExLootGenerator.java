package org.crimsoncrips.alexscavesexemplified.datagen.loottables;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ACExLootGenerator extends LootTableProvider  {
    //Props to Drull and TF for assistance//
    public ACExLootGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, ACExLootTables.allBuiltin(), List.of(
                new LootTableProvider.SubProviderEntry(ACExManualLoot::new, LootContextParamSets.PIGLIN_BARTER),
                new LootTableProvider.SubProviderEntry(ACExBlockDrops::new, LootContextParamSets.BLOCK)
        ), provider);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {

    }
}
