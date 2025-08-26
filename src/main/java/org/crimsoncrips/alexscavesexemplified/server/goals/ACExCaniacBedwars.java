//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.entity.living.CaniacEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ACExCaniacBedwars extends MoveToBlockGoal {
    private final CaniacEntity caniac;

    public ACExCaniacBedwars(CaniacEntity caniac, int range) {
        super(caniac, 2.0F, range, range);
        this.caniac = caniac;
    }

    protected int nextStartTick(PathfinderMob mob) {
        return reducedTickDelay(200 + this.caniac.getRandom().nextInt(500));
    }

    public double acceptedDistance() {
        return (double)2.0F;
    }

    public void tick() {
        super.tick();
        BlockPos target = this.getMoveToTarget();

        caniac.setArmSpinSpeed(50);
        this.caniac.lookAt(Anchor.EYES, Vec3.atCenterOf(target));
        BlockState blockState = this.caniac.level().getBlockState(target);

        if (this.isReachedTarget() && blockState.getBlock() instanceof BedBlock) {
            caniac.level().destroyBlock(blockPos,  true);
            stop();
        }

    }

    @Override
    public void stop() {
        caniac.setArmSpinSpeed(0);
        super.stop();
    }

    @Override
    public void start() {
        caniac.playSound(ACSoundRegistry.CANIAC_HURT.get(), 2.0F, 0.0F);
        caniac.setArmSpinSpeed(0);
        super.start();
    }

    protected BlockPos getMoveToTarget() {
        return this.blockPos;
    }

    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        BlockState blockState = worldIn.getBlockState(pos);
        return pos != null && blockState.getBlock() instanceof BedBlock;
    }
}
