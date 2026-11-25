package org.crimsoncrips.alexscavesexemplified.client;



import org.crimsoncrips.alexscavesexemplified.backport.ClientTickEvent;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.crimsoncrips.alexscavesexemplified.client.tooltip.ACExGammaText;
import org.crimsoncrips.alexscavesexemplified.client.tooltip.ACExGammaTooltip;
import org.crimsoncrips.alexscavesexemplified.client.tooltip.ACExIrradiatedTooltip;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import java.util.HashSet;
import java.util.Set;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExDataComponents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import org.crimsoncrips.alexscavesexemplified.mixins.backport.ACExCameraAccessor;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.ACExDataMaps;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;
import com.github.alexmodguy.alexscaves.server.misc.ACKeybindRegistry;
import org.crimsoncrips.alexscavesexemplified.client.screen.ACExGammaWheelScreen;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExGammaAttacks;

@OnlyIn(Dist.CLIENT)
public class ACExClientEvents {

    private static final float GAMMA_TREMORZILLA_EXTRA_ZOOM = 10.0F * (1.4F * 1.5F - 1.0F);

    @SubscribeEvent(priority = EventPriority.LOW)
    public void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity != null && event.getCamera().isDetached() && cameraEntity.getVehicle() instanceof TremorzillaEntity tremorzilla && ACExData.get(tremorzilla, ACExAttachments.GAMMA) && AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get()) {
            ACExCameraAccessor camera = (ACExCameraAccessor) event.getCamera();
            camera.invokeMove(-camera.invokeGetMaxZoom(GAMMA_TREMORZILLA_EXTRA_ZOOM), 0.0F, 0.0F);
        }
    }

    @SubscribeEvent
    public void clientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        boolean pressed = false;
        while (ACKeybindRegistry.KEY_SPECIAL_ABILITY.consumeClick()) {
            pressed = true;
        }
        if (pressed && minecraft.screen == null && minecraft.player != null && minecraft.player.getVehicle() instanceof TremorzillaEntity tremorzilla && ACExGammaAttacks.canRiderUse(tremorzilla, minecraft.player)) {
            ACExGammaWheelScreen.open(tremorzilla);
        }
    }

    @SubscribeEvent
    public void itemTooltip(ItemTooltipEvent tooltipEvent) {
        if (ACExDataComponents.LINKED_SUBMARINE.has(tooltipEvent.getItemStack())) {
            tooltipEvent.getToolTip().add(Component.translatable("misc.alexscavesexemplified.linked_submarine").withStyle(ChatFormatting.AQUA));
        }
        if (ACExDataComponents.IRRADIATED.has(tooltipEvent.getItemStack())) {
            tooltipEvent.getToolTip().add(Component.translatable(ACExIrradiatedTooltip.KEY).withStyle(ChatFormatting.GREEN));
        }
        ACExDataMaps.DepthResistance depthResistance = ACExDataMaps.DEPTH_RESISTANCE.get(tooltipEvent.getItemStack().getItem());
        if (depthResistance != null && depthResistance.depth() != 0 && AlexsCavesExemplified.COMMON_CONFIG.ABYSSAL_CRUSH_ENABLED.get()) {
            String depth = (depthResistance.depth() > 0 ? "+" : "") + depthResistance.depth();
            tooltipEvent.getToolTip().add(Component.translatable("misc.alexscavesexemplified.depth_resistance", depth).withStyle(depthResistance.depth() > 0 ? ChatFormatting.BLUE : ChatFormatting.RED));
        }
    }

    @SubscribeEvent
    public void gatherTooltip(RenderTooltipEvent.GatherComponents gatherEvent) {
        List<Either<FormattedText, TooltipComponent>> elements = gatherEvent.getTooltipElements();
        if (ACExGammaText.isGammaBomb(gatherEvent.getItemStack()) && !elements.isEmpty()) {
            elements.get(0).left().filter(Component.class::isInstance).ifPresent(name -> elements.set(0, Either.right(new ACExGammaTooltip((Component) name))));
        }
        if (!ACExDataComponents.IRRADIATED.has(gatherEvent.getItemStack()))
            return;
        gatherEvent.getTooltipElements().replaceAll(element -> element.left().filter(ACExIrradiatedTooltip::matches).<Either<FormattedText, TooltipComponent>>map(text -> Either.right(new ACExIrradiatedTooltip((Component) text))).orElse(element));
    }

    private final Set<Integer> shaking = new HashSet<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public void preRender(RenderLivingEvent.Pre preEvent) {
        LivingEntity entity = preEvent.getEntity();
        if (entity.hasEffect(ACExEffects.RABIAL.get()) && AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get()) {
            preEvent.getPoseStack().pushPose();
            shaking.add(entity.getId());
            double vibrate = (entity.getRandom().nextFloat() - 0.5F) * (Math.sin((double) entity.tickCount / 50) * 0.5 + 0.5) * 0.1;
            if (vibrate >= 0) {
                preEvent.getPoseStack().translate(vibrate, vibrate, vibrate);
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void postRender(RenderLivingEvent.Post postEvent) {
        if (shaking.remove(postEvent.getEntity().getId())) {
            postEvent.getPoseStack().popPose();
        }
    }

}
