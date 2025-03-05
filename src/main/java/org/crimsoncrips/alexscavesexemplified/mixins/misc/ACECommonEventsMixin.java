package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.AcidBlock;
import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.github.alexmodguy.alexscaves.server.event.CommonEvents;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.client.event.ScreenEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.NucleeperXtra;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(CommonEvents.class)
public class ACECommonEventsMixin {


    @Inject(method = "checkAndDestroyExploitItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"), cancellable = true)
    private static void onStep(Player player, EquipmentSlot slot, CallbackInfo ci, @Local ItemStack itemInHand) {
        if(AlexsCavesExemplified.COMMON_CONFIG.POWERED_LOCATORS_ENABLED.get()){
            if(!itemInHand.getOrCreateTag().getBoolean("WitherProtection")){
                if (slot == EquipmentSlot.MAINHAND) {
                    ItemStack offHand = player.getItemBySlot(EquipmentSlot.OFFHAND);
                    if (offHand.is(Items.NETHER_STAR)) {
                        itemInHand.enchant(Enchantments.UNBREAKING, 10);
                        itemInHand.getOrCreateTag().putBoolean("WitherProtection", true);

                        player.playSound(SoundEvents.WITHER_DEATH);
                        player.broadcastBreakEvent(EquipmentSlot.OFFHAND);
                        offHand.shrink(1);
                        if (!player.level().isClientSide) {
                            player.displayClientMessage(Component.translatable("item.alexscavesexemplified.locator_protection"), true);
                        }
                        ci.cancel();
                    }
                } else if (slot == EquipmentSlot.OFFHAND) {
                    ItemStack mainHand = player.getItemBySlot(EquipmentSlot.MAINHAND);
                    if (mainHand.is(Items.NETHER_STAR)) {
                        itemInHand.enchant(Enchantments.UNBREAKING, 10);
                        itemInHand.getOrCreateTag().putBoolean("WitherProtection", true);

                        player.playSound(SoundEvents.WITHER_DEATH);
                        player.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                        mainHand.shrink(1);
                        if (!player.level().isClientSide) {
                            player.displayClientMessage(Component.translatable("item.alexscavesexemplified.locator_protection"), true);
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
