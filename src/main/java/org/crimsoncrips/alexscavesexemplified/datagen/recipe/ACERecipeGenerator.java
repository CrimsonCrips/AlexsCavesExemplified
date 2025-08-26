package org.crimsoncrips.alexscavesexemplified.datagen.recipe;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;


import java.util.function.Consumer;

public class ACERecipeGenerator extends ACERecipeHelper {
	public ACERecipeGenerator(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ACExBlockRegistry.METAL_CAULDRON.get(), 2)
				.pattern("s s")
				.pattern("i i")
				.pattern("iii")
				.define('s', Ingredient.of(ACBlockRegistry.SCRAP_METAL_PLATE.get()))
				.define('i', Ingredient.of(ItemTags.IRON_ORES))
				.unlockedBy("has_item", has(ACBlockRegistry.SCRAP_METAL_PLATE.get()))
				.save(consumer);






	}
}
