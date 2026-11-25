package org.crimsoncrips.alexscavesexemplified.server.blocks;

import com.github.alexmodguy.alexscaves.server.entity.ACFrogRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;

import javax.annotation.Nullable;

public enum PreservedCreature implements StringRepresentable {
    TEMPERATE_FROG("temperate_frog", false),
    WARM_FROG("warm_frog", false),
    COLD_FROG("cold_frog", false),
    PRIMORDIAL_FROG("primordial_frog", false),
    TADPOLE("tadpole", false),
    COCKROACH("cockroach", true),
    BABY_COCKROACH("baby_cockroach", true),
    FLY("fly", true),
    BABY_FLY("baby_fly", true);

    private static final PreservedCreature[] FROGS = {TEMPERATE_FROG, WARM_FROG, COLD_FROG, PRIMORDIAL_FROG};

    private final String name;
    private final boolean alexsMobs;

    PreservedCreature(String name, boolean alexsMobs) {
        this.name = name;
        this.alexsMobs = alexsMobs;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public boolean isAvailable() {
        return !this.alexsMobs || ModList.get().isLoaded("alexsmobs");
    }

    public Component getDisplayName() {
        return Component.translatable("block." + AlexsCavesExemplified.MODID + ".preserved_amber." + this.name);
    }

    @Nullable
    public static PreservedCreature random(RandomSource random) {
        return switch (random.nextInt(4)) {
            case 0 -> {
                if (!ModList.get().isLoaded("alexsmobs"))
                    yield null;
                boolean baby = random.nextBoolean();
                yield random.nextBoolean() ? (baby ? BABY_COCKROACH : COCKROACH) : (baby ? BABY_FLY : FLY);
            }
            case 1 -> FROGS[random.nextInt(FROGS.length)];
            default -> TADPOLE;
        };
    }

    @Nullable
    public Mob create(ServerLevel level) {
        if (!this.isAvailable())
            return null;
        return switch (this) {
            case TEMPERATE_FROG -> frog(level, FrogVariant.TEMPERATE);
            case WARM_FROG -> frog(level, FrogVariant.WARM);
            case COLD_FROG -> frog(level, FrogVariant.COLD);
            case PRIMORDIAL_FROG -> frog(level, ACFrogRegistry.PRIMORDIAL.get());
            case TADPOLE -> EntityType.TADPOLE.create(level);
            case COCKROACH, BABY_COCKROACH -> AMCompat.createPreserved(level, false, this == BABY_COCKROACH);
            case FLY, BABY_FLY -> AMCompat.createPreserved(level, true, this == BABY_FLY);
        };
    }

    @Nullable
    private static Frog frog(ServerLevel level, FrogVariant variant) {
        Frog frog = EntityType.FROG.create(level);
        if (frog != null) {
            frog.setVariant(variant);
        }
        return frog;
    }
}
