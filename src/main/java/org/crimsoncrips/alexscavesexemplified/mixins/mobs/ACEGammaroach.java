package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

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
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;


@Mixin(GammaroachEntity.class)
public abstract class ACEGammaroach extends PathfinderMob implements TargetsDroppedItems {


    @Shadow public abstract boolean isFed();

    protected ACEGammaroach(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        GammaroachEntity gammaroach = (GammaroachEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.ROACH_FEEDING_ENABLED.get()) {
            gammaroach.targetSelector.addGoal(1, new MobTargetItemGoal<>(this, false));
        }
    }

    @Override
    public boolean canTargetItem(ItemStack itemStack) {
        return itemStack.isEdible();
    }

    public void onGetItem(ItemEntity itemEntity) {
        ACEUtils.awardAdvancement(itemEntity.getOwner(),"feed_roach","feedroach");
        if (itemEntity.getItem().isEdible()) {
            this.heal(5);
            List<Pair<MobEffectInstance, Float>> test = Objects.requireNonNull(itemEntity.getItem().getFoodProperties(this)).getEffects();
            if (!test.isEmpty()){
                for (int i = 0; i < test.size(); i++){
                    this.addEffect(new MobEffectInstance(test.get(i).getFirst()));
                }
            }
        }
        itemEntity.getItem().shrink(1);
    }


}
