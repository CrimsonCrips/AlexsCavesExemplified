package org.crimsoncrips.alexscavesexemplified.datagen.advancement;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.AlexMealItem;
import com.github.alexthe666.citadel.Citadel;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACEBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.item.ACEItemRegistry;

import java.util.function.Consumer;

public class ACEAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {
	/**
	 * test
	 */
	@Override
	public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer, ExistingFileHelper helper) {
		Advancement root = Advancement.Builder.advancement().display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/ace_adv_icon.png"),
						Component.translatable("advancement.alexscavesexemplified.root"),
						Component.translatable("advancement.alexscavesexemplified.root.desc"),
						new ResourceLocation(AlexsCavesExemplified.MODID, "textures/gui/ace_bg.png"),
						FrameType.TASK,
						false, false, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
				.save(consumer, "alexscavesexemplified:root");


		Advancement magnetic = (Advancement.Builder.advancement().parent(root).display(
						createCitadelIcon("alexscaves:textures/misc/advancement/icon/magnetic_caves.png"),
						Component.translatable("advancement.alexscavesexemplified.magnetic_caves"),
						Component.translatable("advancement.alexscavesexemplified.magnetic_caves.desc"),
						null, FrameType.TASK, false, false, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick()))
				.save(consumer, "alexscavesexemplified:magnetic");


		Advancement galena_steal = (Advancement.Builder.advancement().parent(magnetic).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/galena_steal.png"),
						Component.translatable("advancement.alexscavesexemplified.galena_steal"),
						Component.translatable("advancement.alexscavesexemplified.galena_steal.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("steal", new ImpossibleTrigger.TriggerInstance()))
				.rewards(AdvancementRewards.Builder.experience(200))
				.save(consumer, "alexscavesexemplified:galena_steal");

		Advancement resizing = (Advancement.Builder.advancement().parent(magnetic).display(
						ACItemRegistry.HOLOCODER.get(),
						Component.translatable("advancement.alexscavesexemplified.resizing"),
						Component.translatable("advancement.alexscavesexemplified.resizing.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("resize", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:resizing");

		Advancement teletor_rearm = (Advancement.Builder.advancement().parent(galena_steal).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/teletor_rearm.png"),
						Component.translatable("advancement.alexscavesexemplified.teletor_rearm"),
						Component.translatable("advancement.alexscavesexemplified.teletor_rearm.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("rearm", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:teletor_rearm");

		Advancement magnerip = (Advancement.Builder.advancement().parent(teletor_rearm).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/magnerip.png"),
						Component.translatable("advancement.alexscavesexemplified.magnerip"),
						Component.translatable("advancement.alexscavesexemplified.magnerip.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("ripped", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:magnerip");

		Advancement tesla_shock = (Advancement.Builder.advancement().parent(magnetic).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/tesla_shock.png"),
						Component.translatable("advancement.alexscavesexemplified.tesla_shock"),
						Component.translatable("advancement.alexscavesexemplified.tesla_shock.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("shock", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:tesla_shock");

		Advancement self_destruct = (Advancement.Builder.advancement().parent(tesla_shock).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/self_destruct.png"),
						Component.translatable("advancement.alexscavesexemplified.self_destruct"),
						Component.translatable("advancement.alexscavesexemplified.self_destruct.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("explode", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:self_destruct");

		Advancement metal_cauldron = (Advancement.Builder.advancement().parent(magnetic).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/metal_cauldron.png"),
						Component.translatable("advancement.alexscavesexemplified.metal_cauldron"),
						Component.translatable("advancement.alexscavesexemplified.metal_cauldron.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("metal", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:metal_cauldron");

		Advancement acidic_replication = (Advancement.Builder.advancement().parent(metal_cauldron).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/acidic_cauldron.png"),
						Component.translatable("advancement.alexscavesexemplified.acidic_replication"),
						Component.translatable("advancement.alexscavesexemplified.acidic_replication.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("replicate", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:acidic_replication");

		Advancement soda_replication = (Advancement.Builder.advancement().parent(metal_cauldron).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/soda_cauldron.png"),
						Component.translatable("advancement.alexscavesexemplified.soda_replication"),
						Component.translatable("advancement.alexscavesexemplified.soda_replication.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("replicate", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:soda_replication");

		Advancement forlorn = (Advancement.Builder.advancement().parent(root).display(
						createCitadelIcon("alexscaves:textures/misc/advancement/icon/forlorn_hollows.png"),
						Component.translatable("advancement.alexscavesexemplified.forlorn_hollows"),
						Component.translatable("advancement.alexscavesexemplified.forlorn_hollows.desc"),
						null, FrameType.TASK, false, false, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick()))
				.save(consumer, "alexscavesexemplified:forlorn");

		Advancement light_repel = (Advancement.Builder.advancement().parent(forlorn).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/light_repel.png"),
						Component.translatable("advancement.alexscavesexemplified.light_repel"),
						Component.translatable("advancement.alexscavesexemplified.light_repel.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("repelled", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:light_repel");

		Advancement rabial = (Advancement.Builder.advancement().parent(forlorn).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/rabial.png"),
						Component.translatable("advancement.alexscavesexemplified.rabial"),
						Component.translatable("advancement.alexscavesexemplified.rabial.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("has_rabies", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:rabial");

		Advancement rabial_spread = (Advancement.Builder.advancement().parent(rabial).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/rabial_spread.png"),
						Component.translatable("advancement.alexscavesexemplified.rabial_spread"),
						Component.translatable("advancement.alexscavesexemplified.rabial_spread.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("spread", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:rabial_spread");

		Advancement burst_out = (Advancement.Builder.advancement().parent(forlorn).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/burst_out.png"),
						Component.translatable("advancement.alexscavesexemplified.burst_out"),
						Component.translatable("advancement.alexscavesexemplified.burst_out.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("burst", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:burst_out");

		Advancement knawing = (Advancement.Builder.advancement().parent(forlorn).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/knawing.png"),
						Component.translatable("advancement.alexscavesexemplified.knawing"),
						Component.translatable("advancement.alexscavesexemplified.knawing.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("knaw", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:knawing");

		Advancement dark_respect = (Advancement.Builder.advancement().parent(forlorn).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/dark_respect.png"),
						Component.translatable("advancement.alexscavesexemplified.dark_respect"),
						Component.translatable("advancement.alexscavesexemplified.dark_respect.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("respect", new ImpossibleTrigger.TriggerInstance()))
				.rewards(AdvancementRewards.Builder.experience(300))
				.save(consumer, "alexscavesexemplified:dark_respect");

		Advancement gloomoth_trade = (Advancement.Builder.advancement().parent(dark_respect).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/gloomoth_trade.png"),
						Component.translatable("advancement.alexscavesexemplified.gloomoth_trade"),
						Component.translatable("advancement.alexscavesexemplified.gloomoth_trade.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("gloomoth", new ImpossibleTrigger.TriggerInstance()))
				.rewards(AdvancementRewards.Builder.experience(50))
				.save(consumer, "alexscavesexemplified:gloomoth_trade");

		Advancement vesper_trade = (Advancement.Builder.advancement().parent(dark_respect).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/vesper_trade.png"),
						Component.translatable("advancement.alexscavesexemplified.vesper_trade"),
						Component.translatable("advancement.alexscavesexemplified.vesper_trade.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("vesper", new ImpossibleTrigger.TriggerInstance()))
				.rewards(AdvancementRewards.Builder.experience(100))
				.save(consumer, "alexscavesexemplified:vesper_trade");

		Advancement corrodent_trade = (Advancement.Builder.advancement().parent(dark_respect).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/corrodent_trade.png"),
						Component.translatable("advancement.alexscavesexemplified.corrodent_trade"),
						Component.translatable("advancement.alexscavesexemplified.corrodent_trade.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("corrodent", new ImpossibleTrigger.TriggerInstance()))
				.rewards(AdvancementRewards.Builder.experience(70))
				.save(consumer, "alexscavesexemplified:corrodent_trade");

		Advancement shot_down = (Advancement.Builder.advancement().parent(forlorn).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/shot_down.png"),
						Component.translatable("advancement.alexscavesexemplified.shot_down"),
						Component.translatable("advancement.alexscavesexemplified.shot_down.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("shot", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:shot_down");

		Advancement solidified = (Advancement.Builder.advancement().parent(forlorn).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/solidified.png"),
						Component.translatable("advancement.alexscavesexemplified.solidified"),
						Component.translatable("advancement.alexscavesexemplified.solidified.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("solid", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:solidified");

		Advancement abyssal = (Advancement.Builder.advancement().parent(root).display(
						createCitadelIcon("alexscaves:textures/misc/advancement/icon/abyssal_chasm.png"),
						Component.translatable("advancement.alexscavesexemplified.abyssal_chasm"),
						Component.translatable("advancement.alexscavesexemplified.abyssal_chasm.desc"),
						null, FrameType.TASK, false, false, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick()))
				.save(consumer, "alexscavesexemplified:abyssal");

		Advancement ecological_reputation = (Advancement.Builder.advancement().parent(abyssal).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/ecological_reputation.png"),
						Component.translatable("advancement.alexscavesexemplified.ecological_reputation"),
						Component.translatable("advancement.alexscavesexemplified.ecological_reputation.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("affect", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:ecological_reputation");

		Advancement hullbreaker_reputation = (Advancement.Builder.advancement().parent(ecological_reputation).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/hullbreaker_reputation.png"),
						Component.translatable("advancement.alexscavesexemplified.hullbreaker_reputation"),
						Component.translatable("advancement.alexscavesexemplified.hullbreaker_reputation.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("killed", new ImpossibleTrigger.TriggerInstance()))
				.rewards(AdvancementRewards.Builder.experience(200))
				.save(consumer, "alexscavesexemplified:hullbreaker_reputation");

		Advancement submarine_bump = (Advancement.Builder.advancement().parent(abyssal).display(
						ACItemRegistry.SUBMARINE.get(),
						Component.translatable("advancement.alexscavesexemplified.submarine_bump"),
						Component.translatable("advancement.alexscavesexemplified.submarine_bump.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("bump", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:submarine_bump");

		Advancement mine_ownership = (Advancement.Builder.advancement().parent(abyssal).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/mine_ownership.png"),
						Component.translatable("advancement.alexscavesexemplified.mine_ownership"),
						Component.translatable("advancement.alexscavesexemplified.mine_ownership.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("own", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:mine_ownership");

		Advancement noon_guardian = (Advancement.Builder.advancement().parent(mine_ownership).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/noon_guardian.png"),
						Component.translatable("advancement.alexscavesexemplified.noon_guardian"),
						Component.translatable("advancement.alexscavesexemplified.noon_guardian.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("noon", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:noon_guardian");

		Advancement nuclear_mines = (Advancement.Builder.advancement().parent(mine_ownership).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/nuclear_mines.png"),
						Component.translatable("advancement.alexscavesexemplified.nuclear_mines"),
						Component.translatable("advancement.alexscavesexemplified.nuclear_mines.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("nuke_mine", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:nuclear_mines");

		Advancement nuclear_kill = (Advancement.Builder.advancement().parent(nuclear_mines).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/nuclear_kill.png"),
						Component.translatable("advancement.alexscavesexemplified.nuclear_kill"),
						Component.translatable("advancement.alexscavesexemplified.nuclear_kill.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("killed", new ImpossibleTrigger.TriggerInstance()))
				.rewards(AdvancementRewards.Builder.experience(10))
				.save(consumer, "alexscavesexemplified:nuclear_kill");

		Advancement poisonous_skin = (Advancement.Builder.advancement().parent(abyssal).display(
						ACItemRegistry.SEA_PIG.get(),
						Component.translatable("advancement.alexscavesexemplified.poisonous_skin"),
						Component.translatable("advancement.alexscavesexemplified.poisonous_skin.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("touched", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:poisonous_skin");

		Advancement gossamer_feed = (Advancement.Builder.advancement().parent(abyssal).display(
						ACItemRegistry.MARINE_SNOW.get(),
						Component.translatable("advancement.alexscavesexemplified.gossamer_feed"),
						Component.translatable("advancement.alexscavesexemplified.gossamer_feed.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("fed", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:gossamer_feed");


		Advancement candy = (Advancement.Builder.advancement().parent(root).display(
						createCitadelIcon("alexscaves:textures/misc/advancement/icon/candy_cavity.png"),
						Component.translatable("advancement.alexscavesexemplified.candy_cavity"),
						Component.translatable("advancement.alexscavesexemplified.candy_cavity.desc"),
						null, FrameType.TASK, false, false, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick()))
				.save(consumer, "alexscavesexemplified:candy");

		Advancement gluttony = (Advancement.Builder.advancement().parent(candy).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/gluttony.png"),
						Component.translatable("advancement.alexscavesexemplified.gluttony"),
						Component.translatable("advancement.alexscavesexemplified.gluttony.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("eat", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:gluttony");

		Advancement sugar_crashed = (Advancement.Builder.advancement().parent(gluttony).display(
						createCitadelIcon("alexscavesexemplified:textures/mob_effect/sugar_crash.png"),
						Component.translatable("advancement.alexscavesexemplified.sugar_crashed"),
						Component.translatable("advancement.alexscavesexemplified.sugar_crashed.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("crashed", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:sugar_crashed");

		Advancement frostmint_explode = (Advancement.Builder.advancement().parent(candy).display(
						ACBlockRegistry.FROSTMINT.get(),
						Component.translatable("advancement.alexscavesexemplified.frostmint_explode"),
						Component.translatable("advancement.alexscavesexemplified.frostmint_explode.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("explode", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:frostmint_explode");

		Advancement frostmint_freeze = (Advancement.Builder.advancement().parent(frostmint_explode).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/frostmint_freeze.png"),
						Component.translatable("advancement.alexscavesexemplified.frostmint_freeze"),
						Component.translatable("advancement.alexscavesexemplified.frostmint_freeze.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("freeze", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:frostmint_freeze");

		Advancement sticky_soda = (Advancement.Builder.advancement().parent(candy).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/sticky_soda.png"),
						Component.translatable("advancement.alexscavesexemplified.sticky_soda"),
						Component.translatable("advancement.alexscavesexemplified.sticky_soda.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("stick", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:sticky_soda");

		Advancement purple_coloring = (Advancement.Builder.advancement().parent(sticky_soda).display(
						Items.PURPLE_DYE,
						Component.translatable("advancement.alexscavesexemplified.purple_coloring"),
						Component.translatable("advancement.alexscavesexemplified.purple_coloring.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("colored", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:purple_coloring");

		Advancement radiant_wrath = (Advancement.Builder.advancement().parent(candy).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/radiant_wrath.png"),
						Component.translatable("advancement.alexscavesexemplified.radiant_wrath"),
						Component.translatable("advancement.alexscavesexemplified.radiant_wrath.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("powered", new ImpossibleTrigger.TriggerInstance()))
				.rewards(AdvancementRewards.Builder.experience(100))
				.save(consumer, "alexscavesexemplified:radiant_wrath");

		Advancement dropped_consumption = (Advancement.Builder.advancement().parent(candy).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/dropped_consumption.png"),
						Component.translatable("advancement.alexscavesexemplified.dropped_consumption"),
						Component.translatable("advancement.alexscavesexemplified.dropped_consumption.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("consumed", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:dropped_consumption");

		Advancement full_consumption = (Advancement.Builder.advancement().parent(dropped_consumption).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/dropped_consumption.png"),
						Component.translatable("advancement.alexscavesexemplified.full_consumption"),
						Component.translatable("advancement.alexscavesexemplified.full_consumption.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("full_consumed", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:full_consumption");

		Advancement iced_freeze = (Advancement.Builder.advancement().parent(candy).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/iced_freeze.png"),
						Component.translatable("advancement.alexscavesexemplified.iced_freeze"),
						Component.translatable("advancement.alexscavesexemplified.iced_freeze.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("freezed", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:iced_freeze");

		Advancement ice_cream = (Advancement.Builder.advancement().parent(iced_freeze).display(
						ACEItemRegistry.ICE_CREAM_CONE.get(),
						Component.translatable("advancement.alexscavesexemplified.ice_cream"),
						Component.translatable("advancement.alexscavesexemplified.ice_cream.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("made", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:ice_cream");

		Advancement breaking_candy = (Advancement.Builder.advancement().parent(candy).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/breaking_candy.png"),
						Component.translatable("advancement.alexscavesexemplified.breaking_candy"),
						Component.translatable("advancement.alexscavesexemplified.breaking_candy.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("bake", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:breaking_candy");

		Advancement overdrived_conversion = (Advancement.Builder.advancement().parent(breaking_candy).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/overdrived_conversion.png"),
						Component.translatable("advancement.alexscavesexemplified.overdrived_conversion"),
						Component.translatable("advancement.alexscavesexemplified.overdrived_conversion.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("overdrived", new ImpossibleTrigger.TriggerInstance()))
				.rewards(AdvancementRewards.Builder.experience(150))
				.save(consumer, "alexscavesexemplified:overdrived_conversion");

		Advancement amputate = (Advancement.Builder.advancement().parent(candy).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/amputate.png"),
						Component.translatable("advancement.alexscavesexemplified.amputate"),
						Component.translatable("advancement.alexscavesexemplified.amputate.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("amputate", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:amputate");

		Advancement interrupt = (Advancement.Builder.advancement().parent(candy).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/interrupt.png"),
						Component.translatable("advancement.alexscavesexemplified.interrupt"),
						Component.translatable("advancement.alexscavesexemplified.interrupt.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("interrupt", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:interrupt");

		Advancement feed_speedup = (Advancement.Builder.advancement().parent(interrupt).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/feed_speedup.png"),
						Component.translatable("advancement.alexscavesexemplified.feed_speedup"),
						Component.translatable("advancement.alexscavesexemplified.feed_speedup.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("feed", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:feed_speedup");


		Advancement primordial = (Advancement.Builder.advancement().parent(root).display(
						createCitadelIcon("alexscaves:textures/misc/advancement/icon/primordial_caves.png"),
						Component.translatable("advancement.alexscavesexemplified.primordial_caves"),
						Component.translatable("advancement.alexscavesexemplified.primordial_caves.desc"),
						null, FrameType.TASK, false, false, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick()))
				.save(consumer, "alexscavesexemplified:primordial");

		Advancement egg_stealing = (Advancement.Builder.advancement().parent(primordial).display(
						ACEItemRegistry.DINO_EGGS.get(),
						Component.translatable("advancement.alexscavesexemplified.egg_stealing"),
						Component.translatable("advancement.alexscavesexemplified.egg_stealing.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("stole", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:egg_stealing");

		Advancement splat = (Advancement.Builder.advancement().parent(primordial).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/splat.png"),
						Component.translatable("advancement.alexscavesexemplified.splat"),
						Component.translatable("advancement.alexscavesexemplified.splat.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("stepped", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:splat");

		Advancement riding_splat = (Advancement.Builder.advancement().parent(splat).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/riding_splat.png"),
						Component.translatable("advancement.alexscavesexemplified.riding_splat"),
						Component.translatable("advancement.alexscavesexemplified.riding_splat.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("ride_stepped", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:riding_splat");

		Advancement propogate = (Advancement.Builder.advancement().parent(primordial).display(
						ACBlockRegistry.FLYTRAP.get(),
						Component.translatable("advancement.alexscavesexemplified.propogate"),
						Component.translatable("advancement.alexscavesexemplified.propogate.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("bonemeal", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:propogate");

		Advancement serened = (Advancement.Builder.advancement().parent(propogate).display(
						ACItemRegistry.SERENE_SALAD.get(),
						Component.translatable("advancement.alexscavesexemplified.serened"),
						Component.translatable("advancement.alexscavesexemplified.serened.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("serened", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:serened");


		Advancement paint_effects = (Advancement.Builder.advancement().parent(primordial).display(
						ACItemRegistry.TECTONIC_SHARD.get(),
						Component.translatable("advancement.alexscavesexemplified.paint_effects"),
						Component.translatable("advancement.alexscavesexemplified.paint_effects.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("paint", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:paint_effects");

		Advancement egg_sacrifice = (Advancement.Builder.advancement().parent(primordial).display(
						ACBlockRegistry.ATLATITAN_EGG.get(),
						Component.translatable("advancement.alexscavesexemplified.egg_sacrifice"),
						Component.translatable("advancement.alexscavesexemplified.egg_sacrifice.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("egg_sacrifice", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:egg_sacrifice");

		Advancement volcanic_sacrifice = (Advancement.Builder.advancement().parent(egg_sacrifice).display(
						createCitadelIcon("alexscaves:textures/misc/advancement/icon/summon_luxtructosaurus.png"),
						Component.translatable("advancement.alexscavesexemplified.volcanic_sacrifice"),
						Component.translatable("advancement.alexscavesexemplified.volcanic_sacrifice.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("live_sacrifice", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:volcanic_sacrifice");

		Advancement drop_food = (Advancement.Builder.advancement().parent(primordial).display(
						ACBlockRegistry.COOKED_DINOSAUR_CHOP.get(),
						Component.translatable("advancement.alexscavesexemplified.drop_food"),
						Component.translatable("advancement.alexscavesexemplified.drop_food.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("drop", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:drop_food");

		Advancement seethed_taming = (Advancement.Builder.advancement().parent(drop_food).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/seethed_taming.png"),
						Component.translatable("advancement.alexscavesexemplified.seethed_taming"),
						Component.translatable("advancement.alexscavesexemplified.seethed_taming.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("tame", new ImpossibleTrigger.TriggerInstance()))
				.rewards(AdvancementRewards.Builder.experience(200))
				.save(consumer, "alexscavesexemplified:seethed_taming");

		Advancement toxic = (Advancement.Builder.advancement().parent(root).display(
						createCitadelIcon("alexscaves:textures/misc/advancement/icon/toxic_caves.png"),
						Component.translatable("advancement.alexscavesexemplified.toxic_caves"),
						Component.translatable("advancement.alexscavesexemplified.toxic_caves.desc"),
						null, FrameType.TASK, false, false, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick()))
				.save(consumer, "alexscavesexemplified:toxic");

		Advancement deathly_radiation = (Advancement.Builder.advancement().parent(toxic).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/deathly_radiation.png"),
						Component.translatable("advancement.alexscavesexemplified.deathly_radiation"),
						Component.translatable("advancement.alexscavesexemplified.deathly_radiation.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("radiate", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:deathly_radiation");

		Advancement washing_radiation = (Advancement.Builder.advancement().parent(deathly_radiation).display(
						Items.WATER_BUCKET,
						Component.translatable("advancement.alexscavesexemplified.washing_radiation"),
						Component.translatable("advancement.alexscavesexemplified.washing_radiation.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("wash", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:washing_radiation");

		Advancement kirov_reporting = (Advancement.Builder.advancement().parent(toxic).display(
						ACBlockRegistry.NUCLEAR_BOMB.get(),
						Component.translatable("advancement.alexscavesexemplified.kirov_reporting"),
						Component.translatable("advancement.alexscavesexemplified.kirov_reporting.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("kirov", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:kirov_reporting");

		Advancement feed_roach = (Advancement.Builder.advancement().parent(toxic).display(
						ACItemRegistry.TOXIC_PASTE.get(),
						Component.translatable("advancement.alexscavesexemplified.feed_roach"),
						Component.translatable("advancement.alexscavesexemplified.feed_roach.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("feedroach", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:feed_roach");

		Advancement defusing = (Advancement.Builder.advancement().parent(toxic).display(
						Items.SHEARS,
						Component.translatable("advancement.alexscavesexemplified.defusing"),
						Component.translatable("advancement.alexscavesexemplified.defusing.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("defuse", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:defusing");

		Advancement chain_reaction = (Advancement.Builder.advancement().parent(defusing).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/chain_reaction.png"),
						Component.translatable("advancement.alexscavesexemplified.chain_reaction"),
						Component.translatable("advancement.alexscavesexemplified.chain_reaction.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("chain", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:chain_reaction");

		Advancement nucleeper_annhilation = (Advancement.Builder.advancement().parent(chain_reaction).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/nucleeper_annhilation.png"),
						Component.translatable("advancement.alexscavesexemplified.nucleeper_annhilation"),
						Component.translatable("advancement.alexscavesexemplified.nucleeper_annhilation.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("nuke_chained", new ImpossibleTrigger.TriggerInstance()))
				.rewards(AdvancementRewards.Builder.experience(1000))
				.save(consumer, "alexscavesexemplified:nucleeper_annhilation");

		Advancement convert_fish = (Advancement.Builder.advancement().parent(toxic).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/convert_fish.png"),
						Component.translatable("advancement.alexscavesexemplified.convert_fish"),
						Component.translatable("advancement.alexscavesexemplified.convert_fish.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("convert", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:convert_fish");

		Advancement convert_cat = (Advancement.Builder.advancement().parent(convert_fish).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/convert_cat.png"),
						Component.translatable("advancement.alexscavesexemplified.convert_cat"),
						Component.translatable("advancement.alexscavesexemplified.convert_cat.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("convert", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:convert_cat");

		Advancement gamma_tremorzilla = (Advancement.Builder.advancement().parent(toxic).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/gamma_tremorzilla.png"),
						Component.translatable("advancement.alexscavesexemplified.gamma_tremorzilla"),
						Component.translatable("advancement.alexscavesexemplified.gamma_tremorzilla.desc"),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("gamma", new ImpossibleTrigger.TriggerInstance()))
				.save(consumer, "alexscavesexemplified:gamma_tremorzilla");

		Advancement gamma_tremorzilla_kill = (Advancement.Builder.advancement().parent(gamma_tremorzilla).display(
						createCitadelIcon("alexscavesexemplified:textures/gui/adv_icon/gamma_tremorzilla.png"),
						Component.translatable("advancement.alexscavesexemplified.gamma_tremorzilla_kill"),
						Component.translatable("advancement.alexscavesexemplified.gamma_tremorzilla_kill.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("gamma_kill", new ImpossibleTrigger.TriggerInstance()))
				.rewards(AdvancementRewards.Builder.experience(10000))
				.save(consumer, "alexscavesexemplified:gamma_tremorzilla_kill");

	}

	public ItemStack createCitadelIcon(String value){
		ItemStack itemStack = new ItemStack(Citadel.ICON_ITEM.get());
		itemStack.getOrCreateTag().putString("IconLocation",value);
	    return itemStack;
	}

	private PlayerTrigger.TriggerInstance advancementTrigger(Advancement advancement) {
		return this.advancementTrigger(advancement.getId().getPath());
	}

	private PlayerTrigger.TriggerInstance advancementTrigger(String name) {
		return new PlayerTrigger.TriggerInstance(CriteriaTriggers.TICK.getId(), ContextAwarePredicate.create(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(PlayerPredicate.Builder.player().checkAdvancementDone(AlexsCavesExemplified.prefix(name), true).build())).build()));
	}

}
