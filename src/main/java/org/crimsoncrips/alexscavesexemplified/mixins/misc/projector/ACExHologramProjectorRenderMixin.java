package org.crimsoncrips.alexscavesexemplified.mixins.misc.projector;

import com.github.alexmodguy.alexscaves.client.render.blockentity.HologramProjectorBlockRenderer;
import com.github.alexmodguy.alexscaves.server.block.blockentity.HologramProjectorBlockEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACExBaseInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(HologramProjectorBlockRenderer.class)
public abstract class ACExHologramProjectorRenderMixin<T extends HologramProjectorBlockEntity> implements BlockEntityRenderer<T> {


    @ModifyExpressionValue(method = "renderAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getBbWidth()F"))
    private static float alexsMobsInteraction$renderAt1(float original, @Local HologramProjectorBlockEntity projectorBlockEntity) {
        ACExBaseInterface accesor = (ACExBaseInterface)projectorBlockEntity;

        if(AlexsCavesExemplified.COMMON_CONFIG.SCALABLE_HOLOGRAM_ENABLED.get()){
            return original + ((float) accesor.getProjectionScale() / 2);
        }
        return original;
    }

    @ModifyVariable(method = "renderAt", at = @At(value = "STORE"),ordinal = 5,remap = false)
    private static float alexsMobsInteraction$renderAt2(float value, @Local HologramProjectorBlockEntity projectorBlockEntity) {
        ACExBaseInterface accesor = (ACExBaseInterface)projectorBlockEntity;
        return value + ((float) accesor.getProjectionScale() / 2);
    }

    @Inject(method = "renderAt", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
    private static void alexsMobsInteraction$renderAt3(HologramProjectorBlockEntity projectorBlockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, CallbackInfo ci) {
        ACExBaseInterface accesor = (ACExBaseInterface)projectorBlockEntity;
        int scale =  accesor.getProjectionScale();
        poseStack.scale(scale, (scale), scale);
        poseStack.translate(0,-((scale - 1) * 0.5) ,0);
    }

    @Override
    public boolean shouldRender(T pBlockEntity, Vec3 pCameraPos) {
        ACExBaseInterface accesor = (ACExBaseInterface)pBlockEntity;
        return Vec3.atCenterOf(pBlockEntity.getBlockPos()).closerThan(pCameraPos, 128 * (accesor.getProjectionScale() > 0 ? accesor.getProjectionScale() : 1));
    }
}
