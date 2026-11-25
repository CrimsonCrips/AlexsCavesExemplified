package org.crimsoncrips.alexscavesexemplified.mixins.mobs;


import net.minecraftforge.registries.ForgeRegistries;
import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SubterranodonEntity.class)
public abstract class ACExSubterranodonMixin {

    @Unique
    private static final float REDUCTION_PER_ARMOR = 0.04F;
    @Unique
    private static final float MAX_REDUCTION = 0.8F;
    @Unique
    private static final ResourceLocation LIGHTWEIGHT = new ResourceLocation("alexsmobsinteraction", "lightweight");

    @Unique
    private static boolean isLightweight(Player rider) {
        ItemStack chest = rider.getItemBySlot(EquipmentSlot.CHEST);
        Enchantment lightweight = ForgeRegistries.ENCHANTMENTS.getValue(LIGHTWEIGHT);
        return !chest.isEmpty() && lightweight != null && EnchantmentHelper.getItemEnchantmentLevel(lightweight, chest) > 0;
    }

    @ModifyReturnValue(method = "getRiddenSpeed", at = @At("RETURN"))
    private float alexsCavesExemplified$getRiddenSpeed(float speed, @Local(argsOnly = true) Player rider) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.BURDENED_FLIGHT_ENABLED.get() || !((SubterranodonEntity) (Object) this).isFlying() || isLightweight(rider))
            return speed;
        return speed * (1.0F - Math.min(MAX_REDUCTION, rider.getArmorValue() * REDUCTION_PER_ARMOR));
    }
}
