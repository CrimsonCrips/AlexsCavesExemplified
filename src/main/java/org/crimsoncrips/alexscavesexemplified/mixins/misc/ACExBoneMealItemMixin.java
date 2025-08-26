package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GrassBlock;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public abstract class ACExBoneMealItemMixin extends Item {


    public ACExBoneMealItemMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "applyBonemeal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BonemealableBlock;performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"))
    private static void alexsCavesExemplified$entityInside(ItemStack pStack, Level pLevel, BlockPos pPos, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (pLevel.getBiome(pPos).is(ACBiomeRegistry.PRIMORDIAL_CAVES) && AlexsCavesExemplified.COMMON_CONFIG.CAVIAL_BONEMEAL_ENABLED.get() && pLevel.getBlockState(pPos).getBlock() instanceof GrassBlock){
            ACExUtils.awardAdvancement(player,"propogate","bonemeal");
        }
    }
}