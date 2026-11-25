package org.crimsoncrips.alexscavesexemplified;



import org.crimsoncrips.alexscavesexemplified.server.entity.ACExGammaAttacks;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import org.crimsoncrips.alexscavesexemplified.misc.ACExAmberMonolith;
import org.crimsoncrips.alexscavesexemplified.misc.ACExPrimordialProwess;
import org.crimsoncrips.alexscavesexemplified.misc.ACExArmedUnderzealot;
import org.crimsoncrips.alexscavesexemplified.server.ACExDataMaps;
import net.minecraftforge.fml.DistExecutor;
import org.crimsoncrips.alexscavesexemplified.backport.ACExEventBridge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.crimsoncrips.alexscavesexemplified.server.enchantment.ACExEnchants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.tuple.Pair;
import org.crimsoncrips.alexscavesexemplified.client.ACExClientConfig;
import org.crimsoncrips.alexscavesexemplified.client.ACExClientProxy;
import org.crimsoncrips.alexscavesexemplified.client.ACExSoundRegistry;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACExParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.datagen.ACExDatagen;
import org.crimsoncrips.alexscavesexemplified.loot.ACExLootModifiers;
import org.crimsoncrips.alexscavesexemplified.misc.ACExScheduler;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSectumsempra;
import org.crimsoncrips.alexscavesexemplified.networking.ACExPacketRegistration;
import org.crimsoncrips.alexscavesexemplified.server.ACExAddTargetsConfig;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.ACExCommands;
import org.crimsoncrips.alexscavesexemplified.server.ACExServerConfig;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.menu.ACExMenuRegistry;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.ACExCauldronInteraction;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;
import org.crimsoncrips.alexscavesexemplified.server.events.ACEAddGoals;
import org.crimsoncrips.alexscavesexemplified.server.events.ACEAddTargets;
import org.crimsoncrips.alexscavesexemplified.server.events.ACExModEvents;
import org.crimsoncrips.alexscavesexemplified.server.events.ACExemplifiedEvents;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;
import org.crimsoncrips.alexscavesexemplified.server.world.ACExFeatureRegistry;

import java.util.Locale;

@Mod(AlexsCavesExemplified.MODID)
public class AlexsCavesExemplified {

    public static final String MODID = "alexscavesexemplified";
    public static final ACExCommonProxy PROXY = DistExecutor.unsafeRunForDist(() -> ACExClientProxy::new, () -> ACExCommonProxy::new);

    public static final ACExServerConfig COMMON_CONFIG;
    public static final ForgeConfigSpec COMMON_CONFIG_SPEC;
    public static final ACExClientConfig CLIENT_CONFIG;
    public static final ForgeConfigSpec CLIENT_CONFIG_SPEC;
    public static final ACExAddTargetsConfig TARGETS_CONFIG;
    public static final ForgeConfigSpec TARGETS_CONFIG_SPEC;

    static {
        final Pair<ACExServerConfig, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder().configure(ACExServerConfig::new);
        COMMON_CONFIG = serverPair.getLeft();
        COMMON_CONFIG_SPEC = serverPair.getRight();
        final Pair<ACExClientConfig, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(ACExClientConfig::new);
        CLIENT_CONFIG = clientPair.getLeft();
        CLIENT_CONFIG_SPEC = clientPair.getRight();
        final Pair<ACExAddTargetsConfig, ForgeConfigSpec> targetPair = new ForgeConfigSpec.Builder().configure(ACExAddTargetsConfig::new);
        TARGETS_CONFIG = targetPair.getLeft();
        TARGETS_CONFIG_SPEC = targetPair.getRight();
    }

    public AlexsCavesExemplified() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG_SPEC, "alexscavesexemplified-general.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG_SPEC, "alexscavesexemplified-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TARGETS_CONFIG_SPEC, "alexscavesexemplified-target.toml");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ACExDatagen::generateData);
        ACExDataMaps.register();
        ACExAttachments.init();
        ACExEventBridge.init();
        ACExPacketRegistration.register();
        ACExEnchants.DEF_REG.register(modEventBus);
        ACExEntityRegistry.DEF_REG.register(modEventBus);
        ACExLootModifiers.register(modEventBus);
        modEventBus.register(new ACExModEvents());
        MinecraftForge.EVENT_BUS.register(new ACExemplifiedEvents());
        MinecraftForge.EVENT_BUS.register(new ACEAddTargets());
        MinecraftForge.EVENT_BUS.register(new ACEAddGoals());
        MinecraftForge.EVENT_BUS.addListener(ACExSugarStaffAbilities::tickSweetRelease);
        MinecraftForge.EVENT_BUS.addListener(ACExSugarStaffAbilities::removeStaleScripted);
        MinecraftForge.EVENT_BUS.addListener(ACExSugarStaffAbilities::anchorHex);
        MinecraftForge.EVENT_BUS.addListener(ACExSugarStaffAbilities::cancelSpearKnockback);
        MinecraftForge.EVENT_BUS.addListener(ACExArmedUnderzealot::dropDagger);
        MinecraftForge.EVENT_BUS.addListener(ACExCommands::register);
        MinecraftForge.EVENT_BUS.addListener(ACExScheduler::tick);
        MinecraftForge.EVENT_BUS.addListener(ACExSectumsempra::tick);
        MinecraftForge.EVENT_BUS.addListener(ACExSectumsempra::onKill);
        MinecraftForge.EVENT_BUS.addListener(ACExSectumsempra::onAttack);
        MinecraftForge.EVENT_BUS.addListener(ACExSectumsempra::onIncomingDamage);
        MinecraftForge.EVENT_BUS.addListener(ACExSectumsempra::onEntityTick);
        MinecraftForge.EVENT_BUS.addListener(ACExSectumsempra::clear);
        MinecraftForge.EVENT_BUS.addListener(ACExAmberMonolith::onRightClick);
        MinecraftForge.EVENT_BUS.addListener(ACExPrimordialProwess::onEquipmentChange);
        MinecraftForge.EVENT_BUS.addListener(ACExScheduler::clear);
        MinecraftForge.EVENT_BUS.addListener(ACExGammaAttacks::onDetonate);
        ACExParticleRegistry.DEF_REG.register(modEventBus);
        ACExBlockRegistry.DEF_REG.register(modEventBus);
        ACExItemRegistry.DEF_REG.register(modEventBus);
        ACExMenuRegistry.DEF_REG.register(modEventBus);
        ACExFeatureRegistry.DEF_REG.register(modEventBus);
        PROXY.init(modEventBus);
        ACExEffects.EFFECT_REGISTER.register(modEventBus);
        ACExSoundRegistry.DEF_REG.register(modEventBus);
        ACExEffects.POTION_REGISTER.register(modEventBus);
        modEventBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ACExItemRegistry.setup();
            PROXY.clientInit();
            registerFlammability();
            ACExCauldronInteraction.bootStrap();
            ACExModEvents.addBlockEntityBlocks();
        });
    }

    private static void registerFlammability() {
        FireBlock fireblock = (FireBlock) Blocks.FIRE;
        if (AlexsCavesExemplified.COMMON_CONFIG.ADDITIONAL_FLAMMABILITY_ENABLED.get()){
            //Primordial Caves
            fireblock.setFlammable(ACBlockRegistry.AMBER.get(), 5, 20);
            fireblock.setFlammable(ACExBlockRegistry.PRESERVED_AMBER.get(), 5, 20);
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
