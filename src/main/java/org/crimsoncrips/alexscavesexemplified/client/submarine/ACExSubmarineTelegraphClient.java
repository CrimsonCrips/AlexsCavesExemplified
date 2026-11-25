package org.crimsoncrips.alexscavesexemplified.client.submarine;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.crimsoncrips.alexscavesexemplified.backport.ClientTickEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineTelegraph;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineTelegraph.Order;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;

public class ACExSubmarineTelegraphClient {

    public static final KeyMapping KEY = new KeyMapping("key.alexscavesexemplified.submarine_telegraph", GLFW.GLFW_KEY_B, "key.categories.alexscavesexemplified");
    private static final int PANEL = 0xE0202020;
    private static final int OUTLINE = 0xFF8B8B8B;
    private static final int SELECTED_ROW = 0xFF404040;
    private static final int IDLE_TEXT = 0xFF8B8B8B;
    private static final int SAFE_TEXT = 0xFF4BE1FF;
    private static final int FLANK_TEXT = 0xFFFF3B30;
    private static final int TITLE_TEXT = 0xFFFFFFFF;
    private static final int PADDING = 4;
    private static final int ROW_GAP = 3;
    private static final float SCALE = 1.2F;
    private static final float SLIDE_STEP = 1.0F / ACExUtils.seconds(0.25F);

    private static boolean forwardHeld;
    private static boolean backHeld;
    private static boolean visible;
    private static float slide;
    private static float prevSlide;

    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KEY);
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        prevSlide = slide;
        SubmarineEntity submarine = pilotedSubmarine(minecraft);
        if (submarine == null || !AlexsCavesExemplified.CLIENT_CONFIG.SUBMARINE_TELEGRAPH_ENABLED.get()) {
            if (ACExSubmarineTelegraph.submarineId != -1) {
                ACExSubmarineTelegraph.disengage();
            }
            while (KEY.consumeClick()) {
            }
            forwardHeld = false;
            backHeld = false;
            visible = false;
            updateSlide();
            return;
        }
        if (ACExSubmarineTelegraph.submarineId != -1 && ACExSubmarineTelegraph.submarineId != submarine.getId()) {
            ACExSubmarineTelegraph.disengage();
            visible = false;
        }
        while (KEY.consumeClick()) {
            if (!ACExSubmarineTelegraph.engaged(submarine)) {
                ACExSubmarineTelegraph.engage(submarine.getId(), Order.closestTo(submarine.getAcceleration()));
                visible = true;
                ring(minecraft, pitch(ACExSubmarineTelegraph.order));
            } else {
                visible = !visible;
                ring(minecraft, visible ? pitch(ACExSubmarineTelegraph.order) : 0.5F);
            }
        }
        boolean forward = minecraft.options.keyUp.isDown();
        boolean back = minecraft.options.keyDown.isDown();
        if (ACExSubmarineTelegraph.engaged(submarine)) {
            boolean forwardPressed = forward && !forwardHeld;
            boolean backPressed = back && !backHeld;
            if (!visible) {
                if (forwardPressed || backPressed) {
                    ACExSubmarineTelegraph.disengage();
                    ring(minecraft, 0.5F);
                }
            } else {
                if (forwardPressed) {
                    changeOrder(minecraft, 1);
                }
                if (backPressed) {
                    changeOrder(minecraft, -1);
                }
            }
        } else {
            visible = false;
        }
        forwardHeld = forward;
        backHeld = back;
        updateSlide();
    }

    private static void updateSlide() {
        slide = Mth.approach(slide, visible ? 1.0F : 0.0F, SLIDE_STEP);
    }

    public static void onMovementInput(MovementInputUpdateEvent event) {
        if (ACExSubmarineTelegraph.submarineId != -1 && event.getEntity().getVehicle() instanceof SubmarineEntity submarine && ACExSubmarineTelegraph.engaged(submarine)) {
            event.getInput().forwardImpulse = 0.0F;
            event.getInput().up = false;
            event.getInput().down = false;
        }
    }

    @Nullable
    private static SubmarineEntity pilotedSubmarine(Minecraft minecraft) {
        if (minecraft.level == null || minecraft.player == null)
            return null;
        if (minecraft.player.getVehicle() instanceof SubmarineEntity submarine && submarine.getControllingPassenger() == minecraft.player)
            return submarine;
        if (ACExSubmarineRemoteClient.isDriving() && minecraft.level.getEntity(ACExSubmarineRemote.LOCAL.submarineId) instanceof SubmarineEntity submarine)
            return submarine;
        return null;
    }

    private static void changeOrder(Minecraft minecraft, int direction) {
        Order next = ACExSubmarineTelegraph.order.step(direction);
        if (next == ACExSubmarineTelegraph.order)
            return;
        ACExSubmarineTelegraph.order = next;
        ring(minecraft, pitch(next));
    }

    private static float pitch(Order order) {
        return 0.7F + order.ordinal() * 0.12F;
    }

    private static void ring(Minecraft minecraft, float pitch) {
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_BELL.get(), pitch, 0.4F));
    }

    public static void renderOverlay(GuiGraphics graphics, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        float progress = Mth.lerp(partialTick, prevSlide, slide);
        if (progress <= 0.0F || minecraft.options.hideGui)
            return;
        float eased = 1.0F - (1.0F - progress) * (1.0F - progress) * (1.0F - progress);
        Font font = minecraft.font;
        Order[] orders = Order.values();
        Component title = Component.translatable("misc.alexscavesexemplified.engine_telegraph");
        int textWidth = font.width(title);
        for (Order order : orders) {
            textWidth = Math.max(textWidth, font.width(label(order)));
        }
        int rowHeight = font.lineHeight + ROW_GAP;
        int width = textWidth + PADDING * 2;
        int height = PADDING * 2 + rowHeight * (orders.length + 1);
        int left = -width / 2;
        int top = -height / 2;
        int centerX = 0;
        graphics.pose().pushPose();
        float hiddenX = graphics.guiWidth() + width * SCALE / 2.0F;
        float hiddenY = graphics.guiHeight() + height * SCALE / 2.0F;
        graphics.pose().translate(Mth.lerp(eased, hiddenX, graphics.guiWidth() / 2.0F), Mth.lerp(eased, hiddenY, graphics.guiHeight() / 2.0F), 0.0F);
        graphics.pose().scale(SCALE, SCALE, 1.0F);
        graphics.fill(left, top, left + width, top + height, PANEL);
        graphics.renderOutline(left, top, width, height, OUTLINE);
        int y = top + PADDING;
        graphics.drawString(font, title, centerX - font.width(title) / 2, y + ROW_GAP / 2, TITLE_TEXT, false);
        y += rowHeight;
        for (int i = orders.length - 1; i >= 0; i--) {
            Order order = orders[i];
            Component text = label(order);
            boolean selected = order == ACExSubmarineTelegraph.order;
            if (selected) {
                graphics.fill(left + 1, y, left + width - 1, y + rowHeight, SELECTED_ROW);
            }
            int color = !selected ? IDLE_TEXT : order.speeding() ? FLANK_TEXT : SAFE_TEXT;
            graphics.drawString(font, text, centerX - font.width(text) / 2, y + ROW_GAP / 2 + 1, color, false);
            y += rowHeight;
        }
        graphics.pose().popPose();
    }

    private static Component label(Order order) {
        return Component.translatable("misc.alexscavesexemplified.engine_order_" + order.key);
    }
}
