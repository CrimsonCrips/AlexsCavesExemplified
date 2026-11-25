package org.crimsoncrips.alexscavesexemplified.backport;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.Model;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ACExRenderCompat {

    public static void renderToBuffer(Model model, PoseStack poseStack, VertexConsumer consumer, int light, int overlay, int color) {
        model.renderToBuffer(poseStack, consumer, light, overlay, red(color), green(color), blue(color), alpha(color));
    }

    public static float alpha(int color) {
        return (color >>> 24 & 255) / 255.0F;
    }

    public static float red(int color) {
        return (color >> 16 & 255) / 255.0F;
    }

    public static float green(int color) {
        return (color >> 8 & 255) / 255.0F;
    }

    public static float blue(int color) {
        return (color & 255) / 255.0F;
    }

    public static int colorFromFloat(float alpha, float red, float green, float blue) {
        return (Mth.floor(alpha * 255.0F) & 255) << 24 | (Mth.floor(red * 255.0F) & 255) << 16 | (Mth.floor(green * 255.0F) & 255) << 8 | Mth.floor(blue * 255.0F) & 255;
    }

    public static int color(int alpha, int red, int green, int blue) {
        return (alpha & 255) << 24 | (red & 255) << 16 | (green & 255) << 8 | blue & 255;
    }

    public static int opaque(int color) {
        return color | 0xFF000000;
    }

    public static ResourceLocation spriteTexture(ResourceLocation sprite) {
        return new ResourceLocation(sprite.getNamespace(), "textures/gui/sprites/" + sprite.getPath() + ".png");
    }

    public static void blitNineSliced(GuiGraphics graphics, ResourceLocation texture, int x, int y, int width, int height, int border, int textureWidth, int textureHeight) {
        int innerW = textureWidth - border * 2;
        int innerH = textureHeight - border * 2;
        int midW = width - border * 2;
        int midH = height - border * 2;
        graphics.blit(texture, x, y, border, border, 0, 0, border, border, textureWidth, textureHeight);
        graphics.blit(texture, x + width - border, y, border, border, textureWidth - border, 0, border, border, textureWidth, textureHeight);
        graphics.blit(texture, x, y + height - border, border, border, 0, textureHeight - border, border, border, textureWidth, textureHeight);
        graphics.blit(texture, x + width - border, y + height - border, border, border, textureWidth - border, textureHeight - border, border, border, textureWidth, textureHeight);
        if (midW > 0) {
            graphics.blit(texture, x + border, y, midW, border, border, 0, innerW, border, textureWidth, textureHeight);
            graphics.blit(texture, x + border, y + height - border, midW, border, border, textureHeight - border, innerW, border, textureWidth, textureHeight);
        }
        if (midH > 0) {
            graphics.blit(texture, x, y + border, border, midH, 0, border, border, innerH, textureWidth, textureHeight);
            graphics.blit(texture, x + width - border, y + border, border, midH, textureWidth - border, border, border, innerH, textureWidth, textureHeight);
        }
        if (midW > 0 && midH > 0) {
            graphics.blit(texture, x + border, y + border, midW, midH, border, border, innerW, innerH, textureWidth, textureHeight);
        }
    }
}
