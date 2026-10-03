package org.crimsoncrips.alexscavesexemplified.networking;

import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;

public record ACExSubmarineRemotePacket(int submarineId, float strafe, float forward, boolean up, boolean down) implements CustomPacketPayload {

    public static final Type<ACExSubmarineRemotePacket> TYPE = new Type<>(AlexsCavesExemplified.prefix("submarine_remote"));
    public static final StreamCodec<ByteBuf, ACExSubmarineRemotePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ACExSubmarineRemotePacket::submarineId,
            ByteBufCodecs.FLOAT, ACExSubmarineRemotePacket::strafe,
            ByteBufCodecs.FLOAT, ACExSubmarineRemotePacket::forward,
            ByteBufCodecs.BOOL, ACExSubmarineRemotePacket::up,
            ByteBufCodecs.BOOL, ACExSubmarineRemotePacket::down,
            ACExSubmarineRemotePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ACExSubmarineRemotePacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            if (player.level().getEntity(message.submarineId()) instanceof SubmarineEntity submarine && ACExSubmarineRemote.canDrive(player, submarine)) {
                submarine.setData(ACExAttachments.SUBMARINE_REMOTE_INPUT, new ACExSubmarineRemote.RemoteInput(message.strafe(), message.forward(), message.up(), message.down(), submarine.level().getGameTime()));
            }
        });
    }
}
