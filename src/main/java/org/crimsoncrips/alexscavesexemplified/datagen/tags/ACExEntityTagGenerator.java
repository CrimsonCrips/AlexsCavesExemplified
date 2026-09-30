package org.crimsoncrips.alexscavesexemplified.datagen.tags;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ACExEntityTagGenerator extends EntityTypeTagsProvider {
	public static final TagKey<EntityType<?>> ACID_TO_CAT = create(AlexsCavesExemplified.prefix("acid_to_cat"));
	public static final TagKey<EntityType<?>> ACID_TO_FISH = create(AlexsCavesExemplified.prefix("acid_to_fish"));
	public static final TagKey<EntityType<?>> CAN_RABIES = create(AlexsCavesExemplified.prefix("can_rabies"));
	public static final TagKey<EntityType<?>> VESPER_HUNT = create(AlexsCavesExemplified.prefix("vesper_hunt"));
	public static final TagKey<EntityType<?>> LICOWITCH_HATE = create(AlexsCavesExemplified.prefix("licowitch_hate"));
	public static final TagKey<EntityType<?>> GUANO_IMMUNITY = create(AlexsCavesExemplified.prefix("guano_immunity"));
	public static final TagKey<EntityType<?>> CRUSH_IMMUNITY = create(AlexsCavesExemplified.prefix("crush_immunity"));


	public ACExEntityTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, AlexsCavesExemplified.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		tag(GUANO_IMMUNITY).add(
				ACEntityRegistry.UNDERZEALOT.get(),
				ACEntityRegistry.CORRODENT.get(),
				ACEntityRegistry.FORSAKEN.get(),
				ACEntityRegistry.WATCHER.get(),
				ACEntityRegistry.VESPER.get()
		);

		tag(CRUSH_IMMUNITY).add(
				ACEntityRegistry.SUBMARINE.get()
		);

		tag(ACID_TO_CAT).add(
				EntityType.CAT,
				EntityType.OCELOT
		);

		tag(LICOWITCH_HATE).add(
				EntityType.VILLAGER,
				EntityType.IRON_GOLEM
		);

		tag(ACID_TO_FISH).add(
				EntityType.COD,
				EntityType.SALMON,
				EntityType.TROPICAL_FISH,
				EntityType.PUFFERFISH,
				ACEntityRegistry.TRIPODFISH.get()
		)
				.addOptional(ResourceLocation.parse("alexsmobs:flying_fish"))
				.addOptional(ResourceLocation.parse("alexsmobs:blob_fish"))
				.addOptional(ResourceLocation.parse("alexsmobs:cosmic_cod"))
				.addOptional(ResourceLocation.parse("alexsmobs:devils_hole_pupfish"))
				.addOptional(ResourceLocation.parse("alexsmobs:catfish"));


		tag(CAN_RABIES).add(
				EntityType.PLAYER,
				EntityType.BAT,
				EntityType.CAMEL,
				EntityType.CAT,
				EntityType.COW,
				EntityType.DONKEY,
				EntityType.HORSE,
				EntityType.MOOSHROOM,
				EntityType.MULE,
				EntityType.OCELOT,
				EntityType.PIG,
				EntityType.RABBIT,
				EntityType.SHEEP,
				EntityType.VILLAGER,
				EntityType.WANDERING_TRADER,
				EntityType.DOLPHIN,
				EntityType.FOX,
				EntityType.GOAT,
				EntityType.LLAMA,
				EntityType.TRADER_LLAMA,
				EntityType.PANDA,
				EntityType.PIGLIN,
				EntityType.POLAR_BEAR,
				EntityType.TRADER_LLAMA,
				EntityType.WOLF,
				EntityType.HOGLIN,
				EntityType.PIGLIN_BRUTE,
				ACEntityRegistry.CORRODENT.get(),
				ACEntityRegistry.VESPER.get(),
				ACEntityRegistry.UNDERZEALOT.get(),
				ACEntityRegistry.ATLATITAN.get(),
				ACEntityRegistry.FORSAKEN.get(),
				ACEntityRegistry.GLOOMOTH.get(),
				ACEntityRegistry.LICOWITCH.get(),
				ACEntityRegistry.RAYCAT.get(),
				ACEntityRegistry.RELICHEIRUS.get(),
				ACEntityRegistry.SUBTERRANODON.get(),
				ACEntityRegistry.TREMORSAURUS.get(),
				ACEntityRegistry.VALLUMRAPTOR.get()
		).addTag(EntityTypeTags.RAIDERS)
				.addOptional(ResourceLocation.parse("alexsmobs:anteater"))
				.addOptional(ResourceLocation.parse("alexsmobs:bison"))
				.addOptional(ResourceLocation.parse("alexsmobs:cachalot_whale"))
				.addOptional(ResourceLocation.parse("alexsmobs:capuchin_monkey"))
				.addOptional(ResourceLocation.parse("alexsmobs:dropbear"))
				.addOptional(ResourceLocation.parse("alexsmobs:elephant"))
				.addOptional(ResourceLocation.parse("alexsmobs:froststalker"))
				.addOptional(ResourceLocation.parse("alexsmobs:gazelle"))
				.addOptional(ResourceLocation.parse("alexsmobs:gelada_monkey"))
				.addOptional(ResourceLocation.parse("alexsmobs:gorilla"))
				.addOptional(ResourceLocation.parse("alexsmobs:grizzly_bear"))
				.addOptional(ResourceLocation.parse("alexsmobs:jerboa"))
				.addOptional(ResourceLocation.parse("alexsmobs:kangaroo"))
				.addOptional(ResourceLocation.parse("alexsmobs:maned_wolf"))
				.addOptional(ResourceLocation.parse("alexsmobs:moose"))
				.addOptional(ResourceLocation.parse("alexsmobs:bunfungus"))
				.addOptional(ResourceLocation.parse("alexsmobs:murmur"))
				.addOptional(ResourceLocation.parse("alexsmobs:orca"))
				.addOptional(ResourceLocation.parse("alexsmobs:platypus"))
				.addOptional(ResourceLocation.parse("alexsmobs:raccoon"))
				.addOptional(ResourceLocation.parse("alexsmobs:rhinoceros"))
				.addOptional(ResourceLocation.parse("alexsmobs:sea_bear"))
				.addOptional(ResourceLocation.parse("alexsmobs:seal"))
				.addOptional(ResourceLocation.parse("alexsmobs:skunk"))
				.addOptional(ResourceLocation.parse("alexsmobs:maned_wolf"))
				.addOptional(ResourceLocation.parse("alexsmobs:snow_leopard"))
				.addOptional(ResourceLocation.parse("alexsmobs:sugar_glider"))
				.addOptional(ResourceLocation.parse("alexsmobs:tasmanian_devil"))
				.addOptional(ResourceLocation.parse("alexsmobs:tiger"))
				.addOptional(ResourceLocation.parse("alexsmobs:tusklin"));

		tag(VESPER_HUNT).add(
				EntityType.BAT,
				EntityType.SPIDER,
				EntityType.CAVE_SPIDER
		)
				.addOptional(ResourceLocation.parse("alexsmobs:cockroach"));
	}

	private static TagKey<EntityType<?>> create(ResourceLocation rl) {
		return TagKey.create(Registries.ENTITY_TYPE, rl);
	}

	@Override
	public String getName() {
		return "AMI Entity Tags";
	}
}
