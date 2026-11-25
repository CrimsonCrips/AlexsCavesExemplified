package org.crimsoncrips.alexscavesexemplified.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ACExMagnerip {

    public static final int WEAKENED_RIP_TICKS = ACExUtils.seconds(2.5F);
    public static final int RIP_TICKS = ACExUtils.seconds(5.0F);

    public static void pulled(Player player) {
        if (!player.level().isClientSide && AlexsCavesExemplified.COMMON_CONFIG.MAGNERIP_ENABLED.get()) {
            ACExData.set(player, ACExAttachments.MAGNERIP_PULLED, player.level().getGameTime());
        }
    }

    public static void tick(Player player) {
        if (player.level().isClientSide)
            return;
        boolean beingPulled = AlexsCavesExemplified.COMMON_CONFIG.MAGNERIP_ENABLED.get() && !player.isCreative() && !player.isSpectator() && ACExData.has(player, ACExAttachments.MAGNERIP_PULLED) && player.level().getGameTime() - ACExData.get(player, ACExAttachments.MAGNERIP_PULLED) <= 1;
        int ripTicks = ripTicks(player);
        List<EquipmentSlot> targets = beingPulled && ripTicks > 0 ? targets(player) : List.of();
        if (targets.isEmpty()) {
            ACExAttachments.set(player, ACExAttachments.MAGNERIP_PROGRESS, 0.0F);
            return;
        }
        EquipmentSlot target = getTarget(player);
        float progress = ACExData.get(player, ACExAttachments.MAGNERIP_PROGRESS);
        if (target == null || !targets.contains(target)) {
            target = targets.get(player.getRandom().nextInt(targets.size()));
            ACExAttachments.set(player, ACExAttachments.MAGNERIP_SLOT, target.ordinal());
            progress = 0.0F;
        }
        progress += 1.0F / ripTicks;
        if (progress >= 1.0F) {
            player.playSound(ACSoundRegistry.MAGNETRON_HURT.get(), 0.5F, 1.0F);
            ACExUtils.dropMagneticItem(player, player.getItemBySlot(target));
            player.setItemSlot(target, ItemStack.EMPTY);
            ACExAttachments.set(player, ACExAttachments.MAGNERIP_SLOT, -1);
            progress = 0.0F;
        }
        ACExAttachments.set(player, ACExAttachments.MAGNERIP_PROGRESS, progress);
    }

    @Nullable
    public static EquipmentSlot getTarget(Player player) {
        int slot = ACExData.get(player, ACExAttachments.MAGNERIP_SLOT);
        return slot >= 0 && slot < EquipmentSlot.values().length ? EquipmentSlot.values()[slot] : null;
    }

    private static int ripTicks(Player player) {
        if (player.hasEffect(ACEffectRegistry.MAGNETIZING.get()))
            return 0;
        boolean weakness = player.hasEffect(MobEffects.WEAKNESS);
        boolean strength = player.hasEffect(MobEffects.DAMAGE_BOOST);
        if (strength && !weakness)
            return 0;
        return weakness && !strength ? WEAKENED_RIP_TICKS : RIP_TICKS;
    }

    public static boolean canRip(ItemStack stack) {
        return !stack.isEmpty() && stack.is(ACTagRegistry.MAGNETIC_ITEMS) && !EnchantmentHelper.hasBindingCurse(stack);
    }

    private static List<EquipmentSlot> targets(Player player) {
        List<EquipmentSlot> slots = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if ((slot.getType() == EquipmentSlot.Type.ARMOR || slot.getType() == EquipmentSlot.Type.HAND) && canRip(player.getItemBySlot(slot))) {
                slots.add(slot);
            }
        }
        return slots;
    }
}
