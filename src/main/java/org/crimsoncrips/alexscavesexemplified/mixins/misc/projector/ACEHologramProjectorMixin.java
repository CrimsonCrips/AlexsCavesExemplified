package org.crimsoncrips.alexscavesexemplified.mixins.misc.projector;

import com.github.alexmodguy.alexscaves.server.block.HologramProjectorBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.HologramProjectorBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEBaseInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(HologramProjectorBlock.class)
public abstract class ACEHologramProjectorMixin extends BaseEntityBlock {


    protected ACEHologramProjectorMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "use", at = @At(value = "TAIL"))
    private void alexsCavesExemplified$use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (worldIn.getBlockEntity(pos) instanceof HologramProjectorBlockEntity projectorBlockEntity && AlexsCavesExemplified.COMMON_CONFIG.SCALABLE_HOLOGRAM_ENABLED.get() && !player.isShiftKeyDown()) {
            ItemStack heldItem = player.getItemInHand(handIn);
            ACEBaseInterface accesor = (ACEBaseInterface)projectorBlockEntity;
            if (heldItem.is(ACItemRegistry.SCARLET_NEODYMIUM_INGOT.get())) {
                worldIn.playSound((Player) null, pos, ACSoundRegistry.HOLOGRAM_STOP.get(), SoundSource.BLOCKS);
                accesor.setProjectionScale(accesor.getProjectionScale() + 1);
                ACEUtils.awardAdvancement(player,"resizing","resize");
            }
            if (heldItem.is(ACItemRegistry.AZURE_NEODYMIUM_INGOT.get()) && ((ACEBaseInterface) projectorBlockEntity).getProjectionScale() > 1) {
                worldIn.playSound((Player) null, pos, ACSoundRegistry.HOLOGRAM_STOP.get(), SoundSource.BLOCKS);
                accesor.setProjectionScale(accesor.getProjectionScale() - 1);
                ACEUtils.awardAdvancement(player,"resizing","resize");
            }
        }
    }





}
