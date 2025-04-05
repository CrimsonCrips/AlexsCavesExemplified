package org.crimsoncrips.alexscavesexemplified.server.blocks;

import biomesoplenty.init.ModItems;
import com.github.alexmodguy.alexscaves.server.block.ACSoundTypes;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
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


    

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ACEItemRegistry.DEF_REG.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


}
