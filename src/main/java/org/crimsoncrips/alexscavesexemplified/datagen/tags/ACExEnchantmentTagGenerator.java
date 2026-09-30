package org.crimsoncrips.alexscavesexemplified.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.enchantment.ACExEnchants;

import java.util.concurrent.CompletableFuture;

public class ACExEnchantmentTagGenerator extends TagsProvider<Enchantment> {

	public ACExEnchantmentTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
		super(output, Registries.ENCHANTMENT, provider, AlexsCavesExemplified.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(EnchantmentTags.IN_ENCHANTING_TABLE).add(ACExEnchants.MAGNETICISM);
		tag(EnchantmentTags.NON_TREASURE).add(ACExEnchants.MAGNETICISM);
		tag(EnchantmentTags.TRADEABLE).add(ACExEnchants.MAGNETICISM);
		tag(EnchantmentTags.ON_RANDOM_LOOT).add(ACExEnchants.MAGNETICISM);
	}

	@Override
	public String getName() {
		return "Alex's Caves Exemplified Enchantment Tags";
	}
}
