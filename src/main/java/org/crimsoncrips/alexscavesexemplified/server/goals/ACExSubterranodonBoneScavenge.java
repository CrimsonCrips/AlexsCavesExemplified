package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ACExSubterranodonBoneScavenge extends Goal {
    private static final int SEARCH_RANGE = 16;
    private static final int SEARCH_HEIGHT = 8;
    private static final double EAT_DISTANCE = 1.5D;
    private static final int SEARCH_COOLDOWN = ACExUtils.seconds(5.0F);
    private static final int GIVE_UP_TICKS = ACExUtils.seconds(15.0F);
    private static final int BITE_TICKS = 7;
    private static final float HEAL = 2.0F;

    private final SubterranodonEntity subterranodon;
    @Nullable
    private BlockPos bonePos;
    private int cooldown;
    private int ticks;
    private int biteTicks;

    public ACExSubterranodonBoneScavenge(SubterranodonEntity subterranodon) {
        this.subterranodon = subterranodon;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        this.cooldown = reducedTickDelay(SEARCH_COOLDOWN + this.subterranodon.getRandom().nextInt(SEARCH_COOLDOWN));
        if (!this.canScavenge())
            return false;
        this.bonePos = this.findBone();
        return this.bonePos != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.bonePos != null && this.ticks < GIVE_UP_TICKS && this.canScavenge() && this.isBone(this.bonePos);
    }

    @Override
    public void start() {
        this.ticks = 0;
        this.biteTicks = 0;
    }

    @Override
    public void stop() {
        this.bonePos = null;
        this.subterranodon.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.bonePos == null)
            return;
        this.ticks++;
        Vec3 bone = Vec3.atCenterOf(this.bonePos);
        this.subterranodon.getLookControl().setLookAt(bone.x, bone.y, bone.z);
        double reach = EAT_DISTANCE + this.subterranodon.getBbWidth();
        if (this.subterranodon.distanceToSqr(bone) > reach * reach) {
            this.biteTicks = 0;
            if (this.subterranodon.isFlying()) {
                this.subterranodon.getMoveControl().setWantedPosition(bone.x, bone.y + 0.5D, bone.z, 1.0D);
            } else {
                this.subterranodon.getNavigation().moveTo(bone.x, bone.y, bone.z, 1.0D);
            }
            return;
        }
        this.subterranodon.getNavigation().stop();
        if (this.subterranodon.isFlying() && this.subterranodon.onGround()) {
            this.subterranodon.setFlying(false);
        }
        if (this.biteTicks == 0) {
            this.subterranodon.doHurtTarget(this.subterranodon);
        }
        if (++this.biteTicks >= BITE_TICKS) {
            this.subterranodon.level().destroyBlock(this.bonePos, false);
            this.subterranodon.heal(HEAL);
            this.subterranodon.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            this.bonePos = null;
        }
    }

    private boolean canScavenge() {
        LivingEntity target = this.subterranodon.getTarget();
        return !this.subterranodon.isBaby() && !this.subterranodon.isVehicle() && !this.subterranodon.isPassenger() && !this.subterranodon.isDancing() && !this.subterranodon.isInSittingPose() && (target == null || !target.isAlive());
    }

    private boolean isBone(BlockPos pos) {
        BlockState state = this.subterranodon.level().getBlockState(pos);
        return state.is(ACBlockRegistry.THIN_BONE.get()) && !state.getValue(BlockStateProperties.WATERLOGGED);
    }

    @Nullable
    private BlockPos findBone() {
        BlockPos origin = this.subterranodon.blockPosition();
        BlockPos closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-SEARCH_RANGE, -SEARCH_HEIGHT, -SEARCH_RANGE), origin.offset(SEARCH_RANGE, SEARCH_HEIGHT, SEARCH_RANGE))) {
            if (this.isBone(pos)) {
                double distance = pos.distSqr(origin);
                if (distance < closestDistance) {
                    closest = pos.immutable();
                    closestDistance = distance;
                }
            }
        }
        return closest;
    }
}
