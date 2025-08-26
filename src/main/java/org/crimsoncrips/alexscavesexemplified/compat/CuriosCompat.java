package org.crimsoncrips.alexscavesexemplified.compat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExItemTagGenerator;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class CuriosCompat {
    public static boolean hasLight(LivingEntity target){
        return target.isHolding(Ingredient.of(ACExItemTagGenerator.LIGHT)) || curiosLight(target);
    }

    private static boolean curiosLight(LivingEntity target){
        if (ModList.get().isLoaded("curiouslanterns")) {
            try{
                ICuriosItemHandler handler = CuriosApi.getCuriosInventory(target).orElseThrow(() -> new IllegalStateException("Target " + target.getName() + " has no curios inventory!"));
                return handler.getStacksHandler("belt").orElseThrow().getStacks().getStackInSlot(0).is(ACExItemTagGenerator.LIGHT);

            } catch (Exception e) {
                return false;
            }
        } else return false;
    }

}
