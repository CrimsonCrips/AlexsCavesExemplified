package org.crimsoncrips.alexscavesexemplified.server.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.function.Predicate;

public class ACExMobTargetClosePlayers extends NearestAttackableTargetGoal<Player> {

    private Mob mob;
    private float range;

    public ACExMobTargetClosePlayers(Mob mob, int chance, float range, Predicate<LivingEntity> pTargetPredicate) {
        super(mob, Player.class, chance, true, true, pTargetPredicate);
        this.mob = mob;
        this.range = range;
    }

    public boolean canUse() {
        return !this.mob.isBaby() && (!(this.mob instanceof TamableAnimal) || !((TamableAnimal)this.mob).isTame()) ? super.canUse() : false;
    }

    protected double getFollowDistance() {
        return (double)this.range;
    }

    protected AABB getTargetSearchArea(double rangeIn) {
        return this.mob.getBoundingBox().inflate((double)this.range, (double)this.range, (double)this.range);
    }
}
