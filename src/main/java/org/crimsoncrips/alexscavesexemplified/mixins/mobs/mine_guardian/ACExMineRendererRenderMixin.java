package org.crimsoncrips.alexscavesexemplified.mixins.mobs.mine_guardian;

import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import net.minecraft.util.FastColor;
import com.github.alexmodguy.alexscaves.client.model.MineGuardianModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "com.github.alexmodguy.alexscaves.client.render.entity.MineGuardianRenderer$LayerGlow")
public abstract class ACExMineRendererRenderMixin extends RenderLayer<MineGuardianEntity, MineGuardianModel> {

    public ACExMineRendererRenderMixin(RenderLayerParent<MineGuardianEntity, MineGuardianModel> pRenderer) {
        super(pRenderer);
    }

    private static final ResourceLocation TEXTURE_EYE = ResourceLocation.parse("alexscaves:textures/entity/mine_guardian_eye.png");
    private static final ResourceLocation TEXTURE_EXPLODE = ResourceLocation.parse("alexscaves:textures/entity/mine_guardian_explode.png");
    private static final ResourceLocation TEXTURE_NUCLEAR_EYE = ResourceLocation.parse("alexscavesexemplified:textures/entity/mine_guardian/nuclear_guardian_eye.png");
    private static final ResourceLocation TEXTURE_NUCLEAR_EXPLODE = ResourceLocation.parse("alexscavesexemplified:textures/entity/mine_guardian/nuclear_guardian_explode.png");
    private static final ResourceLocation TEXTURE_NUCLEAR_AE = ResourceLocation.parse("alexscavesexemplified:textures/entity/mine_guardian/nuclear_guardian_glow.png");

    private static final ResourceLocation TEXTURE_OWNED = ResourceLocation.parse("alexscavesexemplified:textures/entity/mine_guardian/texture_owned_eye.png");

    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, MineGuardianEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        float explodeProgress = entitylivingbaseIn.getExplodeProgress(partialTicks);
        if (!entitylivingbaseIn.isEyeClosed() && entitylivingbaseIn.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) != -1) {
            VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(RenderType.eyes(Minecraft.getInstance().player.getStringUUID().equals(entitylivingbaseIn.getData(ACExAttachments.MINE_GUARDIAN_OWNER)) ? TEXTURE_OWNED : entitylivingbaseIn.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) > 0 ? TEXTURE_NUCLEAR_EYE : TEXTURE_EYE));
            ((MineGuardianModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder1, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), -1);
        }

        VertexConsumer ivertexbuilder4 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(entitylivingbaseIn.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) > 0 ? TEXTURE_NUCLEAR_EXPLODE : TEXTURE_EXPLODE));
        ((MineGuardianModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder4, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), FastColor.ARGB32.colorFromFloat(explodeProgress, 1.0F, 1.0F, 1.0F));


        if (entitylivingbaseIn.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) == 1) {
            VertexConsumer nuclearGlowing = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(TEXTURE_NUCLEAR_AE));
            ((MineGuardianModel)this.getParentModel()).renderToBuffer(poseStack, nuclearGlowing, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), FastColor.ARGB32.colorFromFloat(Math.min(1.0F, 0.4F + explodeProgress), 1.0F, 1.0F, 1.0F));
        }



    }







}
