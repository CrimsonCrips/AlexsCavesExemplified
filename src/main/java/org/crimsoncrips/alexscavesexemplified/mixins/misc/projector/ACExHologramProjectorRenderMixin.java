package org.crimsoncrips.alexscavesexemplified.mixins.misc.projector;

import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import com.github.alexmodguy.alexscaves.client.render.blockentity.HologramProjectorBlockRenderer;
import com.github.alexmodguy.alexscaves.server.block.blockentity.HologramProjectorBlockEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(HologramProjectorBlockRenderer.class)
public abstract class ACExHologramProjectorRenderMixin<T extends HologramProjectorBlockEntity> implements BlockEntityRenderer<T> {


    @ModifyExpressionValue(method = "renderAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getBbWidth()F"))
    private static float alexsMobsInteraction$renderAt1(float original, @Local HologramProjectorBlockEntity projectorBlockEntity) {
        if(AlexsCavesExemplified.COMMON_CONFIG.SCALABLE_HOLOGRAM_ENABLED.get()){
            return original + ((float) projectorBlockEntity.getData(ACExAttachments.PROJECTION_SCALE) / 2);
        }
        return original;
    }

    @ModifyVariable(method = "renderAt", at = @At(value = "STORE"),ordinal = 5,remap = false)
    private static float alexsMobsInteraction$renderAt2(float value, @Local HologramProjectorBlockEntity projectorBlockEntity) {
        return value + ((float) projectorBlockEntity.getData(ACExAttachments.PROJECTION_SCALE) / 2);
    }

    @Inject(method = "renderAt", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
    private static void alexsMobsInteraction$renderAt3(HologramProjectorBlockEntity projectorBlockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, CallbackInfo ci) {
        int scale = projectorBlockEntity.getData(ACExAttachments.PROJECTION_SCALE);
        poseStack.scale(scale, (scale), scale);
        poseStack.translate(0,-((scale - 1) * 0.5) ,0);
    }

    @Override
    public boolean shouldRender(T pBlockEntity, Vec3 pCameraPos) {
        int scale = pBlockEntity.getData(ACExAttachments.PROJECTION_SCALE);
        return Vec3.atCenterOf(pBlockEntity.getBlockPos()).closerThan(pCameraPos, 128 * (scale > 0 ? scale : 1));
    }
}
