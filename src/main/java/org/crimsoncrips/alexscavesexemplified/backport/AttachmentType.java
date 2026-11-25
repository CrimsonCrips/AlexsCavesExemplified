package org.crimsoncrips.alexscavesexemplified.backport;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

public class AttachmentType<T> implements Supplier<AttachmentType<T>> {

    ResourceLocation id;
    final Function<IAttachmentHolder, T> defaultValue;
    @Nullable
    final Serializer<T> serializer;
    @Nullable
    final StreamCodec<? super FriendlyByteBuf, T> syncCodec;
    @Nullable
    final BiPredicate<IAttachmentHolder, ServerPlayer> syncPredicate;
    final boolean copyOnDeath;

    private AttachmentType(Builder<T> builder) {
        this.defaultValue = builder.defaultValue;
        this.serializer = builder.serializer;
        this.syncCodec = builder.syncCodec;
        this.syncPredicate = builder.syncPredicate;
        this.copyOnDeath = builder.copyOnDeath;
    }

    @Override
    public AttachmentType<T> get() {
        return this;
    }

    public ResourceLocation id() {
        return this.id;
    }

    public boolean isSerializable() {
        return this.serializer != null;
    }

    public boolean isSynced() {
        return this.syncCodec != null;
    }

    T createDefault(IAttachmentHolder holder) {
        return this.defaultValue.apply(holder);
    }

    public static <T> Builder<T> builder(Supplier<T> defaultValue) {
        return new Builder<>(holder -> defaultValue.get());
    }

    public static <T> Builder<T> builder(Function<IAttachmentHolder, T> defaultValue) {
        return new Builder<>(defaultValue);
    }

    public static <S extends INBTSerializable<CompoundTag>> Builder<S> serializable(Function<IAttachmentHolder, S> defaultValue) {
        Builder<S> builder = new Builder<>(defaultValue);
        builder.serializer = new Serializer<>() {
            @Override
            public Tag write(S value) {
                return value.serializeNBT();
            }

            @Override
            public S read(IAttachmentHolder holder, Tag tag) {
                S value = defaultValue.apply(holder);
                if (tag instanceof CompoundTag compound) {
                    value.deserializeNBT(compound);
                }
                return value;
            }
        };
        return builder;
    }

    public static <S extends INBTSerializable<CompoundTag>> Builder<S> serializable(Supplier<S> defaultValue) {
        return serializable(holder -> defaultValue.get());
    }

    interface Serializer<T> {
        @Nullable
        Tag write(T value);

        @Nullable
        T read(IAttachmentHolder holder, Tag tag);
    }

    public static class Builder<T> {
        private final Function<IAttachmentHolder, T> defaultValue;
        private Serializer<T> serializer;
        private StreamCodec<? super FriendlyByteBuf, T> syncCodec;
        private BiPredicate<IAttachmentHolder, ServerPlayer> syncPredicate;
        private boolean copyOnDeath;

        private Builder(Function<IAttachmentHolder, T> defaultValue) {
            this.defaultValue = defaultValue;
        }

        public Builder<T> serialize(Codec<T> codec) {
            this.serializer = new Serializer<>() {
                @Override
                public Tag write(T value) {
                    return codec.encodeStart(NbtOps.INSTANCE, value).result().orElse(null);
                }

                @Override
                public T read(IAttachmentHolder holder, Tag tag) {
                    return codec.parse(NbtOps.INSTANCE, tag).result().orElse(null);
                }
            };
            return this;
        }

        public Builder<T> sync(StreamCodec<? super FriendlyByteBuf, T> codec) {
            this.syncCodec = codec;
            return this;
        }

        public Builder<T> sync(BiPredicate<IAttachmentHolder, ServerPlayer> predicate, StreamCodec<? super FriendlyByteBuf, T> codec) {
            this.syncPredicate = predicate;
            this.syncCodec = codec;
            return this;
        }

        public Builder<T> copyOnDeath() {
            this.copyOnDeath = true;
            return this;
        }

        public AttachmentType<T> build() {
            return new AttachmentType<>(this);
        }
    }
}
