package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetItemGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.TremorConsumption;

import java.util.Collections;
import java.util.List;

public class ACETremorDroppedEatBlock extends MobTargetItemGoal {

    TremorsaurusEntity tremorsaurus;

    public ACETremorDroppedEatBlock(TremorsaurusEntity creature, boolean checkSight, boolean onlyNearby, int tickThreshold, int radius) {
        super(creature, checkSight, onlyNearby, tickThreshold, radius);
        tremorsaurus = creature;
    }

    @Override
    public void tick() {

        TremorConsumption tickAccesor = (TremorConsumption)(Object)tremorsaurus;

        if (this.targetEntity != null && this.targetEntity.isAlive()) {
            this.moveTo();
        } else {
            this.stop();
            this.mob.getNavigation().stop();
        }

        if (this.targetEntity != null && this.mob.hasLineOfSight(this.targetEntity) && (double)this.mob.getBbWidth() > 2.0 && this.mob.onGround()) {
            this.mob.getMoveControl().setWantedPosition(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1.0);
        }

        if (this.targetEntity != null && this.targetEntity.isAlive() && this.mob.distanceToSqr(this.targetEntity) < 4) {
            tremorsaurus.getNavigation().stop();
            tremorsaurus.setInSittingPose(true);
            if (tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION && tremorsaurus.isInSittingPose() && !tickAccesor.isSniffed()) {
                tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_SNIFF);
            }
            if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_SNIFF && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15) {
                if (this.hunter.canTargetItem(targetEntity.getItem())){
                    tickAccesor.setSniffed(true);
                } else this.stop();
            }
            if (tickAccesor.isSniffed() && tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION && tremorsaurus.isInSittingPose()){
                tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_BITE);
            }
            if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_BITE && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15){
                if (this.hunter.canTargetItem(targetEntity.getItem())){
                    ACEUtils.awardAdvancement(targetEntity.getOwner(),"drop_food","drop");
                    tremorsaurus.heal(4);
                    tremorsaurus.playSound(ACSoundRegistry.TREMORSAURUS_BITE.get(), 1F, 1F);
                    targetEntity.kill();
                    if (AlexsCavesExemplified.COMMON_CONFIG.SEETHED_TAMING_ENABLED.get() && tremorsaurus.level().getRandom().nextDouble() < 0.08) {
                        tickAccesor.setSeethed(true);
                    }
                }
                this.stop();
            }
        }
    }

    public void stop() {
        super.stop();
        ((TremorConsumption)tremorsaurus).setSniffed(false);
        tremorsaurus.setInSittingPose(false);
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isPassenger() && (!this.mob.isVehicle() || this.mob.getControllingPassenger() == null)) {
            Mob var2 = this.mob;
            if (var2 instanceof TamableAnimal) {
                TamableAnimal tamableAnimal = (TamableAnimal)var2;
                if (tamableAnimal.isOrderedToSit()) {
                    return false;
                }
            }

            if (!this.mob.getItemInHand(InteractionHand.OFF_HAND).isEmpty() && !this.mob.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                return false;
            } else {
                if (!this.mustUpdate) {
                    long worldTime = this.mob.level().getGameTime() % 10L;
                    if (this.mob.getNoActionTime() >= 100 && worldTime != 0L) {
                        return false;
                    }

                    if (this.mob.getRandom().nextInt(this.executionChance) != 0 && worldTime != 0L) {
                        return false;
                    }
                }

                List<ItemEntity> list = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.getTargetableArea(this.getFollowDistance()), this.targetEntitySelector);
                if (list.isEmpty()) {
                    return false;
                } else {
                    Collections.sort(list, this.theNearestAttackableTargetSorter);
                    this.targetEntity = (ItemEntity)list.get(0);
                    this.mustUpdate = false;
                    this.hunter.onFindTarget(this.targetEntity);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    protected int nextStartTick(PathfinderMob mob) {
        return reducedTickDelay(100 + tremorsaurus.getRandom().nextInt(100));
    }
}
