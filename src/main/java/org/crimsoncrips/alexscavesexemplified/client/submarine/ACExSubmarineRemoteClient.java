package org.crimsoncrips.alexscavesexemplified.client.submarine;


import net.minecraft.client.Camera;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import org.crimsoncrips.alexscavesexemplified.backport.ClientTickEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.crimsoncrips.alexscavesexemplified.backport.PacketDistributor;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;
import org.crimsoncrips.alexscavesexemplified.networking.ACExSubmarineRemotePacket;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class ACExSubmarineRemoteClient {

    private static final float CAMERA_DISTANCE = 9.0F;
    private static final float CAMERA_PITCH = 0.0F;
    private static final ResourceLocation VIGNETTE = new ResourceLocation("textures/misc/vignette.png");
    private static final float BODY_SOUND_VOLUME = 0.25F;
    private static final float VIGNETTE_START = 0.4F;
    private static final float VIGNETTE_COVER_START = 0.6F;
    private static final float VIGNETTE_MAX_COVER = 0.85F;
    private static final int FADE_OUT = ACExUtils.seconds(0.4F);
    private static final int FADE_HOLD = ACExUtils.seconds(0.15F);
    private static final int FADE_IN = ACExUtils.seconds(0.4F);

    private static int submarineId = -1;
    private static int fadeTicks = -1;
    private static int pendingSubmarine = -1;
    private static boolean exiting;
    private static boolean exitRequested;

    public static boolean isDriving() {
        return submarineId != -1;
    }

    private static boolean isFading() {
        return fadeTicks != -1;
    }

    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!event.getLevel().isClientSide || !ACExSubmarineRemote.enabled())
            return;
        UUID linked = ACExSubmarineRemote.linkedTo(event.getItemStack());
        if (linked == null)
            return;
        Minecraft minecraft = Minecraft.getInstance();
        if (isDriving() && !isFading()) {
            beginExit();
        } else if (!isFading()) {
            SubmarineEntity submarine = findSubmarine(minecraft, linked);
            if (submarine != null && (submarine.isVehicle() || ACExSubmarineRemote.drivenByOther(minecraft.player, submarine))) {
                minecraft.player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.submarine_remote_in_use"), true);
            } else if (submarine != null && ACExSubmarineRemote.canDrive(minecraft.player, submarine)) {
                beginFade(submarine.getId());
            } else {
                minecraft.player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.submarine_remote_out_of_range"), true);
            }
        }
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    private static SubmarineEntity findSubmarine(Minecraft minecraft, UUID uuid) {
        if (minecraft.level == null || minecraft.player == null)
            return null;
        for (SubmarineEntity submarine : minecraft.level.getEntitiesOfClass(SubmarineEntity.class, minecraft.player.getBoundingBox().inflate(ACExSubmarineRemote.RANGE))) {
            if (submarine.getUUID().equals(uuid)) {
                return submarine;
            }
        }
        return null;
    }

    private static void beginFade(int targetSubmarine) {
        fadeTicks = 0;
        pendingSubmarine = targetSubmarine;
    }

    private static void beginExit() {
        exiting = true;
        beginFade(-1);
    }

    private static void switchCamera(Minecraft minecraft) {
        if (pendingSubmarine == -1) {
            stop(minecraft);
            return;
        }
        Entity entity = minecraft.level == null ? null : minecraft.level.getEntity(pendingSubmarine);
        if (minecraft.player != null && entity instanceof SubmarineEntity submarine && ACExSubmarineRemote.canDrive(minecraft.player, submarine)) {
            start(minecraft, submarine);
        }
    }

    private static void start(Minecraft minecraft, SubmarineEntity submarine) {
        submarine.setAcceleration(0.0F);
        submarine.setDeltaMovement(Vec3.ZERO);
        submarineId = submarine.getId();
        ACExSubmarineRemote.LOCAL.submarineId = submarineId;
        exitRequested = false;
        minecraft.setCameraEntity(submarine);
        minecraft.player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.submarine_remote_start", minecraft.options.keyShift.getTranslatedKeyMessage()), true);
    }

    private static void stop(Minecraft minecraft) {
        if (minecraft.level != null && minecraft.level.getEntity(submarineId) instanceof SubmarineEntity submarine) {
            submarine.setAcceleration(0.0F);
            submarine.setDeltaMovement(Vec3.ZERO);
        }
        submarineId = -1;
        exiting = false;
        ACExSubmarineRemote.LOCAL.reset();
        if (minecraft.player != null) {
            minecraft.setCameraEntity(minecraft.player);
        }
    }

    public static void onMovementInput(MovementInputUpdateEvent event) {
        if (!isDriving())
            return;
        Minecraft minecraft = Minecraft.getInstance();
        ACExSubmarineRemote.LocalControl input = ACExSubmarineRemote.LOCAL;
        if (exiting) {
            input.clearInput();
        } else {
            input.strafe = event.getInput().leftImpulse;
            input.forward = event.getInput().forwardImpulse;
            input.up = event.getInput().jumping;
            input.down = minecraft.options.keySprint.isDown();
        }
        exitRequested |= event.getInput().shiftKeyDown;
        event.getInput().leftImpulse = 0.0F;
        event.getInput().forwardImpulse = 0.0F;
        event.getInput().up = false;
        event.getInput().down = false;
        event.getInput().left = false;
        event.getInput().right = false;
        event.getInput().jumping = false;
        event.getInput().shiftKeyDown = false;
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (isFading()) {
            fadeTicks++;
            if (fadeTicks == FADE_OUT) {
                switchCamera(minecraft);
            }
            if (fadeTicks >= FADE_OUT + FADE_HOLD + FADE_IN) {
                fadeTicks = -1;
            }
        }
        if (!isDriving())
            return;
        LocalPlayer player = minecraft.player;
        Entity entity = minecraft.level == null ? null : minecraft.level.getEntity(submarineId);
        if (player == null || !(entity instanceof SubmarineEntity submarine)) {
            stop(minecraft);
            return;
        }
        if (!exiting && !isFading() && (exitRequested || !ACExSubmarineRemote.canDrive(player, submarine))) {
            beginExit();
        }
        player.setSprinting(false);
        PacketDistributor.sendToServer(new ACExSubmarineRemotePacket(submarineId, submarine.getX(), submarine.getY(), submarine.getZ(), submarine.getYRot(), submarine.getXRot()));
    }

    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (isDriving() && event.getCamera().getEntity() instanceof SubmarineEntity) {
            event.setPitch(CAMERA_PITCH);
        }
    }

    public static double cameraDistance(Camera camera, double distance) {
        if (isDriving() && camera.getEntity() instanceof SubmarineEntity) {
            return CAMERA_DISTANCE;
        }
        return distance;
    }

    public static void onRenderHand(RenderHandEvent event) {
        if (isDriving()) {
            event.setCanceled(true);
        }
    }

    public static void onRenderGui(RenderGuiEvent.Post event) {
        renderDistanceVignette(event.getGuiGraphics());
        if (!isFading())
            return;
        float time = fadeTicks + event.getPartialTick();
        float alpha;
        if (time < FADE_OUT) {
            alpha = time / FADE_OUT;
        } else if (time < FADE_OUT + FADE_HOLD) {
            alpha = 1.0F;
        } else {
            alpha = 1.0F - (time - FADE_OUT - FADE_HOLD) / FADE_IN;
        }
        int opacity = (int) (Mth.clamp(alpha, 0.0F, 1.0F) * 255.0F);
        if (opacity <= 0)
            return;
        GuiGraphics graphics = event.getGuiGraphics();
        graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), opacity << 24);
    }

    private static void renderDistanceVignette(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!isDriving() || minecraft.player == null || minecraft.level == null || !(minecraft.level.getEntity(submarineId) instanceof SubmarineEntity submarine))
            return;
        float reach = (float) (minecraft.player.distanceTo(submarine) / ACExSubmarineRemote.RANGE);
        float strength = Mth.clamp((reach - VIGNETTE_START) / (1.0F - VIGNETTE_START), 0.0F, 1.0F);
        if (strength <= 0.0F)
            return;
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        graphics.setColor(strength, strength, strength, 1.0F);
        graphics.blit(VIGNETTE, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        float cover = Mth.clamp((strength - VIGNETTE_COVER_START) / (1.0F - VIGNETTE_COVER_START), 0.0F, 1.0F);
        if (cover > 0.0F) {
            graphics.fill(0, 0, width, height, (int) (cover * VIGNETTE_MAX_COVER * 255.0F) << 24);
        }
    }

    public static void onPlaySound(PlaySoundEvent event) {
        SoundInstance sound = event.getSound();
        Minecraft minecraft = Minecraft.getInstance();
        if (sound == null || sound.isRelative() || sound instanceof TickableSoundInstance || !isDriving() || minecraft.player == null || minecraft.level == null || !(minecraft.level.getEntity(submarineId) instanceof SubmarineEntity submarine))
            return;
        Vec3 at = new Vec3(sound.getX(), sound.getY(), sound.getZ());
        if (at.distanceToSqr(minecraft.player.position()) < at.distanceToSqr(submarine.position())) {
            event.setSound(new ACExMuffledSoundInstance(sound, BODY_SOUND_VOLUME));
        }
    }

    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        submarineId = -1;
        ACExSubmarineRemote.LOCAL.reset();
        fadeTicks = -1;
        pendingSubmarine = -1;
        exiting = false;
    }
}
