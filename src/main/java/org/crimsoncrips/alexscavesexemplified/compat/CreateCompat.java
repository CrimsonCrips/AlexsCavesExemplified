package org.crimsoncrips.alexscavesexemplified.compat;

import biomesoplenty.api.block.BOPBlocks;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.FrostmintSpearEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Unique;

public class CreateCompat {
    public static boolean isHoney(){
       return AllFluids.CHOCOLATE.getBlock().isPresent();
    }
    public static boolean isChocolate(){
       return AllFluids.HONEY.getBlock().isPresent();
    }


    public static void solidifyCreateLiquid(FrostmintSpearEntity frostmintSpear, Level level, BlockPos blockPos){
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    BlockPos icePos = new BlockPos(blockPos.getX() + x, blockPos.getY() + y , blockPos.getZ() + z);
                    BlockState blockState = level.getBlockState(icePos);
                    if (blockState.getFluidState().getFluidType().equals(AllFluids.CHOCOLATE.getType())) {
                        level.setBlock(icePos, ACBlockRegistry.BLOCK_OF_CHOCOLATE.get().defaultBlockState(), 3);
                        level.scheduleTick(icePos, blockState.getBlock(), 2);
                        frostmintSpear.discard();
                        explode(level,frostmintSpear);
                    } else if (blockState.getFluidState().getFluidType().equals(AllFluids.HONEY.getType())) {
                        level.setBlock(icePos, Blocks.HONEY_BLOCK.defaultBlockState(), 3);
                        level.scheduleTick(icePos, blockState.getBlock(), 2);
                        frostmintSpear.discard();
                        explode(level,frostmintSpear);
                    }

                }
            }
        }
    }

    private static void explode(Level level, FrostmintSpearEntity frostmintSpear) {
        FrostmintExplosion explosion = new FrostmintExplosion(level, frostmintSpear.getOwner(), frostmintSpear.getX(), frostmintSpear.getY(0.5), frostmintSpear.getZ(), 2.0F, Explosion.BlockInteraction.KEEP, true);
        explosion.explode();
        explosion.finalizeExplosion(true);
    }

    public static int createDivingSuit(LivingEntity livingEntity){
        int i = 0;
        if (livingEntity.getItemBySlot(EquipmentSlot.HEAD).is((Item) AllItems.COPPER_DIVING_HELMET.get())) {
            i = i + 2;
        }
        if (livingEntity.getItemBySlot(EquipmentSlot.HEAD).is((Item) AllItems.NETHERITE_DIVING_HELMET.get())) {
            i = i + 4;
        }
        if (livingEntity.getItemBySlot(EquipmentSlot.FEET).is((Item) AllItems.COPPER_DIVING_BOOTS.get())) {
            i = i + 2;
        }
        if (livingEntity.getItemBySlot(EquipmentSlot.FEET).is((Item) AllItems.NETHERITE_DIVING_BOOTS.get())) {
            i = i + 4;
        }

        return i;
    }

    public static Block createBlockRegistry(int num){
        return switch (num) {
            case 1 -> AllFluids.CHOCOLATE.getBlock().get();
            case 2 -> AllFluids.HONEY.getBlock().get();
            case 3 -> AllBlocks.FLUID_PIPE.get();
            case 4 -> AllBlocks.SMART_FLUID_PIPE.get();


            default -> throw new IllegalStateException("Unexpected value: " + num);
        };
    }

}