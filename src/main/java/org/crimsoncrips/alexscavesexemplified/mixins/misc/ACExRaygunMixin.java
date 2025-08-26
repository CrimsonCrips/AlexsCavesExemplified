package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.item.HazmatArmorItem;
import com.github.alexmodguy.alexscaves.server.item.RaygunItem;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(RaygunItem.class)
public abstract class ACExRaygunMixin extends Item {


    public ACExRaygunMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "onUseTick", at = @At("HEAD"))
    private void onUseTick(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci) {
        int i = getUseDuration(stack) - timeUsing;
        float time = i < 15 ? i / (float) 15 : 1F;
        HitResult realHitResult = ProjectileUtil.getHitResultOnViewVector(living, Entity::canBeHitByProjectile, 25.0F * time);

        if(stack.getEnchantmentLevel(ACEnchantmentRegistry.X_RAY.get()) <= 0 && AlexsCavesExemplified.COMMON_CONFIG.REARAYNGEMENT_ENABLED.get() && !level.isClientSide()){
            if (realHitResult instanceof BlockHitResult blockHitResult) {
                BlockPos pos = blockHitResult.getBlockPos();
                if (stack.getEnchantmentLevel(ACEnchantmentRegistry.GAMMA_RAY.get()) > 0) {

                    if (level.random.nextDouble() < 0.2 && !level.getBlockState(pos).is(BlockTags.WITHER_IMMUNE)){
                        level.destroyBlock(pos, false, living);
                    }
                }
                else {
                    if (!level.getBlockState(pos).isAir() && !level.getBlockState(pos.above()).is(BlockTags.WITHER_IMMUNE))
                        level.setBlock(pos.above(), Blocks.FIRE.defaultBlockState(),2);
                }
            }
        }
    }

    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getType()Lnet/minecraft/world/entity/EntityType;"), cancellable = true)
    private void alexsCavesExemplified$onUseTick(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci, @Local(ordinal = 1) boolean gamma, @Local(ordinal = 3) int radiationLevel, @Local Entity entity, @Local(ordinal = 1) LivingEntity livingEntity) {
          if (AlexsCavesExemplified.COMMON_CONFIG.ARMORED_LIQUIDATORS_ENABLED.get()){
              ci.cancel();
              int hazmatLevel = HazmatArmorItem.getWornAmount(livingEntity);
              if (!livingEntity.getType().is(ACTagRegistry.RESISTS_RADIATION) && livingEntity.addEffect(new MobEffectInstance((MobEffect) ACEffectRegistry.IRRADIATED.get(), 800 / (hazmatLevel > 0 ? (hazmatLevel + 1) - radiationLevel : 1),radiationLevel - hazmatLevel + (gamma ? 2 : 0)))) {
                  AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(entity.getId(), living.getId(), gamma && hazmatLevel < 3 ? 4 : 0, 800 / (hazmatLevel > 0 ? (hazmatLevel + 1) - radiationLevel : 1)));
              }
          }

    }

    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"))
    private void alexsCavesExemplified$onUseTick1(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci,@Local (ordinal = 0) AABB hitBox) {
        for (Entity entity : level.getEntities(living, hitBox, Entity::isAlive)) {
            if (entity instanceof ItemEntity itemEntity && stack.getEnchantmentLevel(ACEnchantmentRegistry.GAMMA_RAY.get()) > 0 && itemEntity.getItem().is(ACBlockRegistry.NUCLEAR_BOMB.get().asItem())){
                if(living instanceof Player player && !player.isCreative()){
                    RaygunItem.setCharge(stack,RaygunItem.getCharge(stack) + 500);
                }
                
                ItemEntity gammaBomb = new ItemEntity(level,itemEntity.getX(),itemEntity.getY() + 0.5,itemEntity.getZ(), ACExBlockRegistry.GAMMA_NUCLEAR_BOMB.get().asItem().getDefaultInstance());
                level.addFreshEntity(gammaBomb);
                gammaBomb.setGlowingTag(true);
                gammaBomb.getItem().setCount(itemEntity.getItem().getCount());

                itemEntity.discard();
            }
        }
    }



}
