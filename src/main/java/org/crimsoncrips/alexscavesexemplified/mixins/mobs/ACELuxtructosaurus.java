package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import com.github.alexthe666.alexsmobs.AlexsMobs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LuxtructosaurusEntity.class)
public abstract class ACELuxtructosaurus extends SauropodBaseEntity {

    protected ACELuxtructosaurus(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "onStep", at = @At("TAIL"),remap = false)
    private void registerGoals(CallbackInfo ci) {

        LuxtructosaurusEntity lux = (LuxtructosaurusEntity)(Object)this;

        for (LivingEntity entity : lux.level().getEntitiesOfClass(LivingEntity.class, lux.getBoundingBox().expandTowards(1, -2, 1))) {
            if (entity != lux && entity.getBbHeight() <= 2.2F && AlexsCavesExemplified.COMMON_CONFIG.STOMP_DAMAGE_ENABLED.get()) {
                entity.hurt(lux.damageSources().mobAttack(lux), 6.0F);
                ACEUtils.awardAdvancement(entity,"splat","stepped");
            }
        }
    }
}
