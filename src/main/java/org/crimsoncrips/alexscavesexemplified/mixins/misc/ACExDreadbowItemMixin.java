package org.crimsoncrips.alexscavesexemplified.mixins.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.backport.ACEnchantmentHelper;
import com.github.alexmodguy.alexscaves.server.entity.item.DarkArrowEntity;
import com.github.alexmodguy.alexscaves.server.item.DreadbowItem;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(DreadbowItem.class)
public abstract class ACExDreadbowItemMixin extends ProjectileWeaponItem {

    @Unique
    private static final double POWER_BONUS = 0.25D;

    public ACExDreadbowItemMixin(Properties pProperties) {
        super(pProperties);
    }


    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/DarkArrowEntity;setPerfectShot(Z)V"))
    private void alexsCavesExemplified$releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i1, CallbackInfo ci, @Local AbstractArrow abstractArrow, @Local DarkArrowEntity darkArrowEntity) {
        if(AlexsCavesExemplified.COMMON_CONFIG.DREAD_ADDAPTIONS_ENABLED.get()){
            double power = ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.POWER_ARROWS, itemStack);
            int punch = ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.PUNCH_ARROWS, itemStack);
            boolean flaming = ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.FLAMING_ARROWS, itemStack) == 1;

            darkArrowEntity.setShadowArrowDamage((float) (darkArrowEntity.getShadowArrowDamage() * (1.0D + POWER_BONUS * power)));
            if (punch > 0) {
                ACExData.set(darkArrowEntity, ACExAttachments.DARK_ARROW_KNOCKBACK, punch);
            }
            if (flaming) {
                darkArrowEntity.setSecondsOnFire(100);
            }
        }
    }

    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/DarkArrowEntity;setShadowArrowDamage(F)V"))
    private void alexsCavesExemplified$onUseTick(Level level, LivingEntity living, ItemStack itemStack, int timeUsing, CallbackInfo ci, @Local AbstractArrow abstractArrow, @Local DarkArrowEntity darkArrowEntity) {
        if(AlexsCavesExemplified.COMMON_CONFIG.DREAD_ADDAPTIONS_ENABLED.get()) {
            int punch = ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.PUNCH_ARROWS, itemStack);
            boolean flaming = ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.FLAMING_ARROWS, itemStack) == 1;

            if (punch > 0) {
                ACExData.set(darkArrowEntity, ACExAttachments.DARK_ARROW_KNOCKBACK, punch);
            }
            if (flaming) {
                darkArrowEntity.setSecondsOnFire(100);
            }
        }
    }

    @WrapWithCondition(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private boolean alexsCavesExemplified$onUseTick1(ItemStack instance, int pDecrement,@Local(ordinal = 0) ItemStack bow) {
        return !AlexsCavesExemplified.COMMON_CONFIG.DREAD_ADDAPTIONS_ENABLED.get() || ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.INFINITY_ARROWS, bow) != 1;
    }



    @ModifyArg(method = "onUseTick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/DarkArrowEntity;setShadowArrowDamage(F)V"))
    private float alexsCavesExemplified$1(float f,@Local (ordinal = 0) ItemStack itemStack) {
        if(AlexsCavesExemplified.COMMON_CONFIG.DREAD_ADDAPTIONS_ENABLED.get()) {
            double power = ACEnchantmentHelper.getEnchantmentLevelFromStack(Enchantments.POWER_ARROWS, itemStack);
            return (float) (f * (1.0D + POWER_BONUS * power));
        }
        return f;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (AlexsCavesExemplified.COMMON_CONFIG.DREAD_ADDAPTIONS_ENABLED.get() && (enchantment == Enchantments.POWER_ARROWS || enchantment == Enchantments.FLAMING_ARROWS || enchantment == Enchantments.PUNCH_ARROWS)) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

}