package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.MagneticWeaponEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GingerbreadManEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MagnetronEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MagnetronPartEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TeletorEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.GalenaGauntletItem;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;

import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MagnetronMagneticism;
import org.crimsoncrips.alexscavesexemplified.server.enchantment.ACEEnchants;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
    private boolean alexsCavesExemplified$use(boolean original, @Local Player player, @Local InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        Entity entityLook = ACEUtils.getLookingAtEntity(player);
        return original || ((entityLook instanceof ItemEntity item && grabableItems(item.getItem(),itemstack)) || (entityLook instanceof MagnetronEntity || entityLook instanceof MagnetronPartEntity) || (entityLook instanceof MagneticWeaponEntity magneticWeaponEntity && magneticWeaponEntity.getController() instanceof TeletorEntity)) && AlexsCavesExemplified.COMMON_CONFIG.MAGNETICISM_ENABLED.get() && itemstack.getEnchantmentLevel(ACEEnchants.MAGNETICISM.get()) > 0;
    }

        @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;)I"))
    private void alexsCavesExemplified$onUseTick(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci, @Local(ordinal = 1) ItemStack otherStack, @Local boolean otherMagneticWeaponsInUse) {
        Entity entityLook = null;

        if (living instanceof Player player){
            entityLook = ACEUtils.getLookingAtEntity(player);
        }
        for(MagneticWeaponEntity magneticWeapon : level.getEntitiesOfClass(MagneticWeaponEntity.class, living.getBoundingBox().inflate(16))){
            Entity controller = magneticWeapon.getController();
            if(controller != null && controller.is(living)){
                otherMagneticWeaponsInUse = true;
                break;
            }
        }


        if (AlexsCavesExemplified.COMMON_CONFIG.MAGNETICISM_ENABLED.get() && living instanceof Player player && !otherMagneticWeaponsInUse && stack.getEnchantmentLevel(ACEEnchants.MAGNETICISM.get()) > 0 && !grabableItems(otherStack, stack)) {
            if (entityLook instanceof MagneticWeaponEntity magneticWeaponEntity && magneticWeaponEntity.getController() instanceof TeletorEntity teletor) {
                magneticWeaponEntity.setControllerUUID(player.getUUID());
                teletor.setWeaponUUID(null);
                ItemStack itemStack = magneticWeaponEntity.getItemStack();
                if (itemStack.isDamageableItem()) {
                    itemStack.setDamageValue(itemStack.getMaxDamage() - teletor.getRandom().nextInt(1 + teletor.getRandom().nextInt(Math.max(itemStack.getMaxDamage() - 3, 1))));
                }
                ACEUtils.awardAdvancement(player, "galena_steal", "steal");
            } else if (entityLook instanceof ItemEntity item && grabableItems(item.getItem(), stack)) {
                ItemStack copy = item.getItem().copy();
                item.discard();
                MagneticWeaponEntity magneticWeapon = ACEntityRegistry.MAGNETIC_WEAPON.get().create(level);
                if (magneticWeapon != null) {
                    magneticWeapon.setItemStack(copy);
                    magneticWeapon.setPos(item.position().add(0, 1, 0));
                    magneticWeapon.setControllerUUID(player.getUUID());
                    level.addFreshEntity(magneticWeapon);
                }
            } else if (entityLook instanceof MagnetronEntity|| entityLook instanceof MagnetronPartEntity) {
                MagnetronEntity magnetronEntity = entityLook instanceof MagnetronEntity ? (MagnetronEntity)entityLook : ((MagnetronPartEntity) entityLook).getParent();

                if (!magnetronEntity.isAlive())
                    return;

                Vec3 vec3 = findTargetPos(player.blockPosition(), magnetronEntity);
                float partialTicks = 1;
                float yBodyRot = Mth.lerp(partialTicks, player.yBodyRotO, player.yBodyRot);
                boolean mainHand = player.getItemInHand(InteractionHand.MAIN_HAND).is(ACItemRegistry.GALENA_GAUNTLET.get());
                Vec3 offset = new Vec3(player.getBbWidth()  * (mainHand ? -0.75F : 0.75F), player.getBbHeight() * 0.68F, player.getBbWidth() * -0.1F).yRot((float) Math.toRadians(-yBodyRot));
                Vec3 armViewExtra = player.getViewVector(partialTicks).normalize().scale(0.75F);
                Vec3 from = player.getPosition(partialTicks).add(offset).add(armViewExtra);

                if (!level.isClientSide && player.getRandom().nextDouble() < 0.15) {
                    ((ServerLevel) level).sendParticles(ACEParticleRegistry.AZURE_FOCUSED_LIGHTNING.get(), from.x, from.y, from.z, 0, -vec3.x, -vec3.y + 1, -vec3.z, 0.5D);
                }
                    MagnetronMagneticism accesor = (MagnetronMagneticism) magnetronEntity;
                if (accesor.getRippedHeart() <= 100){
                    accesor.setRippedHeart(accesor.getRippedHeart() + 1);
                    if(!player.isCreative()){
                        magnetronEntity.setTarget(player);
                    }
                } else {
                    if (!player.isCreative()){
                        stack.hurtAndBreak(80,player,livingEntity -> {});
                        player.getCooldowns().addCooldown(stack.getItem(),500);
                    }
                    MagneticWeaponEntity magneticWeapon = (MagneticWeaponEntity)((EntityType)ACEntityRegistry.MAGNETIC_WEAPON.get()).create(level);
                    magnetronEntity.captureDrops();
                    magnetronEntity.kill();
                    if (magneticWeapon != null) {
                        magneticWeapon.setItemStack(ACBlockRegistry.HEART_OF_IRON.get().asItem().getDefaultInstance());
                        magneticWeapon.setPos(magnetronEntity.position().add(0.0F, 1.0F, 0.0F));
                        magneticWeapon.setControllerUUID(player.getUUID());
                        level.addFreshEntity(magneticWeapon);
                    }
                    player.stopUsingItem();
                }
            }
        }
    }



    public boolean grabableItems(ItemStack item, ItemStack gauntlet){
        boolean crystallization = gauntlet.getEnchantmentLevel(ACEnchantmentRegistry.CRYSTALLIZATION.get()) > 0;
        return item.is(crystallization ? ACTagRegistry.GALENA_GAUNTLET_CRYSTALLIZATION_ITEMS : (ACTagRegistry.MAGNETIC_ITEMS));
    }

    @Unique
    private static Vec3 findTargetPos(BlockPos blockPos, LivingEntity livingEntity) {
        Vec3 center = Vec3.atCenterOf(blockPos);
        return new Vec3(center.x - livingEntity.getX(),center.y - livingEntity.getY(), center.z - livingEntity.getZ());
    }
}