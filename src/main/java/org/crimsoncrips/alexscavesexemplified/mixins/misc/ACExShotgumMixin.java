package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import net.minecraft.sounds.SoundEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import org.crimsoncrips.alexscavesexemplified.backport.ACEnchantmentHelper;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.GumballEntity;
import com.github.alexmodguy.alexscaves.server.item.ShotGumItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import org.crimsoncrips.alexscavesexemplified.misc.ACExBuckshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShotGumItem.class)
public abstract class ACExShotgumMixin extends Item {

    public ACExShotgumMixin(Properties pProperties) {
        super(pProperties);
    }

    @ModifyArg(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/GumballEntity;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"), index = 5)
    private float alexsCavesExemplified$inventoryTick1(float inaccuracy, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) Level level) {
        return ACExBuckshot.enabled() && !this.explosive(level, stack) ? ACExBuckshot.SPREAD : inaccuracy;
    }

    @WrapOperation(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/GumballEntity;setDamage(F)V"))
    private void alexsCavesExemplified$inventoryTick2(GumballEntity gumball, float damage, Operation<Void> original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) Level level, @Local(argsOnly = true) Entity shooter) {
        if (ACExBuckshot.enabled() && !this.explosive(level, stack)) {
            ACExBuckshot.markPellet(gumball, shooter);
        } else {
            original.call(gumball, damage);
        }
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void alexsCavesExemplified$inventoryTick3(ItemStack stack, Level level, Entity entity, int slot, boolean held, CallbackInfo ci) {
        if (!ACExBuckshot.enabled())
            return;
        int recoil = ACExBuckshot.tickRecoil(stack);
        if (level instanceof ServerLevel serverLevel && entity instanceof LivingEntity living) {
            ACExBuckshot.tickSmokeWisps(serverLevel, living, stack, recoil);
        }
        if (recoil == ACExBuckshot.PUMP_SOUND_TICK && !level.isClientSide) {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CROSSBOW_LOADING_END, entity.getSoundSource(), 1.0F, 0.7F);
        }
        if (ShotGumItem.isShooting(stack) && ShotGumItem.getShootTime(stack) < 5) {
            ShotGumItem.setShootTime(stack, 5);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void alexsCavesExemplified$use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (ACExBuckshot.enabled() && ShotGumItem.hasAmmo(stack) && ACExBuckshot.getRecoil(stack) > 0) {
            cir.setReturnValue(InteractionResultHolder.pass(stack));
        }
    }

    @Inject(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/item/ShotGumItem;setShooting(Lnet/minecraft/world/item/ItemStack;Z)V"))
    private void alexsCavesExemplified$inventoryTick4(ItemStack stack, Level level, Entity entity, int slot, boolean held, CallbackInfo ci) {
        if (!ACExBuckshot.enabled())
            return;
        ACExBuckshot.setRecoil(stack, ACExBuckshot.RECOIL_TICKS, ACExBuckshot.RECOIL_TICKS);
        if (level instanceof ServerLevel serverLevel && entity instanceof LivingEntity living) {
            ACExBuckshot.muzzleSmoke(serverLevel, living, ACExBuckshot.inLeftHand(living, stack));
        }
    }

    @Inject(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/item/ShotGumItem;setShooting(Lnet/minecraft/world/item/ItemStack;Z)V"))
    private void alexsCavesExemplified$inventoryTick5(ItemStack stack, Level level, Entity entity, int slot, boolean held, CallbackInfo ci) {
        if (!level.isClientSide && ACExBuckshot.knockbackEnabled() && entity instanceof LivingEntity living) {
            ACExBuckshot.blastBack(living);
        }
    }

    @ModifyArg(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), index = 4)
    private SoundEvent alexsCavesExemplified$inventoryTick6(SoundEvent sound) {
        return ACExBuckshot.enabled() ? SoundEvents.GENERIC_EXPLODE : sound;
    }

    @Inject(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/item/ShotGumItem;setShooting(Lnet/minecraft/world/item/ItemStack;Z)V"))
    private void alexsCavesExemplified$inventoryTick7(ItemStack stack, Level level, Entity entity, int slot, boolean held, CallbackInfo ci) {
        if (level.isClientSide || !ACExBuckshot.enabled() || this.explosive(level, stack) || !(entity instanceof LivingEntity living))
            return;
        boolean leftHand = ACExBuckshot.inLeftHand(living, stack);
        boolean ricochet = ACEnchantmentHelper.getEnchantmentLevel(level, ACEnchantmentRegistry.TARGETED_RICOCHET, stack) > 0;
        boolean split = ACEnchantmentHelper.getEnchantmentLevel(level, ACEnchantmentRegistry.TRIPLE_SPLIT, stack) > 0;
        int maximumBounces = 4 + ACEnchantmentHelper.getEnchantmentLevel(level, ACEnchantmentRegistry.BOUNCY_BALL, stack) * 2;
        ACExBuckshot.fireExtraPellets(level, living, leftHand, ricochet, split, maximumBounces);
    }

    @Unique
    private boolean explosive(Level level, ItemStack stack) {
        return ACEnchantmentHelper.getEnchantmentLevel(level, ACEnchantmentRegistry.EXPLOSIVE_FLAVOR, stack) > 0;
    }
}
