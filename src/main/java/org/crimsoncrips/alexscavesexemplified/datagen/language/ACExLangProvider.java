package org.crimsoncrips.alexscavesexemplified.datagen.language;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public abstract class ACExLangProvider extends LanguageProvider {
	public ACExLangProvider(PackOutput output) {
		super(output, AlexsCavesExemplified.MODID, "en_us");
	}

	public void addEffect(String effectKey, String title, String description){
		this.add("effect.alexscavesexemplified." + effectKey + ".title", title);
		this.add("effect.alexscavesexemplified." + effectKey + ".description", description);
	}

	public void addDeathMessage(String deathKey, String name) {
		this.add("death.attack." + deathKey, name);
	}

	public void addSubtitle(String subtitleKey,String name) {
		this.add("subtitle.alexscavesexemplified.sound." + subtitleKey,name);
	}

	public void addLoreBlock(String subtitleKey, String name) {
		this.add("block." + subtitleKey + ".desc", name);
	}

	public void addEnchantmentDesc(String effectKey, String title,String description){
		this.add("enchantment.alexscavesexemplified." + effectKey, title);
		this.add("enchantment.alexscavesexemplified." + effectKey + ".desc", description);
	}

	public void addAdvancementDesc(String advancementKey, String title,String description){
		this.add("advancement.alexscavesexemplified." + advancementKey, title);
		this.add("advancement.alexscavesexemplified." + advancementKey + ".desc", description);
	}

	public void addMisc(String subtitleKey,String text) {
		this.add("misc.alexscavesexemplified." + subtitleKey,text);
	}


}
