package org.crimsoncrips.alexscavesexemplified.client.submarine;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.Camera;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.crimsoncrips.alexscavesexemplified.backport.ClientTickEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ViewportEvent;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineHunt;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Map;
import java.util.WeakHashMap;

@OnlyIn(Dist.CLIENT)
public class ACExSubmarineHud {

    private static final int BAR_WIDTH = 4;
    private static final int BAR_HEIGHT = 11;
    private static final int DOT_GAP = 2;
    private static final int DOT_SIZE = 3;
    private static final int BACKGROUND = 0xAA000000;
    private static final int EMPTY_COLOR = 0xFF4BE1FF;
    private static final int FULL_COLOR = 0xFFFF3B30;
    private static final float WORLD_SCALE = 0.04F;
    private static final double HEAD_OFFSET = 0.7D;
    private static final int EDGE_MARGIN = 16;
    private static final float SEE_THROUGH_ALPHA = 0.3F;
    private static final int FADE_TICKS = ACExUtils.seconds(0.3F);
    private static final int FACE_SIZE = 12;
    private static final int TRACKER_MARGIN = 20;
    private static final int TRACKER_TEXT_COLOR = 0xFFFFFFFF;

    private static final Map<Entity, Fade> FADES = new WeakHashMap<>();
    private static double lastFov = 70.0D;

    private static class Fade {
        private float opacity;
        private float prevOpacity;
        private float shownAlert;
    }

    public static void onComputeFov(ViewportEvent.ComputeFov event) {
        lastFov = event.getFOV();
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            FADES.clear();
            return;
        }
        for (Entity entity : minecraft.level.entitiesForRendering()) {
            if (!ACExSubmarineHunt.isHunter(entity))
                continue;
            boolean showing = ACExSubmarineHunt.getAlert(entity) > 0.0F && isMySubmarine(minecraft, ACExSubmarineHunt.getAlertTarget(entity));
            Fade fade = FADES.get(entity);
            if (fade == null) {
                if (!showing)
                    continue;
                fade = new Fade();
                FADES.put(entity, fade);
            }
            fade.prevOpacity = fade.opacity;
            if (showing) {
                fade.shownAlert = ACExSubmarineHunt.getAlert(entity);
                fade.opacity = Math.min(1.0F, fade.opacity + 1.0F / FADE_TICKS);
            } else {
                fade.opacity = Math.max(0.0F, fade.opacity - 1.0F / FADE_TICKS);
                if (fade.opacity <= 0.0F && fade.prevOpacity <= 0.0F) {
                    FADES.remove(entity);
                }
            }
        }
    }

    private static boolean isMySubmarine(Minecraft minecraft, int submarineId) {
        if (submarineId == -1 || minecraft.player == null)
            return false;
        if (ACExSubmarineRemote.LOCAL.submarineId == submarineId)
            return true;
        return minecraft.player.getVehicle() instanceof SubmarineEntity submarine && submarine.getId() == submarineId;
    }

    private static float opacity(Entity entity, float partialTick) {
        Fade fade = FADES.get(entity);
        return fade == null ? 0.0F : Mth.lerp(partialTick, fade.prevOpacity, fade.opacity);
    }

    private static float shownAlert(Entity entity) {
        Fade fade = FADES.get(entity);
        return fade == null ? 0.0F : fade.shownAlert;
    }

    private static int withOpacity(int color, float opacity) {
        return (Math.round(FastColor.ARGB32.alpha(color) * opacity) & 255) << 24 | (color & 0xFFFFFF);
    }

    private static int fillColor(float alert) {
        if (alert >= 1.0F && (Util.getMillis() / 150L) % 2L == 0L)
            return 0xFFFFFFFF;
        return FastColor.ARGB32.lerp(alert, EMPTY_COLOR, FULL_COLOR);
    }

    public static void onRenderLiving(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        float opacity = opacity(entity, event.getPartialTick());
        if (opacity <= 0.0F)
            return;
        float alert = shownAlert(entity);
        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.translate(0.0D, entity.getBbHeight() + HEAD_OFFSET, 0.0D);
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        poseStack.scale(WORLD_SCALE, -WORLD_SCALE, WORLD_SCALE);
        Matrix4f pose = poseStack.last().pose();
        drawBar(event.getMultiBufferSource().getBuffer(RenderType.textBackgroundSeeThrough()), pose, alert, opacity * SEE_THROUGH_ALPHA);
        drawBar(event.getMultiBufferSource().getBuffer(RenderType.textBackground()), pose, alert, opacity);
        poseStack.popPose();
    }

    private static void drawBar(VertexConsumer consumer, Matrix4f pose, float alert, float opacity) {
        float left = -BAR_WIDTH / 2.0F;
        float right = left + BAR_WIDTH;
        float top = -(BAR_HEIGHT + DOT_GAP + DOT_SIZE) / 2.0F;
        float bottom = top + BAR_HEIGHT;
        float fillTop = top + BAR_HEIGHT * (1.0F - alert);
        float dotTop = bottom + DOT_GAP;
        float dotBottom = dotTop + DOT_SIZE;
        int color = withOpacity(fillColor(alert), opacity);
        int background = withOpacity(BACKGROUND, opacity);
        frame(consumer, pose, left, top, right, bottom, background);
        if (fillTop > top) {
            quad(consumer, pose, left, top, right, fillTop, background);
        }
        quad(consumer, pose, left, fillTop, right, bottom, color);
        frame(consumer, pose, left, dotTop, right, dotBottom, background);
        quad(consumer, pose, left, dotTop, right, dotBottom, alert >= 1.0F ? color : background);
    }

    private static void frame(VertexConsumer consumer, Matrix4f pose, float x0, float y0, float x1, float y1, int background) {
        quad(consumer, pose, x0 - 1, y0 - 1, x1 + 1, y0, background);
        quad(consumer, pose, x0 - 1, y1, x1 + 1, y1 + 1, background);
        quad(consumer, pose, x0 - 1, y0, x0, y1, background);
        quad(consumer, pose, x1, y0, x1 + 1, y1, background);
    }

    private static void quad(VertexConsumer consumer, Matrix4f pose, float x0, float y0, float x1, float y1, int color) {
        consumer.vertex(pose, x0, y0, 0.0F).color(color).uv2(LightTexture.FULL_BRIGHT).endVertex();
        consumer.vertex(pose, x0, y1, 0.0F).color(color).uv2(LightTexture.FULL_BRIGHT).endVertex();
        consumer.vertex(pose, x1, y1, 0.0F).color(color).uv2(LightTexture.FULL_BRIGHT).endVertex();
        consumer.vertex(pose, x1, y0, 0.0F).color(color).uv2(LightTexture.FULL_BRIGHT).endVertex();
    }

    private static void drawGuiBar(GuiGraphics graphics, int centerX, int centerY, float alert, float opacity) {
        int left = centerX - BAR_WIDTH / 2;
        int top = centerY - (BAR_HEIGHT + DOT_GAP + DOT_SIZE) / 2;
        int dotTop = top + BAR_HEIGHT + DOT_GAP;
        int color = withOpacity(fillColor(alert), opacity);
        int background = withOpacity(BACKGROUND, opacity);
        graphics.fill(left - 1, top - 1, left + BAR_WIDTH + 1, top + BAR_HEIGHT + 1, background);
        graphics.fill(left - 1, dotTop - 1, left + BAR_WIDTH + 1, dotTop + DOT_SIZE + 1, background);
        int filled = Math.round(BAR_HEIGHT * alert);
        graphics.fill(left, top + BAR_HEIGHT - filled, left + BAR_WIDTH, top + BAR_HEIGHT, color);
        if (alert >= 1.0F) {
            graphics.fill(left, dotTop, left + BAR_WIDTH, dotTop + DOT_SIZE, color);
        }
    }

    private record ScreenPoint(int x, int y, boolean onScreen) {
    }

    private static ScreenPoint project(Camera camera, Vec3 world, int width, int height, int margin) {
        Vector3f look = camera.getLookVector();
        Vector3f up = camera.getUpVector();
        Vector3f leftVector = camera.getLeftVector();
        Vec3 offset = world.subtract(camera.getPosition());
        double forward = offset.x * look.x() + offset.y * look.y() + offset.z * look.z();
        double side = -(offset.x * leftVector.x() + offset.y * leftVector.y() + offset.z * leftVector.z());
        double vertical = offset.x * up.x() + offset.y * up.y() + offset.z * up.z();
        double tanY = Math.tan(Math.toRadians(lastFov) / 2.0D);
        double tanX = tanY * width / height;
        double halfWidth = width / 2.0D;
        double halfHeight = height / 2.0D;
        if (forward > 0.0D) {
            double screenX = halfWidth + side / forward / tanX * halfWidth;
            double screenY = halfHeight - vertical / forward / tanY * halfHeight;
            if (screenX >= margin && screenX <= width - margin && screenY >= margin && screenY <= height - margin) {
                return new ScreenPoint(Mth.floor(screenX), Mth.floor(screenY), true);
            }
        }
        double dirX = side;
        double dirY = -vertical;
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        if (length < 1.0E-4D) {
            dirX = 0.0D;
            dirY = 1.0D;
        } else {
            dirX /= length;
            dirY /= length;
        }
        double reachX = halfWidth - margin;
        double reachY = halfHeight - margin;
        double reach = Math.min(Math.abs(dirX) > 1.0E-4D ? reachX / Math.abs(dirX) : Double.MAX_VALUE, Math.abs(dirY) > 1.0E-4D ? reachY / Math.abs(dirY) : Double.MAX_VALUE);
        return new ScreenPoint(Mth.floor(halfWidth + dirX * reach), Mth.floor(halfHeight + dirY * reach), false);
    }

    public static void renderOverlay(GuiGraphics graphics, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null || minecraft.options.hideGui)
            return;
        
        Camera camera = minecraft.gameRenderer.getMainCamera();
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        for (Map.Entry<Entity, Fade> entry : FADES.entrySet()) {
            Entity hunter = entry.getKey();
            float opacity = opacity(hunter, partialTick);
            if (opacity <= 0.0F)
                continue;
            ScreenPoint point = project(camera, hunter.getPosition(partialTick).add(0.0D, hunter.getBbHeight(), 0.0D), width, height, EDGE_MARGIN);
            if (!point.onScreen()) {
                drawGuiBar(graphics, point.x(), point.y(), shownAlert(hunter), opacity);
            }
        }
        if (ACExSubmarineRemoteClient.isDriving()) {
            renderPlayerTracker(graphics, minecraft, camera, partialTick, width, height);
        }
    }

    private static void renderPlayerTracker(GuiGraphics graphics, Minecraft minecraft, Camera camera, float partialTick, int width, int height) {
        Vec3 body = minecraft.player.getPosition(partialTick).add(0.0D, minecraft.player.getBbHeight() + 0.3D, 0.0D);
        ScreenPoint point = project(camera, body, width, height, TRACKER_MARGIN);
        int distance = Mth.floor(camera.getPosition().distanceTo(minecraft.player.getPosition(partialTick)));
        Component text = Component.translatable("misc.alexscavesexemplified.submarine_tracker_distance", distance);
        int textWidth = minecraft.font.width(text);
        int faceX = point.x() - FACE_SIZE / 2;
        int faceY = point.y() - FACE_SIZE - 1;
        graphics.fill(faceX - 1, faceY - 1, faceX + FACE_SIZE + 1, faceY + FACE_SIZE + 1, BACKGROUND);
        PlayerFaceRenderer.draw(graphics, minecraft.player.getSkinTextureLocation(), faceX, faceY, FACE_SIZE);
        graphics.fill(point.x() - textWidth / 2 - 2, point.y() + 1, point.x() + textWidth / 2 + 2, point.y() + 2 + minecraft.font.lineHeight, BACKGROUND);
        graphics.drawString(minecraft.font, text, point.x() - textWidth / 2, point.y() + 2, TRACKER_TEXT_COLOR, false);
    }
}
