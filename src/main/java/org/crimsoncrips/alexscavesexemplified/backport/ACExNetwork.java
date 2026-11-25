package org.crimsoncrips.alexscavesexemplified.backport;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ACExNetwork {

    private static final String VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(AlexsCavesExemplified.prefix("main"), () -> VERSION, VERSION::equals, VERSION::equals);

    private static final Map<ResourceLocation, Entry<?>> ENTRIES = new HashMap<>();

    private record Entry<T extends CustomPacketPayload>(StreamCodec<? super FriendlyByteBuf, T> codec, BiConsumer<T, IPayloadContext> handler, boolean toClient) {
    }

    public record Envelope(ResourceLocation id, FriendlyByteBuf data) {
    }

    private static boolean registered;

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        CHANNEL.registerMessage(0, Envelope.class, ACExNetwork::write, ACExNetwork::read, ACExNetwork::handle);
    }

    public static <T extends CustomPacketPayload> void playToClient(CustomPacketPayload.Type<T> type, StreamCodec<? super FriendlyByteBuf, T> codec, BiConsumer<T, IPayloadContext> handler) {
        ENTRIES.put(type.id(), new Entry<>(codec, handler, true));
    }

    public static <T extends CustomPacketPayload> void playToServer(CustomPacketPayload.Type<T> type, StreamCodec<? super FriendlyByteBuf, T> codec, BiConsumer<T, IPayloadContext> handler) {
        ENTRIES.put(type.id(), new Entry<>(codec, handler, false));
    }

    @SuppressWarnings("unchecked")
    public static <T extends CustomPacketPayload> Envelope wrap(T payload) {
        ResourceLocation id = payload.type().id();
        Entry<T> entry = (Entry<T>) ENTRIES.get(id);
        if (entry == null) {
            throw new IllegalStateException("Unregistered payload " + id);
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        entry.codec().encode(buf, payload);
        return new Envelope(id, buf);
    }

    private static void write(Envelope envelope, FriendlyByteBuf buf) {
        buf.writeResourceLocation(envelope.id());
        FriendlyByteBuf data = envelope.data();
        buf.writeVarInt(data.readableBytes());
        buf.writeBytes(data, data.readerIndex(), data.readableBytes());
    }

    private static Envelope read(FriendlyByteBuf buf) {
        ResourceLocation id = buf.readResourceLocation();
        int length = buf.readVarInt();
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer(length));
        buf.readBytes(data, length);
        return new Envelope(id, data);
    }

    @SuppressWarnings("unchecked")
    private static <T extends CustomPacketPayload> void handle(Envelope envelope, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.setPacketHandled(true);
        Entry<T> entry = (Entry<T>) ENTRIES.get(envelope.id());
        if (entry == null) {
            return;
        }
        boolean toClient = context.getDirection() == NetworkDirection.PLAY_TO_CLIENT;
        if (toClient != entry.toClient()) {
            return;
        }
        T payload = entry.codec().decode(envelope.data());
        IPayloadContext payloadContext = new IPayloadContext() {
            @Override
            public Player player() {
                return toClient ? DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> ClientAccess::player) : context.getSender();
            }

            @Override
            public void enqueueWork(Runnable runnable) {
                context.enqueueWork(runnable);
            }
        };
        entry.handler().accept(payload, payloadContext);
    }

    private static class ClientAccess {
        private static Player player() {
            return Minecraft.getInstance().player;
        }
    }

    public static void sendToServer(CustomPacketPayload payload) {
        CHANNEL.sendToServer(wrap(payload));
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        CHANNEL.send(net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> player), wrap(payload));
    }
}
