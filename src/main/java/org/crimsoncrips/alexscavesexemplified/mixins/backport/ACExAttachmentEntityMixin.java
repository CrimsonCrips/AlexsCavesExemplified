package org.crimsoncrips.alexscavesexemplified.mixins.backport;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.backport.AttachmentData;
import org.crimsoncrips.alexscavesexemplified.backport.IAttachmentHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ACExAttachmentEntityMixin implements IAttachmentHolder {

    @Unique
    private AttachmentData attachmentData;

    @Override
    public AttachmentData acexAttachments() {
        if (this.attachmentData == null) {
            this.attachmentData = new AttachmentData();
        }
        return this.attachmentData;
    }

    @Inject(method = "saveWithoutId", at = @At("RETURN"))
    private void alexsCavesExemplified$saveWithoutId(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        ACExData.save(this, cir.getReturnValue());
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void alexsCavesExemplified$load(CompoundTag tag, CallbackInfo ci) {
        ACExData.load(this, tag);
    }
}
