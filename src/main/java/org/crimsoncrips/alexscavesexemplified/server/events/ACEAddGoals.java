package org.crimsoncrips.alexscavesexemplified.server.events;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetClosePlayers;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetItemGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.NotorHologramGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.NotorScanGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.VesperTargetUnderneathEntities;
import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.CaniacEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GammaroachEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GingerbreadManEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GrottoceratopsEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.RelicheirusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.crimsoncrips.alexscavesexemplified.server.ACExServerConfig;
import org.crimsoncrips.alexscavesexemplified.server.goals.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ACEAddGoals {

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        ACExServerConfig config = AlexsCavesExemplified.COMMON_CONFIG;

        if (event.getEntity() instanceof TremorsaurusEntity tremorsaurus) {
            if (config.SEETHED_TAMING_ENABLED.get()) {
                removeGoals(tremorsaurus.goalSelector, wrapped -> wrapped.getGoal().getClass() == TemptGoal.class);
            }
            if (config.SCAVENGING_ENABLED.get()) {
                addGoalFirst(tremorsaurus.targetSelector, 2, new ACExTremorDroppedEatBlock(tremorsaurus, true, true, 200 + tremorsaurus.getRandom().nextInt(150), 30));
                addGoalFirst(tremorsaurus.goalSelector, 3, new ACExTremorEatBlock(tremorsaurus, 1, 30, 3));
            }
            if (config.SEETHED_TAMING_ENABLED.get()) {
                addGoalFirst(tremorsaurus.goalSelector, 2, new ACExTremorTempt(tremorsaurus, 1.1, Ingredient.of(new ItemLike[]{ACBlockRegistry.COOKED_DINOSAUR_CHOP.get(), ACBlockRegistry.DINOSAUR_CHOP.get()}), false));
            }
            if (config.EGG_ANGER_ENABLED.get()) {
                addGoalFirst(tremorsaurus.targetSelector, 4, new ACExDinosaurEggAttack<>(tremorsaurus, LivingEntity.class, true));
            }
        }

        if (event.getEntity() instanceof GrottoceratopsEntity grottoceratops && config.EGG_ANGER_ENABLED.get()) {
            grottoceratops.targetSelector.addGoal(4, new ACExDinosaurEggAttack<>(grottoceratops, LivingEntity.class, true));
        }

        if (event.getEntity() instanceof RelicheirusEntity relicheirus && config.EGG_ANGER_ENABLED.get()) {
            relicheirus.targetSelector.addGoal(4, new ACExDinosaurEggAttack<>(relicheirus, LivingEntity.class, true));
        }

        if (event.getEntity() instanceof SubterranodonEntity subterranodon && config.EGG_ANGER_ENABLED.get()) {
            subterranodon.targetSelector.addGoal(4, new ACExDinosaurEggAttack<>(subterranodon, LivingEntity.class, true));
        }

        if (event.getEntity() instanceof AtlatitanEntity atlatitan && config.EGG_ANGER_ENABLED.get()) {
            atlatitan.targetSelector.addGoal(4, new ACExDinosaurEggAttack<>(atlatitan, LivingEntity.class, true) {
                @Override
                public boolean canContinueToUse() {
                    return super.canContinueToUse() && atlatitan.getRideableFor() <= 0;
                }
            });
        }

        if (event.getEntity() instanceof VallumraptorEntity vallumraptor) {
            if (config.EGG_ANGER_ENABLED.get()) {
                vallumraptor.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(vallumraptor, LivingEntity.class, 100, true, false, livingEntity -> livingEntity.isHolding(Ingredient.of(ACBlockRegistry.VALLUMRAPTOR_EGG.get()))) {
                    @Override
                    public boolean canContinueToUse() {
                        return super.canContinueToUse() && !vallumraptor.isTame();
                    }
                });
            }
            if (config.SCAVENGING_ENABLED.get()) {
                vallumraptor.goalSelector.addGoal(6, new ACExVallumraptorEatBlock(vallumraptor, 1.4, 20));
            }
        }

        if (event.getEntity() instanceof BrainiacEntity brainiac && config.WASTE_PICKUP_ENABLED.get()) {
            brainiac.targetSelector.addGoal(4, new ACExPickupDroppedBarrels<>(brainiac, true));
        }

        if (event.getEntity() instanceof GammaroachEntity gammaroach && config.ROACH_FEEDING_ENABLED.get()) {
            gammaroach.targetSelector.addGoal(1, new MobTargetItemGoal<>(gammaroach, false));
        }

        if (event.getEntity() instanceof CaniacEntity caniac && config.CANIAC_MANIAC_ENABLED.get()) {
            caniac.goalSelector.addGoal(2, new NearestAttackableTargetGoal<>(caniac, LivingEntity.class, 5000, false, false, Objects::nonNull) {
                @Override
                public void start() {
                    caniac.playSound(ACSoundRegistry.CANIAC_HURT.get(), 2.0F, 0.0F);
                    super.start();
                }
            });
            caniac.goalSelector.addGoal(2, new ACExCaniacBedwars(caniac, 24));
            caniac.goalSelector.addGoal(2, new ACExCaniacExplode(caniac, 24));
        }

        if (event.getEntity() instanceof GingerbreadManEntity gingerbreadMan && config.HIVE_MIND_ENABLED.get()) {
            gingerbreadMan.targetSelector.addGoal(1, new ACExHurtByTargetGoal(gingerbreadMan).setAlertOthers());
        }

        if (event.getEntity() instanceof CorrodentEntity corrodent) {
            if (config.FORLORN_LIGHT_EFFECT_ENABLED.get()) {
                removeGoals(corrodent.targetSelector, wrapped -> wrapped.getGoal().getClass() == MobTarget3DGoal.class);
                corrodent.targetSelector.addGoal(2, new MobTarget3DGoal(corrodent, Player.class, false, 10, livingEntity -> livingEntity instanceof Player player && !CuriosCompat.hasLight(player)));
            }
            if (config.KNAWING_ENABLED.get()) {
                corrodent.targetSelector.addGoal(1, new ACExKnawingGoal(corrodent, false));
            }
        }

        if (event.getEntity() instanceof VesperEntity vesper) {
            if (config.FORLORN_LIGHT_EFFECT_ENABLED.get()) {
                removeGoals(vesper.targetSelector, wrapped -> wrapped.getPriority() == 2 && wrapped.getGoal().getClass() == VesperTargetUnderneathEntities.class);
                vesper.targetSelector.addGoal(3, new ACExVesperTarget<>(vesper, 32.0F, Player.class, livingEntity -> !CuriosCompat.hasLight(livingEntity) || vesper.getLastAttacker() == livingEntity));
                vesper.goalSelector.addGoal(1, new AvoidEntityGoal<>(vesper, LivingEntity.class, 4.0F, 1.5, 2, livingEntity -> vesper.getLastAttacker() != livingEntity && CuriosCompat.hasLight(livingEntity)));
            }
            if (config.ANTI_SACRIFICE_ENABLED.get()) {
                vesper.targetSelector.addGoal(3, new ACExVesperTarget<>(vesper, 32.0F, UnderzealotEntity.class, livingEntity -> livingEntity instanceof UnderzealotEntity underzealot && underzealot.getFirstPassenger() instanceof VesperEntity));
            }
        }

        if (event.getEntity() instanceof UnderzealotEntity underzealot) {
            if (config.FORLORN_LIGHT_EFFECT_ENABLED.get() || config.UNDERZEALOT_RESPECT_ENABLED.get()) {
                removeGoals(underzealot.targetSelector, wrapped -> wrapped.getPriority() == 2 && wrapped.getGoal() instanceof MobTargetClosePlayers);
            }
            underzealot.targetSelector.addGoal(2, new ACExMobTargetClosePlayers(underzealot, 40, 12.0F, livingEntity -> {
                boolean light = !CuriosCompat.hasLight(livingEntity) || !config.FORLORN_LIGHT_EFFECT_ENABLED.get();
                boolean respect = (!livingEntity.getItemBySlot(EquipmentSlot.CHEST).is(ACItemRegistry.CLOAK_OF_DARKNESS.get()) && !livingEntity.getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.HOOD_OF_DARKNESS.get())) || !config.UNDERZEALOT_RESPECT_ENABLED.get();
                return light && respect;
            }) {
                @Override
                public boolean canUse() {
                    return !underzealot.isTargetingBlocked() && super.canUse();
                }
            });
        }

        if (event.getEntity() instanceof NucleeperEntity nucleeper) {
            if (config.DEFUSION_ENABLED.get()) {
                removeGoals(nucleeper.goalSelector, wrapped -> wrapped.getPriority() == 2 && wrapped.getGoal().getClass().getEnclosingClass() == NucleeperEntity.class);
            }
            nucleeper.goalSelector.addGoal(2, new ACExNucleeperMelee(nucleeper));
        }

        if (event.getEntity() instanceof MineGuardianEntity mineGuardian) {
            if (config.REMINEDING_ENABLED.get()) {
                removeGoals(mineGuardian.targetSelector, wrapped -> wrapped.getGoal().getClass() == HurtByTargetGoal.class);
            }
            mineGuardian.targetSelector.addGoal(1, new ACExMineGuardianHurtBy(mineGuardian));
        }

        if (event.getEntity() instanceof NotorEntity notor && config.SELF_DESTRUCT_ENABLED.get()) {
            removeGoals(notor.goalSelector, wrapped -> wrapped.getGoal().getClass() == NotorHologramGoal.class || wrapped.getGoal().getClass() == NotorScanGoal.class);
            notor.goalSelector.addGoal(1, new NotorHologramGoal(notor) {
                @Override
                public boolean canUse() {
                    return super.canUse() && notor.getLastHurtByMob() == null;
                }
            });
            notor.goalSelector.addGoal(2, new NotorScanGoal(notor) {
                @Override
                public boolean canUse() {
                    return super.canUse() && notor.getLastHurtByMob() == null;
                }
            });
            notor.goalSelector.addGoal(2, new ACExSelfDestruct(notor));
        }

        if (ModList.get().isLoaded("alexsmobs") && config.FLY_TRAPPED_ENABLED.get()) {
            AMCompat.addFlyGoals(event.getEntity());
        }
    }

    public static void addGoalFirst(GoalSelector selector, int priority, Goal goal) {
        List<WrappedGoal> existing = new ArrayList<>(selector.getAvailableGoals());
        selector.getAvailableGoals().clear();
        selector.addGoal(priority, goal);
        for (WrappedGoal wrapped : existing) {
            selector.addGoal(wrapped.getPriority(), wrapped.getGoal());
        }
    }

    private static void removeGoals(GoalSelector selector, Predicate<WrappedGoal> predicate) {
        selector.getAvailableGoals().removeIf(predicate);
    }
}
