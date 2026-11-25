package org.crimsoncrips.alexscavesexemplified.client;







import org.crimsoncrips.alexscavesexemplified.backport.ACExEventBridge;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import org.crimsoncrips.alexscavesexemplified.client.magnerip.ACExMagneripClient;
import org.crimsoncrips.alexscavesexemplified.client.sectumsempra.ACExSectumsempraClient;
import org.crimsoncrips.alexscavesexemplified.client.submarine.ACExSubmarineSonar;
import org.crimsoncrips.alexscavesexemplified.client.submarine.ACExSubmarineHud;
import org.crimsoncrips.alexscavesexemplified.client.submarine.ACExSubmarineRemoteClient;
import org.crimsoncrips.alexscavesexemplified.client.submarine.ACExSubmarineTelegraphClient;
import org.crimsoncrips.alexscavesexemplified.client.radiant.ACExRadiantEssenceClient;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.client.render.entity.GumWormRenderer;
import com.github.alexmodguy.alexscaves.client.render.entity.UnderzealotRenderer;
import org.crimsoncrips.alexscavesexemplified.client.layer.UnderzealotDaggerLayer;
import com.github.alexmodguy.alexscaves.client.particle.TephraParticle;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraftforge.client.event.ModelEvent;
import org.crimsoncrips.alexscavesexemplified.client.screen.QuarryUpgradeScreen;
import org.crimsoncrips.alexscavesexemplified.server.menu.ACExMenuRegistry;
import org.crimsoncrips.alexscavesexemplified.server.blocks.PreservedAmberBlock;
import org.crimsoncrips.alexscavesexemplified.server.blocks.PreservedCreature;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.item.PreservedAmberItem;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;
import org.crimsoncrips.alexscavesexemplified.server.item.IceCreamConeItem;
import org.crimsoncrips.alexscavesexemplified.client.soul.SiphonedSoulRenderer;
import org.crimsoncrips.alexscavesexemplified.client.entity.CuriosityOfferingRenderer;
import org.crimsoncrips.alexscavesexemplified.client.monolith.ACExDarkRitualClient;
import org.crimsoncrips.alexscavesexemplified.server.entity.DarkRitualEntity;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.client.event.EntityRenderersEvent;
import org.crimsoncrips.alexscavesexemplified.ACExCommonProxy;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
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

    @Override
    public void clientInit() {
        MenuScreens.register(ACExMenuRegistry.QUARRY_UPGRADES.get(), QuarryUpgradeScreen::new);
        ItemProperties.register(ACExBlockRegistry.PRESERVED_AMBER.get().asItem(), AlexsCavesExemplified.prefix("creature"), (stack, level, entity, seed) -> PreservedAmberItem.getCreature(stack).isAvailable() ? PreservedAmberItem.getCreature(stack).ordinal() : PreservedCreature.values().length);
        ItemProperties.register(ACExItemRegistry.ICE_CREAM_CONE.get(), AlexsCavesExemplified.prefix("formation"), (stack, level, entity, seed) -> {
            IceCreamConeItem.Formation formation = IceCreamConeItem.getFormation(stack);
            return formation == null ? 0 : formation.modelIndex();
        });
    }

    @Override
    public void tickDarkRitual(DarkRitualEntity ritual) {
        ACExDarkRitualClient.tick(ritual);
    }

    @Override
    public void init(IEventBus modEventBus) {
        ACExEventBridge.initClient();
        MinecraftForge.EVENT_BUS.register(new ACExClientEvents());
        MinecraftForge.EVENT_BUS.addListener(ACExRadiantEssenceClient::renderFlights);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineRemoteClient::onRightClickItem);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineRemoteClient::onMovementInput);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineRemoteClient::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineRemoteClient::onLoggingOut);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineRemoteClient::onRenderGui);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineRemoteClient::onRenderHand);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineRemoteClient::onPlaySound);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineRemoteClient::onCameraAngles);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineHud::onComputeFov);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineHud::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(ACExMagneripClient::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(ACExSectumsempraClient::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineHud::onRenderLiving);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineSonar::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineSonar::onRenderLevel);
        modEventBus.addListener(ACExSubmarineSonar::registerKeys);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineTelegraphClient::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(ACExSubmarineTelegraphClient::onMovementInput);
        modEventBus.addListener(ACExSubmarineTelegraphClient::registerKeys);
        modEventBus.addListener(this::setupParticles);
        modEventBus.addListener(this::registerRenderers);
        modEventBus.addListener(this::addLayers);
        modEventBus.addListener(this::showToasts);
        modEventBus.addListener(this::registerTooltips);
        modEventBus.addListener(this::hideUnavailablePreservedCreatures);
        modEventBus.addListener(ACExShaders::register);
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, modListScreen) -> new ACExConfigScreen(modListScreen)));
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
        event.registerEntityRenderer(ACExEntityRegistry.SIPHONED_SOUL.get(), SiphonedSoulRenderer::new);
        event.registerEntityRenderer(ACExEntityRegistry.CURIOSITY_OFFERING.get(), CuriosityOfferingRenderer::new);
        event.registerEntityRenderer(ACExEntityRegistry.DARK_RITUAL.get(), NoopRenderer::new);
    }

    public void addLayers(EntityRenderersEvent.AddLayers event) {
        if (event.getRenderer(ACEntityRegistry.UNDERZEALOT.get()) instanceof UnderzealotRenderer underzealotRenderer) {
            underzealotRenderer.addLayer(new UnderzealotDaggerLayer(underzealotRenderer));
        }
    }

    public void hideUnavailablePreservedCreatures(ModelEvent.ModifyBakingResult event) {
        BakedModel amber = event.getModels().get(BlockModelShaper.stateToModelLocation(ACBlockRegistry.AMBER.get().defaultBlockState()));
        if (amber == null)
            return;
        for (BlockState state : ACExBlockRegistry.PRESERVED_AMBER.get().getStateDefinition().getPossibleStates()) {
            if (!state.getValue(PreservedAmberBlock.CREATURE).isAvailable()) {
                event.getModels().put(BlockModelShaper.stateToModelLocation(state), amber);
            }
        }
    }

    public void registerTooltips(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ACExIrradiatedTooltip.class, tooltip -> tooltip);
        event.register(ACExGammaTooltip.class, tooltip -> tooltip);
    }

    public void showToasts(RegisterGuiOverlaysEvent event) {
        event.registerBelow(VanillaGuiOverlay.EXPERIENCE_BAR.id(), TOAST_LAYER.getPath(), (gui, graphics, partialTick, width, height) -> ACExToastManager.render(graphics));
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "radiant_meter", (gui, graphics, partialTick, width, height) -> ACExRadiantEssenceClient.renderMeter(graphics));
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "sectumsempra_bar", (gui, graphics, partialTick, width, height) -> ACExSectumsempraClient.renderBar(graphics, partialTick));
        event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "submarine_hud", (gui, graphics, partialTick, width, height) -> ACExSubmarineHud.renderOverlay(graphics, partialTick));
        event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "submarine_telegraph", (gui, graphics, partialTick, width, height) -> ACExSubmarineTelegraphClient.renderOverlay(graphics, partialTick));
    }


}
