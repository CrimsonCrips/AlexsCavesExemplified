package org.crimsoncrips.alexscavesexemplified.client.entity;

import com.github.alexmodguy.alexscaves.client.model.GumWormSegmentModel;
import com.github.alexmodguy.alexscaves.client.render.entity.GumWormSegmentRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.GumWormEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GumWormSegmentEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.server.entity.GumwirmEntity;

public class GumwirmSegmentRenderer extends EntityRenderer<GumWormSegmentEntity> {

    private static final ResourceLocation TEXTURE_0 = ResourceLocation.fromNamespaceAndPath("alexscaves", "textures/entity/gum_worm_segment_0.png");
    private static final ResourceLocation TEXTURE_1 = ResourceLocation.fromNamespaceAndPath("alexscaves", "textures/entity/gum_worm_segment_1.png");
    private static final ResourceLocation TEXTURE_2 = ResourceLocation.fromNamespaceAndPath("alexscaves", "textures/entity/gum_worm_segment_2.png");
    private static final ResourceLocation TEXTURE_CONNECTOR = ResourceLocation.fromNamespaceAndPath("alexscaves", "textures/entity/gum_worm_connection.png");
    private static final GumWormSegmentModel MODEL = new GumWormSegmentModel();
    private static final float SCALE = GumwirmEntity.SCALE;

    public GumwirmSegmentRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(GumWormSegmentEntity entity, Frustum camera, double x, double y, double z) {
        if (super.shouldRender(entity, camera, x, y, z)) {
            return true;
        }
        Entity front = entity.getFrontEntity();
        if (front == null) {
            return false;
        }
        Vec3 from = entity.position();
        Vec3 to = front.position();
        return camera.isVisible(new AABB(to.x, to.y, to.z, from.x, from.y, from.z));
    }

    @Override
    public void render(GumWormSegmentEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        Entity front = entity.getFrontEntity();
        Entity back = entity.getBackEntity();
        float yRotLerp = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float xRotLerp = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        int overlay = GumWormSegmentRenderer.getOverlayCoords(entity, 0.0F);
        poseStack.pushPose();
        poseStack.scale(SCALE, SCALE, SCALE);
        poseStack.translate(0.0, 1.0, 0.0);
        poseStack.mulPose(Axis.YN.rotationDegrees(yRotLerp));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRotLerp + 180.0F));
        poseStack.translate(0.0, -0.5, 0.0);
        MODEL.setGumVisible(front != null, back != null);
        MODEL.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTicks, 0.0F, 0.0F);
        MODEL.renderToBuffer(poseStack, bufferIn.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity))), packedLightIn, overlay, -1);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        if (front != null) {
            Vec3 centeredHeight = new Vec3(0.0, entity.getBbHeight() * 0.5F, 0.0);
            Vec3 from = new Vec3(0.0, 0.0, -0.9F * SCALE)
                    .xRot((float) Math.toRadians(180.0F - xRotLerp))
                    .yRot(-((float) Math.toRadians(yRotLerp)))
                    .add(centeredHeight);
            Vec3 attachment = front.getPosition(partialTicks).add(centeredHeight);
            float zBack = 0.0F;
            if (front instanceof GumWormSegmentEntity) {
                zBack = -0.9F * SCALE;
            } else if (front instanceof GumWormEntity) {
                zBack = -1.7F * SCALE;
            }
            float theirYRotLerp = Mth.lerp(partialTicks, front.yRotO, front.getYRot());
            float theirXRotLerp = front.getViewXRot(partialTicks);
            Vec3 to = attachment.add(new Vec3(0.0, 0.0, zBack)
                            .xRot(-((float) Math.toRadians(theirXRotLerp)))
                            .yRot(-((float) Math.toRadians(theirYRotLerp))))
                    .subtract(entity.getPosition(partialTicks));
            VertexConsumer gumConsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_CONNECTOR));
            poseStack.pushPose();
            poseStack.translate(from.x, from.y, from.z);
            poseStack.scale(SCALE, SCALE, SCALE);
            GumWormSegmentRenderer.renderGum(to.subtract(from).scale(1.0F / SCALE), poseStack, gumConsumer, packedLightIn, overlay);
            poseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(GumWormSegmentEntity entity) {
        return switch (entity.getIndex() % 3) {
            case 1 -> TEXTURE_1;
            case 2 -> TEXTURE_2;
            default -> TEXTURE_0;
        };
    }
}
