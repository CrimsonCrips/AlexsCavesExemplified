package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nucleeper;

import com.github.alexmodguy.alexscaves.client.model.NucleeperModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeRenderTypes;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.NucleeperXtra;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "com.github.alexmodguy.alexscaves.client.render.entity.NucleeperRenderer$LayerGlow")
public abstract class ACENucleeperRenderRenderMixin extends RenderLayer<NucleeperEntity, NucleeperModel> {




    private static final ResourceLocation TEXTURE_EXPLODE = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_explode.png");
    private static final ResourceLocation TEXTURE_BROKEN_GLASS = new ResourceLocation("alexscavesexemplified:textures/entity/nucleeper/broken_glass.png");

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_glow.png");
    private static final ResourceLocation TEXTURE_RUSTED_GLOW = new ResourceLocation("alexscavesexemplified:textures/entity/nucleeper/nucleeper_rusted_glow.png");

    private static final ResourceLocation TEXTURE_GLASS = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_glass.png");

    private static final ResourceLocation TEXTURE_BUTTONS_0 = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_buttons_0.png");
    private static final ResourceLocation TEXTURE_BUTTONS_1 = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_buttons_1.png");
    private static final ResourceLocation TEXTURE_BUTTONS_2 = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_buttons_2.png");
    
    public ACENucleeperRenderRenderMixin(RenderLayerParent<NucleeperEntity, NucleeperModel> pRenderer) {
        super(pRenderer);
    }


    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, NucleeperEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float alpha = (float)((double)1.0F + Math.sin((double)(ageInTicks * 0.3F))) * 0.25F + 0.5F;
        float explodeProgress = entitylivingbaseIn.getExplodeProgress(partialTicks);
        VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(((NucleeperXtra)entitylivingbaseIn).isRusted() ? TEXTURE_RUSTED_GLOW : TEXTURE_GLOW));
        ((NucleeperModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder1, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
        VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(ForgeRenderTypes.getUnlitTranslucent(((NucleeperXtra)entitylivingbaseIn).isRusted() ? TEXTURE_BROKEN_GLASS : TEXTURE_GLASS));
        ((NucleeperModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder2, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        int buttonDiv = entitylivingbaseIn.tickCount / 5 % 6;
        if (entitylivingbaseIn.isCharged()) {
            buttonDiv = entitylivingbaseIn.tickCount / 2 % 6;
        }

        ResourceLocation buttons;
        if (!((NucleeperXtra)entitylivingbaseIn).isDefused()){
            if (buttonDiv < 2) {
                buttons = TEXTURE_BUTTONS_0;
            } else if (buttonDiv < 4) {
                buttons = TEXTURE_BUTTONS_1;
            } else {
                buttons = TEXTURE_BUTTONS_2;
            }
            VertexConsumer ivertexbuilder3 = bufferIn.getBuffer(RenderType.eyes(buttons));
            ((NucleeperModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder3, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);

        }
        VertexConsumer ivertexbuilder4 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(TEXTURE_EXPLODE));
        ((NucleeperModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder4, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, explodeProgress);
    }



}
