package org.crimsoncrips.alexscavesexemplified.mixins.backport;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExDataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ACExItemFoilMixin {

    @Inject(method = "isFoil", at = @At("HEAD"), cancellable = true)
    private void alexsCavesExemplified$isFoil(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.hasTag() && ACExDataComponents.LINKED_SUBMARINE.has(stack)) {
            cir.setReturnValue(true);
        }
    }
}
