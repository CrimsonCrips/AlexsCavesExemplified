package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.blockentity.HologramProjectorBlockEntity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.ACEFeatures;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEBaseInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(GrassBlock.class)
public abstract class ACEGrassBlock extends SpreadingSnowyDirtBlock implements BonemealableBlock {


    protected ACEGrassBlock(Properties pProperties) {
        super(pProperties);
    }


    @ModifyVariable(method = "performBonemeal", at = @At(value = "STORE"))
    private Optional<Holder.Reference<PlacedFeature>> alexsMobsInteraction$renderAt2(Optional<Holder.Reference<PlacedFeature>> value, @Local ServerLevel level,@Local(ordinal = 0) BlockPos blockPos) {
        if (level.getBiome(blockPos).is(ACBiomeRegistry.PRIMORDIAL_CAVES) && AlexsCavesExemplified.COMMON_CONFIG.CAVIAL_BONEMEAL_ENABLED.get()){
            return level.registryAccess().registryOrThrow(Registries.PLACED_FEATURE).getHolder(ACEFeatures.PLACED_PRIMORDIAL_BONEMEAL);
        }
        return value;
    }
}