package org.crimsoncrips.alexscavesexemplified.client;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ACExClientConfig {

    public final ModConfigSpec.BooleanValue MAGNETIC_MOVEMENT_ENABLED;


    public ACExClientConfig(final ModConfigSpec.Builder builder) {
        builder.push("visuals");
        this.MAGNETIC_MOVEMENT_ENABLED = buildBoolean(builder, "MAGNETIC_MOVEMENT_ENABLED", " ", true, "Whether Magnetic items move around your inventory when in magnetic caves");

        builder.pop();

    }

    private static ModConfigSpec.BooleanValue buildBoolean(ModConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }
}
