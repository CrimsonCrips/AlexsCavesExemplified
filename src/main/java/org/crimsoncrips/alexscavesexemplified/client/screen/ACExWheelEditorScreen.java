package org.crimsoncrips.alexscavesexemplified.client.screen;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.BOX_HEIGHT;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.BOX_WIDTH;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.OFFSET_X;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.OFFSET_Y;

@OnlyIn(Dist.CLIENT)
public class ACExWheelEditorScreen extends Screen {

    private static final int WHEEL_TOP = 32;
    private static final int TRAY_SIZE = 22;
    private static final int TRAY_GAP = 3;
    private static final int TRAY_BOTTOM_MARGIN = 58;
    private static final int TRAY_LABEL_GAP = 12;
    private static final double DRAG_THRESHOLD = 3.0D;

    private final Screen parent;
    private final ItemStack[] icons = ACExSugarStaffWheelScreen.createIcons();
    private final ItemStack chargeIcon = new ItemStack(ACItemRegistry.RADIANT_ESSENCE.get());
    private final List<Integer> layout = new ArrayList<>();
    private int heldAbility = -1;
    private int heldSlot = -1;
    private boolean dragging;
    private double pressX;
    private double pressY;

    public ACExWheelEditorScreen(Screen parent) {
        super(Component.translatable("misc.alexscavesexemplified.wheel_editor_title"));
        this.parent = parent;
        for (int ability : ACExSugarStaffWheelScreen.layout()) {
            this.layout.add(ability);
        }
    }

    @Override
    protected void init() {
        int buttonY = this.height - 26;
        this.addRenderableWidget(Button.builder(Component.translatable("misc.alexscavesexemplified.wheel_editor_reset"), b -> {
            this.layout.clear();
            for (int ability = 0; ability < ACExSugarStaffAbilities.ABILITY_COUNT; ability++) {
                this.layout.add(ability);
            }
        }).bounds(this.width / 2 - 154, buttonY, 100, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> {
            ACExSugarStaffWheelScreen.saveLayout(this.layout);
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 - 50, buttonY, 100, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, b -> this.onClose())
                .bounds(this.width / 2 + 54, buttonY, 100, 20).build());
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    private int trayTop() {
        return this.height - TRAY_BOTTOM_MARGIN;
    }

    private int centerX() {
        return this.width / 2;
    }

    private int centerY() {
        return (WHEEL_TOP + trayTop() - TRAY_LABEL_GAP) / 2;
    }

    private float wheelScale() {
        float fitHeight = (trayTop() - TRAY_LABEL_GAP - WHEEL_TOP) / (float) (OFFSET_Y * 2 + BOX_HEIGHT + 4);
        float fitWidth = (this.width - 20) / (float) (OFFSET_X * 2 + BOX_WIDTH + 4);
        return Math.min(1.0F, Math.min(fitHeight, fitWidth));
    }

    private List<Integer> hidden() {
        List<Integer> hidden = new ArrayList<>();
        for (int ability = 0; ability < ACExSugarStaffAbilities.ABILITY_COUNT; ability++) {
            if (!this.layout.contains(ability)) {
                hidden.add(ability);
            }
        }
        return hidden;
    }

    private int trayX(int index, int count) {
        return this.width / 2 - (count * (TRAY_SIZE + TRAY_GAP) - TRAY_GAP) / 2 + index * (TRAY_SIZE + TRAY_GAP);
    }

    private int wheelSlotAt(double mouseX, double mouseY) {
        float scale = wheelScale();
        double localX = (mouseX - centerX()) / scale;
        double localY = (mouseY - centerY()) / scale;
        int count = this.layout.size();
        for (int slot = 0; slot < count; slot++) {
            int x = ACExSugarStaffWheelScreen.boxX(slot, count, 0);
            int y = ACExSugarStaffWheelScreen.boxY(slot, count, 0);
            if (localX >= x && localX < x + BOX_WIDTH && localY >= y && localY < y + BOX_HEIGHT)
                return slot;
        }
        return -1;
    }

    private boolean inWheelArea(double mouseX, double mouseY) {
        float scale = wheelScale();
        double localX = (mouseX - centerX()) / scale;
        double localY = (mouseY - centerY()) / scale;
        return Math.abs(localX) <= OFFSET_X + BOX_WIDTH / 2.0D && Math.abs(localY) <= OFFSET_Y + BOX_HEIGHT / 2.0D;
    }

    private int trayAt(double mouseX, double mouseY) {
        List<Integer> hidden = hidden();
        int top = trayTop();
        for (int i = 0; i < hidden.size(); i++) {
            int x = trayX(i, hidden.size());
            if (mouseX >= x && mouseX < x + TRAY_SIZE && mouseY >= top && mouseY < top + TRAY_SIZE)
                return hidden.get(i);
        }
        return -1;
    }

    private boolean inTrayArea(double mouseY) {
        return mouseY >= trayTop() - TRAY_LABEL_GAP && mouseY < this.height - 30;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 0xFFFFFFFF);
        guiGraphics.drawCenteredString(this.font, Component.translatable("misc.alexscavesexemplified.wheel_editor_hint"), this.width / 2, 19, 0xFFA0A0A0);

        float scale = wheelScale();
        int count = this.layout.size();
        int hoveredSlot = wheelSlotAt(mouseX, mouseY);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(centerX(), centerY(), 0.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        for (int slot = 0; slot < count; slot++) {
            int x = ACExSugarStaffWheelScreen.boxX(slot, count, 0);
            int y = ACExSugarStaffWheelScreen.boxY(slot, count, 0);
            if (this.dragging && slot == this.heldSlot) {
                guiGraphics.renderOutline(x, y, BOX_WIDTH, BOX_HEIGHT, 0x606A5A80);
                continue;
            }
            boolean target = this.dragging && slot == hoveredSlot;
            drawBox(guiGraphics, this.layout.get(slot), x, y, String.valueOf((slot + 1) % 10), target || (!this.dragging && slot == hoveredSlot), target);
        }
        if (count == 0) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("misc.alexscavesexemplified.wheel_editor_empty"), 0, -4, 0xFF8A7AA0);
        }
        guiGraphics.pose().popPose();

        List<Integer> hidden = hidden();
        int top = trayTop();
        guiGraphics.drawCenteredString(this.font, Component.translatable("misc.alexscavesexemplified.wheel_editor_hidden"), this.width / 2, top - TRAY_LABEL_GAP + 1, 0xFFA0A0A0);
        if (hidden.isEmpty()) {
            guiGraphics.renderOutline(this.width / 2 - 60, top, 120, TRAY_SIZE, 0x406A5A80);
        }
        int hoveredTray = -1;
        for (int i = 0; i < hidden.size(); i++) {
            int ability = hidden.get(i);
            int x = trayX(i, hidden.size());
            boolean held = this.dragging && !isFromWheel() && ability == this.heldAbility;
            boolean hover = mouseX >= x && mouseX < x + TRAY_SIZE && mouseY >= top && mouseY < top + TRAY_SIZE;
            guiGraphics.fill(x, top, x + TRAY_SIZE, top + TRAY_SIZE, hover ? 0xD0402A55 : 0xB0101018);
            guiGraphics.renderOutline(x, top, TRAY_SIZE, TRAY_SIZE, hover ? 0xFFFFE58A : 0xFF6A5A80);
            if (!held) {
                guiGraphics.renderItem(this.icons[ability], x + (TRAY_SIZE - 16) / 2, top + (TRAY_SIZE - 16) / 2);
            }
            if (hover) {
                hoveredTray = ability;
            }
        }
        if (this.dragging && isFromWheel() && inTrayArea(mouseY)) {
            guiGraphics.renderOutline(this.width / 2 - 62, top - 2, 124, TRAY_SIZE + 4, 0xFFFFE58A);
        }

        if (this.dragging && this.heldAbility >= 0) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(mouseX, mouseY, 400.0F);
            guiGraphics.pose().scale(scale, scale, 1.0F);
            drawBox(guiGraphics, this.heldAbility, -BOX_WIDTH / 2, -BOX_HEIGHT / 2, "", true, false);
            guiGraphics.pose().popPose();
        } else if (hoveredTray >= 0) {
            guiGraphics.renderComponentTooltip(this.font, List.of(ACExSugarStaffWheelScreen.abilityName(hoveredTray), Component.translatable("misc.alexscavesexemplified.wheel_editor_cost", ACExSugarStaffAbilities.chargeCost(hoveredTray)).withStyle(ChatFormatting.YELLOW)), mouseX, mouseY);
        }
    }

    private void drawBox(GuiGraphics guiGraphics, int ability, int x, int y, String key, boolean highlighted, boolean target) {
        guiGraphics.fill(x, y, x + BOX_WIDTH, y + BOX_HEIGHT, highlighted ? 0xD0402A55 : 0xB0101018);
        guiGraphics.renderOutline(x, y, BOX_WIDTH, BOX_HEIGHT, target ? 0xFFFFE58A : highlighted ? 0xFFB8A0D8 : 0xFF6A5A80);
        guiGraphics.renderItem(this.icons[ability], x + BOX_WIDTH / 2 - 8, y + 3);
        guiGraphics.drawCenteredString(this.font, ACExSugarStaffWheelScreen.abilityName(ability), x + BOX_WIDTH / 2, y + BOX_HEIGHT - 12, highlighted ? 0xFFFFE58A : 0xFFFFFFFF);
        if (!key.isEmpty()) {
            guiGraphics.drawString(this.font, key, x + 3, y + 3, 0xFF8A7AA0, false);
        }
        int cost = ACExSugarStaffAbilities.chargeCost(ability);
        String costText = String.valueOf(cost);
        int costX = x + BOX_WIDTH - 3 - this.font.width(costText);
        guiGraphics.drawString(this.font, costText, costX, y + 3, cost <= 0 ? 0xFF8A7AA0 : 0xFFFFF3A6, false);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(costX - 10, y + 2, 0.0F);
        guiGraphics.pose().scale(0.5F, 0.5F, 1.0F);
        guiGraphics.renderItem(this.chargeIcon, 0, 0);
        guiGraphics.pose().popPose();
    }

    private boolean isFromWheel() {
        return this.heldSlot >= 0;
    }

    private void release() {
        this.heldAbility = -1;
        this.heldSlot = -1;
        this.dragging = false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button))
            return true;
        int slot = wheelSlotAt(mouseX, mouseY);
        int tray = trayAt(mouseX, mouseY);
        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if (slot >= 0) {
                this.layout.remove(slot);
                return true;
            }
            if (tray >= 0) {
                this.layout.add(tray);
                return true;
            }
            return false;
        }
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT)
            return false;
        if (slot >= 0) {
            this.heldAbility = this.layout.get(slot);
            this.heldSlot = slot;
        } else if (tray >= 0) {
            this.heldAbility = tray;
            this.heldSlot = -1;
        } else {
            return false;
        }
        this.dragging = false;
        this.pressX = mouseX;
        this.pressY = mouseY;
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.heldAbility >= 0 && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (!this.dragging && Math.hypot(mouseX - this.pressX, mouseY - this.pressY) > DRAG_THRESHOLD) {
                this.dragging = true;
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.heldAbility < 0 || button != GLFW.GLFW_MOUSE_BUTTON_LEFT)
            return super.mouseReleased(mouseX, mouseY, button);
        int ability = this.heldAbility;
        if (!this.dragging) {
            if (!isFromWheel()) {
                this.layout.add(ability);
            }
            release();
            return true;
        }
        int target = wheelSlotAt(mouseX, mouseY);
        if (isFromWheel()) {
            if (target >= 0) {
                this.layout.remove(this.heldSlot);
                this.layout.add(target, ability);
            } else if (inTrayArea(mouseY)) {
                this.layout.remove(this.heldSlot);
            }
        } else if (target >= 0) {
            this.layout.add(target, ability);
        } else if (inWheelArea(mouseX, mouseY)) {
            this.layout.add(ability);
        }
        release();
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
