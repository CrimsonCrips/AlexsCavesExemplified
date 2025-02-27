package org.crimsoncrips.alexscavesexemplified.datagen.language.locale;

import net.minecraft.data.PackOutput;
import org.crimsoncrips.alexscavesexemplified.datagen.language.ACELangProvider;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACEBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.item.ACEItemRegistry;

public class ACEEnglishGenerator extends ACELangProvider {

	public ACEEnglishGenerator(PackOutput output) {
		super(output);
	}


	@Override
	protected void addTranslations() {
		this.addMisc("feature_disabled", "Feature Disabled");
		this.addMisc("ace_book.title","Alexs Caves Exemplified");
		this.addMisc("ace_book.description","Welcome to an exemplified reality!");
		this.addMisc("locator_protection","Your locator has been infused with the star.");
		this.addMisc("candy_warn_0","Mom: That's enough candy for today my child.");
		this.addMisc("candy_warn_1","Dad: That's enough candy, child.");


		this.addEffect("rabial","Rabial Infection","Deathly Nervous Disease");
		this.addEffect("sugar_crash","Sugar Crash","Sugar Crashed");

		this.addDeathMessage("rabial_end",0,"%s died to Rabies");
		this.addDeathMessage("sugar_crash",0,"%s died from a sugar overdose");
		this.addDeathMessage("sugar_crash",1,"%s died from a chocoverdose");
		this.addDeathMessage("stomach_damage",0,"%s's stomach exploded");
		this.addDeathMessage("depth_crush",0,"%s died from the crushing depths");
		this.addDeathMessage("depth_crush",1,"%s got Ocean Gated");
		this.addDeathMessage("depth_crush",2,"%s imploded");
		this.addDeathMessage("depth_crush",3,"%s flattened as a pancake");
		this.addDeathMessage("sweet_punish",0,"%s disobeyed");
		this.addDeathMessage("sweet_punish",1,"%s got slippered to death");
		this.addDeathMessage("sweet_punish",2,"%s got spanked for disobedience");
		this.addDeathMessage("sweet_punish",3,"%s punihsment was bitter-sweet");
		this.addDeathMessage("sweet_punish",4,"%s got their sweet relief");

		this.addSubtitle("tesla_fire","Tesla fired");
		this.addSubtitle("tesla_powerup","Tesla powering");
		this.addSubtitle("tesla_exploding","Tesla exploding");
		this.addSubtitle("caramel_eat","Caramel Cube assimilated");
		this.addSubtitle("sweet_punished","Comeuppance served");
		this.addSubtitle("pspspsps","Pspspspspsps");

		this.addBlock(ACEBlockRegistry.METAL_CAULDRON,"Metal Cauldron");
		this.addBlock(ACEBlockRegistry.PURPLE_SODA_CAULDRON,"Purple Soda Cauldron");
		this.addBlock(ACEBlockRegistry.ACID_CAULDRON,"Acid Cauldron");

		this.addItem(ACEItemRegistry.ICE_CREAM_CONE, "Ice Cream Cone");

		this.addEnchantmentDesc("magneticism","Magneticism", "Boosts Magnetic Caves related tools");

		this.addAdvancementDesc("root","Alexs Caves Exemplified", "An Exemplified Universe.");
		this.addAdvancementDesc("magnetic_caves","Magnefied Reality", "Magnetic Caves Additions");
		this.addAdvancementDesc("forlorn_hollows","Forlornliness Syndrome", "Forlorn Hollows Additions");
		this.addAdvancementDesc("primordial_caves","Primoredial Features", "Primordial Caves Additions");
		this.addAdvancementDesc("abyssal_chasm","Abyssmal Pressure", "Abysmal Chasm Additions");
		this.addAdvancementDesc("candy_cavity","Candiesz Nuts", "Candy Cavity Additions");
		this.addAdvancementDesc("toxic_caves","Talkicity Chat", "Toxic Caves Additions");


	}
}
