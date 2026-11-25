package org.crimsoncrips.alexscavesexemplified.backport;

import net.minecraftforge.eventbus.api.Event;

public abstract class ClientTickEvent extends Event {

    public static class Pre extends ClientTickEvent {
    }

    public static class Post extends ClientTickEvent {
    }
}
