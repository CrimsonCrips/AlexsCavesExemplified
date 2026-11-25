package org.crimsoncrips.alexscavesexemplified.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.crimsoncrips.alexscavesexemplified.server.entity.CuriosityOfferingEntity;

public class CuriosityOfferingRenderer extends EntityRenderer<CuriosityOfferingEntity> {

    private final ItemRenderer itemRenderer;

    public CuriosityOfferingRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(CuriosityOfferingEntity offering, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float age = offering.tickCount + partialTicks;
        float scale = offering.getScale(age);
        poseStack.pushPose();
        poseStack.translate(0.0F, offering.getLift(age), 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(offering.getSpin(age)));
        poseStack.scale(scale, scale, scale);
        this.itemRenderer.renderStatic(offering.getItem(), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, offering.level(), offering.getId());
        poseStack.popPose();
        super.render(offering, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CuriosityOfferingEntity offering) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
