package org.crimsoncrips.alexscavesexemplified.datagen.language;

import net.minecraft.data.PackOutput;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;

public class ACExLangGen extends ACExLangProvider {

	public ACExLangGen(PackOutput output) {
		super(output);
	}


	@Override
	protected void addTranslations() {
		this.addMisc("feature_disabled", "Feature Disabled");
		this.addMisc("config_title", "Alex's Caves Exemplified Config");
		this.addMisc("config_tab_client", "Client");
		this.addMisc("config_tab_general", "General");
		this.addMisc("config_tab_targets", "Targets");
		this.addMisc("config_note_remote", "Connected to a server, these settings can only be changed on the server");
		this.add("key.alexscavesexemplified.submarine_sonar", "Submarine Sonar");
		this.add("key.alexscavesexemplified.submarine_telegraph", "Submarine Telegraph");
		this.add("key.categories.alexscavesexemplified", "Alex's Caves Exemplified");
		this.addMisc("locator_protection","Your locator has been infused with the star.");



		this.addEffect("rabial","Rabial Infection","Deathly Nervous Disease");
		this.addEffect("sugar_crash","Sugar Crash","Sugar Crashed");
		this.addEffect("serened","Serened","Serene");
		this.addEffect("transmutation","Transmutation","Turns small creatures into candy");
		this.add("item.minecraft.potion.effect.transmutation", "Potion of Transmutation");
		this.add("item.minecraft.splash_potion.effect.transmutation", "Splash Potion of Transmutation");
		this.add("item.minecraft.lingering_potion.effect.transmutation", "Lingering Potion of Transmutation");
		this.add("item.minecraft.tipped_arrow.effect.transmutation", "Arrow of Transmutation");
		this.addMisc("irradiated_crop", "Irradiated");
		this.addMisc("sugar_staff_wheel", "Sugar Staff");
		this.addMisc("sugar_staff_base_peppermints", "Peppermints");
		this.addMisc("sugar_staff_base_hex", "Hex");
		this.addMisc("sugar_staff_requires_charges", "Requires %s radiant charges");
		this.addMisc("sugar_staff_peppernami", "Peppernami");
		this.addMisc("sugar_staff_radiant_shield", "Radiant Shield");
		this.addMisc("sugar_staff_frost_barrage", "Frost Barrage");
		this.addMisc("sugar_staff_pepper_crush", "Pepper Crush");
		this.addMisc("sugar_staff_hailstorm", "Hailstorm");
		this.addMisc("sugar_staff_line_of_gum", "Line Of Gum");
		this.addMisc("sugar_staff_gummy_swarm", "Gummy Swarm");
		this.addMisc("sugar_staff_sweet_release", "Sweet Release");
		this.addMisc("wheel_editor_title", "Sugar Staff Wheel Layout");
		this.addMisc("wheel_editor_hint", "Drag to reorder - Right-click to hide - Click a hidden ability to add it");
		this.addMisc("wheel_editor_hidden", "Hidden Abilities");
		this.addMisc("wheel_editor_empty", "Drag abilities here");
		this.addMisc("wheel_editor_reset", "Reset");
		this.addMisc("wheel_editor_open", "Edit Layout");
		this.addMisc("wheel_editor_cost", "Costs %s radiant charges");
		this.addMisc("max_sugar_staff", "Sugar Staff enchanted with every enchantment at max level");
		this.addMisc("radiant_charges_set", "Set %s's radiant charges to %s");
		this.addMisc("sweet_release_started", "Started Sweet Release on %s");
		this.addMisc("sweet_release_started_multiple", "Started Sweet Release on %s entities");
		this.addMisc("sweet_release_failed", "No valid targets for Sweet Release");
		this.addMisc("resistant_warden", "Summoned a Warden with infinite Resistance XI");
		this.addMisc("radiant_charges_set_multiple", "Set the radiant charges of %s players to %s");
		this.addMisc("deep_reputation", "%s's deep one reputation: %s (%s)");
		this.addMisc("deep_reputation_failed", "Could not read deep one reputation");
		this.addMisc("magic_conch_refused", "The deep ones refuse to answer your call");
		this.addMisc("deep_reaction_aggressive", "Aggressive");
		this.addMisc("deep_reaction_stalking", "Stalking");
		this.addMisc("deep_reaction_neutral", "Neutral");
		this.addMisc("deep_reaction_helpful", "Helpful");
		this.addMisc("sugar_staff_no_room", "No room to summon");
		this.addMisc("sugar_staff_no_charge", "No radiant charges");
		this.addMisc("sugar_staff_no_target", "No target in sight");
		this.addMisc("sugar_staff_cooldown", "Recharging");
		this.addMisc("gamma_wheel", "Gamma Tremorzilla");
		this.addMisc("gamma_waste_spit", "Waste Spit");
		this.addMisc("gamma_spike_laser", "Spike Laser");
		this.addMisc("gamma_sludge_rain", "Sludge Rain");
		this.addMisc("gamma_nuke_beam", "Nuke Beam");
		this.addMisc("gamma_spike_trials", "Spike Trials");
		this.addMisc("gamma_spike_stomp", "Spike Stomp");
		this.addMisc("gamma_controlled_laser", "Controlled Laser");
		this.addMisc("gamma_busy", "Tremorzilla is busy");
		this.addMisc("gamma_not_charged", "Beam not charged");
		this.addMisc("sugar_staff_requires_punting", "Requires Peppermint Punting");
		this.addMisc("sugar_staff_requires_max", "Requires every Sugar Staff enchantment at max level");
		this.add("config.jade.plugin_alexscavesexemplified.gamma_name", "Gamma Nuclear Bomb Name");
		this.add("config.jade.plugin_alexscavesexemplified.mine_guardian_owner", "Mine Guardian Owner");
		this.add("config.jade.plugin_alexscavesexemplified.preserved_creature", "Preserved Amber Creature");
		this.add("config.jade.plugin_alexscavesexemplified.monolith_summon", "Amber Monolith Summon Timer");
		this.addMisc("monolith_creature", "Summons: %s x%s");
		this.addMisc("monolith_summon", "Next summon: %s");
		this.addMisc("monolith_summoning", "Summoning...");
		this.addMisc("mine_guardian_owner", "Owner: %s");
		this.addMisc("mine_guardian_owner_none", "N/A");
		this.addMisc("hologram_scale", "Hologram Scale: %s/%s");
		this.addMisc("max_sectumsempra", "Filled %s's Sectumsempra bar");
		this.addMisc("max_sectumsempra_multiple", "Filled the Sectumsempra bar of %s players");
		this.add("book.alexscavesexemplified.root", "Exemplified Compendium");
		this.add("book.alexscavesexemplified.general_features", "General Features");
		this.add("book.alexscavesexemplified.switch_exemplified", "Rewrite into the Exemplified Compendium");
		this.add("book.alexscavesexemplified.switch_base", "Rewrite into the Cave Compendium");
		this.add("container.alexscavesexemplified.quarry_upgrades.status", "+%s%% Speed, Fortune %s");
		this.add("container.alexscavesexemplified.quarry_upgrades.speed", "+%s%% Speed");
		this.addMisc("linked_submarine", "Linked to a submarine");
		this.addMisc("submarine_linked", "Controller linked to the submarine");
		this.addMisc("submarine_remote_start", "Driving the submarine, press %s to return");
		this.addMisc("submarine_remote_out_of_range", "The linked submarine is out of range");
		this.addMisc("submarine_remote_in_use", "Someone else is controlling this submarine");
		this.addMisc("submarine_tracker_distance", "%s blocks");
		this.addMisc("engine_telegraph", "Telegraph");
		this.addMisc("engine_order_astern", "Astern");
		this.addMisc("engine_order_all_stop", "All Stop");
		this.addMisc("engine_order_ahead_slow", "Ahead Slow");
		this.addMisc("engine_order_ahead_half", "Ahead Half");
		this.addMisc("engine_order_ahead_full", "Ahead Full");
		this.addMisc("engine_order_ahead_flank", "Ahead Flank");
		this.addMisc("depth_resistance", "%s Blocks Crush Depth");

		this.addEntityType(ACExEntityRegistry.GAMMA_NUCLEAR_BOMB,"Gamma Nuclear Bomb");
		this.addEntityType(ACExEntityRegistry.GUMWIRM,"Gumwirm");
		this.addEntityType(ACExEntityRegistry.GUMWIRM_SEGMENT,"Gumwirm Segment");
		this.addEntityType(ACExEntityRegistry.SPIKES,"Spikes");
		this.addEntityType(ACExEntityRegistry.TOXIC_WASTE,"Toxic Waste");
		this.addEntityType(ACExEntityRegistry.TOXIC_BARREL,"Toxic Barrel");
		this.addEntityType(ACExEntityRegistry.SIPHONED_SOUL,"Siphoned Soul");
		this.addEntityType(ACExEntityRegistry.CURIOSITY_OFFERING,"Curiosity Offering");
		this.addEntityType(ACExEntityRegistry.DARK_RITUAL,"Dark Ritual");
		this.add("entity.alexscavesexemplified.gamma_tremorzilla", "Gamma Tremorzilla");

		this.addDeathMessage("rabial_end","%s died to Rabies");
		this.addDeathMessage("sugar_crash","%s died from a sugar overdose");
		this.addDeathMessage("stomach_damage","%s's stomach exploded");
		this.addDeathMessage("depth_crush","%s got Ocean Gated");

		this.addSubtitle("tesla_fire","Tesla fired");
		this.addSubtitle("tesla_powerup","Tesla powering");
		this.addSubtitle("tesla_exploding","Tesla exploding");
		this.addSubtitle("caramel_eat","Caramel Cube assimilated");

		this.addBlock(ACExBlockRegistry.METAL_CAULDRON,"Metal Cauldron");
		this.addBlock(ACExBlockRegistry.PURPLE_SODA_CAULDRON,"Purple Soda Cauldron");
		this.addBlock(ACExBlockRegistry.ACID_CAULDRON,"Acid Cauldron");
		this.addBlock(ACExBlockRegistry.GAMMA_NUCLEAR_BOMB,"Gamma Nuclear Bomb");
		this.addBlock(ACExBlockRegistry.PRESERVED_AMBER,"Preserved Amber");
		this.addBlock(ACExBlockRegistry.DARK_MONOLITH,"Dark Monolith");
		this.add("block.alexscavesexemplified.preserved_amber.contains","Contains: %s");
		this.add("block.alexscavesexemplified.preserved_amber.temperate_frog","Temperate Frog");
		this.add("block.alexscavesexemplified.preserved_amber.warm_frog","Warm Frog");
		this.add("block.alexscavesexemplified.preserved_amber.cold_frog","Cold Frog");
		this.add("block.alexscavesexemplified.preserved_amber.primordial_frog","Primordial Frog");
		this.add("block.alexscavesexemplified.preserved_amber.tadpole","Tadpole");
		this.add("block.alexscavesexemplified.preserved_amber.cockroach","Cockroach");
		this.add("block.alexscavesexemplified.preserved_amber.baby_cockroach","Baby Cockroach");
		this.add("block.alexscavesexemplified.preserved_amber.fly","Fly");
		this.add("block.alexscavesexemplified.preserved_amber.baby_fly","Baby Fly");

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

		this.addItem(ACExItemRegistry.ICE_CREAM_CONE, "Ice Cream Cone");
		this.add("item.alexscavesexemplified.ice_cream_cone.flavor.vanilla", "Vanilla");
		this.add("item.alexscavesexemplified.ice_cream_cone.flavor.chocolate", "Chocolate");
		this.add("item.alexscavesexemplified.ice_cream_cone.flavor.sweetberry", "Sweetberry");
		this.add("item.alexscavesexemplified.ice_cream_cone.when_eaten", "When Eaten:");
		this.add("item.alexscavesexemplified.ice_cream_cone.chance", "%s%% chance: %s");
		this.add("item.alexscavesexemplified.ice_cream_cone.clears_effects", "Clears Effects");
		this.addItem(ACExItemRegistry.GUMWIRM_SPAWN_EGG, "Gumwirm Spawn Egg");
		this.addItem(ACExItemRegistry.GAMMA_TREMORZILLA_SPAWN_EGG, "Gamma Tremorzilla Spawn Egg");


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
		this.addAdvancementDesc("noon_guardian","Pufferboom", "Feed a mine guardian a pufferfish");
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
