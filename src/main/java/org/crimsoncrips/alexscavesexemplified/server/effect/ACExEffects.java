package org.crimsoncrips.alexscavesexemplified.server.effect;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExEffects {

    public static final DeferredRegister<MobEffect> EFFECT_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, AlexsCavesExemplified.MODID);
    public static final DeferredRegister<Potion> POTION_REGISTER = DeferredRegister.create(Registries.POTION, AlexsCavesExemplified.MODID);

    public static final RegistryObject<MobEffect> SUGAR_CRASH = EFFECT_REGISTER.register("sugar_crash", ACExSugarCrash::new);
    public static final RegistryObject<MobEffect> RABIAL = EFFECT_REGISTER.register("rabial", ACExRabial::new);
    public static final RegistryObject<MobEffect> SERENED = EFFECT_REGISTER.register("serened", ACExSerened::new);
    public static final RegistryObject<MobEffect> TRANSMUTATION = EFFECT_REGISTER.register("transmutation", ACExTransmutation::new);

    public static final RegistryObject<Potion> TRANSMUTATION_POTION = POTION_REGISTER.register("transmutation", () -> new Potion(new MobEffectInstance(TRANSMUTATION.get(), ACExUtils.seconds(0.05F))));
}
