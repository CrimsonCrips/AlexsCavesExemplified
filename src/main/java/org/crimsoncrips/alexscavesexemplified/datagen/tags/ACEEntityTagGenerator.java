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

	public ACEEntityTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, AlexsCavesExemplified.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		tag(ACID_TO_CAT).add(
				EntityType.CAT,
				EntityType.OCELOT
		);

		tag(ACID_TO_FISH).add(
				EntityType.COD,
				EntityType.SALMON,
				EntityType.TROPICAL_FISH,
				EntityType.PUFFERFISH,
				AMCompat.amEntityType(1),
				AMCompat.amEntityType(2),
				AMCompat.amEntityType(3),
				AMCompat.amEntityType(4),
				AMCompat.amEntityType(5)
		);

		tag(ACID_TO_FISH).add(
				EntityType.COD,
				EntityType.SALMON,
				EntityType.TROPICAL_FISH,
				EntityType.PUFFERFISH,
				AMCompat.amEntityType(1),
				AMCompat.amEntityType(2),
				AMCompat.amEntityType(3),
				AMCompat.amEntityType(4),
				AMCompat.amEntityType(5),
				ACEntityRegistry.TRIPODFISH.get()
		);

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
				AMCompat.amEntityType(6),
				AMCompat.amEntityType(7),
				AMCompat.amEntityType(8),
				AMCompat.amEntityType(9),
				AMCompat.amEntityType(10),
				AMCompat.amEntityType(11),
				AMCompat.amEntityType(12),
				AMCompat.amEntityType(13),
				AMCompat.amEntityType(14),
				AMCompat.amEntityType(15),
				AMCompat.amEntityType(16),
				AMCompat.amEntityType(17),
				AMCompat.amEntityType(18),
				AMCompat.amEntityType(19),
				AMCompat.amEntityType(20),
				AMCompat.amEntityType(21),
				AMCompat.amEntityType(22),
				AMCompat.amEntityType(23),
				AMCompat.amEntityType(24),
				AMCompat.amEntityType(25),
				AMCompat.amEntityType(26),
				AMCompat.amEntityType(27),
				AMCompat.amEntityType(28),
				AMCompat.amEntityType(29),
				AMCompat.amEntityType(30),
				AMCompat.amEntityType(31),
				AMCompat.amEntityType(32),
				AMCompat.amEntityType(33),
				AMCompat.amEntityType(34),
				AMCompat.amEntityType(35),
				AMCompat.amEntityType(36),
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
		).addTag(EntityTypeTags.RAIDERS);

		tag(VESPER_HUNT).add(
				AMCompat.amEntityType(37),
				AMCompat.amEntityType(38),
				EntityType.BAT,
				EntityType.SPIDER,
				EntityType.CAVE_SPIDER
		);
	}

	private static TagKey<EntityType<?>> create(ResourceLocation rl) {
		return TagKey.create(Registries.ENTITY_TYPE, rl);
	}

	@Override
	public String getName() {
		return "AMI Entity Tags";
	}
}
