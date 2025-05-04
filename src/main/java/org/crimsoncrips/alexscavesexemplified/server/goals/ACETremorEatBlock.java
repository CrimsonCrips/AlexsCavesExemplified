package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import net.thevaliantsquidward.cavedelight.item.ModItems;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.CaveDelightCompat;
import org.crimsoncrips.alexscavesexemplified.compat.FarmersDelightCompat;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACEBlockTagGenerator;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.TremorConsumption;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;

public class ACETremorEatBlock extends MoveToBlockGoal {

    TremorsaurusEntity tremorsaurus;

    public ACETremorEatBlock(TremorsaurusEntity pMob, double pSpeedModifier, int pSearchRange,int pVerticalSearchRange) {
        super(pMob, pSpeedModifier, pSearchRange,pVerticalSearchRange);
        tremorsaurus = pMob;
    }

    @Override
    public void tick() {
        super.tick();
        TremorConsumption tickAccesor = (TremorConsumption)tremorsaurus;
        Level level = tremorsaurus.level();

        tremorsaurus.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(blockPos));
        for (VallumraptorEntity vallumraptor : level.getEntitiesOfClass(VallumraptorEntity.class, new AABB(blockPos.offset(-3, -3, -3), blockPos.offset(3, 3, 3)))) {
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
                if (isValidTarget(level, blockPos)){
                    tickAccesor.setSniffed(true);
                } else this.stop();
            }

            if (tickAccesor.isSniffed() && tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION && tremorsaurus.isInSittingPose()){
                tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_BITE);
            }

            if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_BITE && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15){
                if (isValidTarget(level, blockPos) && !CaveDelightCompat.roastedCheck(tremorsaurus,blockPos)){
                    tremorsaurus.heal(4);
                    tremorsaurus.playSound(ACSoundRegistry.TREMORSAURUS_BITE.get(), 1F, 1F);
                    level.destroyBlock(blockPos, false);

                    if(ModList.get().isLoaded("farmersdelight")){
                        FarmersDelightCompat.dinoEat(blockPos,level);
                    }

                    if (AlexsCavesExemplified.COMMON_CONFIG.SEETHED_TAMING_ENABLED.get() && level.getRandom().nextDouble() < 0.4) {
                        mob.addEffect(new MobEffectInstance(ACEEffects.SERENED.get(), 2400, 0));
                    }
                }
                if (CaveDelightCompat.roastedCheck(tremorsaurus,blockPos)){
                    tremorsaurus.heal(4);
                    tremorsaurus.playSound(ACSoundRegistry.TREMORSAURUS_BITE.get(), 1F, 1F);
                    CaveDelightCompat.roastedConsume(tremorsaurus,blockPos);

                    if (AlexsCavesExemplified.COMMON_CONFIG.SEETHED_TAMING_ENABLED.get() && level.getRandom().nextDouble() < 0.8) {
                        mob.addEffect(new MobEffectInstance(ACEEffects.SERENED.get(), 5400, 0));
                    }
                }
                this.stop();
            }
        }

    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        BlockState blockState = worldIn.getBlockState(pos);
        boolean hasEntityFood = CaveDelightCompat.roastedCheck(tremorsaurus,pos);
        return blockState.is(ACEBlockTagGenerator.DINO_SCAVENGE) || (hasEntityFood);
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
