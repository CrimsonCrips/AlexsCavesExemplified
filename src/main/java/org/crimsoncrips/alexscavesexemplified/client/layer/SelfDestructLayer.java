package org.crimsoncrips.alexscavesexemplified.client.layer;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import com.github.alexmodguy.alexscaves.client.model.NotorModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class SelfDestructLayer extends RenderLayer<NotorEntity, NotorModel> {


    public SelfDestructLayer(RenderLayerParent<NotorEntity, NotorModel> pRenderer) {
        super(pRenderer);
    }

    private static final ResourceLocation SELF_DESTRUCT_LAYER = ResourceLocation.fromNamespaceAndPath(AlexsCavesExemplified.MODID, "textures/entity/self_destruct_layer.png");


    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, NotorEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        int destructTime = pLivingEntity.getData(ACExAttachments.SELF_DESTRUCT_TIME);
        if ((destructTime / ACExUtils.seconds(0.25F)) % 2 == 0 && destructTime > 0){
            NotorModel entitymodel = this.getParentModel();
            entitymodel.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
            this.getParentModel().copyPropertiesTo(entitymodel);

            entitymodel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            VertexConsumer magmaGlow = pBuffer.getBuffer(ACRenderTypes.getEyesAlphaEnabled(SELF_DESTRUCT_LAYER));
            entitymodel.renderToBuffer(pPoseStack, magmaGlow, pPackedLight, LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0), -1);

        }
    }

}