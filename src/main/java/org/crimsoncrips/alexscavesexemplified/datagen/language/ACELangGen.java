package org.crimsoncrips.alexscavesexemplified.datagen.language;

import net.minecraft.data.PackOutput;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;
import org.crimsoncrips.alexscavesexemplified.server.item.ACEItemRegistry;

public class ACELangGen extends ACELangProvider {

	public ACELangGen(PackOutput output) {
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
		this.addEffect("serened","Serened","Serene");

		this.addEntityType(ACExEntityRegistry.GAMMA_NUCLEAR_BOMB,"Gamma Nuclear Bomb");

		this.addDeathMessage("rabial_end","%s died to Rabies");
		this.addDeathMessage("sugar_crash","%s died from a sugar overdose");
		this.addDeathMessage("stomach_damage","%s's stomach exploded");
		this.addDeathMessage("depth_crush","%s got Ocean Gated");
		this.addDeathMessage("sweet_punish","%s got slippered to death");

		this.addSubtitle("tesla_fire","Tesla fired");
		this.addSubtitle("tesla_powerup","Tesla powering");
		this.addSubtitle("tesla_exploding","Tesla exploding");
		this.addSubtitle("caramel_eat","Caramel Cube assimilated");
		this.addSubtitle("sweet_punished","Comeuppance served");
		this.addSubtitle("pspspsps","Pspspspspsps");

		this.addBlock(ACExBlockRegistry.METAL_CAULDRON,"Metal Cauldron");
		this.addBlock(ACExBlockRegistry.PURPLE_SODA_CAULDRON,"Purple Soda Cauldron");
		this.addBlock(ACExBlockRegistry.ACID_CAULDRON,"Acid Cauldron");
		this.addBlock(ACExBlockRegistry.GAMMA_NUCLEAR_BOMB,"Gamma Nuclear Bomb");

		this.addLoreBlock(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_1.getId().toLanguageKey(), "\"Sacrifice I\"");
		this.addLoreBlock(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_2.getId().toLanguageKey(), "\"Sacrifice II\"");
		this.addLoreBlock(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_3.getId().toLanguageKey(), "\"Sacrifice III\"");
		this.addLoreBlock(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_4.getId().toLanguageKey(), "\"Sacrifice IV\"");
		this.addLoreBlock(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_5.getId().toLanguageKey(), "\"Sacrifice V\"");
		this.addLoreBlock(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_6.getId().toLanguageKey(), "\"Sacrifice VII\"");
		this.addLoreBlock(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_7.getId().toLanguageKey(), "\"Sacrifice VII\"");
		this.addLoreBlock(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_8.getId().toLanguageKey(), "\"Sacrifice VIII\"");
		this.addLoreBlock(ACExBlockRegistry.CAVE_PAINTING_SACRIFICE_9.getId().toLanguageKey(), "\"Sacrifice IX\"");

		this.addLoreBlock(ACExBlockRegistry.GAMMA_NUCLEAR_BOMB.getId().toLanguageKey(), "The Embodiment of Desolation.");

		this.addItem(ACEItemRegistry.ICE_CREAM_CONE, "Ice Cream Cone");


		this.addEnchantmentDesc("magneticism","Magneticism", "Boosts Magnetic Caves related tools");

		this.addAdvancementDesc("root","Alexs Caves Exemplified", "An Exemplified Universe.");
		this.addAdvancementDesc("magnetic_caves","Magnefied Reality", "Magnetic Caves Additions");
		this.addAdvancementDesc("forlorn_hollows","Forlornliness Syndrome", "Forlorn Hollows Additions");
		this.addAdvancementDesc("primordial_caves","Primoredial Features", "Primordial Caves Additions");
		this.addAdvancementDesc("abyssal_chasm","Abyssmal Pressure", "Abysmal Chasm Additions");
		this.addAdvancementDesc("candy_cavity","Candiesz Nuts", "Candy Cavity Additions");
		this.addAdvancementDesc("toxic_caves","Talkicity Chat", "Toxic Caves Additions");

		this.addAdvancementDesc("galena_steal","Double-Edged Magnetism", "Use the Magneticism enchantment on a galena gauntlet to steal a teletor's weapon");
		this.addAdvancementDesc("teletor_rearm","Magnetized Rearming", "Have an unarmed teletor rearm itself with your tools");
		this.addAdvancementDesc("tesla_shock","Shocking Therapy", "Get electrocuted by a nearby tesla coil");
		this.addAdvancementDesc("magnerip","Magneripped", "Have your magnetic items ripped off while with weakness");
		this.addAdvancementDesc("self_destruct","Imperial Burst Drones", "Have a notor self-destruct");
		this.addAdvancementDesc("resizing","Holographic Resizing", "Resize a hologram with magnetic ingots");
		this.addAdvancementDesc("metal_cauldron","Reinforced Cauldron", "Use metal to reinforce a cauldron to allow for caves-related liquid replication");
		this.addAdvancementDesc("soda_replication","Soda Addiction", "Use a purple soda bottle on a metal cauldron to duplicate soda");
		this.addAdvancementDesc("acidic_replication","Acidicity", "Place a acidrock above a metal cauldron to duplicate acid");

		this.addAdvancementDesc("light_repel","BACK, DEMON!", "Ward off attacking forlorn residents with lights in your hand");
		this.addAdvancementDesc("rabial","Rat Flu", "Get afflicted with rabies");
		this.addAdvancementDesc("rabial_spread","Black Death v2", "Spread rabies to other entities");
		this.addAdvancementDesc("burst_out","Pop Goes The Weasel!", "Uncover forlorn residents when digging inside the Forlorn Hollows");
		this.addAdvancementDesc("knawing","Teething", "Have a corrodent knaw on a dropped item");
		this.addAdvancementDesc("dark_respect","Shadow Wizard Money Gang", "Gain the respect of a fellow underzealot through clothing");
		this.addAdvancementDesc("gloomoth_trade","Bug Catching", "Trade in a gloomoth to a underzealot by leashing it");
		this.addAdvancementDesc("vesper_trade","Covid Transaction", "Trade in a vesper to a underzealot by leashing it");
		this.addAdvancementDesc("corrodent_trade","Cult Assimilation", "Trade in a corrodent to a underzealot by leashing it");
		this.addAdvancementDesc("shot_down","Dark Duck Hunt", "Shoot down a vesper");
		this.addAdvancementDesc("solidified","Statutory Totems", "Observe a watcher solidify into a totem");

		this.addAdvancementDesc("ecological_reputation","Eco-Friendly", "Affect the local ecosystem of the abyss in a positive or negative way...");
		this.addAdvancementDesc("hullbreaker_reputation","Destabilized Hunting", "Kill a Hullbreaker to affect your reputation...");
		this.addAdvancementDesc("submarine_bump","Collateral Diving", "Bump into a fish in a submarine");
		this.addAdvancementDesc("mine_ownership","Nautical Warfare", "Make your own mine guardian,loyal to whoever made it");
		this.addAdvancementDesc("noon_guardian","Pufferboom", "Name a mine guardian, 'Noon'");
		this.addAdvancementDesc("nuclear_mines","A Great place for our nuclear test!", "Arm a mine guardian with a nuke, might not be a great idea...");
		this.addAdvancementDesc("nuclear_kill","Underwater Defusion", "Kill a nuclear mine guardian,at least the fish are happy");
		this.addAdvancementDesc("poisonous_skin","Poisonous Pigs", "Touch a sea pig");
		this.addAdvancementDesc("gossamer_feed","Bio-Farm", "Drop marine snow towards a gossamer worm");

		this.addAdvancementDesc("gluttony","Wreck-That Candy", "Consume candy blocks by right clicking,while hungry");
		this.addAdvancementDesc("sugar_crashed","Candy Overdose", "After effects of sugar rushes");
		this.addAdvancementDesc("frostmint_explode","Dropped Frostsplosions", "Frostmint blocks arent the only way to explode yknow..");
		this.addAdvancementDesc("frostmint_freeze","Cold Freeze", "Freeze liquid with a frostmint spear");
		this.addAdvancementDesc("sticky_soda","Sticky Situation v2", "Purple Soda is made of sugar yknow that?");
		this.addAdvancementDesc("purple_coloring","Purpled", "Recolor an uncolored dyeable armor in purple soda");
		this.addAdvancementDesc("radiant_wrath","Radiant Wrath", "Amplify your Sugar Staff with a Radiant Essence in your offhand");
		this.addAdvancementDesc("dropped_consumption","Drop Of Sating", "Feed your pet Sack Of Sating outside your inventory");
		this.addAdvancementDesc("full_consumption","Filled Sating", "Sack Of Sating will eat all pickup-able foods if the food cannot be picked up");
		this.addAdvancementDesc("iced_freeze","Yummy Snowball", "Throw ice cream");
		this.addAdvancementDesc("breaking_candy","Breaking Candy", "Cook gelatin by heating up a filled cauldron and dropping bones and dyes");
		this.addAdvancementDesc("ice_cream","Favorite Sweet", "Make ice cream with wafer blocks and ice cream blocks from bottom to top!");
		this.addAdvancementDesc("overdrived_conversion","Overdrived Conversion", "Power up your conversion crucible with radiant essence");
		this.addAdvancementDesc("amputate","Gingerchops", "Chop off a limp from a gingerbread man");
		this.addAdvancementDesc("crumby_rage","Appalled Eating", "Eat gingerbread crumbs in front of them");
		this.addAdvancementDesc("interrupt","Undercooking", "Interrupt a sleeping gummybear by attacking it");
		this.addAdvancementDesc("feed_speedup","Jellybean Speedrun", "Speed up a gummybear's hibernation with sweetish fish");

		this.addAdvancementDesc("egg_stealing","Eggnapping", "Steal a dinosaur egg, and anger its parents.");
		this.addAdvancementDesc("splat","Splat!", "Get stepped on by a big dinosaur");
		this.addAdvancementDesc("riding_splat","Trampling", "Control an atlatitan to stomp on puny mobs");
		this.addAdvancementDesc("paint_effects","P2W Skins", "Paint a dinosaur, to give it specific effects");
		this.addAdvancementDesc("egg_sacrifice","Egg Fuel", "Sacrifice an atlatitan egg to the volcanoes");
		this.addAdvancementDesc("volcanic_sacrifice","Lux Reborn", "Sacrifice a live baby atlatitan");
		this.addAdvancementDesc("sacrifice_painting","Prophesized Sacrifice", "Paint a depiction of a volcanic sacrifice after defeating the Luxtructosaurus");
		this.addAdvancementDesc("drop_food","Feeding the Past", "Drop the carnivores some meat");
		this.addAdvancementDesc("seethed_taming","Satisfied Dino", "Tame a Tremor by seething it with enough meat");
		this.addAdvancementDesc("propogate","Primordial Propogation", "Propogate flora in the primordial caves");
		this.addAdvancementDesc("serened","Serene Neutrality", "Inflict serened on yourself or another mob with serene salad, causing non-players to not attack unless attacked");

		this.addAdvancementDesc("deathly_radiation","Deathly Radiation", "Have your body submit to immense radiation");
		this.addAdvancementDesc("washing_radiation","Nuclear Wash", "Wash off radiation with water, soap is applicable");
		this.addAdvancementDesc("kirov_reporting","Kirov, Reporting!", "Drop an explosive, during flight");
		this.addAdvancementDesc("feed_roach","Mutated Asmongold Viewer", "Feed a gammaroach");
		this.addAdvancementDesc("chain_reaction","Chain Reaction", "Have a chain reaction of nucleeper explosions");
		this.addAdvancementDesc("nucleeper_annhilation","World War Nucleeper", "Chain react 10 nucleepers");
		this.addAdvancementDesc("convert_fish","Ocean Pollution", "Convert a fish into a radgill with acid");
		this.addAdvancementDesc("convert_cat","+1 Braincell", "Convert a cat into a raycat with acid");
		this.addAdvancementDesc("gamma_tremorzilla","Gammazilla", "Convert a tremorzilla into a gamma tremorzilla with a gamma nuclear explosion");
		this.addAdvancementDesc("gamma_tremorzilla_kill","Legend Killer", "Kill off a gamma tremorzilla");
		this.addAdvancementDesc("defusing","Defused Payload", "Defuse a Nucleeper");

	}
}
