package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;

public class ACExTremorTempt extends TemptGoal {

    double speedModifier;
    TremorsaurusEntity tremorsaurus;
    private final Ingredient items;

    public ACExTremorTempt(TremorsaurusEntity tremorsaurus, double pSpeedModifier, Ingredient pItems, boolean pCanScare) {
        super(tremorsaurus, pSpeedModifier, pItems, pCanScare);
        this.tremorsaurus = tremorsaurus;
        this.items = pItems;
        this.speedModifier = pSpeedModifier;
    }

    public void tick() {
        this.tremorsaurus.getLookControl().setLookAt(this.player, (float)(this.tremorsaurus.getMaxHeadYRot() + 20), (float)this.tremorsaurus.getMaxHeadXRot());
        if (this.tremorsaurus.distanceToSqr(this.player) < 6.25D) {
            this.tremorsaurus.getNavigation().stop();
            if (tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION) {
                tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_BITE);
            }

            if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_BITE && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15) {
                ItemStack heldFood = this.items.test(player.getMainHandItem()) ? player.getMainHandItem() : player.getOffhandItem();

                if (!player.isCreative()){
                    heldFood.shrink(1);
                }
                if (player.getRandom().nextDouble() < 0.05 || player.isCreative()){
                    tremorsaurus.tame(player);
                    ACExUtils.awardAdvancement(player,"seethed_taming","tame");
                    tremorsaurus.level().broadcastEntityEvent(tremorsaurus, (byte)7);
                    stop();
                }
            }
        } else {
            this.tremorsaurus.getNavigation().moveTo(this.player, this.speedModifier);
        }

    }

    @Override
    public boolean canUse() {
        return super.canUse() && tremorsaurus.hasEffect(ACExEffects.SERENED.get());
    }
}