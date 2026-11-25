package org.crimsoncrips.alexscavesexemplified.backport;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import javax.annotation.Nullable;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class DataMapType<R, T> {

    static final List<DataMapType<?, ?>> TYPES = new ArrayList<>();

    private final ResourceLocation id;
    private final ResourceKey<? extends Registry<R>> registryKey;
    private final Codec<T> codec;
    final boolean synced;
    private final Map<R, T> values = new IdentityHashMap<>();

    private DataMapType(ResourceLocation id, ResourceKey<? extends Registry<R>> registryKey, Codec<T> codec, boolean synced) {
        this.id = id;
        this.registryKey = registryKey;
        this.codec = codec;
        this.synced = synced;
    }

    public ResourceLocation id() {
        return this.id;
    }

    public ResourceKey<? extends Registry<R>> registryKey() {
        return this.registryKey;
    }

    public Codec<T> codec() {
        return this.codec;
    }

    @Nullable
    public T get(R object) {
        return this.values.get(object);
    }

    public String path() {
        return "data_maps/" + this.registryKey.location().getPath() + "/" + this.id.getPath() + ".json";
    }

    @SuppressWarnings("unchecked")
    private Registry<R> registry() {
        return (Registry<R>) BuiltInRegistries.REGISTRY.get(this.registryKey.location());
    }

    void load(ResourceManager manager) {
        Map<ResourceLocation, JsonElement> merged = new HashMap<>();
        for (Resource resource : manager.getResourceStack(new ResourceLocation(this.id.getNamespace(), this.path()))) {
            try (Reader reader = resource.openAsReader()) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                if (root.has("replace") && root.get("replace").getAsBoolean()) {
                    merged.clear();
                }
                JsonObject entries = root.getAsJsonObject("values");
                if (entries != null) {
                    for (Map.Entry<String, JsonElement> entry : entries.entrySet()) {
                        merged.put(new ResourceLocation(entry.getKey()), entry.getValue());
                    }
                }
            } catch (Exception ignored) {
            }
        }
        this.values.clear();
        Registry<R> registry = this.registry();
        if (registry == null) {
            return;
        }
        merged.forEach((key, json) -> {
            if (!registry.containsKey(key)) {
                return;
            }
            JsonElement value = json.isJsonObject() && json.getAsJsonObject().has("value") ? json.getAsJsonObject().get("value") : json;
            this.codec.parse(JsonOps.INSTANCE, value).result().ifPresent(parsed -> this.values.put(registry.get(key), parsed));
        });
    }

    CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        Registry<R> registry = this.registry();
        if (registry == null) {
            return tag;
        }
        this.values.forEach((key, value) -> {
            ResourceLocation location = registry.getKey(key);
            if (location != null) {
                this.codec.encodeStart(NbtOps.INSTANCE, value).result().ifPresent(encoded -> tag.put(location.toString(), encoded));
            }
        });
        return tag;
    }

    void read(CompoundTag tag) {
        this.values.clear();
        Registry<R> registry = this.registry();
        if (registry == null) {
            return;
        }
        for (String key : tag.getAllKeys()) {
            ResourceLocation location = ResourceLocation.tryParse(key);
            if (location == null || !registry.containsKey(location)) {
                continue;
            }
            Tag encoded = tag.get(key);
            this.codec.parse(NbtOps.INSTANCE, encoded).result().ifPresent(parsed -> this.values.put(registry.get(location), parsed));
        }
    }

    public static <R, T> Builder<R, T> builder(ResourceLocation id, ResourceKey<? extends Registry<R>> registryKey, Codec<T> codec) {
        return new Builder<>(id, registryKey, codec);
    }

    public static class Builder<R, T> {
        private final ResourceLocation id;
        private final ResourceKey<? extends Registry<R>> registryKey;
        private final Codec<T> codec;
        private boolean synced;

        private Builder(ResourceLocation id, ResourceKey<? extends Registry<R>> registryKey, Codec<T> codec) {
            this.id = id;
            this.registryKey = registryKey;
            this.codec = codec;
        }

        public Builder<R, T> synced(Codec<T> networkCodec, boolean mandatory) {
            this.synced = true;
            return this;
        }

        public DataMapType<R, T> build() {
            DataMapType<R, T> type = new DataMapType<>(this.id, this.registryKey, this.codec, this.synced);
            TYPES.add(type);
            return type;
        }
    }
}
