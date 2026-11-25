package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.blockentity.ConversionCrucibleBlockEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ConversionCrucibleBlockEntity.class)
public interface ACExConversionCrucibleAccessor {

    @Accessor("wantStack")
    void setWantStack(ItemStack wantStack);
}
