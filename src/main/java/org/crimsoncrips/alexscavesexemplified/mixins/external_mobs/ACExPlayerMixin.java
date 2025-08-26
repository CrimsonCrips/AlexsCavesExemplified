package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.message.WorldEventMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.ACExSoundRegistry;
import org.crimsoncrips.alexscavesexemplified.datagen.ACExDamageTypes;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExItemTagGenerator;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACExBaseInterface;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Player.class)
public abstract class ACExPlayerMixin extends LivingEntity implements ACExBaseInterface {


    @Shadow public abstract void displayClientMessage(Component pChatComponent, boolean pActionBar);

    @Shadow public abstract void die(DamageSource pCause);

    @Unique
    private Item[] lastAte = new Item[2];

    private int sweets = 0;

    protected ACExPlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void addSweets(int sweets) {
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

        if (AlexsCavesExemplified.COMMON_CONFIG.GLUTTONY_ENABLED.get()) {
            if (pFood.is(ACItemRegistry.SHARPENED_CANDY_CANE.get())) {
                this.hurt(this.damageSources().generic(), 3);
            }
            if (pFood.is(ACItemRegistry.BIOME_TREAT.get())) {
                this.hurt(this.damageSources().generic(), 1);
            }
            if (pFood.is(ACExItemTagGenerator.COLD_FOOD)) {
                this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), getTicksFrozen() + 65));
            }
            if (pFood.is(ACItemRegistry.HOT_CHOCOLATE_BOTTLE.get())) {
                this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), getTicksFrozen() - 100));
                this.setRemainingFireTicks(60);
            }
        }

        if (pFood.is(ACExItemTagGenerator.SWEETS)) {
            addSweets(1);
        }

        if (pFood.is(ACItemRegistry.SERENE_SALAD.get()) && AlexsCavesExemplified.COMMON_CONFIG.SERENED_ENABLED.get()){
            this.addEffect(new MobEffectInstance(ACExEffects.SERENED.get(), 1200, 0));
            ACExUtils.awardAdvancement(this,"serened","serened");
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

        if (lastAte[0] != null && lastAte[1] != null && AlexsCavesExemplified.COMMON_CONFIG.GLUTTONY_ENABLED.get()) {
            String[] foodItems = {"purple_soda_bottle", "frostmint"};
            String firstFood = foodItems[random.nextInt(0, 2)];
            if (lastAte[0].toString().equals(firstFood) && lastAte[1].toString().equals(foodItems[random.nextInt(0, 2)]) && !lastAte[1].toString().equals(firstFood)) {
                AlexsCaves.sendMSGToAll(new WorldEventMessage(9 , (int)this.getX(), (int)this.getY(), (int) this.getZ()));
                level.playSound(null, this.getBlockX(), this.getBlockY(), this.getBlockZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, -3.0F);
                this.hurt(ACExDamageTypes.getDamageSource(level(), ACExDamageTypes.STOMACH_DAMAGE), 6F);

                lastAte[0] = null;
                lastAte[1] = null;
            }
        }
        if (sweets >= 10) {
            sweets = 0;
            player.playSound(ACExSoundRegistry.SWEET_PUNISHED.get(), 1, 1);
            this.die(ACExDamageTypes.getDamageSource(level(), ACExDamageTypes.SWEET_PUNISH));
        }
    }


    private void sweetCounter(int value){
        if (AlexsCavesExemplified.COMMON_CONFIG.SWEET_PUNISHMENT_ENABLED.get()) {
            sweets = sweets + value;
            if (sweets == 8 && !this.level().isClientSide) {
                if (this.level().getRandom().nextBoolean()) {
                    this.displayClientMessage(Component.translatable("misc.alexscavesexemplified.candy_warn_0"), true);
                } else this.displayClientMessage(Component.nullToEmpty("misc.alexscavesexemplified.candy_warn_1"), true);
            }
        }
    }

}
