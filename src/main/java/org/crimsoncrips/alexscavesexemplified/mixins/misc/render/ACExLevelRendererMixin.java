package org.crimsoncrips.alexscavesexemplified.mixins.misc.render;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.PostChain;
import org.crimsoncrips.alexscavesexemplified.client.monolith.ACExDarkMonolithGlow;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public abstract class ACExLevelRendererMixin {

    @Shadow
    @Nullable
    private PostChain entityEffect;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private boolean outlineProcessed;

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void alexsCavesExemplified$renderLevelHead(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        this.outlineProcessed = false;
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OutlineBufferSource;endOutlineBatch()V", shift = At.Shift.AFTER))
    private void alexsCavesExemplified$renderLevel(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        ACExDarkMonolithGlow.drawDeferred();
        if (ACExDarkMonolithGlow.consumeOutlineRequest() && this.entityEffect != null) {
            this.entityEffect.process(partialTick);
            this.minecraft.getMainRenderTarget().bindWrite(false);
            this.outlineProcessed = true;
        }
    }

    @WrapWithCondition(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;process(F)V", ordinal = 0))
    private boolean alexsCavesExemplified$renderLevelOutline(PostChain chain, float partialTick) {
        return !this.outlineProcessed;
    }
}
