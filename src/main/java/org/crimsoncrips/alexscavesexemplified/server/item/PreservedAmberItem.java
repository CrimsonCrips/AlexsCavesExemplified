package org.crimsoncrips.alexscavesexemplified.server.item;



import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.PreservedAmberBlock;
import org.crimsoncrips.alexscavesexemplified.server.blocks.PreservedCreature;

import java.util.List;

public class PreservedAmberItem extends BlockItem {

    public PreservedAmberItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static PreservedCreature getCreature(ItemStack stack) {
        CompoundTag blockState = stack.getTagElement(BlockItem.BLOCK_STATE_TAG);
        PreservedCreature creature = blockState == null ? null : PreservedAmberBlock.CREATURE.getValue(blockState.getString(PreservedAmberBlock.CREATURE.getName())).orElse(null);
        return creature == null ? PreservedCreature.TEMPERATE_FROG : creature;
    }

    public static ItemStack withCreature(ItemStack stack, PreservedCreature creature) {
        stack.getOrCreateTagElement(BlockItem.BLOCK_STATE_TAG).putString(PreservedAmberBlock.CREATURE.getName(), PreservedAmberBlock.CREATURE.getName(creature));
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        PreservedCreature creature = getCreature(stack);
        if (!creature.isAvailable())
            return;
        tooltip.add(Component.translatable("block." + AlexsCavesExemplified.MODID + ".preserved_amber.contains", creature.getDisplayName()).withStyle(ChatFormatting.GRAY));
    }
}
