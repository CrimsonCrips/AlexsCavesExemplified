package org.crimsoncrips.alexscavesexemplified.mixins.mobs.magnetron;

import com.github.alexmodguy.alexscaves.client.model.MagnetronModel;
import com.github.alexmodguy.alexscaves.server.entity.living.MagnetronEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MagnetronMagneticism;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(MagnetronModel.class)
public abstract class ACEMagnetronModelMixin extends AdvancedEntityModel<MagnetronEntity> {

    @Shadow
    @Final
    private AdvancedModelBox wheel;

    @Inject(method = "setupAnim(Lcom/github/alexmodguy/alexscaves/server/entity/living/MagnetronEntity;FFFFF)V", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$setupAnim(MagnetronEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        MagnetronMagneticism accesor = (MagnetronMagneticism) entity;
        int rippedHeart = accesor.getRippedHeart();
        float twitchinessAmount = (float) Math.sin(ageInTicks * 0.1F);
        this.flap(wheel, 10, (float)Math.sin(rippedHeart * 0.1F), false, 0, 0F, rippedHeart * 2, twitchinessAmount);
    }


}
