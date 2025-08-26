package org.crimsoncrips.alexscavesexemplified.server.enchantment;


import com.github.alexmodguy.alexscaves.server.item.GalenaGauntletItem;
import com.github.alexmodguy.alexscaves.server.item.ResistorShieldItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACExEnchants {


    public static final DeferredRegister<Enchantment> DEF_REG;

    public static final RegistryObject<Enchantment> MAGNETICISM;

    public static final EnchantmentCategory MAGNETICISM_CATEGORY;

    public ACExEnchants() {
    }


    static {
        DEF_REG = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "alexscavesexemplified");
        MAGNETICISM_CATEGORY = EnchantmentCategory.create("magneticism_category", (item) -> {
            return item instanceof GalenaGauntletItem || item instanceof ResistorShieldItem;
        });
        MAGNETICISM = DEF_REG.register("magneticism", () -> new ACExMagneticism(Enchantment.Rarity.RARE, MAGNETICISM_CATEGORY, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
    }
}
