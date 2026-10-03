package org.crimsoncrips.alexscavesexemplified.client.screen;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACKeybindRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import org.crimsoncrips.alexscavesexemplified.client.render.ACExShaders;
import org.joml.Vector3f;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.crimsoncrips.alexscavesexemplified.networking.ACExGammaAbilityPacket;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExGammaAttacks;
import org.lwjgl.glfw.GLFW;

import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.BOX_HEIGHT;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.BOX_WIDTH;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.OFFSET_Y;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.AURA_PATTERN_UNIT;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.ELECTRIC_DISSIPATION;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.ELECTRIC_MARGIN;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.ELECTRIC_OPACITY;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.PULSE_EXTRA_REACH;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.PULSE_OPACITY;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.PULSE_RING_WIDTH;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.RADIANT_AURA_OPACITY;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.RADIANT_AURA_REACH;
import static org.crimsoncrips.alexscavesexemplified.client.screen.ACExSugarStaffWheelScreen.SWEET_RELEASE_OPACITY;

@OnlyIn(Dist.CLIENT)
public class ACExGammaWheelScreen extends Screen {

    private static final String[] NAMES = {"gamma_waste_spit", "gamma_spike_laser", "gamma_toxic_barrel", "gamma_nuke_beam", "gamma_spike_storm", "gamma_controlled_laser"};
    private static final Vector3f AQUA = new Vector3f(0.45F, 0.95F, 1.0F);

    private final long openedAt = Util.getMillis();
    private final TremorzillaEntity tremorzilla;
    private final ItemStack[] icons;
    private final ItemStack centerIcon = new ItemStack(ACBlockRegistry.TREMORZILLA_EGG.get());
    private final boolean holdMode;
    private int hovered = -1;

    public static void open(TremorzillaEntity tremorzilla) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen == null && minecraft.player != null) {
            minecraft.setScreen(new ACExGammaWheelScreen(tremorzilla));
        }
    }

    private ACExGammaWheelScreen(TremorzillaEntity tremorzilla) {
        super(Component.translatable("misc.alexscavesexemplified.gamma_wheel"));
        this.tremorzilla = tremorzilla;
        this.icons = new ItemStack[]{
                new ItemStack(ACItemRegistry.URANIUM.get()),
                new ItemStack(ACBlockRegistry.ACIDIC_RADROCK.get()),
                new ItemStack(ACBlockRegistry.WASTE_DRUM.get()),
                new ItemStack(ACBlockRegistry.NUCLEAR_BOMB.get()),
                new ItemStack(Items.POINTED_DRIPSTONE),
                new ItemStack(ACItemRegistry.RAYGUN.get())
        };
        this.holdMode = specialKeyHeld();
    }

    private static boolean specialKeyHeld() {
        Minecraft minecraft = Minecraft.getInstance();
        InputConstants.Key key = ACKeybindRegistry.KEY_SPECIAL_ABILITY.getKey();
        long window = minecraft.getWindow().getWindow();
        if (key.getType() == InputConstants.Type.MOUSE) {
            return GLFW.glfwGetMouseButton(window, key.getValue()) == GLFW.GLFW_PRESS;
        }
        return key.getType() == InputConstants.Type.KEYSYM && InputConstants.isKeyDown(window, key.getValue());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        if (this.minecraft != null && (this.minecraft.player == null || this.minecraft.player.getVehicle() != this.tremorzilla || !ACExGammaAttacks.canRiderUse(this.tremorzilla, this.minecraft.player))) {
            this.onClose();
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0x50000000, 0x70000000);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int[] slots = this.slots();
        int count = slots.length;
        this.hovered = ACExSugarStaffWheelScreen.segmentAt(mouseX - centerX, mouseY - centerY, count);
        boolean hoveredCooling = false;
        boolean hoveredUncharged = false;
        boolean charged = ACExGammaAttacks.beamCharged(this.tremorzilla);
        float meter = Math.min(1.0F, this.tremorzilla.getMeterAmount());
        float time = (Util.getMillis() - this.openedAt) / 1000.0F;

        for (int slot = 0; slot < count; slot++) {
            int i = slots[slot];
            if (ACExGammaAttacks.cooldownRemaining(this.tremorzilla, i) > 0 || (i == ACExGammaAttacks.CONTROLLED_LASER && !charged))
                continue;
            int boxX = ACExSugarStaffWheelScreen.boxX(slot, count, centerX);
            int boxY = ACExSugarStaffWheelScreen.boxY(slot, count, centerY);
            float scale = ACExSugarStaffWheelScreen.guiScale();
            if (i == ACExGammaAttacks.CONTROLLED_LASER) {
                ShaderInstance pulse = ACExShaders.sweetReleasePulse();
                if (pulse != null) {
                    float reach = ELECTRIC_MARGIN + PULSE_EXTRA_REACH;
                    pulse.safeGetUniform("Reach").set(reach * scale);
                    pulse.safeGetUniform("RingWidth").set(PULSE_RING_WIDTH * scale);
                    ACExSugarStaffWheelScreen.drawAuraLayer(guiGraphics, pulse, boxX, boxY, reach, time, PULSE_OPACITY * SWEET_RELEASE_OPACITY, AQUA);
                }
                ShaderInstance electric = ACExShaders.sweetReleaseElectric();
                if (electric != null) {
                    electric.safeGetUniform("Margin").set(ELECTRIC_MARGIN * scale);
                    electric.safeGetUniform("Dissipation").set(ELECTRIC_DISSIPATION * scale);
                    electric.safeGetUniform("PatternUnit").set(AURA_PATTERN_UNIT * scale);
                    ACExSugarStaffWheelScreen.drawAuraLayer(guiGraphics, electric, boxX, boxY, ELECTRIC_MARGIN, time, ELECTRIC_OPACITY * SWEET_RELEASE_OPACITY, AQUA);
                }
            } else {
                ShaderInstance aura = ACExShaders.radiantAura();
                if (aura != null) {
                    aura.safeGetUniform("Reach").set(RADIANT_AURA_REACH * scale);
                    ACExSugarStaffWheelScreen.drawAuraLayer(guiGraphics, aura, boxX, boxY, RADIANT_AURA_REACH, time, RADIANT_AURA_OPACITY, AQUA);
                }
            }
        }

        for (int slot = 0; slot < count; slot++) {
            int i = slots[slot];
            int boxX = ACExSugarStaffWheelScreen.boxX(slot, count, centerX);
            int boxY = ACExSugarStaffWheelScreen.boxY(slot, count, centerY);
            boolean selected = slot == this.hovered;
            int remaining = ACExGammaAttacks.cooldownRemaining(this.tremorzilla, i);
            boolean uncharged = i == ACExGammaAttacks.CONTROLLED_LASER && !charged;
            boolean unavailable = remaining > 0 || uncharged;
            if (selected && remaining > 0) {
                hoveredCooling = true;
            }
            if (selected && uncharged) {
                hoveredUncharged = true;
            }
            guiGraphics.fill(boxX, boxY, boxX + BOX_WIDTH, boxY + BOX_HEIGHT, selected ? 0xD0244A2A : 0xB0101810);
            guiGraphics.renderOutline(boxX, boxY, BOX_WIDTH, BOX_HEIGHT, selected ? 0xFF9CFF6A : 0xFF4E7A4A);
            if (i == ACExGammaAttacks.CONTROLLED_LASER && !unavailable) {
                ACExSugarStaffWheelScreen.drawSweetReleaseGlow(guiGraphics, boxX, boxY, time, AQUA);
            }
            guiGraphics.renderItem(this.icons[i], boxX + BOX_WIDTH / 2 - 8, boxY + 3);
            if (remaining > 0) {
                int cover = Math.round(BOX_HEIGHT * ACExGammaAttacks.cooldownProgress(this.tremorzilla, i));
                guiGraphics.fill(boxX + 1, boxY + BOX_HEIGHT - 1 - cover, boxX + BOX_WIDTH - 1, boxY + BOX_HEIGHT - 1, 0x90000000);
            } else if (uncharged) {
                int cover = Math.round(BOX_HEIGHT * (1.0F - meter));
                guiGraphics.fill(boxX + 1, boxY + 1, boxX + BOX_WIDTH - 1, boxY + 1 + cover, 0x90000000);
            }
            if (i == ACExGammaAttacks.CONTROLLED_LASER) {
                String meterText = Math.round(meter * 100.0F) + "%";
                guiGraphics.drawString(this.font, meterText, boxX + BOX_WIDTH - 3 - this.font.width(meterText), boxY + 3, charged ? 0xFFB6F6FF : 0xFFFF7777, false);
            }
            guiGraphics.drawCenteredString(this.font, abilityName(i), boxX + BOX_WIDTH / 2, boxY + BOX_HEIGHT - 12, unavailable ? 0xFF777777 : selected ? 0xFF9CFF6A : 0xFFFFFFFF);
            guiGraphics.drawString(this.font, String.valueOf(slot + 1), boxX + 3, boxY + 3, 0xFF6A9A66, false);
            if (remaining > 0) {
                String seconds = String.format("%.1fs", remaining / 20.0F);
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0.0F, 0.0F, 300.0F);
                guiGraphics.drawCenteredString(this.font, seconds, boxX + BOX_WIDTH / 2, boxY + 7, 0xFFFF7777);
                guiGraphics.pose().popPose();
            }
        }

        guiGraphics.renderItem(this.centerIcon, centerX - 8, centerY - 8);
        int noteY = centerY + OFFSET_Y + BOX_HEIGHT / 2 + 6;
        if (hoveredUncharged) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("misc.alexscavesexemplified.gamma_not_charged"), centerX, noteY, 0xFFFF7777);
            noteY += 11;
        }
        if (hoveredCooling) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("misc.alexscavesexemplified.sugar_staff_cooldown"), centerX, noteY, 0xFFFF7777);
        }
    }

    private static Component abilityName(int ability) {
        return Component.translatable("misc.alexscavesexemplified." + NAMES[ability]);
    }

    private int[] slots() {
        int count = ACExGammaAttacks.ABILITY_COUNT;
        int[] slots = new int[count];
        for (int i = 0; i < count; i++) {
            slots[i] = i;
        }
        return slots;
    }

    private void select(int slot) {
        int[] slots = this.slots();
        if (slot >= 0 && slot < slots.length) {
            PacketDistributor.sendToServer(new ACExGammaAbilityPacket(slots[slot]));
        }
        this.onClose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            select(ACExSugarStaffWheelScreen.segmentAt(mouseX - this.width / 2.0D, mouseY - this.height / 2.0D, this.slots().length));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.holdMode && ACKeybindRegistry.KEY_SPECIAL_ABILITY.matchesMouse(button)) {
            select(ACExSugarStaffWheelScreen.segmentAt(mouseX - this.width / 2.0D, mouseY - this.height / 2.0D, this.slots().length));
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (this.holdMode && ACKeybindRegistry.KEY_SPECIAL_ABILITY.matches(keyCode, scanCode)) {
            select(this.hovered);
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode >= GLFW.GLFW_KEY_1 && keyCode - GLFW.GLFW_KEY_1 < this.slots().length) {
            select(keyCode - GLFW.GLFW_KEY_1);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
