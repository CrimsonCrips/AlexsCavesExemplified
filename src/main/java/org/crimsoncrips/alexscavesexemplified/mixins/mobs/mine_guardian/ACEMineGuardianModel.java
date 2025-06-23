package org.crimsoncrips.alexscavesexemplified.mixins.mobs.mine_guardian;

import com.github.alexmodguy.alexscaves.client.model.MineGuardianModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.entity.MineGuardianRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

@Mixin(MineGuardianModel.class)
public abstract class ACEMineGuardianModel extends AdvancedEntityModel<MineGuardianEntity> {


    @Shadow @Final private AdvancedModelBox eye;

    @Inject(method = "setupAnim(Lcom/github/alexmodguy/alexscaves/server/entity/living/MineGuardianEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getCameraEntity()Lnet/minecraft/world/entity/Entity;"),locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void alexsCavesExemplified$setupAnim(MineGuardianEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci, float partialTicks, float explodeProgress, float scanProgress) {
        MineGuardianXtra accesor = (MineGuardianXtra) entity;
        if (accesor.alexsCavesExemplified$getVariant() == -1){
            ci.cancel();
            eye.setPos(eye.defaultPositionX - 4, eye.defaultPositionY - 2, eye.defaultPositionZ + 2);
        }
    }







}
