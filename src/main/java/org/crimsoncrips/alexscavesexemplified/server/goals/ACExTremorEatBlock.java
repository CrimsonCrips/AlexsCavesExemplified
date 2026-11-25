package org.crimsoncrips.alexscavesexemplified.server.goals;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.DinosaurChopBlock;
import com.github.alexmodguy.alexscaves.server.block.ThinBoneBlock;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExBlockTagGenerator;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;

public class ACExTremorEatBlock extends MoveToBlockGoal {

    TremorsaurusEntity tremorsaurus;

    public ACExTremorEatBlock(TremorsaurusEntity pMob, double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
        super(pMob, pSpeedModifier, pSearchRange,pVerticalSearchRange);
        tremorsaurus = pMob;
    }

    @Override
    public void tick() {
        super.tick();
        Level level = tremorsaurus.level();

        tremorsaurus.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(blockPos));
        for (VallumraptorEntity vallumraptor : level.getEntitiesOfClass(VallumraptorEntity.class, new AABB(Vec3.atLowerCornerOf(blockPos.offset(-3, -3, -3)), Vec3.atLowerCornerOf(blockPos.offset(3, 3, 3))))) {
            if (tremorsaurus.distanceToSqr(this.mob.position()) < 10){
                tremorsaurus.tryRoar();
            }
        }
        if (this.isReachedTarget()) {
            tremorsaurus.getNavigation().stop();
            tremorsaurus.setInSittingPose(true);
            if (tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION && tremorsaurus.isInSittingPose() && !ACExData.get(tremorsaurus, ACExAttachments.SNIFFED)) {
                tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_SNIFF);
            }

            if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_SNIFF && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15) {
                if (isValidTarget(level, blockPos)){
                    ACExData.set(tremorsaurus, ACExAttachments.SNIFFED, true);
                } else this.stop();
            }

            if (ACExData.get(tremorsaurus, ACExAttachments.SNIFFED) && tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION && tremorsaurus.isInSittingPose()){
                tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_BITE);
            }

            if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_BITE && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15){
                if (isValidTarget(level, blockPos)){
                    tremorsaurus.heal(4);
                    tremorsaurus.playSound(ACSoundRegistry.TREMORSAURUS_BITE.get(), 1F, 1F);
                    BlockState chop = level.getBlockState(blockPos);
                    level.destroyBlock(blockPos, false);
                    if (chop.getBlock() instanceof DinosaurChopBlock) {
                        level.setBlock(blockPos, ACBlockRegistry.THIN_BONE.get().defaultBlockState().setValue(ThinBoneBlock.AXIS, chop.getValue(DinosaurChopBlock.FACING).getAxis()), 3);
                    }

                    if (AlexsCavesExemplified.COMMON_CONFIG.SEETHED_TAMING_ENABLED.get() && level.getRandom().nextDouble() < 0.45) {
                        mob.addEffect(new MobEffectInstance(ACExEffects.SERENED.get(), ACExUtils.seconds(120.0F), 0));
                    }
                }
                this.stop();
            }
        }

    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        BlockState blockState = worldIn.getBlockState(pos);
        return blockState.is(ACExBlockTagGenerator.DINO_SCAVENGE);
    }

    public void stop() {
        super.stop();
        ACExData.set(tremorsaurus, ACExAttachments.SNIFFED, false);
        tremorsaurus.setInSittingPose(false);
        this.blockPos = BlockPos.ZERO;
    }

    public double acceptedDistance() {
        return 4F;
    }

    protected int nextStartTick(PathfinderMob mob) {
        return reducedTickDelay(ACExUtils.seconds(5.0F) + tremorsaurus.getRandom().nextInt(ACExUtils.seconds(5.0F)));
    }
}
