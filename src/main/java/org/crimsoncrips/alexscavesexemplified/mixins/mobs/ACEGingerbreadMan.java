package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.GingerbreadManEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEHurtByTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GingerbreadManEntity.class)
public abstract class ACEGingerbreadMan extends Monster {


    protected ACEGingerbreadMan(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        GingerbreadManEntity gingerbreadMan = (GingerbreadManEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.HIVE_MIND_ENABLED.get()) gingerbreadMan.targetSelector.addGoal(1, (new ACEHurtByTargetGoal(gingerbreadMan)).setAlertOthers(new Class[0]));
    }

    @Override
    public boolean isSensitiveToWater() {
        return AlexsCavesExemplified.COMMON_CONFIG.GINGER_DISINTEGRATE_ENABLED.get();
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
            ACEUtils.awardAdvancement(player,"amputate","amputate");
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }
}
