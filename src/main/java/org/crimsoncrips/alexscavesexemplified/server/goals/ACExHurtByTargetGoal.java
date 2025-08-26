package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.entity.living.GingerbreadManEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class ACExHurtByTargetGoal extends HurtByTargetGoal {

    public ACExHurtByTargetGoal(GingerbreadManEntity gingerbreadManEntity) {
        super(gingerbreadManEntity);
        this.setAlertOthers(GingerbreadManEntity.class);
    }

    protected void alertOther(Mob mob, LivingEntity target) {
        if (mob instanceof GingerbreadManEntity gingerbreadMan && this.mob.hasLineOfSight(target)) {
            if (gingerbreadMan.isOvenSpawned())
                return;
            mob.setTarget(target);
        }

    }
}
