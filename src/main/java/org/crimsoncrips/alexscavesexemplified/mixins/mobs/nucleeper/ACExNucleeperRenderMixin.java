package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nucleeper;

import com.github.alexmodguy.alexscaves.client.model.NucleeperModel;
import com.github.alexmodguy.alexscaves.client.render.entity.NucleeperRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.NucleeperXtra;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NucleeperRenderer.class)
public abstract class ACExNucleeperRenderMixin extends MobRenderer<NucleeperEntity, NucleeperModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper.png");

    public ACExNucleeperRenderMixin(EntityRendererProvider.Context pContext, NucleeperModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }


    @Inject(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/living/NucleeperEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"), cancellable = true)
    private void alexsCavesExemplified$render(NucleeperEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, CallbackInfo ci) {
        if (((NucleeperXtra)entityIn).isDefused()){
            ci.cancel();
            poseStack.popPose();
            poseStack.popPose();
            super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        }
    }



}
