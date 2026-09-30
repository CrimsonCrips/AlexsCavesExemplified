package org.crimsoncrips.alexscavesexemplified.mixins.mobs.teletor;

import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponents;
import com.github.alexmodguy.alexscaves.server.entity.item.MagneticWeaponEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TeletorEntity.class)
public abstract class ACExTeletorMixin extends Monster {


    @Shadow public abstract Entity getWeapon();

    protected ACExTeletorMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "dropEquipment", at = @At(value = "HEAD"))
    private void alexsCavesExemplified$dropEquipment(CallbackInfo ci) {
        Entity weapon = this.getWeapon();
        if (weapon instanceof MagneticWeaponEntity magneticWeapon) {
            ItemStack itemstack = magneticWeapon.getItemStack();
            float f1 = this.getEquipmentDropChance(EquipmentSlot.MAINHAND);
            if (!itemstack.isEmpty() && !EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP) && !(this.random.nextFloat() < f1) && itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("Stolen")) {
                ItemEntity stolenItem = new ItemEntity(this.level(), this.getX(), this.getEyeY(), this.getZ(), itemstack);
                stolenItem.setGlowingTag(true);
                this.level().addFreshEntity(stolenItem);
            }

        }
    }


}
