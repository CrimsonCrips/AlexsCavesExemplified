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
import org.crimsoncrips.alexscavesexemplified.client.ACExClientConfig;
import org.crimsoncrips.alexscavesexemplified.client.ACExClientProxy;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACExParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.datagen.ACExDatagen;
import org.crimsoncrips.alexscavesexemplified.server.ACExServerConfig;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.client.ACExSoundRegistry;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.ACExCauldronInteraction;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;
import org.crimsoncrips.alexscavesexemplified.server.enchantment.ACExEnchants;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;
import org.crimsoncrips.alexscavesexemplified.server.events.ACExModEvents;
import org.crimsoncrips.alexscavesexemplified.server.events.ACExemplifiedEvents;
import org.crimsoncrips.alexscavesexemplified.loot.ACExLootModifiers;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;

import java.util.Locale;

@SuppressWarnings("deprecated")
@Mod(AlexsCavesExemplified.MODID)
public class AlexsCavesExemplified {

    public static final String MODID = "alexscavesexemplified";
    public static final ACExCommonProxy PROXY = DistExecutor.runForDist(() -> ACExClientProxy::new, () -> ACExCommonProxy::new);

    public static final ACExServerConfig COMMON_CONFIG;
    private static final ForgeConfigSpec COMMON_CONFIG_SPEC;
    public static final ACExClientConfig CLIENT_CONFIG;
    private static final ForgeConfigSpec CLIENT_CONFIG_SPEC;

    static {
        final Pair<ACExServerConfig, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder().configure(ACExServerConfig::new);
        COMMON_CONFIG = serverPair.getLeft();
        COMMON_CONFIG_SPEC = serverPair.getRight();
        final Pair<ACExClientConfig, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(ACExClientConfig::new);
        CLIENT_CONFIG = clientPair.getLeft();
        CLIENT_CONFIG_SPEC = clientPair.getRight();
    }

    public AlexsCavesExemplified() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG_SPEC, "alexscavesexemplified-general.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG_SPEC, "alexscavesexemplified-client.toml");


        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ACExDatagen::generateData);
        ACExEntityRegistry.DEF_REG.register(modEventBus);
        ACExLootModifiers.register(modEventBus);
        ACExEnchants.DEF_REG.register(modEventBus);
        modEventBus.register(new ACExModEvents());
        MinecraftForge.EVENT_BUS.register(new ACExemplifiedEvents());
        MinecraftForge.EVENT_BUS.register(this);
        ACExParticleRegistry.DEF_REG.register(modEventBus);
        ACExBlockRegistry.DEF_REG.register(modEventBus);
        ACExItemRegistry.DEF_REG.register(modEventBus);
        PROXY.init();
        ACExEffects.EFFECT_REGISTER.register(modEventBus);
        ACExSoundRegistry.DEF_REG.register(modEventBus);
        ACExEffects.POTION_REGISTER.register(modEventBus);
        modEventBus.addListener(this::setup);



    }





    private void setup(final FMLCommonSetupEvent event) {
        ACExEffects.init();

        event.enqueueWork(() -> PROXY.clientInit());

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

            ACExCauldronInteraction.bootStrap();
        }

    }





    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }
}
