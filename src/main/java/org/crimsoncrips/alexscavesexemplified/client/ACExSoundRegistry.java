package org.crimsoncrips.alexscavesexemplified.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExSoundRegistry {
    public static final DeferredRegister<SoundEvent> DEF_REG = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AlexsCavesExemplified.MODID);

    public static final RegistryObject<SoundEvent> TESLA_POWERUP = createSoundEvent("tesla_powerup");
    public static final RegistryObject<SoundEvent> TESLA_EXPLODING = createSoundEvent("tesla_exploding");
    public static final RegistryObject<SoundEvent> TESLA_FIRE = createSoundEvent("tesla_fire");
    public static final RegistryObject<SoundEvent> CARAMEL_EAT = createSoundEvent("caramel_eat");
    public static final RegistryObject<SoundEvent> SWEET_PUNISHED = createSoundEvent("sweet_punished");
    public static final RegistryObject<SoundEvent> PSPSPSPS = createSoundEvent("pspspsps");


    private static RegistryObject<SoundEvent> createSoundEvent(final String soundName) {
        return DEF_REG.register(soundName, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(AlexsCavesExemplified.MODID, soundName)));
    }
}
