package org.crimsoncrips.alexscavesexemplified.server.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExEffects {

    public static final DeferredRegister<MobEffect> EFFECT_REGISTER = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, AlexsCavesExemplified.MODID);
    public static final DeferredRegister<Potion> POTION_REGISTER = DeferredRegister.create(ForgeRegistries.POTIONS, AlexsCavesExemplified.MODID);

    public static final RegistryObject<MobEffect> SUGAR_CRASH = EFFECT_REGISTER.register("sugar_crash", ACExSugarCrash::new);
    public static final RegistryObject<MobEffect> RABIAL = EFFECT_REGISTER.register("rabial", ACExRabial::new);
    public static final RegistryObject<MobEffect> SERENED = EFFECT_REGISTER.register("serened", ACExSerened::new);


    public static ItemStack createPotion(Potion potion){
        return  PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
    }




    public static void init(){

    }
}
