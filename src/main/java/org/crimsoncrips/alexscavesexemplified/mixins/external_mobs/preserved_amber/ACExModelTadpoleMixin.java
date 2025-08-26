package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs.preserved_amber;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.TadpoleModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.level.block.Block;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TadpoleModel.class)
public abstract class ACExModelTadpoleMixin<T extends Tadpole> extends AgeableListModel<T> {

    //Props to Drullkus for assistance


    @Shadow @Final private ModelPart root;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/animal/frog/Tadpole;FFFFF)V", at = @At(value = "HEAD"),cancellable = true)
    private void tick(T frog, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo ci) {
        Block block = frog.level().getBlockState(frog.blockPosition()).getBlock();
        if (AlexsCavesExemplified.COMMON_CONFIG.PRESERVED_AMBER_ENABLED.get()) {
            this.root.getAllParts().forEach(ModelPart::resetPose);
            if (frog.isNoAi() && block == ACBlockRegistry.AMBER.get()) ci.cancel();
        }
    }


}
