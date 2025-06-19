package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import com.github.alexmodguy.alexscaves.client.model.TremorzillaModel;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TremorzillaModel.class)
public abstract class ACETremorzillaModelMixin extends AdvancedEntityModel<TremorzillaEntity> {

    @ModifyExpressionValue(method = "setupAnim(Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;getBeamProgress(F)F"),remap = false)
    private float onlyFlyIfAllowed(float original, @Local TremorzillaEntity tremorzilla) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && myAccessor.isAnimationBeaming() ? (float) (original * 0.1) : original;
    }




}
