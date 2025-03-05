package org.crimsoncrips.alexscavesexemplified.server.item;


import com.github.alexmodguy.alexscaves.server.item.ACFoods;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACEItemRegistry {
    public static final DeferredRegister<Item> DEF_REG = DeferredRegister.create(ForgeRegistries.ITEMS, AlexsCavesExemplified.MODID);

    public static final RegistryObject<Item> ICE_CREAM_CONE = DEF_REG.register("ice_cream_cone", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.4F).effect(() -> new MobEffectInstance(ACEffectRegistry.SUGAR_RUSH.get(), 300), 0.04F).build())));
    public static final RegistryObject<Item> DINO_EGGS = DEF_REG.register("dino_eggs", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

}