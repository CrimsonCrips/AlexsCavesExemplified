package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


@Mixin(Gui.class)
public abstract class ACEGuiMixin {


    @Shadow @Final protected Minecraft minecraft;

    @Shadow @Final protected RandomSource random;

    @Shadow protected int screenWidth;

    @Shadow protected ItemStack lastToolHighlight;

    @Shadow public abstract Font getFont();

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

    @Inject(method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
    private void alexsCavesExemplified$renderslectedItemName2(GuiGraphics pGuiGraphics, int yShift, CallbackInfo ci, @Local(ordinal = 1) int i, @Local(ordinal = 2) int j, @Local(ordinal = 3) int k, @Local(ordinal = 4) int l){
        Minecraft minecraft = this.minecraft;
        if (minecraft.player != null){
            //Thanks Drullkus for the help and TerriblyBadCoder for the inspiration
            float time = minecraft.player.tickCount;
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.fill(j - 2, k - 2, j + i + 2, k + 9 + 2, this.minecraft.options.getBackgroundColor(0));
            Font font = IClientItemExtensions.of(lastToolHighlight).getFont(lastToolHighlight, IClientItemExtensions.FontContext.SELECTED_ITEM_NAME);

            MutableComponent mutablecomponent = Component.empty().append(this.lastToolHighlight.getHoverName()).withStyle(this.lastToolHighlight.getRarity().getStyleModifier());
            if (this.lastToolHighlight.hasCustomHoverName()) {
                mutablecomponent.withStyle(ChatFormatting.ITALIC);
            }

            Component component = this.lastToolHighlight.getHighlightTip(mutablecomponent);

            if(lastToolHighlight.getItem() == ACExBlockRegistry.GAMMA_NUCLEAR_BOMB.get().asItem()){
                for (int t = 6; t > 1; t--) {
                    float distance = -5f;
                    float speed = 10; //higher == slower
                    pGuiGraphics.pose().pushPose();
                    float movement = ((float) (Math.sin((time / speed) + t / 10.0)) * distance);
                    pGuiGraphics.pose().translate(movement, 0, 0);
                    int newl = l / (t);

                    int jC = (this.screenWidth - this.getFont().width(component)) / 2;


                    if (font == null) {
                        pGuiGraphics.drawString(this.getFont(), component, jC, k, 16777215 + (newl << 24));
                    } else {
                        jC = (this.screenWidth - font.width(component)) / 2;
                        pGuiGraphics.drawString(font, component, jC, k, 16777215 + (newl << 24));
                    }
                    pGuiGraphics.pose().popPose();
                }
            }

            pGuiGraphics.pose().popPose();
        }
    }



    @WrapWithCondition(method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"))
    private boolean alexsCavesExemplified$renderSelectedItemName(GuiGraphics instance, Font pFont, Component pText, int pX, int pY, int pColor){
        return this.lastToolHighlight.getItem() != ACExBlockRegistry.GAMMA_NUCLEAR_BOMB.get().asItem();
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