package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.client.model.CaniacModel;
import com.github.alexmodguy.alexscaves.server.entity.living.CaniacEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CaniacModel.class, remap = false)
public abstract class ACExCaniacModelMixin {

    @Unique
    private static final float SHAKE_ANGLE = 0.35F;
    @Unique
    private static final float SHAKE_OFFSET = 0.6F;

    @Shadow @Final private AdvancedModelBox head;

    @Inject(method = "setupAnim(Lcom/github/alexmodguy/alexscaves/server/entity/living/CaniacEntity;FFFFF)V", at = @At("TAIL"))
    private void alexsCavesExemplified$setupAnim(CaniacEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.CANIAC_MANIAC_ENABLED.get())
            return;
        RandomSource random = entity.getRandom();
        float intensity = Mth.sin(ageInTicks / 50.0F) * 0.5F + 0.5F;
        this.head.rotateAngleX += (random.nextFloat() - 0.5F) * SHAKE_ANGLE * intensity;
        this.head.rotateAngleY += (random.nextFloat() - 0.5F) * SHAKE_ANGLE * intensity;
        this.head.rotateAngleZ += (random.nextFloat() - 0.5F) * SHAKE_ANGLE * intensity;
        this.head.rotationPointX += (random.nextFloat() - 0.5F) * SHAKE_OFFSET * intensity;
        this.head.rotationPointY += (random.nextFloat() - 0.5F) * SHAKE_OFFSET * intensity;
    }
}
