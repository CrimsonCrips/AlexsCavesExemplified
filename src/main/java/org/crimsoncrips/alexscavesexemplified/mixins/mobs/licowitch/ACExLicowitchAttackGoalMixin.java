package org.crimsoncrips.alexscavesexemplified.mixins.mobs.licowitch;

import com.github.alexmodguy.alexscaves.server.entity.ai.LicowitchAttackGoal;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LicowitchAttackGoal.class, remap = false)
public abstract class ACExLicowitchAttackGoalMixin {

    @Unique
    private static final double TRANSMUTATION_CHANCE = 0.1D;

    @WrapOperation(method = "potionAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ThrownPotion;setItem(Lnet/minecraft/world/item/ItemStack;)V"))
    private void alexsCavesExemplified$potionAttack(ThrownPotion potion, ItemStack stack, Operation<Void> original) {
        if (AlexsCavesExemplified.COMMON_CONFIG.LICOWITCH_TRANSMUTATION_ENABLED.get() && potion.getRandom().nextDouble() < TRANSMUTATION_CHANCE) {
            stack = ACEffectRegistry.createSplashPotion(ACExEffects.TRANSMUTATION_POTION.get());
        }
        original.call(potion, stack);
    }
}
