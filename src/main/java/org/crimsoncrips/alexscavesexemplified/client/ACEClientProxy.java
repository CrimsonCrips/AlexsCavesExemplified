package org.crimsoncrips.alexscavesexemplified.client;

import com.github.alexmodguy.alexscaves.client.event.ClientEvents;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.client.particle.TephraParticle;
import com.github.alexmodguy.alexscaves.client.particle.TeslaBulbLightningParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.crimsoncrips.alexscavesexemplified.ACECommonProxy;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.particle.*;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, value = Dist.CLIENT)
public class ACEClientProxy extends ACECommonProxy {

    public void init() {
        MinecraftForge.EVENT_BUS.register(new ACEClientEvents());
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setupParticles);
    }


    public void setupParticles(RegisterParticleProvidersEvent registry) {
        registry.registerSpriteSet(ACEParticleRegistry.TREMORZILLA_GAMMA_EXPLOSION.get(), SmallExplosions.GammaTremorzillaFactory::new);
        registry.registerSpecial(ACEParticleRegistry.TREMORZILLA_GAMMA_PROTON.get(), new GammaTremorProton.Factory());
        registry.registerSpriteSet(ACEParticleRegistry.TREMORZILLA_GAMMA_LIGHTNING.get(), GammaTremorLightning.Factory::new);

        registry.registerSpecial(ACEParticleRegistry.GAMMA_PROTON.get(), new GammaProton.GammaFactory());

        registry.registerSpecial(ACEParticleRegistry.GAMMA_MUSHROOM_CLOUD.get(), new GammaMushroomCloud.GammaNukeFactory());

        registry.registerSpriteSet(ACEParticleRegistry.GAMMA_TEPHRA.get(), TephraParticle.Factory::new);
        registry.registerSpriteSet(ACEParticleRegistry.GAMMA_TEPHRA_SMALL.get(), TephraParticle.SmallFactory::new);
        registry.registerSpriteSet(ACEParticleRegistry.GAMMA_TEPHRA_FLAME.get(), TephraParticle.FlameFactory::new);

        registry.registerSpecial(ACEParticleRegistry.AZURE_FOCUSED_LIGHTNING.get(), new FocusedLightningParticle.AzureFactory());
        registry.registerSpecial(ACEParticleRegistry.SCARLET_FOCUSED_LIGHTNING.get(), new FocusedLightningParticle.ScarletFactory());


    }


}
