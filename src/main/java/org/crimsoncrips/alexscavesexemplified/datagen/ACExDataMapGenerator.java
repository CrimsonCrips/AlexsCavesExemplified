package org.crimsoncrips.alexscavesexemplified.datagen;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import org.crimsoncrips.alexscavesexemplified.backport.DataMapProvider;
import org.crimsoncrips.alexscavesexemplified.server.ACExDataMaps;
import org.crimsoncrips.alexscavesexemplified.server.ACExDataMaps.DepthResistance;

import java.util.concurrent.CompletableFuture;

public class ACExDataMapGenerator extends DataMapProvider {

    public ACExDataMapGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        Builder<DepthResistance, Item> depth = builder(ACExDataMaps.DEPTH_RESISTANCE);
        depth.add(ACItemRegistry.DIVING_HELMET.getId(), new DepthResistance(10), false);
        depth.add(ACItemRegistry.DIVING_CHESTPLATE.getId(), new DepthResistance(18), false);
        depth.add(ACItemRegistry.DIVING_LEGGINGS.getId(), new DepthResistance(12), false);
        depth.add(ACItemRegistry.DIVING_BOOTS.getId(), new DepthResistance(10), false);

        ModLoadedCondition create = new ModLoadedCondition("create");
        depth.add(create("copper_diving_helmet"), new DepthResistance(8), false, create);
        depth.add(create("copper_backtank"), new DepthResistance(8), false, create);
        depth.add(create("copper_diving_boots"), new DepthResistance(4), false, create);
        depth.add(create("netherite_diving_helmet"), new DepthResistance(15), false, create);
        depth.add(create("netherite_backtank"), new DepthResistance(25), false, create);
        depth.add(create("netherite_diving_boots"), new DepthResistance(15), false, create);

        Builder<ResourceLocation, Fluid> freeze = builder(ACExDataMaps.FROSTMINT_FREEZE);
        freeze.add(new ResourceLocation("water"), BuiltInRegistries.BLOCK.getKey(Blocks.ICE), false);
        freeze.add(new ResourceLocation("lava"), BuiltInRegistries.BLOCK.getKey(Blocks.BASALT), false);
        freeze.add(ACFluidRegistry.PURPLE_SODA_FLUID_SOURCE.getId(), ACBlockRegistry.SUGAR_GLASS.getId(), false);
        freeze.add(ACFluidRegistry.ACID_FLUID_SOURCE.getId(), ACBlockRegistry.RADROCK.getId(), false);
        freeze.add(create("chocolate"), ACBlockRegistry.BLOCK_OF_CHOCOLATE.getId(), false, create);
        freeze.add(create("honey"), BuiltInRegistries.BLOCK.getKey(Blocks.HONEY_BLOCK), false, create);
        ModLoadedCondition biomesOPlenty = new ModLoadedCondition("biomesoplenty");
        freeze.add(new ResourceLocation("biomesoplenty", "blood"), new ResourceLocation("biomesoplenty", "flesh"), false, biomesOPlenty);
    }

    private static ResourceLocation create(String path) {
        return new ResourceLocation("create", path);
    }
}
