package org.crimsoncrips.alexscavesexemplified.server.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExMagneticism extends Enchantment {


    public ACExMagneticism(Rarity p_44676_, EnchantmentCategory p_44677_, EquipmentSlot... p_44678_) {
        super(p_44676_, p_44677_, p_44678_);
    }



    public int getMinCost(int i) {
        return 7 + (i + 1) * 3;
    }

    public int getMaxCost(int i) {
        return super.getMinCost(i) + 13;
    }
    

    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    public String getDescriptionId() {
        if (AlexsCavesExemplified.COMMON_CONFIG.MAGNETICISM_ENABLED.get()) {
            return "enchantment.alexscavesexemplified.magneticism";
        } else {
            return "alexscavesexemplified.feature_disabled";
        }
    }
}
