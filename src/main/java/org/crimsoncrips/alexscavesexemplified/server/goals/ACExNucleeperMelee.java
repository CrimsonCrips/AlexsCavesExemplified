package org.crimsoncrips.alexscavesexemplified.server.goals;

import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class ACExNucleeperMelee extends Goal {

    NucleeperEntity nucleeper;
    public ACExNucleeperMelee(NucleeperEntity nucleeper) {
        this.nucleeper = nucleeper;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        LivingEntity target = nucleeper.getTarget();
        return target != null && target.isAlive() && !nucleeper.getData(ACExAttachments.DEFUSED);
    }

    public void tick() {
        LivingEntity target = nucleeper.getTarget();
        if (target != null && target.isAlive()) {
            nucleeper.getNavigation().moveTo(target, (double)1.0F);
            if (nucleeper.distanceTo(target) < 3.5F + target.getBbWidth()) {
                nucleeper.setTriggered(true);
            }
        }

    }

}