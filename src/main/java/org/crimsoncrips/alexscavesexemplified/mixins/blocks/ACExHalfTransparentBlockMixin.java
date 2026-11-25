package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HalfTransparentBlock.class)
public abstract class ACExHalfTransparentBlockMixin {

    @Inject(method = "skipRendering", at = @At("HEAD"), cancellable = true)
    private void alexsCavesExemplified$skipRendering(BlockState state, BlockState adjacent, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (adjacent.is(ACExBlockRegistry.PRESERVED_AMBER.get()) && state.is(ACBlockRegistry.AMBER.get())) {
            cir.setReturnValue(true);
        }
    }
}
