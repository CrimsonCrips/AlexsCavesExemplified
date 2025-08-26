package org.crimsoncrips.alexscavesexemplified.mixins.mobs.teletor;

import com.github.alexmodguy.alexscaves.client.render.entity.TeletorRenderer;
import com.github.alexmodguy.alexscaves.server.entity.item.MagneticWeaponEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TeletorEntity;
import com.github.alexthe666.citadel.client.render.LightningRender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;


@Mixin(TeletorRenderer.class)
public abstract class ACExTeletorRenderMixin extends EntityRenderer<MagneticWeaponEntity> {


    @Shadow private Map<UUID, LightningRender> lightningRenderMap;

    protected ACExTeletorRenderMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Inject(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/living/TeletorEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$tick(TeletorEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, CallbackInfo ci) {
       if (entityIn.getWeapon() == null){
           lightningRenderMap.remove(entityIn.getUUID());
       }
    }
}
