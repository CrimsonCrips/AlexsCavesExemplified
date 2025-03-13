package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.DarkArrowEntity;
import com.github.alexmodguy.alexscaves.server.item.DreadbowItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.alexmodguy.alexscaves.server.item.DreadbowItem.*;


@Mixin(DreadbowItem.class)
public abstract class ACEDreadbowItem extends ProjectileWeaponItem {

    public ACEDreadbowItem(Properties pProperties) {
        super(pProperties);
    }


    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/DarkArrowEntity;setPerfectShot(Z)V"))
    private void alexsCavesExemplified$releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i1, CallbackInfo ci, @Local AbstractArrow abstractArrow, @Local DarkArrowEntity darkArrowEntity) {
        double power = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack);
        int punch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, itemStack);
        boolean flaming = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack) == 1;

        darkArrowEntity.setShadowArrowDamage((float) (darkArrowEntity.getShadowArrowDamage() * (power > 0 ? power : 1)));
        darkArrowEntity.setKnockback(punch > 0 ? punch : darkArrowEntity.getKnockback());
        if (flaming) {
            darkArrowEntity.setSecondsOnFire(100);
        }
    }

    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/DarkArrowEntity;setShadowArrowDamage(F)V"))
    private void alexsCavesExemplified$onUseTick(Level level, LivingEntity living, ItemStack itemStack, int timeUsing, CallbackInfo ci, @Local AbstractArrow abstractArrow, @Local DarkArrowEntity darkArrowEntity) {
        int punch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, itemStack);
        boolean flaming = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack) == 1;

        darkArrowEntity.setKnockback(punch > 0 ? punch : darkArrowEntity.getKnockback());
        if (flaming) {
            darkArrowEntity.setSecondsOnFire(100);
        }
    }



    @ModifyArg(method = "onUseTick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/DarkArrowEntity;setShadowArrowDamage(F)V"))
    private float alexsCavesExemplified$1(float f,@Local (ordinal = 0) ItemStack itemStack) {
        double power = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack);
        return (float) (f * (power > 0 ? power : 1));
    }

    @ModifyConstant(method = "onUseTick",constant = @Constant(intValue = 3))
    private int alexsCavesExemplified$tick3(int constant) {
        return (AlexsCavesExemplified.COMMON_CONFIG.RATATATATATA_ENABLED.get()) ? 1 : constant;
    }

    @ModifyReturnValue(method = "getMaxLoadTime", at = @At("RETURN"),remap = false)
    private static int getMaxLoadTime(int original) {
        return AlexsCavesExemplified.COMMON_CONFIG.RATATATATATA_ENABLED.get() ? 1 : original;
    }

}