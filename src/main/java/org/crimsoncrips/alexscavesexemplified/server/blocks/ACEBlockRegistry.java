package org.crimsoncrips.alexscavesexemplified.server.blocks;

import biomesoplenty.init.ModItems;
import com.github.alexmodguy.alexscaves.server.block.ACSoundTypes;
import com.github.alexmodguy.alexscaves.server.block.CavePaintingBlock;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.BlockItemWithSupplierLore;
import com.github.alexthe666.alexsmobs.item.AMBlockItem;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.item.BlockItemAMRender;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.AcidCauldronBlock;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.MetalCauldronBlock;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.PurpleSodaCauldronBlock;
import org.crimsoncrips.alexscavesexemplified.server.item.ACEItemRegistry;

import java.util.function.Supplier;

public class ACEBlockRegistry {

   public static final DeferredRegister<Block> DEF_REG = DeferredRegister.create(ForgeRegistries.BLOCKS, AlexsCavesExemplified.MODID);

    public static final RegistryObject<Block> METAL_CAULDRON = DEF_REG.register("metal_cauldron", () -> new MetalCauldronBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.0F).noOcclusion().strength(5F, 15.0F).sound(ACSoundTypes.SCRAP_METAL)));

    public static final RegistryObject<Block> ACID_CAULDRON = DEF_REG.register("acid_cauldron", () -> new AcidCauldronBlock(BlockBehaviour.Properties.copy(METAL_CAULDRON.get()).lightLevel((p_50870_) -> 15).strength(5F, 15.0F).sound(ACSoundTypes.SCRAP_METAL)));

    public static final RegistryObject<Block> PURPLE_SODA_CAULDRON = DEF_REG.register("purple_soda_cauldron", () -> new PurpleSodaCauldronBlock(BlockBehaviour.Properties.copy(METAL_CAULDRON.get()).strength(5F, 15.0F).sound(ACSoundTypes.SCRAP_METAL)));


    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_1 = registerBlockAndItem("cave_painting_sacrifice_1", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_2 = registerBlockAndItem("cave_painting_sacrifice_2", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_3 = registerBlockAndItem("cave_painting_sacrifice_3", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_4 = registerBlockAndItem("cave_painting_sacrifice_4", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_5 = registerBlockAndItem("cave_painting_sacrifice_5", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_6 = registerBlockAndItem("cave_painting_sacrifice_6", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_7 = registerBlockAndItem("cave_painting_sacrifice_7", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_8 = registerBlockAndItem("cave_painting_sacrifice_8", CavePaintingBlock::new);
    public static final RegistryObject<Block> CAVE_PAINTING_SACRIFICE_9 = registerBlockAndItem("cave_painting_sacrifice_9", CavePaintingBlock::new);


    private static RegistryObject<Block> registerBlockAndItem(String name, Supplier<Block> block) {
        RegistryObject<Block> blockObj = DEF_REG.register(name, block);
        ACEItemRegistry.DEF_REG.register(name, () -> new BlockItemWithSupplierLore(blockObj, new Item.Properties()));
        return blockObj;
    }
    


}
