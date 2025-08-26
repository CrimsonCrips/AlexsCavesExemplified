package org.crimsoncrips.alexscavesexemplified.datagen.loottables;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.Util;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class ACExManualLoot implements LootTableSubProvider {

    //Props to Drull and TF for assistance//
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {


        consumer.accept(ACExLootTables.GLOOMOTH_TRADE, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(5))
                        .add(LootItem.lootTableItem(ACItemRegistry.MOTH_DUST.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(80))
                        .add(LootItem.lootTableItem(ACBlockRegistry.MOTH_BALL.get().asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(80))
                        .add(LootItem.lootTableItem(ACBlockRegistry.THORNWOOD_BRANCH.get().asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))).setWeight(40))
                        .add(LootItem.lootTableItem(ACBlockRegistry.THORNWOOD_LOG.get().asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(40))
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ACItemRegistry.OCCULT_GEM.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(5))
                        .add(LootItem.lootTableItem(ACItemRegistry.FERTILIZER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))).setWeight(15))
                ));

        consumer.accept(ACExLootTables.CORRODENT_TRADE, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(5))
                        .add(LootItem.lootTableItem(ACItemRegistry.CORRODENT_TEETH.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(40))
                        .add(LootItem.lootTableItem(ACItemRegistry.FERTILIZER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(60))
                        .add(LootItem.lootTableItem(ACItemRegistry.GUANO.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))).setWeight(80))
                        .add(LootItem.lootTableItem(ACItemRegistry.BURROWING_ARROW.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(50))
                        .add(LootItem.lootTableItem(ACBlockRegistry.THORNWOOD_BRANCH.get().asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 4))).setWeight(80))
                        .add(LootItem.lootTableItem(ACBlockRegistry.THORNWOOD_LOG.get().asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(80))
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ACItemRegistry.OCCULT_GEM.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(5))
                        .add(LootItem.lootTableItem(ACItemRegistry.DESOLATE_DAGGER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(2))
                        .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "alexscaves:haste")))).setWeight(2))
                ));

        consumer.accept(ACExLootTables.VESPER_TRADE, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(5))
                        .add(LootItem.lootTableItem(ACItemRegistry.VESPER_STEW.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(40))
                        .add(LootItem.lootTableItem(ACItemRegistry.FERTILIZER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(60))
                        .add(LootItem.lootTableItem(ACBlockRegistry.GUANO_BLOCK.get().asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(80))
                        .add(LootItem.lootTableItem(ACBlockRegistry.THORNWOOD_BRANCH.get().asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 4))).setWeight(80))
                        .add(LootItem.lootTableItem(ACBlockRegistry.THORNWOOD_LOG.get().asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(80))
                        .add(LootItem.lootTableItem(ACBlockRegistry.BEHOLDER.get().asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(20))
                        .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "alexscaves:long_haste")))).setWeight(20))
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ACItemRegistry.DARKENED_APPLE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(2))
                        .add(LootItem.lootTableItem(ACItemRegistry.OCCULT_GEM.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(15))
                        .add(LootItem.lootTableItem(ACItemRegistry.DESOLATE_DAGGER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(8))
                ));

        consumer.accept(ACExLootTables.NUCLEEPER_DEFUSION, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(5))
                        .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))).setWeight(60))
                        .add(LootItem.lootTableItem(ACItemRegistry.URANIUM_SHARD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))).setWeight(80))
                        .add(LootItem.lootTableItem(ACItemRegistry.URANIUM.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(50))
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ACItemRegistry.FISSILE_CORE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(10))
                        .add(LootItem.lootTableItem(Blocks.IRON_BLOCK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).setWeight(1))

                ));

    }


}
