package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(PlayerList.class)
public abstract class ACExPlayerListMixin {

    @Shadow @Final private List<ServerPlayer> players;

    @Inject(method = "broadcast", at = @At("TAIL"))
    private void alexsCavesExemplified$broadcast(@Nullable Player except, double x, double y, double z, double radius, ResourceKey<Level> dimension, Packet<?> packet, CallbackInfo ci) {
        if (!(packet instanceof ClientboundSoundPacket) && !(packet instanceof ClientboundSoundEntityPacket) || !ACExSubmarineRemote.enabled())
            return;
        double radiusSqr = radius * radius;
        for (ServerPlayer player : this.players) {
            if (player == except || player.level().dimension() != dimension || player.distanceToSqr(x, y, z) < radiusSqr)
                continue;
            SubmarineEntity submarine = ACExSubmarineRemote.remoteSubmarineOf(player);
            if (submarine != null && submarine.distanceToSqr(x, y, z) < radiusSqr) {
                player.connection.send(packet);
            }
        }
    }
}
