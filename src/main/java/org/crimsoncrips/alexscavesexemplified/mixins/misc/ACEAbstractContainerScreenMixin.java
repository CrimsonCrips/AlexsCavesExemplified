package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Collections;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

@Mixin(AbstractContainerScreen.class)
public abstract class ACEAbstractContainerScreenMixin<T extends AbstractContainerMenu> extends Screen implements MenuAccess<T> {


    @Shadow @Final protected T menu;

    protected ACEAbstractContainerScreenMixin(Component pTitle) {
        super(pTitle);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;)V"))
    private void alexsCavesExemplified$renderHotbar1(AbstractContainerScreen instance, GuiGraphics pGuiGraphics, Slot i1, Operation<Void> original, @Local(ordinal = 4) int k) {
        ItemStack itemStack = this.menu.slots.get(k).getItem();
        if(magneticMove(itemStack)){
            int t = minecraft.player.tickCount;
            double speed = 0.1;
            pGuiGraphics.pose().pushPose();

            //Thank you Reimnop for the giga nerd math code
            double x = -sin(speed * t) * cos(0.1 * t + (k + itemStack.getBarWidth()) * 4638.361D + 164.35D) + cos(speed * t);
            double y = cos(speed * t) * cos(0.1 * t + (k + itemStack.getBarWidth()) * 4638.361D + 364.35D) + sin(speed * t);
            pGuiGraphics.pose().translate(x, y, 0);
        }
        original.call(instance, pGuiGraphics, i1);
        if(magneticMove(itemStack)){
            pGuiGraphics.pose().popPose();
        }
    }



    public boolean magneticMove(ItemStack itemStack){
        return itemStack.is(ACTagRegistry.MAGNETIC_ITEMS) && minecraft.player.level().getBiome(minecraft.player.blockPosition()).is(ACBiomeRegistry.MAGNETIC_CAVES) && AlexsCavesExemplified.CLIENT_CONFIG.MAGNETIC_MOVEMENT_ENABLED.get();
    }
}