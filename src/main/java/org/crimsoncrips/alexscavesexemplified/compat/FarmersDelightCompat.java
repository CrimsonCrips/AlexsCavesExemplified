package org.crimsoncrips.alexscavesexemplified.compat;

import com.github.alexthe666.alexsmobs.entity.*;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.FeastBlock;

public class FarmersDelightCompat {

    public static void dinoEat(BlockPos blockPos, Level level){
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.getBlock() instanceof FeastBlock feastBlock){
            int servings = blockState.getValue(feastBlock.getServingsProperty());
            level.setBlock(blockPos, blockState.setValue(feastBlock.getServingsProperty(), Math.min(0, servings - 3)), 3);
            level.playSound(null, blockPos, SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

}