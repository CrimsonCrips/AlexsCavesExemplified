package org.crimsoncrips.alexscavesexemplified.client;

import com.github.alexmodguy.alexscaves.client.particle.TephraParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.crimsoncrips.alexscavesexemplified.ACExCommonProxy;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.hud.ACExToastManager;
import org.crimsoncrips.alexscavesexemplified.client.screen.ACExConfigScreen;
import org.crimsoncrips.alexscavesexemplified.client.entity.GammaNuclearBombRenderer;
import org.crimsoncrips.alexscavesexemplified.client.particle.*;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;

@OnlyIn(Dist.CLIENT)
public class ACExClientProxy extends ACExCommonProxy {

    private static final ResourceLocation TOAST_LAYER = AlexsCavesExemplified.prefix("toasts");

    public void init(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(new ACExClientEvents());
        modEventBus.addListener(this::setupParticles);
        modEventBus.addListener(this::registerRenderers);
        modEventBus.addListener(this::showToasts);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class,
                (IConfigScreenFactory) (container, modListScreen) -> new ACExConfigScreen(modListScreen));
    }


    public void setupParticles(RegisterParticleProvidersEvent registry) {
        registry.registerSpriteSet(ACExParticleRegistry.TREMORZILLA_GAMMA_EXPLOSION.get(), SmallExplosions.GammaTremorzillaFactory::new);
        registry.registerSpecial(ACExParticleRegistry.TREMORZILLA_GAMMA_PROTON.get(), new GammaTremorProton.Factory());
        registry.registerSpriteSet(ACExParticleRegistry.TREMORZILLA_GAMMA_LIGHTNING.get(), GammaTremorLightning.Factory::new);

        registry.registerSpecial(ACExParticleRegistry.GAMMA_PROTON.get(), new GammaProton.GammaFactory());

        registry.registerSpecial(ACExParticleRegistry.GAMMA_MUSHROOM_CLOUD.get(), new GammaMushroomCloud.GammaNukeFactory());

        registry.registerSpriteSet(ACExParticleRegistry.GAMMA_TEPHRA.get(), TephraParticle.Factory::new);
        registry.registerSpriteSet(ACExParticleRegistry.GAMMA_TEPHRA_SMALL.get(), TephraParticle.SmallFactory::new);
        registry.registerSpriteSet(ACExParticleRegistry.GAMMA_TEPHRA_FLAME.get(), TephraParticle.FlameFactory::new);

        registry.registerSpecial(ACExParticleRegistry.AZURE_FOCUSED_LIGHTNING.get(), new FocusedLightningParticle.AzureFactory());
        registry.registerSpecial(ACExParticleRegistry.SCARLET_FOCUSED_LIGHTNING.get(), new FocusedLightningParticle.ScarletFactory());


    }

    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ACExEntityRegistry.GAMMA_NUCLEAR_BOMB.get(), GammaNuclearBombRenderer::new);
    }

    public void showToasts(RegisterGuiLayersEvent event) {
        event.registerBelow(VanillaGuiLayers.EXPERIENCE_BAR, TOAST_LAYER, (graphics, partialTicks) -> ACExToastManager.render(graphics));
    }


}
