package org.crimsoncrips.alexscavesexemplified.compat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExItemTagGenerator;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCompat {

    private static final boolean CURIOS_LOADED = ModList.get().isLoaded("curios");

    public static boolean hasLight(LivingEntity target) {
        return target.isHolding(CuriosCompat::isLight) || CURIOS_LOADED && curiosLight(target);
    }

    public static boolean repelledByLight(Mob mob, LivingEntity target) {
        return AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get() && target != mob.getLastHurtByMob() && hasLight(target);
    }

    private static boolean isLight(ItemStack stack) {
        return stack.is(ACExItemTagGenerator.LIGHT);
    }

    private static boolean curiosLight(LivingEntity target) {
        return CuriosApi.getCuriosInventory(target).map(handler -> handler.isEquipped(CuriosCompat::isLight)).orElse(false);
    }
}
