package org.crimsoncrips.cavesfix;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.crimsoncrips.cavesfix.event.ACExemplifiedEvents;

@Mod(AlexsCavesExemplified.MODID)
public class AlexsCavesExemplified {

    public static final String MODID = "cavesfix";

    public AlexsCavesExemplified() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(new ACExemplifiedEvents());
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::setup);

    }


    private void setup(final FMLCommonSetupEvent event) {
    }


}
