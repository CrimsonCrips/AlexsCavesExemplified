package org.crimsoncrips.alexscavesexemplified.networking;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExPacketRegistration {

    @SubscribeEvent
    public void setupPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(AlexsCavesExemplified.MODID).versioned("1.0.0").optional();
        registrar.playToClient(ACExToastPacket.TYPE, ACExToastPacket.STREAM_CODEC, ACExToastPacket::handle);
        registrar.playToServer(ACExSubmarineRemotePacket.TYPE, ACExSubmarineRemotePacket.STREAM_CODEC, ACExSubmarineRemotePacket::handle);
        registrar.playToClient(ACExRadiantConsumePacket.TYPE, ACExRadiantConsumePacket.STREAM_CODEC, ACExRadiantConsumePacket::handle);
        registrar.playToClient(ACExSugarStaffWheelPacket.TYPE, ACExSugarStaffWheelPacket.STREAM_CODEC, ACExSugarStaffWheelPacket::handle);
        registrar.playToServer(ACExSugarStaffCastPacket.TYPE, ACExSugarStaffCastPacket.STREAM_CODEC, ACExSugarStaffCastPacket::handle);
        registrar.playToServer(ACExGammaAbilityPacket.TYPE, ACExGammaAbilityPacket.STREAM_CODEC, ACExGammaAbilityPacket::handle);
    }
}
