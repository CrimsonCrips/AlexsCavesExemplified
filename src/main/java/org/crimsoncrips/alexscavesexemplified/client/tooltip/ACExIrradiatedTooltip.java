package org.crimsoncrips.alexscavesexemplified.client.tooltip;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public record ACExIrradiatedTooltip(Component text) implements TooltipComponent, ClientTooltipComponent {

    public static final String KEY = "misc.alexscavesexemplified.irradiated_crop";

    private static final int GLOW_SAMPLES = 8;
    private static final float GLOW_RADIUS = 1.0F;
    private static final float PULSE_SPEED = 600.0F;

    public static boolean matches(Object element) {
        return element instanceof Component component && component.getContents() instanceof TranslatableContents contents && contents.getKey().equals(KEY);
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public int getWidth(Font font) {
        return font.width(text);
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
        float pulse = (Mth.sin(Util.getMillis() / PULSE_SPEED) + 1.0F) * 0.5F;
        FormattedCharSequence plain = Component.literal(text.getString()).getVisualOrderText();
        int glowAlpha = (int) (0x28 + 0x38 * pulse);
        int glowColor = (glowAlpha << 24) | 0x33FF33;
        for (int i = 0; i < GLOW_SAMPLES; i++) {
            float angle = (float) (Math.PI * 2 * i / GLOW_SAMPLES);
            font.drawInBatch(plain, x + Mth.cos(angle) * GLOW_RADIUS, y + Mth.sin(angle) * GLOW_RADIUS, glowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        }
        int green = (int) ((0.65F + 0.3F * pulse) * 255.0F);
        int textColor = 0xFF000000 | (0x26 << 16) | (green << 8);
        font.drawInBatch(plain, x, y, textColor, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
    }
}
