package org.crimsoncrips.alexscavesexemplified.mixins.blocks;


import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.CavePaintingBlock;
import com.github.alexmodguy.alexscaves.server.block.SmoothLimestoneBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACEBlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(SmoothLimestoneBlock.class)
public class ACESmoothLimestoneMixin extends Block {


    public ACESmoothLimestoneMixin(Properties pProperties) {
        super(pProperties);
    }

    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/SmoothLimestoneBlock;attemptPlaceMysteryCavePainting(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Z",ordinal = 1),remap = false)
    private boolean alexsCavesExemplified$use(SmoothLimestoneBlock instance, Level level, BlockPos paintingPos, Direction direction, boolean j, Operation<Boolean> original, @Local Player player) {
        if (AlexsCavesExemplified.COMMON_CONFIG.VOLCANIC_SACRIFICE_ENABLED.get() && player.getRandom().nextBoolean()) {
            return attemptPlaceSacrificeCavePainting(level,paintingPos,direction,j);
        }
        return original.call(instance,level,paintingPos,direction,j);

    }

    @Unique
    private boolean attemptPlaceSacrificeCavePainting(Level level, BlockPos pos, Direction facing, boolean checkOnly) {
        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                BlockPos paintingPos;
                if (facing == Direction.DOWN) {
                    paintingPos = pos.relative(Direction.SOUTH, i).relative(Direction.WEST, j);
                } else if (facing == Direction.UP) {
                    paintingPos = pos.relative(Direction.NORTH, i).relative(Direction.WEST, j);
                } else {
                    paintingPos = pos.above(i).relative(facing.getClockWise(), j);
                }

                if (!level.getBlockState(paintingPos).is((Block)ACBlockRegistry.SMOOTH_LIMESTONE.get())) {
                    return false;
                }

                if (!checkOnly) {
                    BlockState cavePainting = this.getSacrificeCavePainting(i,j).defaultBlockState();
                    level.setBlockAndUpdate(paintingPos, (BlockState)cavePainting.setValue(CavePaintingBlock.FACING, facing));
                }
            }
        }

        return true;
    }

    private Block getSacrificeCavePainting(int i, int j) {
        if(i == -1 && j == -1){
            return ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_9.get();
        }else if(i == -1 && j == 0){
            return ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_8.get();
        }else if(i == -1 && j == 1){
            return ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_7.get();
        }else if(i == 0 && j == -1){
            return ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_6.get();
        }else if(i == 0 && j == 0){
            return ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_5.get();
        }else if(i == 0 && j == 1){
            return ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_4.get();
        }else if(i == 1 && j == -1){
            return ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_3.get();
        }else if(i == 1 && j == 0){
            return ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_2.get();
        }else if(i == 1 && j == 1){
            return ACEBlockRegistry.CAVE_PAINTING_SACRIFICE_1.get();
        }
        return ACBlockRegistry.CAVE_PAINTING_DARK.get();
    }
}
