package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.DinosaurChopBlock;
import com.github.alexmodguy.alexscaves.server.block.ThinBoneBlock;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static com.github.alexmodguy.alexscaves.server.block.DinosaurChopBlock.BITES;

public class ACEVallumraptorEatBlock extends MoveToBlockGoal {
    VallumraptorEntity vallumraptor;

    public ACEVallumraptorEatBlock(VallumraptorEntity pMob, double pSpeedModifier, int pSearchRange) {
        super(pMob, pSpeedModifier, pSearchRange);
        vallumraptor = pMob;
    }

    @Override
    public void tick() {
        super.tick();

        for (TremorsaurusEntity tremorsaurus : vallumraptor.level().getEntitiesOfClass(TremorsaurusEntity.class, new AABB(blockPos.offset(-5, -5, -5), blockPos.offset(5, 5, 5)))) {
            stop();
        }
        vallumraptor.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(blockPos));
        if (this.isReachedTarget()) {
            vallumraptor.getNavigation().stop();

            if (vallumraptor.getAnimation() == VallumraptorEntity.NO_ANIMATION) {
                vallumraptor.setAnimation(VallumraptorEntity.ANIMATION_MELEE_BITE);
            }

            if (vallumraptor.getAnimation() == VallumraptorEntity.ANIMATION_MELEE_BITE && vallumraptor.getAnimationTick() >= 10 && vallumraptor.getAnimationTick() <= 15) {
                if (isValidTarget(vallumraptor.level(), blockPos)) {
                    BlockState blockState = vallumraptor.level().getBlockState(blockPos);
                    vallumraptor.heal(1);
                    int i = blockState.getValue(BITES);
                    vallumraptor.level().destroyBlock(blockPos, false);
                    if (i < 3) {
                        vallumraptor.level().setBlock(blockPos, blockState.setValue(BITES, i + 1), 3);
                    } else {
                        vallumraptor.level().setBlock(blockPos, (ACBlockRegistry.THIN_BONE.get()).defaultBlockState().setValue(ThinBoneBlock.AXIS, blockState.getValue(DinosaurChopBlock.FACING).getAxis()), 4);
                    }
                    vallumraptor.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                }
                stop();
            }
        }
    }
    public void stop() {
        super.stop();
        this.blockPos = BlockPos.ZERO;
    }
    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        BlockState blockState = worldIn.getBlockState(pos);
        return blockState.is(ACBlockRegistry.DINOSAUR_CHOP.get()) || blockState.is(ACBlockRegistry.COOKED_DINOSAUR_CHOP.get());
    }

    public double acceptedDistance() {
        return 3F;
    }

    @Override
    protected int nextStartTick(PathfinderMob pCreature) {
        return reducedTickDelay(100 + vallumraptor.getRandom().nextInt(100));
    }
}
