package org.crimsoncrips.alexscavesexemplified.mixins.misc.projector;

import com.github.alexmodguy.alexscaves.server.block.blockentity.HologramProjectorBlockEntity;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEBaseInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HologramProjectorBlockEntity.class)
public abstract class ACEHologramProjectorEntityMixin extends BlockEntity implements ACEBaseInterface {




    @Unique
    private int projectionScale = 1;

    public ACEHologramProjectorEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }



    @Inject(method = "load", at = @At(value = "TAIL"))
    private void alexsCavesExemplified$load(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("ProjectionScale")) {
            projectionScale = tag.getInt("ProjectionScale");
        }
    }

    @Inject(method = "saveAdditional", at = @At(value = "TAIL"))
    private void alexsCavesExemplified$saveAdditional(CompoundTag tag, CallbackInfo ci) {
        tag.putInt("ProjectionScale", projectionScale);
    }

    @Inject(method = "onDataPacket", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet, CallbackInfo ci) {
        if (packet != null && packet.getTag() != null && packet.getTag().contains("ProjectionScale")) {
            projectionScale = packet.getTag().getInt("ProjectionScale");
        }
    }

    @Inject(method = "getUpdateTag", at = @At(value = "RETURN"))
    private void alexsCavesExemplified$getUpdateTag(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = cir.getReturnValue();

        if (tag != null) {
            tag.putInt("ProjectionScale", projectionScale);
        }
    }


    public int getProjectionScale() {
        return projectionScale;
    }

    public void setProjectionScale(int val) {
        projectionScale = val;
    }
}