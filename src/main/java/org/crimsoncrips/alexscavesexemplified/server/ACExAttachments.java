package org.crimsoncrips.alexscavesexemplified.server;

import net.minecraft.world.phys.Vec3;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.commons.lang3.mutable.MutableInt;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExGammaAttacks;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;

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

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> ARMED = DEF_REG.register("armed", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Vec3>> PELLET_ORIGIN = DEF_REG.register("pellet_origin", () -> AttachmentType.builder(() -> Vec3.ZERO).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> RADIANT_METER = DEF_REG.register("radiant_meter", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).sync((holder, player) -> holder == player, ByteBufCodecs.VAR_INT).copyOnDeath().build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> SEEKING_SPEAR = DEF_REG.register("seeking_spear", () -> AttachmentType.builder(() -> false).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ACExSugarStaffAbilities.GumballShot>> GUMBALL_SHOT = DEF_REG.register("gumball_shot", () -> AttachmentType.builder(() -> new ACExSugarStaffAbilities.GumballShot()).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ACExSugarStaffAbilities.PepperCrush>> PEPPER_CRUSH = DEF_REG.register("pepper_crush", () -> AttachmentType.builder(() -> new ACExSugarStaffAbilities.PepperCrush()).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ACExSugarStaffAbilities.WormScript>> WORM_SCRIPT = DEF_REG.register("worm_script", () -> AttachmentType.builder(() -> new ACExSugarStaffAbilities.WormScript()).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ACExSugarStaffAbilities.GummySwarmScript>> GUMMY_SWARM = DEF_REG.register("gummy_swarm", () -> AttachmentType.builder(() -> new ACExSugarStaffAbilities.GummySwarmScript()).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ACExSugarStaffAbilities.WirmOrbit>> WIRM_ORBIT = DEF_REG.register("wirm_orbit", () -> AttachmentType.builder(() -> new ACExSugarStaffAbilities.WirmOrbit()).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> SCRIPT_LIVE = DEF_REG.register("script_live", () -> AttachmentType.builder(() -> false).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> STAFF_PEPPERMINT = DEF_REG.register("staff_peppermint", () -> AttachmentType.builder(() -> false).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> SCRIPTED_WORM = DEF_REG.register("scripted_worm", () -> AttachmentType.builder(() -> false).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SHAKE_OWNER = DEF_REG.register("shake_owner", () -> AttachmentType.builder(() -> -1).sync(ByteBufCodecs.VAR_INT).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Float>> PEPPERMINT_SCALE = DEF_REG.register("peppermint_scale", () -> AttachmentType.builder(() -> 1.0F).sync(ByteBufCodecs.FLOAT).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ACExSugarStaffAbilities.SweetRelease>> SWEET_RELEASE = DEF_REG.register("sweet_release", () -> AttachmentType.builder(() -> new ACExSugarStaffAbilities.SweetRelease()).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> GAMMA = DEF_REG.register("gamma", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> SECOND_PHASE = DEF_REG.register("second_phase", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> ANIMATION_BEAMING = DEF_REG.register("animation_beaming", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> MAGNETIZING = DEF_REG.register("magnetizing", () -> AttachmentType.builder(() -> true).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> LAUNCHED_TARGET_ID = DEF_REG.register("launched_target_id", () -> AttachmentType.builder(() -> -1).sync(ByteBufCodecs.VAR_INT).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Float>> SPIN_ANGLE = DEF_REG.register("spin_angle", () -> AttachmentType.<Float>builder(holder -> holder instanceof Entity entity ? (float) entity.getRandom().nextInt(0, 361) : 0F).sync(ByteBufCodecs.FLOAT).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> DRAGGED_PROTECTION = DEF_REG.register("dragged_protection", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> DARK_ARROW_KNOCKBACK = DEF_REG.register("dark_arrow_knockback", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> FROST_RADIANT = DEF_REG.register("frost_radiant", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> STAFF_SPEAR = DEF_REG.register("staff_spear", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> STAFF_HEX = DEF_REG.register("staff_hex", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> PEPPER_RADIANT = DEF_REG.register("pepper_radiant", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> PEPPER_LOCK = DEF_REG.register("pepper_lock", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> PEPPER_MASSIVE = DEF_REG.register("pepper_massive", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> PEPPER_CHIPPER = DEF_REG.register("pepper_chipper", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> SACRIFICE_GIVEN = DEF_REG.register("sacrifice_given", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> PROJECTION_SCALE = DEF_REG.register("projection_scale", () -> AttachmentType.builder(() -> 1).serialize(Codec.INT).sync(ByteBufCodecs.VAR_INT).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<MutableInt>> TESLA_CHARGE = DEF_REG.register("tesla_charge", () -> AttachmentType.builder(() -> new MutableInt()).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> OVERDRIVED = DEF_REG.register("overdrived", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ACExSubmarineRemote.RemoteInput>> SUBMARINE_REMOTE_INPUT = DEF_REG.register("submarine_remote_input", () -> AttachmentType.builder(() -> ACExSubmarineRemote.RemoteInput.NONE).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> STOP_SEEKING = DEF_REG.register("stop_seeking", () -> AttachmentType.builder(() -> false).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ACExSugarStaffAbilities.AbilityCooldowns>> ABILITY_COOLDOWNS = DEF_REG.register("ability_cooldowns", () -> AttachmentType.builder(() -> ACExSugarStaffAbilities.AbilityCooldowns.NONE).serialize(ACExSugarStaffAbilities.AbilityCooldowns.CODEC).sync((holder, player) -> holder == player, ACExSugarStaffAbilities.AbilityCooldowns.STREAM_CODEC).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ACExSugarStaffAbilities.AbilityCooldowns>> GAMMA_COOLDOWNS = DEF_REG.register("gamma_cooldowns", () -> AttachmentType.builder(() -> ACExSugarStaffAbilities.AbilityCooldowns.NONE).serialize(ACExSugarStaffAbilities.AbilityCooldowns.CODEC).sync(ACExSugarStaffAbilities.AbilityCooldowns.STREAM_CODEC).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ACExGammaAttacks.BeamMerge>> GAMMA_BEAM_MERGE = DEF_REG.register("gamma_beam_merge", () -> AttachmentType.builder(() -> ACExGammaAttacks.BeamMerge.NONE).sync(ACExGammaAttacks.BeamMerge.STREAM_CODEC).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> GAMMA_LEAPING = DEF_REG.register("gamma_leaping", () -> AttachmentType.builder(() -> false).sync(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> GAMMA_RIDER_REQUEST = DEF_REG.register("gamma_rider_request", () -> AttachmentType.builder(() -> -1).build());

    public static <T> void set(IAttachmentHolder holder, Supplier<AttachmentType<T>> type, T value) {
        if (!holder.hasData(type) || !Objects.equals(holder.getData(type), value)) {
            holder.setData(type, value);
        }
    }
}
