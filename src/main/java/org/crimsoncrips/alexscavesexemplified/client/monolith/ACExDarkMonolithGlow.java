package org.crimsoncrips.alexscavesexemplified.client.monolith;


import org.crimsoncrips.alexscavesexemplified.backport.ACExRenderCompat;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.client.render.ACExRenderTypes;

import java.util.HashMap;
import java.util.Map;

public class ACExDarkMonolithGlow {

    private static final int OUTLINE_RED = 255;
    private static final int OUTLINE_GREEN = 40;
    private static final int OUTLINE_BLUE = 40;
    private static final double OUTLINE_FADE_START = 2.0D;
    private static final double OUTLINE_RANGE = 5.0D;
    private static final int MASK_COLOR = FastColor.ARGB32.color(255, 0, 0, 0);
    private static final int DEFERRED_BUFFER_SIZE = 8192;

    private static final Map<RenderType, BufferBuilder> DEFERRED = new HashMap<>();
    private static boolean outlineRequested;

    public static void render(EntityModel<?> model, PoseStack poseStack, BlockPos monolith, ResourceLocation texture, int light, int overlay, int color) {
        Minecraft minecraft = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        RenderType glow = ACExRenderTypes.getDarkMonolithGlow(texture);
        PostEffectRegistry.renderEffectForNextTick(ClientProxy.IRRADIATED_SHADER);
        ACExRenderCompat.renderToBuffer(model, poseStack, bufferSource.getBuffer(glow), light, overlay, color);
        bufferSource.endBatch(glow);
        ACExRenderCompat.renderToBuffer(model, poseStack, deferred(ACExRenderTypes.getDarkMonolithMask(texture)), light, overlay, MASK_COLOR);

        float outline = outlineStrength(minecraft, monolith);
        if (outline > 0.0F) {
            int outlineColor = FastColor.ARGB32.color(Mth.ceil(Mth.sqrt(outline) * 255.0F), OUTLINE_RED, OUTLINE_GREEN, OUTLINE_BLUE);
            ACExRenderCompat.renderToBuffer(model, poseStack, deferred(ACExRenderTypes.getDarkMonolithOutline(texture)), light, overlay, outlineColor);
            outlineRequested = true;
        }
    }

    private static float outlineStrength(Minecraft minecraft, BlockPos monolith) {
        Level level = minecraft.level;
        if (level == null)
            return 0.0F;
        Vec3 camera = minecraft.gameRenderer.getMainCamera().getPosition();
        Vec3 center = Vec3.atCenterOf(monolith);
        double distance = camera.distanceTo(center);
        if (distance >= OUTLINE_RANGE)
            return 0.0F;
        BlockHitResult hit = level.clip(new ClipContext(camera, center, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, minecraft.getCameraEntity()));
        if (hit.getType() != HitResult.Type.MISS && !hit.getBlockPos().equals(monolith))
            return 0.0F;
        return 1.0F - (float) Mth.clamp((distance - OUTLINE_FADE_START) / (OUTLINE_RANGE - OUTLINE_FADE_START), 0.0D, 1.0D);
    }

    private static VertexConsumer deferred(RenderType type) {
        BufferBuilder builder = DEFERRED.computeIfAbsent(type, key -> new BufferBuilder(DEFERRED_BUFFER_SIZE));
        if (!builder.building()) {
            builder.begin(type.mode(), type.format());
        }
        return builder;
    }

    public static boolean consumeOutlineRequest() {
        boolean requested = outlineRequested;
        outlineRequested = false;
        return requested;
    }

    public static void drawDeferred() {
        if (DEFERRED.isEmpty())
            return;
        DEFERRED.forEach((type, builder) -> {
            if (builder.building()) {
                type.end(builder, RenderSystem.getVertexSorting());
            }
        });
    }
}
