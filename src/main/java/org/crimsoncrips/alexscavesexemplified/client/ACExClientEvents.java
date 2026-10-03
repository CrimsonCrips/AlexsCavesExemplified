package org.crimsoncrips.alexscavesexemplified.client;

import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.crimsoncrips.alexscavesexemplified.client.tooltip.ACExGammaText;
import org.crimsoncrips.alexscavesexemplified.client.tooltip.ACExGammaTooltip;
import org.crimsoncrips.alexscavesexemplified.client.tooltip.ACExIrradiatedTooltip;
import java.util.List;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExDataComponents;
import com.github.alexmodguy.alexscaves.server.entity.living.CaniacEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import com.github.alexmodguy.alexscaves.mixin.client.CameraAccessor;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
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
        if (cameraEntity != null && event.getCamera().isDetached() && cameraEntity.getVehicle() instanceof TremorzillaEntity tremorzilla && tremorzilla.getData(ACExAttachments.GAMMA) && AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get()) {
            CameraAccessor camera = (CameraAccessor) event.getCamera();
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
        if (tooltipEvent.getItemStack().has(ACExDataComponents.LINKED_SUBMARINE)) {
            tooltipEvent.getToolTip().add(Component.translatable("misc.alexscavesexemplified.linked_submarine").withStyle(ChatFormatting.AQUA));
        }
        if (tooltipEvent.getItemStack().has(ACExDataComponents.IRRADIATED)) {
            tooltipEvent.getToolTip().add(Component.translatable(ACExIrradiatedTooltip.KEY).withStyle(ChatFormatting.GREEN));
        }
    }

    @SubscribeEvent
    public void gatherTooltip(RenderTooltipEvent.GatherComponents gatherEvent) {
        List<Either<FormattedText, TooltipComponent>> elements = gatherEvent.getTooltipElements();
        if (ACExGammaText.isGammaBomb(gatherEvent.getItemStack()) && !elements.isEmpty()) {
            elements.get(0).left().filter(Component.class::isInstance).ifPresent(name -> elements.set(0, Either.right(new ACExGammaTooltip((Component) name))));
        }
        if (!gatherEvent.getItemStack().has(ACExDataComponents.IRRADIATED))
            return;
        gatherEvent.getTooltipElements().replaceAll(element -> element.left().filter(ACExIrradiatedTooltip::matches).<Either<FormattedText, TooltipComponent>>map(text -> Either.right(new ACExIrradiatedTooltip((Component) text))).orElse(element));
    }

    double vibrate = 0;



    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void preRender(RenderLivingEvent.Pre preEvent) {
        if (preEvent.getEntity().hasEffect(ACExEffects.RABIAL) && AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get()) {
            preEvent.getPoseStack().pushPose();
            vibrate = (preEvent.getEntity().getRandom().nextFloat() - 0.5F) * (Math.sin((double) preEvent.getEntity().tickCount / 50) * 0.5 + 0.5) * 0.1;
            if (vibrate >= 0) {
                preEvent.getPoseStack().translate(vibrate,  vibrate, vibrate);
            }
        }
        if (preEvent.getEntity() instanceof CaniacEntity && AlexsCavesExemplified.COMMON_CONFIG.CANIAC_MANIAC_ENABLED.get()) {
            preEvent.getPoseStack().pushPose();
            vibrate = (preEvent.getEntity().getRandom().nextFloat() - 0.5F) * (Math.sin((double) preEvent.getEntity().tickCount / 50) * 0.5 + 0.5) * 0.1;
            if (vibrate >= 0) {
                preEvent.getPoseStack().translate(vibrate,  vibrate, vibrate);
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void postRender(RenderLivingEvent.Post postEvent) {
        if (postEvent.getEntity().hasEffect(ACExEffects.RABIAL)) {
            postEvent.getPoseStack().popPose();
        }
        if (postEvent.getEntity() instanceof CaniacEntity && AlexsCavesExemplified.COMMON_CONFIG.CANIAC_MANIAC_ENABLED.get()) {
            postEvent.getPoseStack().popPose();
        }
    }

}
