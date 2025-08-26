package org.crimsoncrips.alexscavesexemplified.mixins.mobs.mine_guardian;

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
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "com.github.alexmodguy.alexscaves.client.render.entity.MineGuardianRenderer$LayerGlow")
public abstract class ACExMineRendererRenderMixin extends RenderLayer<MineGuardianEntity, MineGuardianModel> {

    public ACExMineRendererRenderMixin(RenderLayerParent<MineGuardianEntity, MineGuardianModel> pRenderer) {
        super(pRenderer);
    }

    private static final ResourceLocation TEXTURE_EYE = new ResourceLocation("alexscaves:textures/entity/mine_guardian_eye.png");
    private static final ResourceLocation TEXTURE_EXPLODE = new ResourceLocation("alexscaves:textures/entity/mine_guardian_explode.png");
    private static final ResourceLocation TEXTURE_NUCLEAR_EYE = new ResourceLocation("alexscavesexemplified:textures/entity/mine_guardian/nuclear_guardian_eye.png");
    private static final ResourceLocation TEXTURE_NUCLEAR_EXPLODE = new ResourceLocation("alexscavesexemplified:textures/entity/mine_guardian/nuclear_guardian_explode.png");
    private static final ResourceLocation TEXTURE_NUCLEAR_AE = new ResourceLocation("alexscavesexemplified:textures/entity/mine_guardian/nuclear_guardian_glow.png");

    private static final ResourceLocation TEXTURE_OWNED = new ResourceLocation("alexscavesexemplified:textures/entity/mine_guardian/texture_owned_eye.png");

    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, MineGuardianEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        MineGuardianXtra accesor = (MineGuardianXtra) entitylivingbaseIn;

        float explodeProgress = entitylivingbaseIn.getExplodeProgress(partialTicks);
        if (!entitylivingbaseIn.isEyeClosed() && accesor.alexsCavesExemplified$getVariant() != -1) {
            VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(RenderType.eyes(Minecraft.getInstance().player.getStringUUID().equals(accesor.alexsCavesExemplified$getOwner()) ? TEXTURE_OWNED : accesor.alexsCavesExemplified$getVariant() > 0 ? TEXTURE_NUCLEAR_EYE : TEXTURE_EYE));
            ((MineGuardianModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder1, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }

        VertexConsumer ivertexbuilder4 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(accesor.alexsCavesExemplified$getVariant() > 0 ? TEXTURE_NUCLEAR_EXPLODE : TEXTURE_EXPLODE));
        ((MineGuardianModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder4, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, explodeProgress);


        if (accesor.alexsCavesExemplified$getVariant() == 1) {
            VertexConsumer nuclearGlowing = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(TEXTURE_NUCLEAR_AE));
            ((MineGuardianModel)this.getParentModel()).renderToBuffer(poseStack, nuclearGlowing, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 0.4F + explodeProgress);
        }



    }







}
