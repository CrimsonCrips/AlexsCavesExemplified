package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tesla_bulb;

import com.github.alexmodguy.alexscaves.server.block.blockentity.TeslaBulbBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.client.ACExSoundRegistry;
import org.crimsoncrips.alexscavesexemplified.misc.ACExTeslaShock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(TeslaBulbBlockEntity.class)
public abstract class ACExTeslaBulbEntityMixin extends BlockEntity {

    public ACExTeslaBulbEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private static void alexsCavesExemplified$tick1(Level level, BlockPos blockPos, BlockState state, TeslaBulbBlockEntity entity, CallbackInfo ci) {
        if (ACExTeslaShock.enabled()) {
            level.playLocalSound(blockPos, ACExSoundRegistry.TESLA_FIRE.get(), SoundSource.BLOCKS, 2, 1, false);
        }
    }


    @Inject(method = "tick", at = @At(value = "HEAD"),remap = false)
    private static void alexsCavesExemplified$tick2(Level level, BlockPos blockPos, BlockState state, TeslaBulbBlockEntity entity, CallbackInfo ci) {
        if (ACExTeslaShock.enabled()) {
            ACExTeslaShock.tick(level, blockPos, entity);
        }
    }
}
