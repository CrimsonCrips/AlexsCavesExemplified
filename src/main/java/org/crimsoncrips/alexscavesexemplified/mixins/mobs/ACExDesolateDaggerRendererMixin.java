package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.client.render.entity.DesolateDaggerRenderer;
import com.github.alexmodguy.alexscaves.server.entity.item.DesolateDaggerEntity;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.Mth;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSectumsempra;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DesolateDaggerRenderer.class)
public abstract class ACExDesolateDaggerRendererMixin {

    @Unique
    private static final float FADE_TICKS = 10.0F;

    @ModifyArg(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/item/DesolateDaggerEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;", ordinal = 0))
    private float alexsCavesExemplified$render(float yaw, @Local(argsOnly = true) DesolateDaggerEntity dagger, @Local(argsOnly = true, ordinal = 1) float partialTicks) {
        if (!ACExSectumsempra.isSectumsempraDagger(dagger))
            return yaw;
        return yaw + ACExSectumsempra.SETTLE_SPIN_DEGREES * (1.0F - ACExSectumsempra.settle(dagger, partialTicks));
    }

    @ModifyArg(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/item/DesolateDaggerEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/render/entity/DesolateDaggerRenderer;renderModel(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lcom/mojang/blaze3d/vertex/VertexConsumer;FLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/resources/model/BakedModel;FFFIILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V"), index = 2, remap = false)
    private float alexsCavesExemplified$render1(float alpha, @Local(argsOnly = true) DesolateDaggerEntity dagger, @Local(argsOnly = true, ordinal = 1) float partialTicks) {
        if (!ACExSectumsempra.isSectumsempraDagger(dagger))
            return alpha;
        return alpha * Mth.clamp((dagger.tickCount + partialTicks) / FADE_TICKS, 0.0F, 1.0F);
    }
}
