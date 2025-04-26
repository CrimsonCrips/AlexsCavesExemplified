package org.crimsoncrips.alexscavesexemplified.mixins.mobs.base_deep_entity.deep_mage;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneKnightEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.LanternfishEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TripodfishEntity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(DeepOneKnightEntity.class)
public abstract class ACEDeepMageMixin extends PathfinderMob {

    protected ACEDeepMageMixin(EntityType entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.DEEP_HUNTING_ENABLED.get()){
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, TripodfishEntity.class, true, true));
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LanternfishEntity.class, true, true));
        }
    }
}
