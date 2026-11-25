package org.crimsoncrips.alexscavesexemplified.networking;

import org.crimsoncrips.alexscavesexemplified.backport.ACExNetwork;

public class ACExPacketRegistration {

    public static void register() {
        ACExNetwork.register();
        ACExNetwork.playToClient(ACExToastPacket.TYPE, ACExToastPacket.STREAM_CODEC, ACExToastPacket::handle);
        ACExNetwork.playToServer(ACExSubmarineRemotePacket.TYPE, ACExSubmarineRemotePacket.STREAM_CODEC, ACExSubmarineRemotePacket::handle);
        ACExNetwork.playToClient(ACExRadiantConsumePacket.TYPE, ACExRadiantConsumePacket.STREAM_CODEC, ACExRadiantConsumePacket::handle);
        ACExNetwork.playToClient(ACExSugarStaffWheelPacket.TYPE, ACExSugarStaffWheelPacket.STREAM_CODEC, ACExSugarStaffWheelPacket::handle);
        ACExNetwork.playToServer(ACExSugarStaffCastPacket.TYPE, ACExSugarStaffCastPacket.STREAM_CODEC, ACExSugarStaffCastPacket::handle);
        ACExNetwork.playToServer(ACExGammaAbilityPacket.TYPE, ACExGammaAbilityPacket.STREAM_CODEC, ACExGammaAbilityPacket::handle);
    }
}
