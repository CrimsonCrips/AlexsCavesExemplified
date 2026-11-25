package org.crimsoncrips.alexscavesexemplified.networking;

import io.netty.buffer.ByteBuf;
import org.crimsoncrips.alexscavesexemplified.backport.ByteBufCodecs;
import org.crimsoncrips.alexscavesexemplified.backport.StreamCodec;
import org.crimsoncrips.alexscavesexemplified.backport.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.crimsoncrips.alexscavesexemplified.backport.IPayloadContext;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;

public record ACExSugarStaffCastPacket(int ability) implements CustomPacketPayload {

    public static final Type<ACExSugarStaffCastPacket> TYPE = new Type<>(AlexsCavesExemplified.prefix("sugar_staff_cast"));
    public static final StreamCodec<ByteBuf, ACExSugarStaffCastPacket> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(ACExSugarStaffCastPacket::new, ACExSugarStaffCastPacket::ability);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ACExSugarStaffCastPacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer serverPlayer) {
                ACExSugarStaffAbilities.cast(serverPlayer, message.ability());
            }
        });
    }
}
