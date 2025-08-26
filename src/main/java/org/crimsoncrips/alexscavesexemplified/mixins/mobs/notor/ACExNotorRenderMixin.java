package org.crimsoncrips.alexscavesexemplified.mixins.mobs.notor;

import com.github.alexmodguy.alexscaves.client.model.NotorModel;
import com.github.alexmodguy.alexscaves.client.render.entity.NotorRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import org.crimsoncrips.alexscavesexemplified.client.layer.SelfDestructLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NotorRenderer.class)
public abstract class ACExNotorRenderMixin extends MobRenderer<NotorEntity, NotorModel> {


    public ACExNotorRenderMixin(EntityRendererProvider.Context pContext, NotorModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"),remap = false)
    private void alexsMobsInteraction$getItem(EntityRendererProvider.Context renderManagerIn, CallbackInfo ci) {
        this.addLayer(new SelfDestructLayer(this));
    }
}
