package org.crimsoncrips.alexscavesexemplified.mixins.misc.projector;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import com.github.alexmodguy.alexscaves.server.block.HologramProjectorBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.HologramProjectorBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
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
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(HologramProjectorBlock.class)
public abstract class ACExHologramProjectorMixin extends BaseEntityBlock {


    protected ACExHologramProjectorMixin(Properties pProperties) {
        super(pProperties);
    }

    @Unique
    private static final int MAX_SCALE = 10;

    @Inject(method = "use", at = @At(value = "TAIL"), cancellable = true)
    private void alexsCavesExemplified$use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (!(worldIn.getBlockEntity(pos) instanceof HologramProjectorBlockEntity projectorBlockEntity) || !AlexsCavesExemplified.COMMON_CONFIG.SCALABLE_HOLOGRAM_ENABLED.get() || player.isShiftKeyDown())
            return;
        ItemStack heldItem = player.getMainHandItem();
        int scale = Math.max(1, ACExData.get(projectorBlockEntity, ACExAttachments.PROJECTION_SCALE));
        int newScale = scale;
        if (heldItem.is(ACItemRegistry.SCARLET_NEODYMIUM_INGOT.get())) {
            newScale = Math.min(scale + 1, MAX_SCALE);
        } else if (heldItem.is(ACItemRegistry.AZURE_NEODYMIUM_INGOT.get())) {
            newScale = Math.max(scale - 1, 1);
        } else {
            return;
        }
        if (!worldIn.isClientSide) {
            if (newScale != scale) {
                ACExData.set(projectorBlockEntity, ACExAttachments.PROJECTION_SCALE, newScale);
                projectorBlockEntity.setChanged();
                worldIn.playSound(null, pos, ACSoundRegistry.HOLOGRAM_STOP.get(), SoundSource.BLOCKS);
                ACExUtils.awardAdvancement(player, "resizing", "resize");
            }
            player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.hologram_scale", newScale, MAX_SCALE), true);
        }
        cir.setReturnValue(InteractionResult.sidedSuccess(worldIn.isClientSide));
    }

}
