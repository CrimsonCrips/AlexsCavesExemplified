package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.tooltip.ACExGammaText;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.client.render.ACExGlowVertexConsumer;
import org.crimsoncrips.alexscavesexemplified.client.render.ACExRenderTypes;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExDataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ACExItemRendererMixin {

    @Shadow public abstract void render(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model);

    @Unique
    private static boolean alexsCavesExemplified$glowing = false;

    @Unique
    private static final int GUI_GLOW_RINGS = 3;
    @Unique
    private static final int GUI_GLOW_SAMPLES = 8;
    @Unique
    private static final float GUI_GLOW_SPACING = 0.75F;
    @Unique
    private static final float GUI_GLOW_ALPHA = 0.14F;

    @Inject(method = "render", at = @At("TAIL"))
    private void alexsCavesExemplified$render(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        if (alexsCavesExemplified$glowing || displayContext.firstPerson())
            return;
        boolean gamma = ACExGammaText.isGammaBomb(itemStack) && AlexsCavesExemplified.CLIENT_CONFIG.GAMMA_BOMB_GLOW_ENABLED.get();
        if (!gamma && !(itemStack.has(ACExDataComponents.IRRADIATED) && AlexsCaves.CLIENT_CONFIG.radiationGlowEffect.get()))
            return;
        alexsCavesExemplified$glowing = true;
        try {
            if (displayContext == ItemDisplayContext.GUI) {
                VertexConsumer buffer = bufferSource.getBuffer(gamma ? ACExRenderTypes.getGammaItem() : ACExRenderTypes.getIrradiatedItem());
                for (int ring = 1; ring <= GUI_GLOW_RINGS; ring++) {
                    float radius = ring * GUI_GLOW_SPACING / 16.0F;
                    float alpha = GUI_GLOW_ALPHA * (1.0F - (ring - 1) / (float) GUI_GLOW_RINGS);
                    for (int i = 0; i < GUI_GLOW_SAMPLES; i++) {
                        float angle = (float) (Math.PI * 2 * i / GUI_GLOW_SAMPLES);
                        renderGlow(itemStack, displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model, Mth.cos(angle) * radius, Mth.sin(angle) * radius, -0.5F, 1.0F, alpha);
                    }
                }
                renderGlow(itemStack, displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model, 0.0F, 0.0F, 0.0F, 1.0F, 0.25F);
            } else {
                PostEffectRegistry.renderEffectForNextTick(ClientProxy.IRRADIATED_SHADER);
                VertexConsumer buffer = bufferSource.getBuffer(gamma ? ACRenderTypes.getBlueRadiationGlow(TextureAtlas.LOCATION_BLOCKS) : ACRenderTypes.getRadiationGlow(TextureAtlas.LOCATION_BLOCKS));
                renderGlow(itemStack, displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model, 0.0F, 0.0F, 0.0F, 1.0F, 0.33F);
            }
        } finally {
            alexsCavesExemplified$glowing = false;
        }
    }

    @Unique
    private void renderGlow(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, VertexConsumer buffer, int combinedLight, int combinedOverlay, BakedModel model, float x, float y, float depth, float scale, float alpha) {
        VertexConsumer glow = new ACExGlowVertexConsumer(buffer, alpha);
        poseStack.pushPose();
        poseStack.translate(x, y, depth);
        poseStack.scale(scale, scale, scale);
        render(itemStack, displayContext, leftHand, poseStack, renderType -> glow, combinedLight, combinedOverlay, model);
        poseStack.popPose();
    }
}
