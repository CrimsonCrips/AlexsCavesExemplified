package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetItemGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.GossamerWormEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.TargetsDroppedItems;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GossamerWormEntity.class)
public abstract class ACEGossamerWorm extends WaterAnimal implements TargetsDroppedItems {


    protected ACEGossamerWorm(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        GossamerWormEntity gossamerWorm = (GossamerWormEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.GOSSAMER_FEEDING_ENABLED.get()){
            gossamerWorm.targetSelector.addGoal(1, new MobTargetItemGoal<>(this, false){
                @Override
                public void tick() {
                    super.tick();

                }
            });
        }
    }

    @Override
    public boolean canTargetItem(ItemStack itemStack) {
        return AlexsCavesExemplified.COMMON_CONFIG.GOSSAMER_FEEDING_ENABLED.get() && itemStack.is(ACItemRegistry.MARINE_SNOW.get());
    }

    public void onGetItem(ItemEntity itemEntity) {
        this.heal(1);
        itemEntity.getItem().shrink(1);
    }

}
