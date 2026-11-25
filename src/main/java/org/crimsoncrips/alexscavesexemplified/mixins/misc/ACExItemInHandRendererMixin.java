package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ACExItemInHandRendererMixin {

    @Inject(method = "itemUsed", at = @At("HEAD"), cancellable = true)
    private void alexsCavesExemplified$itemUsed(InteractionHand hand, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.getItemInHand(hand).is(ACItemRegistry.SUGAR_STAFF.get()) && AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get()) {
            ci.cancel();
        }
    }
}
