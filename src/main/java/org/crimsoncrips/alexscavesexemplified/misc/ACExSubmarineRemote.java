package org.crimsoncrips.alexscavesexemplified.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExDataComponents;

import javax.annotation.Nullable;
import java.util.UUID;

public class ACExSubmarineRemote {

    public static final double RANGE = 70.0D;

    public record RemoteInput(float strafe, float forward, boolean up, boolean down, long time) {
        public static final RemoteInput NONE = new RemoteInput(0.0F, 0.0F, false, false, Long.MIN_VALUE);
    }

    public static boolean enabled() {
        return AlexsCavesExemplified.COMMON_CONFIG.SUBMARINE_REMOTE_ENABLED.get();
    }

    public static void link(ItemStack controller, SubmarineEntity submarine) {
        controller.set(ACExDataComponents.LINKED_SUBMARINE, submarine.getUUID());
        controller.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
    }

    @Nullable
    public static UUID linkedTo(ItemStack stack) {
        return stack.is(ACItemRegistry.GAME_CONTROLLER.get()) ? stack.get(ACExDataComponents.LINKED_SUBMARINE) : null;
    }

    public static boolean holdsControllerFor(Player player, SubmarineEntity submarine) {
        for (InteractionHand hand : InteractionHand.values()) {
            if (submarine.getUUID().equals(linkedTo(player.getItemInHand(hand)))) {
                return true;
            }
        }
        return false;
    }

    public static boolean canDrive(Player player, SubmarineEntity submarine) {
        return enabled() && player.isAlive() && submarine.isAlive() && !submarine.isVehicle() && submarine.getDamageLevel() < 4
                && player.distanceToSqr(submarine) <= RANGE * RANGE && holdsControllerFor(player, submarine);
    }
}
