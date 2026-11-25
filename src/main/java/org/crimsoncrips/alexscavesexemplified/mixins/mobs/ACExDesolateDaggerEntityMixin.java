package org.crimsoncrips.alexscavesexemplified.mixins.mobs;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.server.entity.item.DesolateDaggerEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import com.llamalad7.mixinextras.sugar.Local;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSectumsempra;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DesolateDaggerEntity.class)
public abstract class ACExDesolateDaggerEntityMixin {

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/DesolateDaggerEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 0))
    private Vec3 alexsCavesExemplified$tick1(Vec3 orbitMovement, @Local(ordinal = 0) Entity target) {
        DesolateDaggerEntity dagger = (DesolateDaggerEntity) (Object) this;
        if (!ACExSectumsempra.isSectumsempraDagger(dagger))
            return orbitMovement;
        Vec3 offset = ACExData.get(dagger, ACExAttachments.SECTUMSEMPRA_OFFSET).scale(ACExSectumsempra.settle(dagger, 0.0F) * (1.0F - dagger.getStab()));
        return ACExSectumsempra.center(target).add(offset).subtract(dagger.position());
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean alexsCavesExemplified$tick(Entity target, DamageSource source, float amount, Operation<Boolean> original) {
        if (ACExSectumsempra.isSectumsempraDagger((DesolateDaggerEntity) (Object) this))
            return false;
        return original.call(target, source, amount);
    }
}
