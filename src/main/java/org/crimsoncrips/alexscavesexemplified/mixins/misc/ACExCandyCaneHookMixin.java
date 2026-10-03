package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.CandyCaneHookEntity;
import com.github.alexmodguy.alexscaves.server.item.CandyCaneHookItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.server.entity.GumwirmEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CandyCaneHookEntity.class)
public abstract class ACExCandyCaneHookMixin {

    @Shadow public abstract Entity getHookedEntity();

    @Shadow public abstract Entity getOwner();

    @Shadow public abstract InteractionHand getHandLaunchedFrom();

    @Inject(method = "tick", at = @At("HEAD"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (((Entity) (Object) this).level().isClientSide || !(this.getHookedEntity() instanceof GumwirmEntity))
            return;
        if (this.getOwner() instanceof Player player) {
            ItemStack stack = player.getItemInHand(this.getHandLaunchedFrom());
            if (stack.getItem() instanceof CandyCaneHookItem) {
                CandyCaneHookItem.setReelingIn(stack, true);
            }
        }
    }
}
