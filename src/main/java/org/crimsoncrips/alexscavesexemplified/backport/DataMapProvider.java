package org.crimsoncrips.alexscavesexemplified.backport;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class DataMapProvider implements DataProvider {

    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final Map<DataMapType<?, ?>, Builder<?, ?>> builders = new LinkedHashMap<>();

    protected DataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.output = output;
        this.lookupProvider = lookupProvider;
    }

    protected abstract void gather(HolderLookup.Provider provider);

    @SuppressWarnings("unchecked")
    protected <T, R> Builder<T, R> builder(DataMapType<R, T> type) {
        return (Builder<T, R>) this.builders.computeIfAbsent(type, key -> new Builder<>(type));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.lookupProvider.thenCompose(provider -> {
            this.builders.clear();
            this.gather(provider);
            List<CompletableFuture<?>> futures = new ArrayList<>();
            for (Builder<?, ?> builder : this.builders.values()) {
                Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(builder.type.id().getNamespace()).resolve(builder.type.path());
                futures.add(DataProvider.saveStable(cache, builder.toJson(), path));
            }
            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "Data Maps";
    }

    public static class Builder<T, R> {
        private final DataMapType<R, T> type;
        private final Map<ResourceLocation, T> values = new LinkedHashMap<>();

        private Builder(DataMapType<R, T> type) {
            this.type = type;
        }

        public Builder<T, R> add(ResourceLocation id, T value, boolean replace, Object... conditions) {
            this.values.put(id, value);
            return this;
        }

        private JsonElement toJson() {
            JsonObject root = new JsonObject();
            JsonObject entries = new JsonObject();
            this.values.forEach((id, value) -> this.type.codec().encodeStart(JsonOps.INSTANCE, value).result().ifPresent(json -> entries.add(id.toString(), json)));
            root.add("values", entries);
            return root;
        }
    }
}
