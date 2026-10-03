package org.crimsoncrips.alexscavesexemplified.client.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record ACExGammaTooltip(Component text) implements TooltipComponent, ClientTooltipComponent {

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public int getWidth(Font font) {
        return font.width(text) + ACExGammaText.SWAY * 2;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        ACExGammaText.draw(guiGraphics, font, text, x + ACExGammaText.SWAY, y, 255);
    }
}
