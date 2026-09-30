package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.core.component.DataComponents;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetItemGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.GammaroachEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.TargetsDroppedItems;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;


@Mixin(GammaroachEntity.class)
public abstract class ACExGammaroachMixin extends PathfinderMob implements TargetsDroppedItems {


    @Shadow public abstract boolean isFed();

    protected ACExGammaroachMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    public boolean canTargetItem(ItemStack itemStack) {
        return itemStack.has(DataComponents.FOOD);
    }

    public void onGetItem(ItemEntity itemEntity) {
        ACExUtils.awardAdvancement(itemEntity.getOwner(),"feed_roach","feedroach");
        if (itemEntity.getItem().has(DataComponents.FOOD)) {
            this.heal(5);
            List<FoodProperties.PossibleEffect> test = Objects.requireNonNull(itemEntity.getItem().getFoodProperties(this)).effects();
            if (!test.isEmpty()){
                for (int i = 0; i < test.size(); i++){
                    this.addEffect(new MobEffectInstance(test.get(i).effect()));
                }
            }
        }
        itemEntity.getItem().shrink(1);
    }


}
