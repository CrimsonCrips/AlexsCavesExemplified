package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexthe666.alexsmobs.entity.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.crimsoncrips.alexscavesexemplified.client.ACESoundRegistry;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;

public class ACEDinosaurEggAttack<T extends DinosaurEntity> extends NearestAttackableTargetGoal {

    public ACEDinosaurEggAttack(Mob pMob, Class pTargetType, boolean pMustSee) {
        super(pMob, pTargetType, pMustSee);
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && !mob.isAlliedTo(target) && !mob.isBaby();
    }

    protected void findTarget() {
        if (mob instanceof VallumraptorEntity){
            isHoldingEgg(ACBlockRegistry.VALLUMRAPTOR_EGG.get().asItem());
        }
        if (mob instanceof SubterranodonEntity){
            isHoldingEgg(ACBlockRegistry.SUBTERRANODON_EGG.get().asItem());
        }
        if (mob instanceof GrottoceratopsEntity){
            isHoldingEgg(ACBlockRegistry.GROTTOCERATOPS_EGG.get().asItem());
        }
        if (mob instanceof TremorsaurusEntity){
            isHoldingEgg(ACBlockRegistry.TREMORSAURUS_EGG.get().asItem());
        }
        if (mob instanceof RelicheirusEntity){
            isHoldingEgg(ACBlockRegistry.RELICHEIRUS_EGG.get().asItem());
        }
        if (mob instanceof AtlatitanEntity){
            isHoldingEgg(ACBlockRegistry.ATLATITAN_EGG.get().asItem());
        }
    }

    @Override
    public void tick() {
        super.tick();
        ACEUtils.awardAdvancement(target,"egg_stealing","stole");
    }

    public void isHoldingEgg(Item item){
        this.target = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(this.targetType, this.getTargetSearchArea(this.getFollowDistance()), (p_148152_) -> {
            return p_148152_ instanceof LivingEntity living && living.isHolding(item) && !mob.isAlliedTo(living);
        }), this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());

    }
}