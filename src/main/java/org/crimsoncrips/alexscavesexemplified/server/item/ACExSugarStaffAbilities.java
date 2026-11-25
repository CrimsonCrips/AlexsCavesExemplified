package org.crimsoncrips.alexscavesexemplified.server.item;





import org.crimsoncrips.alexscavesexemplified.mixins.backport.ACExItemDisplayAccessor;
import org.crimsoncrips.alexscavesexemplified.mixins.backport.ACExDisplayAccessor;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.backport.ACEnchantmentHelper;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.FrostmintSpearEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.GumballEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.SpinningPeppermintEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.SugarStaffHexEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GumWormEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GumWormSegmentEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.SeaStaffItem;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import com.mojang.math.Transformation;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraft.world.entity.player.Player;
import org.crimsoncrips.alexscavesexemplified.backport.EntityTickEvent;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.misc.ACExScheduler;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;
import org.crimsoncrips.alexscavesexemplified.server.entity.GumwirmEntity;

import org.joml.Vector3f;

import net.minecraftforge.entity.PartEntity;
import net.minecraft.world.phys.AABB;
import java.util.function.Predicate;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import org.crimsoncrips.alexscavesexemplified.backport.ByteBufCodecs;
import org.crimsoncrips.alexscavesexemplified.backport.StreamCodec;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ACExSugarStaffAbilities {

    public static final int BASE_PEPPERMINTS = 0;
    public static final int BASE_HEX = 1;
    public static final int PEPPERNAMI = 2;
    public static final int RADIANT_SHIELD = 3;
    public static final int FROST_BARRAGE = 4;
    public static final int PEPPER_CRUSH = 5;
    public static final int HAILSTORM = 6;
    public static final int LINE_OF_GUM = 7;
    public static final int GUMMY_SWARM = 8;
    public static final int SWEET_RELEASE = 9;
    public static final int ABILITY_COUNT = 10;

    private static final String SCRIPTED_TAG = "alexscavesexemplified_scripted";

    public static final List<RegistryObject<Enchantment>> STAFF_ENCHANTMENTS = List.of(ACEnchantmentRegistry.HUMUNGOUS_HEX, ACEnchantmentRegistry.SPELL_LASTING, ACEnchantmentRegistry.MULTIPLE_MINT, ACEnchantmentRegistry.PEPPERMINT_PUNTING, ACEnchantmentRegistry.SEEKCANDY);

    private static final double AIM_RANGE = 64.0D;
    private static final float BACK_OVERSHOOT = 1.70158F;
    private static final int TARGET_RANGE = 32;
    private static final double LOOK_LEEWAY = 0.6D;
    private static final double LOOK_LEEWAY_PER_BLOCK = 0.03D;
    private static final int WORM_PATH_LIMIT = 400;
    private static final double WORM_VOID_MARGIN = 60.0D;

    private static final int HAILSTORM_SPEARS = 20;
    private static final int HAILSTORM_LIFESPAN = ACExUtils.seconds(15.0F);
    private static final int HAILSTORM_LIFESPAN_PER_LEVEL = ACExUtils.seconds(3.0F);
    private static final int HAILSTORM_RAIN_INTERVAL = ACExUtils.seconds(3.0F);
    private static final int HAILSTORM_COOLDOWN = ACExUtils.seconds(50.0F);
    private static final float HAILSTORM_FREEZE_PER_LEVEL = 0.05F;

    private static final int PEPPERNAMI_WIDTH = 13;
    private static final double PEPPERNAMI_SPACING = 0.9D;
    private static final double PEPPERNAMI_BEHIND = 3.0D;
    private static final double PEPPERNAMI_LINE_GAP = 1.5D;
    private static final int PEPPERNAMI_BASE_LINES = 2;
    private static final int PEPPERNAMI_LINES_PER_LEVEL = 2;
    private static final float PEPPERNAMI_SIZE_PER_LEVEL = 0.9F;
    private static final float PEPPERNAMI_SPEED = 8.0F;
    private static final int PEPPERNAMI_LIFESPAN = ACExUtils.seconds(5.0F);
    private static final int PEPPERNAMI_LIFESPAN_PER_LEVEL = ACExUtils.seconds(5.0F);
    private static final int PEPPERNAMI_COOLDOWN = ACExUtils.seconds(15.0F);

    private static final int SWEET_RELEASE_WIRM_START = ACExUtils.seconds(5.0F);
    private static final int SWEET_RELEASE_LIFT_START = ACExUtils.seconds(9.0F);
    private static final int SWEET_RELEASE_LIFT_DURATION = ACExUtils.seconds(1.0F);
    private static final int SWEET_RELEASE_VOLLEY_START = ACExUtils.seconds(10.0F);
    private static final int SWEET_RELEASE_VOLLEY_INTERVAL = ACExUtils.seconds(2.0F);
    private static final int SWEET_RELEASE_CHIPPER_START = ACExUtils.seconds(12.0F);
    private static final int SWEET_RELEASE_CHIPPER_RISE = ACExUtils.seconds(3.0F);
    private static final int SWEET_RELEASE_CHIPPER_DURATION = ACExUtils.seconds(4.0F);
    private static final int SWEET_RELEASE_WORM_START = ACExUtils.seconds(15.0F);
    private static final int SWEET_RELEASE_END = ACExUtils.seconds(16.0F);

    private static final int SWEET_RELEASE_PEPPERMINTS = 10;
    private static final float SWEET_RELEASE_HEX_SCALE = 3.0F;
    private static final double SWEET_RELEASE_RING_RADIUS = 8.0D;
    private static final double SWEET_RELEASE_RING_GAP = 2.0D;
    private static final int SWEET_RELEASE_RING_SIZE = 24;
    private static final int SWEET_RELEASE_RING_LIFESPAN = ACExUtils.seconds(6.0F);
    private static final float SWEET_RELEASE_RING_SPEED = PEPPERNAMI_SPEED * 0.5F * 1.9F;
    private static final int SWEET_RELEASE_VOLLEYS = 3;
    private static final int SWEET_RELEASE_SPEARS = 16;
    private static final int SWEET_RELEASE_LIFT = 8;
    private static final double SWEET_RELEASE_SPEAR_DISTANCE = 8.0D;
    private static final double SWEET_RELEASE_CHIPPER_DEPTH = 6.0D;
    private static final float SWEET_RELEASE_CHIPPER_SCALE = 4.0F;
    private static final float SWEET_RELEASE_CHIPPER_SPIN = 12.0F;
    private static final float SWEET_RELEASE_CRUSH_DAMAGE = 4.0F;
    private static final int SWEET_RELEASE_COOLDOWN = ACExUtils.seconds(60.0F);
    private static final int SWEET_RELEASE_WIRMS = 20;
    private static final int SWEET_RELEASE_WIRM_INTERVAL = ACExUtils.seconds(0.1F);
    private static final float SWEET_RELEASE_WIRM_BITE_DAMAGE = 2.0F;
    private static final double SWEET_RELEASE_WIRM_REACH = 1D;
    private static final int SWEET_RELEASE_WIRM_CLOSE_IN = ACExUtils.seconds(1.0F);
    private static final int SWEET_RELEASE_WIRM_ORBIT = ACExUtils.seconds(2.0F);
    private static final double SWEET_RELEASE_WIRM_ORBIT_RADIUS = 2.0D;
    private static final double SWEET_RELEASE_WIRM_MIN_RADIUS = 0.2D;
    private static final float SWEET_RELEASE_WIRM_ORBIT_SPEED = 0.2F;
    private static final double SWEET_RELEASE_WIRM_EMERGE_SPEED = 0.9D;
    private static final int SWEET_RELEASE_WIRM_BITE_INTERVAL = ACExUtils.seconds(0.5F);
    private static final double SWEET_RELEASE_WIRM_FLING_SPEED = 0.8D;
    private static final double SWEET_RELEASE_WIRM_FLING_LIFT = 0.4D;
    private static final double SWEET_RELEASE_WIRM_DIG_SPEED = 0.3D;
    private static final int SWEET_RELEASE_GUN_DURATION = ACExUtils.seconds(3.0F);
    private static final int SWEET_RELEASE_GUNS = 50;
    private static final double SWEET_RELEASE_GUN_DISTANCE = 8D;
    private static final int SWEET_RELEASE_GUN_FIRE_INTERVAL = ACExUtils.seconds(0.15F);
    private static final float SWEET_RELEASE_GUN_DAMAGE = 1.0F;
    private static final int SWEET_RELEASE_EXTRA_WORMS = 4;
    private static final int SWEET_RELEASE_EXTRA_WORM_DELAY = ACExUtils.seconds(1.0F);
    private static final int SWEET_RELEASE_EXTRA_WORM_INTERVAL = ACExUtils.seconds(0.75F);
    private static final int SWEET_RELEASE_EXTRA_WORM_GRACE = ACExUtils.seconds(5.0F);
    private static final float SWEET_RELEASE_REFUND = 0.5F;
    private static final int SWEET_RELEASE_FLASH_INTERVAL = ACExUtils.seconds(0.5F);

    private static final double WORM_DEPTH = 6.0D;
    private static final double WORM_RISE_SPEED = 1.2D;
    private static final double WORM_ARC_SPEED = 0.45D;
    private static final int WORM_ARC_RAMP = ACExUtils.seconds(0.25F);
    private static final double WORM_GRAVITY = 0.07D;
    private static final double WORM_MAX_FALL = 1.2D;
    private static final double WORM_SEGMENT_SPACING = 2.5D;
    private static final double WORM_HEAD_GAP = 1.2D;
    private static final double WORM_BODY_LENGTH = 55.0D;
    private static final float WORM_BITE_DAMAGE = 20.0F;
    private static final int WORM_MAX_AGE = ACExUtils.seconds(25.0F);

    private static final int RADIANT_SHIELD_COOLDOWN = ACExUtils.seconds(20.0F);
    private static final int RADIANT_SHIELD_LIFESPAN = ACExUtils.seconds(10.0F);
    private static final int RADIANT_SHIELD_LIFESPAN_PER_LEVEL = ACExUtils.seconds(5.0F);
    public static final int RADIANT_SHIELD_BURST_TIME = ACExUtils.seconds(2.0F);
    public static final double RADIANT_SHIELD_BURST_SPEED = 0.8D;

    private static final int LINE_OF_GUM_GUNS = 7;
    private static final double LINE_OF_GUM_SPACING = 1.2D;
    private static final int LINE_OF_GUM_VOLLEYS = 3;
    private static final int LINE_OF_GUM_VOLLEY_INTERVAL = ACExUtils.seconds(0.5F);
    private static final int LINE_OF_GUM_PELLETS = 4;
    private static final float LINE_OF_GUM_SPREAD = 6.0F;
    private static final float LINE_OF_GUM_SPEED = 1.6F;
    private static final float LINE_OF_GUM_DAMAGE = 4.0F;
    private static final int LINE_OF_GUM_GUMBALL_LIFE = ACExUtils.seconds(3.0F);
    private static final float LINE_OF_GUM_LIFE_PER_LEVEL = 0.05F;
    private static final float LINE_OF_GUM_HOMING = 0.25F;
    private static final int LINE_OF_GUM_COOLDOWN = ACExUtils.seconds(20.0F);
    private static final int LINE_OF_GUM_APPEAR_TICKS = ACExUtils.seconds(0.3F);
    private static final int LINE_OF_GUM_DISAPPEAR_TICKS = ACExUtils.seconds(0.6F);
    private static final int LINE_OF_GUM_DISAPPEAR_DELAY = ACExUtils.seconds(0.3F);
    private static final double LINE_OF_GUM_RECOIL = 0.35D;

    private static final float PEPPER_CRUSH_SCALE = 3.5F * 2.4F;
    private static final float PEPPER_CRUSH_SPEED = 6.0F;
    private static final int PEPPER_CRUSH_LIFESPAN = ACExUtils.seconds(10.0F);
    private static final int PEPPER_CRUSH_LIFESPAN_PER_LEVEL = ACExUtils.seconds(5.0F);
    private static final float PEPPER_CRUSH_TURN = 6.0F;
    private static final float PEPPER_CRUSH_CRUSH_DAMAGE = 6.0F;
    private static final int PEPPER_CRUSH_EMIT_INTERVAL = ACExUtils.seconds(1.0F);
    private static final int PEPPER_CRUSH_EMIT_LIFESPAN = ACExUtils.seconds(3.0F);
    private static final int PEPPER_CRUSH_COOLDOWN = ACExUtils.seconds(30.0F);

    private static final int FROST_BARRAGE_SPEARS = 30;
    private static final int FROST_BARRAGE_BATCHES = 5;
    private static final int FROST_BARRAGE_BATCH_INTERVAL = ACExUtils.seconds(0.2F);
    private static final double FROST_BARRAGE_BEHIND = 2.0D;
    private static final double FROST_BARRAGE_WIDTH = 3.0D;
    private static final float FROST_BARRAGE_INACCURACY = 1.5F;
    private static final double FROST_BARRAGE_SEEK_RANGE = 10.0D;
    private static final float FROST_BARRAGE_HOMING = 0.06F;
    private static final int FROST_BARRAGE_COOLDOWN = ACExUtils.seconds(25.0F);
    private static final double FROST_BARRAGE_TARGET_SPREAD = 2.5D;
    private static final double SPEAR_GRAVITY = 0.05D;
    private static final double SPEAR_DRAG = 0.99D;

    private static final int GUMMY_SWARM_COUNT = 11;
    private static final double GUMMY_SWARM_SPACING = 1.0D;
    private static final double GUMMY_SWARM_AHEAD = 1.0D;
    private static final double GUMMY_SWARM_SPEED = 0.35D;
    private static final double GUMMY_SWARM_WAVE_HEIGHT = 0.7D;
    private static final float GUMMY_SWARM_WAVE_SPEED = 0.3F;
    private static final float GUMMY_SWARM_PHASE_STEP = 0.9F;
    private static final int GUMMY_SWARM_LIFESPAN = ACExUtils.seconds(8.0F);
    private static final int GUMMY_SWARM_LIFESPAN_PER_LEVEL = ACExUtils.seconds(3.0F);
    private static final float GUMMY_SWARM_DAMAGE = 3.0F;
    private static final double GUMMY_SWARM_HIT_REACH = 0.3D;
    private static final double GUMMY_SWARM_SEEK_RANGE = 16.0D;
    private static final float GUMMY_SWARM_TURN = 8.0F;
    private static final double GUMMY_SWARM_DIVE_GRAVITY = 0.05D;
    private static final double GUMWIRM_BODY_LENGTH = 5.5D;
    private static final double GUMWIRM_HEAD_GAP = 0.12D;
    private static final double GUMWIRM_SEGMENT_SPACING = 0.25D;
    private static final int GUMMY_SWARM_SURFACE_SCAN = 4;
    private static final double GUMMY_SWARM_SURFACE_FOLLOW = 0.3D;
    private static final int GUMWIRM_DIVE_TIMEOUT = ACExUtils.seconds(5.0F);
    private static final int GUMMY_SWARM_COOLDOWN = ACExUtils.seconds(15.0F);

    public static class SweetRelease {
        private final double x;
        private final double y;
        private final double z;
        private final int casterId;
        private final Vec3 side;
        private final boolean chargeSpent;
        private final List<Integer> spawned = new ArrayList<>();
        private final int[] chippers = {-1, -1, -1, -1};
        private Display.ItemDisplay[] guns;
        private int age;
        private int lift = -1;
        private int endAge = SWEET_RELEASE_END;
        private int extraWormsAt = -1;
        private int bitesRemaining;
        private boolean eaten;
        private boolean releasing;
        private float storedDamage;
        private int lastFlash = -SWEET_RELEASE_FLASH_INTERVAL;

        public SweetRelease() {
            this(0, 0, 0, -1, Vec3.ZERO, false);
        }

        public SweetRelease(double x, double y, double z, int casterId, Vec3 side, boolean chargeSpent) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.casterId = casterId;
            this.side = side;
            this.chargeSpent = chargeSpent;
        }

        private Vec3 facing() {
            return new Vec3(this.side.z, 0.0D, -this.side.x);
        }
    }

    public static class GumballShot {
        private final int targetId;
        private int life;

        public GumballShot() {
            this(0, -1);
        }

        public GumballShot(int life, int targetId) {
            this.life = life;
            this.targetId = targetId;
        }
    }

    public static class PepperCrush {
        private final int targetId;
        private final int multipleMint;
        private int age;

        public PepperCrush() {
            this(-1, 0);
        }

        public PepperCrush(int targetId, int multipleMint) {
            this.targetId = targetId;
            this.multipleMint = multipleMint;
        }
    }

    public static class WormScript {
        private final Vec3 direction;
        private final double groundY;
        private final int targetId;
        private final List<Vec3> path = new ArrayList<>();
        private Vec3 velocity = new Vec3(0.0D, WORM_RISE_SPEED, 0.0D);
        private int age;
        private int arcTicks;
        private boolean bitten;
        private double buried;

        public WormScript() {
            this(Vec3.ZERO, 0, -1);
        }

        public WormScript(Vec3 direction, double groundY, int targetId) {
            this.direction = direction;
            this.groundY = groundY;
            this.targetId = targetId;
        }
    }

    public static class WirmOrbit {
        private final int targetId;
        private final Vec3 axisU;
        private final Vec3 axisV;
        private final List<Vec3> path = new ArrayList<>();
        private Vec3 center;
        private Vec3 velocity = Vec3.ZERO;
        private float angle;
        private int age;
        private int orbitAge = -1;
        private int lastBite = -SWEET_RELEASE_WIRM_BITE_INTERVAL;
        private boolean flung;
        private int flungAge;
        private double buried;

        public WirmOrbit() {
            this(-1, new Vec3(1.0D, 0.0D, 0.0D), new Vec3(0.0D, 0.0D, 1.0D), 0.0F, Vec3.ZERO);
        }

        public WirmOrbit(int targetId, Vec3 axisU, Vec3 axisV, float angle, Vec3 center) {
            this.targetId = targetId;
            this.axisU = axisU;
            this.axisV = axisV;
            this.angle = angle;
            this.center = center;
        }
    }

    public static class GummySwarmScript {
        private final int lifespan;
        private final int ownerId;
        private final int targetId;
        private final boolean seeking;
        private final float phase;
        private final List<Vec3> path = new ArrayList<>();
        private Vec3 direction;
        private Vec3 velocity = Vec3.ZERO;
        private double baseY;
        private int age;
        private boolean diving;
        private double buried;

        public GummySwarmScript() {
            this(Vec3.ZERO, 0, 0, -1, -1, false, 0.0F);
        }

        public GummySwarmScript(Vec3 direction, double baseY, int lifespan, int ownerId, int targetId, boolean seeking, float phase) {
            this.direction = direction;
            this.baseY = baseY;
            this.lifespan = lifespan;
            this.ownerId = ownerId;
            this.targetId = targetId;
            this.seeking = seeking;
            this.phase = phase;
        }
    }

    public record AbilityCooldowns(List<Long> ends, List<Integer> lengths) {
        public static final AbilityCooldowns NONE = new AbilityCooldowns(List.of(), List.of());
        public static final Codec<AbilityCooldowns> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.LONG.listOf().fieldOf("ends").forGetter(AbilityCooldowns::ends),
                Codec.INT.listOf().fieldOf("lengths").forGetter(AbilityCooldowns::lengths)
        ).apply(instance, AbilityCooldowns::new));
        public static final StreamCodec<ByteBuf, AbilityCooldowns> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_LONG.apply(ByteBufCodecs.list()), AbilityCooldowns::ends,
                ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list()), AbilityCooldowns::lengths,
                AbilityCooldowns::new);

        public long end(int ability) {
            return ability >= 0 && ability < this.ends.size() ? this.ends.get(ability) : 0L;
        }

        public int length(int ability) {
            return ability >= 0 && ability < this.lengths.size() ? this.lengths.get(ability) : 0;
        }

        public AbilityCooldowns with(int ability, long end, int length) {
            List<Long> newEnds = new ArrayList<>();
            List<Integer> newLengths = new ArrayList<>();
            for (int i = 0; i < Math.max(this.ends.size(), ability + 1); i++) {
                newEnds.add(i == ability ? end : this.end(i));
                newLengths.add(i == ability ? length : this.length(i));
            }
            return new AbilityCooldowns(List.copyOf(newEnds), List.copyOf(newLengths));
        }
    }

    public static int cooldownRemaining(Player player, int ability) {
        return (int) Math.max(0L, ACExData.get(player, ACExAttachments.ABILITY_COOLDOWNS).end(ability) - player.level().getGameTime());
    }

    public static float cooldownProgress(Player player, int ability) {
        int length = ACExData.get(player, ACExAttachments.ABILITY_COOLDOWNS).length(ability);
        return length <= 0 ? 0.0F : Mth.clamp(cooldownRemaining(player, ability) / (float) length, 0.0F, 1.0F);
    }

    public static int enchantLevel(ItemStack staff, Supplier<? extends Enchantment> enchantment) {
        return ACEnchantmentHelper.getEnchantmentLevelFromStack(enchantment, staff);
    }

    public static String abilityKey(int ability) {
        return switch (ability) {
            case BASE_PEPPERMINTS -> "peppermints";
            case BASE_HEX -> "hex";
            case PEPPERNAMI -> "peppernami";
            case RADIANT_SHIELD -> "radiant_shield";
            case FROST_BARRAGE -> "frost_barrage";
            case PEPPER_CRUSH -> "pepper_crush";
            case HAILSTORM -> "hailstorm";
            case LINE_OF_GUM -> "line_of_gum";
            case GUMMY_SWARM -> "gummy_swarm";
            case SWEET_RELEASE -> "sweet_release";
            default -> "";
        };
    }

    public static int abilityFromKey(String key) {
        for (int ability = 0; ability < ABILITY_COUNT; ability++) {
            if (abilityKey(ability).equals(key))
                return ability;
        }
        return -1;
    }

    public static List<String> defaultLayout() {
        List<String> keys = new ArrayList<>();
        for (int ability = 0; ability < ABILITY_COUNT; ability++) {
            keys.add(abilityKey(ability));
        }
        return keys;
    }

    public static int chargeCost(int ability) {
        return switch (ability) {
            case PEPPERNAMI -> 1;
            case RADIANT_SHIELD -> 2;
            case FROST_BARRAGE -> 2;
            case PEPPER_CRUSH -> 3;
            case HAILSTORM -> 4;
            case LINE_OF_GUM -> 4;
            case GUMMY_SWARM -> 5;
            case SWEET_RELEASE -> 10;
            default -> 0;
        };
    }

    public static Component requirement(int ability, Player player, ItemStack staff) {
        if ((ability == PEPPERNAMI || ability == PEPPER_CRUSH) && enchantLevel(staff, ACEnchantmentRegistry.PEPPERMINT_PUNTING) <= 0) {
            return Component.translatable("misc.alexscavesexemplified.sugar_staff_requires_punting");
        }
        if (ability == SWEET_RELEASE) {
            for (RegistryObject<Enchantment> key : STAFF_ENCHANTMENTS) {
                int maxLevel = key.isPresent() ? key.get().getMaxLevel() : 1;
                if (enchantLevel(staff, key) < maxLevel) {
                    return Component.translatable("misc.alexscavesexemplified.sugar_staff_requires_max");
                }
            }
        }
        int cost = chargeCost(ability);
        if (cost > 0 && ACExUtils.getRadiantCharges(player) < cost) {
            return Component.translatable("misc.alexscavesexemplified.sugar_staff_requires_charges", cost);
        }
        return null;
    }

    public static void cast(ServerPlayer player, int ability) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get() || ability < 0 || ability >= ABILITY_COUNT)
            return;
        InteractionHand hand = player.getMainHandItem().is(ACItemRegistry.SUGAR_STAFF.get()) ? InteractionHand.MAIN_HAND : player.getOffhandItem().is(ACItemRegistry.SUGAR_STAFF.get()) ? InteractionHand.OFF_HAND : null;
        if (hand == null)
            return;
        ItemStack staff = player.getItemInHand(hand);
        if (cooldownRemaining(player, ability) > 0) {
            player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.sugar_staff_cooldown"), true);
            return;
        }
        Component requirement = requirement(ability, player, staff);
        if (requirement != null) {
            player.displayClientMessage(requirement, true);
            return;
        }
        int cooldown = switch (ability) {
            case BASE_PEPPERMINTS -> castBasePeppermints(player, staff);
            case BASE_HEX -> castBaseHex(player, staff);
            case HAILSTORM -> castHailstorm(player, staff);
            case PEPPERNAMI -> castPeppernami(player, staff);
            case SWEET_RELEASE -> castSweetRelease(player);
            case LINE_OF_GUM -> castLineOfGum(player, staff);
            case PEPPER_CRUSH -> castPepperCrush(player, staff);
            case FROST_BARRAGE -> castFrostBarrage(player, staff);
            case GUMMY_SWARM -> castGummySwarm(player, staff);
            default -> castRadiantShield(player, staff);
        };
        if (cooldown < 0)
            return;
        player.swing(hand, true);
        ACExDataComponents.STAFF_CAST.set(staff, player.level().getGameTime());
        int cost = chargeCost(ability);
        if (!player.isCreative()) {
            ACExUtils.useRadiantCharges(player, cost);
            if (cooldown > 0) {
                ACExData.set(player, ACExAttachments.ABILITY_COOLDOWNS, ACExData.get(player, ACExAttachments.ABILITY_COOLDOWNS).with(ability, player.level().getGameTime() + cooldown, cooldown));
            }
        }
        if (cost > 0) {
            ACExUtils.awardAdvancement(player, "radiant_wrath", "powered");
        }
        staff.hurtAndBreak(cost > 0 ? 10 : 1, player, breaker -> breaker.broadcastBreakEvent(hand));
    }

    private static int castBasePeppermints(ServerPlayer player, ItemStack staff) {
        Level level = player.level();
        Entity lookingAtEntity = SeaStaffItem.getClosestLookingAtEntityFor(level, player, 32.0D);
        int spawnIn = 3 + enchantLevel(staff, ACEnchantmentRegistry.MULTIPLE_MINT);
        boolean punting = enchantLevel(staff, ACEnchantmentRegistry.PEPPERMINT_PUNTING) > 0;
        boolean seeking = lookingAtEntity != null && enchantLevel(staff, ACEnchantmentRegistry.SEEKCANDY) > 0;
        int despawnTime = ACExUtils.seconds(4.0F);
        for (int i = 0; i < spawnIn; i++) {
            SpinningPeppermintEntity peppermint = ACEntityRegistry.SPINNING_PEPPERMINT.get().create(level);
            if (peppermint == null)
                continue;
            peppermint.setPos(player.position().add(0.0D, player.getBbHeight() * 0.45F, 0.0D));
            if (punting) {
                peppermint.setStraight(true);
                peppermint.setYRot(180.0F + player.getYHeadRot() + (i - 1) * 15);
                peppermint.setSpinSpeed(8.0F);
                despawnTime = ACExUtils.seconds(1.0F);
            } else {
                peppermint.setStraight(false);
                peppermint.setYRot(180 + (i - 1) * 30);
                peppermint.setSpinSpeed(12.0F);
            }
            if (seeking) {
                peppermint.setSeekingEntityId(lookingAtEntity.getId());
                peppermint.setSpinSpeed(50.0F);
                despawnTime = ACExUtils.seconds(2.5F);
            }
            peppermint.setSpinRadius(3.5F);
            peppermint.setOwner(player);
            peppermint.setStartAngle(i * 360 / (float) spawnIn);
            peppermint.setLifespan(ACExUtils.seconds(4.0F));
            level.addFreshEntity(peppermint);
        }
        level.playSound(null, player.blockPosition(), ACSoundRegistry.SUGAR_STAFF_CAST_PEPPERMINT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        return despawnTime;
    }

    private static int castBaseHex(ServerPlayer player, ItemStack staff) {
        Level level = player.level();
        Vec3 ground = ACMath.getGroundBelowPosition(level, player.getEyePosition());
        SugarStaffHexEntity hex = ACEntityRegistry.SUGAR_STAFF_HEX.get().create(level);
        if (hex == null)
            return -1;
        hex.setOwner(player);
        hex.setPos(ground.x, ground.y, ground.z);
        hex.setHexScale(1.0F + 0.25F * enchantLevel(staff, ACEnchantmentRegistry.HUMUNGOUS_HEX));
        level.addFreshEntity(hex);
        level.playSound(null, player.blockPosition(), ACSoundRegistry.SUGAR_STAFF_CAST_HEX.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        hex.setLifespan(ACExUtils.seconds(5.0F) + ACExUtils.seconds(3.0F) * enchantLevel(staff, ACEnchantmentRegistry.SPELL_LASTING));
        return ACExUtils.seconds(5.0F);
    }

    private static int castHailstorm(ServerPlayer player, ItemStack staff) {
        ServerLevel level = player.serverLevel();
        int humunguous = enchantLevel(staff, ACEnchantmentRegistry.HUMUNGOUS_HEX);
        int spellLasting = enchantLevel(staff, ACEnchantmentRegistry.SPELL_LASTING);
        HitResult realHitResult = ProjectileUtil.getHitResultOnViewVector(player, Entity::canBeHitByProjectile, 128);
        if (realHitResult.getType() == HitResult.Type.MISS) {
            realHitResult = ProjectileUtil.getHitResultOnViewVector(player, Entity::canBeHitByProjectile, 42);
        }
        Vec3 hitLocation = realHitResult.getLocation();
        BlockPos mutableSkyPos = new BlockPos.MutableBlockPos(hitLocation.x, hitLocation.y + 1.5, hitLocation.z);

        int maxFallHeight = 15;
        for (int k = 0; mutableSkyPos.getY() < level.getMaxBuildHeight() && level.isEmptyBlock(mutableSkyPos) && k < maxFallHeight; k++) {
            mutableSkyPos = mutableSkyPos.above();
            k++;
        }

        int spears = HAILSTORM_SPEARS + (humunguous <= 0 ? 0 : 10 * humunguous + 1);
        float freeze = 1.0F + HAILSTORM_FREEZE_PER_LEVEL * enchantLevel(staff, ACEnchantmentRegistry.MULTIPLE_MINT);
        BlockPos skyPos = mutableSkyPos;
        rainFrostmint(level, skyPos, hitLocation, spears, freeze);
        for (int repeat = 1; repeat <= spellLasting; repeat++) {
            ACExScheduler.schedule(level, repeat * HAILSTORM_RAIN_INTERVAL, () -> rainFrostmint(level, skyPos, hitLocation, spears, freeze));
        }

        SugarStaffHexEntity sugarStaffHexEntity = ACEntityRegistry.SUGAR_STAFF_HEX.get().create(level);
        sugarStaffHexEntity.setOwner(player);
        ACExData.set(sugarStaffHexEntity, ACExAttachments.STAFF_HEX, true);
        sugarStaffHexEntity.setPos(hitLocation);
        sugarStaffHexEntity.setHexScale(1.0F + 0.25F * humunguous);
        sugarStaffHexEntity.setLifespan(HAILSTORM_LIFESPAN + HAILSTORM_LIFESPAN_PER_LEVEL * spellLasting);
        level.addFreshEntity(sugarStaffHexEntity);
        level.playSound(null, player.blockPosition(), ACSoundRegistry.SUGAR_STAFF_CAST_HEX.get(), SoundSource.PLAYERS, 1.0F, 0.0F);
        return HAILSTORM_COOLDOWN;
    }

    private static void rainFrostmint(ServerLevel level, BlockPos skyPos, Vec3 hitLocation, int spears, float freeze) {
        for (int j = 0; j < spears; j++) {
            FrostmintSpearEntity frostmintSpearEntity = ACEntityRegistry.FROSTMINT_SPEAR.get().spawn(level, BlockPos.containing(0, 0, 0), MobSpawnType.MOB_SUMMONED);
            if (frostmintSpearEntity != null) {
                frostmintSpearEntity.pickup = FrostmintSpearEntity.Pickup.CREATIVE_ONLY;
                frostmintSpearEntity.setBaseDamage(frostmintSpearEntity.getBaseDamage() * 2);

                Vec3 vec3 = skyPos.getCenter().add(level.random.nextFloat() * 16 - 8, level.random.nextFloat() * 4 - 2, level.random.nextFloat() * 16 - 8);
                int clearTries = 0;
                while (clearTries < 6 && !level.isEmptyBlock(BlockPos.containing(vec3)) && level.getFluidState(BlockPos.containing(vec3)).isEmpty()) {
                    clearTries++;
                    vec3 = skyPos.getCenter().add(level.random.nextFloat() * 16 - 8, level.random.nextFloat() * 4 - 2, level.random.nextFloat() * 16 - 8);
                }
                if (!level.isEmptyBlock(BlockPos.containing(vec3)) && level.getFluidState(BlockPos.containing(vec3)).isEmpty()) {
                    vec3 = skyPos.getCenter();
                }
                frostmintSpearEntity.setPos(vec3);
                Vec3 vec31 = hitLocation.subtract(vec3);
                frostmintSpearEntity.shoot(vec31.x, vec31.y, vec31.z, 0.5F + 1.5F * level.random.nextFloat(), level.random.nextFloat() * 10);
                ACExData.set(frostmintSpearEntity, ACExAttachments.FROST_RADIANT, true);
                ACExData.set(frostmintSpearEntity, ACExAttachments.STAFF_SPEAR, true);
                ACExData.set(frostmintSpearEntity, ACExAttachments.SPEAR_FREEZE, freeze);
            }
        }
    }

    private static int castPeppernami(ServerPlayer player, ItemStack staff) {
        Level level = player.level();
        int multipleMint = enchantLevel(staff, ACEnchantmentRegistry.MULTIPLE_MINT);
        int lifespan = PEPPERNAMI_LIFESPAN + PEPPERNAMI_LIFESPAN_PER_LEVEL * enchantLevel(staff, ACEnchantmentRegistry.SPELL_LASTING);
        int lines = PEPPERNAMI_BASE_LINES + PEPPERNAMI_LINES_PER_LEVEL * multipleMint;
        Vec3 forward = Vec3.directionFromRotation(0.0F, player.getYHeadRot());
        Vec3 right = new Vec3(-forward.z, 0.0D, forward.x);
        double behind = PEPPERNAMI_BEHIND;
        for (int line = 0; line < lines; line++) {
            float scale = 1.0F + PEPPERNAMI_SIZE_PER_LEVEL * (line / PEPPERNAMI_LINES_PER_LEVEL);
            int count = Math.max(3, Math.round(PEPPERNAMI_WIDTH / scale));
            Vec3 center = player.position().subtract(forward.scale(behind));
            for (int i = 0; i < count; i++) {
                Vec3 pos = center.add(right.scale((i - (count - 1) / 2.0D) * PEPPERNAMI_SPACING * scale));
                spawnRollingPeppermint(level, player, pos, 180.0F + player.getYHeadRot(), PEPPERNAMI_SPEED, scale, lifespan);
            }
            behind += PEPPERNAMI_LINE_GAP * scale;
        }
        level.playSound(null, player.blockPosition(), ACSoundRegistry.SUGAR_STAFF_CAST_PEPPERMINT.get(), SoundSource.PLAYERS, 1.0F, 0.8F);
        return PEPPERNAMI_COOLDOWN;
    }

    private static int castGummySwarm(ServerPlayer player, ItemStack staff) {
        ServerLevel level = player.serverLevel();
        int lifespan = GUMMY_SWARM_LIFESPAN + GUMMY_SWARM_LIFESPAN_PER_LEVEL * enchantLevel(staff, ACEnchantmentRegistry.SPELL_LASTING);
        boolean seeking = enchantLevel(staff, ACEnchantmentRegistry.SEEKCANDY) > 0;
        LivingEntity looked = seeking ? lookedAtEntity(player, TARGET_RANGE) : null;
        int targetId = looked == null ? -1 : looked.getId();
        Vec3 forward = Vec3.directionFromRotation(0.0F, player.getYHeadRot());
        Vec3 right = new Vec3(-forward.z, 0.0D, forward.x);
        Vec3 center = player.position().add(forward.scale(GUMMY_SWARM_AHEAD));
        for (int i = 0; i < GUMMY_SWARM_COUNT; i++) {
            Vec3 pos = center.add(right.scale((i - (GUMMY_SWARM_COUNT - 1) / 2.0D) * GUMMY_SWARM_SPACING));
            spawnGumwirm(level, player, pos, forward, lifespan, targetId, seeking, -Mth.HALF_PI + i * GUMMY_SWARM_PHASE_STEP);
        }
        level.playSound(null, player.blockPosition(), ACSoundRegistry.GUM_WORM_IDLE.get(), SoundSource.PLAYERS, 1.0F, 1.8F);
        return GUMMY_SWARM_COOLDOWN;
    }

    private static void spawnGumwirm(ServerLevel level, ServerPlayer player, Vec3 pos, Vec3 direction, int lifespan, int targetId, boolean seeking, float phase) {
        GumwirmEntity wirm = ACExEntityRegistry.GUMWIRM.get().create(level);
        if (wirm == null)
            return;
        double baseY = surfaceNear(level, pos.x, player.getY(), pos.z);
        wirm.moveTo(pos.x, baseY + GUMMY_SWARM_WAVE_HEIGHT * Mth.sin(phase), pos.z, yawOf(direction), 0.0F);
        wirm.setNoAi(true);
        wirm.setLeaping(true);
        markScripted(wirm);
        ACExData.set(wirm, ACExAttachments.SCRIPTED_WORM, true);
        ACExData.set(wirm, ACExAttachments.GUMMY_SWARM, new GummySwarmScript(direction, baseY, lifespan, player.getId(), targetId, seeking, phase));
        ForgeEventFactory.onFinalizeSpawn(wirm, level, level.getCurrentDifficultyAt(wirm.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(wirm);
    }

    private static void tickGummySwarm(GumwirmEntity wirm, GummySwarmScript script) {
        if (!wirm.isAlive()) {
            wirm.setDeltaMovement(Vec3.ZERO);
            return;
        }
        script.age++;
        Level level = wirm.level();
        Entity owner = level.getEntity(script.ownerId);
        Vec3 head = wirm.position();
        if (script.path.isEmpty()) {
            script.path.add(head);
        }
        wirm.setLeaping(true);
        if (!script.diving) {
            if (script.seeking) {
                LivingEntity target = gumwirmTarget(wirm, script, owner);
                if (target != null) {
                    Vec3 toward = aimPoint(target, head).subtract(head);
                    if (toward.horizontalDistanceSqr() > 1.0E-4D) {
                        script.direction = Vec3.directionFromRotation(0.0F, Mth.approachDegrees(yawOf(script.direction), yawOf(toward), GUMMY_SWARM_TURN));
                    }
                }
            }
            double nextX = head.x + script.direction.x * GUMMY_SWARM_SPEED;
            double nextZ = head.z + script.direction.z * GUMMY_SWARM_SPEED;
            script.baseY = Mth.lerp(GUMMY_SWARM_SURFACE_FOLLOW, script.baseY, surfaceNear(level, nextX, script.baseY, nextZ));
            double nextY = script.baseY + GUMMY_SWARM_WAVE_HEIGHT * Mth.sin(script.age * GUMMY_SWARM_WAVE_SPEED + script.phase);
            script.velocity = new Vec3(nextX, nextY, nextZ).subtract(head);
            if (script.age >= script.lifespan) {
                script.diving = true;
            }
        } else {
            script.velocity = new Vec3(script.direction.x * GUMMY_SWARM_SPEED, Math.max(script.velocity.y - GUMMY_SWARM_DIVE_GRAVITY, -GUMMY_SWARM_SPEED * 1.5D), script.direction.z * GUMMY_SWARM_SPEED);
        }
        Vec3 next = head.add(script.velocity);
        if (script.diving && next.y < script.baseY - GUMWIRM_HEAD_GAP) {
            script.buried += script.velocity.length();
        }
        wirm.setDeltaMovement(script.velocity);
        wirm.setTargetDigPitch((float) -(Mth.atan2(script.velocity.y, script.velocity.horizontalDistance()) * 180.0F / (float) Math.PI));
        if (script.velocity.horizontalDistanceSqr() > 1.0E-4D) {
            float yaw = yawOf(script.velocity);
            wirm.setYRot(yaw);
            wirm.setYHeadRot(yaw);
            wirm.setYBodyRot(yaw);
        }
        wirm.setPos(next);
        script.path.add(next);
        if (script.path.size() > WORM_PATH_LIMIT) {
            script.path.remove(0);
        }
        if (!script.diving) {
            DamageSource source = wirm.damageSources().mobProjectile(wirm, owner instanceof LivingEntity living ? living : null);
            for (LivingEntity living : mobsIn(level, wirm.getBoundingBox().inflate(GUMMY_SWARM_HIT_REACH), living -> validGumwirmTarget(living, owner))) {
                living.hurt(source, GUMMY_SWARM_DAMAGE);
            }
        }
        if (script.buried > GUMWIRM_BODY_LENGTH || script.age > script.lifespan + GUMWIRM_DIVE_TIMEOUT || next.y <= level.getMinBuildHeight() - WORM_VOID_MARGIN) {
            wirm.discard();
        }
    }

    private static LivingEntity gumwirmTarget(GumwirmEntity wirm, GummySwarmScript script, Entity owner) {
        if (wirm.level().getEntity(script.targetId) instanceof LivingEntity target && target.isAlive()) {
            return target;
        }
        return nearestMob(wirm.level(), wirm.getBoundingBox().inflate(GUMMY_SWARM_SEEK_RANGE), wirm.position(), living -> validGumwirmTarget(living, owner));
    }

    private static boolean validGumwirmTarget(LivingEntity living, Entity owner) {
        return living != owner && living.isAlive() && !living.isSpectator() && !(living instanceof Player player && player.isCreative()) && !ACExData.has(living, ACExAttachments.SCRIPTED_WORM) && (owner == null || !living.isAlliedTo(owner));
    }

    private static double surfaceNear(Level level, double x, double y, double z) {
        BlockPos.MutableBlockPos pos = BlockPos.containing(x, y + 0.5D, z).mutable();
        for (int i = 0; i < GUMMY_SWARM_SURFACE_SCAN && solidAt(level, pos); i++) {
            pos.move(0, 1, 0);
        }
        for (int i = 0; i < GUMMY_SWARM_SURFACE_SCAN && !solidAt(level, pos.below()); i++) {
            pos.move(0, -1, 0);
        }
        return pos.getY();
    }

    private static boolean solidAt(Level level, BlockPos pos) {
        return !level.getBlockState(pos).getCollisionShape(level, pos).isEmpty();
    }

    private static float yawOf(Vec3 direction) {
        return (float) Math.toDegrees(Mth.atan2(-direction.x, direction.z));
    }

    private static SpinningPeppermintEntity spawnRollingPeppermint(Level level, LivingEntity owner, Vec3 pos, float yRot, float speed, float scale, int lifespan) {
        SpinningPeppermintEntity peppermint = ACEntityRegistry.SPINNING_PEPPERMINT.get().create(level);
        if (peppermint == null)
            return null;
        ACExData.set(peppermint, ACExAttachments.PEPPERMINT_SCALE, scale);
        peppermint.refreshDimensions();
        peppermint.setPos(pos);
        if (!level.noCollision(peppermint))
            return null;
        peppermint.setStraight(true);
        ACExData.set(peppermint, ACExAttachments.STAFF_PEPPERMINT, true);
        peppermint.setYRot(yRot);
        peppermint.setSpinSpeed(speed);
        peppermint.setOwner(owner);
        peppermint.setLifespan(lifespan);
        level.addFreshEntity(peppermint);
        return peppermint;
    }

    private static int castRadiantShield(ServerPlayer player, ItemStack staff) {
        Level level = player.level();
        int multipleMint = enchantLevel(staff, ACEnchantmentRegistry.MULTIPLE_MINT);
        int lifespan = RADIANT_SHIELD_LIFESPAN + RADIANT_SHIELD_LIFESPAN_PER_LEVEL * enchantLevel(staff, ACEnchantmentRegistry.SPELL_LASTING);
        boolean burst = enchantLevel(staff, ACEnchantmentRegistry.PEPPERMINT_PUNTING) > 0;
        for (int layers = 1; layers < (multipleMint != 0 ? multipleMint + 2 : 2); layers++) {
            for (int l = 0; l < 5 * (2 + layers); l++) {
                SpinningPeppermintEntity peppermint = makeRadiantPeppermint(5 * (2 + layers), l, level, player, 12 / layers != 0 ? (float) 12 / layers : 1, 2.5F + layers - 1, lifespan, false, null);
                if (burst) {
                    ACExData.set(peppermint, ACExAttachments.SHIELD_BURST, true);
                }
            }
        }
        level.playSound(null, player.blockPosition(), ACSoundRegistry.SUGAR_STAFF_CAST_PEPPERMINT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        return multipleMint >= 2 ? ACExUtils.seconds(30.0F) : (multipleMint == 1 ? ACExUtils.seconds(25.0F) : RADIANT_SHIELD_COOLDOWN);
    }

    private static int castSweetRelease(ServerPlayer player) {
        LivingEntity target = lookedAtEntity(player, TARGET_RANGE);
        if (!startSweetRelease(target, player, !player.isCreative())) {
            player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.sugar_staff_no_target"), true);
            return -1;
        }
        return SWEET_RELEASE_COOLDOWN;
    }

    public static boolean startSweetRelease(LivingEntity target, @Nullable LivingEntity caster, boolean chargeSpent) {
        if (target == null || !target.isAlive() || ACExData.has(target, ACExAttachments.SWEET_RELEASE) || target == caster)
            return false;
        Level level = target.level();
        LivingEntity anchor = caster != null ? caster : target;
        Vec3 toTarget = caster == null ? Vec3.ZERO : target.position().subtract(caster.position()).multiply(1.0D, 0.0D, 1.0D);
        Vec3 facing = toTarget.lengthSqr() < 1.0E-4D ? Vec3.directionFromRotation(0.0F, anchor.getYHeadRot()) : toTarget.normalize();
        SweetRelease lock = new SweetRelease(target.getX(), target.getY(), target.getZ(), caster == null ? -1 : caster.getId(), new Vec3(-facing.z, 0.0D, facing.x), chargeSpent && caster != null);
        ACExData.set(target, ACExAttachments.SWEET_RELEASE, lock);

        float lockScale = Mth.clamp(target.getBbHeight() * 0.4F, 0.4F, 3.0F);
        float lockRadius = target.getBbWidth() * 0.5F + lockScale * 0.5F + 0.2F;
        for (int i = 0; i < SWEET_RELEASE_PEPPERMINTS; i++) {
            SpinningPeppermintEntity lockPeppermint = makeRadiantPeppermint(SWEET_RELEASE_PEPPERMINTS, i, level, anchor, 12.0F, lockRadius, SWEET_RELEASE_END + SWEET_RELEASE_EXTRA_WORM_GRACE * 2, false, target);
            lockPeppermint.setOwner(caster);
            ACExData.set(lockPeppermint, ACExAttachments.PEPPER_LOCK, true);
            ACExData.set(lockPeppermint, ACExAttachments.PEPPERMINT_SCALE, lockScale);
            lockPeppermint.refreshDimensions();
            lock.spawned.add(lockPeppermint.getId());
        }

        SugarStaffHexEntity hex = ACEntityRegistry.SUGAR_STAFF_HEX.get().create(level);
        if (hex != null) {
            hex.setOwner(caster);
            ACExData.set(hex, ACExAttachments.STAFF_HEX, true);
            hex.setPos(target.position());
            hex.setHexScale(SWEET_RELEASE_HEX_SCALE);
            hex.setLifespan(SWEET_RELEASE_WORM_START + ACExUtils.seconds(1.0F));
            level.addFreshEntity(hex);
            lock.spawned.add(hex.getId());
        }

        int lines = PEPPERNAMI_BASE_LINES + PEPPERNAMI_LINES_PER_LEVEL * 2;
        for (int line = 0; line < lines; line++) {
            float scale = 1.0F + PEPPERNAMI_SIZE_PER_LEVEL * (line / PEPPERNAMI_LINES_PER_LEVEL);
            double radius = SWEET_RELEASE_RING_RADIUS + line * SWEET_RELEASE_RING_GAP * scale;
            for (int i = 0; i < SWEET_RELEASE_RING_SIZE; i++) {
                double angle = Math.PI * 2 * (i + (line % 2) * 0.5D) / SWEET_RELEASE_RING_SIZE;
                Vec3 offset = new Vec3(Math.cos(angle), 0.0D, Math.sin(angle));
                float heading = (float) Math.toDegrees(Mth.atan2(offset.x, -offset.z));
                SpinningPeppermintEntity ringPeppermint = spawnRollingPeppermint(level, caster, target.position().add(offset.scale(radius)), 180.0F + heading, SWEET_RELEASE_RING_SPEED, scale, SWEET_RELEASE_RING_LIFESPAN);
                if (ringPeppermint != null) {
                    lock.spawned.add(ringPeppermint.getId());
                }
            }
        }
        level.playSound(null, anchor.blockPosition(), ACSoundRegistry.SUGAR_STAFF_CAST_HEX.get(), SoundSource.PLAYERS, 1.5F, 0.5F);
        return true;
    }

    private static int castLineOfGum(ServerPlayer player, ItemStack staff) {
        ServerLevel level = player.serverLevel();
        boolean seeking = enchantLevel(staff, ACEnchantmentRegistry.SEEKCANDY) > 0;
        int life = Math.round(LINE_OF_GUM_GUMBALL_LIFE * (1.0F + LINE_OF_GUM_LIFE_PER_LEVEL * enchantLevel(staff, ACEnchantmentRegistry.SPELL_LASTING)));
        Display.ItemDisplay[] guns = new Display.ItemDisplay[LINE_OF_GUM_GUNS];
        for (int i = 0; i < LINE_OF_GUM_GUNS; i++) {
            if (i * 2 == LINE_OF_GUM_GUNS - 1)
                continue;
            Display.ItemDisplay gun = EntityType.ITEM_DISPLAY.create(level);
            if (gun == null)
                continue;
            ((ACExItemDisplayAccessor) gun).invokeSetItemStack(new ItemStack(ACItemRegistry.SHOT_GUM.get()));
            ((ACExItemDisplayAccessor) gun).invokeSetItemTransform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
            markScripted(gun);
            setGunScale(gun, 0.0F);
            guns[i] = gun;
        }
        long[] lastFire = {-1L};
        orientGuns(player, guns, 0.0D);
        for (Display.ItemDisplay gun : guns) {
            if (gun != null) {
                level.addFreshEntity(gun);
            }
        }
        for (int tick = 1; tick <= LINE_OF_GUM_APPEAR_TICKS; tick++) {
            float scale = easeOutBack(tick / (float) LINE_OF_GUM_APPEAR_TICKS);
            ACExScheduler.schedule(level, tick, () -> setGunScales(guns, scale));
        }
        for (int volley = 0; volley < LINE_OF_GUM_VOLLEYS; volley++) {
            ACExScheduler.schedule(level, LINE_OF_GUM_APPEAR_TICKS + volley * LINE_OF_GUM_VOLLEY_INTERVAL, () -> {
                lastFire[0] = level.getGameTime();
                fireLineOfGum(level, player, guns, seeking, life);
            });
        }
        int disappearAt = LINE_OF_GUM_APPEAR_TICKS + (LINE_OF_GUM_VOLLEYS - 1) * LINE_OF_GUM_VOLLEY_INTERVAL + LINE_OF_GUM_DISAPPEAR_DELAY;
        for (int tick = 1; tick <= disappearAt + LINE_OF_GUM_DISAPPEAR_TICKS; tick++) {
            ACExScheduler.schedule(level, tick, () -> orientGuns(player, guns, lineOfGumRecoil(level, lastFire[0])));
        }
        for (int tick = 1; tick <= LINE_OF_GUM_DISAPPEAR_TICKS; tick++) {
            float scale = 1.0F - easeInBack(tick / (float) LINE_OF_GUM_DISAPPEAR_TICKS);
            ACExScheduler.schedule(level, disappearAt + tick, () -> setGunScales(guns, scale));
        }
        ACExScheduler.schedule(level, disappearAt + LINE_OF_GUM_DISAPPEAR_TICKS + 1, () -> {
            for (Display.ItemDisplay gun : guns) {
                if (gun != null) {
                    gun.discard();
                }
            }
        });
        return LINE_OF_GUM_COOLDOWN;
    }

    private static double lineOfGumRecoil(Level level, long lastFire) {
        if (lastFire < 0L)
            return 0.0D;
        long since = level.getGameTime() - lastFire;
        if (since < 0L || since >= LINE_OF_GUM_VOLLEY_INTERVAL)
            return 0.0D;
        return LINE_OF_GUM_RECOIL * Mth.square(1.0F - since / (float) LINE_OF_GUM_VOLLEY_INTERVAL);
    }

    private static void orientGuns(ServerPlayer player, Display.ItemDisplay[] guns, double recoil) {
        if (player.isRemoved())
            return;
        float yaw = player.getYHeadRot();
        Vec3 flatForward = Vec3.directionFromRotation(0.0F, yaw);
        Vec3 right = new Vec3(-flatForward.z, 0.0D, flatForward.x);
        Vec3 base = player.getEyePosition().add(0.0D, -0.4D, 0.0D).add(flatForward.scale(0.5D)).subtract(player.getLookAngle().scale(recoil));
        for (int i = 0; i < guns.length; i++) {
            Display.ItemDisplay gun = guns[i];
            if (gun == null || gun.isRemoved())
                continue;
            Vec3 pos = base.add(right.scale((i - (guns.length - 1) / 2.0D) * LINE_OF_GUM_SPACING));
            gun.moveTo(pos.x, pos.y, pos.z, yaw, player.getXRot());
        }
    }

    private static Vec3 lookTarget(ServerPlayer player) {
        Vec3 eye = player.getEyePosition();
        Vec3 end = eye.add(player.getLookAngle().scale(AIM_RANGE));
        HitResult blockHit = player.level().clip(new ClipContext(eye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 reach = blockHit.getType() == HitResult.Type.MISS ? end : blockHit.getLocation();
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(player, eye, reach, player.getBoundingBox().expandTowards(reach.subtract(eye)).inflate(1.0D), entity -> !entity.isSpectator() && entity.isPickable(), eye.distanceToSqr(reach));
        return entityHit != null ? entityHit.getLocation() : reach;
    }

    private static LivingEntity lookedAtEntity(ServerPlayer player, double range) {
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        Vec3 end = eye.add(look.scale(range));
        HitResult blockHit = player.level().clip(new ClipContext(eye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }
        LivingEntity closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (Entity candidate : player.level().getEntitiesOfClass(Entity.class, player.getBoundingBox().expandTowards(end.subtract(eye)).inflate(LOOK_LEEWAY + LOOK_LEEWAY_PER_BLOCK * range), entity -> (entity instanceof LivingEntity || entity instanceof PartEntity<?>) && entity.isPickable())) {
            LivingEntity living = owningMob(candidate);
            if (living == null || living == player || !living.isAlive() || living.isSpectator())
                continue;
            double leeway = LOOK_LEEWAY + LOOK_LEEWAY_PER_BLOCK * eye.distanceTo(candidate.position());
            Optional<Vec3> hit = candidate.getBoundingBox().inflate(leeway).clip(eye, end);
            if (hit.isPresent()) {
                double distance = eye.distanceToSqr(hit.get());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closest = living;
                }
            }
        }
        return closest;
    }

    private static LivingEntity owningMob(Entity entity) {
        Entity resolved = entity instanceof PartEntity<?> part ? part.getParent() : entity;
        if (ModList.get().isLoaded("alexsmobs")) {
            resolved = AMCompat.multipartHead(resolved);
        }
        return resolved instanceof LivingEntity living ? living : null;
    }

    private static Vec3 aimPoint(Entity target, Vec3 from) {
        Vec3 best = target.getBoundingBox().getCenter();
        PartEntity<?>[] parts = target.isMultipartEntity() ? target.getParts() : null;
        if (parts != null) {
            double bestDistance = best.distanceToSqr(from);
            for (PartEntity<?> part : parts) {
                Vec3 center = part.getBoundingBox().getCenter();
                double distance = center.distanceToSqr(from);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = center;
                }
            }
        }
        return best;
    }

    private static List<LivingEntity> mobsIn(Level level, AABB box, Predicate<LivingEntity> valid) {
        List<LivingEntity> mobs = new ArrayList<>();
        for (Entity entity : level.getEntitiesOfClass(Entity.class, box, entity -> entity instanceof LivingEntity || entity instanceof PartEntity<?>)) {
            LivingEntity living = owningMob(entity);
            if (living != null && !mobs.contains(living) && valid.test(living)) {
                mobs.add(living);
            }
        }
        return mobs;
    }

    private static LivingEntity nearestMob(Level level, AABB box, Vec3 from, Predicate<LivingEntity> valid) {
        LivingEntity nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (LivingEntity living : mobsIn(level, box, valid)) {
            double distance = aimPoint(living, from).distanceToSqr(from);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearest = living;
            }
        }
        return nearest;
    }

    private static Vec3 arcVelocity(Vec3 from, Vec3 to) {
        Vec3 delta = to.subtract(from);
        double flight = Mth.clamp(18.0D + delta.horizontalDistance() * 0.6D, 20.0D, 45.0D);
        double dragFactor = (1.0D - Math.pow(SPEAR_DRAG, flight)) / (flight * (1.0D - SPEAR_DRAG));
        double vy = (delta.y + 0.5D * SPEAR_GRAVITY * flight * flight) / flight;
        return new Vec3(delta.x / flight / dragFactor, vy / dragFactor, delta.z / flight / dragFactor);
    }

    public static final ThreadLocal<Boolean> SPEAR_HITTING = ThreadLocal.withInitial(() -> false);
    public static final ThreadLocal<Float> SPEAR_FREEZE = ThreadLocal.withInitial(() -> 1.0F);

    public static void cancelSpearKnockback(LivingKnockBackEvent event) {
        if (SPEAR_HITTING.get()) {
            event.setCanceled(true);
        }
    }

    private static void markScripted(Entity entity) {
        entity.addTag(SCRIPTED_TAG);
        ACExData.set(entity, ACExAttachments.SCRIPT_LIVE, true);
    }

    public static void anchorHex(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof SugarStaffHexEntity hex && ACExData.get(hex, ACExAttachments.STAFF_HEX)) {
            hex.setDeltaMovement(Vec3.ZERO);
        }
    }

    public static void removeStaleScripted(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!event.getLevel().isClientSide() && entity.getTags().contains(SCRIPTED_TAG) && !ACExData.has(entity, ACExAttachments.SCRIPT_LIVE)) {
            event.setCanceled(true);
        }
    }

    private static float easeOutBack(float t) {
        float c3 = BACK_OVERSHOOT + 1.0F;
        return 1.0F + c3 * (t - 1.0F) * (t - 1.0F) * (t - 1.0F) + BACK_OVERSHOOT * (t - 1.0F) * (t - 1.0F);
    }

    private static float easeInBack(float t) {
        float c3 = BACK_OVERSHOOT + 1.0F;
        return c3 * t * t * t - BACK_OVERSHOOT * t * t;
    }

    private static void setGunScales(Display.ItemDisplay[] guns, float scale) {
        for (Display.ItemDisplay gun : guns) {
            if (gun != null && !gun.isRemoved()) {
                setGunScale(gun, scale);
            }
        }
    }

    private static void setGunScale(Display.ItemDisplay gun, float scale) {
        ((ACExDisplayAccessor) gun).invokeSetTransformation(new Transformation(null, null, new Vector3f(scale, scale, scale), null));
        ((ACExDisplayAccessor) gun).invokeSetInterpolationDuration(1);
        gun.getEntityData().set(ACExDisplayAccessor.getInterpolationStartId(), 0, true);
    }

    private static void fireLineOfGum(ServerLevel level, ServerPlayer player, Display.ItemDisplay[] guns, boolean seeking, int life) {
        Vec3 lookPoint = lookTarget(player);
        for (Display.ItemDisplay gun : guns) {
            if (gun == null || gun.isRemoved())
                continue;
            Vec3 aim = lookPoint.subtract(gun.position());
            for (int i = 0; i < LINE_OF_GUM_PELLETS; i++) {
                GumballEntity gumball = new GumballEntity(level, player);
                gumball.setPos(gun.position());
                gumball.shoot(aim.x, aim.y, aim.z, LINE_OF_GUM_SPEED, LINE_OF_GUM_SPREAD);
                gumball.setDamage(LINE_OF_GUM_DAMAGE);
                gumball.setMaximumBounces(seeking ? 3 : 2);
                gumball.setTargetsOnBounce(seeking);
                ACExData.set(gumball, ACExAttachments.GUMBALL_SHOT, new GumballShot(life, -1));
                level.addFreshEntity(gumball);
            }
            level.playSound(null, gun.blockPosition(), ACSoundRegistry.GUMBALL_LAUNCH.get(), SoundSource.PLAYERS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
        }
    }

    private static int castPepperCrush(ServerPlayer player, ItemStack staff) {
        Level level = player.level();
        int targetId = -1;
        LivingEntity looked = enchantLevel(staff, ACEnchantmentRegistry.SEEKCANDY) > 0 ? lookedAtEntity(player, TARGET_RANGE) : null;
        if (looked != null) {
            targetId = looked.getId();
        }
        Vec3 forward = Vec3.directionFromRotation(0.0F, player.getYHeadRot());
        int crushLifespan = PEPPER_CRUSH_LIFESPAN + PEPPER_CRUSH_LIFESPAN_PER_LEVEL * enchantLevel(staff, ACEnchantmentRegistry.SPELL_LASTING);
        SpinningPeppermintEntity giant = spawnRollingPeppermint(level, player, player.position().add(forward.scale(2.5D)), 180.0F + player.getYHeadRot(), PEPPER_CRUSH_SPEED, PEPPER_CRUSH_SCALE, crushLifespan);
        if (giant == null) {
            giant = spawnRollingPeppermint(level, player, player.position(), 180.0F + player.getYHeadRot(), PEPPER_CRUSH_SPEED, PEPPER_CRUSH_SCALE, crushLifespan);
        }
        if (giant == null) {
            player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.sugar_staff_no_room"), true);
            return -1;
        }
        ACExData.set(giant, ACExAttachments.PEPPER_MASSIVE, true);
        ACExData.set(giant, ACExAttachments.SHAKE_OWNER, player.getId());
        ACExData.set(giant, ACExAttachments.PEPPER_CRUSH, new PepperCrush(targetId, enchantLevel(staff, ACEnchantmentRegistry.MULTIPLE_MINT)));
        level.playSound(null, player.blockPosition(), ACSoundRegistry.SUGAR_STAFF_CAST_PEPPERMINT.get(), SoundSource.PLAYERS, 2.0F, 0.5F);
        return PEPPER_CRUSH_COOLDOWN;
    }

    private static int castFrostBarrage(ServerPlayer player, ItemStack staff) {
        ServerLevel level = player.serverLevel();
        boolean seeking = enchantLevel(staff, ACEnchantmentRegistry.SEEKCANDY) > 0;
        int perBatch = Math.max(1, FROST_BARRAGE_SPEARS / FROST_BARRAGE_BATCHES);
        for (int batch = 0; batch < FROST_BARRAGE_BATCHES; batch++) {
            ACExScheduler.schedule(level, batch * FROST_BARRAGE_BATCH_INTERVAL, () -> {
                if (player.isRemoved())
                    return;
                float yaw = player.getYHeadRot();
                Vec3 forward = Vec3.directionFromRotation(0.0F, yaw);
                Vec3 lookPoint = lookTarget(player);
                Vec3 right = new Vec3(-forward.z, 0.0D, forward.x);
                for (int i = 0; i < perBatch; i++) {
                    Vec3 from = player.position().subtract(forward.scale(FROST_BARRAGE_BEHIND)).add(right.scale((level.random.nextDouble() * 2.0D - 1.0D) * FROST_BARRAGE_WIDTH)).add(0.0D, 1.5D + level.random.nextDouble(), 0.0D);
                    if (!level.isEmptyBlock(BlockPos.containing(from)))
                        continue;
                    FrostmintSpearEntity spear = ACEntityRegistry.FROSTMINT_SPEAR.get().create(level);
                    if (spear == null)
                        continue;
                    spear.setPos(from);
                    spear.setOwner(player);
                    spear.pickup = FrostmintSpearEntity.Pickup.CREATIVE_ONLY;
                    ACExData.set(spear, ACExAttachments.FROST_RADIANT, true);
                    ACExData.set(spear, ACExAttachments.STAFF_SPEAR, true);
                    Vec3 landing = lookPoint.add((level.random.nextDouble() * 2.0D - 1.0D) * FROST_BARRAGE_TARGET_SPREAD, 0.0D, (level.random.nextDouble() * 2.0D - 1.0D) * FROST_BARRAGE_TARGET_SPREAD);
                    Vec3 launch = arcVelocity(from, landing);
                    spear.shoot(launch.x, launch.y, launch.z, (float) launch.length(), FROST_BARRAGE_INACCURACY);
                    if (seeking) {
                        ACExData.set(spear, ACExAttachments.SEEKING_SPEAR, true);
                    }
                    level.addFreshEntity(spear);
                }
                level.playSound(null, player.blockPosition(), ACSoundRegistry.SUGAR_STAFF_CAST_HEX.get(), SoundSource.PLAYERS, 0.8F, 1.6F + level.random.nextFloat() * 0.3F);
            });
        }
        return FROST_BARRAGE_COOLDOWN;
    }

    private static void tickSeekingSpear(FrostmintSpearEntity spear) {
        Vec3 motion = spear.getDeltaMovement();
        if (spear.inGround || motion.y >= 0.0D || motion.lengthSqr() < 1.0E-4D)
            return;
        Entity owner = spear.getOwner();
        LivingEntity nearest = nearestMob(spear.level(), spear.getBoundingBox().inflate(FROST_BARRAGE_SEEK_RANGE), spear.position(), living -> living != owner && living.isAlive() && !living.isSpectator() && (owner == null || !living.isAlliedTo(owner)));
        if (nearest == null)
            return;
        Vec3 desired = aimPoint(nearest, spear.position()).subtract(spear.position()).normalize().scale(motion.length());
        spear.setDeltaMovement(motion.lerp(desired, FROST_BARRAGE_HOMING));
        spear.hasImpulse = true;
    }

    public static boolean interceptSweetReleaseHurt(LivingEntity target, DamageSource source, float amount) {
        if (target.level().isClientSide || !ACExData.has(target, ACExAttachments.SWEET_RELEASE) || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return false;
        SweetRelease lock = ACExData.get(target, ACExAttachments.SWEET_RELEASE);
        if (lock.releasing)
            return false;
        if (amount > 0.0F && !target.isInvulnerableTo(source)) {
            lock.storedDamage += amount;
            if (lock.age - lock.lastFlash >= SWEET_RELEASE_FLASH_INTERVAL) {
                lock.lastFlash = lock.age;
                target.level().broadcastDamageEvent(target, source);
            }
        }
        return true;
    }

    private static void releaseStoredDamage(LivingEntity target, SweetRelease lock, DamageSource source, float extra) {
        lock.releasing = true;
        float total = lock.storedDamage + extra;
        lock.storedDamage = 0.0F;
        if (total > 0.0F) {
            hurtLocked(target, source, total);
        }
    }

    private static void tickGumball(GumballEntity gumball, GumballShot shot) {
        if (--shot.life <= 0) {
            gumball.discard();
            return;
        }
        Entity target = shot.targetId == -1 ? null : gumball.level().getEntity(shot.targetId);
        if (target == null || !target.isAlive())
            return;
        Vec3 motion = gumball.getDeltaMovement();
        Vec3 desired = aimPoint(target, gumball.position()).subtract(gumball.position()).normalize().scale(Math.max(motion.length(), LINE_OF_GUM_SPEED * 0.75D));
        gumball.setDeltaMovement(motion.lerp(desired, LINE_OF_GUM_HOMING));
        gumball.hasImpulse = true;
    }

    private static void tickPepperCrush(SpinningPeppermintEntity giant, PepperCrush data) {
        data.age++;
        Level level = giant.level();
        LivingEntity owner = giant.getOwner();
        Entity target = data.targetId == -1 ? null : level.getEntity(data.targetId);
        if (target != null && target.isAlive()) {
            Vec3 toTarget = aimPoint(target, giant.position()).subtract(giant.position());
            float desired = (float) Math.toDegrees(Mth.atan2(-toTarget.x, toTarget.z));
            float current = giant.getYRot() - 180.0F;
            giant.setYRot(180.0F + Mth.approachDegrees(current, desired, PEPPER_CRUSH_TURN));
        }
        for (LivingEntity crushed : level.getEntitiesOfClass(LivingEntity.class, giant.getBoundingBox())) {
            if (crushed != owner && (owner == null || !crushed.isAlliedTo(owner))) {
                crushed.hurt(giant.damageSources().mobProjectile(giant, owner), PEPPER_CRUSH_CRUSH_DAMAGE);
            }
        }
        if (data.multipleMint > 0 && owner != null && data.age % PEPPER_CRUSH_EMIT_INTERVAL == 0) {
            float heading = giant.getYRot() - 180.0F;
            for (int i = 0; i < data.multipleMint * 2; i++) {
                float side = (i % 2 == 0 ? 1.0F : -1.0F) * (35.0F + 25.0F * (i / 2));
                Vec3 out = Vec3.directionFromRotation(0.0F, heading + side);
                spawnRollingPeppermint(level, owner, giant.position().add(out.scale(giant.getBbWidth() * 0.6D)), 180.0F + heading + side, PEPPERNAMI_SPEED, 1.0F, PEPPER_CRUSH_EMIT_LIFESPAN);
            }
        }
    }

    public static SpinningPeppermintEntity makeRadiantPeppermint(int amountOfPeppermint, float loopInt, Level level, LivingEntity living, float spinSpeed, float spinRadius, int setLifespan, boolean setStraight, Entity seeking) {
        SpinningPeppermintEntity peppermint = ACEntityRegistry.SPINNING_PEPPERMINT.get().create(living.level());
        peppermint.setPos(living.position().add(0, living.getBbHeight() * 0.45F, 0));
        if (seeking != null) {
            peppermint.setSeekingEntityId(seeking.getId());
        }
        ACExData.set(peppermint, ACExAttachments.PEPPER_RADIANT, true);
        ACExData.set(peppermint, ACExAttachments.STAFF_PEPPERMINT, true);
        peppermint.setStraight(setStraight);
        peppermint.setYRot(180 + (loopInt - 1) * 30);
        peppermint.setSpinSpeed(spinSpeed);
        peppermint.setSpinRadius(spinRadius);
        peppermint.setOwner(living);
        peppermint.setStartAngle(loopInt * 360 / (float) amountOfPeppermint);
        peppermint.setLifespan(setLifespan);
        level.addFreshEntity(peppermint);
        return peppermint;
    }

    public static void tickSweetRelease(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity.level().isClientSide)
            return;
        if (entity instanceof GumwirmEntity wirm && ACExData.has(wirm, ACExAttachments.GUMMY_SWARM)) {
            tickGummySwarm(wirm, ACExData.get(wirm, ACExAttachments.GUMMY_SWARM));
            return;
        }
        if (entity instanceof GumWormSegmentEntity segment && segment.getHeadEntity() instanceof GumwirmEntity wirm && ACExData.has(wirm, ACExAttachments.GUMMY_SWARM)) {
            followPath(segment, wirm.position(), ACExData.get(wirm, ACExAttachments.GUMMY_SWARM).path, GUMWIRM_HEAD_GAP, GUMWIRM_SEGMENT_SPACING);
            return;
        }
        if (entity instanceof GumwirmEntity wirm && ACExData.has(wirm, ACExAttachments.WIRM_ORBIT)) {
            tickWirmOrbit(wirm, ACExData.get(wirm, ACExAttachments.WIRM_ORBIT));
            return;
        }
        if (entity instanceof GumWormSegmentEntity segment && segment.getHeadEntity() instanceof GumwirmEntity wirm && ACExData.has(wirm, ACExAttachments.WIRM_ORBIT)) {
            followPath(segment, wirm.position(), ACExData.get(wirm, ACExAttachments.WIRM_ORBIT).path, GUMWIRM_HEAD_GAP, GUMWIRM_SEGMENT_SPACING);
            return;
        }
        if (entity instanceof GumWormEntity worm && ACExData.has(worm, ACExAttachments.WORM_SCRIPT)) {
            tickWorm(worm, ACExData.get(worm, ACExAttachments.WORM_SCRIPT));
            return;
        }
        if (entity instanceof GumWormSegmentEntity segment && segment.getHeadEntity() instanceof GumWormEntity worm && ACExData.has(worm, ACExAttachments.WORM_SCRIPT)) {
            followPath(segment, worm.position(), ACExData.get(worm, ACExAttachments.WORM_SCRIPT).path, WORM_HEAD_GAP, WORM_SEGMENT_SPACING);
            return;
        }
        if (entity instanceof FrostmintSpearEntity spear && ACExData.has(spear, ACExAttachments.SEEKING_SPEAR)) {
            tickSeekingSpear(spear);
            return;
        }
        if (entity instanceof GumballEntity gumball && ACExData.has(gumball, ACExAttachments.GUMBALL_SHOT)) {
            tickGumball(gumball, ACExData.get(gumball, ACExAttachments.GUMBALL_SHOT));
            return;
        }
        if (entity instanceof SpinningPeppermintEntity giant && ACExData.has(giant, ACExAttachments.PEPPER_CRUSH)) {
            tickPepperCrush(giant, ACExData.get(giant, ACExAttachments.PEPPER_CRUSH));
            return;
        }
        if (!(entity instanceof LivingEntity target) || !ACExData.has(target, ACExAttachments.SWEET_RELEASE))
            return;
        ServerLevel level = (ServerLevel) target.level();
        SweetRelease lock = ACExData.get(target, ACExAttachments.SWEET_RELEASE);
        lock.age++;
        if (!target.isAlive()) {
            if (!lock.releasing) {
                forceStopSweetRelease(target, lock);
            } else {
                endSweetRelease(target, lock);
            }
            return;
        }
        if (lock.eaten || lock.age > lock.endAge) {
            endSweetRelease(target, lock);
            return;
        }
        Entity caster = level.getEntity(lock.casterId);

        if (lock.age == SWEET_RELEASE_CHIPPER_START) {
            Vec3[] directions = {lock.side, lock.side.scale(-1.0D), lock.facing(), lock.facing().scale(-1.0D)};
            for (int i = 0; i < lock.chippers.length; i++) {
                lock.chippers[i] = spawnChipper(level, caster, lock, directions[i], SWEET_RELEASE_CHIPPER_SPIN);
            }
        }
        if (lock.age == SWEET_RELEASE_LIFT_START) {
            int lift = 0;
            while (lift < SWEET_RELEASE_LIFT && level.noCollision(target, target.getBoundingBox().move(0.0D, lift + 1, 0.0D))) {
                lift++;
            }
            lock.lift = lift;
        }
        float raise = lock.lift <= 0 ? 0.0F : Math.min(1.0F, (lock.age - SWEET_RELEASE_LIFT_START) / (float) SWEET_RELEASE_LIFT_DURATION);
        double y = lock.y + Math.max(0, lock.lift) * (1.0F - Mth.square(1.0F - raise));
        holdAt(target, lock.x, y, lock.z);

        int volley = lock.age - SWEET_RELEASE_VOLLEY_START;
        if (volley >= 0 && volley % SWEET_RELEASE_VOLLEY_INTERVAL == 0 && volley / SWEET_RELEASE_VOLLEY_INTERVAL < SWEET_RELEASE_VOLLEYS) {
            spearTarget(target, caster);
        }
        tickSweetReleaseGuns(level, target, lock, caster, volley);

        int sinceChipper = lock.age - SWEET_RELEASE_CHIPPER_START;
        int chipperDuration = SWEET_RELEASE_CHIPPER_DURATION;
        if (sinceChipper == chipperDuration) {
            discardChippers(level, lock);
        } else if (sinceChipper >= 0 && sinceChipper < chipperDuration) {
            float rise = Math.min(1.0F, sinceChipper / (float) SWEET_RELEASE_CHIPPER_RISE);
            double below = (y - lock.y + SWEET_RELEASE_CHIPPER_DEPTH + SWEET_RELEASE_CHIPPER_SCALE * 0.5D) * Mth.square(1.0F - rise);
            Vec3 center = new Vec3(lock.x, y + target.getBbHeight() * 0.5D - below, lock.z);
            Vec3[] directions = {lock.side, lock.side.scale(-1.0D), lock.facing(), lock.facing().scale(-1.0D)};
            Entity crusher = null;
            for (int i = 0; i < lock.chippers.length; i++) {
                Entity chipper = placeChipper(level, lock.chippers[i], center, directions[i], target);
                if (crusher == null) {
                    crusher = chipper;
                }
            }
            if (lock.age % ACExUtils.seconds(0.5F) == 0 && crusher != null) {
                target.hurt(target.damageSources().mobProjectile(crusher, caster instanceof LivingEntity living ? living : null), SWEET_RELEASE_CRUSH_DAMAGE);
            }
        }

        int sinceWirms = lock.age - SWEET_RELEASE_WIRM_START;
        if (sinceWirms >= 0 && sinceWirms % SWEET_RELEASE_WIRM_INTERVAL == 0 && sinceWirms / SWEET_RELEASE_WIRM_INTERVAL < SWEET_RELEASE_WIRMS) {
            summonWirm(level, target, lock, sinceWirms / SWEET_RELEASE_WIRM_INTERVAL);
        }
        if (lock.age == SWEET_RELEASE_WORM_START) {
            summonWorm(level, target, lock, lock.side);
        }
        if (lock.extraWormsAt >= 0) {
            int sinceExtra = lock.age - lock.extraWormsAt;
            if (sinceExtra >= 0 && sinceExtra % SWEET_RELEASE_EXTRA_WORM_INTERVAL == 0 && sinceExtra / SWEET_RELEASE_EXTRA_WORM_INTERVAL < SWEET_RELEASE_EXTRA_WORMS) {
                Vec3[] directions = {lock.facing(), lock.side.scale(-1.0D), lock.facing().scale(-1.0D), lock.side};
                summonWorm(level, target, lock, directions[(sinceExtra / SWEET_RELEASE_EXTRA_WORM_INTERVAL) % directions.length]);
            }
        }
    }

    private static void tickSweetReleaseGuns(ServerLevel level, LivingEntity target, SweetRelease lock, Entity caster, int sinceStart) {
        int duration = SWEET_RELEASE_GUN_DURATION;
        if (sinceStart < 0 || sinceStart > duration + 1)
            return;
        Vec3 center = target.getBoundingBox().getCenter();
        if (sinceStart == 0) {
            lock.guns = new Display.ItemDisplay[SWEET_RELEASE_GUNS];
            for (int i = 0; i < SWEET_RELEASE_GUNS; i++) {
                Display.ItemDisplay gun = EntityType.ITEM_DISPLAY.create(level);
                if (gun == null)
                    continue;
                ((ACExItemDisplayAccessor) gun).invokeSetItemStack(new ItemStack(ACItemRegistry.SHOT_GUM.get()));
                ((ACExItemDisplayAccessor) gun).invokeSetItemTransform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
                markScripted(gun);
                setGunScale(gun, 0.0F);
                lock.guns[i] = gun;
            }
            placeSweetReleaseGuns(lock.guns, center, sinceStart, duration);
            for (Display.ItemDisplay gun : lock.guns) {
                if (gun != null) {
                    level.addFreshEntity(gun);
                    lock.spawned.add(gun.getId());
                }
            }
            return;
        }
        if (lock.guns == null)
            return;
        if (sinceStart == duration + 1) {
            for (Display.ItemDisplay gun : lock.guns) {
                if (gun != null) {
                    gun.discard();
                }
            }
            lock.guns = null;
            return;
        }
        placeSweetReleaseGuns(lock.guns, center, sinceStart, duration);
        if (sinceStart <= LINE_OF_GUM_APPEAR_TICKS) {
            setGunScales(lock.guns, easeOutBack(sinceStart / (float) LINE_OF_GUM_APPEAR_TICKS));
        } else if (sinceStart > duration - LINE_OF_GUM_DISAPPEAR_TICKS) {
            setGunScales(lock.guns, 1.0F - easeInBack((sinceStart - (duration - LINE_OF_GUM_DISAPPEAR_TICKS)) / (float) LINE_OF_GUM_DISAPPEAR_TICKS));
        } else {
            for (int i = 0; i < lock.guns.length; i++) {
                Display.ItemDisplay gun = lock.guns[i];
                if (gun == null || gun.isRemoved() || (sinceStart + i) % SWEET_RELEASE_GUN_FIRE_INTERVAL != 0)
                    continue;
                GumballEntity gumball = new GumballEntity(level, caster instanceof LivingEntity living ? living : null);
                gumball.setPos(gun.position());
                Vec3 aim = center.subtract(gun.position());
                gumball.shoot(aim.x, aim.y, aim.z, LINE_OF_GUM_SPEED, 1.0F);
                gumball.setDamage(SWEET_RELEASE_GUN_DAMAGE);
                gumball.setMaximumBounces(0);
                ACExData.set(gumball, ACExAttachments.GUMBALL_SHOT, new GumballShot(LINE_OF_GUM_GUMBALL_LIFE, -1));
                level.addFreshEntity(gumball);
                if (i % 4 == 0) {
                    level.playSound(null, gun.blockPosition(), ACSoundRegistry.GUMBALL_LAUNCH.get(), SoundSource.PLAYERS, 0.7F, 0.8F + level.random.nextFloat() * 0.4F);
                }
            }
        }
    }

    private static double sweetReleaseGunRecoil(int index, int sinceStart, int duration) {
        int since = (sinceStart + index) % SWEET_RELEASE_GUN_FIRE_INTERVAL;
        int lastFire = sinceStart - since;
        if (lastFire <= LINE_OF_GUM_APPEAR_TICKS || lastFire > duration - LINE_OF_GUM_DISAPPEAR_TICKS)
            return 0.0D;
        return LINE_OF_GUM_RECOIL * Mth.square(1.0F - since / (float) SWEET_RELEASE_GUN_FIRE_INTERVAL);
    }

    private static void placeSweetReleaseGuns(Display.ItemDisplay[] guns, Vec3 center, int sinceStart, int duration) {
        float golden = (float) (Math.PI * (3.0D - Math.sqrt(5.0D)));
        for (int i = 0; i < guns.length; i++) {
            Display.ItemDisplay gun = guns[i];
            if (gun == null || gun.isRemoved())
                continue;
            float yLevel = 1.0F - (i + 0.5F) / guns.length * 2.0F;
            float ring = Mth.sqrt(1.0F - yLevel * yLevel);
            float theta = golden * i;
            Vec3 direction = new Vec3(Mth.cos(theta) * ring, yLevel, Mth.sin(theta) * ring);
            Vec3 pos = center.add(direction.scale(SWEET_RELEASE_GUN_DISTANCE + sweetReleaseGunRecoil(i, sinceStart, duration)));
            Vec3 toward = direction.scale(-1.0D);
            float yaw = (float) Math.toDegrees(Mth.atan2(-toward.x, toward.z));
            float pitch = (float) -Math.toDegrees(Mth.atan2(toward.y, toward.horizontalDistance()));
            gun.moveTo(pos.x, pos.y, pos.z, yaw, pitch);
        }
    }

    private static void forceStopSweetRelease(LivingEntity target, SweetRelease lock) {
        Level level = target.level();
        ACExData.remove(target, ACExAttachments.SWEET_RELEASE);
        discardSweetReleaseEntities(level, lock);
        for (int id : lock.spawned) {
            Entity entity = level.getEntity(id);
            if (entity != null) {
                entity.discard();
            }
        }
        if (lock.chargeSpent && level.getEntity(lock.casterId) instanceof Player caster) {
            ACExUtils.refundRadiantCharge(caster, Math.round(chargeCost(SWEET_RELEASE) * SWEET_RELEASE_REFUND));
        }
    }

    private static void discardChippers(Level level, SweetRelease lock) {
        for (int i = 0; i < lock.chippers.length; i++) {
            Entity entity = lock.chippers[i] == -1 ? null : level.getEntity(lock.chippers[i]);
            if (entity != null) {
                entity.discard();
            }
            lock.chippers[i] = -1;
        }
    }

    private static void discardSweetReleaseEntities(Level level, SweetRelease lock) {
        discardChippers(level, lock);
        if (lock.guns != null) {
            for (Display.ItemDisplay gun : lock.guns) {
                if (gun != null) {
                    gun.discard();
                }
            }
        }
    }

    private static void holdAt(LivingEntity target, double x, double y, double z) {
        target.setDeltaMovement(Vec3.ZERO);
        target.fallDistance = 0.0F;
        if (target instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.teleport(x, y, z, serverPlayer.getYRot(), serverPlayer.getXRot());
        } else {
            target.teleportTo(x, y, z);
        }
        target.hurtMarked = true;
    }

    private static void endSweetRelease(LivingEntity target, SweetRelease lock) {
        Level level = target.level();
        if (!lock.releasing && lock.storedDamage > 0.0F && target.isAlive()) {
            Entity caster = level.getEntity(lock.casterId);
            releaseStoredDamage(target, lock, caster == null ? target.damageSources().magic() : target.damageSources().indirectMagic(caster, caster), 0.0F);
        }
        ACExData.remove(target, ACExAttachments.SWEET_RELEASE);
        discardSweetReleaseEntities(level, lock);
        for (int id : lock.spawned) {
            Entity entity = level.getEntity(id);
            if (entity instanceof SugarStaffHexEntity || (entity instanceof SpinningPeppermintEntity && ACExData.get(entity, ACExAttachments.PEPPER_LOCK))) {
                entity.discard();
            }
        }
    }

    private static int spawnChipper(ServerLevel level, Entity caster, SweetRelease lock, Vec3 direction, float spin) {
        SpinningPeppermintEntity chipper = ACEntityRegistry.SPINNING_PEPPERMINT.get().create(level);
        if (chipper == null)
            return -1;
        ACExData.set(chipper, ACExAttachments.PEPPERMINT_SCALE, SWEET_RELEASE_CHIPPER_SCALE);
        chipper.refreshDimensions();
        chipper.setPos(lock.x, lock.y, lock.z);
        chipper.setStraight(true);
        ACExData.set(chipper, ACExAttachments.STAFF_PEPPERMINT, true);
        chipper.setYRot(180.0F + (float) Math.toDegrees(Mth.atan2(-direction.x, direction.z)));
        chipper.setSpinSpeed(spin);
        chipper.setNoGravity(true);
        if (caster instanceof LivingEntity living) {
            chipper.setOwner(living);
        }
        chipper.setLifespan(SWEET_RELEASE_CHIPPER_DURATION + SWEET_RELEASE_EXTRA_WORM_GRACE);
        ACExData.set(chipper, ACExAttachments.PEPPER_CHIPPER, true);
        level.addFreshEntity(chipper);
        level.playSound(null, chipper.blockPosition(), ACSoundRegistry.SUGAR_STAFF_CAST_PEPPERMINT.get(), SoundSource.PLAYERS, 2.0F, 0.5F);
        return chipper.getId();
    }

    private static Entity placeChipper(ServerLevel level, int id, Vec3 center, Vec3 side, LivingEntity target) {
        Entity chipper = id == -1 ? null : level.getEntity(id);
        if (chipper == null)
            return null;
        double radius = chipper.getBbWidth() * 0.5D;
        Vec3 wheelCenter = center.add(side.scale(radius + target.getBbWidth() * 0.5D));
        chipper.setDeltaMovement(Vec3.ZERO);
        chipper.setPos(wheelCenter.x, wheelCenter.y - chipper.getBbHeight() * 0.5D, wheelCenter.z);
        return chipper;
    }

    private static void summonWirm(ServerLevel level, LivingEntity target, SweetRelease lock, int index) {
        GumwirmEntity wirm = ACExEntityRegistry.GUMWIRM.get().create(level);
        if (wirm == null)
            return;
        float golden = (float) (Math.PI * (3.0D - Math.sqrt(5.0D)));
        float yLevel = 1.0F - (index + 0.5F) / SWEET_RELEASE_WIRMS * 2.0F;
        float ring = Mth.sqrt(1.0F - yLevel * yLevel);
        float theta = golden * index;
        Vec3 normal = new Vec3(Mth.cos(theta) * ring, yLevel, Mth.sin(theta) * ring);
        Vec3 reference = Math.abs(normal.y) > 0.9D ? new Vec3(1.0D, 0.0D, 0.0D) : new Vec3(0.0D, 1.0D, 0.0D);
        Vec3 axisU = normal.cross(reference).normalize();
        Vec3 axisV = normal.cross(axisU).normalize();
        Vec3 start = new Vec3(lock.x, lock.y - 1.0D, lock.z);
        wirm.moveTo(start.x, start.y, start.z, 0.0F, -90.0F);
        wirm.setNoAi(true);
        wirm.setLeaping(true);
        markScripted(wirm);
        ACExData.set(wirm, ACExAttachments.SCRIPTED_WORM, true);
        ACExData.set(wirm, ACExAttachments.WIRM_ORBIT, new WirmOrbit(target.getId(), axisU, axisV, level.random.nextFloat() * Mth.TWO_PI, target.getBoundingBox().getCenter()));
        ForgeEventFactory.onFinalizeSpawn(wirm, level, level.getCurrentDifficultyAt(BlockPos.containing(start)), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(wirm);
        lock.spawned.add(wirm.getId());
    }

    private static void tickWirmOrbit(GumwirmEntity wirm, WirmOrbit orbit) {
        orbit.age++;
        Level level = wirm.level();
        Vec3 head = wirm.position();
        if (orbit.path.isEmpty()) {
            orbit.path.add(head);
        }
        wirm.setLeaping(true);
        LivingEntity target = level.getEntity(orbit.targetId) instanceof LivingEntity living && living.isAlive() && ACExData.has(living, ACExAttachments.SWEET_RELEASE) ? living : null;
        if (!orbit.flung && (target == null || orbit.orbitAge >= SWEET_RELEASE_WIRM_ORBIT)) {
            orbit.flung = true;
            Vec3 outward = head.subtract(orbit.center);
            if (outward.lengthSqr() < 1.0E-4D) {
                outward = orbit.axisU;
            }
            orbit.velocity = outward.normalize().scale(SWEET_RELEASE_WIRM_FLING_SPEED).add(0.0D, SWEET_RELEASE_WIRM_FLING_LIFT, 0.0D);
        }
        Vec3 next;
        if (!orbit.flung) {
            orbit.center = target.getBoundingBox().getCenter();
            orbit.angle += SWEET_RELEASE_WIRM_ORBIT_SPEED;
            double minRadius = target.getBbWidth() * 0.5D + SWEET_RELEASE_WIRM_MIN_RADIUS;
            float closing = orbit.orbitAge < 0 ? 0.0F : Math.min(1.0F, orbit.orbitAge / (float) SWEET_RELEASE_WIRM_CLOSE_IN);
            double radius = Mth.lerp(closing, minRadius + SWEET_RELEASE_WIRM_ORBIT_RADIUS, minRadius);
            Vec3 desired = orbit.center.add(orbit.axisU.scale(Mth.cos(orbit.angle) * radius)).add(orbit.axisV.scale(Mth.sin(orbit.angle) * radius));
            if (orbit.orbitAge < 0) {
                Vec3 toward = desired.subtract(head);
                if (toward.length() <= SWEET_RELEASE_WIRM_EMERGE_SPEED) {
                    next = desired;
                    orbit.orbitAge = 0;
                } else {
                    next = head.add(toward.normalize().scale(SWEET_RELEASE_WIRM_EMERGE_SPEED));
                }
            } else {
                orbit.orbitAge++;
                next = desired;
            }
            if (orbit.age - orbit.lastBite >= SWEET_RELEASE_WIRM_BITE_INTERVAL && target.getBoundingBox().inflate(SWEET_RELEASE_WIRM_REACH).contains(next)) {
                orbit.lastBite = orbit.age;
                target.hurt(casterDamage(wirm, level.getEntity(ACExData.get(target, ACExAttachments.SWEET_RELEASE).casterId)), SWEET_RELEASE_WIRM_BITE_DAMAGE);
            }
            orbit.velocity = next.subtract(head);
        } else {
            orbit.flungAge++;
            if (orbit.buried > 0.0D) {
                orbit.velocity = new Vec3(orbit.velocity.x * 0.8D, Math.min(orbit.velocity.y, -SWEET_RELEASE_WIRM_DIG_SPEED), orbit.velocity.z * 0.8D);
            } else {
                orbit.velocity = new Vec3(orbit.velocity.x * 0.98D, Math.max(orbit.velocity.y - WORM_GRAVITY, -WORM_MAX_FALL), orbit.velocity.z * 0.98D);
            }
            next = head.add(orbit.velocity);
            if (solidAt(level, BlockPos.containing(next))) {
                orbit.buried += orbit.velocity.length();
            }
        }
        wirm.setDeltaMovement(orbit.velocity);
        wirm.setTargetDigPitch((float) -(Mth.atan2(orbit.velocity.y, orbit.velocity.horizontalDistance()) * 180.0F / (float) Math.PI));
        if (orbit.velocity.horizontalDistanceSqr() > 1.0E-4D) {
            float yaw = yawOf(orbit.velocity);
            wirm.setYRot(yaw);
            wirm.setYHeadRot(yaw);
            wirm.setYBodyRot(yaw);
        }
        wirm.setPos(next);
        orbit.path.add(next);
        if (orbit.path.size() > WORM_PATH_LIMIT) {
            orbit.path.remove(0);
        }
        if (orbit.buried > GUMWIRM_BODY_LENGTH || orbit.flungAge > GUMWIRM_DIVE_TIMEOUT || next.y <= level.getMinBuildHeight() - WORM_VOID_MARGIN) {
            wirm.discard();
        }
    }

    private static void summonWorm(ServerLevel level, LivingEntity target, SweetRelease lock, Vec3 direction) {
        GumWormEntity worm = ACEntityRegistry.GUM_WORM.get().create(level);
        if (worm == null)
            return;
        Vec3 start = new Vec3(lock.x, lock.y - WORM_DEPTH, lock.z);
        worm.moveTo(start.x, start.y, start.z, (float) Math.toDegrees(Mth.atan2(-direction.x, direction.z)), -90.0F);
        worm.setNoAi(true);
        markScripted(worm);
        ACExData.set(worm, ACExAttachments.SCRIPTED_WORM, true);
        ACExData.set(worm, ACExAttachments.WORM_SCRIPT, new WormScript(direction, lock.y, target.getId()));
        ForgeEventFactory.onFinalizeSpawn(worm, level, level.getCurrentDifficultyAt(BlockPos.containing(start)), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(worm);
        lock.spawned.add(worm.getId());
    }

    private static void tickWorm(GumWormEntity worm, WormScript script) {
        script.age++;
        Level level = worm.level();
        Entity target = level.getEntity(script.targetId);
        Vec3 head = worm.position();
        if (script.path.isEmpty()) {
            script.path.add(head);
        }
        if (!script.bitten) {
            worm.setLeaping(true);
            double goalY = target != null && target.isAlive() ? target.getBoundingBox().getCenter().y : script.groundY + 2.0D;
            Vec3 next = head.add(script.velocity);
            boolean contact = target instanceof LivingEntity living && living.isAlive() && living.getBoundingBox().inflate(1.5D).contains(next);
            if (contact) {
                bite((LivingEntity) target, worm);
            }
            if (contact || next.y >= goalY || script.age > WORM_MAX_AGE / 3) {
                script.bitten = true;
                worm.setLeaping(false);
            }
        } else {
            script.arcTicks++;
            double horizontal = WORM_ARC_SPEED * Math.min(1.0D, script.arcTicks / (double) WORM_ARC_RAMP);
            script.velocity = new Vec3(script.direction.x * horizontal, Math.max(script.velocity.y - WORM_GRAVITY, -WORM_MAX_FALL), script.direction.z * horizontal);
        }
        Vec3 next = head.add(script.velocity);
        if (script.bitten && next.y < script.groundY - 1.0D) {
            script.buried += script.velocity.length();
        }
        worm.setDeltaMovement(script.velocity);
        worm.setTargetDigPitch((float) -(Mth.atan2(script.velocity.y, script.velocity.horizontalDistance()) * 180.0F / (float) Math.PI));
        if (script.velocity.horizontalDistanceSqr() > 1.0E-4D) {
            float yaw = (float) Math.toDegrees(Mth.atan2(-script.velocity.x, script.velocity.z));
            worm.setYRot(yaw);
            worm.setYHeadRot(yaw);
            worm.setYBodyRot(yaw);
        }
        worm.setPos(next);
        script.path.add(next);
        if (script.path.size() > WORM_PATH_LIMIT) {
            script.path.remove(0);
        }
        if (script.buried > WORM_BODY_LENGTH || next.y <= level.getMinBuildHeight() - WORM_VOID_MARGIN || script.age > WORM_MAX_AGE) {
            worm.discard();
        }
    }

    private static DamageSource casterDamage(LivingEntity attacker, Entity caster) {
        return attacker.damageSources().mobProjectile(attacker, caster instanceof LivingEntity living ? living : null);
    }

    private static void hurtLocked(LivingEntity target, DamageSource source, float amount) {
        target.invulnerableTime = 0;
        target.hurt(source, amount);
    }

    private static void bite(LivingEntity living, GumWormEntity worm) {
        if (ACExData.has(living, ACExAttachments.SWEET_RELEASE)) {
            SweetRelease lock = ACExData.get(living, ACExAttachments.SWEET_RELEASE);
            DamageSource source = casterDamage(worm, living.level().getEntity(lock.casterId));
            if (!lock.releasing) {
                releaseStoredDamage(living, lock, source, WORM_BITE_DAMAGE);
                if (living.isAlive()) {
                    lock.extraWormsAt = lock.age + SWEET_RELEASE_EXTRA_WORM_DELAY;
                    lock.bitesRemaining = SWEET_RELEASE_EXTRA_WORMS;
                    lock.endAge = lock.extraWormsAt + SWEET_RELEASE_EXTRA_WORM_INTERVAL * SWEET_RELEASE_EXTRA_WORMS + SWEET_RELEASE_EXTRA_WORM_GRACE;
                } else {
                    lock.eaten = true;
                }
            } else if (!lock.eaten) {
                hurtLocked(living, source, WORM_BITE_DAMAGE);
                if (--lock.bitesRemaining <= 0) {
                    lock.eaten = true;
                }
            }
        } else {
            living.invulnerableTime = 0;
            living.hurt(worm.damageSources().mobAttack(worm), WORM_BITE_DAMAGE);
        }
    }

    private static void followPath(GumWormSegmentEntity segment, Vec3 headPos, List<Vec3> path, double headGap, double spacing) {
        double distance = headGap + spacing * (segment.getIndex() + 1);
        Vec3 ahead = headPos;
        Vec3 point = ahead;
        boolean placed = false;
        for (int i = path.size() - 1; i >= 0; i--) {
            Vec3 previous = path.get(i);
            double step = previous.distanceTo(point);
            if (step >= distance && step > 1.0E-6D) {
                ahead = point;
                point = point.add(previous.subtract(point).scale(distance / step));
                placed = true;
                break;
            }
            distance -= step;
            ahead = point;
            point = previous;
        }
        if (!placed) {
            ahead = point;
            point = point.subtract(0.0D, distance, 0.0D);
        }
        Vec3 facing = ahead.subtract(point);
        segment.setDeltaMovement(point.subtract(segment.position()));
        segment.setPos(point);
        if (facing.lengthSqr() > 1.0E-6D) {
            segment.setYRot((float) Math.toDegrees(Mth.atan2(-facing.x, facing.z)));
            segment.setXRot((float) -(Mth.atan2(facing.y, facing.horizontalDistance()) * 180.0F / (float) Math.PI));
        }
    }

    private static void spearTarget(LivingEntity target, Entity caster) {
        Level level = target.level();
        Vec3 center = target.getBoundingBox().getCenter();
        float golden = (float) (Math.PI * (3.0D - Math.sqrt(5.0D)));
        for (int i = 0; i < SWEET_RELEASE_SPEARS; i++) {
            float yLevel = 1.0F - (i + 0.5F) / SWEET_RELEASE_SPEARS * 2.0F;
            float ring = Mth.sqrt(1.0F - yLevel * yLevel);
            float theta = golden * i;
            Vec3 direction = new Vec3(Mth.cos(theta) * ring, yLevel, Mth.sin(theta) * ring);
            Vec3 from = center.add(direction.scale(SWEET_RELEASE_SPEAR_DISTANCE));
            if (!level.isEmptyBlock(BlockPos.containing(from)))
                continue;
            FrostmintSpearEntity spear = ACEntityRegistry.FROSTMINT_SPEAR.get().create(level);
            if (spear == null)
                continue;
            spear.setPos(from);
            spear.setOwner(caster);
            spear.pickup = FrostmintSpearEntity.Pickup.CREATIVE_ONLY;
            ACExData.set(spear, ACExAttachments.FROST_RADIANT, true);
            ACExData.set(spear, ACExAttachments.STAFF_SPEAR, true);
            Vec3 toward = center.subtract(from);
            spear.shoot(toward.x, toward.y, toward.z, 1.6F, 0.0F);
            level.addFreshEntity(spear);
        }
        level.playSound(null, BlockPos.containing(center), ACSoundRegistry.SUGAR_STAFF_CAST_HEX.get(), SoundSource.PLAYERS, 1.5F, 1.4F);
    }
}
