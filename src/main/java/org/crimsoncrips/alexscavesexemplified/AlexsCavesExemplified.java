package org.crimsoncrips.alexscavesexemplified;


import biomesoplenty.worldgen.feature.misc.DripstoneSplatterFeature;
import com.crimsoncrips.alexsmobsinteraction.effect.AMIEffects;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.level.feature.ACFeatureRegistry;
import com.github.alexthe666.alexsmobs.client.event.ClientEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.crimsoncrips.alexscavesexemplified.client.event.ACEClientEvents;
import org.crimsoncrips.alexscavesexemplified.config.ACEConfigHolder;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.effect.ACEEffects;
import org.crimsoncrips.alexscavesexemplified.enchantment.ACEEnchants;
import org.crimsoncrips.alexscavesexemplified.event.ACExemplifiedEvents;
import org.crimsoncrips.alexscavesexemplified.loot.ACELootModifiers;

import java.util.Locale;

@Mod(AlexsCavesExemplified.MODID)
public class AlexsCavesExemplified {

    public static final String MODID = "alexscavesexemplified";



    public AlexsCavesExemplified() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onModConfigEvent);
        ACELootModifiers.register(modEventBus);
        ACEEnchants.DEF_REG.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new ACExemplifiedEvents());
        MinecraftForge.EVENT_BUS.register(this);

        ACEEffects.EFFECT_REGISTER.register(modEventBus);
        ACEEffects.POTION_REGISTER.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new ACEClientEvents());


        modEventBus.addListener(this::setup);


        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ACEConfigHolder.EXEMPLIFIED_SPEC, "alexscavesexemplified.toml");

    }



    @SubscribeEvent
    public void onModConfigEvent(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == ACEConfigHolder.EXEMPLIFIED_SPEC) {
            ACExemplifiedConfig.bake();
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        AMIEffects.init();
        FireBlock fireblock = (FireBlock) Blocks.FIRE;
        if (ACExemplifiedConfig.ADDITIONAL_FLAMMABILITY_ENABLED){
            //Primordial Caves
            fireblock.setFlammable(ACBlockRegistry.AMBER.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.PEWEN_BRANCH.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.PEWEN_PLANKS.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.PEWEN_WOOD.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.PEWEN_LOG.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.STRIPPED_PEWEN_LOG.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.STRIPPED_PEWEN_WOOD.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.PEWEN_FENCE_GATE.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.PEWEN_PLANKS_FENCE.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.PEWEN_PLANKS_STAIRS.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.PEWEN_PLANKS_SLAB.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.PEWEN_TRAPDOOR.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.PEWEN_DOOR.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.PEWEN_PINES.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.ARCHAIC_VINE.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.FIDDLEHEAD.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.CURLY_FERN.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.FLYTRAP.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.CYCAD.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.ANCIENT_LEAVES.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.FERN_THATCH.get(), 5, 20);

            //Forlorn Hollows
            fireblock.setFlammable(ACBlockRegistry.THORNWOOD_LOG.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.THORNWOOD_BRANCH.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.THORNWOOD_WOOD.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.STRIPPED_THORNWOOD_LOG.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.STRIPPED_THORNWOOD_WOOD.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.THORNWOOD_PLANKS.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.THORNWOOD_PLANKS_STAIRS.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.THORNWOOD_PLANKS_SLAB.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.THORNWOOD_PLANKS_FENCE.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.THORNWOOD_DOOR.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.THORNWOOD_TRAPDOOR.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.UNDERWEED.get(), 5, 20);
            fireblock.setFlammable(ACBlockRegistry.FORSAKEN_IDOL.get(), 5, 20);

        }
    }




    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }
}
