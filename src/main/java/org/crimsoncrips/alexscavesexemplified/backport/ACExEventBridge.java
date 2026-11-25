package org.crimsoncrips.alexscavesexemplified.backport;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

public class ACExEventBridge {

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(ACExEventBridge::onServerTick);
    }

    public static void initClient() {
        MinecraftForge.EVENT_BUS.addListener(ACExEventBridge::onClientTick);
    }

    private static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            MinecraftForge.EVENT_BUS.post(new ServerTickEvent.Pre(event.getServer()));
        } else {
            MinecraftForge.EVENT_BUS.post(new ServerTickEvent.Post(event.getServer()));
        }
    }

    private static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            MinecraftForge.EVENT_BUS.post(new ClientTickEvent.Pre());
        } else {
            MinecraftForge.EVENT_BUS.post(new ClientTickEvent.Post());
        }
    }
}
