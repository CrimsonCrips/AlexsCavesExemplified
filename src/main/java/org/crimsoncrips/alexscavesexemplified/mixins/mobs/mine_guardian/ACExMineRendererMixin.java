package org.crimsoncrips.alexscavesexemplified.mixins.mobs.mine_guardian;

import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.client.model.MineGuardianModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.entity.MineGuardianRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

@Mixin(MineGuardianRenderer.class)
public abstract class ACExMineRendererMixin extends MobRenderer<MineGuardianEntity, MineGuardianModel> {

    private static final ResourceLocation TEXTURE = ResourceLocation.parse("alexscaves:textures/entity/mine_guardian.png");
    private static final ResourceLocation TEXTURE_SLEEPING = ResourceLocation.parse("alexscaves:textures/entity/mine_guardian_sleeping.png");

    private static final ResourceLocation TEXTURE_NOON = ResourceLocation.parse("alexscavesexemplified:textures/entity/mine_guardian/noon_guardian.png");
    private static final ResourceLocation TEXTURE_NOON_SLEEPING = ResourceLocation.parse("alexscavesexemplified:textures/entity/mine_guardian/noon_guardian_sleeping.png");
    private static final ResourceLocation TEXTURE_NOON_ACTIVE = ResourceLocation.parse("alexscavesexemplified:textures/entity/mine_guardian/noon_guardian_active.png");

    private static final ResourceLocation TEXTURE_AE_NUCLEAR = ResourceLocation.parse("alexscavesexemplified:textures/entity/mine_guardian/nuclear_ae_guardian.png");
    private static final ResourceLocation TEXTURE_AE_NUCLEAR_SLEEPING = ResourceLocation.parse("alexscavesexemplified:textures/entity/mine_guardian/nuclear_ae_guardian_sleeping.png");

    private static final ResourceLocation TEXTURE_JESSE_NUCLEAR = ResourceLocation.parse("alexscavesexemplified:textures/entity/mine_guardian/nuclear_jesse_guardian.png");
    private static final ResourceLocation TEXTURE_JESSE_NUCLEAR_SLEEPING = ResourceLocation.parse("alexscavesexemplified:textures/entity/mine_guardian/nuclear_jesse_guardian_sleeping.png");


    public ACExMineRendererMixin(EntityRendererProvider.Context pContext, MineGuardianModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    private static void nuclearShineOrigin(VertexConsumer p_114220_, Matrix4f p_114221_, float xOffset, float yOffset) {
        p_114220_.addVertex(p_114221_, 0.0F, 0.0F, 0.0F).setColor(10, 240, 50, 255);
    }

    private static void nuclearShineLeft(VertexConsumer p_114215_, Matrix4f p_114216_, float p_114217_, float p_114218_, float xOffset, float yOffset) {
        p_114215_.addVertex(p_114216_, -ACMath.HALF_SQRT_3 * p_114218_, p_114217_, 0).setColor(15, 250, 59, 0);
    }

    private static void nuclearShineRight(VertexConsumer p_114224_, Matrix4f p_114225_, float p_114226_, float p_114227_, float xOffset, float yOffset) {
        p_114224_.addVertex(p_114225_, ACMath.HALF_SQRT_3 * p_114227_, p_114226_, 0).setColor(15, 250, 59, 0);
    }

    private static void noonShineOrigin(VertexConsumer p_114220_, Matrix4f p_114221_, float xOffset, float yOffset) {
        p_114220_.addVertex(p_114221_, 0.0F, 0.0F, 0.0F).setColor(60, 210, 230, 255);
    }

    private static void noonShineLeft(VertexConsumer p_114215_, Matrix4f p_114216_, float p_114217_, float p_114218_, float xOffset, float yOffset) {
        p_114215_.addVertex(p_114216_, -ACMath.HALF_SQRT_3 * p_114218_, p_114217_, 0).setColor(64, 233, 255, 0);
    }

    private static void noonShineRight(VertexConsumer p_114224_, Matrix4f p_114225_, float p_114226_, float p_114227_, float xOffset, float yOffset) {
        p_114224_.addVertex(p_114225_, ACMath.HALF_SQRT_3 * p_114227_, p_114226_, 0).setColor(64, 233, 255, 0);
    }

    private static void ownedShineOrigin(VertexConsumer p_114220_, Matrix4f p_114221_, float xOffset, float yOffset) {
        p_114220_.addVertex(p_114221_, 0.0F, 0.0F, 0.0F).setColor(255, 255, 255, 255);
    }

    private static void ownedShineLeft(VertexConsumer p_114215_, Matrix4f p_114216_, float p_114217_, float p_114218_, float xOffset, float yOffset) {
        p_114215_.addVertex(p_114216_, -ACMath.HALF_SQRT_3 * p_114218_, p_114217_, 0).setColor(255, 255, 255, 0);
    }

    private static void ownedShineRight(VertexConsumer p_114224_, Matrix4f p_114225_, float p_114226_, float p_114227_, float xOffset, float yOffset) {
        p_114224_.addVertex(p_114225_, ACMath.HALF_SQRT_3 * p_114227_, p_114226_, 0).setColor(255, 255, 255, 0);
    }

    @Override
    public ResourceLocation getTextureLocation(MineGuardianEntity entity) {
        return switch (entity.getData(ACExAttachments.MINE_GUARDIAN_VARIANT)) {
            case -1 -> entity.isEyeClosed() ? TEXTURE_NOON_SLEEPING : (entity.isScanning() ? TEXTURE_NOON : TEXTURE_NOON_ACTIVE);
            case 1 -> entity.isEyeClosed() ? TEXTURE_AE_NUCLEAR_SLEEPING : TEXTURE_AE_NUCLEAR;
            case 2 -> entity.isEyeClosed() ? TEXTURE_JESSE_NUCLEAR_SLEEPING : TEXTURE_JESSE_NUCLEAR;
            default -> entity.isEyeClosed() ? TEXTURE_SLEEPING : TEXTURE;
        };
    }

    @Inject(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/living/MineGuardianEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V" ,ordinal = 2),locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void alexsCavesExemplified$render(MineGuardianEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, CallbackInfo ci, float bodyYaw, float scanProgress, float ticks, float length, float width, float extraX) {
        assert Minecraft.getInstance().player != null;
        if (!Minecraft.getInstance().player.getStringUUID().equals(entityIn.getData(ACExAttachments.MINE_GUARDIAN_OWNER))) {
            if (entityIn.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) == -1) {
                ci.cancel();
                poseStack.translate(0.3F, -0.5F, -0.1F);
                PoseStack.Pose posestack$pose = poseStack.last();
                Matrix4f matrix4f1 = posestack$pose.pose();
                VertexConsumer lightConsumer = bufferIn.getBuffer(ACRenderTypes.getSubmarineLights());
                noonShineOrigin(lightConsumer, matrix4f1, 0.0F, 0.0F);
                noonShineLeft(lightConsumer, matrix4f1, length, width, 0.0F, 0.0F);
                noonShineRight(lightConsumer, matrix4f1, length, width, 0.0F, 0.0F);
                noonShineLeft(lightConsumer, matrix4f1, length, width, 0.0F, 0.0F);
                poseStack.popPose();
                poseStack.popPose();
            }
            if (entityIn.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) > 0) {
                ci.cancel();
                poseStack.translate(0.0F, -0.5F, 0.0F);
                PoseStack.Pose posestack$pose = poseStack.last();
                Matrix4f matrix4f1 = posestack$pose.pose();
                VertexConsumer lightConsumer = bufferIn.getBuffer(ACRenderTypes.getSubmarineLights());
                nuclearShineOrigin(lightConsumer, matrix4f1, 0.0F, 0.0F);
                nuclearShineLeft(lightConsumer, matrix4f1, length, width, 0.0F, 0.0F);
                nuclearShineRight(lightConsumer, matrix4f1, length, width, 0.0F, 0.0F);
                nuclearShineLeft(lightConsumer, matrix4f1, length, width, 0.0F, 0.0F);
                poseStack.popPose();
                poseStack.popPose();
            }
        } else {
            ci.cancel();
            if (entityIn.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) == -1) {
                poseStack.translate(0.3F, -0.5F, -0.1F);
            } else {
                poseStack.translate(0.0F, -0.5F, 0.0F);
            }
            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f1 = posestack$pose.pose();
            VertexConsumer lightConsumer = bufferIn.getBuffer(ACRenderTypes.getSubmarineLights());
            ownedShineOrigin(lightConsumer, matrix4f1, 0.0F, 0.0F);
            ownedShineLeft(lightConsumer, matrix4f1, length, width, 0.0F, 0.0F);
            ownedShineRight(lightConsumer, matrix4f1, length, width, 0.0F, 0.0F);
            ownedShineLeft(lightConsumer, matrix4f1, length, width, 0.0F, 0.0F);
            poseStack.popPose();
            poseStack.popPose();
        }
    }

}
