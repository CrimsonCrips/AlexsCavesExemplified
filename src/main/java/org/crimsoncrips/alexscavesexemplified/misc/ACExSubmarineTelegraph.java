package org.crimsoncrips.alexscavesexemplified.misc;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ACExSubmarineTelegraph {

    public static final float SAFE_SPEED = (float) (ACExSubmarineHunt.MAX_SUBMARINE_SPEED * ACExSubmarineHunt.SPEEDING_RATIO * 0.9D);
    private static final float THROTTLE_RATE = 0.02F;

    public static int submarineId = -1;
    public static Order order = Order.ALL_STOP;

    public enum Order {
        ASTERN("astern", -0.35F),
        ALL_STOP("all_stop", 0.0F),
        AHEAD_SLOW("ahead_slow", 0.25F),
        AHEAD_HALF("ahead_half", 0.5F),
        AHEAD_FULL("ahead_full", 0.75F),
        AHEAD_FLANK("ahead_flank", 1.0F);

        public final String key;
        public final float throttle;

        Order(String key, float speed) {
            this.key = key;
            this.throttle = speed * (float) ACExSubmarineHunt.MAX_SUBMARINE_SPEED;
        }

        public boolean speeding() {
            return this == AHEAD_FLANK;
        }

        public Order step(int direction) {
            Order[] orders = values();
            return orders[Mth.clamp(this.ordinal() + direction, 0, orders.length - 1)];
        }

        public static Order closestTo(float acceleration) {
            Order closest = ALL_STOP;
            for (Order order : values()) {
                if (Math.abs(order.throttle - acceleration) < Math.abs(closest.throttle - acceleration)) {
                    closest = order;
                }
            }
            return closest;
        }
    }

    public static boolean engaged(Entity submarine) {
        return submarine.level().isClientSide && submarine.getId() == submarineId;
    }

    public static void engage(int id, Order startOrder) {
        submarineId = id;
        order = startOrder;
    }

    public static void disengage() {
        submarineId = -1;
        order = Order.ALL_STOP;
    }

    public static float throttle(float acceleration, float trackedSpeed) {
        if (!order.speeding() && trackedSpeed > SAFE_SPEED)
            return Mth.approach(acceleration, 0.0F, THROTTLE_RATE);
        return Mth.approach(acceleration, order.throttle, THROTTLE_RATE);
    }
}
