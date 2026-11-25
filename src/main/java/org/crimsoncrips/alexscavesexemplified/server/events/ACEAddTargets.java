package org.crimsoncrips.alexscavesexemplified.server.events;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.LanternfishEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.LicowitchEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TripodfishEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import com.google.common.base.Predicates;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExEntityTagGenerator;
import org.crimsoncrips.alexscavesexemplified.server.ACExAddTargetsConfig;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExVesperTarget;

import java.util.function.Predicate;

public class ACEAddTargets {

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        ACExAddTargetsConfig config = AlexsCavesExemplified.TARGETS_CONFIG;

        if (event.getEntity() instanceof LicowitchEntity licowitch && config.LICOWITCH_ENABLED.get()) {
            licowitch.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(licowitch, LivingEntity.class, true, buildPredicateFromTag(ACExEntityTagGenerator.LICOWITCH_HATE)));
        }

        if (event.getEntity() instanceof DeepOneBaseEntity deepOne && config.DEEP_ONES_ENABLED.get()) {
            ACEAddGoals.addGoalFirst(deepOne.targetSelector, 2, new NearestAttackableTargetGoal<>(deepOne, LanternfishEntity.class, true, true));
            ACEAddGoals.addGoalFirst(deepOne.targetSelector, 2, new NearestAttackableTargetGoal<>(deepOne, TripodfishEntity.class, true, true));
        }

        if (event.getEntity() instanceof VesperEntity vesper && config.VESPER_ENABLED.get()) {
            vesper.targetSelector.addGoal(3, new ACExVesperTarget<>(vesper, 32.0F, LivingEntity.class, buildPredicateFromTag(ACExEntityTagGenerator.VESPER_HUNT)));
        }
    }

    //Taken from AM
    private static Predicate<LivingEntity> buildPredicateFromTag(TagKey<EntityType<?>> entityTag) {
        return entityTag == null ? Predicates.alwaysFalse() : (e) -> e.isAlive() && e.getType().is(entityTag);
    }
}
