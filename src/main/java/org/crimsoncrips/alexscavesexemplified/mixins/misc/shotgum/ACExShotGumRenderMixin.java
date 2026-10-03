package org.crimsoncrips.alexscavesexemplified.mixins.misc.shotgum;

import com.github.alexmodguy.alexscaves.client.model.ShotGumModel;
import com.github.alexmodguy.alexscaves.client.render.item.ACItemstackRenderer;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.misc.ACExBuckshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ACItemstackRenderer.class, remap = false)
public abstract class ACExShotGumRenderMixin {

    @WrapOperation(method = "renderByItem", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/model/ShotGumModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"))
    private void alexsCavesExemplified$renderByItem(ShotGumModel model, Entity entity, float shootProgress, float gumballsLeft, float crankAngle, float netHeadYaw, float headPitch, Operation<Void> original, @Local(argsOnly = true) ItemStack stack) {
        if (!ACExBuckshot.enabled()) {
            original.call(model, entity, shootProgress, gumballsLeft, crankAngle, netHeadYaw, headPitch);
            return;
        }
        original.call(model, entity, 0.0F, gumballsLeft, crankAngle, netHeadYaw, headPitch);
        float age = ACExBuckshot.shotAge(stack, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false));
        ACExShotGumModelAccessor parts = (ACExShotGumModelAccessor) model;
        parts.alexsCavesExemplified$getBarrels().rotationPointZ += 3.0F * ACExBuckshot.pump(age);
        parts.alexsCavesExemplified$getCrank().rotateAngleX += (float) Math.toRadians(ACExBuckshot.pumpCrank(age));
    }
}
