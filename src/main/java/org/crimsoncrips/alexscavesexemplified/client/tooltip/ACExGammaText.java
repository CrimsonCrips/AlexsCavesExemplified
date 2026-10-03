package org.crimsoncrips.alexscavesexemplified.client.tooltip;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;

@OnlyIn(Dist.CLIENT)
public class ACExGammaText {

    public static final int SWAY = 5;

    public static boolean isGammaBomb(ItemStack stack) {
        return stack.is(ACExBlockRegistry.GAMMA_NUCLEAR_BOMB.get().asItem());
    }

    public static void draw(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int alpha) {
        float time = Util.getMillis() / 50.0F;
        for (int t = 6; t > 1; t--) {
            float movement = Mth.sin(time / 10.0F + t / 10.0F) * -SWAY;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(movement, 0, 0);
            guiGraphics.drawString(font, text, x, y, 0xFFFFFF | ((alpha / t) << 24));
            guiGraphics.pose().popPose();
        }
    }
}
