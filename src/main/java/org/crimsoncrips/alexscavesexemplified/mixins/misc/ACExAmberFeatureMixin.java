package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.level.feature.AmbersolFeature;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.blocks.PreservedAmberBlock;
import org.crimsoncrips.alexscavesexemplified.server.blocks.PreservedCreature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(AmbersolFeature.class)
public abstract class ACExAmberFeatureMixin extends Feature<NoneFeatureConfiguration> {

    public ACExAmberFeatureMixin(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }
    //Massive Props to Drullkus for assistance


    @ModifyArg(method = "drawOrb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), index = 1)
    private static BlockState alexsCavesExemplified$drawOrb(BlockState state, @Local(argsOnly = true) RandomSource random) {
        if (!state.is(ACBlockRegistry.AMBER.get()) || !AlexsCavesExemplified.COMMON_CONFIG.PRESERVED_AMBER_ENABLED.get() || random.nextDouble() >= 0.02)
            return state;
        PreservedCreature creature = PreservedCreature.random(random);
        if (creature == null)
            return state;
        return ACExBlockRegistry.PRESERVED_AMBER.get().defaultBlockState().setValue(PreservedAmberBlock.CREATURE, creature).setValue(PreservedAmberBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
    }
}
