package org.crimsoncrips.alexscavesexemplified.mixins.mobs.submarine;

import com.github.alexmodguy.alexscaves.client.sound.SubmarineSound;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = SubmarineSound.class, remap = false)
public abstract class ACExSubmarineSoundMixin {

    @Shadow @Final private SubmarineEntity submarine;

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/SubmarineEntity;isVehicle()Z", remap = true), remap = true)
    private boolean alexsCavesExemplified$tick(boolean original) {
        return original || ACExSubmarineRemote.isRemoteControlled(this.submarine);
    }
}
