package org.crimsoncrips.alexscavesexemplified.mixins.blocks;



import org.crimsoncrips.alexscavesexemplified.backport.ACExCompat;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.server.block.ConversionCrucibleBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.ConversionCrucibleBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
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

    @ModifyReturnValue(method = "use", at = @At("RETURN"))
    private InteractionResult alexsCavesExemplified$use(InteractionResult original, @Local(argsOnly = true) Level worldIn, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) Player player, @Local(argsOnly = true) InteractionHand handIn) {
        ItemStack playerItem = player.getItemInHand(handIn);
        if (worldIn.getBlockEntity(pos) instanceof ConversionCrucibleBlockEntity crucible && !player.isShiftKeyDown() && !crucible.isWitchMode()) {
            if(playerItem.is(ACItemRegistry.RADIANT_ESSENCE.get()) && crucible.getConvertingToBiome() == null && !ACExData.get(crucible, ACExAttachments.OVERDRIVED)){
                crucible.setFilledLevel(1);
                ACExUtils.awardAdvancement(player,"overdrived_conversion","overdrived");
                ((ACExConversionCrucibleAccessor) crucible).setWantStack(ACItemRegistry.BIOME_TREAT.get().getDefaultInstance());
                ACExCompat.consume(playerItem, 1, player);
                worldIn.playSound(null, pos, ACSoundRegistry.CONVERSION_CRUCIBLE_ACTIVATE.get(), SoundSource.BLOCKS);
                crucible.markUpdated();
                ACExData.set(crucible, ACExAttachments.OVERDRIVED, true);
                return InteractionResult.SUCCESS;
            }
        }
        return original;
    }


}
