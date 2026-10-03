package org.crimsoncrips.alexscavesexemplified.networking;

import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExGammaAttacks;

public record ACExGammaAbilityPacket(int ability) implements CustomPacketPayload {

    public static final Type<ACExGammaAbilityPacket> TYPE = new Type<>(AlexsCavesExemplified.prefix("gamma_ability"));
    public static final StreamCodec<ByteBuf, ACExGammaAbilityPacket> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(ACExGammaAbilityPacket::new, ACExGammaAbilityPacket::ability);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ACExGammaAbilityPacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer serverPlayer && serverPlayer.getVehicle() instanceof TremorzillaEntity tremorzilla && message.ability() >= 0 && message.ability() < ACExGammaAttacks.ABILITY_COUNT && ACExGammaAttacks.canRiderUse(tremorzilla, serverPlayer)) {
                tremorzilla.setData(ACExAttachments.GAMMA_RIDER_REQUEST, message.ability());
            }
        });
    }
}
