package org.crimsoncrips.alexscavesexemplified.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExDataComponents;

import javax.annotation.Nullable;
import java.util.UUID;

public class ACExSubmarineRemote {

    public static final double RANGE = 140.0D;
    public static final double RELEASE_MARGIN = 16.0D;

    public static final int REMOTE_TIMEOUT = ACExUtils.seconds(0.5F);
    public static final double MAX_MOVE_PER_UPDATE = 4.0D;
    public static final float TURN_SPEED = 3.0F;
    public static final float TURN_ACCELERATION = 0.35F;
    public static final LocalControl LOCAL = new LocalControl();

    public static class LocalControl {
        public int submarineId = -1;
        public float strafe;
        public float forward;
        public boolean up;
        public boolean down;

        public void clearInput() {
            this.strafe = 0.0F;
            this.forward = 0.0F;
            this.up = false;
            this.down = false;
        }

        public void reset() {
            this.submarineId = -1;
            this.clearInput();
        }
    }

    public static boolean isRemoteControlled(Entity submarine) {
        if (submarine.isVehicle())
            return false;
        if (submarine.level().isClientSide)
            return submarine.getId() == LOCAL.submarineId;
        return ACExData.has(submarine, ACExAttachments.SUBMARINE_REMOTE_TIME) && submarine.level().getGameTime() - ACExData.get(submarine, ACExAttachments.SUBMARINE_REMOTE_TIME) <= REMOTE_TIMEOUT;
    }

    public static boolean enabled() {
        return AlexsCavesExemplified.COMMON_CONFIG.SUBMARINE_CHANGES_ENABLED.get();
    }

    public static void link(ItemStack controller, SubmarineEntity submarine) {
        ACExDataComponents.LINKED_SUBMARINE.set(controller, submarine.getUUID());
    }

    @Nullable
    public static UUID linkedTo(ItemStack stack) {
        return stack.is(ACItemRegistry.GAME_CONTROLLER.get()) ? ACExDataComponents.LINKED_SUBMARINE.get(stack) : null;
    }

    public static boolean holdsControllerFor(Player player, SubmarineEntity submarine) {
        for (InteractionHand hand : InteractionHand.values()) {
            if (submarine.getUUID().equals(linkedTo(player.getItemInHand(hand)))) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static SubmarineEntity remoteSubmarineOf(Player player) {
        int id = ACExData.get(player, ACExAttachments.REMOTE_SUBMARINE);
        if (id == -1)
            return null;
        if (player.level().getEntity(id) instanceof SubmarineEntity submarine && isRemoteControlled(submarine))
            return submarine;
        ACExData.set(player, ACExAttachments.REMOTE_SUBMARINE, -1);
        return null;
    }

    @Nullable
    public static Player driverOf(SubmarineEntity submarine) {
        if (submarine.getControllingPassenger() instanceof Player rider)
            return rider;
        for (Player player : submarine.level().players()) {
            if (remoteSubmarineOf(player) == submarine)
                return player;
        }
        return null;
    }

    public static boolean canDrive(Player player, SubmarineEntity submarine) {
        return canDrive(player, submarine, RANGE);
    }

    public static boolean canDrive(Player player, SubmarineEntity submarine, double range) {
        return enabled() && player.isAlive() && submarine.isAlive() && !submarine.isVehicle() && submarine.getDamageLevel() < 4
                && !drivenByOther(player, submarine) && player.distanceToSqr(submarine) <= range * range && holdsControllerFor(player, submarine);
    }

    public static int remoteDriverId(SubmarineEntity submarine) {
        int driver = ACExData.get(submarine, ACExAttachments.SUBMARINE_DRIVER);
        if (driver == -1 || submarine.level().isClientSide)
            return driver;
        return isRemoteControlled(submarine) ? driver : -1;
    }

    public static boolean drivenByOther(Player player, SubmarineEntity submarine) {
        int driver = remoteDriverId(submarine);
        return driver != -1 && driver != player.getId();
    }
}
