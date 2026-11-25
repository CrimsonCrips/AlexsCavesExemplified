package org.crimsoncrips.alexscavesexemplified.backport;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class PacketDistributor {

    public static void sendToServer(CustomPacketPayload payload, CustomPacketPayload... payloads) {
        ACExNetwork.sendToServer(payload);
        for (CustomPacketPayload other : payloads) {
            ACExNetwork.sendToServer(other);
        }
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        ACExNetwork.sendToPlayer(player, payload);
        for (CustomPacketPayload other : payloads) {
            ACExNetwork.sendToPlayer(player, other);
        }
    }

    public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        ACExNetwork.CHANNEL.send(net.minecraftforge.network.PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), ACExNetwork.wrap(payload));
        for (CustomPacketPayload other : payloads) {
            ACExNetwork.CHANNEL.send(net.minecraftforge.network.PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), ACExNetwork.wrap(other));
        }
    }

    public static void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload payload) {
        ACExNetwork.CHANNEL.send(net.minecraftforge.network.PacketDistributor.TRACKING_ENTITY.with(() -> entity), ACExNetwork.wrap(payload));
    }
}
