package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.TargetsDroppedItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACExBaseInterface;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExPickupDroppedBarrels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(BrainiacEntity.class)
public abstract class ACExBrainiacMixin extends Monster implements ACExBaseInterface, TargetsDroppedItems {

    protected ACExBrainiacMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        BrainiacEntity brainiac = (BrainiacEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.WASTE_PICKUP_ENABLED.get()){
            brainiac.targetSelector.addGoal(4, new ACExPickupDroppedBarrels<>(brainiac,true));
        }
    }

    @Override
    public boolean canTargetItem(ItemStack itemStack) {
        return itemStack.is(ACBlockRegistry.WASTE_DRUM.get().asItem());
    }

    @Override
    public double getMaxDistToItem() {
        return 8;
    }

}
