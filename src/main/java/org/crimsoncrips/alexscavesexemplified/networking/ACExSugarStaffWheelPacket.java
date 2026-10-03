package org.crimsoncrips.alexscavesexemplified.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen;

public record ACExSugarStaffWheelPacket() implements CustomPacketPayload {

    public static final Type<ACExSugarStaffWheelPacket> TYPE = new Type<>(AlexsCavesExemplified.prefix("sugar_staff_wheel"));
    public static final StreamCodec<ByteBuf, ACExSugarStaffWheelPacket> STREAM_CODEC = StreamCodec.unit(new ACExSugarStaffWheelPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ACExSugarStaffWheelPacket message, IPayloadContext ctx) {
        ctx.enqueueWork(ACExSugarStaffWheelScreen::open);
    }
}
