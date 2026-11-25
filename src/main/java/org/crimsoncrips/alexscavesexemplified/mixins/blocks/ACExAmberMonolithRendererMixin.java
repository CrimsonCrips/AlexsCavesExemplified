package org.crimsoncrips.alexscavesexemplified.mixins.blocks;


import org.crimsoncrips.alexscavesexemplified.backport.ACExRenderCompat;
import com.github.alexmodguy.alexscaves.client.render.blockentity.AmberMonolithBlockRenderer;
import com.github.alexmodguy.alexscaves.server.block.blockentity.AmberMonolithBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.crimsoncrips.alexscavesexemplified.client.monolith.ACExDarkMonolithGlow;
import org.crimsoncrips.alexscavesexemplified.misc.ACExAmberMonolith;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AmberMonolithBlockRenderer.class)
public abstract class ACExAmberMonolithRendererMixin {

    @Unique
    private static BlockPos renderingDark;

    @WrapOperation(method = "render(Lcom/github/alexmodguy/alexscaves/server/block/blockentity/AmberMonolithBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/render/blockentity/AmberMonolithBlockRenderer;renderEntityInAmber(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;F)V"), remap = false)
    private void alexsCavesExemplified$render(Entity entity, double x, double y, double z, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, float transparency, Operation<Void> original, @Local(argsOnly = true) AmberMonolithBlockEntity monolith) {
        renderingDark = ACExAmberMonolith.isDark(monolith) ? monolith.getBlockPos() : null;
        try {
            original.call(entity, x, y, z, yaw, partialTicks, poseStack, buffer, transparency);
        } finally {
            renderingDark = null;
        }
    }

    @WrapOperation(method = "renderEntityInAmber", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V", remap = true), remap = false)
    private static void alexsCavesExemplified$renderEntityInAmber(EntityModel<?> model, PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha, Operation<Void> original, @Local(argsOnly = true) Entity entity, @Local(argsOnly = true) MultiBufferSource buffer) {
        if (renderingDark == null) {
            original.call(model, poseStack, consumer, light, overlay, red, green, blue, alpha);
            return;
        }
        original.call(model, poseStack, consumer, light, overlay, red, green, blue, alpha);
        ResourceLocation texture = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).getTextureLocation(entity);
        ACExDarkMonolithGlow.render(model, poseStack, renderingDark, texture, light, overlay, ACExRenderCompat.colorFromFloat(alpha, red, green, blue));
    }
}
