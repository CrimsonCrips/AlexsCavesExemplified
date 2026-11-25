package org.crimsoncrips.alexscavesexemplified.server;

import io.netty.buffer.ByteBuf;
import org.crimsoncrips.alexscavesexemplified.misc.ACExQuarryUpgrades;
import net.minecraft.world.phys.Vec3;
import com.mojang.serialization.Codec;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.backport.AttachmentType;
import org.crimsoncrips.alexscavesexemplified.backport.ByteBufCodecs;
import org.crimsoncrips.alexscavesexemplified.backport.StreamCodec;
import net.minecraft.world.entity.Entity;
import org.crimsoncrips.alexscavesexemplified.misc.ACExRaygunBlasting;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExGammaAttacks;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;

import java.util.Objects;
import java.util.function.Supplier;

public class ACExAttachments {

    public static final AttachmentType<Boolean> SNIFFED = ACExData.register("sniffed", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());


    public static final AttachmentType<Boolean> DEFUSED = ACExData.register("defused", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final AttachmentType<String> MINE_GUARDIAN_OWNER = ACExData.register("mine_guardian_owner", () -> AttachmentType.builder(() -> "-1").serialize(Codec.STRING).sync(ByteBufCodecs.STRING_UTF8).build());

    public static final AttachmentType<Integer> MINE_GUARDIAN_VARIANT = ACExData.register("mine_guardian_variant", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).sync(ByteBufCodecs.VAR_INT).build());

    public static final AttachmentType<Integer> SELF_DESTRUCT_TIME = ACExData.register("self_destruct_time", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).sync(ByteBufCodecs.VAR_INT).build());

    public static final AttachmentType<Integer> RIPPED_HEART = ACExData.register("ripped_heart", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).sync(ByteBufCodecs.VAR_INT).build());

    public static final AttachmentType<Integer> RIPPED_HEART_DELAY = ACExData.register("ripped_heart_delay", () -> AttachmentType.builder(() -> 0).build());

    public static final AttachmentType<Boolean> ARMED = ACExData.register("armed", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final AttachmentType<Vec3> PELLET_ORIGIN = ACExData.register("pellet_origin", () -> AttachmentType.builder(() -> Vec3.ZERO).build());

    public static final AttachmentType<Integer> RADIANT_METER = ACExData.register("radiant_meter", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).sync((holder, player) -> holder == player, ByteBufCodecs.VAR_INT).copyOnDeath().build());

    public static final AttachmentType<Boolean> SEEKING_SPEAR = ACExData.register("seeking_spear", () -> AttachmentType.builder(() -> false).build());

    public static final AttachmentType<ACExSugarStaffAbilities.GumballShot> GUMBALL_SHOT = ACExData.register("gumball_shot", () -> AttachmentType.builder(() -> new ACExSugarStaffAbilities.GumballShot()).build());

    public static final AttachmentType<ACExRaygunBlasting.BreakProgress> RAYGUN_BREAK = ACExData.register("raygun_break", () -> AttachmentType.builder(() -> new ACExRaygunBlasting.BreakProgress()).build());
    public static final AttachmentType<ACExSugarStaffAbilities.PepperCrush> PEPPER_CRUSH = ACExData.register("pepper_crush", () -> AttachmentType.builder(() -> new ACExSugarStaffAbilities.PepperCrush()).build());

    public static final AttachmentType<ACExSugarStaffAbilities.WormScript> WORM_SCRIPT = ACExData.register("worm_script", () -> AttachmentType.builder(() -> new ACExSugarStaffAbilities.WormScript()).build());

    public static final AttachmentType<ACExSugarStaffAbilities.GummySwarmScript> GUMMY_SWARM = ACExData.register("gummy_swarm", () -> AttachmentType.builder(() -> new ACExSugarStaffAbilities.GummySwarmScript()).build());

    public static final AttachmentType<ACExSugarStaffAbilities.WirmOrbit> WIRM_ORBIT = ACExData.register("wirm_orbit", () -> AttachmentType.builder(() -> new ACExSugarStaffAbilities.WirmOrbit()).build());

    public static final AttachmentType<Boolean> SCRIPT_LIVE = ACExData.register("script_live", () -> AttachmentType.builder(() -> false).build());

    public static final AttachmentType<Boolean> STAFF_PEPPERMINT = ACExData.register("staff_peppermint", () -> AttachmentType.builder(() -> false).sync(ByteBufCodecs.BOOL).build());

    public static final AttachmentType<Boolean> SCRIPTED_WORM = ACExData.register("scripted_worm", () -> AttachmentType.builder(() -> false).sync(ByteBufCodecs.BOOL).build());

    public static final AttachmentType<Integer> SHAKE_OWNER = ACExData.register("shake_owner", () -> AttachmentType.builder(() -> -1).sync(ByteBufCodecs.VAR_INT).build());

    public static final AttachmentType<Float> PEPPERMINT_SCALE = ACExData.register("peppermint_scale", () -> AttachmentType.builder(() -> 1.0F).sync(ByteBufCodecs.FLOAT).build());

    public static final AttachmentType<ACExSugarStaffAbilities.SweetRelease> SWEET_RELEASE = ACExData.register("sweet_release", () -> AttachmentType.builder(() -> new ACExSugarStaffAbilities.SweetRelease()).build());

    public static final AttachmentType<Boolean> GAMMA = ACExData.register("gamma", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final AttachmentType<Boolean> SECOND_PHASE = ACExData.register("second_phase", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final AttachmentType<Boolean> ANIMATION_BEAMING = ACExData.register("animation_beaming", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final AttachmentType<Boolean> MAGNETIZING = ACExData.register("magnetizing", () -> AttachmentType.builder(() -> true).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final AttachmentType<Integer> LAUNCHED_TARGET_ID = ACExData.register("launched_target_id", () -> AttachmentType.builder(() -> -1).sync(ByteBufCodecs.VAR_INT).build());

    public static final AttachmentType<Integer> MAGNERIP_SLOT = ACExData.register("magnerip_slot", () -> AttachmentType.builder(() -> -1).sync(ByteBufCodecs.VAR_INT).build());
    public static final AttachmentType<Float> MAGNERIP_PROGRESS = ACExData.register("magnerip_progress", () -> AttachmentType.builder(() -> 0.0F).sync(ByteBufCodecs.FLOAT).build());
    public static final AttachmentType<Long> MAGNERIP_PULLED = ACExData.register("magnerip_pulled", () -> AttachmentType.builder(() -> 0L).build());
    public static final AttachmentType<Integer> SHIELD_HOLDER = ACExData.register("shield_holder", () -> AttachmentType.builder(() -> -1).sync(ByteBufCodecs.VAR_INT).build());

    public static final AttachmentType<Float> SPIN_ANGLE = ACExData.register("spin_angle", () -> AttachmentType.<Float>builder(holder -> holder instanceof Entity entity ? (float) entity.level().getRandom().nextInt(0, 361) : 0F).sync(ByteBufCodecs.FLOAT).build());

    public static final AttachmentType<Boolean> DRAGGED_PROTECTION = ACExData.register("dragged_protection", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final AttachmentType<Integer> DARK_ARROW_KNOCKBACK = ACExData.register("dark_arrow_knockback", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());

    public static final AttachmentType<Boolean> FROST_RADIANT = ACExData.register("frost_radiant", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final AttachmentType<Boolean> STAFF_SPEAR = ACExData.register("staff_spear", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final AttachmentType<Boolean> STAFF_HEX = ACExData.register("staff_hex", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final AttachmentType<Boolean> PEPPER_RADIANT = ACExData.register("pepper_radiant", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final AttachmentType<Boolean> PEPPER_LOCK = ACExData.register("pepper_lock", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final AttachmentType<Boolean> PEPPER_MASSIVE = ACExData.register("pepper_massive", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final AttachmentType<Boolean> PEPPER_CHIPPER = ACExData.register("pepper_chipper", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final AttachmentType<Float> SUB_ALERT = ACExData.register("sub_alert", () -> AttachmentType.builder(() -> 0.0F).sync(ByteBufCodecs.FLOAT).build());
    public static final AttachmentType<Integer> SUB_ALERT_TARGET = ACExData.register("sub_alert_target", () -> AttachmentType.builder(() -> -1).sync(ByteBufCodecs.VAR_INT).build());
    public static final AttachmentType<Integer> SUBMARINE_DRIVER = ACExData.register("submarine_driver", () -> AttachmentType.builder(() -> -1).sync(ByteBufCodecs.VAR_INT).build());
    public static final AttachmentType<Integer> REMOTE_SUBMARINE = ACExData.register("remote_submarine", () -> AttachmentType.builder(() -> -1).build());
    public static final AttachmentType<Boolean> SHIELD_BURST = ACExData.register("shield_burst", () -> AttachmentType.builder(() -> false).build());
    public static final AttachmentType<Float> SPEAR_FREEZE = ACExData.register("spear_freeze", () -> AttachmentType.builder(() -> 1.0F).build());
    public static final AttachmentType<Boolean> CHAIN_DETONATED = ACExData.register("chain_detonated", () -> AttachmentType.builder(() -> false).build());
    public static final AttachmentType<Integer> GUMMY_SLEEP_TOTAL = ACExData.register("gummy_sleep_total", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());
    public static final AttachmentType<Boolean> SACRIFICE_GIVEN = ACExData.register("sacrifice_given", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final AttachmentType<Float> SECTUMSEMPRA_CHARGE = ACExData.register("sectumsempra_charge", () -> AttachmentType.builder(() -> 0.0F).serialize(Codec.FLOAT).sync(ByteBufCodecs.FLOAT).copyOnDeath().build());
    public static final AttachmentType<Boolean> SECTUMSEMPRA_LOCK = ACExData.register("sectumsempra_lock", () -> AttachmentType.builder(() -> false).sync(ByteBufCodecs.BOOL).build());
    public static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.DOUBLE, Vec3::x, ByteBufCodecs.DOUBLE, Vec3::y, ByteBufCodecs.DOUBLE, Vec3::z, Vec3::new);
    public static final AttachmentType<Vec3> SECTUMSEMPRA_OFFSET = ACExData.register("sectumsempra_offset", () -> AttachmentType.builder(() -> Vec3.ZERO).sync(VEC3_STREAM_CODEC).build());
    public static final AttachmentType<Float> SMASHER_SPEED = ACExData.register("smasher_speed", () -> AttachmentType.builder(() -> 1.0F).sync(ByteBufCodecs.FLOAT).build());
    public static final AttachmentType<ACExQuarryUpgrades> QUARRY_UPGRADES = ACExData.register("quarry_upgrades", () -> AttachmentType.serializable(ACExQuarryUpgrades::new).build());
    public static final AttachmentType<Integer> PROJECTION_SCALE = ACExData.register("projection_scale", () -> AttachmentType.builder(() -> 1).serialize(Codec.INT).sync(ByteBufCodecs.VAR_INT).build());

    public static final AttachmentType<Integer> MONOLITH_SUMMON_FAILS = ACExData.register("monolith_summon_fails", () -> AttachmentType.builder(() -> 0).build());
    public static final AttachmentType<Integer> MONOLITH_RITUAL = ACExData.register("monolith_ritual", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());

    public static final AttachmentType<Long> TESLA_CHARGE_START = ACExData.register("tesla_charge_start", () -> AttachmentType.builder(() -> -1L).sync(ByteBufCodecs.VAR_LONG).build());

    public static final AttachmentType<Boolean> OVERDRIVED = ACExData.register("overdrived", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static final AttachmentType<Long> SUBMARINE_REMOTE_TIME = ACExData.register("submarine_remote_time", () -> AttachmentType.builder(() -> 0L).build());

    public static final AttachmentType<Boolean> STOP_SEEKING = ACExData.register("stop_seeking", () -> AttachmentType.builder(() -> false).build());

    public static final AttachmentType<ACExSugarStaffAbilities.AbilityCooldowns> ABILITY_COOLDOWNS = ACExData.register("ability_cooldowns", () -> AttachmentType.builder(() -> ACExSugarStaffAbilities.AbilityCooldowns.NONE).serialize(ACExSugarStaffAbilities.AbilityCooldowns.CODEC).sync((holder, player) -> holder == player, ACExSugarStaffAbilities.AbilityCooldowns.STREAM_CODEC).build());

    public static final AttachmentType<ACExSugarStaffAbilities.AbilityCooldowns> GAMMA_COOLDOWNS = ACExData.register("gamma_cooldowns", () -> AttachmentType.builder(() -> ACExSugarStaffAbilities.AbilityCooldowns.NONE).serialize(ACExSugarStaffAbilities.AbilityCooldowns.CODEC).sync(ACExSugarStaffAbilities.AbilityCooldowns.STREAM_CODEC).build());

    public static final AttachmentType<ACExGammaAttacks.BeamMerge> GAMMA_BEAM_MERGE = ACExData.register("gamma_beam_merge", () -> AttachmentType.builder(() -> ACExGammaAttacks.BeamMerge.NONE).sync(ACExGammaAttacks.BeamMerge.STREAM_CODEC).build());

    public static final AttachmentType<Boolean> GAMMA_LEAPING = ACExData.register("gamma_leaping", () -> AttachmentType.builder(() -> false).sync(ByteBufCodecs.BOOL).build());

    public static final AttachmentType<Float> GAMMA_BEAM_WIDTH = ACExData.register("gamma_beam_width", () -> AttachmentType.builder(() -> 1.0F).sync(ByteBufCodecs.FLOAT).build());

    public static final AttachmentType<Integer> GAMMA_RIDER_REQUEST = ACExData.register("gamma_rider_request", () -> AttachmentType.builder(() -> -1).build());

    public static void init() {
        ACExData.init();
    }

    public static <T> void set(Object holder, Supplier<AttachmentType<T>> type, T value) {
        if (!ACExData.has(holder, type) || !Objects.equals(ACExData.get(holder, type), value)) {
            ACExData.set(holder, type, value);
        }
    }
}
