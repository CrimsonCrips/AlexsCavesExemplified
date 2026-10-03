package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.ConversionCrucibleBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.ConversionCrucibleBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.mixins.misc.ACExConversionCrucibleAccessor;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;


@Mixin(ConversionCrucibleBlock.class)
public abstract class ACExConversionCrucibleMixin extends BaseEntityBlock {


    protected ACExConversionCrucibleMixin(Properties pProperties) {
        super(pProperties);
    }

    @ModifyReturnValue(method = "useItemOn", at = @At("RETURN"))
    private ItemInteractionResult alexsCavesExemplified$useItemOn(ItemInteractionResult original, @Local(argsOnly = true) ItemStack playerItem, @Local(argsOnly = true) Level worldIn, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) Player player) {
        if (worldIn.getBlockEntity(pos) instanceof ConversionCrucibleBlockEntity crucible && !player.isShiftKeyDown() && !crucible.isWitchMode()) {
            if(playerItem.is(ACItemRegistry.RADIANT_ESSENCE.get()) && crucible.getConvertingToBiome() == null && !crucible.getData(ACExAttachments.OVERDRIVED)){
                crucible.setFilledLevel(1);
                ACExUtils.awardAdvancement(player,"overdrived_conversion","overdrived");
                ((ACExConversionCrucibleAccessor) crucible).setWantStack(ACItemRegistry.BIOME_TREAT.get().getDefaultInstance());
                playerItem.shrink(1);
                worldIn.playSound(null, pos, ACSoundRegistry.CONVERSION_CRUCIBLE_ACTIVATE.get(), SoundSource.BLOCKS);
                crucible.markUpdated();
                crucible.setData(ACExAttachments.OVERDRIVED, true);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return original;
    }


}
