package org.crimsoncrips.alexscavesexemplified.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ACExClientConfig {

    public final ForgeConfigSpec.BooleanValue MAGNETIC_MOVEMENT_ENABLED;
    public final ForgeConfigSpec.BooleanValue PATCHOULI_REMINDER_ENABLED;


    public ACExClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("visuals");
        this.MAGNETIC_MOVEMENT_ENABLED = buildBoolean(builder, "MAGNETIC_MOVEMENT_ENABLED", " ", true, "Whether Magnetic items move around your inventory when in magnetic caves");
        this.PATCHOULI_REMINDER_ENABLED = buildBoolean(builder, "PATCHOULI_REMINDER_ENABLED", " ", true, "Patchouli Reminder");

        builder.pop();

    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }
}
