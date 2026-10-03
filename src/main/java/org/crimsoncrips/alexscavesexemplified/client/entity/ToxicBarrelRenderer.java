package org.crimsoncrips.alexscavesexemplified.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.crimsoncrips.alexscavesexemplified.server.entity.ToxicBarrelEntity;

public class ToxicBarrelRenderer extends EntityRenderer<ToxicBarrelEntity> {

    private static final float SPIN = 4.0F;
    private static final float HELD_SPIN = 14.0F;
    private static final float HELD_SHAKE = 0.2F;

    public ToxicBarrelRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 2.0F;
    }

    @Override
    public void render(ToxicBarrelEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float age = entity.tickCount + partialTicks;
        boolean held = entity.isHeld();
        float spin = age * (held ? HELD_SPIN : SPIN);
        poseStack.pushPose();
        poseStack.translate(0.0F, ToxicBarrelEntity.SCALE * 0.5F, 0.0F);
        if (held) {
            poseStack.translate(Mth.sin(age * 3.1F) * HELD_SHAKE, Mth.sin(age * 4.3F) * HELD_SHAKE, Mth.cos(age * 3.7F) * HELD_SHAKE);
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(spin));
        poseStack.mulPose(Axis.ZP.rotationDegrees(spin * 0.6F));
        poseStack.scale(ToxicBarrelEntity.SCALE, ToxicBarrelEntity.SCALE, ToxicBarrelEntity.SCALE);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(ToxicBarrelEntity.displayState(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ToxicBarrelEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
