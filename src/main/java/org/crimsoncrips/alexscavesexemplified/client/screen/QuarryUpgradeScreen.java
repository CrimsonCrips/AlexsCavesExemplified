package org.crimsoncrips.alexscavesexemplified.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExQuarryUpgrades;
import org.crimsoncrips.alexscavesexemplified.server.menu.QuarryUpgradeMenu;

@OnlyIn(Dist.CLIENT)
public class QuarryUpgradeScreen extends AbstractContainerScreen<QuarryUpgradeMenu> {

    private static final int PANEL_COLOR = 0xE0202020;
    private static final int OUTLINE_COLOR = 0xFF808080;
    private static final int SLOT_COLOR = 0xFF404040;
    private static final int TEXT_COLOR = 0xFFFFFF;
    private static final int TITLE_Y = 8;
    private static final int STATUS_Y = 44;

    public QuarryUpgradeScreen(QuarryUpgradeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = QuarryUpgradeMenu.HOTBAR_Y + 24;
        this.inventoryLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, PANEL_COLOR);
        graphics.renderOutline(x, y, this.imageWidth, this.imageHeight, OUTLINE_COLOR);
        for (Slot slot : this.menu.slots) {
            int slotX = x + slot.x - 1;
            int slotY = y + slot.y - 1;
            graphics.fill(slotX, slotY, slotX + 18, slotY + 18, SLOT_COLOR);
            graphics.renderOutline(slotX, slotY, 18, 18, OUTLINE_COLOR);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        int centerX = this.leftPos + this.imageWidth / 2;
        graphics.drawCenteredString(this.font, this.getTitle(), centerX, this.topPos + TITLE_Y, TEXT_COLOR);
        graphics.drawCenteredString(this.font, this.status(), centerX, this.topPos + STATUS_Y, TEXT_COLOR);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }

    private Component status() {
        ACExQuarryUpgrades upgrades = this.menu.getUpgrades();
        int speed = Math.round((ACExQuarryUpgrades.speedMultiplier(upgrades.azure()) - 1.0F) * 100.0F);
        if (upgrades.scarlet() <= 0)
            return Component.translatable("container." + AlexsCavesExemplified.MODID + ".quarry_upgrades.speed", speed);
        return Component.translatable("container." + AlexsCavesExemplified.MODID + ".quarry_upgrades.status", speed, Component.translatable("enchantment.level." + upgrades.scarlet()));
    }
}
