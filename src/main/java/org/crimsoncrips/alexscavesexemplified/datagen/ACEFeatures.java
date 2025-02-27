package org.crimsoncrips.alexscavesexemplified.datagen;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import java.util.List;

public class ACEFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> PRIMORDIAL_BONEMEAL = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(AlexsCavesExemplified.MODID, "primordial_bonemeal"));
    public static final ResourceKey<PlacedFeature> PLACED_PRIMORDIAL_BONEMEAL = ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(AlexsCavesExemplified.MODID, "placed_primordial_bonemeal"));


    static void generateFeatureConfigurations(BootstapContext<ConfiguredFeature<?, ?>> context) {
        context.register(PRIMORDIAL_BONEMEAL, new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(1, 3, 3, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                .add(Blocks.GRASS.defaultBlockState(), 10)
                .add(Blocks.FERN.defaultBlockState(), 5)
                .add(Blocks.LARGE_FERN.defaultBlockState(), 3)
                .add(ACBlockRegistry.FLYTRAP.get().defaultBlockState(), 4)
                .add(ACBlockRegistry.CURLY_FERN.get().defaultBlockState(), 8)
                .add(ACBlockRegistry.FIDDLEHEAD.get().defaultBlockState(), 4)
                .add(ACBlockRegistry.CYCAD.get().defaultBlockState(), 1)
        )), BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE)))));
    }

    static void generateFeaturePlacements(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> featureConfigLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(PLACED_PRIMORDIAL_BONEMEAL, new PlacedFeature(featureConfigLookup.getOrThrow(PRIMORDIAL_BONEMEAL), List.of()));
    }

}
