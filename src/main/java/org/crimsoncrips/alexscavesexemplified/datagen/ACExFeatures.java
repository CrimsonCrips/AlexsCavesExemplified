package org.crimsoncrips.alexscavesexemplified.datagen;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.world.ACExFeatureRegistry;
import org.crimsoncrips.alexscavesexemplified.server.world.ChopPileConfiguration;

import java.util.List;

public class ACExFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> PRIMORDIAL_BONEMEAL = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(AlexsCavesExemplified.MODID, "primordial_bonemeal"));
    public static final ResourceKey<PlacedFeature> PLACED_PRIMORDIAL_BONEMEAL = ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(AlexsCavesExemplified.MODID, "placed_primordial_bonemeal"));

    public static final ResourceKey<ConfiguredFeature<?, ?>> FRESH_CHOP_PILE = configured("fresh_chop_pile");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GNAWED_CHOP_PILE = configured("gnawed_chop_pile");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SCAVENGED_CHOP_PILE = configured("scavenged_chop_pile");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BONE_PILE = configured("bone_pile");
    public static final ResourceKey<PlacedFeature> PLACED_FRESH_CHOP_PILE = placed("fresh_chop_pile");
    public static final ResourceKey<PlacedFeature> PLACED_GNAWED_CHOP_PILE = placed("gnawed_chop_pile");
    public static final ResourceKey<PlacedFeature> PLACED_SCAVENGED_CHOP_PILE = placed("scavenged_chop_pile");
    public static final ResourceKey<PlacedFeature> PLACED_BONE_PILE = placed("bone_pile");

    private static ResourceKey<ConfiguredFeature<?, ?>> configured(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(AlexsCavesExemplified.MODID, name));
    }

    private static ResourceKey<PlacedFeature> placed(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(AlexsCavesExemplified.MODID, name));
    }

    private static ConfiguredFeature<?, ?> chopPile(int minCount, int maxCount, int spread, float boneChance, int maxBites) {
        return new ConfiguredFeature<>(ACExFeatureRegistry.CHOP_PILE.get(), new ChopPileConfiguration(UniformInt.of(minCount, maxCount), spread, boneChance, maxBites));
    }

    private static List<PlacementModifier> pilePlacement(int rarity) {
        return List.of(
                CountPlacement.of(6),
                RarityFilter.onAverageOnceEvery(rarity),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)),
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.CAVE_AIR), 32),
                RandomOffsetPlacement.vertical(ConstantInt.of(1)),
                SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -5),
                BiomeFilter.biome());
    }


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
        context.register(FRESH_CHOP_PILE, chopPile(3, 5, 2, 0.0F, 0));
        context.register(GNAWED_CHOP_PILE, chopPile(3, 6, 2, 0.0F, 3));
        context.register(SCAVENGED_CHOP_PILE, chopPile(4, 7, 2, 0.5F, 3));
        context.register(BONE_PILE, chopPile(5, 8, 3, 0.85F, 3));
    }

    static void generateFeaturePlacements(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> featureConfigLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(PLACED_PRIMORDIAL_BONEMEAL, new PlacedFeature(featureConfigLookup.getOrThrow(PRIMORDIAL_BONEMEAL), List.of()));
        context.register(PLACED_FRESH_CHOP_PILE, new PlacedFeature(featureConfigLookup.getOrThrow(FRESH_CHOP_PILE), pilePlacement(32)));
        context.register(PLACED_GNAWED_CHOP_PILE, new PlacedFeature(featureConfigLookup.getOrThrow(GNAWED_CHOP_PILE), pilePlacement(32)));
        context.register(PLACED_SCAVENGED_CHOP_PILE, new PlacedFeature(featureConfigLookup.getOrThrow(SCAVENGED_CHOP_PILE), pilePlacement(32)));
        context.register(PLACED_BONE_PILE, new PlacedFeature(featureConfigLookup.getOrThrow(BONE_PILE), pilePlacement(40)));
    }

}
