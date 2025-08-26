package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import net.minecraft.world.entity.LivingEntity;

public class ACExSelfDestruct extends ACExFollowNearestGoal {



    public ACExSelfDestruct(NotorEntity mob) {
        super(mob, LivingEntity.class, 15, 1.3,living -> {
            return mob.getLastHurtByMob() == living;
        });
    }

    @Override
    public boolean canUse() {
        return super.canUse() && mob.getLastHurtByMob() instanceof LivingEntity;
    }

    @Override
    public void tick() {
        if (getTarget() != null) {
            mob.setLastHurtByMob(getTarget());
        }
        var target = getTarget();
        if (target != null && !mob.isLeashed()) {
            mob.getLookControl().setLookAt(target, 10.0F, (float) this.mob.getMaxHeadXRot());
            navigation.moveTo(target, speedModifier);
        } else {
            navigation.stop();
        }
    }

}