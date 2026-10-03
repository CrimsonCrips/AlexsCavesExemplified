package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.client.render.entity.SpinningPeppermintRenderer;
import com.github.alexmodguy.alexscaves.server.entity.item.SpinningPeppermintEntity;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpinningPeppermintRenderer.class)
public class ACExSpinningPeppermintRendererMixin {

    @Inject(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/item/SpinningPeppermintEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void alexsCavesExemplified$render(SpinningPeppermintEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource source, int lightIn, CallbackInfo ci) {
        float scale = entity.getData(ACExAttachments.PEPPERMINT_SCALE);
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
    }

    @ModifyArg(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/item/SpinningPeppermintEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;", ordinal = 2))
    private float alexsCavesExemplified$render2(float degrees, @Local(argsOnly = true) SpinningPeppermintEntity entity) {
        return entity.hasData(ACExAttachments.STAFF_PEPPERMINT) ? -degrees : degrees;
    }

    @Inject(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/item/SpinningPeppermintEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"))
    private void alexsCavesExemplified$render1(SpinningPeppermintEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource source, int lightIn, CallbackInfo ci) {
        poseStack.popPose();
    }
}
