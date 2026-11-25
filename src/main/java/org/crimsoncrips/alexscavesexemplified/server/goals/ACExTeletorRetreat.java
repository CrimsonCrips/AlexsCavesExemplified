package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.entity.living.TeletorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ACExTeletorRetreat extends Goal {

    private static final double PANIC_RANGE = 16.0D;
    private static final double SAFE_RANGE = 24.0D;
    private static final double FLEE_DISTANCE = 10.0D;
    private static final double FLEE_SPEED = 1.4D;
    private static final int REPATH_INTERVAL = ACExUtils.seconds(0.5F);
    private static final int ATTEMPTS = 8;

    private final TeletorEntity teletor;
    private Vec3 fleeTo;
    private int repathTime;

    public ACExTeletorRetreat(TeletorEntity teletor) {
        this.teletor = teletor;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity threat = this.teletor.getTarget();
        return this.teletor.getWeapon() == null && threat != null && threat.isAlive() && this.teletor.distanceTo(threat) < PANIC_RANGE;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity threat = this.teletor.getTarget();
        return this.teletor.getWeapon() == null && threat != null && threat.isAlive() && this.teletor.distanceTo(threat) < SAFE_RANGE;
    }

    @Override
    public void start() {
        this.fleeTo = null;
        this.repathTime = 0;
    }

    @Override
    public void stop() {
        this.fleeTo = null;
        this.teletor.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity threat = this.teletor.getTarget();
        if (threat == null)
            return;
        if (--this.repathTime <= 0 || this.fleeTo == null || this.teletor.position().distanceToSqr(this.fleeTo) < 4.0D) {
            this.repathTime = REPATH_INTERVAL;
            Vec3 found = this.findFleePosition(threat);
            if (found != null) {
                this.fleeTo = found;
            }
        }
        if (this.fleeTo != null) {
            this.teletor.getNavigation().stop();
            this.teletor.getMoveControl().setWantedPosition(this.fleeTo.x, this.fleeTo.y, this.fleeTo.z, FLEE_SPEED);
            this.teletor.getLookControl().setLookAt(this.fleeTo.x, this.fleeTo.y, this.fleeTo.z);
        }
    }

    @Nullable
    private Vec3 findFleePosition(LivingEntity threat) {
        Vec3 away = this.teletor.position().subtract(threat.position());
        Vec3 direction = away.horizontalDistanceSqr() < 1.0E-4D ? new Vec3(this.teletor.getRandom().nextDouble() - 0.5D, 0.0D, this.teletor.getRandom().nextDouble() - 0.5D).normalize() : new Vec3(away.x, 0.0D, away.z).normalize();
        for (int i = 0; i < ATTEMPTS; i++) {
            double spread = (this.teletor.getRandom().nextDouble() - 0.5D) * (Math.PI / 2.0D) * (1.0D + i * 0.25D);
            Vec3 candidate = this.teletor.position().add(direction.yRot((float) spread).scale(FLEE_DISTANCE)).add(0.0D, this.teletor.getRandom().nextInt(5) - 1, 0.0D);
            BlockPos pos = BlockPos.containing(candidate);
            if (this.teletor.level().isEmptyBlock(pos) && this.teletor.level().isEmptyBlock(pos.above())) {
                return Vec3.atCenterOf(pos);
            }
        }
        return null;
    }
}
