package org.crimsoncrips.alexscavesexemplified.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.HullbreakerEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.DeepOneReaction;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ACExSubmarineHunt {

    public static final double MAX_SUBMARINE_SPEED = 0.65D;
    public static final double SPEEDING_RATIO = 0.8D;
    public static final double HUNT_RANGE = 32.0D;
    public static final double FORGET_RANGE = 64.0D;
    private static final int FILL_TICKS = ACExUtils.seconds(3.0F);
    private static final int DRAIN_TICKS = ACExUtils.seconds(2.5F);
    private static final int DANGER_ALERT_TICKS = ACExUtils.seconds(5.0F);
    private static final int DEEP_ONE_ATTACK_COOLDOWN = ACExUtils.seconds(1.5F);
    private static final float HULLBREAKER_SUBMARINE_DAMAGE = 0.5F;

    public static boolean enabled() {
        return AlexsCavesExemplified.COMMON_CONFIG.SUBMARINE_CHANGES_ENABLED.get();
    }

    public static boolean isHunter(Entity entity) {
        return entity instanceof HullbreakerEntity || entity instanceof DeepOneBaseEntity deepOne && !deepOne.isSummoned();
    }

    public static float getAlert(Entity hunter) {
        return ACExData.get(hunter, ACExAttachments.SUB_ALERT);
    }

    public static int getAlertTarget(Entity hunter) {
        return ACExData.get(hunter, ACExAttachments.SUB_ALERT_TARGET);
    }

    public static boolean isSpeeding(SubmarineEntity submarine) {
        return ((ACExTrackedSubmarine) submarine).getTrackedSpeed() >= MAX_SUBMARINE_SPEED * SPEEDING_RATIO;
    }

    @Nullable
    public static SubmarineEntity huntedSubmarine(Mob hunter) {
        if (getAlert(hunter) < 1.0F || !(hunter.level().getEntity(getAlertTarget(hunter)) instanceof SubmarineEntity submarine) || !submarine.isAlive() || hunter.distanceTo(submarine) > FORGET_RANGE)
            return null;
        return submarine;
    }

    public static void tick(Mob hunter) {
        if (hunter.level().isClientSide)
            return;
        float alert = getAlert(hunter);
        if (!enabled() || !isHunter(hunter) || !hunter.isAlive()) {
            if (alert > 0.0F) {
                set(hunter, 0.0F, -1);
            }
            return;
        }
        if (huntedSubmarine(hunter) != null)
            return;
        SubmarineEntity speeding = null;
        double closest = Double.MAX_VALUE;
        for (SubmarineEntity submarine : hunter.level().getEntitiesOfClass(SubmarineEntity.class, hunter.getBoundingBox().inflate(HUNT_RANGE))) {
            double distance = hunter.distanceToSqr(submarine);
            if (distance < closest && isSpeeding(submarine) && provokedBy(hunter, submarine) && hunter.hasLineOfSight(submarine)) {
                speeding = submarine;
                closest = distance;
            }
        }
        if (speeding != null) {
            alert = Math.min(1.0F, alert + 1.0F / FILL_TICKS);
            set(hunter, alert, speeding.getId());
            if (alert >= 1.0F) {
                hunter.playSound(hunter instanceof HullbreakerEntity ? ACSoundRegistry.HULLBREAKER_ATTACK.get() : ACSoundRegistry.DEEP_ONE_HOSTILE.get(), 3.0F, 1.0F);
            }
        } else if (alert > 0.0F) {
            alert = Math.max(0.0F, alert - 1.0F / DRAIN_TICKS);
            set(hunter, alert, alert > 0.0F ? getAlertTarget(hunter) : -1);
        }
    }

    private static boolean provokedBy(Mob hunter, SubmarineEntity submarine) {
        Player driver = ACExSubmarineRemote.driverOf(submarine);
        if (driver == null || driver.isCreative() || driver.isSpectator())
            return false;
        return !(hunter instanceof DeepOneBaseEntity deepOne) || deepOne.getReactionTo(driver) != DeepOneReaction.HELPFUL;
    }

    private static void set(Mob hunter, float alert, int targetId) {
        ACExAttachments.set(hunter, ACExAttachments.SUB_ALERT, alert);
        ACExAttachments.set(hunter, ACExAttachments.SUB_ALERT_TARGET, targetId);
    }

    public static class HuntGoal extends Goal {
        private final Mob hunter;
        private int attackCooldown;

        public HuntGoal(Mob hunter) {
            this.hunter = hunter;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return enabled() && huntedSubmarine(this.hunter) != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void stop() {
            this.hunter.getNavigation().stop();
        }

        @Override
        public void tick() {
            SubmarineEntity submarine = huntedSubmarine(this.hunter);
            if (submarine == null)
                return;
            submarine.setDangerAlertTicks(DANGER_ALERT_TICKS);
            double distance = this.hunter.distanceTo(submarine);
            float reach = this.hunter.getBbWidth() + submarine.getBbWidth();
            this.hunter.lookAt(EntityAnchorArgument.Anchor.EYES, submarine.getBoundingBox().getCenter());
            if (distance > reach + 1.0F) {
                this.hunter.getNavigation().moveTo(submarine, this.hunter instanceof HullbreakerEntity ? 1.6D : 1.2D);
            }
            if (this.hunter instanceof HullbreakerEntity hullbreaker) {
                this.tickHullbreaker(hullbreaker, submarine, distance, reach);
            } else {
                this.tickDeepOne(submarine, distance, reach);
            }
        }

        private void tickHullbreaker(HullbreakerEntity hullbreaker, SubmarineEntity submarine, double distance, float reach) {
            if (distance < reach + 7.0F && hullbreaker.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                Animation animation = hullbreaker.getRandom().nextBoolean() ? HullbreakerEntity.ANIMATION_BITE : HullbreakerEntity.ANIMATION_BASH;
                hullbreaker.setAnimation(animation);
                if (hullbreaker.isInWaterOrBubble()) {
                    hullbreaker.playSound(ACSoundRegistry.HULLBREAKER_ATTACK.get());
                }
            }
            boolean biting = hullbreaker.getAnimation() == HullbreakerEntity.ANIMATION_BITE && hullbreaker.getAnimationTick() > 10 && hullbreaker.getAnimationTick() <= 14;
            boolean bashing = hullbreaker.getAnimation() == HullbreakerEntity.ANIMATION_BASH && hullbreaker.getAnimationTick() > 10 && hullbreaker.getAnimationTick() <= 12;
            if ((biting || bashing) && distance < reach + 5.0F) {
                float multiplier = bashing ? 1.5F : 1.0F;
                float damage = (float) hullbreaker.getAttributeValue(Attributes.ATTACK_DAMAGE) * multiplier * HULLBREAKER_SUBMARINE_DAMAGE;
                submarine.hurt(hullbreaker.damageSources().mobAttack(hullbreaker), damage);
                submarine.setDeltaMovement(submarine.getDeltaMovement().add(submarine.position().subtract(hullbreaker.position()).normalize().scale(0.4D * multiplier)));
            }
        }

        private void tickDeepOne(SubmarineEntity submarine, double distance, float reach) {
            if (this.attackCooldown > 0) {
                this.attackCooldown--;
            }
            if (distance <= reach + 1.0F && this.attackCooldown <= 0) {
                this.attackCooldown = DEEP_ONE_ATTACK_COOLDOWN;
                this.hunter.swing(InteractionHand.MAIN_HAND);
                this.hunter.playSound(ACSoundRegistry.DEEP_ONE_ATTACK.get());
                submarine.hurt(this.hunter.damageSources().mobAttack(this.hunter), (float) this.hunter.getAttributeValue(Attributes.ATTACK_DAMAGE));
            }
        }
    }
}
