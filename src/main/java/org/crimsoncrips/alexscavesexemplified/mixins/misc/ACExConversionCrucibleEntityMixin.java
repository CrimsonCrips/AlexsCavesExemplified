package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.ConversionCrucibleBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.ConversionCrucibleBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;


@Mixin(ConversionCrucibleBlockEntity.class)
public abstract class ACExConversionCrucibleEntityMixin extends BlockEntity {


    @Shadow public abstract boolean isWitchMode();

    @Shadow public abstract ItemStack getWantItem();

    @Shadow private int witchRainbowColor;
    @Shadow private int biomeColor;


    public ACExConversionCrucibleEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/ConversionCrucibleBlockEntity;recursivelySpreadBiomeBlocks(Ljava/util/List;Lnet/minecraft/core/BlockPos;II)V"),index = 2,remap = false)
    private static int alexsCavesExemplified$1(int maxDistance, @Local(ordinal = 0, argsOnly = true) ConversionCrucibleBlockEntity bl1) {
        return bl1.getData(ACExAttachments.OVERDRIVED) ? 50 : maxDistance;
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/ConversionCrucibleBlockEntity;recursivelySpreadBiomeBlocks(Ljava/util/List;Lnet/minecraft/core/BlockPos;II)V"),index = 3,remap = false)
    private static int alexsCavesExemplified$tick2(int distanceIn, @Local(ordinal = 0, argsOnly = true) ConversionCrucibleBlockEntity bl1) {
        return bl1.getData(ACExAttachments.OVERDRIVED) ? 50 : distanceIn;
    }

    @ModifyExpressionValue(method = "convertBiome", at = @At(value = "INVOKE", target = "Ljava/lang/Math;ceil(D)D"),remap = false)
    private double alexsMobsInteraction$convertBiome(double original) {
        return original * (this.getData(ACExAttachments.OVERDRIVED) ? 2.8 : 1);
    }

    @ModifyConstant(method = "tick",constant = @Constant(intValue = 100,ordinal = 0),remap = false)
    private static int alexsCavesExemplified$tick3(int amount, @Local(ordinal = 0, argsOnly = true) ConversionCrucibleBlockEntity bl1) {
        if (AlexsCavesExemplified.COMMON_CONFIG.OVERDRIVED_CONVERSION_ENABLED.get() && bl1.getData(ACExAttachments.OVERDRIVED)){
            return 300;
        } else {
            return amount;
        }
    }

    @Inject(method = "tick", at = @At(value = "TAIL"),remap = false)
    private static void alexsCavesExemplified$tick4(Level level, BlockPos pos, BlockState state, ConversionCrucibleBlockEntity entity, CallbackInfo ci) {
        if (entity.tickCount % ACExUtils.seconds(0.25F) == 0 && AlexsCavesExemplified.COMMON_CONFIG.OVERDRIVED_CONVERSION_ENABLED.get() && !entity.isWitchMode()) {
            for(ItemEntity item : getItemsAbove(level, pos)) {
                if (entity.getConvertingToBiome() == null && item.getItem().is(ACItemRegistry.RADIANT_ESSENCE.get()) && !level.isClientSide && entity.getFilledLevel() <= 0){
                    entity.setFilledLevel(1);
                    ACExUtils.awardAdvancement(item.getOwner(),"overdrived_conversion","overdrived");
                    ((ACExConversionCrucibleAccessor) entity).setWantStack(ACItemRegistry.BIOME_TREAT.get().getDefaultInstance());
                    item.getItem().shrink(1);
                    level.playSound(null, pos, ACSoundRegistry.CONVERSION_CRUCIBLE_ACTIVATE.get(), SoundSource.BLOCKS);
                    entity.markUpdated();
                    entity.setData(ACExAttachments.OVERDRIVED, true);
                }
            }
        }
    }

    @ModifyReturnValue(method = "getConvertingToColor", at = @At("RETURN"),remap = false)
    private int beam4(int original) {
        return isWitchMode() || (this.getData(ACExAttachments.OVERDRIVED) && getWantItem().is(ACItemRegistry.BIOME_TREAT.get())) ? witchRainbowColor : biomeColor;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/ConversionCrucibleBlockEntity;setFilledLevel(I)V",ordinal = 0),remap = false)
    private static void alexsCavesExemplified$tick5(Level level, BlockPos pos, BlockState state, ConversionCrucibleBlockEntity entity, CallbackInfo ci) {
        ACExAttachments.set(entity, ACExAttachments.OVERDRIVED, false);
    }

    @Unique
    private static List<ItemEntity> getItemsAbove(Level level, BlockPos pos) {
        return ConversionCrucibleBlock.getSuckShape().toAabbs().stream().flatMap((aabb) -> level.getEntitiesOfClass(ItemEntity.class, aabb.move((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
    }



}
