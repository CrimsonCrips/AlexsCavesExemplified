package org.crimsoncrips.alexscavesexemplified;


import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.crimsoncrips.alexscavesexemplified.client.ACEClientConfig;
import org.crimsoncrips.alexscavesexemplified.client.ACEClientEvents;
import org.crimsoncrips.alexscavesexemplified.client.ACEClientProxy;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.datagen.ACEDatagen;
import org.crimsoncrips.alexscavesexemplified.server.ACEServerConfig;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACEBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.client.ACESoundRegistry;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.ACECauldronInteraction;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;
import org.crimsoncrips.alexscavesexemplified.server.enchantment.ACEEnchants;
import org.crimsoncrips.alexscavesexemplified.server.ACExemplifiedEvents;
import org.crimsoncrips.alexscavesexemplified.loot.ACELootModifiers;
import org.crimsoncrips.alexscavesexemplified.server.item.ACEItemRegistry;

import java.util.Locale;

@SuppressWarnings("deprecated")
@Mod(AlexsCavesExemplified.MODID)
public class AlexsCavesExemplified {

    public static final String MODID = "alexscavesexemplified";
    public static final ACECommonProxy PROXY = DistExecutor.runForDist(() -> ACEClientProxy::new, () -> ACECommonProxy::new);

    public static final ACEServerConfig COMMON_CONFIG;
    private static final ForgeConfigSpec COMMON_CONFIG_SPEC;
    public static final ACEClientConfig CLIENT_CONFIG;
    private static final ForgeConfigSpec CLIENT_CONFIG_SPEC;

    static {
        final Pair<ACEServerConfig, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder().configure(ACEServerConfig::new);
        COMMON_CONFIG = serverPair.getLeft();
        COMMON_CONFIG_SPEC = serverPair.getRight();
        final Pair<ACEClientConfig, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(ACEClientConfig::new);
        CLIENT_CONFIG = clientPair.getLeft();
        CLIENT_CONFIG_SPEC = clientPair.getRight();
    }

    public AlexsCavesExemplified() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG_SPEC, "ace-general.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG_SPEC, "ace-client.toml");


        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ACEDatagen::generateData);

        ACELootModifiers.register(modEventBus);
        ACEEnchants.DEF_REG.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new ACExemplifiedEvents());
        MinecraftForge.EVENT_BUS.register(this);
        ACEParticleRegistry.DEF_REG.register(modEventBus);
        ACEBlockRegistry.DEF_REG.register(modEventBus);
        ACEItemRegistry.DEF_REG.register(modEventBus);
        PROXY.init();
        ACEEffects.EFFECT_REGISTER.register(modEventBus);
        ACESoundRegistry.DEF_REG.register(modEventBus);
        ACEEffects.POTION_REGISTER.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new ACEClientEvents());

        modEventBus.addListener(this::setup);



    }



    private void setup(final FMLCommonSetupEvent event) {

        FireBlock fireblock = (FireBlock) Blocks.FIRE;
        if (AlexsCavesExemplified.COMMON_CONFIG.ADDITIONAL_FLAMMABILITY_ENABLED.get()){
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

            ACECauldronInteraction.bootStrap();
        }

    }





    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }
}
