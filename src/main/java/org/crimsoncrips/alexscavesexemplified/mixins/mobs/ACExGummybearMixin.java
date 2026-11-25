package org.crimsoncrips.alexscavesexemplified.mixins.mobs;





import org.crimsoncrips.alexscavesexemplified.backport.ACExCompat;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Unique;
import com.github.alexmodguy.alexscaves.server.entity.living.GummyBearEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.GummyColors;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;


@Mixin(GummyBearEntity.class)
public abstract class ACExGummybearMixin extends Animal {


    @Unique
    private static final int JELLYBEAN_SLEEP_TICKS = ACExUtils.seconds(400.0F);

    @Shadow private int sleepFor;

    @Shadow private int jellybeansToMake;

    @Shadow public abstract boolean isDigestiblePotion(ItemStack itemStack);

    @Shadow public abstract boolean isDigesting();


    @Shadow public abstract void setDigesting(boolean bool);

    @Shadow public abstract boolean digestEffect(Potion potion);

    @Shadow public abstract GummyColors getGummyColor();

    @Shadow public abstract boolean isBearSleeping();

    @Shadow public abstract void setStanding(boolean bool);

    @Shadow public abstract void setSitting(boolean bool);

    protected ACExGummybearMixin(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float pAmount) {

        if (AlexsCavesExemplified.COMMON_CONFIG.JELLYBEAN_CHANGES_ENABLED.get()){
            Entity entity = damageSource.getEntity();
            if (entity != null && isBearSleeping()) {
                this.jellybeansToMake = Math.max(0, (ACExData.get(this, ACExAttachments.GUMMY_SLEEP_TOTAL) - sleepFor) / JELLYBEAN_SLEEP_TICKS);
                this.sleepFor = 0;
                ACExUtils.awardAdvancement(entity,"interrupt","interrupt");

            }
        }

        return super.hurt(damageSource, pAmount);
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Potion potion = PotionUtils.getPotion(itemstack);
        if (isDigestiblePotion(itemstack) && !isDigesting() && potion != Potions.EMPTY) {
            if (AlexsCavesExemplified.COMMON_CONFIG.JELLYBEAN_CHANGES_ENABLED.get()) {
                this.jellybeansToMake = 0;
            }
            this.setDigesting(true);
            this.digestEffect(potion);
            this.usePlayerItem(player, hand, itemstack);
            if (!player.getAbilities().instabuild) {
                player.addItem(new ItemStack(Items.GLASS_BOTTLE));
            }

            this.sleepFor = ACExUtils.seconds(1200.0F) * (2 + this.random.nextInt(2));
            if (AlexsCavesExemplified.COMMON_CONFIG.JELLYBEAN_CHANGES_ENABLED.get()) {
                ACExData.set(this, ACExAttachments.GUMMY_SLEEP_TOTAL, sleepFor);
                this.jellybeansToMake = 1 + sleepFor / JELLYBEAN_SLEEP_TICKS;
            } else {
                this.jellybeansToMake = this.random.nextInt(2) + 3;
            }
            this.playSound((SoundEvent)ACSoundRegistry.GUMMY_BEAR_EAT.get(), this.getSoundVolume(), this.getVoicePitch());
            return InteractionResult.SUCCESS;
        }

        if (isBearSleeping() && AlexsCavesExemplified.COMMON_CONFIG.SWEETISH_SPEEDUP_ENABLED.get() && this.sleepFor > 0 && itemstack.is(matchingSweetishFish())) {
            boost(player, itemstack);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    @ModifyConstant(method = "tick",constant = @Constant(intValue = 85))
    private int alexsCavesExemplified$tick(int amount) {
        if (AlexsCavesExemplified.COMMON_CONFIG.JELLYBEAN_CHANGES_ENABLED.get()){
            return 100000000;
        } else {
            return amount;
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.AC_FIXES_ENABLED.get()) {
            this.setStanding(false);
            this.setSitting(compound.getBoolean("BearSitting"));
        }
    }

    @Unique
    private Item matchingSweetishFish() {
        return switch (this.getGummyColor()) {
            case GREEN -> ACItemRegistry.SWEETISH_FISH_GREEN.get();
            case BLUE -> ACItemRegistry.SWEETISH_FISH_BLUE.get();
            case YELLOW -> ACItemRegistry.SWEETISH_FISH_YELLOW.get();
            case PINK -> ACItemRegistry.SWEETISH_FISH_PINK.get();
            default -> ACItemRegistry.SWEETISH_FISH_RED.get();
        };
    }

    @Unique
    private void boost(Player player, ItemStack itemStack){
        ACExCompat.consume(itemStack, 1, player);
        ACExUtils.awardAdvancement(player,"feed_speedup","feed");
        if (sleepFor >= ACExUtils.seconds(50.0F)) {
            this.sleepFor = sleepFor - ACExUtils.seconds(50.0F);
        } else {
            this.sleepFor = 0;
        }
        this.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
        for(int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
        }
    }



}
