package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.PrehistoricMixtureItem;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PrehistoricMixtureItem.class)
public abstract class ACEPrehistoricMixinItemMixin extends BowlFoodItem {

    public ACEPrehistoricMixinItemMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "interactLivingEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V"))
    private void alexsCavesExemplified$interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (AlexsCavesExemplified.COMMON_CONFIG.SERENED_ENABLED.get() && itemStack.is(ACItemRegistry.SERENE_SALAD.get()) && livingEntity instanceof Mob mob){
            if ((mob instanceof TamableAnimal tamableAnimal && tamableAnimal.isTame()) || (mob.getRandom().nextInt(1) > 1/mob.getBoundingBox().getSize())){
                mob.addEffect(new MobEffectInstance(ACEEffects.SERENED.get(), 1200, 0));
                ACEUtils.awardAdvancement(player, "serened", "serened");
            }

        }
    }
}
