package org.crimsoncrips.alexscavesexemplified.mixins.mobs.teletor;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.MagneticWeaponEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
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

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        TeletorEntity teletor = (TeletorEntity)(Object)this;

        if (AlexsCavesExemplified.COMMON_CONFIG.TELETOR_ARMORY_ENABLED.get() && !teletor.isDeadOrDying()){
            for (ItemEntity item : teletor.level().getEntitiesOfClass(ItemEntity.class, teletor.getBoundingBox().inflate(8))) {
                if (item.getItem().is(ACTagRegistry.TELETOR_SPAWNS_WITH) && teletor.getWeapon() == null) {
                    ItemStack stolen = item.getItem();
                    MagneticWeaponEntity magneticWeapon = ACEntityRegistry.MAGNETIC_WEAPON.get().create(this.level());
                    stolen.getTag().putBoolean("Stolen", true);
                    magneticWeapon.setItemStack(stolen);
                    magneticWeapon.setControllerUUID(teletor.getUUID());
                    teletor.setWeaponUUID(magneticWeapon.getUUID());
                    level().addFreshEntity(magneticWeapon);
                    magneticWeapon.setPos(item.position());
                    item.discard();

                    ACExUtils.awardAdvancement(item.getOwner(),"teletor_rearm","rearm");
                }
            }
        }
    }

    @Inject(method = "dropEquipment", at = @At(value = "HEAD"))
    private void alexsCavesExemplified$dropEquipment(CallbackInfo ci) {
        Entity weapon = this.getWeapon();
        if (weapon instanceof MagneticWeaponEntity magneticWeapon) {
            ItemStack itemstack = magneticWeapon.getItemStack();
            float f1 = this.getEquipmentDropChance(EquipmentSlot.MAINHAND);
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) && !(this.random.nextFloat() < f1) && itemstack.getTag().getBoolean("Stolen")) {
                ItemEntity stolenItem = new ItemEntity(this.level(), this.getX(), this.getEyeY(), this.getZ(), itemstack);
                stolenItem.setGlowingTag(true);
                this.level().addFreshEntity(stolenItem);
            }

        }
    }


}
