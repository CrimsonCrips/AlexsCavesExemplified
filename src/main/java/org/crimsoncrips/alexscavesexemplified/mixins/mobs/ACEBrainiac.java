package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.TargetsDroppedItems;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEBaseInterface;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEPickupDroppedBarrels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity.ANIMATION_DRINK_BARREL;


@Mixin(BrainiacEntity.class)
public abstract class ACEBrainiac extends Monster implements ACEBaseInterface, TargetsDroppedItems {

    protected ACEBrainiac(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        BrainiacEntity brainiac = (BrainiacEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.WASTE_PICKUP_ENABLED.get()){
            brainiac.targetSelector.addGoal(4, new ACEPickupDroppedBarrels<>(brainiac,true));
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
