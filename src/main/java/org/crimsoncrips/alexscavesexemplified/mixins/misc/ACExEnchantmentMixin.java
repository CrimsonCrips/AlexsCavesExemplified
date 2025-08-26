package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.ConversionCrucibleBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.extensions.IForgeEnchantment;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ConversionAmplified;
import org.crimsoncrips.alexscavesexemplified.server.enchantment.ACEEnchants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Enchantment.class)
public abstract class ACExEnchantmentMixin implements net.minecraftforge.common.extensions.IForgeEnchantment {

    @ModifyReturnValue(method = "canApplyAtEnchantingTable", at = @At("RETURN"),remap = false)
    private boolean alexsMobsInteraction$use(boolean original, @Local(argsOnly = true) ItemStack tool) {
        Enchantment enchantment = (Enchantment)(Object)this;
        if(tool.is(ACItemRegistry.DREADBOW.get()) && AlexsCavesExemplified.COMMON_CONFIG.DREAD_ADDAPTIONS_ENABLED.get()){
            Enchantment[] dreadbowAdditions = {Enchantments.POWER_ARROWS, Enchantments.FLAMING_ARROWS, Enchantments.PUNCH_ARROWS};
            for (Enchantment dreadbowAddition : dreadbowAdditions) {
                if (enchantment.equals(dreadbowAddition)) {
                    return true;
                }
            }
        }
        return original;
    }

}