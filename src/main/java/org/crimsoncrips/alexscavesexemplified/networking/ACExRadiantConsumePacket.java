package org.crimsoncrips.alexscavesexemplified.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.radiant.ACExRadiantEssenceClient;

public record ACExRadiantConsumePacket(int entityId) implements CustomPacketPayload {

    public static final Type<ACExRadiantConsumePacket> TYPE = new Type<>(AlexsCavesExemplified.prefix("radiant_consume"));
    public static final StreamCodec<ByteBuf, ACExRadiantConsumePacket> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(ACExRadiantConsumePacket::new, ACExRadiantConsumePacket::entityId);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ACExRadiantConsumePacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> ACExRadiantEssenceClient.playConsume(message.entityId()));
    }
}
