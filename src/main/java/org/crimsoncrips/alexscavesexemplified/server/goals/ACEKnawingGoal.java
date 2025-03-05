package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetItemGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;

public class ACEKnawingGoal extends MobTargetItemGoal {

    CorrodentEntity corrodent;

    public ACEKnawingGoal(CorrodentEntity creature, boolean checkSight) {
        super(creature, checkSight);
        corrodent = creature;
    }

    @Override
    public void tick() {
        if (this.targetEntity != null && this.targetEntity.isAlive()) {
            this.moveTo();
        } else {
            this.stop();
            this.mob.getNavigation().stop();
        }

        if (this.targetEntity != null && this.mob.hasLineOfSight(this.targetEntity) && (double)this.mob.getBbWidth() > (double)2.0F && this.mob.onGround()) {
            this.mob.getMoveControl().setWantedPosition(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), (double)1.0F);
        }

        if (this.targetEntity != null && this.targetEntity.isAlive() && this.mob.distanceToSqr(this.targetEntity) < this.hunter.getMaxDistToItem() && this.mob.getItemInHand(InteractionHand.OFF_HAND).isEmpty() && this.mob.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            if (corrodent.getAnimation() == CorrodentEntity.NO_ANIMATION) {
                corrodent.setAnimation(CorrodentEntity.ANIMATION_BITE);
            }
            if (corrodent.getAnimation() == CorrodentEntity.ANIMATION_BITE && corrodent.getAnimationTick() >= 5) {
                this.hunter.onGetItem(this.targetEntity);
                if (targetEntity.getOwner() instanceof ServerPlayer serverPlayer){
                    ACEUtils.awardAdvancement(serverPlayer,"knawing","knaw");
                }
                this.stop();
            }
        }

    }


}
