package org.crimsoncrips.alexscavesexemplified.backport;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.Event;

public abstract class ServerTickEvent extends Event {

    private final MinecraftServer server;

    protected ServerTickEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    public static class Pre extends ServerTickEvent {
        public Pre(MinecraftServer server) {
            super(server);
        }
    }

    public static class Post extends ServerTickEvent {
        public Post(MinecraftServer server) {
            super(server);
        }
    }
}
