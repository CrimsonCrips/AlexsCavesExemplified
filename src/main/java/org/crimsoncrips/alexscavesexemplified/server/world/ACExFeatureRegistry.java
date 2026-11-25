package org.crimsoncrips.alexscavesexemplified.server.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExFeatureRegistry {

    public static final DeferredRegister<Feature<?>> DEF_REG = DeferredRegister.create(Registries.FEATURE, AlexsCavesExemplified.MODID);

    public static final RegistryObject<ChopPileFeature> CHOP_PILE = DEF_REG.register("chop_pile", ChopPileFeature::new);
}
