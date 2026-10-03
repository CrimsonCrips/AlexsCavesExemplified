package org.crimsoncrips.alexscavesexemplified.networking;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.hud.ACExToastManager;

public record ACExToastPacket(Component message, long displayTimeMs) implements CustomPacketPayload {

    public static final Type<ACExToastPacket> TYPE = new Type<>(AlexsCavesExemplified.prefix("toast_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ACExToastPacket> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.STREAM_CODEC, ACExToastPacket::message,
            ByteBufCodecs.VAR_LONG, ACExToastPacket::displayTimeMs,
            ACExToastPacket::new
    );

    public static final long DISPLAY_TIME_MS = ACExUtils.secondsInMillis(5.0F);

    public ACExToastPacket(Component message) {
        this(message, DISPLAY_TIME_MS);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ACExToastPacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> ACExToastManager.addToast(message.message(), message.displayTimeMs()));
    }
}
