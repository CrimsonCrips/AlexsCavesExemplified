package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.ai.HullbreakerInspectMobGoal;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.HullbreakerEntity;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;


@Mixin(HullbreakerInspectMobGoal.class)
public abstract class ACEHullbreakerGoalMixin extends Goal {

    @Shadow private LivingEntity inspectingTarget;

    @Shadow private HullbreakerEntity entity;

    @Shadow private int phaseTime;

    @Shadow private int maxPhaseTime;

    @Shadow private boolean staring;

    @Shadow private Vec3 startCirclingAt;

    @Shadow public abstract boolean isPreyWatching();

    @Shadow public abstract Vec3 orbitAroundPos(Vec3 target, float circleDistance);

    @Shadow private boolean clockwise;

    private static final Predicate<LivingEntity> NEW_TARGETTING = (mob) -> {
        return HullbreakerEntity.GLOWING_TARGET.test(mob) || CuriosCompat.hasLight(mob);
    };


    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        ci.cancel();
        double distance = (double)this.entity.distanceTo(this.inspectingTarget);
        if (this.entity.getInterestLevel() >= 5 && NEW_TARGETTING.test(this.inspectingTarget)) {
            if (!(this.inspectingTarget instanceof Player) || !((Player)this.inspectingTarget).isCreative()) {
                this.entity.setTarget(this.inspectingTarget);
            }

            this.inspectingTarget = null;
        } else {
            if (this.entity.getRandom().nextInt(20) == 0 && ! NEW_TARGETTING.test(this.inspectingTarget)) {
                this.inspectingTarget = null;
            } else {
                if (this.entity.getAnimation() == HullbreakerEntity.ANIMATION_PUZZLE && this.entity.getAnimationTick() > 50) {
                    this.phaseTime = this.maxPhaseTime;
                }

                if (this.phaseTime++ > this.maxPhaseTime) {
                    this.entity.setInterestLevel(this.entity.getInterestLevel() + 1);
                    this.staring = this.entity.getRandom().nextBoolean() && !this.staring;
                    this.phaseTime = 0;
                    this.startCirclingAt = this.inspectingTarget.getEyePosition();
                    this.maxPhaseTime = this.staring ? 120 : 120 + 80 * Math.min(0, 5 - this.entity.getInterestLevel());
                }

                if (this.staring) {
                    this.entity.lookAt(EntityAnchorArgument.Anchor.EYES, this.inspectingTarget.getEyePosition());
                    if (this.isPreyWatching() && distance < (double)18.0F) {
                        if (this.entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                            this.entity.setAnimation(HullbreakerEntity.ANIMATION_PUZZLE);
                        }

                        this.entity.getNavigation().stop();
                    } else if (this.entity.getNavigation().isDone()) {
                        Vec3 frontVision = this.inspectingTarget.getEyePosition().add(this.inspectingTarget.getLookAngle().scale((double)12.0F));
                        this.entity.getNavigation().moveTo(frontVision.x, frontVision.y, frontVision.z, (double)1.0F);
                    }
                } else {
                    if (this.startCirclingAt == null) {
                        this.startCirclingAt = this.inspectingTarget.getEyePosition();
                    }

                    Vec3 circle = this.orbitAroundPos(this.inspectingTarget.getEyePosition(), (float)(12 + Math.min(0, 5 - this.entity.getInterestLevel()) * 3));
                    this.entity.getNavigation().moveTo(circle.x, circle.y, circle.z, (double)1.4F);
                    this.entity.setYHeadRot(this.entity.yBodyRot + (float)(this.clockwise ? 30 : -30));
                }

                SubmarineEntity.alertSubmarineMountOf(this.inspectingTarget);
            }

        }
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        long worldTime = this.entity.level().getGameTime() % 10L;
        if (this.entity.getRandom().nextInt(60) == 0 || worldTime == 0L || target != null && target.isAlive()) {
            AABB aabb = this.entity.getBoundingBox().inflate((double)80.0F);
            List<LivingEntity> list = this.entity.level().getEntitiesOfClass(LivingEntity.class, aabb, NEW_TARGETTING);
            if (list.isEmpty()) {
                return false;
            } else {
                LivingEntity closest = null;

                for(LivingEntity mob : list) {
                    if ((closest == null || mob.distanceToSqr(this.entity) < closest.distanceToSqr(this.entity)) && this.entity.hasLineOfSight(mob) && !mob.is(this.entity)) {
                        closest = mob;
                    }
                }

                this.inspectingTarget = closest;
                return this.inspectingTarget != null;
            }
        } else {
            return false;
        }
    }
}
