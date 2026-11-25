package org.crimsoncrips.alexscavesexemplified.server.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record ChopPileConfiguration(IntProvider count, int spread, float boneChance, int maxBites) implements FeatureConfiguration {
    public static final Codec<ChopPileConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            IntProvider.codec(1, 64).fieldOf("count").forGetter(ChopPileConfiguration::count),
            Codec.intRange(0, 8).fieldOf("spread").forGetter(ChopPileConfiguration::spread),
            Codec.floatRange(0.0F, 1.0F).fieldOf("bone_chance").forGetter(ChopPileConfiguration::boneChance),
            Codec.intRange(0, 3).fieldOf("max_bites").forGetter(ChopPileConfiguration::maxBites)
    ).apply(instance, ChopPileConfiguration::new));
}
