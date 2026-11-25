package org.crimsoncrips.alexscavesexemplified.backport;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class ByteBufCodecs {

    public static FriendlyByteBuf friendly(ByteBuf buf) {
        return buf instanceof FriendlyByteBuf friendly ? friendly : new FriendlyByteBuf(buf);
    }

    public static final StreamCodec<ByteBuf, Boolean> BOOL = StreamCodec.of(ByteBuf::writeBoolean, ByteBuf::readBoolean);
    public static final StreamCodec<ByteBuf, Byte> BYTE = StreamCodec.of((buf, value) -> buf.writeByte(value), ByteBuf::readByte);
    public static final StreamCodec<ByteBuf, Short> SHORT = StreamCodec.of((buf, value) -> buf.writeShort(value), ByteBuf::readShort);
    public static final StreamCodec<ByteBuf, Integer> INT = StreamCodec.of(ByteBuf::writeInt, ByteBuf::readInt);
    public static final StreamCodec<ByteBuf, Integer> VAR_INT = StreamCodec.of((buf, value) -> friendly(buf).writeVarInt(value), buf -> friendly(buf).readVarInt());
    public static final StreamCodec<ByteBuf, Long> LONG = StreamCodec.of(ByteBuf::writeLong, ByteBuf::readLong);
    public static final StreamCodec<ByteBuf, Long> VAR_LONG = StreamCodec.of((buf, value) -> friendly(buf).writeVarLong(value), buf -> friendly(buf).readVarLong());
    public static final StreamCodec<ByteBuf, Float> FLOAT = StreamCodec.of(ByteBuf::writeFloat, ByteBuf::readFloat);
    public static final StreamCodec<ByteBuf, Double> DOUBLE = StreamCodec.of(ByteBuf::writeDouble, ByteBuf::readDouble);
    public static final StreamCodec<ByteBuf, String> STRING_UTF8 = StreamCodec.of((buf, value) -> friendly(buf).writeUtf(value), buf -> friendly(buf).readUtf());
    public static final StreamCodec<ByteBuf, CompoundTag> COMPOUND_TAG = StreamCodec.of((buf, value) -> friendly(buf).writeNbt(value), buf -> friendly(buf).readNbt());
    public static final StreamCodec<ByteBuf, UUID> UUID = StreamCodec.of((buf, value) -> friendly(buf).writeUUID(value), buf -> friendly(buf).readUUID());
    public static final StreamCodec<ByteBuf, ResourceLocation> RESOURCE_LOCATION = StreamCodec.of((buf, value) -> friendly(buf).writeResourceLocation(value), buf -> friendly(buf).readResourceLocation());
    public static final StreamCodec<ByteBuf, Component> COMPONENT = StreamCodec.of((buf, value) -> friendly(buf).writeComponent(value), buf -> friendly(buf).readComponent());
    public static final StreamCodec<ByteBuf, Vec3> VEC3 = StreamCodec.of((buf, value) -> {
        buf.writeDouble(value.x);
        buf.writeDouble(value.y);
        buf.writeDouble(value.z);
    }, buf -> new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));

    public static <T> StreamCodec<ByteBuf, T> idMapper(IntFunction<T> byId, ToIntFunction<T> toId) {
        return StreamCodec.of((buf, value) -> friendly(buf).writeVarInt(toId.applyAsInt(value)), buf -> byId.apply(friendly(buf).readVarInt()));
    }

    public static <B extends ByteBuf, V> StreamCodec.CodecOperation<B, V, List<V>> list() {
        return codec -> StreamCodec.of((buf, list) -> {
            friendly(buf).writeVarInt(list.size());
            for (V value : list) {
                codec.encode(buf, value);
            }
        }, buf -> {
            int size = friendly(buf).readVarInt();
            List<V> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                list.add(codec.decode(buf));
            }
            return list;
        });
    }
}
