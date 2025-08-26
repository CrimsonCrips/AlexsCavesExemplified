package org.crimsoncrips.alexscavesexemplified.datagen.loottables;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;

public class ACExLootGenerator extends LootTableProvider  {
    //Props to Drull and TF for assistance//
    public ACExLootGenerator(PackOutput output) {
        super(output, ACExLootTables.allBuiltin(), List.of(
                new LootTableProvider.SubProviderEntry(ACExManualLoot::new, LootContextParamSets.PIGLIN_BARTER),
                new LootTableProvider.SubProviderEntry(ACExBlockDrops::new, LootContextParamSets.BLOCK)
        ));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationcontext) {

    }
}
