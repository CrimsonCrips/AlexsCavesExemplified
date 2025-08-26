package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.GingerbreadManEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExHurtByTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GingerbreadManEntity.class)
public abstract class ACExGingerbreadManMixin extends Monster {


    protected ACExGingerbreadManMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        GingerbreadManEntity gingerbreadMan = (GingerbreadManEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.HIVE_MIND_ENABLED.get()) gingerbreadMan.targetSelector.addGoal(1, (new ACExHurtByTargetGoal(gingerbreadMan)).setAlertOthers(new Class[0]));
    }



    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() instanceof AxeItem && AlexsCavesExemplified.COMMON_CONFIG.AMPUTATION_ENABLED.get()) {
            GingerbreadManEntity gingerbread = (GingerbreadManEntity)(Object)this;
            if (gingerbread.hasBothLegs()) {
                gingerbread.hurt(player.damageSources().mobAttack(player), 0.5F);
                gingerbread.setLostLimb(gingerbread.getRandom().nextBoolean(), false, true);
                player.swing(player.getUsedItemHand());
            } else if (gingerbread.getRandom().nextInt(2) == 0) {
                player.swing(player.getUsedItemHand());
                gingerbread.hurt(player.damageSources().mobAttack(player), 0.5F);
                gingerbread.setLostLimb(gingerbread.getRandom().nextBoolean(), true, true);
            }
            ACExUtils.awardAdvancement(player,"amputate","amputate");
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }
}
