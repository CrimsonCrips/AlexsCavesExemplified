package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.model.TremorzillaModel;
import com.github.alexmodguy.alexscaves.client.render.entity.layer.TremorzillaRiderLayer;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExGammaAttacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TremorzillaRiderLayer.class)
public abstract class ACExTremorzillaRiderLayerMixin extends RenderLayer<TremorzillaEntity, TremorzillaModel> {

    @Unique
    private static final float EYE_DOWN = -5.0F / 16.0F;
    @Unique
    private static final float EYE_BACK = -14.0F / 16.0F;

    public ACExTremorzillaRiderLayerMixin(RenderLayerParent<TremorzillaEntity, TremorzillaModel> renderer) {
        super(renderer);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void alexsCavesExemplified$render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, TremorzillaEntity tremorzilla, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (!tremorzilla.getData(ACExAttachments.GAMMA))
            return;
        ci.cancel();
        if (!tremorzilla.isVehicle())
            return;
        float bodyYaw = tremorzilla.yBodyRotO + (tremorzilla.yBodyRot - tremorzilla.yBodyRotO) * partialTicks;
        float inverse = 1.0F / ACExGammaAttacks.modelScale(tremorzilla);
        Minecraft minecraft = Minecraft.getInstance();
        for (Entity passenger : tremorzilla.getPassengers()) {
            if (passenger == minecraft.player && minecraft.options.getCameraType().isFirstPerson())
                continue;
            poseStack.pushPose();
            this.getParentModel().translateToNeck(poseStack);
            poseStack.translate(0.0F, EYE_DOWN, EYE_BACK);
            poseStack.mulPose(Axis.XN.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.YN.rotationDegrees(360.0F - bodyYaw));
            poseStack.scale(inverse, inverse, inverse);
            poseStack.translate(0.0F, ACExGammaAttacks.RIDER_LIFT - passenger.getEyeHeight(), 0.0F);
            AlexsCaves.PROXY.releaseRenderingEntity(passenger.getUUID());
            TremorzillaRiderLayer.renderPassenger(passenger, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, poseStack, buffer, packedLight);
            AlexsCaves.PROXY.blockRenderingEntity(passenger.getUUID());
            poseStack.popPose();
        }
    }
}
