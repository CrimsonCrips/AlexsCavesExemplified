package org.crimsoncrips.alexscavesexemplified.client.submarine;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;
import org.crimsoncrips.alexscavesexemplified.networking.ACExSubmarineRemotePacket;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class ACExSubmarineRemoteClient {

    private static int submarineId = -1;
    private static float strafe;
    private static float forward;
    private static boolean up;
    private static boolean down;
    private static boolean exitRequested;

    public static boolean isDriving() {
        return submarineId != -1;
    }

    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!event.getLevel().isClientSide || !ACExSubmarineRemote.enabled())
            return;
        UUID linked = ACExSubmarineRemote.linkedTo(event.getItemStack());
        if (linked == null)
            return;
        Minecraft minecraft = Minecraft.getInstance();
        if (isDriving()) {
            stop(minecraft);
        } else {
            SubmarineEntity submarine = findSubmarine(minecraft, linked);
            if (submarine != null && ACExSubmarineRemote.canDrive(minecraft.player, submarine)) {
                start(minecraft, submarine);
            } else {
                minecraft.player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.submarine_remote_out_of_range"), true);
            }
        }
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    private static SubmarineEntity findSubmarine(Minecraft minecraft, UUID uuid) {
        if (minecraft.level == null || minecraft.player == null)
            return null;
        for (SubmarineEntity submarine : minecraft.level.getEntitiesOfClass(SubmarineEntity.class, minecraft.player.getBoundingBox().inflate(ACExSubmarineRemote.RANGE))) {
            if (submarine.getUUID().equals(uuid)) {
                return submarine;
            }
        }
        return null;
    }

    private static void start(Minecraft minecraft, SubmarineEntity submarine) {
        submarineId = submarine.getId();
        exitRequested = false;
        minecraft.setCameraEntity(submarine);
        minecraft.player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.submarine_remote_start", minecraft.options.keyShift.getTranslatedKeyMessage()), true);
    }

    private static void stop(Minecraft minecraft) {
        submarineId = -1;
        strafe = 0.0F;
        forward = 0.0F;
        up = false;
        down = false;
        if (minecraft.player != null) {
            minecraft.setCameraEntity(minecraft.player);
        }
    }

    public static void onMovementInput(MovementInputUpdateEvent event) {
        if (!isDriving())
            return;
        Minecraft minecraft = Minecraft.getInstance();
        strafe = event.getInput().leftImpulse;
        forward = event.getInput().forwardImpulse;
        up = event.getInput().jumping;
        down = minecraft.options.keySprint.isDown();
        exitRequested |= event.getInput().shiftKeyDown;
        event.getInput().leftImpulse = 0.0F;
        event.getInput().forwardImpulse = 0.0F;
        event.getInput().up = false;
        event.getInput().down = false;
        event.getInput().left = false;
        event.getInput().right = false;
        event.getInput().jumping = false;
        event.getInput().shiftKeyDown = false;
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        if (!isDriving())
            return;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        Entity entity = minecraft.level == null ? null : minecraft.level.getEntity(submarineId);
        if (exitRequested || player == null || !(entity instanceof SubmarineEntity submarine) || !ACExSubmarineRemote.canDrive(player, submarine)) {
            stop(minecraft);
            return;
        }
        player.setSprinting(false);
        PacketDistributor.sendToServer(new ACExSubmarineRemotePacket(submarineId, strafe, forward, up, down));
    }

    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        submarineId = -1;
    }
}
