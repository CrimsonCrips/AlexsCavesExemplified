package org.crimsoncrips.alexscavesexemplified.backport;

import com.mojang.serialization.Codec;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class DataComponentType<T> implements Supplier<DataComponentType<T>> {

    private String key;
    @Nullable
    private final Codec<T> codec;
    @Nullable
    private final StreamCodec<? super FriendlyByteBuf, T> streamCodec;

    private DataComponentType(Builder<T> builder) {
        this.codec = builder.codec;
        this.streamCodec = builder.streamCodec;
    }

    public static <T> DataComponentType<T> register(String name, Supplier<DataComponentType<T>> factory) {
        DataComponentType<T> type = factory.get();
        type.key = AlexsCavesExemplified.MODID + ":" + name;
        return type;
    }

    @Override
    public DataComponentType<T> get() {
        return this;
    }

    @Nullable
    public T get(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(this.key)) {
            return null;
        }
        return this.read(tag.get(this.key));
    }

    public T getOrDefault(ItemStack stack, T fallback) {
        T value = this.get(stack);
        return value == null ? fallback : value;
    }

    public boolean has(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(this.key);
    }

    @Nullable
    public T set(ItemStack stack, @Nullable T value) {
        T previous = this.get(stack);
        if (value == null) {
            this.remove(stack);
            return previous;
        }
        Tag written = this.write(value);
        if (written != null) {
            stack.getOrCreateTag().put(this.key, written);
        }
        return previous;
    }

    @Nullable
    public T remove(ItemStack stack) {
        T previous = this.get(stack);
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            tag.remove(this.key);
            if (tag.isEmpty()) {
                stack.setTag(null);
            }
        }
        return previous;
    }

    public T update(ItemStack stack, T fallback, UnaryOperator<T> updater) {
        T value = updater.apply(this.getOrDefault(stack, fallback));
        this.set(stack, value);
        return value;
    }

    public boolean matches(ItemStack stack, T value) {
        return Objects.equals(this.get(stack), value);
    }

    @Nullable
    private Tag write(T value) {
        if (this.codec != null) {
            return this.codec.encodeStart(NbtOps.INSTANCE, value).result().orElse(null);
        }
        if (this.streamCodec != null) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            this.streamCodec.encode(buf, value);
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            return new ByteArrayTag(bytes);
        }
        return null;
    }

    @Nullable
    private T read(Tag tag) {
        if (this.codec != null) {
            return this.codec.parse(NbtOps.INSTANCE, tag).result().orElse(null);
        }
        if (this.streamCodec != null && tag instanceof ByteArrayTag bytes) {
            return this.streamCodec.decode(new FriendlyByteBuf(Unpooled.wrappedBuffer(bytes.getAsByteArray())));
        }
        return null;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private Codec<T> codec;
        private StreamCodec<? super FriendlyByteBuf, T> streamCodec;

        public Builder<T> persistent(Codec<T> codec) {
            this.codec = codec;
            return this;
        }

        public Builder<T> networkSynchronized(StreamCodec<? super FriendlyByteBuf, T> streamCodec) {
            this.streamCodec = streamCodec;
            return this;
        }

        public DataComponentType<T> build() {
            return new DataComponentType<>(this);
        }
    }
}
