package org.crimsoncrips.alexscavesexemplified.datagen.patchouli;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.reimnop.pgen.PGenBookProvider;
import com.reimnop.pgen.builder.PGenEntryBuilder;
import com.reimnop.pgen.builder.page.PGenSpotlightPageBuilder;
import com.reimnop.pgen.data.PGenMultiblock;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACEBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.item.ACEItemRegistry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ACEBookProvider extends PGenBookProvider {

    public ACEBookProvider(String modId, CompletableFuture<HolderLookup.Provider> lookupProvider, PackOutput packOutput) {
        super(modId, lookupProvider, packOutput);
    }

    @Override
    protected void generate(HolderLookup.Provider provider) {
        addBook("acewiki",
                "AC Exemplified Wiki",
                "Welcome to an exemplified universe!",
                true,
                book -> {
                    book.withCreativeTab(CreativeModeTabs.TOOLS_AND_UTILITIES.location())
                            .addLanguage("en_us", lang -> {
                                //General Category
                                lang.addCategory("general",
                                                "General",
                                                "General Additions",
                                                new ResourceLocation("alexscavesexemplified:textures/gui/adv_icon/ace_adv_icon.png"),
                                                category -> category.withSortnum(0))
                                        .addEntry("redo_spelunky",
                                                "Redoable Spelunky",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "general"),
                                                entry -> {
                                                    entry
                                                            .addSpotlightPage(
                                                                    itemIconGiver(ACBlockRegistry.SPELUNKERY_TABLE),
                                                                    page -> page.withText(
                                                                            "Allows for you to back-out of the spelunkery table and not have the tablet break," +
                                                                                    "useful for when interrupted by any unwanted interruptions").withTitle("Redoable Spelunky")
                                                            );
                                                })
                                        .addEntry("spelunkery_attempts",
                                                "Spelunkery Attempts",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "general"),
                                                entry -> {
                                                    entry
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/general/spelunkery_attempts.png")
                                                                        .withText("Adjustable attempts for spelunking in the spelunkery table")
                                                                        .withTitle("Spelunkery Attempts");
                                                            });
                                                })
                                        .addEntry("charged_caves",
                                                "Charged Caves",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "general"),
                                                entry -> {
                                                    entry
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/general/charged_caves.png")
                                                                        .withText("AC Creepers have a chance to be charged when summoning")
                                                                        .withTitle("Charged Caves");
                                                            });
                                                })
                                        .addEntry("additional_flamability",
                                                "Additional Flamability",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "general"),
                                                entry -> {
                                                    entry
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/general/additional_flamability.png")
                                                                        .withText("Adds flamability with AC blocks that cant be lit originally")
                                                                        .withTitle("Additional Flamability");
                                                            }).addTextPage("Amber,Pewen Blocks,Stripped Pewen Blocks,Archaic Vine," +
                                                                    "Fiddlehead,Curly Fern,Flytrap,Cycad,Ancient Leaves,Fern Thatch" +
                                                                    "Thornwood Blocks,Stripped Thornwood Blocks,Underwood,Forsaken Idol", page -> {
                                                            });
                                                })
                                        .addEntry("forgiving_spelunking",
                                                "Forgiving Spelunking",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "general"),
                                                entry -> {
                                                    entry
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/general/forgiving_spelunking.png")
                                                                        .withText("Spelunking is more forgiving when you fail deciphering a tablet")
                                                                        .withTitle("Forgiving Spelunking");
                                                            });
                                                })

                                        .addEntry("cavial_bonemeal",
                                                "Cavial Bonemeal",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "general"),
                                                entry -> {
                                                    entry
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/general/cavial_bonemeal.png")
                                                                        .withText("Bonemealing compatible flora to improve or populate the area with flora when bonemealing")
                                                                        .withTitle("Cavial Bonemeal");
                                                            }).addTextPage("Ping-Pong Sponges to improve length \n" +
                                                                    "Primordial Caves to propagate flora", page -> {
                                                            });
                                                })

                                        .addEntry("liquid_replication",
                                                "Liquid Replication",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "general"),
                                                entry -> {
                                                    entry
                                                            .addTextPage("Allows the replication of AC liquids with a special cauldron with special methods" +
                                                                    " (with compatibility with create's fluid piping system)", page -> page.withTitle("Liquid Replication"))
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/general/liquid_replication/metal_cauldron.png")
                                                                        .withText("Right click a normal cauldron while sneaking with a metal block in hand");
                                                            })
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/general/liquid_replication/acidic_cauldron.png")
                                                                        .withText("Place an acidrock on top of the metal cauldron");
                                                            })
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/general/liquid_replication/soda_cauldron.png")
                                                                        .withText("Right click with a soda bottle to be able to bucket it freely");
                                                            });

                                                })

                                        .addEntry("powered_locators",
                                                "Powered Locators",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "general"),
                                                entry -> {
                                                    entry
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/general/powered_locators.png")
                                                                        .withText("Sacrifice a held wither star to empower a locating item to prevent godly intervention")
                                                                        .withTitle("Powered Locators");
                                                            });
                                                })

                                        .addEntry("add_targets",
                                                "Add Targets",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "general"),
                                                entry -> {
                                                    entry
                                                            .addTextPage("Adds extra targets for mobs to improve immersion \n" +
                                                                    "Licowitch - Iron Golem, Villagers \n" +
                                                                    "Vesper - Cockroach, Bat, Spiders \n" +
                                                                    "Deep Ones - Fish" +
                                                                    "Grotocerotops/Relicheirus - Players holding Limespears", page -> {
                                                            });
                                                })
                                ;

                                //Candy Cavity
                                lang.addCategory("candy_cavity",
                                                "Candy Cavity",
                                                "Candy Cavity Additions",
                                                new ResourceLocation("alexscaves:textures/misc/advancement/icon/candy_cavity.png"),
                                                category -> {
                                                })

                                        .addEntry("candy_general",
                                                "General",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "candy_cavity"),
                                                entry -> {
                                                    entry
                                                            //Gluttony
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/gluttony.png")
                                                                        .withText("Eat candy blocks by right clicking while crouching," +
                                                                                "has a chance to give sugar rush when consuming")
                                                                        .withTitle("Gluttony");
                                                            })
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/gluttony2.png")
                                                                        .withText("Adds minor interactions with eating food");
                                                            })
                                                            .addTextPage("Consuming frostmint and purple soda will cause a minor explosion", page -> {
                                                            })

                                                            //Sticky Soda
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/sticky_soda.png")
                                                                        .withText("Purple soda applys slowness when in it")
                                                                        .withTitle("Sticky Soda");
                                                            })

                                                            //Radiant Wrath
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/radiant_wrath.png")
                                                                        .withText("Overdrives your casted sugar staff abilities at the cost of a radiant essence held off-offhand")
                                                                        .withTitle("Radiant Wrath");
                                                            })

                                                            //Tuned Sating
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/tuned_sating.png")
                                                                        .withText("Drop foods directly onto a Sack Of Sating, Will also eat food that cant be picked up when in inventory")
                                                                        .withTitle("Tuned Sating");
                                                            })

                                                            //Sugar Crash
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/sugar_crash.png")
                                                                        .withText("Inflicts you with sugar crash after the effects of Sugar Rush, causing minor damage and slowness")
                                                                        .withTitle("Sugar Crash");
                                                            })

                                                            //Iced Cream
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/iced_cream.png")
                                                                        .withText("Thrown Ice Cream Scoops inflict frost")
                                                                        .withTitle("Iced Cream");
                                                            })

                                                            //Cryonic Cavity
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/cryonic_cavity.png")
                                                                        .withText("Candy Cavity slowly freezes you")
                                                                        .withTitle("Cryonic Cavity");
                                                            })

                                                            //Breaking Candy
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/breaking_candy.png")
                                                                        .withText("Allows the cooking of gelatin with a lit cauldron")
                                                                        .withTitle("Breaking Candy");
                                                            })
                                                            .addSpotlightPage(
                                                                    item -> item.addTag(new ResourceLocation("alexscavesexemplified:gelatinable")),
                                                                    page -> page.withText(
                                                                            "To cook gelatin, drop in a dye of choice for the color, and a bone item," +
                                                                                    "inside a heated cauldron,and simply wait").withTitle(" ")
                                                            )

                                                            //Ice Cream Cone
                                                            .addSpotlightPage(
                                                                    itemIconGiver(ACEItemRegistry.ICE_CREAM_CONE),
                                                                    page -> page.withText(
                                                                            "Assemble tasty ice creams (i love ice cream), Note that the Ice Cream sequence doesnt matter apart from the cone").withTitle("Ice Cream Cone")
                                                            )
                                                            .addMultiblockPage(" ", page -> page.withMultiblock(new PGenMultiblock(
                                                                            List.of(
                                                                                    List.of("V"),
                                                                                    List.of("S"),
                                                                                    List.of("C"),
                                                                                    List.of("B"),
                                                                                    List.of("0")
                                                                            ),
                                                                            Map.of("0", "alexscaves:wafer_cookie_wall", "B", "alexscaves:wafer_cookie_block", "C", "alexscaves:chocolate_ice_cream", "S", "alexscaves:sweetberry_ice_cream", "V", "alexscaves:vanilla_ice_cream")
                                                                    ))
                                                            )

                                                            //Overdrived Conversion
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/overdrived_conversion.png")
                                                                        .withText("Allows a conversion crucible to be overdrived by dropping a radiant essence to increase the radius of it")
                                                                        .withTitle("Overdrived Conversion");
                                                            })

                                                            //Purple Leathered
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/purple_leathered.png")
                                                                        .withText("Tints an uncolored armor into purple, when submerged in soda," +
                                                                                "(Compatible with modded armor)")
                                                                        .withTitle("Purple Leathered");
                                                            })

                                                            //Amplified Frostmint
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/amplified_frostmint.png")
                                                                        .withText("Adds interactibility with frostmint items, in item form and spear form")
                                                                        .withTitle("Amplified Frostmint");
                                                            })
                                                            .addTextPage("Dropped Frostmint items will explode simillar to falling frostmint \n" +
                                                                    "Frostmint Spears when in collision will solidify nearby liquids", page -> {
                                                            });

                                                })

                                        .addEntry("caniac",
                                                "Caniac",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "candy_cavity"),
                                                entry -> {
                                                    entry
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/caniac/caniac_sensitivity.png")
                                                                        .withText("Caniacs dissolve and will avoid water")
                                                                        .withTitle("Caniac Sensitivity");
                                                            })
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/caniac/caniac_maniac.png")
                                                                        .withText("Caniacs do unhinged things like lighting placed explosives,destroying beds,and attacking random entities")
                                                                        .withTitle("Caniac Maniac");
                                                            });
                                                })

                                        .addEntry("candicorn",
                                                "Candicorn",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "candy_cavity"),
                                                entry -> {
                                                    entry
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/candicorn/candicorn_heal.png")
                                                                        .withText("Candicorn can be healed with caramel apples")
                                                                        .withTitle("Candicorn Heal");
                                                            });
                                                })

                                        .addEntry("caramel_cube",
                                                "Caramel Cube",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "candy_cavity"),
                                                entry -> {
                                                    entry
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/caramel_cube/sticky_caramel.png")
                                                                        .withText("Caramel Cubes's stickyness apply to their attacks")
                                                                        .withTitle("Sticky Caramel");
                                                            });
                                                })


                                        .addEntry("gingerbread_man",
                                                "Gingerbread Man",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "candy_cavity"),
                                                entry -> {
                                                    entry
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/gingerbread_man/amputation.png")
                                                                        .withText("Allows the amputation of gingerbread limbs")
                                                                        .withTitle("Amputation");
                                                            })
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/gingerbread_man/hive_mind.png")
                                                                        .withText("Attack one of them, Fight all of them.")
                                                                        .withTitle("Hive Mind");
                                                            });
                                                })

                                        .addEntry("gummy_bear",
                                                "Gummybear",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "candy_cavity"),
                                                entry -> {
                                                    entry
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/gummy_bear/jellybean_changes.png")
                                                                        .withText("Gummybears can be interrupted during their hibernation," +
                                                                                "allowing for jellybeans to be rushed")
                                                                        .withTitle("Jellybean Changes");
                                                            })
                                                            .addSpotlightPage(
                                                                    itemIconGiver(ACItemRegistry.JELLY_BEAN),
                                                                    page -> page.withText(
                                                                            "Amount of jellybeans made are dependant on the time taken for hibernation, with the max of 12 jellybeans for the full duration," +
                                                                                    "Works with Sweetish Speedup").withTitle(" ")
                                                            )
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/gummy_bear/sweetish_speedup.png")
                                                                        .withText("Feed a hibernating gummybear their matching color of fish to speedup hibernation")
                                                                        .withTitle("Sweetish Speedup");
                                                            });
                                                })

                                        .addEntry("gum_worm",
                                                "Gum Worm",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "candy_cavity"),
                                                entry -> {
                                                    entry
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/candy_cavity/gum_worm/pressured_hooks.png")
                                                                        .withText("Inflicts damage in hooks when using with a gum worm")
                                                                        .withTitle("Pressured Hooks");
                                                            });
                                                })

                                ;
                                lang.addCategory("forlorn_hollows",
                                                "Forlorn Hollows",
                                                "Forlorn Hollows Additions",
                                                new ResourceLocation("alexscaves:textures/misc/advancement/icon/forlorn_hollows.png"),
                                                category -> {
                                                })
                                        .addEntry("forlorn_general",
                                                "General",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "forlorn_hollows"),
                                                entry -> {
                                                    entry
                                                            //Forlorn Light Effect
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/forlorn_light_effect.png")
                                                                        .withText("Holding light wards off some mobs of the Forlorn (Compatible with Curios Lanterns)")
                                                                        .withTitle("Forlorn Light Effect");
                                                            })

                                                            //Burst Out
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/burst_out.png")
                                                                        .withText("A random land dwelling forlorn denizen may pop out when excavating in the biome")
                                                                        .withTitle("Burst Out");
                                                            })

                                                            //Rabies
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/rabies.png")
                                                                        .withText("Rabies are discovered within Forlorn mobs," +
                                                                                "becoming sensitive to water, and attack most mobs randomly")
                                                                        .withTitle("Rabies");
                                                            })
                                                            .addTextPage("Those afflicted with Rabies will suffer damage at the end of its cycle", page -> {
                                                            })

                                                            //Guaslowpoke
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/guaslowpoke.png")
                                                                        .withText("Guano inflicts slowness whether on top or hit by one")
                                                                        .withTitle("Guaslowpoke");
                                                            })

                                                            //Beholdent Stalking
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/beholdent_stalking.png")
                                                                        .withText("Unmanned Beholders stalk nearby players (purely visual)")
                                                                        .withTitle("Beholding Stalking");
                                                            })

                                                            //Dreadbow Adaption
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/dreadbow_adaption.png")
                                                                        .withText("Vanilla Bow Enchants work for Dreadbow")
                                                                        .withTitle("Dreadbow Adaption");
                                                            });
                                                })
                                        .addEntry("corrodent",
                                                "Corrodent",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "forlorn_hollows"),
                                                entry -> {
                                                    entry
                                                            //Knawing
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/corrodent/knawing.png")
                                                                        .withText("Corrodents knaw on items and foods")
                                                                        .withTitle("Knawing");
                                                            })
                                                            //Corrodent Conversion
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/corrodent/corrodent_conversion.png")
                                                                        .withText("Corrodents can be assimilated into Underzealots")
                                                                        .withTitle("Corrodent Conversion");
                                                            });
                                                })

                                        .addEntry("underzealot",
                                                "Underzealot",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "forlorn_hollows"),
                                                entry -> {
                                                    entry
                                                            //Underzealot Respect
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/underzealot/underzealot_respect.png")
                                                                        .withText("Wearing the full set of the Underzealot's clothing makes them neutral with you")
                                                                        .withTitle("Underzealot Respect");
                                                            })
                                                            //Dark Offering
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/underzealot/dark_offering.png")
                                                                        .withText("Allows a player to leash sacrificeable denizens and give them to Underzealots for their rituals by right clicking,")
                                                                        .withTitle("Dark Offering");
                                                            })
                                                            .addTextPage("(REQUIRES 'Underzealot Respect' FOR THIS PART OF THE FEATURE) \n If neutral with the Underzealots," +
                                                                    "They will give an item native to the Forlorn Hollows in return of offering them a sacrifice," +
                                                                    "What they give in return is dependant on the sacrifice type", page -> {
                                                            })
                                                    ;
                                                })

                                        .addEntry("vesper",
                                                "Vesper",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "forlorn_hollows"),
                                                entry -> {
                                                    entry
                                                            //Anti Sacrifice
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/vesper/anti_sacrifice.png")
                                                                        .withText("Vespers attack underzealots seen sacrificing their own kind")
                                                                        .withTitle("Anti Sacrifice");
                                                            })
                                                            //Vesper Shotdown
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/vesper/vesper_shotdown.png")
                                                                        .withText("Vespers can be shot down when bowed down")
                                                                        .withTitle("Vesper Shotdown");
                                                            })
                                                    ;
                                                })

                                        .addEntry("watcher",
                                                "Watcher",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "forlorn_hollows"),
                                                entry -> {
                                                    entry
                                                            //Solidified
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/forlorn_hollows/watcher/solidified.png")
                                                                        .withText("Watchers solidify into statues after a certain amount of time")
                                                                        .withTitle("Solidified");
                                                            })
                                                    ;
                                                });

                                lang.addCategory("toxic_caves",
                                                "Toxic Caves",
                                                "Toxic Caves Addition",
                                                new ResourceLocation("alexscaves:textures/misc/advancement/icon/toxic_caves.png"),
                                                category -> {
                                                })
                                        .addEntry("toxic_general",
                                                "General",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "toxic_caves"),
                                                entry -> {
                                                    entry
                                                            //Exemplified Irradiation
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/exemplified_irradiation.png")
                                                                        .withText("When reaching a certain level of irradiation, severe side effects are afflicted")
                                                                        .withTitle("Exemplified Irradiation");
                                                            })
                                                            .addTextPage("Side effects are as follows /n" +
                                                                    "Weakness, Hunger, Nausea, Slowness, (If Alex's Mobs is present) Exsanguination", page -> {

                                                            })

                                                            //Kirov Reporting
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/kirov_reporting.png")
                                                                        .withText("Allows the dropping of TnT, Tnt Minecarts and Nukes when airborne, with a flint and steel off-hand")
                                                                        .withTitle("Kirov Reporting!");
                                                            })

                                                            //Armored Liquidators
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/armored_liquidators.png")
                                                                        .withText("Hazmat Armor resists radiation sources better")
                                                                        .withTitle("Armored Liquidators");
                                                            })

                                                            //Rearayngement
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/rearayngement.png")
                                                                        .withText("Improves upon Rayguns to be more destructive")
                                                                        .withTitle("Rearayngement");
                                                            })

                                                            //Irradiation Washoff
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/irradiation_washoff.png")
                                                                        .withText("Douse yourself in water to wash off irradiation,(If Supplementaries is present) Can use soap to further wash off such")
                                                                        .withTitle("Irradiation Washoff");
                                                            })

                                                            //Geothermal Effects
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/geothermal_effects.png")
                                                                        .withText("Effects those on top of geothermal vents based on the liquid that is being spewed")
                                                                        .withTitle("Geothermal Effects");
                                                            });


                                                })
                                        .addEntry("brainiac",
                                                "Brainiac",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "toxic_caves"),
                                                entry -> {
                                                    entry
                                                            //Waste Pickup
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/brainiac/waste_pickup.png")
                                                                        .withText("Brainiacs pickup dropped waste drums when non")
                                                                        .withTitle("Waste Pickup");
                                                            });


                                                })

                                        .addEntry("gammaroach",
                                                "Gammaroach",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "toxic_caves"),
                                                entry -> {
                                                    entry
                                                            //Roach Feeding
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/gammaroach/roach_feeding.png")
                                                                        .withText("Gammaroaches eat dropped food")
                                                                        .withTitle("Roach Feeding");
                                                            });


                                                })

                                        .addEntry("nucleeper",
                                                "Nucleeper",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "toxic_caves"),
                                                entry -> {
                                                    entry
                                                            //Nuclear Chain
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/nucleeper/nuclear_chain.png")
                                                                        .withText("Nucleeper explosions explode other nearby nucleepers")
                                                                        .withTitle("Nuclear Chain");
                                                            })

                                                            //Defusion
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/nucleeper/defusion.png")
                                                                        .withText("Nucleepers can be defused and be rendered walking husks")
                                                                        .withTitle("Defusion");
                                                            });


                                                })
                                        .addEntry("nucleeper",
                                                "Nucleeper",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "toxic_caves"),
                                                entry -> {
                                                    entry
                                                            //Nuclear Chain
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/nucleeper/nuclear_chain.png")
                                                                        .withText("Nucleeper explosions explode other nearby nucleepers")
                                                                        .withTitle("Nuclear Chain");
                                                            })

                                                            //Defusion
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/nucleeper/defusion.png")
                                                                        .withText("Nucleepers can be defused and be rendered walking husks")
                                                                        .withTitle("Defusion");
                                                            });


                                                })
                                        .addEntry("radgill",
                                                "Radgill",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "toxic_caves"),
                                                entry -> {
                                                    entry
                                                            //Fish Mutation
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/radgill/fish_mutation.png")
                                                                        .withText("Fish can be mutated into radgills in acid")
                                                                        .withTitle("Fish Mutation");
                                                            });
                                                })

                                        .addEntry("raycat",
                                                "Raycat",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "toxic_caves"),
                                                entry -> {
                                                    entry
                                                            //Cat Mutation
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/raycat/cat_mutation.png")
                                                                        .withText("Cat can be mutated into raycat in acid")
                                                                        .withTitle("Cat Mutation");
                                                            });
                                                })

                                        .addEntry("tremorzilla",
                                                "Tremorzilla",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "toxic_caves"),
                                                entry -> {
                                                    entry
                                                            //Gamma Tremorzilla
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/toxic_caves/tremorzilla/gamma_tremorzilla.png")
                                                                        .withText("Born from pure condensed energy, Gamma Tremorzilla is an ascension above its former self")
                                                                        .withTitle("Gamma Tremorzilla");
                                                            });
                                                });

                                lang.addCategory("primordial_caves",
                                                "Primordial Caves",
                                                "Primordial Caves Addition",
                                                new ResourceLocation("alexscaves:textures/misc/advancement/icon/primordial_caves.png"),
                                                category -> {
                                                })
                                        .addEntry("primordial_general",
                                                "General",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "primordial_caves"),
                                                entry -> {
                                                    entry
                                                            //Serened
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/primordial_caves/serened.png")
                                                                        .withText("Serened is given with serene salads to mobs, becoming neutral,weakening them but upping health")
                                                                        .withTitle("Serened");
                                                            })

                                                            //Egg Anger
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/primordial_caves/egg_anger.png")
                                                                        .withText("Handling eggs of dinosaurs will anger them")
                                                                        .withTitle("Egg Anger");
                                                            })

                                                            //Scavenging
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/primordial_caves/scavenging.png")
                                                                        .withText("Carnivorous Dinosaurs will scavenge dropped/placed meat on the ground")
                                                                        .withTitle("Scavenging");
                                                            })

                                                            .addTextPage("When tremorsauruses finds food but is swarming with vallumraptors. Tremorsauruses will roar loudly to ward off competition", page -> {
                                                            })

                                                            //Stomping
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/primordial_caves/stomping.png")
                                                                        .withText("Atlatitan/Luxtructosaurus deals damage when stepping")
                                                                        .withTitle("Stomping");
                                                            })

                                                            //Preserved Amber
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/primordial_caves/preserved_amber.png")
                                                                        .withText("Natural amber can be discovered to have trapped critters inside them,")
                                                                        .withTitle("Preserved Amber");
                                                            })

                                                            .addTextPage(" from frogs (If Alexs Mobs is present) to flies and cockroaches", page -> {
                                                            })

                                                            //Fly Trapped
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/primordial_caves/fly_trapped.png")
                                                                        .withText("(REQUIRES ALEXS MOBS) Fly Trap plants snap at the presence of physical flies")
                                                                        .withTitle("Fly Trapped");
                                                            });


                                                })

                                        .addEntry("atlatitan",
                                                "Atlatitan",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "primordial_caves"),
                                                entry -> {
                                                    entry
                                                            //Volcanic Sacrifice
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/primordial_caves/atlatitan/volcanic_sacrifice.png")
                                                                        .withText("Sacrifice an Atlatitan Egg or a baby Atlatitan in a volcano to refresh its cooldown")
                                                                        .withTitle("Volcanic Sacrifice");
                                                            })
                                                            .addTextPage("Adds a new cave painting in a 3x3 space related to this feature once a Lux has been slain once", page -> {
                                                            });


                                                })

                                        .addEntry("tremorsaurus",
                                                "Tremorsaurus",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "primordial_caves"),
                                                entry -> {
                                                    entry
                                                            //Seethed Taming
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/primordial_caves/tremorsaurus/seethed_taming.png")
                                                                        .withText("(REQUIRES SCAVENGING) Serened Tremorsauruses can be tamed by letting it eat meat off of your hand")
                                                                        .withTitle("Seethed Taming");
                                                            })

                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/primordial_caves/tremorsaurus/seethed_taming.png")
                                                                        .withText("(REQUIRES SCAVENGING) Serened Tremorsauruses can be tamed by letting it eat meat off of your hand")
                                                                        .withTitle("Seethed Taming");
                                                            });

                                                });

                                lang.addCategory("magnetic_caves",
                                                "Magnetic Caves",
                                                "Magnetic Caves Addition",
                                                new ResourceLocation("alexscaves:textures/misc/advancement/icon/magnetic_caves.png"),
                                                category -> {
                                                })
                                        .addEntry("magnetic_general",
                                                "General",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "magnetic_caves"),
                                                entry -> {
                                                    entry
                                                            //Magneticism
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/magnetic_caves/magneticism.png")
                                                                        .withText("Magneticism is an enchantment given to gears from Magnetic Caves,adding new mechanics")
                                                                        .withTitle("Magneticism");
                                                            })

                                                            .addSpotlightPage(
                                                                    itemIconGiver(ACItemRegistry.GALENA_GAUNTLET),
                                                                    page -> page.withText(
                                                                            "Galena Gaunlet now has the ability to be able to appropriate Teletor weapons for your own \n" +
                                                                            "Grab magnetic and crystalized items from afar \n and the ability to rip out the hearts of Magnetrons, at the cost of significant damage and cooldown dealt ").withTitle(" ")
                                                            )

                                                            .addSpotlightPage(
                                                                    itemIconGiver(ACItemRegistry.RESISTOR_SHIELD),
                                                                    page -> page.withText(
                                                                            "Resistor Shield now has the ability to affect dropped items \n" +
                                                                            "Can launch nearby seeking arrows at a pointed target if the shield is azure \n"+
                                                                            "Circle nearby seeking arrows during use while scarlet").withTitle(" ")
                                                            )




                                                            //Shocking Therapy
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/magnetic_caves/shocking_therapy.png")
                                                                        .withText("Tesla Bulbs directly shock nearby players when in range")
                                                                        .withTitle("Shocking Therapy");
                                                            })

                                                            .addTextPage("Also adds sfx from the franchise, Command And Conquer for the tesla bulbs", page ->{}
                                                            )

                                                            //Scallable Hologram
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/magnetic_caves/scalable_hologram.png")
                                                                        .withText("Scale Holograms with Azure/Scarlet Ingots")
                                                                        .withTitle("Scalable Hologram");
                                                            })

                                                            //Magnetic Movement
                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/magnetic_caves/magnetic_movement.png")
                                                                        .withText("(CLIENT SIDE) Magnetic Items sway around if within the Magnetic Caves")
                                                                        .withTitle("Magnetic Movement");
                                                            })

                                                            .addImagePage(page -> {
                                                                page.addImage("textures/gui/wiki/magnetic_caves/magnerip.png")
                                                                        .withText("Rips out magnetic items out of hand with the magnetic forces if inflicted with weakness")
                                                                        .withTitle("Magnerip");
                                                            })

                                                            .addTextPage("(IF HARDCORE_MAGNERIP ENABLED) Rips out all magnetic items off of your inventory, Disabled By Default", page ->{}
                                                            );
                                                    ;


                                                });


                            });
                });

}

public Consumer<PGenSpotlightPageBuilder.ItemBuilder> itemIconGiver(RegistryObject itemIcon){
    return item -> {
        item.addItem(itemIcon.getKey() != null ? itemIcon.getKey().location() : new ResourceLocation("error"));
    };
}



}
