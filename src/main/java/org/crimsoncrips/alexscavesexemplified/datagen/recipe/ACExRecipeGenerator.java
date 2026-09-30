package org.crimsoncrips.alexscavesexemplified.datagen.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;


import java.util.concurrent.CompletableFuture;

public class ACExRecipeGenerator extends ACExRecipeHelper {
	public ACExRecipeGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void buildRecipes(RecipeOutput consumer) {

//		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ACExBlockRegistry.METAL_CAULDRON.get(), 2)
//				.pattern("s s")
//				.pattern("i i")
//				.pattern("iii")
//				.define('s', Ingredient.of(ACBlockRegistry.SCRAP_METAL_PLATE.get()))
//				.define('i', Ingredient.of(ItemTags.IRON_ORES))
//				.unlockedBy("has_item", has(ACBlockRegistry.SCRAP_METAL_PLATE.get()))
//				.save(consumer);






	}
}
