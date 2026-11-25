package org.crimsoncrips.alexscavesexemplified.server.enchantment;

import com.github.alexmodguy.alexscaves.server.item.GalenaGauntletItem;
import com.github.alexmodguy.alexscaves.server.item.ResistorShieldItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExEnchants {

    public static final DeferredRegister<Enchantment> DEF_REG = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, AlexsCavesExemplified.MODID);

    public static final EnchantmentCategory MAGNETICISM_CATEGORY = EnchantmentCategory.create("magneticism_category", item -> item instanceof GalenaGauntletItem || item instanceof ResistorShieldItem);

    public static final RegistryObject<Enchantment> MAGNETICISM = DEF_REG.register("magneticism", () -> new ACExMagneticism(Enchantment.Rarity.RARE, MAGNETICISM_CATEGORY, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));

    public static int getLevel(Level level, ItemStack stack, RegistryObject<Enchantment> enchantment) {
        if (stack.isEmpty() || !enchantment.isPresent()) {
            return 0;
        }
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment.get(), stack);
    }
}
