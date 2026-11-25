package org.crimsoncrips.alexscavesexemplified.client.sectumsempra;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.DarknessArmorItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.crimsoncrips.alexscavesexemplified.backport.ClientTickEvent;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSectumsempra;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class ACExSectumsempraClient {

    private static final ItemStack ICON = new ItemStack(ACItemRegistry.DESOLATE_DAGGER.get());
    private static final int ICON_SIZE = 24;
    private static final int ICON_BOTTOM_OFFSET = 30;
    private static final float EMPTY_SHADE = 0.25F;
    public static final int METER_SPACING = 14;
    private static final DustParticleOptions EMPOWERED_DUST = new DustParticleOptions(new Vector3f(0.75F, 0.05F, 0.1F), 0.8F);

    private static boolean holdingDagger(LocalPlayer player) {
        return player.getMainHandItem().is(ACItemRegistry.DESOLATE_DAGGER.get());
    }

    public static boolean isMeterShown(LocalPlayer player) {
        return !Minecraft.getInstance().options.hideGui && ACExSectumsempra.enabled() && ACExSectumsempra.wearsDarkness(player);
    }

    public static void renderBar(GuiGraphics graphics, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || !isMeterShown(player))
            return;
        float charge = ACExSectumsempra.getCharge(player);
        int x = graphics.guiWidth() / 2 - ICON_SIZE / 2 + (DarknessArmorItem.hasMeter(player) ? METER_SPACING : 0);
        int y = graphics.guiHeight() - ICON_BOTTOM_OFFSET - ICON_SIZE;
        float scale = ICON_SIZE / 16.0F;
        drawIcon(graphics, x, y, scale, EMPTY_SHADE, EMPTY_SHADE, EMPTY_SHADE);
        int filled = Math.round(ICON_SIZE * charge);
        if (filled <= 0)
            return;
        boolean flash = charge >= 1.0F && player.tickCount / 2 % 2 == 1;
        graphics.enableScissor(x, y + ICON_SIZE - filled, x + ICON_SIZE, y + ICON_SIZE);
        if (flash) {
            drawIcon(graphics, x, y, scale, 1.0F, 0.35F, 0.35F);
        } else {
            drawIcon(graphics, x, y, scale, 1.0F, 1.0F, 1.0F);
        }
        graphics.disableScissor();
    }

    private static void drawIcon(GuiGraphics graphics, int x, int y, float scale, float red, float green, float blue) {
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0.0F);
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.setColor(red, green, blue, 1.0F);
        graphics.renderItem(ICON, 0, 0);
        graphics.flush();
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.pose().popPose();
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.isPaused() || !ACExSectumsempra.enabled() || !holdingDagger(player) || !ACExSectumsempra.isEmpowered(player) || player.tickCount % 2 != 0)
            return;
        Vec3 hand = player.position().add(0.0D, player.getBbHeight() * 0.6D, 0.0D).add(player.getLookAngle().yRot((float) Math.toRadians(-60.0D)).multiply(0.5D, 0.0D, 0.5D));
        player.level().addParticle(EMPOWERED_DUST, hand.x + (player.getRandom().nextDouble() - 0.5D) * 0.3D, hand.y + (player.getRandom().nextDouble() - 0.5D) * 0.3D, hand.z + (player.getRandom().nextDouble() - 0.5D) * 0.3D, 0.0D, 0.02D, 0.0D);
    }
}
