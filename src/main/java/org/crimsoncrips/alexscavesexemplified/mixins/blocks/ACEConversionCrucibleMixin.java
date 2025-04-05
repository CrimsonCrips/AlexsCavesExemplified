package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.ConversionCrucibleBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.ConversionCrucibleBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ConversionAmplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;


@Mixin(ConversionCrucibleBlock.class)
public abstract class ACEConversionCrucibleMixin extends BaseEntityBlock {


    protected ACEConversionCrucibleMixin(Properties pProperties) {
        super(pProperties);
    }

    @ModifyReturnValue(method = "use", at = @At("RETURN"))
    private InteractionResult alexsMobsInteraction$use(InteractionResult original,@Local Level worldIn,@Local BlockPos pos,@Local Player player,@Local InteractionHand handIn) {
        ItemStack playerItem = player.getItemInHand(handIn);
        if (worldIn.getBlockEntity(pos) instanceof ConversionCrucibleBlockEntity crucible && !player.isShiftKeyDown()) {
            if(playerItem.is(ACItemRegistry.RADIANT_ESSENCE.get())){
                crucible.setFilledLevel(1);
                ACEUtils.awardAdvancement(player,"overdrived_conversion","overdrived");
                ((ConversionAmplified) crucible).setStack(ACItemRegistry.BIOME_TREAT.get().getDefaultInstance());
                playerItem.shrink(1);
                worldIn.playSound(null, pos, ACSoundRegistry.CONVERSION_CRUCIBLE_ACTIVATE.get(), SoundSource.BLOCKS);
                crucible.markUpdated();
                ((ConversionAmplified) crucible).setOverdrived(true);
                return InteractionResult.SUCCESS;
            }
        }
        return original;
    }


}
