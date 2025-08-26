package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nucleeper;

import com.github.alexmodguy.alexscaves.client.model.NucleeperModel;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.github.alexthe666.alexsmobs.entity.EntityMurmur;
import com.github.alexthe666.alexsmobs.entity.EntityTiger;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.NucleeperXtra;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(NucleeperModel.class)
public abstract class ACENucleeperModelMixin extends AdvancedEntityModel<NucleeperEntity> {

    @Shadow @Final private AdvancedModelBox lpupil;

    @Shadow @Final private AdvancedModelBox rpupil;

    @Inject(method = "setupAnim(Lcom/github/alexmodguy/alexscaves/server/entity/living/NucleeperEntity;FFFFF)V", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$add(NucleeperEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (((NucleeperXtra)entity).isDefused()){
            this.lpupil.setPos(lpupil.defaultPositionX,lpupil.defaultPositionY,lpupil.defaultPositionZ + 2);
            this.rpupil.setPos(rpupil.defaultPositionX,rpupil.defaultPositionY,rpupil.defaultPositionZ + 2);
        }
    }
}
