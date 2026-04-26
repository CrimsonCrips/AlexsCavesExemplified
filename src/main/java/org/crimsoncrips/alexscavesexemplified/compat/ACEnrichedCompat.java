package org.crimsoncrips.alexscavesexemplified.compat;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import net.hellomouse.alexscavesenriched.ACEEntityRegistry;
import net.hellomouse.alexscavesenriched.AlexsCavesEnriched;
import net.hellomouse.alexscavesenriched.entity.NuclearExplosion2Entity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;

public class ACEnrichedCompat {

    public static void summonNuclearExplosion2(Level level, Entity entity){
        NuclearExplosion2Entity enrichedNuke = (NuclearExplosion2Entity) ((EntityType<?>) ACEEntityRegistry.NUCLEAR_EXPLOSION2.get()).create(level);
        enrichedNuke.setSize(AlexsCaves.COMMON_CONFIG.nukeExplosionSizeModifier.get().floatValue() * 1.3F);
        ((Gammafied) enrichedNuke).setGamma(true);
        enrichedNuke.copyPosition(entity);
        level.addFreshEntity(enrichedNuke);
    }

    public static boolean config(){
        return AlexsCavesEnriched.CONFIG.nuclear.useNewNuke;
    }

}