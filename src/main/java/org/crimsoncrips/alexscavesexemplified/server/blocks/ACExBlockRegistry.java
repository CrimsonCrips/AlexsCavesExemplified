package org.crimsoncrips.alexscavesexemplified.server.blocks;

import com.github.alexmodguy.alexscaves.server.block.ACSoundTypes;
import com.github.alexmodguy.alexscaves.server.block.CavePaintingBlock;
import com.github.alexmodguy.alexscaves.server.item.BlockItemWithSupplierLore;
import com.github.alexthe666.citadel.item.BlockItemWithSupplier;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.AcidCauldronBlock;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.MetalCauldronBlock;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.PurpleSodaCauldronBlock;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;

import java.util.function.Supplier;

public class ACExBlockRegistry {

   public static final DeferredRegister<Block> DEF_REG = DeferredRegister.create(ForgeRegistries.BLOCKS, AlexsCavesExemplified.MODID);

    public static final Rarity RARITY_GAMMA = Rarity.create("alexscavesexemplified:gamma", ChatFormatting.AQUA);

    public static final RegistryObject<Block> METAL_CAULDRON = registerBlockAndItem("metal_cauldron", () -> new MetalCauldronBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(ACSoundTypes.SCRAP_METAL).noOcclusion()));

    public static final RegistryObject<Block> GAMMA_NUCLEAR_BOMB = registerBlockAndItemAnomaly("gamma_nuclear_bomb", () -> new ACExGammaNuke(),RARITY_GAMMA);

    public static final RegistryObject<Block> ACID_CAULDRON = DEF_REG.register("acid_cauldron", () -> new AcidCauldronBlock(BlockBehaviour.Properties.copy(METAL_CAULDRON.get()).lightLevel((p_50870_) -> 13)));

    public static final RegistryObject<Block> PURPLE_SODA_CAULDRON = DEF_REG.register("purple_soda_cauldron", () -> new PurpleSodaCauldronBlock(BlockBehaviour.Properties.copy(METAL_CAULDRON.get())));


    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_1 = registerBlockAndItemLored("cave_painting_sacrifice_1", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_2 = registerBlockAndItemLored("cave_painting_sacrifice_2", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_3 = registerBlockAndItemLored("cave_painting_sacrifice_3", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_4 = registerBlockAndItemLored("cave_painting_sacrifice_4", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_5 = registerBlockAndItemLored("cave_painting_sacrifice_5", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_6 = registerBlockAndItemLored("cave_painting_sacrifice_6", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_7 = registerBlockAndItemLored("cave_painting_sacrifice_7", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_8 = registerBlockAndItemLored("cave_painting_sacrifice_8", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_9 = registerBlockAndItemLored("cave_painting_sacrifice_9", CavePaintingBlock::new);



    private static RegistryObject<Block> registerBlockAndItem(String name, Supplier<Block> block) {
        RegistryObject<Block> blockObj = DEF_REG.register(name, block);
        ACExItemRegistry.DEF_REG.register(name, () -> new BlockItemWithSupplier(blockObj, new Item.Properties()));
        return blockObj;
    }

    private static RegistryObject<Block> registerBlockAndItemAnomaly(String name, Supplier<Block> block,Rarity rarity) {
        RegistryObject<Block> blockObj = DEF_REG.register(name, block);
        ACExItemRegistry.DEF_REG.register(name, () -> new BlockItemWithSupplierLore(blockObj, new Item.Properties().rarity(rarity)));
        return blockObj;
    }

    private static RegistryObject<Block> registerBlockAndItemLored(String name, Supplier<Block> block) {
        RegistryObject<Block> blockObj = DEF_REG.register(name, block);
        ACExItemRegistry.DEF_REG.register(name, () -> new BlockItemWithSupplierLore(blockObj, new Item.Properties()));
        return blockObj;
    }
    


}
