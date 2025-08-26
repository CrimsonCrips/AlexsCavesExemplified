package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs.preserved_amber;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.client.model.FrogModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.block.Block;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FrogModel.class)
public abstract class ACExModelFrogMixin<T extends Frog> extends HierarchicalModel<T> {

    //Props to Drullkus for assistance


    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/animal/frog/Frog;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/FrogModel;animate(Lnet/minecraft/world/entity/AnimationState;Lnet/minecraft/client/animation/AnimationDefinition;F)V",ordinal = 0),cancellable = true)
    private void tick(T frog, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo ci) {
        Block block = frog.level().getBlockState(frog.blockPosition()).getBlock();
        if (AlexsCavesExemplified.COMMON_CONFIG.PRESERVED_AMBER_ENABLED.get() && frog.isNoAi() && block == ACBlockRegistry.AMBER.get()) ci.cancel();
    }


}
