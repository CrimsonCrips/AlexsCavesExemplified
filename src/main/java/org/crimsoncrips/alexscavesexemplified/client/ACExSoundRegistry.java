package org.crimsoncrips.alexscavesexemplified.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExSoundRegistry {
    public static final DeferredRegister<SoundEvent> DEF_REG = DeferredRegister.create(Registries.SOUND_EVENT, AlexsCavesExemplified.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> TESLA_POWERUP = createSoundEvent("tesla_powerup");
    public static final DeferredHolder<SoundEvent, SoundEvent> TESLA_EXPLODING = createSoundEvent("tesla_exploding");
    public static final DeferredHolder<SoundEvent, SoundEvent> TESLA_FIRE = createSoundEvent("tesla_fire");
    public static final DeferredHolder<SoundEvent, SoundEvent> CARAMEL_EAT = createSoundEvent("caramel_eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> SWEET_PUNISHED = createSoundEvent("sweet_punished");
    public static final DeferredHolder<SoundEvent, SoundEvent> PSPSPSPS = createSoundEvent("pspspsps");


    private static DeferredHolder<SoundEvent, SoundEvent> createSoundEvent(final String soundName) {
        return DEF_REG.register(soundName, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(AlexsCavesExemplified.MODID, soundName)));
    }
}
