package org.crimsoncrips.alexscavesexemplified.mixins.mobs.boundroid;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.spongepowered.asm.mixin.Unique;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.client.model.BoundroidModel;
import com.github.alexmodguy.alexscaves.client.render.entity.BoundroidRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BoundroidRenderer.class)
public abstract class ACExBoundroidRendererMixin extends MobRenderer<BoundroidEntity, BoundroidModel> {

    @Unique
    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/boundroid.png");
    @Unique
    private static final ResourceLocation TEXTURE_SCARED = new ResourceLocation("alexscaves:textures/entity/boundroid_scared.png");
    @Unique
    private static final ResourceLocation TEXTURE_DISABLED = new ResourceLocation("alexscavesexemplified:textures/entity/boundroid_disabled.png");

    public ACExBoundroidRendererMixin(EntityRendererProvider.Context pContext, BoundroidModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }


    public ResourceLocation getTextureLocation(BoundroidEntity entity) {
        return !ACExData.get(entity, ACExAttachments.MAGNETIZING) ? TEXTURE_DISABLED : entity.isScared() ? TEXTURE_SCARED : TEXTURE;
    }

}
