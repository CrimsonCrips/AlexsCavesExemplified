package org.crimsoncrips.alexscavesexemplified.mixins.mobs.gumbeeper;

import com.github.alexmodguy.alexscaves.client.model.GumbeeperModel;
import com.github.alexmodguy.alexscaves.client.render.entity.GumbeeperRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.GumbeeperEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(GumbeeperRenderer.class)
public abstract class ACExGumbeeperRenderMixin extends MobRenderer<GumbeeperEntity, GumbeeperModel> {


    public ACExGumbeeperRenderMixin(EntityRendererProvider.Context pContext, GumbeeperModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    @Override
    public void render(GumbeeperEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int pPackedLight) {

        float cameraY = Minecraft.getInstance().getEntityRenderDispatcher().camera.getYRot();
        Component text = Component.nullToEmpty("Test");

        poseStack.pushPose();
        poseStack.translate(0F, 2, 0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - cameraY));
        poseStack.mulPose(Axis.XP.rotationDegrees(0));
        poseStack.scale(0.02F, 0.02F, 0.02F);
        float f = (float)(-Minecraft.getInstance().font.width(text) / 2);
        Minecraft.getInstance().font.drawInBatch8xOutline(text.getVisualOrderText(), f, 0.0F, FastColor.ARGB32.color(Mth.clamp((int) (1 * 255), 4, 255), 255, 255, 255), 0, poseStack.last().pose(), bufferIn, 240);
        poseStack.popPose();

        super.render(pEntity, pEntityYaw, pPartialTicks, poseStack, bufferIn, pPackedLight);

    }
}
