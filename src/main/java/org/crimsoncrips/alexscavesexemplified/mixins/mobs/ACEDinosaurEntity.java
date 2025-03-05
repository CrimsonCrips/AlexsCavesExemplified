package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(DinosaurEntity.class)
public abstract class ACEDinosaurEntity extends TamableAnimal {


    @Shadow public abstract int getAltSkinForItem(ItemStack stack);

    protected ACEDinosaurEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void registerGoals(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.PAINT_EFFECTS_ENABLED.get())
            return;

        ItemStack itemstack = player.getItemInHand(hand);
        InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
        InteractionResult type = super.mobInteract(player, hand);
        if (!interactionresult.consumesAction() && !type.consumesAction()){
            if (this.getAltSkinForItem(itemstack) > 0) {
                ACEUtils.awardAdvancement(player,"paint_effects","paint");
                this.addEffect(new MobEffectInstance(getAltSkinForItem(itemstack) == 2 ? MobEffects.FIRE_RESISTANCE : MobEffects.HEAL, 5000, 0));
            }
        }
    }


}
