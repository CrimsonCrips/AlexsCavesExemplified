package org.crimsoncrips.alexscavesexemplified.datagen.patchouli;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.reimnop.pgen.PGenBookProvider;
import com.reimnop.pgen.builder.PGenEntryBuilder;
import com.reimnop.pgen.builder.page.PGenSpotlightPageBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACEBlockRegistry;

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
                                                category -> {})
                                        .addEntry("redo_spelunky",
                                                "Redoable Spelunky",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "general"),
                                                entry -> {entry
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
                                                entry -> {entry
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
                                                entry -> {entry
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
                                                entry -> {entry
                                                        .addImagePage(page -> {
                                                            page.addImage("textures/gui/wiki/general/additional_flamability.png")
                                                                    .withText("Adds flamability with AC blocks that cant be lit originally")
                                                                    .withTitle("Additional Flamability");
                                                        }).addTextPage("Amber,Pewen Blocks,Stripped Pewen Blocks,Archaic Vine," +
                                                                "Fiddlehead,Curly Fern,Flytrap,Cycad,Ancient Leaves,Fern Thatch" +
                                                                "Thornwood Blocks,Stripped Thornwood Blocks,Underwood,Forsaken Idol",page -> {});
                                                })
                                        .addEntry("forgiving_spelunking",
                                                "Forgiving Spelunking",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "general"),
                                                entry -> {entry
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
                                                entry -> {entry
                                                        .addImagePage(page -> {
                                                            page.addImage("textures/gui/wiki/general/cavial_bonemeal.png")
                                                                    .withText("Bonemealing compatible flora to improve or populate the area with flora when bonemealing")
                                                                    .withTitle("Cavial Bonemeal");
                                                        }).addTextPage("Ping-Pong Sponges to improve length \n" +
                                                                "Primordial Caves to propagate flora", page -> {});
                                                })

                                        .addEntry("liquid_replication",
                                                "Liquid Replication",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "general"),
                                                entry -> {entry
                                                        .addTextPage("Allows the replication of AC liquids with a special cauldron with special methods" +
                                                                " (with compatibility with create's fluid piping system)",page -> page.withTitle("Liquid Replication"))
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
                                                entry -> {entry
                                                        .addImagePage(page -> {
                                                            page.addImage("textures/gui/wiki/general/powered_locators.png")
                                                                    .withText("Sacrifice a held wither star to empower a locating item to prevent godly intervention")
                                                                    .withTitle("Powered Locators");
                                                        });
                                                })

                                ;
                                lang.addCategory("candy_cavity",
                                        "Candy Cavity",
                                        "Candy Cavity Additions",
                                        new ResourceLocation("alexscaves:textures/misc/advancement/icon/candy_cavity.png"),
                                        category -> {})

                                        .addEntry("gluttony",
                                                "Gluttony",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "candy_cavity"),
                                                entry -> {entry
                                                        .addImagePage(page -> {
                                                            page.addImage("textures/gui/wiki/candy_cavity/gluttony.png")
                                                                    .withText("Eat candy blocks by right clicking while crouching," +
                                                                            "has a chance to give sugar rush when consuming")
                                                                    .withTitle("Gluttony");
                                                        });
                                                })

                                        .addEntry("sticky_soda",
                                                "Sticky Soda",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "candy_cavity"),
                                                entry -> {entry
                                                        .addImagePage(page -> {
                                                            page.addImage("textures/gui/wiki/candy_cavity/sticky_soda.png")
                                                                    .withText("Purple soda applys slowness when in it")
                                                                    .withTitle("Sticky Soda");
                                                        });
                                                })

                                        .addEntry("radiant_wrath",
                                                "Radiant Wrath",
                                                new ResourceLocation("paper"),
                                                new ResourceLocation(AlexsCavesExemplified.MODID, "candy_cavity"),
                                                entry -> {entry
                                                        .addImagePage(page -> {
                                                            page.addImage("textures/gui/wiki/candy_cavity/radiant_wrath.png")
                                                                    .withText("Purple soda applys slowness when in it")
                                                                    .withTitle("Radiant Wrath");
                                                        });
                                                })

                                ;
                                lang.addCategory("forlorn_hollows",
                                        "Forlorn Hollows",
                                        "Forlorn Hollows Additions",
                                        new ResourceLocation("alexscaves:textures/misc/advancement/icon/forlorn_hollows.png"),
                                        category -> {}).addEntry("dreadbow_adaption",
                                        "Dreadbow Adaption",
                                        new ResourceLocation("paper"),
                                        new ResourceLocation(AlexsCavesExemplified.MODID, "forlorn_hollows"),
                                        entry -> {entry
                                                .addImagePage(page -> {
                                                    page.addImage("textures/gui/wiki/forlorn_hollows/dreadbow_adaption.png")
                                                            .withText("Vanilla Bow Enchants work for Dreadbow")
                                                            .withTitle("Dreadbow Adaption");
                                                });
                                        })

                                ;

                            });
                });
    }

    public Consumer<PGenSpotlightPageBuilder.ItemBuilder> itemIconGiver(RegistryObject itemIcon){
        return item -> {
            item.addItem(itemIcon.getKey() != null ? itemIcon.getKey().location() : new ResourceLocation("error"));
        };
    }



}
