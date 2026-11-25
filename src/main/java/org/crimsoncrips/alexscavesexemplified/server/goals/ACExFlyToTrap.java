package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;

public class ACExFlyToTrap extends MoveToBlockGoal {

    private static final int SEARCH_INTERVAL = ACExUtils.seconds(2.0F);

    EntityFly fly;
    private boolean reachedTarget;

    public ACExFlyToTrap(EntityFly pMob, double pSpeedModifier, int pSearchRange) {
        super(pMob, pSpeedModifier, pSearchRange,6);
        fly = pMob;
    }

    @Override
    public void tick() {
        BlockPos flytrap = this.getMoveToTarget();
        reachedTarget = flytrap.closerToCenterThan(fly.position(), this.acceptedDistance());

        Level level = fly.level();
        BlockState blockState = level.getBlockState(blockPos);

        fly.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(blockPos));

        if (this.shouldRecalculatePath()) {
            this.mob.getNavigation().moveTo((double)((float)blockPos.getX()) + 0.5D, (double)blockPos.getY(), (double)((float)blockPos.getZ()) + 0.5D, this.speedModifier);
        }

        if (this.isReachedTarget() && blockState.is(ACBlockRegistry.FLYTRAP.get()) && blockState.getValue(flytrapOpen())) {
            fly.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            fly.captureDrops();
            fly.kill();
            fly.level().setBlock(blockPos, blockState.setValue(flytrapOpen(), false), 2);
        }

    }

    protected boolean isReachedTarget() {
        return this.reachedTarget;
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        BlockState blockState = worldIn.getBlockState(pos);
        return blockState.is(ACBlockRegistry.FLYTRAP.get()) && blockState.getValue(flytrapOpen());
    }

    public double acceptedDistance() {
        return 2F;
    }

    private static BooleanProperty flytrapOpen() {
        return (BooleanProperty) ACBlockRegistry.FLYTRAP.get().getStateDefinition().getProperty("open");
    }

    protected int nextStartTick(PathfinderMob mob) {
        return reducedTickDelay(SEARCH_INTERVAL + mob.getRandom().nextInt(SEARCH_INTERVAL));
    }
}
