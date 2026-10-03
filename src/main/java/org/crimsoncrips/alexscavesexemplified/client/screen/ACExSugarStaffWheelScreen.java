package org.crimsoncrips.alexscavesexemplified.client.screen;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.ShaderInstance;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.render.ACExShaders;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.networking.ACExSugarStaffCastPacket;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ACExSugarStaffWheelScreen extends Screen {

    private static final int DEADZONE = 18;
    static final int OFFSET_X = 150;
    static final int OFFSET_Y = 85;
    static final int BOX_WIDTH = 84;
    static final int BOX_HEIGHT = 34;
    private static final String[] NAMES = {"sugar_staff_base_peppermints", "sugar_staff_base_hex", "sugar_staff_peppernami", "sugar_staff_radiant_shield", "sugar_staff_frost_barrage", "sugar_staff_pepper_crush", "sugar_staff_hailstorm", "sugar_staff_line_of_gum", "sugar_staff_gummy_swarm", "sugar_staff_sweet_release"};

    static final float GLOW_VIRTUAL_HEIGHT = 420.0F;
    static final float GLOW_STRENGTH = 2.5F;
    static final float GLOW_OPACITY = 0.6F;
    static final float AURA_PATTERN_UNIT = 11.0F;
    static final float ELECTRIC_OPACITY = 1.0F;
    static final float ELECTRIC_MARGIN = 8.0F;
    static final float ELECTRIC_DISSIPATION = 1.7F;
    static final float PULSE_OPACITY = 0.9F;
    static final float PULSE_EXTRA_REACH = 10.0F;
    static final float PULSE_RING_WIDTH = 2.0F;
    static final float SWEET_RELEASE_OPACITY = 0.8F;
    static final float RADIANT_AURA_REACH = 8.0F;
    static final float RADIANT_AURA_OPACITY = 0.7F;

    private final long openedAt = Util.getMillis();
    private final ItemStack[] icons;
    private final ItemStack chargeIcon = new ItemStack(ACItemRegistry.RADIANT_ESSENCE.get());
    private int[] slots;
    private final boolean holdMode;
    private int hovered = -1;

    public static void open() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen == null && minecraft.player != null) {
            minecraft.setScreen(new ACExSugarStaffWheelScreen());
        }
    }

    private ACExSugarStaffWheelScreen() {
        super(Component.translatable("misc.alexscavesexemplified.sugar_staff_wheel"));
        this.icons = createIcons();
        this.holdMode = useKeyHeld();
        this.slots = layout();
    }

    static ItemStack[] createIcons() {
        return new ItemStack[]{
                new ItemStack(ACBlockRegistry.SMALL_PEPPERMINT.get()),
                new ItemStack(ACItemRegistry.SUGAR_STAFF.get()),
                new ItemStack(ACBlockRegistry.SMALL_PEPPERMINT.get()),
                new ItemStack(ACItemRegistry.RADIANT_ESSENCE.get()),
                new ItemStack(ACBlockRegistry.FROSTMINT.get()),
                new ItemStack(ACBlockRegistry.LARGE_PEPPERMINT.get()),
                new ItemStack(ACItemRegistry.FROSTMINT_SPEAR.get()),
                new ItemStack(ACItemRegistry.SHOT_GUM.get()),
                new ItemStack(ACExItemRegistry.GUMWIRM_SPAWN_EGG.get()),
                new ItemStack(ACBlockRegistry.GOBTHUMPER.get())
        };
    }

    static Component abilityName(int ability) {
        return Component.translatable("misc.alexscavesexemplified." + NAMES[ability]);
    }

    static int[] layout() {
        List<Integer> abilities = new ArrayList<>();
        for (String key : AlexsCavesExemplified.CLIENT_CONFIG.WHEEL_LAYOUT.get()) {
            int ability = ACExSugarStaffAbilities.abilityFromKey(key);
            if (ability >= 0 && !abilities.contains(ability)) {
                abilities.add(ability);
            }
        }
        return abilities.stream().mapToInt(Integer::intValue).toArray();
    }

    static void saveLayout(List<Integer> abilities) {
        AlexsCavesExemplified.CLIENT_CONFIG.WHEEL_LAYOUT.set(abilities.stream().map(ACExSugarStaffAbilities::abilityKey).toList());
        AlexsCavesExemplified.CLIENT_CONFIG_SPEC.save();
    }

    private static boolean useKeyHeld() {
        Minecraft minecraft = Minecraft.getInstance();
        InputConstants.Key key = minecraft.options.keyUse.getKey();
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
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0x50000000, 0x70000000);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        this.slots = layout();
        int count = this.slots.length;
        this.hovered = segmentAt(mouseX - centerX, mouseY - centerY, count);
        ItemStack staff = this.minecraft.player == null ? ItemStack.EMPTY : this.minecraft.player.getMainHandItem().is(ACItemRegistry.SUGAR_STAFF.get()) ? this.minecraft.player.getMainHandItem() : this.minecraft.player.getOffhandItem();
        Component hoveredRequirement = null;
        int charges = this.minecraft.player == null ? 0 : ACExUtils.getRadiantCharges(this.minecraft.player);
        boolean hoveredCooling = false;
        float time = (Util.getMillis() - this.openedAt) / 1000.0F;
        Component[] requirements = new Component[ACExSugarStaffAbilities.ABILITY_COUNT];
        for (int i = 0; i < ACExSugarStaffAbilities.ABILITY_COUNT; i++) {
            requirements[i] = this.minecraft.player == null ? null : ACExSugarStaffAbilities.requirement(i, this.minecraft.player, staff);
        }

        for (int slot = 0; slot < count; slot++) {
            int i = this.slots[slot];
            if (requirements[i] != null || ACExSugarStaffAbilities.chargeCost(i) <= 0 || (this.minecraft.player != null && ACExSugarStaffAbilities.cooldownRemaining(this.minecraft.player, i) > 0))
                continue;
            int boxX = boxX(slot, count, centerX);
            int boxY = boxY(slot, count, centerY);
            if (i == ACExSugarStaffAbilities.SWEET_RELEASE) {
                ShaderInstance pulse = ACExShaders.sweetReleasePulse();
                if (pulse != null) {
                    float reach = ELECTRIC_MARGIN + PULSE_EXTRA_REACH;
                    pulse.safeGetUniform("Reach").set(reach * guiScale());
                    pulse.safeGetUniform("RingWidth").set(PULSE_RING_WIDTH * guiScale());
                    drawAuraLayer(guiGraphics, pulse, boxX, boxY, reach, time, PULSE_OPACITY * SWEET_RELEASE_OPACITY, null);
                }
                ShaderInstance electric = ACExShaders.sweetReleaseElectric();
                if (electric != null) {
                    electric.safeGetUniform("Margin").set(ELECTRIC_MARGIN * guiScale());
                    electric.safeGetUniform("Dissipation").set(ELECTRIC_DISSIPATION * guiScale());
                    electric.safeGetUniform("PatternUnit").set(AURA_PATTERN_UNIT * guiScale());
                    drawAuraLayer(guiGraphics, electric, boxX, boxY, ELECTRIC_MARGIN, time, ELECTRIC_OPACITY * SWEET_RELEASE_OPACITY, null);
                }
            } else {
                ShaderInstance aura = ACExShaders.radiantAura();
                if (aura != null) {
                    aura.safeGetUniform("Reach").set(RADIANT_AURA_REACH * guiScale());
                    drawAuraLayer(guiGraphics, aura, boxX, boxY, RADIANT_AURA_REACH, time, RADIANT_AURA_OPACITY, null);
                }
            }
        }

        for (int slot = 0; slot < count; slot++) {
            int i = this.slots[slot];
            int boxX = boxX(slot, count, centerX);
            int boxY = boxY(slot, count, centerY);
            boolean selected = slot == this.hovered;
            Component requirement = requirements[i];
            int remaining = this.minecraft.player == null ? 0 : ACExSugarStaffAbilities.cooldownRemaining(this.minecraft.player, i);
            if (selected && requirement != null) {
                hoveredRequirement = requirement;
            }
            if (selected && remaining > 0) {
                hoveredCooling = true;
            }
            guiGraphics.fill(boxX, boxY, boxX + BOX_WIDTH, boxY + BOX_HEIGHT, requirement != null ? 0xB0080808 : selected ? 0xD0402A55 : 0xB0101018);
            guiGraphics.renderOutline(boxX, boxY, BOX_WIDTH, BOX_HEIGHT, selected ? (requirement != null ? 0xFFAA4444 : 0xFFFFE58A) : 0xFF6A5A80);
            if (i == ACExSugarStaffAbilities.SWEET_RELEASE && requirement == null && remaining <= 0) {
                drawSweetReleaseGlow(guiGraphics, boxX, boxY, time, null);
            }
            guiGraphics.renderItem(this.icons[i], boxX + BOX_WIDTH / 2 - 8, boxY + 3);
            Component name = abilityName(i);
            if (remaining > 0) {
                int cover = Math.round(BOX_HEIGHT * ACExSugarStaffAbilities.cooldownProgress(this.minecraft.player, i));
                guiGraphics.fill(boxX + 1, boxY + BOX_HEIGHT - 1 - cover, boxX + BOX_WIDTH - 1, boxY + BOX_HEIGHT - 1, 0x90000000);
            }
            guiGraphics.drawCenteredString(this.font, name, boxX + BOX_WIDTH / 2, boxY + BOX_HEIGHT - 12, requirement != null || remaining > 0 ? 0xFF777777 : selected ? 0xFFFFE58A : 0xFFFFFFFF);
            guiGraphics.drawString(this.font, String.valueOf((slot + 1) % 10), boxX + 3, boxY + 3, 0xFF8A7AA0, false);
            int cost = ACExSugarStaffAbilities.chargeCost(i);
            String costText = String.valueOf(cost);
            int costX = boxX + BOX_WIDTH - 3 - this.font.width(costText);
            guiGraphics.drawString(this.font, costText, costX, boxY + 3, cost <= 0 ? 0xFF8A7AA0 : cost > charges ? 0xFFFF5555 : 0xFFFFF3A6, false);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(costX - 10, boxY + 2, 0.0F);
            guiGraphics.pose().scale(0.5F, 0.5F, 1.0F);
            guiGraphics.renderItem(this.chargeIcon, 0, 0);
            guiGraphics.pose().popPose();
            if (remaining > 0) {
                String seconds = String.format("%.1fs", remaining / 20.0F);
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0.0F, 0.0F, 300.0F);
                guiGraphics.drawCenteredString(this.font, seconds, boxX + BOX_WIDTH / 2, boxY + 7, 0xFFFF7777);
                guiGraphics.pose().popPose();
            }
        }

        guiGraphics.renderItem(new ItemStack(ACItemRegistry.RADIANT_ESSENCE.get()), centerX - 8, centerY - 12);
        guiGraphics.drawCenteredString(this.font, String.valueOf(charges), centerX, centerY + 6, charges > 0 ? 0xFFFFF3A6 : 0xFFFF5555);
        int noteY = centerY + OFFSET_Y + BOX_HEIGHT / 2 + 6;
        if (hoveredRequirement != null) {
            guiGraphics.drawCenteredString(this.font, hoveredRequirement, centerX, noteY, 0xFFFF7777);
            noteY += 11;
        }
        if (hoveredCooling) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("misc.alexscavesexemplified.sugar_staff_cooldown"), centerX, noteY, 0xFFFF7777);
        }
    }

    static float guiScale() {
        return (float) Minecraft.getInstance().getWindow().getGuiScale();
    }

    static int boxX(int slot, int count, int centerX) {
        return centerX + (int) Math.round(Math.cos(Math.toRadians(segmentAngle(slot, count))) * OFFSET_X) - BOX_WIDTH / 2;
    }

    static int boxY(int slot, int count, int centerY) {
        return centerY + (int) Math.round(Math.sin(Math.toRadians(segmentAngle(slot, count))) * OFFSET_Y) - BOX_HEIGHT / 2;
    }

    static void drawAuraLayer(GuiGraphics guiGraphics, ShaderInstance shader, int boxX, int boxY, float margin, float time, float opacity, @Nullable Vector3f tint) {
        guiGraphics.flush();
        float left = boxX - margin;
        float top = boxY - margin;
        float right = boxX + BOX_WIDTH + margin;
        float bottom = boxY + BOX_HEIGHT + margin;
        float scale = guiScale();
        Matrix4f matrix = guiGraphics.pose().last().pose();
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        builder.addVertex(matrix, left, bottom, 0.0F).setUv(0.0F, 0.0F);
        builder.addVertex(matrix, right, bottom, 0.0F).setUv(1.0F, 0.0F);
        builder.addVertex(matrix, right, top, 0.0F).setUv(1.0F, 1.0F);
        builder.addVertex(matrix, left, top, 0.0F).setUv(0.0F, 1.0F);
        shader.safeGetUniform("iTime").set(time);
        shader.safeGetUniform("RegionSize").set((right - left) * scale, (bottom - top) * scale);
        shader.safeGetUniform("BoxSize").set(BOX_WIDTH * scale, BOX_HEIGHT * scale);
        shader.safeGetUniform("Opacity").set(opacity);
        applyTint(shader, tint);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(() -> shader);
        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
    }

    static void drawSweetReleaseGlow(GuiGraphics guiGraphics, int boxX, int boxY, float time, @Nullable Vector3f tint) {
        ShaderInstance shader = ACExShaders.sweetReleaseGlow();
        if (shader == null)
            return;
        guiGraphics.flush();
        float left = boxX;
        float right = boxX + BOX_WIDTH;
        float top = boxY;
        float bottom = boxY + BOX_HEIGHT;
        Matrix4f matrix = guiGraphics.pose().last().pose();
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        builder.addVertex(matrix, left, bottom, 0.0F).setUv(0.0F, 0.0F);
        builder.addVertex(matrix, right, bottom, 0.0F).setUv(1.0F, 0.0F);
        builder.addVertex(matrix, right, top, 0.0F).setUv(1.0F, 1.0F);
        builder.addVertex(matrix, left, top, 0.0F).setUv(0.0F, 1.0F);
        shader.safeGetUniform("iTime").set(time);
        shader.safeGetUniform("VirtualSize").set(GLOW_VIRTUAL_HEIGHT * (right - left) / (bottom - top), GLOW_VIRTUAL_HEIGHT);
        shader.safeGetUniform("Strength").set(GLOW_STRENGTH);
        shader.safeGetUniform("Opacity").set(GLOW_OPACITY * SWEET_RELEASE_OPACITY);
        applyTint(shader, tint);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        RenderSystem.setShader(() -> shader);
        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
    }

    private static void applyTint(ShaderInstance shader, @Nullable Vector3f tint) {
        shader.safeGetUniform("TintStrength").set(tint == null ? 0.0F : 1.0F);
        if (tint != null) {
            shader.safeGetUniform("Tint").set(tint.x(), tint.y(), tint.z());
        }
    }

    private static double segmentAngle(int slot, int count) {
        return -90.0D + slot * 360.0D / Math.max(1, count);
    }

    static int segmentAt(double dx, double dy, int count) {
        if (count <= 0 || dx * dx + dy * dy < DEADZONE * DEADZONE)
            return -1;
        double angle = Math.toDegrees(Mth.atan2(dy * OFFSET_X / (double) OFFSET_Y, dx)) + 90.0D;
        return (int) Math.floorMod(Math.round(angle / (360.0D / count)), (long) count);
    }

    private void select(int slot) {
        if (slot >= 0 && slot < this.slots.length) {
            PacketDistributor.sendToServer(new ACExSugarStaffCastPacket(this.slots[slot]));
        }
        this.onClose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            select(segmentAt(mouseX - this.width / 2.0D, mouseY - this.height / 2.0D, this.slots.length));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.holdMode && this.minecraft.options.keyUse.matchesMouse(button)) {
            select(segmentAt(mouseX - this.width / 2.0D, mouseY - this.height / 2.0D, this.slots.length));
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (this.holdMode && this.minecraft.options.keyUse.matches(keyCode, scanCode)) {
            select(this.hovered);
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode >= GLFW.GLFW_KEY_1 && keyCode <= GLFW.GLFW_KEY_9 && keyCode - GLFW.GLFW_KEY_1 < this.slots.length) {
            select(keyCode - GLFW.GLFW_KEY_1);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_0 && this.slots.length >= 10) {
            select(9);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
