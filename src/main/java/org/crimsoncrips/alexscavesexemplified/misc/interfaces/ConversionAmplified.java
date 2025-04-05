package org.crimsoncrips.alexscavesexemplified.misc.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public interface ConversionAmplified {

    void setStack(ItemStack itemStack);

    void setOverdrived(boolean val);

    boolean isOverdrived();


    List<ItemEntity> getItemsAbove(Level level, BlockPos pos);

}
