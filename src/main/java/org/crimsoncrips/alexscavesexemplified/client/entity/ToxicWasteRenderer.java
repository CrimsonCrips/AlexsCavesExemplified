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
import org.crimsoncrips.alexscavesexemplified.server.entity.ToxicWasteEntity;

public class ToxicWasteRenderer extends EntityRenderer<ToxicWasteEntity> {

    private static final float SPIN = 12.0F;

    public ToxicWasteRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(ToxicWasteEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float spin = (entity.tickCount + partialTicks) * SPIN;
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.5F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(spin));
        poseStack.mulPose(Axis.XP.rotationDegrees(spin * 0.7F));
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(entity.displayState(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ToxicWasteEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
