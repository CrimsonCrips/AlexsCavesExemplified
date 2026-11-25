package org.crimsoncrips.alexscavesexemplified.server.menu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExMenuRegistry {

    public static final DeferredRegister<MenuType<?>> DEF_REG = DeferredRegister.create(Registries.MENU, AlexsCavesExemplified.MODID);

    public static final RegistryObject<MenuType<QuarryUpgradeMenu>> QUARRY_UPGRADES = DEF_REG.register("quarry_upgrades", () -> new MenuType<>(QuarryUpgradeMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
