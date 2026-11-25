package org.crimsoncrips.alexscavesexemplified.backport;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public record ACExAttachmentSyncPacket(boolean entity, int entityId, BlockPos pos, ResourceLocation typeId, byte[] data) implements CustomPacketPayload {

    public static final Type<ACExAttachmentSyncPacket> TYPE = new Type<>(AlexsCavesExemplified.prefix("attachment_sync"));

    public static final StreamCodec<FriendlyByteBuf, ACExAttachmentSyncPacket> STREAM_CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeBoolean(packet.entity());
        if (packet.entity()) {
            buf.writeVarInt(packet.entityId());
        } else {
            buf.writeBlockPos(packet.pos());
        }
        buf.writeResourceLocation(packet.typeId());
        buf.writeByteArray(packet.data());
    }, buf -> {
        boolean entity = buf.readBoolean();
        int id = entity ? buf.readVarInt() : -1;
        BlockPos pos = entity ? BlockPos.ZERO : buf.readBlockPos();
        return new ACExAttachmentSyncPacket(entity, id, pos, buf.readResourceLocation(), buf.readByteArray());
    });

    static <T> ACExAttachmentSyncPacket forEntity(int id, AttachmentType<T> type, T value) {
        return new ACExAttachmentSyncPacket(true, id, BlockPos.ZERO, type.id(), bytes(type, value));
    }

    static <T> ACExAttachmentSyncPacket forBlock(BlockPos pos, AttachmentType<T> type, T value) {
        return new ACExAttachmentSyncPacket(false, -1, pos, type.id(), bytes(type, value));
    }

    private static <T> byte[] bytes(AttachmentType<T> type, T value) {
        FriendlyByteBuf buf = ACExData.encode(type, value);
        byte[] out = new byte[buf.readableBytes()];
        buf.readBytes(out);
        return out;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
