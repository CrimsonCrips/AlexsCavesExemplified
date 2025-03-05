package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import com.github.alexmodguy.alexscaves.client.model.TremorzillaModel;
import com.github.alexmodguy.alexscaves.client.render.entity.CustomBookEntityRenderer;
import com.github.alexmodguy.alexscaves.client.render.entity.TremorzillaRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


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

    public ResourceLocation getTextureLocation(TremorzillaEntity entity) {
        Gammafied myAccessor = (Gammafied) entity;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA : entity.getAltSkin() == 2 ? TEXTURE_TECTONIC : (entity.getAltSkin() == 1 ? TEXTURE_RETRO : TEXTURE);
    }

    private TremorzillaEntity tremorzilla;
    @Inject(method = "renderBeam", at = @At("HEAD"),remap = false)
    private void captureEntity(TremorzillaEntity entity, PoseStack poseStack, MultiBufferSource source, float partialTicks, float width, float length, boolean inner, boolean glowSecondPass, CallbackInfo ci) {
        tremorzilla = entity;
    }

    @ModifyVariable(method = "renderBeam", at = @At(value = "STORE"), ordinal = 0,remap = false)
    private ResourceLocation beam1(ResourceLocation value) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA_BEAM_INNER : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_BEAM_INNER : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_BEAM_INNER : TEXTURE_BEAM_INNER);
    }


    @ModifyVariable(method = "renderBeam", at = @At(value = "STORE",ordinal = 1),remap = false)
    private ResourceLocation beam2(ResourceLocation original) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA_BEAM_OUTER : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_BEAM_OUTER : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_BEAM_OUTER : TEXTURE_BEAM_OUTER);
    }

    @ModifyReturnValue(method = "getEndBeamTexture", at = @At("RETURN"),remap = false)
    private ResourceLocation beam3(ResourceLocation original) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA_BEAM_END_0 : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_BEAM_END_0 : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_BEAM_END_0 : TEXTURE_BEAM_END_0);
    }

    @ModifyReturnValue(method = "getEndBeamTexture", at = @At("RETURN"),remap = false)
    private ResourceLocation beam4(ResourceLocation original) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA_BEAM_END_1 : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_BEAM_END_1 : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_BEAM_END_1 : TEXTURE_BEAM_END_1);
    }

    @ModifyReturnValue(method = "getEndBeamTexture", at = @At("RETURN"),remap = false)
    private ResourceLocation beam5(ResourceLocation original) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA_BEAM_END_2 : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_BEAM_END_2 : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_BEAM_END_2 : TEXTURE_BEAM_END_2);
    }

    @ModifyReturnValue(method = "getEndBeamTexture", at = @At("RETURN"),remap = false)
    private ResourceLocation beam6(ResourceLocation original) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && myAccessor.isGamma() ? TEXTURE_GAMMA_BEAM_END_0 : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_BEAM_END_0 : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_BEAM_END_0 : TEXTURE_BEAM_END_0);
    }




}
