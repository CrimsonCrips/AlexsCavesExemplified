package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import net.minecraft.util.FastColor;
import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.model.TremorzillaModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(targets = "com.github.alexmodguy.alexscaves.client.render.entity.TremorzillaRenderer$LayerGlow")
public abstract class ACExTremorzillaRenderGlowMixin extends RenderLayer<TremorzillaEntity, TremorzillaModel> {

    private static final ResourceLocation TEXTURE_GLOW = ResourceLocation.parse("alexscaves:textures/entity/tremorzilla/tremorzilla_glow.png");
    private static final ResourceLocation TEXTURE_GLOW_POWERED = ResourceLocation.parse("alexscaves:textures/entity/tremorzilla/tremorzilla_glow_powered.png");

    private static final ResourceLocation TEXTURE_RETRO_GLOW = ResourceLocation.parse("alexscaves:textures/entity/tremorzilla/tremorzilla_retro_glow.png");
    private static final ResourceLocation TEXTURE_RETRO_GLOW_POWERED = ResourceLocation.parse("alexscaves:textures/entity/tremorzilla/tremorzilla_retro_glow_powered.png");

    private static final ResourceLocation TEXTURE_TECTONIC_GLOW = ResourceLocation.parse("alexscaves:textures/entity/tremorzilla/tremorzilla_tectonic_glow.png");
    private static final ResourceLocation TEXTURE_TECTONIC_GLOW_POWERED = ResourceLocation.parse("alexscaves:textures/entity/tremorzilla/tremorzilla_tectonic_glow_powered.png");

    private static final ResourceLocation TEXTURE_GAMMA_GLOW = ResourceLocation.parse("alexscavesexemplified:textures/entity/tremorzilla/tremorzilla_gamma_glow.png");
    private static final ResourceLocation TEXTURE_GAMMA_GLOW_POWERED = ResourceLocation.parse("alexscavesexemplified:textures/entity/tremorzilla/tremorzilla_gamma_glow_powered.png");

    public ACExTremorzillaRenderGlowMixin(RenderLayerParent<TremorzillaEntity, TremorzillaModel> pRenderer) {
        super(pRenderer);
    }






    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, TremorzillaEntity tremorzilla, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float normalAlpha = (float)Math.sin((double)(ageInTicks * 0.2F)) * 0.15F + 0.5F;
        float spikeDownAmount = tremorzilla.getClientSpikeDownAmount(partialTicks);
        VertexConsumer normalGlowConsumer = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(tremorzilla.isPowered() ? AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && tremorzilla.getData(ACExAttachments.GAMMA) ? TEXTURE_GAMMA_GLOW_POWERED :tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_GLOW_POWERED : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_GLOW_POWERED : TEXTURE_GLOW_POWERED) : (AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && tremorzilla.getData(ACExAttachments.GAMMA) ? TEXTURE_GAMMA_GLOW : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_GLOW : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_GLOW : TEXTURE_GLOW))));
        ((TremorzillaModel)this.getParentModel()).renderToBuffer(matrixStackIn, normalGlowConsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(tremorzilla, 0.0F), FastColor.ARGB32.colorFromFloat(normalAlpha, 1.0F, 1.0F, 1.0F));
        if (spikeDownAmount > 0.0F) {
            VertexConsumer spikeGlowConsumer;
            if ((Boolean) AlexsCaves.CLIENT_CONFIG.radiationGlowEffect.get()) {
                PostEffectRegistry.renderEffectForNextTick(ClientProxy.IRRADIATED_SHADER);
                spikeGlowConsumer = bufferIn.getBuffer(ACRenderTypes.getTremorzillaBeam(AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && tremorzilla.getData(ACExAttachments.GAMMA) ? TEXTURE_GAMMA_GLOW_POWERED : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_GLOW_POWERED : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_GLOW_POWERED : TEXTURE_GLOW_POWERED), true));
            } else {
                spikeGlowConsumer = normalGlowConsumer;
            }

            ((TremorzillaModel)this.getParentModel()).showSpikesBasedOnProgress(spikeDownAmount, 0.0F);
            ((TremorzillaModel)this.getParentModel()).renderToBuffer(matrixStackIn, spikeGlowConsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(tremorzilla, 0.0F), -1);
            ((TremorzillaModel)this.getParentModel()).showAllSpikes();
        }

    }





}
