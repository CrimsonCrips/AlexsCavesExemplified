package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.ConversionCrucibleBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.ConversionCrucibleBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ConverstionAmplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;


@Mixin(ConversionCrucibleBlockEntity.class)
public abstract class ACEConversionCrucibleEntityMixin extends BlockEntity implements ConverstionAmplified {


    @Shadow private ItemStack wantStack;

    @Shadow public abstract boolean isWitchMode();

    @Shadow public abstract ItemStack getWantItem();

    @Shadow private int witchRainbowColor;
    @Shadow private int biomeColor;
    private boolean overdrived;


    public ACEConversionCrucibleEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/ConversionCrucibleBlockEntity;recursivelySpreadBiomeBlocks(Ljava/util/List;Lnet/minecraft/core/BlockPos;II)V"),index = 2,remap = false)
    private static int alexsCavesExemplified$1(int maxDistance, @Local(ordinal = 0, argsOnly = true) ConversionCrucibleBlockEntity bl1) {
        return ((ConverstionAmplified) bl1).alexsCavesExemplified$isOverdrived() ? 50 : maxDistance;
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/ConversionCrucibleBlockEntity;recursivelySpreadBiomeBlocks(Ljava/util/List;Lnet/minecraft/core/BlockPos;II)V"),index = 3,remap = false)
    private static int alexsCavesExemplified$tick2(int distanceIn, @Local(ordinal = 0, argsOnly = true) ConversionCrucibleBlockEntity bl1) {
        return ((ConverstionAmplified) bl1).alexsCavesExemplified$isOverdrived() ? 50 : distanceIn;
    }

    @ModifyConstant(method = "tick",constant = @Constant(intValue = 100,ordinal = 0),remap = false)
    private static int alexsCavesExemplified$tick3(int amount, @Local(ordinal = 0, argsOnly = true) ConversionCrucibleBlockEntity bl1) {
        if (AlexsCavesExemplified.COMMON_CONFIG.OVERDRIVED_CONVERSION_ENABLED.get() && ((ConverstionAmplified) bl1).alexsCavesExemplified$isOverdrived()){
            return 300;
        } else {
            return amount;
        }
    }

    @Inject(method = "tick", at = @At(value = "TAIL"),remap = false)
    private static void alexsCavesExemplified$tick4(Level level, BlockPos pos, BlockState state, ConversionCrucibleBlockEntity entity, CallbackInfo ci) {
        if (entity.tickCount % 5 == 0 && AlexsCavesExemplified.COMMON_CONFIG.OVERDRIVED_CONVERSION_ENABLED.get() && !entity.isWitchMode()) {
            for(ItemEntity item : ((ConverstionAmplified) entity).alexsCavesExemplified$getItemsAtAndAbove(level, pos)) {
                if (entity.getConvertingToBiome() == null && item.getItem().is(ACItemRegistry.RADIANT_ESSENCE.get()) && !level.isClientSide && entity.getFilledLevel() <= 0){
                    entity.setFilledLevel(1);
                    ((ConverstionAmplified) entity).alexsCavesExemplified$setStack(ACItemRegistry.BIOME_TREAT.get().getDefaultInstance());
                    item.getItem().shrink(1);
                    level.playSound(null, pos, ACSoundRegistry.CONVERSION_CRUCIBLE_ACTIVATE.get(), SoundSource.BLOCKS);
                    entity.markUpdated();
                    ((ConverstionAmplified) entity).alexsCavesExemplified$setOverdrived(true);
                }
            }
        }
    }

    @ModifyReturnValue(method = "getConvertingToColor", at = @At("RETURN"),remap = false)
    private int beam4(int original) {
        return isWitchMode() || (overdrived && getWantItem().is(ACItemRegistry.BIOME_TREAT.get())) ? witchRainbowColor : biomeColor;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/ConversionCrucibleBlockEntity;convertBiome()V"),remap = false)
    private static void alexsCavesExemplified$tick5(Level level, BlockPos pos, BlockState state, ConversionCrucibleBlockEntity entity, CallbackInfo ci) {
        ((ConverstionAmplified) entity).alexsCavesExemplified$setOverdrived(false);
    }

    public void alexsCavesExemplified$setStack(ItemStack wantStack) {
        this.wantStack = wantStack;
    }

    public void alexsCavesExemplified$setOverdrived(boolean overdrived) {
        this.overdrived = overdrived;
    }

    public boolean alexsCavesExemplified$isOverdrived() {
        return overdrived;
    }

    @Inject(method = "loadAdditional", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$loadAdditional(CompoundTag compound, CallbackInfo ci) {
        this.overdrived = compound.getBoolean("Overdrived");
    }

    @Inject(method = "saveAdditional", at = @At(value = "TAIL"))
    private void alexsCavesExemplified$saveAdditional(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Overdrived", this.overdrived);
    }



    public List<ItemEntity> alexsCavesExemplified$getItemsAtAndAbove(Level level, BlockPos pos) {
        return (List)ConversionCrucibleBlock.getSuckShape().toAabbs().stream().flatMap((aabb) -> level.getEntitiesOfClass(ItemEntity.class, aabb.move((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
    }



}
