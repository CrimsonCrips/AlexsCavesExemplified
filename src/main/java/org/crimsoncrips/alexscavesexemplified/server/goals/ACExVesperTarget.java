//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.phys.AABB;

public class ACExVesperTarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private VesperEntity entity;
    private final float flyingRange;

    public ACExVesperTarget(VesperEntity mob, float flyingRange, Class<T> clzz, Predicate<LivingEntity> pTargetPredicate) {
        super(mob, clzz, 10, true, true, pTargetPredicate);
        this.entity = mob;
        this.flyingRange = flyingRange;
    }

    protected AABB getTargetSearchArea(double distance) {
        if (this.entity.isHanging()) {
            AABB aabb = this.entity.getBoundingBox();
            double newDistance = 2.0;
            return new AABB(aabb.minX - newDistance, (double)(this.entity.level().getMinBuildHeight() - 5), aabb.minZ - newDistance, aabb.maxX + newDistance, aabb.maxY + 1.0, aabb.maxZ + newDistance);
        } else {
            return this.entity.getBoundingBox().inflate((double)this.flyingRange, (double)this.flyingRange, (double)this.flyingRange);
        }
    }
}
