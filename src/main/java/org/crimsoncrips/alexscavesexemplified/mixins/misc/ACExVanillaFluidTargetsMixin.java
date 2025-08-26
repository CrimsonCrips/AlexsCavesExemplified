package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.pipes.VanillaFluidTargets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(VanillaFluidTargets.class)
public abstract class ACExVanillaFluidTargetsMixin {

    @ModifyReturnValue(method = "canProvideFluidWithoutCapability", at = @At("RETURN"),remap = false)
    private static boolean alexsMobsInteraction$canProvideFluidWithoutCapability(boolean original, @Local BlockState blockState) {
        if (blockState.is(ACExBlockRegistry.ACID_CAULDRON.get())) {
            return ACExBlockRegistry.ACID_CAULDRON.getHolder().isPresent();
        } else if (blockState.is(ACExBlockRegistry.PURPLE_SODA_CAULDRON.get())) {
            return ACExBlockRegistry.PURPLE_SODA_CAULDRON.getHolder().isPresent();
        }
        return original;
    }

    @ModifyReturnValue(method = "drainBlock", at = @At("RETURN"),remap = false)
    private static FluidStack alexsMobsInteraction$drainBlock(FluidStack original,@Local BlockPos pos, @Local BlockState state, @Local boolean simulate, @Local Level level) {
        if (state.is(ACExBlockRegistry.ACID_CAULDRON.get())) {
            if (!simulate) {
                level.setBlock(pos, ACExBlockRegistry.METAL_CAULDRON.get().defaultBlockState(), 3);
            }
            return new FluidStack(ACFluidRegistry.ACID_FLUID_SOURCE.get(), 1000);
        } else if (state.is(ACExBlockRegistry.PURPLE_SODA_CAULDRON.get())) {
            if (!simulate) {
                level.setBlock(pos, ACExBlockRegistry.METAL_CAULDRON.get().defaultBlockState(), 3);
            }
            return new FluidStack(ACFluidRegistry.PURPLE_SODA_FLUID_SOURCE.get(), 1000);
        }
        return original;
    }






}
