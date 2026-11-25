package org.crimsoncrips.alexscavesexemplified.networking;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import io.netty.buffer.ByteBuf;
import org.crimsoncrips.alexscavesexemplified.backport.ByteBufCodecs;
import org.crimsoncrips.alexscavesexemplified.backport.StreamCodec;
import org.crimsoncrips.alexscavesexemplified.backport.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.backport.IPayloadContext;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;

public record ACExSubmarineRemotePacket(int submarineId, double x, double y, double z, float yRot, float xRot) implements CustomPacketPayload {

    public static final Type<ACExSubmarineRemotePacket> TYPE = new Type<>(AlexsCavesExemplified.prefix("submarine_remote"));
    public static final StreamCodec<ByteBuf, ACExSubmarineRemotePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ACExSubmarineRemotePacket::submarineId,
            ByteBufCodecs.DOUBLE, ACExSubmarineRemotePacket::x,
            ByteBufCodecs.DOUBLE, ACExSubmarineRemotePacket::y,
            ByteBufCodecs.DOUBLE, ACExSubmarineRemotePacket::z,
            ByteBufCodecs.FLOAT, ACExSubmarineRemotePacket::yRot,
            ByteBufCodecs.FLOAT, ACExSubmarineRemotePacket::xRot,
            ACExSubmarineRemotePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ACExSubmarineRemotePacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            if (player.level().getEntity(message.submarineId()) instanceof SubmarineEntity submarine && ACExSubmarineRemote.canDrive(player, submarine, ACExSubmarineRemote.RANGE + ACExSubmarineRemote.RELEASE_MARGIN)) {
                ACExData.set(submarine, ACExAttachments.SUBMARINE_REMOTE_TIME, submarine.level().getGameTime());
                ACExData.set(player, ACExAttachments.REMOTE_SUBMARINE, submarine.getId());
                ACExAttachments.set(submarine, ACExAttachments.SUBMARINE_DRIVER, player.getId());
                if (submarine.position().distanceToSqr(new Vec3(message.x(), message.y(), message.z())) <= ACExSubmarineRemote.MAX_MOVE_PER_UPDATE * ACExSubmarineRemote.MAX_MOVE_PER_UPDATE) {
                    submarine.absMoveTo(message.x(), message.y(), message.z(), message.yRot(), message.xRot());
                }
            }
        });
    }
}
