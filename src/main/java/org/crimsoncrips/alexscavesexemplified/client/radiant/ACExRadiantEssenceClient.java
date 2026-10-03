package org.crimsoncrips.alexscavesexemplified.client.radiant;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ACExRadiantEssenceClient {

    private static final long FLIGHT_TIME = ACExUtils.secondsInMillis(1.0F);
    private static final double FLIGHT_DISTANCE = 1.8D;
    private static final double FLIGHT_RISE = 0.7D;
    private static final long METER_SHOW_TIME = ACExUtils.secondsInMillis(3.0F);
    private static final long METER_FADE_TIME = ACExUtils.secondsInMillis(0.6F);

    private static final float FRONT_CONE = 60.0F;

    private static final ResourceLocation BOTTLE = AlexsCavesExemplified.prefix("textures/gui/radiant_meter.png");
    private static final int BOTTLE_SIZE = 48;
    private static final int TEXT_Y = 35;
    private static final float TEXT_SCALE = 1.5F;
    private static final int[] BOTTLE_MASK = {0, 0, 0, 0, 256, 256, 256, 896, 3840, 8144, 8176, 12280, 12280, 14328, 0, 0};
    private static final int FULL_METER = 100;
    private static final float FILL_SPEED = 1.5F;
    private static final float RAINBOW_SPEED = 0.25F;
    private static final float SLOSH_STIFFNESS = 60.0F;
    private static final float SLOSH_DAMPING = 4.0F;
    private static final float SLOSH_TURN = 0.02F;
    private static final float SLOSH_STRAFE = 0.5F;
    private static final float SLOSH_MAX = 0.6F;

    private static float slosh = 0.0F;
    private static float sloshVelocity = 0.0F;
    private static float lastYaw = 0.0F;
    private static float lastStrafe = 0.0F;
    private static long lastSloshTime = -1L;
    private static float fill = 0.0F;
    private static final RandomSource RANDOM = RandomSource.create();

    private record Flight(int entityId, long start, float yaw) {
    }

    private static final List<Flight> FLIGHTS = new ArrayList<>();
    private static ItemStack essence = ItemStack.EMPTY;
    private static int lastMeter = -1;
    private static long meterChanged = -1L;

    public static void playConsume(int entityId) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity entity = minecraft.level == null ? null : minecraft.level.getEntity(entityId);
        float facing = entity == null ? 0.0F : entity.getYHeadRot();
        float yaw = facing + FRONT_CONE + RANDOM.nextFloat() * (360.0F - FRONT_CONE * 2);
        FLIGHTS.add(new Flight(entityId, Util.getMillis(), yaw));
    }

    private static ItemStack essence() {
        if (essence.isEmpty()) {
            essence = new ItemStack(ACItemRegistry.RADIANT_ESSENCE.get());
        }
        return essence;
    }

    public static void renderFlights(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS || FLIGHTS.isEmpty())
            return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            FLIGHTS.clear();
            return;
        }
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        Camera camera = event.getCamera();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
        long now = Util.getMillis();
        Iterator<Flight> iterator = FLIGHTS.iterator();
        while (iterator.hasNext()) {
            Flight flight = iterator.next();
            float t = (now - flight.start()) / (float) FLIGHT_TIME;
            Entity entity = minecraft.level.getEntity(flight.entityId());
            if (t >= 1.0F || entity == null) {
                iterator.remove();
                continue;
            }
            float arc = Mth.sin((float) Math.PI * t);
            Vec3 origin = entity.getPosition(partialTick).add(0.0D, entity.getBbHeight() * 0.5D, 0.0D);
            Vec3 around = Vec3.directionFromRotation(0.0F, flight.yaw());
            Vec3 pos = origin.add(around.scale(FLIGHT_DISTANCE * arc)).add(0.0D, FLIGHT_RISE * arc, 0.0D);
            Vec3 relative = pos.subtract(camera.getPosition());

            poseStack.pushPose();
            poseStack.translate(relative.x, relative.y, relative.z);
            poseStack.mulPose(Axis.YP.rotationDegrees(t * 720.0F));
            float scale = 0.6F + 0.4F * arc;
            poseStack.scale(scale, scale, scale);
            minecraft.getItemRenderer().renderStatic(essence(), ItemDisplayContext.GROUND, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, poseStack, buffer, minecraft.level, flight.entityId());
            poseStack.popPose();
        }
        buffer.endBatch();
    }

    public static void renderMeter(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.options.hideGui)
            return;
        int meter = player.getData(ACExAttachments.RADIANT_METER);
        long now = Util.getMillis();
        if (player.isHolding(ACItemRegistry.SUGAR_STAFF.get())) {
            meterChanged = now;
        }
        if (meter != lastMeter) {
            if (lastMeter != -1) {
                meterChanged = now;
            }
            lastMeter = meter;
        }
        long shown = now - meterChanged;
        if (meterChanged < 0L || shown > METER_SHOW_TIME)
            return;
        float alpha = shown > METER_SHOW_TIME - METER_FADE_TIME ? (METER_SHOW_TIME - shown) / (float) METER_FADE_TIME : 1.0F;
        tickSlosh(player, now);
        int bottleX = guiGraphics.guiWidth() - BOTTLE_SIZE - 4;
        int bottleY = guiGraphics.guiHeight() - BOTTLE_SIZE - 4;
        if (fill > 0.001F) {
            renderFluid(guiGraphics, bottleX, bottleY, now, alpha, fluidSurface(fill));
        }
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.enableBlend();
        guiGraphics.blit(BOTTLE, bottleX, bottleY, BOTTLE_SIZE, BOTTLE_SIZE, 0.0F, 0.0F, 16, 16, 16, 16);
        RenderSystem.disableBlend();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        Font font = minecraft.font;
        String text = String.valueOf(meter);
        int alphaByte = Math.max(4, (int) (alpha * 255.0F));
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(bottleX + BOTTLE_SIZE / 2.0F, bottleY + TEXT_Y, 0.0F);
        guiGraphics.pose().scale(TEXT_SCALE, TEXT_SCALE, 1.0F);
        guiGraphics.drawString(font, text, -font.width(text) / 2, -font.lineHeight / 2, (alphaByte << 24) | 0xFFFFFF, true);
        guiGraphics.pose().popPose();
    }

    private static void tickSlosh(LocalPlayer player, long now) {
        float dt = lastSloshTime < 0L ? 0.0F : Math.min((now - lastSloshTime) / 1000.0F, 0.05F);
        lastSloshTime = now;
        float yaw = player.getYRot();
        float yawSpeed = dt > 0.0F ? Mth.wrapDegrees(yaw - lastYaw) / dt : 0.0F;
        lastYaw = yaw;
        Vec3 right = Vec3.directionFromRotation(0.0F, yaw + 90.0F);
        float strafe = (float) player.getDeltaMovement().dot(right);
        float drive = yawSpeed * SLOSH_TURN + (strafe - lastStrafe) / Math.max(dt, 0.001F) * SLOSH_STRAFE;
        lastStrafe = strafe;
        float target = Math.min(player.getData(ACExAttachments.RADIANT_METER), FULL_METER) / (float) FULL_METER;
        fill += (target - fill) * Math.min(1.0F, dt * FILL_SPEED);
        sloshVelocity += (-SLOSH_STIFFNESS * slosh - SLOSH_DAMPING * sloshVelocity + drive) * dt;
        slosh = Mth.clamp(slosh + sloshVelocity * dt, -SLOSH_MAX, SLOSH_MAX);
    }

    private static float fluidSurface(float fraction) {
        int total = 0;
        for (int row : BOTTLE_MASK) {
            total += Integer.bitCount(row);
        }
        float remaining = fraction * total;
        for (int y = BOTTLE_MASK.length - 1; y >= 0; y--) {
            int width = Integer.bitCount(BOTTLE_MASK[y]);
            if (width == 0)
                continue;
            if (remaining <= width) {
                return y + 1 - remaining / width;
            }
            remaining -= width;
        }
        return 0.0F;
    }

    private static void renderFluid(GuiGraphics guiGraphics, int bottleX, int bottleY, long now, float alpha, float level) {
        float time = now / 1000.0F;
        int scale = BOTTLE_SIZE / 16;
        for (int gx = 0; gx < BOTTLE_SIZE; gx++) {
            float tx = (gx + 0.5F) / scale;
            float surface = level + slosh * (tx - 8.0F) + 0.3F * Mth.sin(tx * 0.9F + time * 3.0F) + 0.15F * Mth.sin(tx * 1.7F - time * 2.1F);
            boolean top = true;
            for (int gy = 0; gy < BOTTLE_SIZE; gy++) {
                float ty = (gy + 0.5F) / scale;
                if (ty < surface || (BOTTLE_MASK[(int) ty] & (1 << (int) tx)) == 0)
                    continue;
                float hue = time * RAINBOW_SPEED + gy / (float) (BOTTLE_SIZE * 1.5F) + gx / (float) (BOTTLE_SIZE * 3);
                int rgb = Mth.hsvToRgb(hue - Mth.floor(hue), top ? 0.35F : 0.7F, 1.0F);
                int fluidAlpha = (int) (alpha * (top ? 230 : 200));
                guiGraphics.fill(bottleX + gx, bottleY + gy, bottleX + gx + 1, bottleY + gy + 1, (fluidAlpha << 24) | rgb);
                top = false;
            }
        }
    }
}
