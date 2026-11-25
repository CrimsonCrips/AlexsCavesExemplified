package org.crimsoncrips.alexscavesexemplified.server;

import net.minecraftforge.common.ForgeConfigSpec;

public class ACExAddTargetsConfig {

    public final ForgeConfigSpec.BooleanValue LICOWITCH_ENABLED;
    public final ForgeConfigSpec.BooleanValue DEEP_ONES_ENABLED;
    public final ForgeConfigSpec.BooleanValue VESPER_ENABLED;


    public ACExAddTargetsConfig(final ForgeConfigSpec.Builder builder) {

        builder.push("Alexs Caves");
        this.LICOWITCH_ENABLED = buildMob(builder, "LICOWITCH_ENABLED", "Licowitches attack mobs in the alexscavesexemplified:licowitch_hate entity tag");
        this.DEEP_ONES_ENABLED = buildMob(builder, "DEEP_ONES_ENABLED", "Deep ones hunt lanternfish and tripodfish");
        this.VESPER_ENABLED = buildMob(builder, "VESPER_ENABLED", "Vespers hunt mobs in the alexscavesexemplified:vesper_hunt entity tag");
        builder.pop();

    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, boolean defaultValue,String comment){
        return builder.translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.BooleanValue buildMob(ForgeConfigSpec.Builder builder, String name, String comment){
        return builder.comment(comment).translation(name).define(name, true);
    }

}
