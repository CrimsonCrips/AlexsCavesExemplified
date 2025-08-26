//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.block.NuclearBombBlock;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.CaniacEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class ACExCaniacExplode extends MoveToBlockGoal {
    private final CaniacEntity caniac;

    public ACExCaniacExplode(CaniacEntity caniac, int range) {
        super(caniac, 2.0F, range, range);
        this.caniac = caniac;
    }

    protected int nextStartTick(PathfinderMob mob) {
        return reducedTickDelay(1000 + this.caniac.getRandom().nextInt(500));
    }

    public double acceptedDistance() {
        return (double)2.0F;
    }

    public void tick() {
        super.tick();
        BlockPos target = this.getMoveToTarget();
        Level level = this.caniac.level();

        caniac.setArmSpinSpeed(50);
        this.caniac.lookAt(Anchor.EYES, Vec3.atCenterOf(target));
        BlockState blockState = this.caniac.level().getBlockState(target);

        if (this.isReachedTarget() && blockState.getBlock() instanceof TntBlock) {
            PrimedTnt primedtnt = new PrimedTnt(level, (double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, caniac);
            level.addFreshEntity(primedtnt);
            level.playSound((Player)null, primedtnt.getX(), primedtnt.getY(), primedtnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(caniac, GameEvent.PRIME_FUSE, blockPos);
            caniac.level().destroyBlock(blockPos,  true);
            stop();
        } else if (this.isReachedTarget() && blockState.getBlock() instanceof NuclearBombBlock) {
            NuclearBombEntity bomb = (NuclearBombEntity)((EntityType) ACEntityRegistry.NUCLEAR_BOMB.get()).create(level);
            bomb.setPos((double)blockPos.getX() + (double)0.5F, (double)blockPos.getY(), (double)blockPos.getZ() + (double)0.5F);
            level.addFreshEntity(bomb);
            level.playSound((Player)null, bomb.getX(), bomb.getY(), bomb.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(caniac, GameEvent.PRIME_FUSE, blockPos);
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
        return blockState.getBlock() instanceof TntBlock || blockState.getBlock() instanceof NuclearBombBlock;
    }
}
