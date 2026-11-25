package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nucleeper;



import org.crimsoncrips.alexscavesexemplified.backport.ACExRenderCompat;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.spongepowered.asm.mixin.Unique;
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
import net.minecraftforge.client.ForgeRenderTypes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "com.github.alexmodguy.alexscaves.client.render.entity.NucleeperRenderer$LayerGlow")
public abstract class ACExNucleeperRenderRenderMixin extends RenderLayer<NucleeperEntity, NucleeperModel> {




    @Unique
    private static final ResourceLocation TEXTURE_EXPLODE = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_explode.png");
    @Unique
    private static final ResourceLocation TEXTURE_BROKEN_GLASS = new ResourceLocation("alexscavesexemplified:textures/entity/nucleeper/broken_glass.png");

    @Unique
    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_glow.png");

    @Unique
    private static final ResourceLocation TEXTURE_GLASS = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_glass.png");

    @Unique
    private static final ResourceLocation TEXTURE_BUTTONS_0 = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_buttons_0.png");
    @Unique
    private static final ResourceLocation TEXTURE_BUTTONS_1 = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_buttons_1.png");
    @Unique
    private static final ResourceLocation TEXTURE_BUTTONS_2 = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_buttons_2.png");
    
    public ACExNucleeperRenderRenderMixin(RenderLayerParent<NucleeperEntity, NucleeperModel> pRenderer) {
        super(pRenderer);
    }


    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, NucleeperEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float alpha = (float)((double)1.0F + Math.sin((double)(ageInTicks * 0.3F))) * 0.25F + 0.5F;
        float explodeProgress = entitylivingbaseIn.getExplodeProgress(partialTicks);
        VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(TEXTURE_GLOW));
        ACExRenderCompat.renderToBuffer(((NucleeperModel)this.getParentModel()), poseStack, ivertexbuilder1, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), ACExRenderCompat.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F));
        VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(ForgeRenderTypes.getUnlitTranslucent(TEXTURE_GLASS));
        ACExRenderCompat.renderToBuffer(((NucleeperModel)this.getParentModel()), poseStack, ivertexbuilder2, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), -1);
        int buttonDiv = entitylivingbaseIn.tickCount / 5 % 6;
        if (entitylivingbaseIn.isCharged()) {
            buttonDiv = entitylivingbaseIn.tickCount / 2 % 6;
        }

        ResourceLocation buttons;
        if (!ACExData.get(entitylivingbaseIn, ACExAttachments.DEFUSED)){
            if (buttonDiv < 2) {
                buttons = TEXTURE_BUTTONS_0;
            } else if (buttonDiv < 4) {
                buttons = TEXTURE_BUTTONS_1;
            } else {
                buttons = TEXTURE_BUTTONS_2;
            }
            VertexConsumer ivertexbuilder3 = bufferIn.getBuffer(RenderType.eyes(buttons));
            ACExRenderCompat.renderToBuffer(((NucleeperModel)this.getParentModel()), poseStack, ivertexbuilder3, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), -1);

        }
        VertexConsumer ivertexbuilder4 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(TEXTURE_EXPLODE));
        ACExRenderCompat.renderToBuffer(((NucleeperModel)this.getParentModel()), poseStack, ivertexbuilder4, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), ACExRenderCompat.colorFromFloat(explodeProgress, 1.0F, 1.0F, 1.0F));
    }



}
