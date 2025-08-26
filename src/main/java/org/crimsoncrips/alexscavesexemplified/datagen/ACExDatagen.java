package org.crimsoncrips.alexscavesexemplified.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.advancement.ACExAdvancementProvider;
import org.crimsoncrips.alexscavesexemplified.datagen.language.ACExLangGen;
import org.crimsoncrips.alexscavesexemplified.datagen.loottables.ACExLootGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.patchouli.ACExBookProvider;
import org.crimsoncrips.alexscavesexemplified.datagen.recipe.ACExRecipeGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.sounds.ACExSoundGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExBlockTagGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExEntityTagGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExItemTagGenerator;

import java.util.concurrent.CompletableFuture;



@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ACExDatagen {
    //Giga Props to Drull and TF for assistance (and code yoinking)//
    public static void generateData(GatherDataEvent event) {
        boolean isServer = event.includeServer();
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        generator.addProvider(event.includeClient(), new ACExSoundGenerator(output, helper));
        generator.addProvider(event.includeServer(), new ACExRegistryDataGenerator(output, provider));
        generator.addProvider(event.includeServer(), new ACExAdvancementProvider(output, provider, helper));
        generator.addProvider(event.includeServer(), new ACExLootGenerator(output));
        generator.addProvider(event.includeServer(), new ACExRecipeGenerator(output));
        generator.addProvider(event.includeServer(), new ACExBookProvider("alexscavesexemplified",provider,output));

        generator.addProvider(event.includeClient(), new ACExLangGen(output));

        generator.addProvider(event.includeServer(), new ACExEntityTagGenerator(output, provider, helper));
        ACExBlockTagGenerator blocktags = new ACExBlockTagGenerator(output, provider, helper);
        generator.addProvider(event.includeServer(), blocktags);
        generator.addProvider(event.includeServer(), new ACExItemTagGenerator(output, provider, blocktags.contentsGetter(), helper));

    }

}
