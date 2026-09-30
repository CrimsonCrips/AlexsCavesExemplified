package org.crimsoncrips.alexscavesexemplified.server.blocks;

import com.github.alexmodguy.alexscaves.server.block.ACSoundTypes;
import com.github.alexmodguy.alexscaves.server.block.CavePaintingBlock;
import com.github.alexmodguy.alexscaves.server.item.BlockItemWithSupplierLore;
import com.github.alexthe666.citadel.item.BlockItemWithSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.AcidCauldronBlock;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.MetalCauldronBlock;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.PurpleSodaCauldronBlock;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExRarity;

import java.util.function.Supplier;

public class ACExBlockRegistry {

   public static final DeferredRegister<Block> DEF_REG = DeferredRegister.create(Registries.BLOCK, AlexsCavesExemplified.MODID);

    public static final DeferredHolder<Block, Block> METAL_CAULDRON = registerBlockAndItem("metal_cauldron", () -> new MetalCauldronBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(ACSoundTypes.SCRAP_METAL).noOcclusion()));

    public static final DeferredHolder<Block, Block> GAMMA_NUCLEAR_BOMB = registerBlockAndItemAnomaly("gamma_nuclear_bomb", () -> new ACExGammaNuke(), ACExRarity.GAMMA.getValue());

    public static final DeferredHolder<Block, Block> ACID_CAULDRON = DEF_REG.register("acid_cauldron", () -> new AcidCauldronBlock(BlockBehaviour.Properties.ofFullCopy(METAL_CAULDRON.get()).lightLevel((p_50870_) -> 13)));

    public static final DeferredHolder<Block, Block> PURPLE_SODA_CAULDRON = DEF_REG.register("purple_soda_cauldron", () -> new PurpleSodaCauldronBlock(BlockBehaviour.Properties.ofFullCopy(METAL_CAULDRON.get())));


    public static final DeferredHolder<Block, Block> CAVE_PAINTING_SACRIFICE_1 = registerBlockAndItemLored("cave_painting_sacrifice_1", CavePaintingBlock::new);
    public static final DeferredHolder<Block, Block> CAVE_PAINTING_SACRIFICE_2 = registerBlockAndItemLored("cave_painting_sacrifice_2", CavePaintingBlock::new);
    public static final DeferredHolder<Block, Block> CAVE_PAINTING_SACRIFICE_3 = registerBlockAndItemLored("cave_painting_sacrifice_3", CavePaintingBlock::new);
    public static final DeferredHolder<Block, Block> CAVE_PAINTING_SACRIFICE_4 = registerBlockAndItemLored("cave_painting_sacrifice_4", CavePaintingBlock::new);
    public static final DeferredHolder<Block, Block> CAVE_PAINTING_SACRIFICE_5 = registerBlockAndItemLored("cave_painting_sacrifice_5", CavePaintingBlock::new);
    public static final DeferredHolder<Block, Block> CAVE_PAINTING_SACRIFICE_6 = registerBlockAndItemLored("cave_painting_sacrifice_6", CavePaintingBlock::new);
    public static final DeferredHolder<Block, Block> CAVE_PAINTING_SACRIFICE_7 = registerBlockAndItemLored("cave_painting_sacrifice_7", CavePaintingBlock::new);
    public static final DeferredHolder<Block, Block> CAVE_PAINTING_SACRIFICE_8 = registerBlockAndItemLored("cave_painting_sacrifice_8", CavePaintingBlock::new);
    public static final DeferredHolder<Block, Block> CAVE_PAINTING_SACRIFICE_9 = registerBlockAndItemLored("cave_painting_sacrifice_9", CavePaintingBlock::new);



    private static DeferredHolder<Block, Block> registerBlockAndItem(String name, Supplier<Block> block) {
        DeferredHolder<Block, Block> blockObj = DEF_REG.register(name, block);
        ACExItemRegistry.DEF_REG.register(name, () -> new BlockItemWithSupplier(blockObj, new Item.Properties()));
        return blockObj;
    }

    private static DeferredHolder<Block, Block> registerBlockAndItemAnomaly(String name, Supplier<Block> block,Rarity rarity) {
        DeferredHolder<Block, Block> blockObj = DEF_REG.register(name, block);
        ACExItemRegistry.DEF_REG.register(name, () -> new BlockItemWithSupplierLore(blockObj, new Item.Properties().rarity(rarity)));
        return blockObj;
    }

    private static DeferredHolder<Block, Block> registerBlockAndItemLored(String name, Supplier<Block> block) {
        DeferredHolder<Block, Block> blockObj = DEF_REG.register(name, block);
        ACExItemRegistry.DEF_REG.register(name, () -> new BlockItemWithSupplierLore(blockObj, new Item.Properties()));
        return blockObj;
    }
    


}
