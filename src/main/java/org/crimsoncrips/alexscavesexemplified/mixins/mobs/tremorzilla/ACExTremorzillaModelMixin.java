package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.client.model.TremorzillaModel;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(TremorzillaModel.class)
public abstract class ACExTremorzillaModelMixin extends AdvancedEntityModel<TremorzillaEntity> {

    @ModifyExpressionValue(method = "setupAnim(Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;getBeamProgress(F)F"),remap = false)
    private float alexsCavesExemplified$setupAnim(float original, @Local TremorzillaEntity tremorzilla) {
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && ACExData.get(tremorzilla, ACExAttachments.ANIMATION_BEAMING) ? (float) (original * 0.1) : original;
    }




}
