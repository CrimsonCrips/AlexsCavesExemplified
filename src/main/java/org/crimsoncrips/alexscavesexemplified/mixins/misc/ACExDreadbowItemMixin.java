package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentHelper;
import com.github.alexmodguy.alexscaves.server.entity.item.DarkArrowEntity;
import com.github.alexmodguy.alexscaves.server.item.DreadbowItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(DreadbowItem.class)
public abstract class ACExDreadbowItemMixin extends ProjectileWeaponItem {

    public ACExDreadbowItemMixin(Properties pProperties) {
        super(pProperties);
    }


    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/DarkArrowEntity;setPerfectShot(Z)V"))
    private void alexsCavesExemplified$releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i1, CallbackInfo ci, @Local AbstractArrow abstractArrow, @Local DarkArrowEntity darkArrowEntity) {
        if(AlexsCavesExemplified.COMMON_CONFIG.DREAD_ADDAPTIONS_ENABLED.get()){
            double power = ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.POWER, itemStack);
            int punch = ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.PUNCH, itemStack);
            boolean flaming = ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.FLAME, itemStack) == 1;

            darkArrowEntity.setShadowArrowDamage((float) (darkArrowEntity.getShadowArrowDamage() * (power > 0 ? power : 1)));
            if (punch > 0) {
                darkArrowEntity.getPersistentData().putInt("alexscavesexemplified:knockback", punch);
            }
            if (flaming) {
                darkArrowEntity.igniteForSeconds(100);
            }
        }
    }

    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/DarkArrowEntity;setShadowArrowDamage(F)V"))
    private void alexsCavesExemplified$onUseTick(Level level, LivingEntity living, ItemStack itemStack, int timeUsing, CallbackInfo ci, @Local AbstractArrow abstractArrow, @Local DarkArrowEntity darkArrowEntity) {
        if(AlexsCavesExemplified.COMMON_CONFIG.DREAD_ADDAPTIONS_ENABLED.get()) {
            int punch = ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.PUNCH, itemStack);
            boolean flaming = ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.FLAME, itemStack) == 1;

            if (punch > 0) {
                darkArrowEntity.getPersistentData().putInt("alexscavesexemplified:knockback", punch);
            }
            if (flaming) {
                darkArrowEntity.igniteForSeconds(100);
            }
        }
    }

    @WrapWithCondition(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private boolean alexsCavesExemplified$onUseTick1(ItemStack instance, int pDecrement,@Local(ordinal = 0) ItemStack bow) {
        return !(ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.INFINITY, bow) == 1);
    }



    @ModifyArg(method = "onUseTick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/DarkArrowEntity;setShadowArrowDamage(F)V"))
    private float alexsCavesExemplified$1(float f,@Local (ordinal = 0) ItemStack itemStack) {
        if(AlexsCavesExemplified.COMMON_CONFIG.DREAD_ADDAPTIONS_ENABLED.get()) {
            double power = ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.POWER, itemStack);
            return (float) (f * (power > 0 ? power : 1));
        }
        return f;
    }

    @ModifyConstant(method = "onUseTick",constant = @Constant(intValue = 3))
    private int alexsCavesExemplified$tick3(int constant) {
        return (AlexsCavesExemplified.COMMON_CONFIG.RATATATATATA_ENABLED.get()) ? 1 : constant;
    }

    @ModifyReturnValue(method = "getMaxLoadTime", at = @At("RETURN"))
    private static int alexsCavesExemplified$getMaxLoadTime(int original) {
        return AlexsCavesExemplified.COMMON_CONFIG.RATATATATATA_ENABLED.get() ? 1 : original;
    }

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        if (AlexsCavesExemplified.COMMON_CONFIG.DREAD_ADDAPTIONS_ENABLED.get() && (enchantment.is(Enchantments.POWER) || enchantment.is(Enchantments.FLAME) || enchantment.is(Enchantments.PUNCH))) {
            return true;
        }
        return super.isPrimaryItemFor(stack, enchantment);
    }

}