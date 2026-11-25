package org.crimsoncrips.alexscavesexemplified.backport;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ACExCompat {

    public static void consume(ItemStack stack, int amount, @Nullable LivingEntity entity) {
        if (entity == null || !(entity instanceof Player player) || !player.getAbilities().instabuild) {
            stack.shrink(amount);
        }
    }

    public static ItemStack consumeAndReturn(ItemStack stack, int amount, @Nullable LivingEntity entity) {
        ItemStack copy = stack.copyWithCount(amount);
        consume(stack, amount, entity);
        return copy;
    }

    public static EquipmentSlot slotForHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }

    public static UUID uuid(ResourceLocation id) {
        return UUID.nameUUIDFromBytes(id.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static AttributeModifier modifier(ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        return new AttributeModifier(uuid(id), id.toString(), amount, operation);
    }
}
