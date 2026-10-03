package org.crimsoncrips.alexscavesexemplified.misc;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;

public class ACExScheduler {

    private record Task(ServerLevel level, long runAt, Runnable action) {
    }

    private static final List<Task> TASKS = new ArrayList<>();

    public static void schedule(ServerLevel level, int delay, Runnable action) {
        TASKS.add(new Task(level, level.getGameTime() + delay, action));
    }

    public static void tick(ServerTickEvent.Post event) {
        if (TASKS.isEmpty())
            return;
        List<Task> due = new ArrayList<>();
        TASKS.removeIf(task -> {
            boolean ready = task.level().getGameTime() >= task.runAt();
            if (ready) {
                due.add(task);
            }
            return ready;
        });
        due.forEach(task -> task.action().run());
    }

    public static void clear(ServerStoppedEvent event) {
        TASKS.clear();
    }
}
