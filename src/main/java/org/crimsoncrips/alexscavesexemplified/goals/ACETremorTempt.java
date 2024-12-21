package org.crimsoncrips.alexscavesexemplified.goals;

import java.util.EnumSet;
import javax.annotation.Nullable;

import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;

public class ACETremorTempt extends TemptGoal {

    double speedModifier;
    TremorsaurusEntity tremorsaurus;
    private final Ingredient items;

    public ACETremorTempt(TremorsaurusEntity tremorsaurus, double pSpeedModifier, Ingredient pItems, boolean pCanScare) {
        super(tremorsaurus, pSpeedModifier, pItems, pCanScare);
        this.tremorsaurus = tremorsaurus;
        this.items = pItems;
        this.speedModifier = pSpeedModifier;
    }

    public void tick() {
        this.tremorsaurus.getLookControl().setLookAt(this.player, (float)(this.tremorsaurus.getMaxHeadYRot() + 20), (float)this.tremorsaurus.getMaxHeadXRot());
        if (this.tremorsaurus.distanceToSqr(this.player) < 6.25D) {
            this.tremorsaurus.getNavigation().stop();
            if (tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION) {
                tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_BITE);
            }

            if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_BITE && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15) {
                if (this.items.test(player.getMainHandItem())){
                    player.getMainHandItem().shrink(1);
                    if (player.getRandom().nextDouble() < 0.4){
                        tremorsaurus.tame(player);
                        tremorsaurus.level().broadcastEntityEvent(tremorsaurus, (byte)7);
                        stop();
                    }
                } else if (this.items.test(player.getOffhandItem())){
                    player.getOffhandItem().shrink(1);
                    if (player.getRandom().nextDouble() < 0.4){
                        tremorsaurus.tame(player);
                        tremorsaurus.level().broadcastEntityEvent(tremorsaurus, (byte)7);
                        stop();
                    }
                }
            }
        } else {
            this.tremorsaurus.getNavigation().moveTo(this.player, this.speedModifier);
        }

    }
}