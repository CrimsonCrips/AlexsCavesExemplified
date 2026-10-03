package org.crimsoncrips.alexscavesexemplified.client;

import org.crimsoncrips.alexscavesexemplified.client.submarine.ACExSubmarineRemoteClient;
import org.crimsoncrips.alexscavesexemplified.client.radiant.ACExRadiantEssenceClient;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.client.render.entity.GumWormRenderer;
import com.github.alexmodguy.alexscaves.client.render.entity.UnderzealotRenderer;
import org.crimsoncrips.alexscavesexemplified.client.layer.UnderzealotDaggerLayer;
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
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import org.crimsoncrips.alexscavesexemplified.client.render.ACExShaders;
import org.crimsoncrips.alexscavesexemplified.client.tooltip.ACExGammaTooltip;
import org.crimsoncrips.alexscavesexemplified.client.tooltip.ACExIrradiatedTooltip;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.hud.ACExToastManager;
import org.crimsoncrips.alexscavesexemplified.client.screen.ACExConfigScreen;
import org.crimsoncrips.alexscavesexemplified.client.entity.GammaNuclearBombRenderer;
import org.crimsoncrips.alexscavesexemplified.client.entity.GumwirmSegmentRenderer;
import org.crimsoncrips.alexscavesexemplified.client.entity.SpikesRenderer;
import org.crimsoncrips.alexscavesexemplified.client.entity.ToxicBarrelRenderer;
import org.crimsoncrips.alexscavesexemplified.client.entity.ToxicWasteRenderer;
import org.crimsoncrips.alexscavesexemplified.client.particle.*;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;

@OnlyIn(Dist.CLIENT)
public class ACExClientProxy extends ACExCommonProxy {

    private static final ResourceLocation TOAST_LAYER = AlexsCavesExemplified.prefix("toasts");

    public void init(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(new ACExClientEvents());
        NeoForge.EVENT_BUS.addListener(ACExRadiantEssenceClient::renderFlights);
        NeoForge.EVENT_BUS.addListener(ACExSubmarineRemoteClient::onRightClickItem);
        NeoForge.EVENT_BUS.addListener(ACExSubmarineRemoteClient::onMovementInput);
        NeoForge.EVENT_BUS.addListener(ACExSubmarineRemoteClient::onClientTick);
        NeoForge.EVENT_BUS.addListener(ACExSubmarineRemoteClient::onLoggingOut);
        modEventBus.addListener(this::setupParticles);
        modEventBus.addListener(this::registerRenderers);
        modEventBus.addListener(this::addLayers);
        modEventBus.addListener(this::showToasts);
        modEventBus.addListener(this::registerTooltips);
        modEventBus.addListener(ACExShaders::register);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class,
                (IConfigScreenFactory) (container, modListScreen) -> new ACExConfigScreen(modListScreen));
    }


    public void setupParticles(RegisterParticleProvidersEvent registry) {
        registry.registerSpriteSet(ACExParticleRegistry.TREMORZILLA_GAMMA_EXPLOSION.get(), SmallExplosions.GammaTremorzillaFactory::new);
        registry.registerSpecial(ACExParticleRegistry.TREMORZILLA_GAMMA_PROTON.get(), new GammaTremorProton.Factory());
        registry.registerSpriteSet(ACExParticleRegistry.TREMORZILLA_GAMMA_LIGHTNING.get(), GammaTremorLightning.Factory::new);

        registry.registerSpecial(ACExParticleRegistry.GAMMA_PROTON.get(), new GammaProton.GammaFactory());

        registry.registerSpecial(ACExParticleRegistry.GAMMA_MUSHROOM_CLOUD.get(), new GammaMushroomCloud.GammaNukeFactory());
        registry.registerSpecial(ACExParticleRegistry.MINI_GAMMA_MUSHROOM_CLOUD.get(), new MiniGammaMushroomCloud.Factory());

        registry.registerSpriteSet(ACExParticleRegistry.GAMMA_TEPHRA.get(), TephraParticle.Factory::new);
        registry.registerSpriteSet(ACExParticleRegistry.GAMMA_TEPHRA_SMALL.get(), TephraParticle.SmallFactory::new);
        registry.registerSpriteSet(ACExParticleRegistry.GAMMA_TEPHRA_FLAME.get(), TephraParticle.FlameFactory::new);

        registry.registerSpecial(ACExParticleRegistry.AZURE_FOCUSED_LIGHTNING.get(), new FocusedLightningParticle.AzureFactory());
        registry.registerSpecial(ACExParticleRegistry.SCARLET_FOCUSED_LIGHTNING.get(), new FocusedLightningParticle.ScarletFactory());


    }

    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ACExEntityRegistry.GAMMA_NUCLEAR_BOMB.get(), GammaNuclearBombRenderer::new);
        event.registerEntityRenderer(ACExEntityRegistry.GUMWIRM.get(), GumWormRenderer::new);
        event.registerEntityRenderer(ACExEntityRegistry.GUMWIRM_SEGMENT.get(), GumwirmSegmentRenderer::new);
        event.registerEntityRenderer(ACExEntityRegistry.SPIKES.get(), SpikesRenderer::new);
        event.registerEntityRenderer(ACExEntityRegistry.TOXIC_WASTE.get(), ToxicWasteRenderer::new);
        event.registerEntityRenderer(ACExEntityRegistry.TOXIC_BARREL.get(), ToxicBarrelRenderer::new);
    }

    public void addLayers(EntityRenderersEvent.AddLayers event) {
        if (event.getRenderer(ACEntityRegistry.UNDERZEALOT.get()) instanceof UnderzealotRenderer underzealotRenderer) {
            underzealotRenderer.addLayer(new UnderzealotDaggerLayer(underzealotRenderer));
        }
    }

    public void registerTooltips(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ACExIrradiatedTooltip.class, tooltip -> tooltip);
        event.register(ACExGammaTooltip.class, tooltip -> tooltip);
    }

    public void showToasts(RegisterGuiLayersEvent event) {
        event.registerBelow(VanillaGuiLayers.EXPERIENCE_BAR, TOAST_LAYER, (graphics, partialTicks) -> ACExToastManager.render(graphics));
        event.registerAbove(VanillaGuiLayers.HOTBAR, AlexsCavesExemplified.prefix("radiant_meter"), (graphics, partialTicks) -> ACExRadiantEssenceClient.renderMeter(graphics));
    }


}
