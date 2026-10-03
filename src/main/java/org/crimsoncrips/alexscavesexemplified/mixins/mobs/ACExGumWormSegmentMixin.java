package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.GumWormEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GumWormSegmentEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;
import org.crimsoncrips.alexscavesexemplified.server.entity.GumwirmEntity;
import org.crimsoncrips.alexscavesexemplified.server.entity.GumwirmSegmentEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GumWormSegmentEntity.class)
public abstract class ACExGumWormSegmentMixin {

    @Inject(method = "getPickResult", at = @At("HEAD"), cancellable = true)
    private void alexsCavesExemplified$getPickResult(CallbackInfoReturnable<ItemStack> cir) {
        Entity head = ((GumWormSegmentEntity) (Object) this).getHeadEntity();
        cir.setReturnValue(head == null ? null : head.getPickResult());
    }

    @WrapOperation(method = "createWormSegmentsFor", at = @At(value = "NEW", target = "(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)Lcom/github/alexmodguy/alexscaves/server/entity/living/GumWormSegmentEntity;"))
    private static GumWormSegmentEntity alexsCavesExemplified$createWormSegmentsFor(EntityType<?> type, Level level, Operation<GumWormSegmentEntity> original, @Local(argsOnly = true) GumWormEntity gumWorm) {
        if (gumWorm instanceof GumwirmEntity) {
            return new GumwirmSegmentEntity(ACExEntityRegistry.GUMWIRM_SEGMENT.get(), level);
        }
        return original.call(type, level);
    }
}
