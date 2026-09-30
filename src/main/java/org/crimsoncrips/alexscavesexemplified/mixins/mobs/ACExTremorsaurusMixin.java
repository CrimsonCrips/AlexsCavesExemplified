package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import com.github.alexmodguy.alexscaves.server.entity.util.TargetsDroppedItems;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExDinosaurEggAttack;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExTremorDroppedEatBlock;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExTremorEatBlock;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExTremorTempt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TremorsaurusEntity.class)
public abstract class ACExTremorsaurusMixin extends DinosaurEntity implements TargetsDroppedItems {


    protected ACExTremorsaurusMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorsaurusEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        TremorsaurusEntity tremorsaurus = (TremorsaurusEntity)(Object)this;
        for (LivingEntity entity : tremorsaurus.level().getEntitiesOfClass(LivingEntity.class, tremorsaurus.getBoundingBox().expandTowards(1, -2, 1))) {
            if (entity != tremorsaurus && entity.getBbHeight() <= 0.8F) {
                entity.hurt(tremorsaurus.damageSources().mobAttack(tremorsaurus), 3.0F);
            }
        }
    }


    public boolean canTargetItem(ItemStack itemStack) {
        return itemStack.is(ACBlockRegistry.DINOSAUR_CHOP.get().asItem()) || itemStack.is(ACBlockRegistry.COOKED_DINOSAUR_CHOP.get().asItem());
    }


    public boolean isSeethed(TremorsaurusEntity tremorsaurus){
        return tremorsaurus.hasEffect(ACExEffects.SERENED);
    }


}
