package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tesla_bulb;


import org.crimsoncrips.alexscavesexemplified.backport.ACExRenderCompat;
import org.spongepowered.asm.mixin.Unique;
import org.crimsoncrips.alexscavesexemplified.misc.ACExTeslaShock;

import net.minecraft.util.FastColor;
import com.github.alexmodguy.alexscaves.client.model.TeslaBulbModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.blockentity.TelsaBulbBlockRenderer;
import com.github.alexmodguy.alexscaves.server.block.blockentity.TeslaBulbBlockEntity;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TelsaBulbBlockRenderer.class)
public abstract class ACExTeslaBulbRenderMixin<T extends TeslaBulbBlockEntity> implements BlockEntityRenderer<T> {

    @Unique
    private static final TeslaBulbModel MODEL = new TeslaBulbModel();
    @Unique
    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves", "textures/entity/tesla_bulb.png");
    @Unique
    private static final ResourceLocation TEXTURE_CHARGE_1 = new ResourceLocation("alexscavesexemplified", "textures/entity/tesla/tesla_charge_1.png");
    @Unique
    private static final ResourceLocation TEXTURE_CHARGE_2 = new ResourceLocation("alexscavesexemplified", "textures/entity/tesla/tesla_charge_2.png");
    @Unique
    private static final ResourceLocation TEXTURE_CHARGE_3 = new ResourceLocation("alexscavesexemplified", "textures/entity/tesla/tesla_charge_3.png");
    @Unique
    private static final ResourceLocation TEXTURE_CHARGE_4 = new ResourceLocation("alexscavesexemplified", "textures/entity/tesla/tesla_charge_4.png");


    @Inject(method = "render(Lcom/github/alexmodguy/alexscaves/server/block/blockentity/TeslaBulbBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/model/TeslaBulbModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"), cancellable = true)
    private void alexsCavesExemplified$render(T teslaBulb, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, CallbackInfo ci, @Local(ordinal = 1) float r, @Local(ordinal = 2) float g, @Local(ordinal = 3) float b) {
        if(AlexsCavesExemplified.COMMON_CONFIG.SHOCKING_THERAPY_ENABLED.get()){
            ci.cancel();
            MODEL.setupAnim(null, 0.0F, teslaBulb.getExplodeProgress(partialTicks), (float)teslaBulb.age + partialTicks, 0.0F, 0.0F);
            ResourceLocation texture = switch (ACExTeslaShock.glowStage(teslaBulb, teslaBulb.getLevel())) {
                case 4 -> TEXTURE_CHARGE_4;
                case 3 -> TEXTURE_CHARGE_3;
                case 2 -> TEXTURE_CHARGE_2;
                case 1 -> TEXTURE_CHARGE_1;
                default -> TEXTURE;
            };
            ACExRenderCompat.renderToBuffer(MODEL, poseStack, bufferIn.getBuffer(ACRenderTypes.getTeslaBulb(texture)), combinedLightIn, combinedOverlayIn, ACExRenderCompat.colorFromFloat(1.0F, r, g, b));
            poseStack.popPose();
        }
    }

    @Inject(method = "render(Lcom/github/alexmodguy/alexscaves/server/block/blockentity/TeslaBulbBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "HEAD"),remap = false)
    private void alexsCavesExemplified$render1(T teslaBulb, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, CallbackInfo ci) {
        if(AlexsCavesExemplified.COMMON_CONFIG.SHOCKING_THERAPY_ENABLED.get()){
            poseStack.clear();

        }
    }






}
