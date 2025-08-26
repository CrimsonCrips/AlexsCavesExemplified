package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs.preserved_amber;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexthe666.alexsmobs.client.model.ModelFly;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ModelFly.class)
public abstract class ACExModelFlyMixin {

    //Props to Drullkus for assistance


    @Inject(method = "setupAnim(Lcom/github/alexthe666/alexsmobs/entity/EntityFly;FFFFF)V", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/alexsmobs/client/model/ModelFly;walk(Lcom/github/alexthe666/citadel/client/model/AdvancedModelBox;FFZFFFF)V",ordinal = 0),cancellable = true,remap = false)
    private void tick(EntityFly fly, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        Block block = fly.level().getBlockState(fly.blockPosition()).getBlock();
        if (AlexsCavesExemplified.COMMON_CONFIG.PRESERVED_AMBER_ENABLED.get() && fly.isNoAi() && ModList.get().isLoaded("alexsmobs") && block == ACBlockRegistry.AMBER.get()) ci.cancel();
    }


}
