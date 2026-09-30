package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nucleeper;

import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import net.minecraft.util.FastColor;
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
import net.neoforged.neoforge.client.NeoForgeRenderTypes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "com.github.alexmodguy.alexscaves.client.render.entity.NucleeperRenderer$LayerGlow")
public abstract class ACExNucleeperRenderRenderMixin extends RenderLayer<NucleeperEntity, NucleeperModel> {




    private static final ResourceLocation TEXTURE_EXPLODE = ResourceLocation.parse("alexscaves:textures/entity/nucleeper/nucleeper_explode.png");
    private static final ResourceLocation TEXTURE_BROKEN_GLASS = ResourceLocation.parse("alexscavesexemplified:textures/entity/nucleeper/broken_glass.png");

    private static final ResourceLocation TEXTURE_GLOW = ResourceLocation.parse("alexscaves:textures/entity/nucleeper/nucleeper_glow.png");

    private static final ResourceLocation TEXTURE_GLASS = ResourceLocation.parse("alexscaves:textures/entity/nucleeper/nucleeper_glass.png");

    private static final ResourceLocation TEXTURE_BUTTONS_0 = ResourceLocation.parse("alexscaves:textures/entity/nucleeper/nucleeper_buttons_0.png");
    private static final ResourceLocation TEXTURE_BUTTONS_1 = ResourceLocation.parse("alexscaves:textures/entity/nucleeper/nucleeper_buttons_1.png");
    private static final ResourceLocation TEXTURE_BUTTONS_2 = ResourceLocation.parse("alexscaves:textures/entity/nucleeper/nucleeper_buttons_2.png");
    
    public ACExNucleeperRenderRenderMixin(RenderLayerParent<NucleeperEntity, NucleeperModel> pRenderer) {
        super(pRenderer);
    }


    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, NucleeperEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float alpha = (float)((double)1.0F + Math.sin((double)(ageInTicks * 0.3F))) * 0.25F + 0.5F;
        float explodeProgress = entitylivingbaseIn.getExplodeProgress(partialTicks);
        VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(TEXTURE_GLOW));
        ((NucleeperModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder1, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), FastColor.ARGB32.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F));
        VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(NeoForgeRenderTypes.getUnlitTranslucent(TEXTURE_GLASS));
        ((NucleeperModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder2, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), -1);
        int buttonDiv = entitylivingbaseIn.tickCount / 5 % 6;
        if (entitylivingbaseIn.isCharged()) {
            buttonDiv = entitylivingbaseIn.tickCount / 2 % 6;
        }

        ResourceLocation buttons;
        if (!entitylivingbaseIn.getData(ACExAttachments.DEFUSED)){
            if (buttonDiv < 2) {
                buttons = TEXTURE_BUTTONS_0;
            } else if (buttonDiv < 4) {
                buttons = TEXTURE_BUTTONS_1;
            } else {
                buttons = TEXTURE_BUTTONS_2;
            }
            VertexConsumer ivertexbuilder3 = bufferIn.getBuffer(RenderType.eyes(buttons));
            ((NucleeperModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder3, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), -1);

        }
        VertexConsumer ivertexbuilder4 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(TEXTURE_EXPLODE));
        ((NucleeperModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder4, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), FastColor.ARGB32.colorFromFloat(explodeProgress, 1.0F, 1.0F, 1.0F));
    }



}
