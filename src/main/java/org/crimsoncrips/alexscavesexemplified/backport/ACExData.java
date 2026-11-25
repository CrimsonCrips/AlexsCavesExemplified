package org.crimsoncrips.alexscavesexemplified.backport;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.fml.DistExecutor;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ACExData {

    public static final String TAG = AlexsCavesExemplified.MODID + ":attachments";

    private static final Map<ResourceLocation, AttachmentType<?>> TYPES = new HashMap<>();

    public static <T> AttachmentType<T> register(String name, Supplier<AttachmentType<T>> factory) {
        AttachmentType<T> type = factory.get();
        type.id = AlexsCavesExemplified.prefix(name);
        TYPES.put(type.id, type);
        return type;
    }

    private static AttachmentData data(Object holder) {
        return ((IAttachmentHolder) holder).acexAttachments();
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Object holder, Supplier<AttachmentType<T>> supplier) {
        AttachmentType<T> type = supplier.get();
        AttachmentData data = data(holder);
        Object value = data.values.get(type);
        if (value == null && !data.values.containsKey(type)) {
            value = type.createDefault((IAttachmentHolder) holder);
            data.values.put(type, value);
        }
        return (T) value;
    }

    public static boolean has(Object holder, Supplier<? extends AttachmentType<?>> supplier) {
        return data(holder).values.containsKey(supplier.get());
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T set(Object holder, Supplier<AttachmentType<T>> supplier, T value) {
        AttachmentType<T> type = supplier.get();
        T previous = (T) data(holder).values.put(type, value);
        sync(holder, type, value);
        return previous;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T remove(Object holder, Supplier<AttachmentType<T>> supplier) {
        AttachmentType<T> type = supplier.get();
        T previous = (T) data(holder).values.remove(type);
        if (type.isSynced()) {
            sync(holder, type, get(holder, type));
            data(holder).values.remove(type);
        }
        return previous;
    }

    public static void syncData(Object holder, Supplier<? extends AttachmentType<?>> supplier) {
        AttachmentType<?> type = supplier.get();
        if (has(holder, type)) {
            syncRaw(holder, type);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void syncRaw(Object holder, AttachmentType<T> type) {
        sync(holder, type, (T) data(holder).values.get(type));
    }

    public static void save(Object holder, CompoundTag tag) {
        AttachmentData data = data(holder);
        if (data.values.isEmpty()) {
            return;
        }
        CompoundTag attachments = new CompoundTag();
        data.values.forEach((type, value) -> {
            Tag written = write(type, value);
            if (written != null) {
                attachments.put(type.id.toString(), written);
            }
        });
        if (!attachments.isEmpty()) {
            tag.put(TAG, attachments);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Tag write(AttachmentType<T> type, Object value) {
        if (type.serializer == null || value == null) {
            return null;
        }
        return type.serializer.write((T) value);
    }

    public static void load(Object holder, CompoundTag tag) {
        if (!tag.contains(TAG, Tag.TAG_COMPOUND)) {
            return;
        }
        CompoundTag attachments = tag.getCompound(TAG);
        AttachmentData data = data(holder);
        for (String key : attachments.getAllKeys()) {
            ResourceLocation id = ResourceLocation.tryParse(key);
            AttachmentType<?> type = id == null ? null : TYPES.get(id);
            if (type == null || type.serializer == null) {
                continue;
            }
            Object value = type.serializer.read((IAttachmentHolder) holder, attachments.get(key));
            if (value != null) {
                data.values.put(type, value);
            }
        }
    }

    private static <T> void sync(Object holder, AttachmentType<T> type, T value) {
        if (type.syncCodec == null || value == null) {
            return;
        }
        if (holder instanceof Entity entity) {
            if (entity.level().isClientSide) {
                return;
            }
            ACExAttachmentSyncPacket packet = ACExAttachmentSyncPacket.forEntity(entity.getId(), type, value);
            if (type.syncPredicate != null) {
                if (entity instanceof ServerPlayer player && type.syncPredicate.test((IAttachmentHolder) holder, player)) {
                    PacketDistributor.sendToPlayer(player, packet);
                }
            } else {
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, packet);
            }
        } else if (holder instanceof BlockEntity blockEntity) {
            Level level = blockEntity.getLevel();
            if (level == null || level.isClientSide) {
                return;
            }
            ACExAttachmentSyncPacket packet = ACExAttachmentSyncPacket.forBlock(blockEntity.getBlockPos(), type, value);
            LevelChunk chunk = level.getChunkAt(blockEntity.getBlockPos());
            ACExNetwork.CHANNEL.send(net.minecraftforge.network.PacketDistributor.TRACKING_CHUNK.with(() -> chunk), ACExNetwork.wrap(packet));
        }
    }

    private static void sendAll(Object holder, ServerPlayer player) {
        AttachmentData data = data(holder);
        data.values.forEach((type, value) -> sendTo(holder, type, value, player));
    }

    @SuppressWarnings("unchecked")
    private static <T> void sendTo(Object holder, AttachmentType<T> type, Object value, ServerPlayer player) {
        if (type.syncCodec == null || value == null) {
            return;
        }
        if (type.syncPredicate != null && !type.syncPredicate.test((IAttachmentHolder) holder, player)) {
            return;
        }
        if (holder instanceof Entity entity) {
            PacketDistributor.sendToPlayer(player, ACExAttachmentSyncPacket.forEntity(entity.getId(), type, (T) value));
        } else if (holder instanceof BlockEntity blockEntity) {
            PacketDistributor.sendToPlayer(player, ACExAttachmentSyncPacket.forBlock(blockEntity.getBlockPos(), type, (T) value));
        }
    }

    static <T> FriendlyByteBuf encode(AttachmentType<T> type, T value) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        type.syncCodec.encode(buf, value);
        return buf;
    }

    static void receive(ACExAttachmentSyncPacket packet) {
        AttachmentType<?> type = TYPES.get(packet.typeId());
        if (type == null || type.syncCodec == null) {
            return;
        }
        Level level = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> ClientAccess::level);
        if (level == null) {
            return;
        }
        Object holder = packet.entity() ? level.getEntity(packet.entityId()) : level.getBlockEntity(packet.pos());
        if (holder == null) {
            return;
        }
        Object value = type.syncCodec.decode(new FriendlyByteBuf(Unpooled.wrappedBuffer(packet.data())));
        data(holder).values.put(type, value);
    }

    private static class ClientAccess {
        private static Level level() {
            return Minecraft.getInstance().level;
        }
    }

    public static void copy(Object from, Object to, boolean death) {
        AttachmentData source = data(from);
        AttachmentData target = data(to);
        source.values.forEach((type, value) -> {
            if (death ? type.copyOnDeath : type.serializer != null) {
                Tag written = write(type, value);
                Object copied = written != null ? type.serializer.read((IAttachmentHolder) to, written) : value;
                target.values.put(type, copied);
            }
        });
    }

    public static void init() {
        ACExNetwork.playToClient(ACExAttachmentSyncPacket.TYPE, ACExAttachmentSyncPacket.STREAM_CODEC, (packet, context) -> context.enqueueWork(() -> receive(packet)));
        MinecraftForge.EVENT_BUS.addListener(ACExData::onStartTracking);
        MinecraftForge.EVENT_BUS.addListener(ACExData::onClone);
        MinecraftForge.EVENT_BUS.addListener(ACExData::onLogin);
        MinecraftForge.EVENT_BUS.addListener(ACExData::onRespawn);
        MinecraftForge.EVENT_BUS.addListener(ACExData::onChangeDimension);
        MinecraftForge.EVENT_BUS.addListener(ACExData::onChunkWatch);
    }

    private static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            sendAll(event.getTarget(), player);
        }
    }

    private static void onClone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        copy(event.getOriginal(), event.getEntity(), event.isWasDeath());
        event.getOriginal().invalidateCaps();
    }

    private static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            sendAll(player, player);
        }
    }

    private static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            sendAll(player, player);
        }
    }

    private static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            sendAll(player, player);
        }
    }

    private static void onChunkWatch(ChunkWatchEvent.Watch event) {
        ServerLevel level = event.getLevel();
        ChunkPos pos = event.getPos();
        LevelChunk chunk = level.getChunkSource().getChunkNow(pos.x, pos.z);
        if (chunk == null) {
            return;
        }
        ServerPlayer player = event.getPlayer();
        for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
            if (!data(blockEntity).values.isEmpty()) {
                sendAll(blockEntity, player);
            }
        }
    }
}
