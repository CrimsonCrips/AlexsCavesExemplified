package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.GumWormEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GumWormEntity.class)
public abstract class ACExGumWormMixin extends Monster {

    @Shadow private float digPitch;

    @Shadow public abstract float getTargetDigPitch();

    protected ACExGumWormMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return !this.hasData(ACExAttachments.SCRIPTED_WORM) && super.shouldDespawnInPeaceful();
    }

    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    private boolean alexsCavesExemplified$isInvulnerableTo(boolean original, DamageSource damageSource) {
        return this.hasData(ACExAttachments.SCRIPTED_WORM) ? !damageSource.is(DamageTypes.GENERIC_KILL) : original;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (this.hasData(ACExAttachments.SCRIPTED_WORM)) {
            this.digPitch = this.getTargetDigPitch();
        }
    }
}
