package org.crimsoncrips.alexscavesexemplified.backport;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExDataMapLoader extends SimplePreparableReloadListener<Void> {

    public record SyncPacket(CompoundTag data) implements CustomPacketPayload {
        public static final Type<SyncPacket> TYPE = new Type<>(AlexsCavesExemplified.prefix("data_map_sync"));
        public static final StreamCodec<io.netty.buffer.ByteBuf, SyncPacket> STREAM_CODEC = ByteBufCodecs.COMPOUND_TAG.map(SyncPacket::new, SyncPacket::data);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    @Override
    protected Void prepare(ResourceManager manager, ProfilerFiller profiler) {
        return null;
    }

    @Override
    protected void apply(Void object, ResourceManager manager, ProfilerFiller profiler) {
        for (DataMapType<?, ?> type : DataMapType.TYPES) {
            type.load(manager);
        }
    }

    public static void init() {
        ACExNetwork.playToClient(SyncPacket.TYPE, SyncPacket.STREAM_CODEC, (packet, context) -> context.enqueueWork(() -> receive(packet.data())));
        MinecraftForge.EVENT_BUS.addListener(ACExDataMapLoader::onAddReloadListeners);
        MinecraftForge.EVENT_BUS.addListener(ACExDataMapLoader::onDatapackSync);
    }

    private static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ACExDataMapLoader());
    }

    private static CompoundTag collect() {
        CompoundTag tag = new CompoundTag();
        for (DataMapType<?, ?> type : DataMapType.TYPES) {
            if (type.synced) {
                tag.put(type.id().toString(), type.write());
            }
        }
        return tag;
    }

    private static void receive(CompoundTag tag) {
        for (DataMapType<?, ?> type : DataMapType.TYPES) {
            if (type.synced && tag.contains(type.id().toString())) {
                type.read(tag.getCompound(type.id().toString()));
            }
        }
    }

    private static void onDatapackSync(OnDatapackSyncEvent event) {
        SyncPacket packet = new SyncPacket(collect());
        if (event.getPlayer() != null) {
            PacketDistributor.sendToPlayer(event.getPlayer(), packet);
        } else {
            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                PacketDistributor.sendToPlayer(player, packet);
            }
        }
    }
}
