package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.MagneticWeaponEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GingerbreadManEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TeletorEntity;
import com.github.alexmodguy.alexscaves.server.item.GalenaGauntletItem;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;

import org.crimsoncrips.alexscavesexemplified.server.enchantment.ACEEnchants;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;


@Mixin(GalenaGauntletItem.class)
public abstract class ACEGauntletItemMixin extends Item {


    public ACEGauntletItemMixin(Properties pProperties) {
        super(pProperties);
    }

    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean onlyFlyIfAllowed(boolean original, @Local Player player, @Local InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        Entity itemLook = ACEUtils.getLookingAtEntity(player);
        return original || ((itemLook instanceof ItemEntity item && item.getItem().is(ACTagRegistry.MAGNETIC_ITEMS) || (itemLook instanceof MagneticWeaponEntity magneticWeaponEntity && magneticWeaponEntity.getController() instanceof TeletorEntity)) && AlexsCavesExemplified.COMMON_CONFIG.MAGNETICISM_ENABLED.get() && itemstack.getEnchantmentLevel(ACEEnchants.MAGNETICISM.get()) > 0);
    }

    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;)I"))
    private void alexsCavesExemplified$onUseTick(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci, @Local(ordinal = 1) ItemStack otherStack, @Local boolean otherMagneticWeaponsInUse) {
        Entity itemlook = null;

        if (living instanceof Player player){
            itemlook = ACEUtils.getLookingAtEntity(player);
        }
        for(MagneticWeaponEntity magneticWeapon : level.getEntitiesOfClass(MagneticWeaponEntity.class, living.getBoundingBox().inflate(48, 48, 48))){
            Entity controller = magneticWeapon.getController();
            if(controller != null && controller.is(living)){
                otherMagneticWeaponsInUse = true;
                break;
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.MAGNETICISM_ENABLED.get() && !otherMagneticWeaponsInUse && stack.getEnchantmentLevel(ACEEnchants.MAGNETICISM.get()) > 0) {
            if (itemlook instanceof MagneticWeaponEntity magneticWeaponEntity && magneticWeaponEntity.getController() instanceof TeletorEntity teletor && !otherStack.is(ACTagRegistry.TELETOR_SPAWNS_WITH)){
                magneticWeaponEntity.setControllerUUID(living.getUUID());
                teletor.setWeaponUUID(null);
                ItemStack itemStack = magneticWeaponEntity.getItemStack();
                if (itemStack.isDamageableItem()) {
                    itemStack.setDamageValue(itemStack.getMaxDamage() - teletor.getRandom().nextInt(1 + teletor.getRandom().nextInt(Math.max(itemStack.getMaxDamage() - 3, 1))));
                }
                ACEUtils.awardAdvancement(living,"galena_steal","steal");
            } else if (itemlook instanceof ItemEntity item && grabableItems(item.getItem(),stack) && !grabableItems(otherStack,stack)) {
                ItemStack copy = item.getItem().copy();
                item.discard();
                MagneticWeaponEntity magneticWeapon = ACEntityRegistry.MAGNETIC_WEAPON.get().create(level);
                if(magneticWeapon != null){
                    magneticWeapon.setItemStack(copy);
                    magneticWeapon.setPos(item.position().add(0, 1, 0));
                    magneticWeapon.setControllerUUID(living.getUUID());
                    level.addFreshEntity(magneticWeapon);
                }
            }
        }
    }



    public boolean grabableItems(ItemStack item, ItemStack gauntlet){
        boolean crystallization = gauntlet.getEnchantmentLevel(ACEnchantmentRegistry.CRYSTALLIZATION.get()) > 0;
        return item.is(crystallization ? ACTagRegistry.GALENA_GAUNTLET_CRYSTALLIZATION_ITEMS : ACTagRegistry.MAGNETIC_ITEMS);
    }
}