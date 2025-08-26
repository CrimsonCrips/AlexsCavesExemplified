package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil.*;


@Mixin(MagnetUtil.class)
public class ACExMagnetUtilMixin {


    @Inject(method = "tickMagnetism", at = @At(value = "TAIL"), remap = false)
    private static void alexsCavesExemplified$tickMagnetism(Entity entity, CallbackInfo ci) {
        if(getEntityMagneticDelta(entity) != Vec3.ZERO){
            if (AlexsCavesExemplified.COMMON_CONFIG.MAGNERIP_ENABLED.get() && entity instanceof Player player) {
                if (AlexsCavesExemplified.COMMON_CONFIG.HARDCORE_MAGNERIP_ENABLED.get()) {
                    Inventory inv = player.getInventory();
                    for (int i = 0; i < inv.getContainerSize(); i++) {
                        ItemStack current = inv.getItem(i);
                        if (current.is(ACTagRegistry.MAGNETIC_ITEMS)) {
                            player.playSound(ACSoundRegistry.MAGNETRON_HURT.get(),0.5f,1);
                            ACExUtils.dropMagneticItem(player,current);
                            inv.setItem(i, ItemStack.EMPTY);
                        }
                    }
                } else {
                    EquipmentSlot equipmentSlot =  player.getRandom().nextBoolean() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                    ItemStack current = player.getItemBySlot(equipmentSlot);
                    if (current.is(ACTagRegistry.MAGNETIC_ITEMS) && player.hasEffect(MobEffects.WEAKNESS)) {
                        player.playSound(ACSoundRegistry.MAGNETRON_HURT.get(),0.5f,1);
                        ACExUtils.dropMagneticItem(player,current);
                        player.setItemSlot(equipmentSlot, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    @ModifyReturnValue(method = "isDynamicallyMagnetic", at = @At("RETURN"),remap = false)
    private static boolean alexsMobsInteraction$isDynamicallyMagnetic(boolean original, @Local LivingEntity entity) {
        if (AlexsCavesExemplified.COMMON_CONFIG.HARDCORE_MAGNERIP_ENABLED.get()){
            boolean hardMagnetism = false;

            if (entity instanceof Player player) {
                Inventory inv = player.getInventory();

                for (int i = 0; i < inv.getContainerSize(); i++) {
                    ItemStack current = inv.getItem(i);
                    if (current.is(ACTagRegistry.MAGNETIC_ITEMS)){
                        hardMagnetism = true;
                    }
                }
            }
            return hardMagnetism;
        }
        return original;
    }

}
