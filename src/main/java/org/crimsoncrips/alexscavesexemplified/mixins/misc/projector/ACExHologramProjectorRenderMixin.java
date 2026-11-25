package org.crimsoncrips.alexscavesexemplified.mixins.misc.projector;



import org.joml.Matrix3f;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import com.github.alexmodguy.alexscaves.client.render.blockentity.HologramProjectorBlockRenderer;
import com.github.alexmodguy.alexscaves.server.block.blockentity.HologramProjectorBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;


@Mixin(HologramProjectorBlockRenderer.class)
public abstract class ACExHologramProjectorRenderMixin<T extends HologramProjectorBlockEntity> implements BlockEntityRenderer<T> {

    @Unique
    private static final float PLAYER_WIDTH = 0.6F;
    @Unique
    private static final float PLAYER_HEIGHT = 1.8F;
    @Unique
    private static final float BEAM_TOP = 1.5F;

    @Unique
    private static float getScale(HologramProjectorBlockEntity projector) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.SCALABLE_HOLOGRAM_ENABLED.get())
            return 1.0F;
        return Math.max(1, ACExData.get(projector, ACExAttachments.PROJECTION_SCALE));
    }

    @WrapOperation(method = "renderAt", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/render/blockentity/HologramProjectorBlockRenderer;shineLeftCornerVertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lorg/joml/Matrix4f;Lorg/joml/Matrix3f;FFFF)V"), remap = false)
    private static void alexsCavesExemplified$shineLeftCornerVertex(VertexConsumer consumer, Matrix4f matrix, Matrix3f normal, float length, float width, float xOffset, float yOffset, Operation<Void> original, @Local(argsOnly = true) HologramProjectorBlockEntity projector) {
        float scale = getScale(projector);
        original.call(consumer, matrix, normal, length * scale, width * scale, xOffset, yOffset);
    }

    @WrapOperation(method = "renderAt", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/render/blockentity/HologramProjectorBlockRenderer;shineRightCornerVertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lorg/joml/Matrix4f;Lorg/joml/Matrix3f;FFFF)V"), remap = false)
    private static void alexsCavesExemplified$shineRightCornerVertex(VertexConsumer consumer, Matrix4f matrix, Matrix3f normal, float length, float width, float xOffset, float yOffset, Operation<Void> original, @Local(argsOnly = true) HologramProjectorBlockEntity projector) {
        float scale = getScale(projector);
        original.call(consumer, matrix, normal, length * scale, width * scale, xOffset, yOffset);
    }

    @WrapOperation(method = "renderAt", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 2))
    private static void alexsCavesExemplified$renderAt(PoseStack poseStack, float x, float y, float z, Operation<Void> original, @Local(argsOnly = true) HologramProjectorBlockEntity projector) {
        scaleHologram(poseStack, x, y, z, original, projector);
    }

    @WrapOperation(method = "renderAt", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 3))
    private static void alexsCavesExemplified$renderAt1(PoseStack poseStack, float x, float y, float z, Operation<Void> original, @Local(argsOnly = true) HologramProjectorBlockEntity projector) {
        scaleHologram(poseStack, x, y, z, original, projector);
    }

    @Unique
    private static void scaleHologram(PoseStack poseStack, float x, float y, float z, Operation<Void> original, HologramProjectorBlockEntity projector) {
        float scale = getScale(projector);
        original.call(poseStack, x, y * scale, z);
        if (scale != 1.0F) {
            poseStack.scale(scale, scale, scale);
        }
    }

    @Override
    public boolean shouldRenderOffScreen(T projector) {
        return AlexsCavesExemplified.COMMON_CONFIG.AC_FIXES_ENABLED.get() || AlexsCavesExemplified.COMMON_CONFIG.SCALABLE_HOLOGRAM_ENABLED.get();
    }

    @Override
    public boolean shouldRender(T projector, Vec3 cameraPos) {
        return Vec3.atCenterOf(projector.getBlockPos()).closerThan(cameraPos, this.getViewDistance() * getScale(projector));
    }
}
