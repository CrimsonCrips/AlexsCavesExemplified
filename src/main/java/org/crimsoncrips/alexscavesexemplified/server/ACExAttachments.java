package org.crimsoncrips.alexscavesexemplified.server;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import java.util.Objects;
import java.util.function.Supplier;

public class ACExAttachments {

    public static final DeferredRegister<AttachmentType<?>> DEF_REG = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, AlexsCavesExemplified.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> SNIFFED = DEF_REG.register("sniffed", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SWEETS = DEF_REG.register("sweets", () -> AttachmentType.builder(() -> 0).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> DEFUSED = DEF_REG.register("defused", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<String>> MINE_GUARDIAN_OWNER = DEF_REG.register("mine_guardian_owner", () -> AttachmentType.builder(() -> "-1").serialize(Codec.STRING).sync(ByteBufCodecs.STRING_UTF8).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> MINE_GUARDIAN_VARIANT = DEF_REG.register("mine_guardian_variant", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).sync(ByteBufCodecs.VAR_INT).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SELF_DESTRUCT_TIME = DEF_REG.register("self_destruct_time", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).sync(ByteBufCodecs.VAR_INT).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> RIPPED_HEART = DEF_REG.register("ripped_heart", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).sync(ByteBufCodecs.VAR_INT).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> RIPPED_HEART_DELAY = DEF_REG.register("ripped_heart_delay", () -> AttachmentType.builder(() -> 0).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> GAMMA = DEF_REG.register("gamma", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> SECOND_PHASE = DEF_REG.register("second_phase", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> ANIMATION_BEAMING = DEF_REG.register("animation_beaming", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> MAGNETIZING = DEF_REG.register("magnetizing", () -> AttachmentType.builder(() -> true).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> LAUNCHED_TARGET_ID = DEF_REG.register("launched_target_id", () -> AttachmentType.builder(() -> -1).sync(ByteBufCodecs.VAR_INT).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Float>> SPIN_ANGLE = DEF_REG.register("spin_angle", () -> AttachmentType.<Float>builder(holder -> holder instanceof Entity entity ? (float) entity.getRandom().nextInt(0, 361) : 0F).sync(ByteBufCodecs.FLOAT).build());

    public static <T> void set(IAttachmentHolder holder, Supplier<AttachmentType<T>> type, T value) {
        if (!holder.hasData(type) || !Objects.equals(holder.getData(type), value)) {
            holder.setData(type, value);
        }
    }
}
