//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetItemGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity;
import com.google.common.base.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class ACEPickupDroppedBarrels<T extends ItemEntity> extends MobTargetItemGoal {

    private BrainiacEntity brainiac;
    
    public ACEPickupDroppedBarrels(BrainiacEntity creature, boolean checkSight) {
        super(creature, checkSight);
        brainiac = creature;
    }




    public boolean canUse() {
        return super.canUse() && !brainiac.hasBarrel();
    }


    public void tick() {
        if (this.targetEntity != null && this.targetEntity.isAlive()) {
            this.moveTo();
        } else {
            this.stop();
            this.mob.getNavigation().stop();
        }

        if (this.targetEntity != null && this.mob.hasLineOfSight(this.targetEntity) && (double)this.mob.getBbWidth() > 2.0 && this.mob.onGround()) {
            this.mob.getMoveControl().setWantedPosition(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1.0);
        }

        if (this.targetEntity != null && this.targetEntity.isAlive() && this.mob.distanceToSqr(this.targetEntity) < 6 && !brainiac.hasBarrel()) {
            if (brainiac.getAnimation() == BrainiacEntity.NO_ANIMATION) {
                brainiac.setAnimation(BrainiacEntity.ANIMATION_BITE);
            }
            if (brainiac.getAnimation() == BrainiacEntity.ANIMATION_BITE && brainiac.getAnimationTick() >= 8 && brainiac.getAnimationTick() <= 15) {
                brainiac.setHasBarrel(true);
                targetEntity.kill();
                this.stop();
            }
        }

    }
}
