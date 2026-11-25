package org.crimsoncrips.alexscavesexemplified.backport;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class ACEnchantmentHelper {

    public static int getEnchantmentLevelFromStack(Enchantment enchantment, ItemStack stack) {
        return stack.isEmpty() ? 0 : EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
    }

    public static int getEnchantmentLevelFromStack(Supplier<? extends Enchantment> enchantment, ItemStack stack) {
        return getEnchantmentLevelFromStack(enchantment.get(), stack);
    }

    public static int getEnchantmentLevel(Level level, Supplier<? extends Enchantment> enchantment, ItemStack stack) {
        return getEnchantmentLevelFromStack(enchantment.get(), stack);
    }

    public static boolean hasEnchantment(Level level, Supplier<? extends Enchantment> enchantment, ItemStack stack) {
        return getEnchantmentLevelFromStack(enchantment.get(), stack) > 0;
    }
}
