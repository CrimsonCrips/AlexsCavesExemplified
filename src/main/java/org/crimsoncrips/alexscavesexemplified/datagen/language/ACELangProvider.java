package org.crimsoncrips.alexscavesexemplified.datagen.language;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.text.WordUtils;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import java.util.function.Supplier;

public abstract class ACELangProvider extends LanguageProvider {
	public ACELangProvider(PackOutput output) {
		super(output, AlexsCavesExemplified.MODID, "en_us");
	}

	public void addEffect(String effectKey, String title, String description){
		this.add("effect.alexscavesexemplified." + effectKey + ".title", title);
		this.add("effect.alexscavesexemplified." + effectKey + ".description", description);
	}

	public void addDeathMessage(String deathKey, int number, String name) {
		this.add("death.alexscavesexemplified.attack." + deathKey + "_" + number, name);
	}

	public void addSubtitle(String subtitleKey,String name) {
		this.add("subtitle.alexscavesexemplified.sound." + subtitleKey,name);
	}

	public void addEnchantmentDesc(String effectKey, String title,String description){
		this.add("enchantment.alexscavesexemplified." + effectKey, title);
		this.add("enchantment.alexscavesexemplified." + effectKey + ".desc", description);
	}

	public void addAdvancementDesc(String advancementKey, String title,String description){
		this.add("advancement.alexscavesexemplified." + advancementKey, title);
		this.add("advancement.alexscavesexemplified." + advancementKey + ".desc", description);
	}

	public void addMisc(String subtitleKey,String name) {
		this.add("misc.alexscavesexemplified." + subtitleKey,name);
	}


}
