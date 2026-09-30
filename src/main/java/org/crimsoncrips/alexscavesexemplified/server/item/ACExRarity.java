package org.crimsoncrips.alexscavesexemplified.server.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.UnaryOperator;

public class ACExRarity {

    public static final EnumProxy<Rarity> GAMMA = new EnumProxy<>(Rarity.class, -1, "alexscavesexemplified:gamma", (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.AQUA));
}
