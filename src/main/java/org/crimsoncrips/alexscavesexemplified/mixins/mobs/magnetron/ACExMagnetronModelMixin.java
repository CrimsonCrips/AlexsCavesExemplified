package org.crimsoncrips.alexscavesexemplified.mixins.mobs.magnetron;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.client.model.MagnetronModel;
import com.github.alexmodguy.alexscaves.server.entity.living.MagnetronEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.util.Mth;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MagnetronModel.class)
public abstract class ACExMagnetronModelMixin extends AdvancedEntityModel<MagnetronEntity> {

    @Shadow
    @Final
    private AdvancedModelBox wheel;

    @Shadow
    @Final
    private AdvancedModelBox headPivot;

    @Inject(method = "setupAnim(Lcom/github/alexmodguy/alexscaves/server/entity/living/MagnetronEntity;FFFFF)V", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$setupAnim(MagnetronEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        int rippedHeart = ACExData.get(entity, ACExAttachments.RIPPED_HEART);
        if (rippedHeart <= 0)
            return;
        float partialTick = ageInTicks - entity.tickCount;
        float progress = Mth.clamp((rippedHeart + partialTick) / ACExUtils.HEART_RIP_TICKS, 0.0F, 1.0F);
        float intensity = 0.15F + 0.85F * progress * progress;
        float time = ageInTicks;
        float tremorZ = Mth.sin(time * 3.1F) * 0.6F + Mth.sin(time * 7.3F) * 0.4F;
        float tremorX = Mth.sin(time * 4.3F + 1.7F) * 0.6F + Mth.sin(time * 9.1F + 0.5F) * 0.4F;
        this.wheel.rotateAngleZ += intensity * 0.35F * tremorZ;
        this.wheel.rotateAngleX += intensity * 0.25F * tremorX;
        this.wheel.rotationPointX += intensity * 1.2F * Mth.sin(time * 5.7F);
        this.wheel.rotationPointY += intensity * 0.8F * Mth.sin(time * 6.9F + 2.0F);
        this.headPivot.rotateAngleZ += intensity * 0.15F * Mth.sin(time * 6.1F + 0.9F);
        this.headPivot.rotationPointY += intensity * 0.5F * Mth.sin(time * 8.3F);
    }


}
