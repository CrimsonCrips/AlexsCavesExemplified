package org.crimsoncrips.alexscavesexemplified.mixins.mobs.teletor;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.MagneticWeaponEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil.getEntityMagneticDelta;
import static com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil.setEntityMagneticDelta;


@Mixin(TeletorEntity.class)
public abstract class ACETeletorMixin extends Monster {


    @Shadow public abstract Entity getWeapon();

    protected ACETeletorMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
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

                    ACEUtils.awardAdvancement(item.getOwner(),"teletor_rearm","rearm");
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
