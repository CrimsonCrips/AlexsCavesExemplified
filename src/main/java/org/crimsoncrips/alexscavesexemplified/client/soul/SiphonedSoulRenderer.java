package org.crimsoncrips.alexscavesexemplified.client.soul;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.crimsoncrips.alexscavesexemplified.server.entity.SiphonedSoulEntity;

@OnlyIn(Dist.CLIENT)
public class SiphonedSoulRenderer extends EntityRenderer<SiphonedSoulEntity> {

    public SiphonedSoulRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SiphonedSoulEntity soul, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Entity replica = soul.getReplica();
        if (replica == null)
            return;
        float scale = soul.getReplicaScale(partialTicks);
        float yaw = soul.getReplicaYaw();
        replica.setPos(soul.getPosition(partialTicks));
        replica.setYRot(yaw);
        replica.yRotO = yaw;
        if (replica instanceof LivingEntity living) {
            living.yBodyRot = yaw;
            living.yBodyRotO = yaw;
            living.yHeadRot = yaw;
            living.yHeadRotO = yaw;
        }
        poseStack.pushPose();
        poseStack.translate(0.0F, -replica.getBbHeight() * 0.5F * scale, 0.0F);
        poseStack.scale(scale, scale, scale);
        try {
            this.entityRenderDispatcher.getRenderer(replica).render(replica, yaw, partialTicks, poseStack, new ACExSoulBufferSource(bufferSource), LightTexture.FULL_BRIGHT);
        } catch (RuntimeException exception) {
            soul.dropReplica();
        }
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SiphonedSoulEntity soul) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
