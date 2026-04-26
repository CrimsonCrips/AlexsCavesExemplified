package org.crimsoncrips.alexscavesexemplified.server;

import net.minecraftforge.common.ForgeConfigSpec;

public class ACExAddTargetsConfig {

    public final ForgeConfigSpec.BooleanValue LICOWITCH_ENABLED;
    public final ForgeConfigSpec.BooleanValue DEEP_ONES_ENABLED;
    public final ForgeConfigSpec.BooleanValue GROTTOCERATOPS_ENABLED;
    public final ForgeConfigSpec.BooleanValue VESPER_ENABLED;
    public final ForgeConfigSpec.BooleanValue RELICHERIRUS_ENABLED;


    public ACExAddTargetsConfig(final ForgeConfigSpec.Builder builder) {

        builder.push("Alexs Caves");
        this.LICOWITCH_ENABLED = buildMob(builder, "LICOWITCH_ENABLED");
        this.DEEP_ONES_ENABLED = buildMob(builder, "DEEP_ONES_ENABLED");
        this.GROTTOCERATOPS_ENABLED = buildMob(builder, "GROTTOCERATOPS_ENABLED");
        this.VESPER_ENABLED = buildMob(builder, "VESPER_ENABLED");
        this.RELICHERIRUS_ENABLED = buildMob(builder, "RELICHERIRUS_ENABLED");
        builder.pop();

    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, boolean defaultValue,String comment){
        return builder.translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.BooleanValue buildMob(ForgeConfigSpec.Builder builder, String name){
        return builder.translation(name).define(name, true);
    }

}
