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
import net.minecraftforge.common.data.ExistingFileHelper;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ACEEntityTagGenerator extends EntityTypeTagsProvider {
	public static final TagKey<EntityType<?>> ACID_TO_CAT = create(AlexsCavesExemplified.prefix("acid_to_cat"));
	public static final TagKey<EntityType<?>> ACID_TO_FISH = create(AlexsCavesExemplified.prefix("acid_to_fish"));
	public static final TagKey<EntityType<?>> CAN_RABIES = create(AlexsCavesExemplified.prefix("can_rabies"));
	public static final TagKey<EntityType<?>> VESPER_HUNT = create(AlexsCavesExemplified.prefix("vesper_hunt"));
	public static final TagKey<EntityType<?>> LICOWITCH_HATE = create(AlexsCavesExemplified.prefix("licowitch_hate"));


	public ACEEntityTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, AlexsCavesExemplified.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

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
				.addOptional(new ResourceLocation("alexsmobs:flying_fish"))
				.addOptional(new ResourceLocation("alexsmobs:blob_fish"))
				.addOptional(new ResourceLocation("alexsmobs:cosmic_cod"))
				.addOptional(new ResourceLocation("alexsmobs:devils_hole_pupfish"))
				.addOptional(new ResourceLocation("alexsmobs:catfish"));


		tag(CAN_RABIES).add(
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
				.addOptional(new ResourceLocation("alexsmobs:anteater"))
				.addOptional(new ResourceLocation("alexsmobs:bison"))
				.addOptional(new ResourceLocation("alexsmobs:cachalot_whale"))
				.addOptional(new ResourceLocation("alexsmobs:capuchin_monkey"))
				.addOptional(new ResourceLocation("alexsmobs:dropbear"))
				.addOptional(new ResourceLocation("alexsmobs:elephant"))
				.addOptional(new ResourceLocation("alexsmobs:froststalker"))
				.addOptional(new ResourceLocation("alexsmobs:gazelle"))
				.addOptional(new ResourceLocation("alexsmobs:gelada_monkey"))
				.addOptional(new ResourceLocation("alexsmobs:gorilla"))
				.addOptional(new ResourceLocation("alexsmobs:grizzly_bear"))
				.addOptional(new ResourceLocation("alexsmobs:jerboa"))
				.addOptional(new ResourceLocation("alexsmobs:kangaroo"))
				.addOptional(new ResourceLocation("alexsmobs:maned_wolf"))
				.addOptional(new ResourceLocation("alexsmobs:moose"))
				.addOptional(new ResourceLocation("alexsmobs:bunfungus"))
				.addOptional(new ResourceLocation("alexsmobs:murmur"))
				.addOptional(new ResourceLocation("alexsmobs:orca"))
				.addOptional(new ResourceLocation("alexsmobs:platypus"))
				.addOptional(new ResourceLocation("alexsmobs:raccoon"))
				.addOptional(new ResourceLocation("alexsmobs:rhinoceros"))
				.addOptional(new ResourceLocation("alexsmobs:sea_bear"))
				.addOptional(new ResourceLocation("alexsmobs:seal"))
				.addOptional(new ResourceLocation("alexsmobs:skunk"))
				.addOptional(new ResourceLocation("alexsmobs:maned_wolf"))
				.addOptional(new ResourceLocation("alexsmobs:snow_leopard"))
				.addOptional(new ResourceLocation("alexsmobs:sugar_glider"))
				.addOptional(new ResourceLocation("alexsmobs:tasmanian_devil"))
				.addOptional(new ResourceLocation("alexsmobs:tiger"))
				.addOptional(new ResourceLocation("alexsmobs:tusklin"));

		tag(VESPER_HUNT).add(
				EntityType.BAT,
				EntityType.SPIDER,
				EntityType.CAVE_SPIDER
		)
				.addOptional(new ResourceLocation("alexsmobs:cockroach"));
	}

	private static TagKey<EntityType<?>> create(ResourceLocation rl) {
		return TagKey.create(Registries.ENTITY_TYPE, rl);
	}

	@Override
	public String getName() {
		return "AMI Entity Tags";
	}
}
