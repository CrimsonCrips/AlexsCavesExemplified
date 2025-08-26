package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.LicowitchEntity;
import com.google.common.base.Predicates;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExEntityTagGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;


@Mixin(LicowitchEntity.class)
public abstract class ACExLicowitchMixin extends Monster {

    protected ACExLicowitchMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.ADD_TARGETS_ENABLED.get()) {
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Villager.class, true, buildPredicateFromTag(ACExEntityTagGenerator.LICOWITCH_HATE)));
        }

    }

    //Taken from AM
    private static Predicate<LivingEntity> buildPredicateFromTag(TagKey<EntityType<?>> entityTag) {
        return entityTag == null ? Predicates.alwaysFalse() : (e) -> e.isAlive() && e.getType().is(entityTag);
    }
}
