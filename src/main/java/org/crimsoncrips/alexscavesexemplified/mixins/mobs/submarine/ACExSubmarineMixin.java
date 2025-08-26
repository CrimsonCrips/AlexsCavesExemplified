package org.crimsoncrips.alexscavesexemplified.mixins.mobs.submarine;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SubmarineEntity.class)
public abstract class ACExSubmarineMixin extends Entity {


    @Shadow public abstract void setDamageLevel(int level);

    @Shadow public abstract int getDamageLevel();

    public ACExSubmarineMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tickController", at = @At("TAIL"),remap = false)
    private void alexsCavesExemplified$registerGoals(Player passenger, CallbackInfo ci) {
        SubmarineEntity submarineEntity = (SubmarineEntity)(Object)this;
        if (passenger.xxa != 0 || passenger.zza != 0){
            for (LivingEntity entity : submarineEntity.level().getEntitiesOfClass(LivingEntity.class, submarineEntity.getBoundingBox().inflate(1.6))) {
                if (entity != passenger && entity.getBbHeight() <= 1.6F && AlexsCavesExemplified.COMMON_CONFIG.SUBMARINE_BUMP_ENABLED.get()) {
                    entity.hurt(submarineEntity.damageSources().mobAttack(entity), 6.0F);
                    entity.knockback(1.5D, submarineEntity.getX() - entity.getX(), submarineEntity.getZ() - entity.getZ());
                    ACExUtils.awardAdvancement(submarineEntity.getFirstPassenger(),"submarine_bump","bump");
                }
            }
        }
    }
}