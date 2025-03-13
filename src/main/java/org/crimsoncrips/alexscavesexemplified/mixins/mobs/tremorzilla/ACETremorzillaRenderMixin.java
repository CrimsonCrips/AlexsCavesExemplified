package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import com.github.alexmodguy.alexscaves.client.model.TremorzillaModel;
import com.github.alexmodguy.alexscaves.client.render.entity.CustomBookEntityRenderer;
import com.github.alexmodguy.alexscaves.client.render.entity.TremorzillaRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;


@Mixin(TremorzillaRenderer.class)
public abstract class ACETremorzillaRenderMixin extends MobRenderer<TremorzillaEntity, TremorzillaModel> implements CustomBookEntityRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla.png");
    private static final ResourceLocation TEXTURE_BEAM_INNER = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_beam_inner.png");
    private static final ResourceLocation TEXTURE_BEAM_OUTER = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_beam_outer.png");
    private static final ResourceLocation TEXTURE_BEAM_END_0 = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_beam_end_0.png");
    private static final ResourceLocation TEXTURE_BEAM_END_1 = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_beam_end_1.png");
    private static final ResourceLocation TEXTURE_BEAM_END_2 = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_beam_end_2.png");

    private static final ResourceLocation TEXTURE_RETRO = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_retro.png");
    private static final ResourceLocation TEXTURE_RETRO_BEAM_INNER = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_retro_beam_inner.png");
    private static final ResourceLocation TEXTURE_RETRO_BEAM_OUTER = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_retro_beam_outer.png");
    private static final ResourceLocation TEXTURE_RETRO_BEAM_END_0 = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_gamma_lbeam_end_0.png");
    private static final ResourceLocation TEXTURE_RETRO_BEAM_END_1 = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_gamma_lbeam_end_1.png");
    private static final ResourceLocation TEXTURE_RETRO_BEAM_END_2 = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_gamma_lbeam_end_2.png");
    
    private static final ResourceLocation TEXTURE_TECTONIC = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_tectonic.png");
    private static final ResourceLocation TEXTURE_TECTONIC_BEAM_INNER = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_tectonic_beam_inner.png");
    private static final ResourceLocation TEXTURE_TECTONIC_BEAM_OUTER = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_tectonic_beam_outer.png");
    private static final ResourceLocation TEXTURE_TECTONIC_BEAM_END_0 = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_tectonic_beam_end_0.png");
    private static final ResourceLocation TEXTURE_TECTONIC_BEAM_END_1 = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_tectonic_beam_end_1.png");
    private static final ResourceLocation TEXTURE_TECTONIC_BEAM_END_2 = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_tectonic_beam_end_2.png");

    private static final ResourceLocation TEXTURE_GAMMA = new ResourceLocation("alexscavesexemplified:textures/entity/tremorzilla/tremorzilla_gamma.png");
    private static final ResourceLocation TEXTURE_GAMMA_BEAM_INNER = new ResourceLocation("alexscavesexemplified:textures/entity/tremorzilla/tremorzilla_gamma_beam_inner.png");
    private static final ResourceLocation TEXTURE_GAMMA_BEAM_OUTER = new ResourceLocation("alexscavesexemplified:textures/entity/tremorzilla/tremorzilla_gamma_beam_outer.png");
    private static final ResourceLocation TEXTURE_GAMMA_BEAM_END_0 = new ResourceLocation("alexscavesexemplified:textures/entity/tremorzilla/tremorzilla_gamma_beam_end_0.png");
    private static final ResourceLocation TEXTURE_GAMMA_BEAM_END_1 = new ResourceLocation("alexscavesexemplified:textures/entity/tremorzilla/tremorzilla_gamma_beam_end_1.png");
    private static final ResourceLocation TEXTURE_GAMMA_BEAM_END_2 = new ResourceLocation("alexscavesexemplified:textures/entity/tremorzilla/tremorzilla_gamma_beam_end_2.png");

    public ACETremorzillaRenderMixin(EntityRendererProvider.Context pContext, TremorzillaModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    @Override
    public ResourceLocation getTextureLocation(TremorzillaEntity entity) {
        Gammafied myAccessor = (Gammafied) entity;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA : entity.getAltSkin() == 2 ? TEXTURE_TECTONIC : (entity.getAltSkin() == 1 ? TEXTURE_RETRO : TEXTURE);
    }

    @Override
    protected void scale(TremorzillaEntity mob, PoseStack matrixStackIn, float partialTicks) {
        float scale = ((Gammafied)mob).isGamma() ? 1.5F : 1F;
        matrixStackIn.scale(scale,scale,scale);
    }


    @ModifyExpressionValue(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/model/TremorzillaModel;getMouthPosition(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;"),remap = false)
    private Vec3 alexsCavesExempified$render1(Vec3 value, @Local TremorzillaEntity tremorzilla) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return myAccessor.isGamma() ? value.multiply(1.5,1.5,1.5) : value;
    }

    @Inject(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/render/entity/TremorzillaRenderer;renderBeam(Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;FFFZZ)V",ordinal = 0),remap = false)
    private void alexsCavesExempified$renderBeam(TremorzillaEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource source, int packedLight, CallbackInfo ci) {
        Gammafied myAccessor = (Gammafied) entity;
        if(myAccessor.isGamma()){
            poseStack.scale(2F, 2F, 2F);
        }
    }

    @ModifyVariable(method = "renderBeam", at = @At(value = "STORE"), ordinal = 0,remap = false)
    private ResourceLocation beam1(ResourceLocation value, @Local TremorzillaEntity tremorzilla) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA_BEAM_INNER : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_BEAM_INNER : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_BEAM_INNER : TEXTURE_BEAM_INNER);
    }


    @ModifyVariable(method = "renderBeam", at = @At(value = "STORE",ordinal = 1),remap = false)
    private ResourceLocation beam2(ResourceLocation original, @Local TremorzillaEntity tremorzilla) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA_BEAM_OUTER : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_BEAM_OUTER : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_BEAM_OUTER : TEXTURE_BEAM_OUTER);
    }

    @ModifyReturnValue(method = "getEndBeamTexture", at = @At("RETURN"),remap = false)
    private ResourceLocation beam3(ResourceLocation original, @Local TremorzillaEntity tremorzilla) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA_BEAM_END_0 : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_BEAM_END_0 : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_BEAM_END_0 : TEXTURE_BEAM_END_0);
    }

    @ModifyReturnValue(method = "getEndBeamTexture", at = @At("RETURN"),remap = false)
    private ResourceLocation beam4(ResourceLocation original, @Local TremorzillaEntity tremorzilla) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA_BEAM_END_1 : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_BEAM_END_1 : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_BEAM_END_1 : TEXTURE_BEAM_END_1);
    }

    @ModifyReturnValue(method = "getEndBeamTexture", at = @At("RETURN"),remap = false)
    private ResourceLocation beam5(ResourceLocation original, @Local TremorzillaEntity tremorzilla) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA_BEAM_END_2 : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_BEAM_END_2 : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_BEAM_END_2 : TEXTURE_BEAM_END_2);
    }

    @ModifyReturnValue(method = "getEndBeamTexture", at = @At("RETURN"),remap = false)
    private ResourceLocation beam6(ResourceLocation original, @Local TremorzillaEntity tremorzilla) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA_BEAM_END_0 : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_BEAM_END_0 : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_BEAM_END_0 : TEXTURE_BEAM_END_0);
    }




}
