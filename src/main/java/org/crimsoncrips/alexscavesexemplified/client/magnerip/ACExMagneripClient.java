package org.crimsoncrips.alexscavesexemplified.client.magnerip;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.crimsoncrips.alexscavesexemplified.backport.ClientTickEvent;
import org.crimsoncrips.alexscavesexemplified.misc.ACExMagnerip;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ACExMagneripClient {

    private static final float MIN_SHAKE = 0.35F;
    private static final float MAX_SHAKE = 1.6F;
    private static final int FADE_TICKS = ACExUtils.seconds(0.75F);

    private static float shake;
    private static float prevShake;

    public static void onClientTick(ClientTickEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        prevShake = shake;
        if (player == null) {
            shake = 0.0F;
            prevShake = 0.0F;
            return;
        }
        float progress = ACExData.get(player, ACExAttachments.MAGNERIP_PROGRESS);
        if (progress > 0.0F) {
            progress = Mth.clamp(progress, 0.0F, 1.0F);
            shake = MIN_SHAKE + (MAX_SHAKE - MIN_SHAKE) * progress * progress;
        } else {
            shake = Math.max(0.0F, shake - MAX_SHAKE / FADE_TICKS);
        }
    }

    private static boolean isShaking(Player player, ItemStack stack) {
        EquipmentSlot target = ACExMagnerip.getTarget(player);
        return target != null && player.getItemBySlot(target) == stack && ACExMagnerip.canRip(stack);
    }

    @Nullable
    public static Vec2 getShake(Player player, ItemStack stack, int seed, float partialTick) {
        float amount = Mth.lerp(partialTick, prevShake, shake);
        if (amount <= 0.0F || player != Minecraft.getInstance().player || !isShaking(player, stack))
            return null;
        float time = player.tickCount + partialTick;
        float x = Mth.sin(time * 2.7F + seed * 1.3F) * 0.7F + Mth.sin(time * 6.1F + seed * 0.7F) * 0.3F;
        float y = Mth.sin(time * 3.4F + seed * 2.1F) * 0.7F + Mth.sin(time * 5.3F + seed * 1.9F) * 0.3F;
        return new Vec2(x * amount, y * amount);
    }
}
