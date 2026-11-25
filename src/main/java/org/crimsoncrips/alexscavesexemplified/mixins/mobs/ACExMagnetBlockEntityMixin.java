package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.blockentity.MagnetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.misc.ACExMagnerip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MagnetBlockEntity.class)
public class ACExMagnetBlockEntityMixin extends BlockEntity {


    public ACExMagnetBlockEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "pushEntity", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$pushEntity(Entity entity, CallbackInfo ci) {
        if (entity instanceof Player player) {
            ACExMagnerip.pulled(player);
        }
    }

}
