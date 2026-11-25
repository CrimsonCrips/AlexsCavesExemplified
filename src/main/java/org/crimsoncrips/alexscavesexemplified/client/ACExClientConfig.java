package org.crimsoncrips.alexscavesexemplified.client;

import net.minecraftforge.common.ForgeConfigSpec;
import org.crimsoncrips.alexscavesexemplified.client.submarine.ACExSonarQuality;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;

import java.util.List;

public class ACExClientConfig {

    public final ForgeConfigSpec.BooleanValue MAGNETIC_MOVEMENT_ENABLED;
    public final ForgeConfigSpec.BooleanValue GAMMA_BOMB_GLOW_ENABLED;
    public final ForgeConfigSpec.BooleanValue EXEMPLIFIED_COMPENDIUM_ENABLED;
    public final ForgeConfigSpec.BooleanValue SUBMARINE_SONAR_ENABLED;
    public final ForgeConfigSpec.EnumValue<ACExSonarQuality> SUBMARINE_SONAR_QUALITY;
    public final ForgeConfigSpec.BooleanValue SUBMARINE_TELEGRAPH_ENABLED;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> WHEEL_LAYOUT;


    public ACExClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("visuals");
        this.MAGNETIC_MOVEMENT_ENABLED = buildBoolean(builder, "MAGNETIC_MOVEMENT_ENABLED", true, "Whether Magnetic items move around your inventory when in magnetic caves");
        this.GAMMA_BOMB_GLOW_ENABLED = buildBoolean(builder, "GAMMA_BOMB_GLOW_ENABLED", true, "Whether the Gamma Nuclear Bomb item pulses with a blue radiation glow");
        this.EXEMPLIFIED_COMPENDIUM_ENABLED = buildBoolean(builder, "EXEMPLIFIED_COMPENDIUM_ENABLED", true, "Whether the Cave Compendium has a button that rewrites it into the Exemplified Compendium");
        this.SUBMARINE_SONAR_ENABLED = buildBoolean(builder, "SUBMARINE_SONAR_ENABLED", true, "Whether pressing the Submarine Sonar key while piloting a submarine sends out a sonar pulse that briefly reveals the surroundings, even in darkness");
        this.SUBMARINE_SONAR_QUALITY = builder.comment("Quality of the submarine sonar effect, HIGH shades surfaces and traces sharp edges, LOW is cheaper with a flat tint and softer edges").translation("SUBMARINE_SONAR_QUALITY").defineEnum("SUBMARINE_SONAR_QUALITY", ACExSonarQuality.HIGH);
        this.SUBMARINE_TELEGRAPH_ENABLED = buildBoolean(builder, "SUBMARINE_TELEGRAPH_ENABLED", true, "Whether pressing the Submarine Telegraph key while piloting a submarine switches to engine orders, where forward and back step through Astern, All Stop, Ahead Slow, Half, Full and Flank and the submarine holds that speed on its own, every order below Flank stays under the speed that alerts Deep Ones and Hullbreakers");

        builder.pop();
        builder.push("sugar_staff_wheel");
        this.WHEEL_LAYOUT = builder.comment("The abilities shown on the Sugar Staff wheel, in order").translation("WHEEL_LAYOUT").defineListAllowEmpty("WHEEL_LAYOUT", ACExSugarStaffAbilities.defaultLayout(), value -> value instanceof String key && ACExSugarStaffAbilities.abilityFromKey(key) >= 0);
        builder.pop();

    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }
}
