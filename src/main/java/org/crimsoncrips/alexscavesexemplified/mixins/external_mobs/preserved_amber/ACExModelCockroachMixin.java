package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs.preserved_amber;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexthe666.alexsmobs.client.model.ModelCockroach;
import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ModelCockroach.class)
public abstract class ACExModelCockroachMixin {

    //Props to Drullkus for assistance


    @Inject(method = "setupAnim(Lcom/github/alexthe666/alexsmobs/entity/EntityCockroach;FFFFF)V", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/alexsmobs/client/model/ModelCockroach;progressRotationPrev(Lcom/github/alexthe666/citadel/client/model/AdvancedModelBox;FFFFF)V",ordinal = 0),cancellable = true,remap = false)
    private void tick(EntityCockroach cockroach, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        Block block = cockroach.level().getBlockState(cockroach.blockPosition()).getBlock();
        if (AlexsCavesExemplified.COMMON_CONFIG.PRESERVED_AMBER_ENABLED.get() && cockroach.isNoAi() && ModList.get().isLoaded("alexsmobs") && block == ACBlockRegistry.AMBER.get()) ci.cancel();
    }


}
