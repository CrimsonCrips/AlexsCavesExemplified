package org.crimsoncrips.alexscavesexemplified.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.advancement.ACEAdvancementProvider;
import org.crimsoncrips.alexscavesexemplified.datagen.language.ACELangGen;
import org.crimsoncrips.alexscavesexemplified.datagen.loottables.ACELootGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.recipe.ACERecipeGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.sounds.ACESoundGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACEBlockTagGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACEEntityTagGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACEItemTagGenerator;

import java.util.concurrent.CompletableFuture;



@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ACEDatagen {
    //Giga Props to Drull and TF for assistance (and code yoinking)//
    public static void generateData(GatherDataEvent event) {
        boolean isServer = event.includeServer();
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        generator.addProvider(event.includeClient(), new ACESoundGenerator(output, helper));
        generator.addProvider(event.includeServer(), new ACERegistryDataGenerator(output, provider));
        generator.addProvider(event.includeServer(), new ACEAdvancementProvider(output, provider, helper));
        generator.addProvider(event.includeServer(), new ACELootGenerator(output));
        generator.addProvider(event.includeServer(), new ACERecipeGenerator(output));

        generator.addProvider(event.includeClient(), new ACELangGen(output));

        generator.addProvider(event.includeServer(), new ACEEntityTagGenerator(output, provider, helper));
        ACEBlockTagGenerator blocktags = new ACEBlockTagGenerator(output, provider, helper);
        generator.addProvider(event.includeServer(), blocktags);
        generator.addProvider(event.includeServer(), new ACEItemTagGenerator(output, provider, blocktags.contentsGetter(), helper));

    }

}
