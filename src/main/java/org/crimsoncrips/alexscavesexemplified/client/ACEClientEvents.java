package org.crimsoncrips.alexscavesexemplified.client;

import com.github.alexmodguy.alexscaves.server.entity.living.CaniacEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.WatcherEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;

@OnlyIn(Dist.CLIENT)
public class ACEClientEvents {

    double vibrate = 0;



    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void preRender(RenderLivingEvent.Pre preEvent) {
        if (preEvent.getEntity().hasEffect(ACEEffects.RABIAL.get()) && AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get()) {
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
        if (postEvent.getEntity().hasEffect(ACEEffects.RABIAL.get())) {
            postEvent.getPoseStack().popPose();
        }
        if (postEvent.getEntity() instanceof CaniacEntity && AlexsCavesExemplified.COMMON_CONFIG.CANIAC_MANIAC_ENABLED.get()) {
            postEvent.getPoseStack().popPose();
        }
    }



}
