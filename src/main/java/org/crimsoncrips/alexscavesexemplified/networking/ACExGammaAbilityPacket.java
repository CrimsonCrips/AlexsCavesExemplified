package org.crimsoncrips.alexscavesexemplified.networking;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import io.netty.buffer.ByteBuf;
import org.crimsoncrips.alexscavesexemplified.backport.ByteBufCodecs;
import org.crimsoncrips.alexscavesexemplified.backport.StreamCodec;
import org.crimsoncrips.alexscavesexemplified.backport.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.crimsoncrips.alexscavesexemplified.backport.IPayloadContext;
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
                ACExData.set(tremorzilla, ACExAttachments.GAMMA_RIDER_REQUEST, message.ability());
            }
        });
    }
}
