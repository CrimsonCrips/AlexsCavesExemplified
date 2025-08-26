package org.crimsoncrips.alexscavesexemplified.mixins.mobs.base_deep_entity.deep_knight;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneKnightEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.LanternfishEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TripodfishEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(DeepOneKnightEntity.class)
public abstract class ACExDeepKnightMixin extends DeepOneBaseEntity {


    protected ACExDeepKnightMixin(EntityType entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.ADD_TARGETS_ENABLED.get()){
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, TripodfishEntity.class, true, true));
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LanternfishEntity.class, true, true));
        }
    }
}
