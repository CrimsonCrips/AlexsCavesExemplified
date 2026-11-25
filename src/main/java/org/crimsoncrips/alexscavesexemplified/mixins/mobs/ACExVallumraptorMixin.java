package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExVallumraptorEatBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VallumraptorEntity.class)
public abstract class ACExVallumraptorMixin {

    @Shadow
    private boolean justLootedChest;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        VallumraptorEntity vallumraptor = (VallumraptorEntity) (Object) this;
        if (AlexsCavesExemplified.COMMON_CONFIG.SCAVENGING_ENABLED.get() && !this.justLootedChest && vallumraptor.getMainHandItem().is(ACTagRegistry.RAW_MEATS)) {
            ACExVallumraptorEatBlock.rollSerened(vallumraptor);
        }
    }
}
