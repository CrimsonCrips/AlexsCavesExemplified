package org.crimsoncrips.alexscavesexemplified.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ACERegistryDataGenerator extends DatapackBuiltinEntriesProvider {

	//Copy from TF
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.DAMAGE_TYPE, ACEDamageTypes::bootstrap)
			.add(Registries.CONFIGURED_FEATURE, ACEFeatures::generateFeatureConfigurations)
			.add(Registries.PLACED_FEATURE, ACEFeatures::generateFeaturePlacements);

	public ACERegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, BUILDER, Set.of(AlexsCavesExemplified.MODID));
	}
}
