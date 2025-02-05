package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.living.CaniacEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.message.WorldEventMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.client.ACESoundRegistry;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.PlayerSweets;
import org.crimsoncrips.alexscavesexemplified.server.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.server.ACEDamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Player.class)
public abstract class ACEPlayer extends LivingEntity implements PlayerSweets {



    @Unique
    private Item[] lastAte = new Item[2];

    private int sweets = 0;

    protected ACEPlayer(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void alexsCavesExemplified$addSweets(int sweets) {
        sweetCounter(sweets);
    }

    @Inject(method = "eat", at = @At(value = "HEAD"))
    private void alexsCavesExemplified$eat(Level pLevel, ItemStack pFood, CallbackInfoReturnable<ItemStack> cir) {
        if (!pLevel.isClientSide){
            if (lastAte[0] != null) {
                lastAte[1] = lastAte[0];
            }
            lastAte[0] = pFood.getItem();
        }

        if (ACExemplifiedConfig.OVERTUNED_CONSUMPTION_ENABLED) {
            if (pFood.is(ACItemRegistry.SHARPENED_CANDY_CANE.get())) {
                this.hurt(this.damageSources().generic(), 1);
            }
            if (pFood.is(ACItemRegistry.BIOME_TREAT.get())) {
                this.hurt(this.damageSources().generic(), 1);
            }
            if (pFood.is(ACExexmplifiedTagRegistry.COLD_FOOD)) {
                this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), getTicksFrozen() + 65));
            }
            if (pFood.is(ACItemRegistry.HOT_CHOCOLATE_BOTTLE.get())) {
                this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), getTicksFrozen() - 100));
                this.setRemainingFireTicks(60);
            }
        }

        if (pFood.is(ACExexmplifiedTagRegistry.SWEETS)) {
            alexsCavesExemplified$addSweets(1);
        }
    }

    @Inject(method = "die", at = @At(value = "TAIL"))
    private void alexsCavesExemplified$die(CallbackInfo ci) {
        sweets = 0;
    }

    @Inject(method = "isSleepingLongEnough", at = @At(value = "HEAD"))
    private void alexsCavesExemplified$die(CallbackInfoReturnable<Boolean> cir) {
        sweets = 0;
    }


    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        Player player = (Player)(Object)this;
        Level level = this.level();

        if (lastAte[0] != null && lastAte[1] != null && ACExemplifiedConfig.OVERTUNED_CONSUMPTION_ENABLED) {
            String[] foodItems = {"purple_soda_bottle", "frostmint"};
            String firstFood = foodItems[random.nextInt(0, 2)];
            if (lastAte[0].toString().equals(firstFood) && lastAte[1].toString().equals(foodItems[random.nextInt(0, 2)]) && !lastAte[1].toString().equals(firstFood)) {
                AlexsCaves.sendMSGToAll(new WorldEventMessage(9 , (int)this.getX(), (int)this.getY(), (int) this.getZ()));
                level.playSound(null, this.getBlockX(), this.getBlockY(), this.getBlockZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, -3.0F);
                this.hurt(ACEDamageTypes.causeStomachDamage(level.registryAccess()), 6F);

                lastAte[0] = null;
                lastAte[1] = null;
            }
        }
        if (sweets >= 10) {
            sweets = 0;
            player.playSound(ACESoundRegistry.SWEET_PUNISHED.get(), 1, 1);
            this.hurt(ACEDamageTypes.causeSweetPunishDamage(level.registryAccess()), 10000);
        }
    }


    private void sweetCounter(int value){
        if (ACExemplifiedConfig.SWEET_PUNISHMENT_ENABLED) {
            sweets = sweets + value;
            if (sweets == 8 && this.level().isClientSide) {
                if (this.level().getRandom().nextBoolean()) {
                    this.sendSystemMessage(Component.nullToEmpty("Mom: That's enough candy for today child"));
                } else this.sendSystemMessage(Component.nullToEmpty("Dad: That's enough candy for today child"));
            }
        }
    }

}
