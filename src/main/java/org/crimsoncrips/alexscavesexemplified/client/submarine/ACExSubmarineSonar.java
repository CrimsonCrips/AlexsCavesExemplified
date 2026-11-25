package org.crimsoncrips.alexscavesexemplified.client.submarine;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.backport.ClientTickEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.mixins.misc.render.ACExPostChainAccessor;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import javax.annotation.Nullable;

public class ACExSubmarineSonar {

    public static final KeyMapping KEY = new KeyMapping("key.alexscavesexemplified.submarine_sonar", GLFW.GLFW_KEY_V, "key.categories.alexscavesexemplified");
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final float RANGE = 64.0F;
    private static final int EXPAND_TICKS = ACExUtils.seconds(2.0F);
    private static final int FADE_TICKS = ACExUtils.seconds(3.0F);
    private static final int LIFETIME = EXPAND_TICKS + FADE_TICKS;
    private static final int MAX_DAMAGE_LEVEL = 3;

    @Nullable
    private static PostChain chain;
    @Nullable
    private static ACExSonarQuality chainACExSonarQuality;
    private static boolean chainFailed;
    private static int chainWidth;
    private static int chainHeight;
    @Nullable
    private static Vec3 origin;
    private static long startTime;

    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KEY);
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            origin = null;
            return;
        }
        if (origin != null && minecraft.level.getGameTime() - startTime > LIFETIME) {
            origin = null;
        }
        while (KEY.consumeClick()) {
            if (origin == null && AlexsCavesExemplified.CLIENT_CONFIG.SUBMARINE_SONAR_ENABLED.get() && minecraft.player.getVehicle() instanceof SubmarineEntity submarine && submarine.getDamageLevel() <= MAX_DAMAGE_LEVEL) {
                ping(minecraft, submarine);
            }
        }
    }

    private static void ping(Minecraft minecraft, SubmarineEntity submarine) {
        origin = submarine.position().add(0.0D, submarine.getBbHeight() * 0.5D, 0.0D);
        startTime = minecraft.level.getGameTime();
        minecraft.level.playLocalSound(origin.x, origin.y, origin.z, ACSoundRegistry.SUBMARINE_SONAR.get(), SoundSource.NEUTRAL, 2.0F, 0.8F, false);
    }

    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL || origin == null)
            return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null)
            return;
        float partialTick = event.getPartialTick();
        float age = minecraft.level.getGameTime() - startTime + partialTick;
        float expand = Mth.clamp(age / EXPAND_TICKS, 0.0F, 1.0F);
        float fade = 1.0F - Mth.clamp((age - EXPAND_TICKS) / FADE_TICKS, 0.0F, 1.0F);
        if (fade <= 0.0F)
            return;
        PostChain sonar = chain(minecraft);
        if (sonar == null)
            return;

        Vec3 camera = event.getCamera().getPosition();
        Vector3f viewOrigin = new Matrix4f(event.getPoseStack().last().pose()).transformPosition(new Vector3f((float) (origin.x - camera.x), (float) (origin.y - camera.y), (float) (origin.z - camera.z)));
        Matrix4f inverseProjection = new Matrix4f(event.getProjectionMatrix()).invert();
        float radius = RANGE * (1.0F - (1.0F - expand) * (1.0F - expand));

        for (PostPass pass : ((ACExPostChainAccessor) sonar).getPasses()) {
            EffectInstance effect = pass.getEffect();
            effect.safeGetUniform("InvProjMat").set(inverseProjection);
            effect.safeGetUniform("SonarOrigin").set(viewOrigin.x, viewOrigin.y, viewOrigin.z);
            effect.safeGetUniform("SonarRadius").set(radius);
            effect.safeGetUniform("SonarRange").set(RANGE);
            effect.safeGetUniform("SonarFade").set(fade);
        }
        sonar.process(partialTick);
        minecraft.getMainRenderTarget().bindWrite(false);
    }

    @Nullable
    private static PostChain chain(Minecraft minecraft) {
        ACExSonarQuality quality = AlexsCavesExemplified.CLIENT_CONFIG.SUBMARINE_SONAR_QUALITY.get();
        if (chain != null && chainACExSonarQuality != quality) {
            chain.close();
            chain = null;
            chainFailed = false;
        }
        if (chainFailed)
            return null;
        RenderTarget main = minecraft.getMainRenderTarget();
        if (chain == null) {
            chainACExSonarQuality = quality;
            try {
                chain = new PostChain(minecraft.getTextureManager(), minecraft.getResourceManager(), main, quality.chain);
            } catch (Exception exception) {
                chainFailed = true;
                LOGGER.warn("Failed to load submarine sonar shader", exception);
                return null;
            }
            chainWidth = -1;
        }
        if (chainWidth != main.width || chainHeight != main.height) {
            chainWidth = main.width;
            chainHeight = main.height;
            chain.resize(main.width, main.height);
        }
        return chain;
    }
}
