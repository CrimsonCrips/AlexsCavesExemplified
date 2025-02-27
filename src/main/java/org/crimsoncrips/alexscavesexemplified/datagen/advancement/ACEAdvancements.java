package org.crimsoncrips.alexscavesexemplified.datagen.advancement;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexthe666.citadel.Citadel;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import java.util.function.Consumer;

public class ACEAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {

	@Override
	public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer, ExistingFileHelper helper) {
		Advancement root = Advancement.Builder.advancement().display(
						createCitadelIcon("alexscavesexemplified:textures/item/ace_adv_icon.png"),
						Component.translatable("advancement.alexscavesexemplified.root"),
						Component.translatable("advancement.alexscavesexemplified.root.desc"),
						new ResourceLocation(AlexsCavesExemplified.MODID, "textures/entity/nucleeper/nucleeper_rusted_glow.png"),
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

		Advancement forlorn = (Advancement.Builder.advancement().parent(root).display(
						createCitadelIcon("alexscaves:textures/misc/advancement/icon/forlorn_hollows.png"),
						Component.translatable("advancement.alexscavesexemplified.forlorn_hollows"),
						Component.translatable("advancement.alexscavesexemplified.forlorn_hollows.desc"),
						null, FrameType.TASK, false, false, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick()))
				.save(consumer, "alexscavesexemplified:forlorn");

		Advancement abysal = (Advancement.Builder.advancement().parent(root).display(
						createCitadelIcon("alexscaves:textures/misc/advancement/icon/abyssal_chasm.png"),
						Component.translatable("advancement.alexscavesexemplified.abyssal_chasm"),
						Component.translatable("advancement.alexscavesexemplified.abyssal_chasm.desc"),
						null, FrameType.TASK, false, false, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick()))
				.save(consumer, "alexscavesexemplified:abyssal");

		Advancement candy = (Advancement.Builder.advancement().parent(root).display(
						createCitadelIcon("alexscaves:textures/misc/advancement/icon/candy_cavity.png"),
						Component.translatable("advancement.alexscavesexemplified.candy_cavity"),
						Component.translatable("advancement.alexscavesexemplified.candy_cavity.desc"),
						null, FrameType.TASK, false, false, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick()))
				.save(consumer, "alexscavesexemplified:candy");

		Advancement primordial = (Advancement.Builder.advancement().parent(root).display(
						createCitadelIcon("alexscaves:textures/misc/advancement/icon/primordial_caves.png"),
						Component.translatable("advancement.alexscavesexemplified.primordial_caves"),
						Component.translatable("advancement.alexscavesexemplified.primordial_caves.desc"),
						null, FrameType.TASK, false, false, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick()))
				.save(consumer, "alexscavesexemplified:primordial");

		Advancement toxic = (Advancement.Builder.advancement().parent(root).display(
						createCitadelIcon("alexscaves:textures/misc/advancement/icon/toxic_caves.png"),
						Component.translatable("advancement.alexscavesexemplified.toxic_caves"),
						Component.translatable("advancement.alexscavesexemplified.toxic_caves.desc"),
						null, FrameType.TASK, false, false, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick()))
				.save(consumer, "alexscavesexemplified:toxic");


	}

	public ItemStack createCitadelIcon(String value){
		ItemStack itemStack = new ItemStack(Citadel.ICON_ITEM.get());
		itemStack.getOrCreateTag().putString("IconLocation",value);
	    return itemStack;
	}

}
