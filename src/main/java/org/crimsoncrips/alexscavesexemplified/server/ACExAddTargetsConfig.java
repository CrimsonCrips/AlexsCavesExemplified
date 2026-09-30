package org.crimsoncrips.alexscavesexemplified.server;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ACExAddTargetsConfig {

    public final ModConfigSpec.BooleanValue LICOWITCH_ENABLED;
    public final ModConfigSpec.BooleanValue DEEP_ONES_ENABLED;
    public final ModConfigSpec.BooleanValue GROTTOCERATOPS_ENABLED;
    public final ModConfigSpec.BooleanValue VESPER_ENABLED;
    public final ModConfigSpec.BooleanValue RELICHERIRUS_ENABLED;


    public ACExAddTargetsConfig(final ModConfigSpec.Builder builder) {

        builder.push("Alexs Caves");
        this.LICOWITCH_ENABLED = buildMob(builder, "LICOWITCH_ENABLED");
        this.DEEP_ONES_ENABLED = buildMob(builder, "DEEP_ONES_ENABLED");
        this.GROTTOCERATOPS_ENABLED = buildMob(builder, "GROTTOCERATOPS_ENABLED");
        this.VESPER_ENABLED = buildMob(builder, "VESPER_ENABLED");
        this.RELICHERIRUS_ENABLED = buildMob(builder, "RELICHERIRUS_ENABLED");
        builder.pop();

    }

    private static ModConfigSpec.BooleanValue buildBoolean(ModConfigSpec.Builder builder, String name, boolean defaultValue,String comment){
        return builder.translation(name).define(name, defaultValue);
    }

    private static ModConfigSpec.BooleanValue buildMob(ModConfigSpec.Builder builder, String name){
        return builder.translation(name).define(name, true);
    }

}
