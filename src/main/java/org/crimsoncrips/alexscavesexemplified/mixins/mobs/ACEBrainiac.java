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
    private static final EntityDataAccessor<Boolean> POWERED = SynchedEntityData.defineId(BrainiacEntity.class, EntityDataSerializers.BOOLEAN);;

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

    @Override
    public boolean isPowered() {
        return this.entityData.get(POWERED);
    }

    public void setPowered(boolean powered) {
        this.entityData.set(POWERED, powered);
    }


    @Inject(method = "postAttackEffect", at = @At("HEAD"),cancellable = true,remap = false)
    private void postAttack(LivingEntity entity, CallbackInfo ci) {
        ci.cancel();
        if (entity != null && entity.isAlive()) {
            entity.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 400,AlexsCavesExemplified.COMMON_CONFIG.WASTE_POWERUP_ENABLED.get() && isPowered() ? 1 : 0));
        }
        if (AlexsCavesExemplified.COMMON_CONFIG.WASTE_POWERUP_ENABLED.get() && isPowered()){
            setPowered(false);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        BrainiacEntity brainiac = (BrainiacEntity)(Object)this;
        Level level = brainiac.level();
        if (AlexsCavesExemplified.COMMON_CONFIG.WASTE_POWERUP_ENABLED.get()){
            if (isPowered()){
                Vec3 particlePos = brainiac.position().add((level.random.nextFloat() - 0.5F) * 2.5F, 0F, (level.random.nextFloat() - 0.5F) * 2.5F);
                level.addParticle(ACParticleRegistry.PROTON.get(), particlePos.x, particlePos.y, particlePos.z, brainiac.getX(), brainiac.getY(0.5F), brainiac.getZ());
            }
            if (!level.isClientSide && brainiac.hasBarrel()) {
                if (brainiac.getAnimation() == ANIMATION_DRINK_BARREL && brainiac.getAnimationTick() >= 60) {
                    setPowered(true);
                }
            }
        }

    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynched(CallbackInfo ci){
        this.entityData.define(POWERED, false);
    }
}
