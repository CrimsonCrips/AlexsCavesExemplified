package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import net.minecraft.world.entity.LivingEntity;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SauropodBaseEntity.class)
public abstract class ACExSauropodMixin {

    @Inject(method = "crushBlocksInRing", at = @At("TAIL"),remap = false)
    private void crush(int width, int ringStartX, int ringStartZ, float dropChance, CallbackInfo ci) {
        SauropodBaseEntity sauropodBase = (SauropodBaseEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.STOMPING_ENABLED.get()){
            for (LivingEntity entity : sauropodBase.level().getEntitiesOfClass(LivingEntity.class, sauropodBase.getBoundingBox().inflate(width))) {
                if (entity != sauropodBase && entity.getBbHeight() <= 3F) {
                    entity.hurt(sauropodBase.damageSources().mobAttack(sauropodBase), 6.0F);
                }
            }
        }
    }
}
