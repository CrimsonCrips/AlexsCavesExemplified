package org.crimsoncrips.alexscavesexemplified.backport;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Cancelable;

public abstract class EntityTickEvent extends EntityEvent {

    protected EntityTickEvent(Entity entity) {
        super(entity);
    }

    @Cancelable
    public static class Pre extends EntityTickEvent {
        public Pre(Entity entity) {
            super(entity);
        }
    }

    public static class Post extends EntityTickEvent {
        public Post(Entity entity) {
            super(entity);
        }
    }

    public static void tick(Entity entity, Runnable original) {
        if (MinecraftForge.EVENT_BUS.post(new Pre(entity))) {
            return;
        }
        original.run();
        MinecraftForge.EVENT_BUS.post(new Post(entity));
    }
}
