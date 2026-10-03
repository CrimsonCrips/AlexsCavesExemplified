package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import com.github.alexmodguy.alexscaves.client.model.SugarStaffModel;
import com.github.alexmodguy.alexscaves.client.render.item.ACItemstackRenderer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExDataComponents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ACItemstackRenderer.class)
public class ACExItemstackRendererMixin {

    @Shadow @Final private static SugarStaffModel SUGAR_STAFF_MODEL;

    @Unique
    private static final float SPIN_TURNS = 3.0F;
    @Unique
    private static final float SPIN_DECAY = ACExUtils.seconds(0.5F);
    @Unique
    private static final float SPIN_TIME = ACExUtils.seconds(4.0F);

    @Inject(method = "renderByItem", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/model/SugarStaffModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", shift = At.Shift.AFTER))
    private void alexsCavesExemplified$renderByItem(CallbackInfo ci, @Local(argsOnly = true) ItemStack itemStack) {
        Long castTime = itemStack.get(ACExDataComponents.STAFF_CAST);
        Minecraft minecraft = Minecraft.getInstance();
        if (castTime == null || minecraft.level == null)
            return;
        float elapsed = minecraft.level.getGameTime() - castTime + minecraft.getTimer().getGameTimeDeltaPartialTick(false);
        if (elapsed < 0.0F || elapsed > SPIN_TIME)
            return;
        float total = (float) (Math.PI * 2.0D * SPIN_TURNS);
        ((ACExSugarStaffModelAccessor) SUGAR_STAFF_MODEL).getMint().rotateAngleX += total * (1.0F - (float) Math.exp(-elapsed / SPIN_DECAY));
    }
}
