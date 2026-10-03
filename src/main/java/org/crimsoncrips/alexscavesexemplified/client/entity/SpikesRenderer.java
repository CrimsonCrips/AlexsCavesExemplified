package org.crimsoncrips.alexscavesexemplified.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.entity.SpikesEntity;
import org.joml.Matrix4f;

public class SpikesRenderer extends EntityRenderer<SpikesEntity> {

    private static final ResourceLocation[] TEXTURES = {
            AlexsCavesExemplified.prefix("textures/entity/tremorzilla/spikes/long.png"),
            AlexsCavesExemplified.prefix("textures/entity/tremorzilla/spikes/short.png"),
            AlexsCavesExemplified.prefix("textures/entity/tremorzilla/spikes/wide.png")
    };
    private static final float SIZE = 2.6F;

    public SpikesRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(SpikesEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float progress = entity.heightProgress(partialTicks);
        if (progress <= 0.0F)
            return;
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));
        poseStack.pushPose();
        poseStack.translate(0.0F, -SIZE * (1.0F - progress), 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
        for (int plane = 0; plane < 2; plane++) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(45.0F + plane * 90.0F));
            Matrix4f matrix = poseStack.last().pose();
            vertex(consumer, poseStack, matrix, -SIZE / 2.0F, 0.0F, 0.0F, 1.0F);
            vertex(consumer, poseStack, matrix, SIZE / 2.0F, 0.0F, 1.0F, 1.0F);
            vertex(consumer, poseStack, matrix, SIZE / 2.0F, SIZE, 1.0F, 0.0F);
            vertex(consumer, poseStack, matrix, -SIZE / 2.0F, SIZE, 0.0F, 0.0F);
            poseStack.popPose();
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private static void vertex(VertexConsumer consumer, PoseStack poseStack, Matrix4f matrix, float x, float y, float u, float v) {
        consumer.addVertex(matrix, x, y, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(SpikesEntity entity) {
        return TEXTURES[entity.getVariant()];
    }
}
