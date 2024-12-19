package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexthe666.alexsmobs.client.model.ModelCockroach;
import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AgeableMob.class)
public abstract class ACEAgeableMob extends PathfinderMob {

    protected ACEAgeableMob(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    //Props to Drullkus for assistance


    @Inject(method = "aiStep", at = @At(value = "HEAD",remap = false),cancellable = true)
    private void aiStep(CallbackInfo ci) {

        Block block = this.level().getBlockState(this.blockPosition()).getBlock();

        if (ACExemplifiedConfig.PRESERVED_AMBER_ENABLED && this.isNoAi() && block == ACBlockRegistry.AMBER.get()) ci.cancel();
    }


}