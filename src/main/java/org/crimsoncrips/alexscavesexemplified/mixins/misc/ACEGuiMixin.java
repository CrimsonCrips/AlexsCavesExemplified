package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


@Mixin(Gui.class)
public abstract class ACEGuiMixin {

    @Shadow @Final protected Minecraft minecraft;

    @Shadow @Final protected RandomSource random;


    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V",ordinal = 0))
    private void alexsCavesExemplified$renderHotbar1(Gui instance, GuiGraphics pGuiGraphics, int j1, int k1, float pX, Player player, ItemStack pPartialTick, int l, Operation<Void> original,@Local(ordinal = 4) int i1) {
        if(magneticMove(player.getInventory().items.get(i1))){
            int t = minecraft.player.tickCount;
            double speed = 0.1;
            pGuiGraphics.pose().pushPose();

            //Thank you Reimnop for the giga nerd math code
            double x = -sin(speed * t) * cos(0.1 * t + i1 * 4638.361D + 164.35D) + cos(speed * t);
            double y = cos(speed * t) * cos(0.2 * t + i1 * 4638.361D + 364.35D) + sin(speed * t);
            pGuiGraphics.pose().translate(x, y, 0);
        }
        original.call(instance, pGuiGraphics, j1, k1, pX, player, pPartialTick, l);
        if(magneticMove(player.getInventory().items.get(i1))){
            pGuiGraphics.pose().popPose();
        }
    }


    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V",ordinal = 1))
    private void alexsCavesExemplified$renderHotbar(Gui instance, GuiGraphics pGuiGraphics, int i, int f, float pX, Player pY, ItemStack itemStack, int pPlayer, Operation<Void> original) {
        if(magneticMove(itemStack)){
            int t = minecraft.player.tickCount;
            double speed = 0.1;
            pGuiGraphics.pose().pushPose();

            //Thank you Reimnop for the giga nerd math code
            double x = -sin(speed * t) * cos(0.1 * t + itemStack.getBarWidth() * 4638.361D + 164.35D) + cos(speed * t);
            double y = cos(speed * t) * cos(0.2 * t + itemStack.getBarWidth() * 4638.361D + 364.35D) + sin(speed * t);
            pGuiGraphics.pose().translate(x, y, 0);
        }
        original.call(instance, pGuiGraphics, i, f, pX, pY, itemStack, pPlayer);
        if(magneticMove(itemStack)){
            pGuiGraphics.pose().popPose();
        }
    }









    public boolean magneticMove(ItemStack itemStack){
        return itemStack.is(ACTagRegistry.MAGNETIC_ITEMS) && minecraft.player.level().getBiome(minecraft.player.blockPosition()).is(ACBiomeRegistry.MAGNETIC_CAVES) && AlexsCavesExemplified.CLIENT_CONFIG.MAGNETIC_MOVEMENT_ENABLED.get();
    }


    



}