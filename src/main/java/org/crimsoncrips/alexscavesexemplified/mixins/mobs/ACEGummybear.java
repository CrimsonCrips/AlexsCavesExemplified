package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;


@Mixin(GummyBearEntity.class)
public abstract class ACEGummybear extends Animal {


    @Shadow private int sleepFor;

    @Shadow private int jellybeansToMake;

    @Shadow public abstract boolean isDigestiblePotion(ItemStack itemStack);

    @Shadow public abstract boolean isDigesting();


    @Shadow public abstract void setDigesting(boolean bool);

    @Shadow public abstract boolean digestEffect(Potion potion);

    @Shadow public abstract GummyColors getGummyColor();

    @Shadow public abstract boolean isBearSleeping();

    private int setVariable;

    protected ACEGummybear(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float pAmount) {

        if (AlexsCavesExemplified.COMMON_CONFIG.JELLYBEAN_CHANGES_ENABLED.get()){
            Entity entity = damageSource.getEntity();
            if (entity != null && isBearSleeping()) {
                this.jellybeansToMake = setVariable / 4000 - sleepFor / 4000;
                this.sleepFor = 0;

            }
        }

        return super.hurt(damageSource, pAmount);
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (isDigestiblePotion(itemstack) && !isDigesting()) {
            if (AlexsCavesExemplified.COMMON_CONFIG.JELLYBEAN_CHANGES_ENABLED.get()) {
                this.jellybeansToMake = 0;
            }
            this.setDigesting(true);
            this.digestEffect(PotionUtils.getPotion(itemstack));
            this.usePlayerItem(player, hand, itemstack);
            if (!player.getAbilities().instabuild) {
                player.addItem(new ItemStack(Items.GLASS_BOTTLE));
            }

            this.sleepFor = 24000 * (2 + this.random.nextInt(2));
            if (AlexsCavesExemplified.COMMON_CONFIG.JELLYBEAN_CHANGES_ENABLED.get()) {
                this.setVariable = sleepFor;
                this.jellybeansToMake = 1 + setVariable / 8000;
            } else {
                this.jellybeansToMake = this.random.nextInt(2) + 3;
            }
            this.playSound((SoundEvent)ACSoundRegistry.GUMMY_BEAR_EAT.get(), this.getSoundVolume(), this.getVoicePitch());
            return InteractionResult.SUCCESS;
        }

        if (isBearSleeping() && AlexsCavesExemplified.COMMON_CONFIG.SWEETISH_SPEEDUP_ENABLED.get() && this.sleepFor > 0){
            return switch (this.getGummyColor().toString()) {
                case "GREEN" -> {
                    if (itemstack.is(ACItemRegistry.SWEETISH_FISH_GREEN.get())) {
                        boost(player);
                    }
                    yield InteractionResult.SUCCESS;
                }
                case "BLUE" -> {
                    if (itemstack.is(ACItemRegistry.SWEETISH_FISH_BLUE.get())) {
                        boost(player);
                    }
                    yield InteractionResult.SUCCESS;
                }
                case "YELLOW" -> {
                    if (itemstack.is(ACItemRegistry.SWEETISH_FISH_YELLOW.get())) {
                        boost(player);
                    }
                    yield InteractionResult.SUCCESS;
                }
                case "PINK" -> {
                    if (itemstack.is(ACItemRegistry.SWEETISH_FISH_PINK.get())) {
                        boost(player);
                    }
                    yield InteractionResult.SUCCESS;
                }
                default -> {
                    if (itemstack.is(ACItemRegistry.SWEETISH_FISH_RED.get())) {
                        boost(player);
                    }
                    yield InteractionResult.SUCCESS;
                }
            };
        }

        return super.mobInteract(player, hand);
    }

    @ModifyConstant(method = "tick",constant = @Constant(intValue = 85))
    private int modifyAmount(int amount) {
        if (AlexsCavesExemplified.COMMON_CONFIG.JELLYBEAN_CHANGES_ENABLED.get()){
            return 100000000;
        } else {
            return amount;
        }
    }

    public void boost(Entity entity){
        ACEUtils.awardAdvancement(entity,"feed_speedup","feed");
        if (sleepFor >= 1000) {
            this.sleepFor = sleepFor - 1000;
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
