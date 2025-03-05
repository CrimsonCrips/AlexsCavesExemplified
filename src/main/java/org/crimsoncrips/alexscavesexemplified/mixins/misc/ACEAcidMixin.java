package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.AcidBlock;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.github.alexmodguy.alexscaves.server.item.MarineSnowItem;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.NucleeperXtra;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(AcidBlock.class)
public class ACEAcidMixin extends LiquidBlock {


    public ACEAcidMixin(FlowingFluid pFluid, Properties pProperties) {
        super(pFluid, pProperties);
    }

    @Inject(method = "entityInside", at = @At(value = "TAIL"))
    private void alexsCavesExemplified$entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.RUSTED_NUCLEEPER_ENABLED.get() && entity instanceof NucleeperEntity nucleeper && !((NucleeperXtra)nucleeper).alexsCavesExemplified$isRusted()) {
            ((NucleeperXtra)nucleeper).alexsCavesExemplified$setRusted(true);
            entity.playSound( ACSoundRegistry.ACID_BURN.get());
            nucleeper.getAttribute(Attributes.ARMOR).setBaseValue(2F);
            nucleeper.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20F);
            nucleeper.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.05F);
            nucleeper.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(2F);
            nucleeper.setHealth(20F);
        }
    }

}
