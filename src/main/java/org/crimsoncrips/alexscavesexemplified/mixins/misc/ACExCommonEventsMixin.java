package org.crimsoncrips.alexscavesexemplified.mixins.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExItemData;
import com.github.alexmodguy.alexscaves.server.event.CommonEvents;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(CommonEvents.class)
public class ACExCommonEventsMixin {


    @Inject(method = "checkAndDestroyExploitItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"), cancellable = true)
    private static void alexsCavesExemplified$checkAndDestroyExploitItem(Player player, EquipmentSlot slot, CallbackInfo ci, @Local ItemStack itemInHand) {
        if(AlexsCavesExemplified.COMMON_CONFIG.POWERED_LOCATORS_ENABLED.get()){
            if(!itemInHand.getOrCreateTag().getBoolean("WitherProtection")){
                if (slot == EquipmentSlot.MAINHAND) {
                    ItemStack offHand = player.getItemBySlot(EquipmentSlot.OFFHAND);
                    if (offHand.is(Items.NETHER_STAR)) {
                        itemInHand.enchant(Enchantments.UNBREAKING, 10);
                        ACExItemData.update(itemInHand, tag -> tag.putBoolean("WitherProtection", true));

                        player.playSound(SoundEvents.WITHER_DEATH);
                        player.broadcastBreakEvent(EquipmentSlot.OFFHAND);
                        offHand.shrink(1);
                        if (!player.level().isClientSide) {
                            player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.locator_protection"), true);
                        }
                        ci.cancel();
                    }
                } else if (slot == EquipmentSlot.OFFHAND) {
                    ItemStack mainHand = player.getItemBySlot(EquipmentSlot.MAINHAND);
                    if (mainHand.is(Items.NETHER_STAR)) {
                        itemInHand.enchant(Enchantments.UNBREAKING, 10);
                        ACExItemData.update(itemInHand, tag -> tag.putBoolean("WitherProtection", true));

                        player.playSound(SoundEvents.WITHER_DEATH);
                        player.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                        mainHand.shrink(1);
                        if (!player.level().isClientSide) {
                            player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.locator_protection"), true);
                        }
                        ci.cancel();
                    }
                }
            } else {
                ci.cancel();
            }
        }
    }

}
