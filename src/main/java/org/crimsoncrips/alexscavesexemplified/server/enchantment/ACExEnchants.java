package org.crimsoncrips.alexscavesexemplified.server.enchantment;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExItemTagGenerator;

public class ACExEnchants {

    public static final ResourceKey<Enchantment> MAGNETICISM = ResourceKey.create(Registries.ENCHANTMENT, AlexsCavesExemplified.prefix("magneticism"));

    public static int getLevel(Level level, ItemStack stack, ResourceKey<Enchantment> enchantment) {
        if (level == null || stack.isEmpty()) {
            return 0;
        }
        return level.registryAccess().lookup(Registries.ENCHANTMENT).flatMap(lookup -> lookup.get(enchantment)).map(stack::getEnchantmentLevel).orElse(0);
    }

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        context.register(MAGNETICISM, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ACExItemTagGenerator.MAGNETICISM_ENCHANTABLE),
                2,
                1,
                Enchantment.constantCost(13),
                Enchantment.constantCost(24),
                4,
                EquipmentSlotGroup.HAND
        )).build(MAGNETICISM.location()));
    }
}
