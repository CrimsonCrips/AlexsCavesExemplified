package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.TremorConsumption;

public class ACETremorEatBlock extends MoveToBlockGoal {

    TremorsaurusEntity tremorsaurus;

    public ACETremorEatBlock(TremorsaurusEntity pMob, double pSpeedModifier, int pSearchRange,int pVerticalSearchRange) {
        super(pMob, pSpeedModifier, pSearchRange,pVerticalSearchRange);
        tremorsaurus = pMob;
    }

    @Override
    public void tick() {
        super.tick();
        TremorConsumption tickAccesor = (TremorConsumption)(Object)tremorsaurus;

        tremorsaurus.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(blockPos));
        for (VallumraptorEntity vallumraptor : tremorsaurus.level().getEntitiesOfClass(VallumraptorEntity.class, new AABB(blockPos.offset(-3, -3, -3), blockPos.offset(3, 3, 3)))) {
            if (tremorsaurus.distanceToSqr(this.mob.position()) < 10){
                tremorsaurus.tryRoar();
            }
        }
        if (this.isReachedTarget()) {
            tremorsaurus.getNavigation().stop();
            tremorsaurus.setInSittingPose(true);
            if (tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION && tremorsaurus.isInSittingPose() && !tickAccesor.isSniffed()) {
                tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_SNIFF);
            }

            if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_SNIFF && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15) {
                if (isValidTarget(tremorsaurus.level(), blockPos)){
                    tickAccesor.setSniffed(true);
                } else this.stop();
            }

            if (tickAccesor.isSniffed() && tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION && tremorsaurus.isInSittingPose()){
                tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_BITE);
            }

            if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_BITE && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15){
                if (isValidTarget(tremorsaurus.level(), blockPos)){
                    tremorsaurus.heal(4);
                    tremorsaurus.level().destroyBlock(blockPos, false);
                    tremorsaurus.playSound(ACSoundRegistry.TREMORSAURUS_BITE.get(), 1F, 1F);
                    if (AlexsCavesExemplified.COMMON_CONFIG.SEETHED_TAMING_ENABLED.get() && tremorsaurus.level().getRandom().nextDouble() < 0.09) {
                        tickAccesor.setSeethed(true);
                    }
                }
                this.stop();
            }
        }

    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        BlockState blockState = worldIn.getBlockState(pos);
        return blockState.is(ACBlockRegistry.DINOSAUR_CHOP.get()) || blockState.is(ACBlockRegistry.COOKED_DINOSAUR_CHOP.get());
    }

    public void stop() {
        super.stop();
        ((TremorConsumption)tremorsaurus).setSniffed(false);
        tremorsaurus.setInSittingPose(false);
        this.blockPos = BlockPos.ZERO;
    }

    public double acceptedDistance() {
        return 4F;
    }

    protected int nextStartTick(PathfinderMob mob) {
        return reducedTickDelay(100 + tremorsaurus.getRandom().nextInt(100));
    }
}
