package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AgeableMob.class)
public abstract class ACExAgeableMobMixin extends PathfinderMob {

    protected ACExAgeableMobMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    //Props to Drullkus for assistance


    @Inject(method = "aiStep", at = @At(value = "HEAD"),cancellable = true)
    private void aiStep(CallbackInfo ci) {

        Block block = this.level().getBlockState(this.blockPosition()).getBlock();

        if (AlexsCavesExemplified.COMMON_CONFIG.PRESERVED_AMBER_ENABLED.get() && this.isNoAi() && block == ACBlockRegistry.AMBER.get()) ci.cancel();
    }


}
