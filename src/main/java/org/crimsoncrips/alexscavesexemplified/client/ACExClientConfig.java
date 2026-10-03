package org.crimsoncrips.alexscavesexemplified.client;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;

import java.util.List;

public class ACExClientConfig {

    public final ModConfigSpec.BooleanValue MAGNETIC_MOVEMENT_ENABLED;
    public final ModConfigSpec.BooleanValue GAMMA_BOMB_GLOW_ENABLED;
    public final ModConfigSpec.ConfigValue<List<? extends String>> WHEEL_LAYOUT;


    public ACExClientConfig(final ModConfigSpec.Builder builder) {
        builder.push("visuals");
        this.MAGNETIC_MOVEMENT_ENABLED = buildBoolean(builder, "MAGNETIC_MOVEMENT_ENABLED", " ", true, "Whether Magnetic items move around your inventory when in magnetic caves");
        this.GAMMA_BOMB_GLOW_ENABLED = buildBoolean(builder, "GAMMA_BOMB_GLOW_ENABLED", " ", true, "Whether the Gamma Nuclear Bomb item pulses with a blue radiation glow");

        builder.pop();
        builder.push("sugar_staff_wheel");
        this.WHEEL_LAYOUT = builder.comment("The abilities shown on the Sugar Staff wheel, in order").translation("WHEEL_LAYOUT").defineListAllowEmpty("WHEEL_LAYOUT", ACExSugarStaffAbilities.defaultLayout(), () -> "peppermints", value -> value instanceof String key && ACExSugarStaffAbilities.abilityFromKey(key) >= 0);
        builder.pop();

    }

    private static ModConfigSpec.BooleanValue buildBoolean(ModConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }
}
