package org.crimsoncrips.alexscavesexemplified.client.compendium;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class ACExCompendium {

    public static final String BASE_DIRECTORY = "alexscaves:books/";
    public static final String EXEMPLIFIED_DIRECTORY = AlexsCavesExemplified.MODID + ":books/";
    public static final String EXEMPLIFIED_ROOT_TITLE = "book." + AlexsCavesExemplified.MODID + ".root";
    public static final int LINES_PER_PAGE = 15;
    private static final float ERASE_LINE_STAGGER = 0.75F;
    private static final float ERASE_LINE_TICKS = 8.0F;
    private static final float WRITE_LINE_STAGGER = 1.0F;
    private static final float WRITE_LINE_TICKS = 12.0F;
    private static final int PHASE_PADDING_TICKS = 4;
    private static final float TITLE_HUE = 0.11F;
    private static final float TITLE_HUE_SWAY = 0.04F;
    private static final float TITLE_WAVE_HEIGHT = 0.6F;
    private static final double TITLE_WAVE_SPEED = 1.2D;
    private static final float TITLE_RAISE = 6.0F;
    public static final String HIGHLIGHTED_WORD = "exemplified";
    private static final float WORD_HUE = 0.12F;
    private static final float WORD_HUE_SWAY = 0.025F;

    public enum Phase {
        NONE,
        ERASE,
        WRITE
    }

    public static final int BUTTON_PAGE = 2;
    private static final ResourceLocation BUTTON_TEXTURE = AlexsCavesExemplified.prefix("textures/item/ace_book.png");
    private static final float BUTTON_X = 122.0F;
    private static final float BUTTON_Y = -5.0F;
    private static final float BUTTON_SIZE = 16.0F;
    private static final float BUTTON_HOVER_SCALE = 1.2F;
    private static final float RIGHT_PAGE_LEAN_X = 0.03F;
    private static final float PAGE_LEAN_Y = -0.38F;
    private static final float LEAN_PER_UNIT_X = 0.00475F;
    private static final float LEAN_PER_UNIT_Y = 0.00425F;

    private static boolean exemplified;
    private static boolean buttonShown;
    private static boolean buttonHovered;
    private static float buttonScale = 1.0F;
    private static Phase phase = Phase.NONE;
    private static int phaseTicks;
    private static int phaseLines = 1;

    public static boolean enabled() {
        return AlexsCavesExemplified.CLIENT_CONFIG.EXEMPLIFIED_COMPENDIUM_ENABLED.get();
    }

    public static void reset() {
        buttonShown = false;
        buttonHovered = false;
        buttonScale = 1.0F;
        exemplified = false;
        phase = Phase.NONE;
        phaseTicks = 0;
    }

    public static boolean isExemplified() {
        return exemplified;
    }

    public static boolean isTransitioning() {
        return phase != Phase.NONE;
    }

    public static boolean showWidgets() {
        return phase == Phase.NONE;
    }

    public static boolean isRootTitle(String title) {
        return EXEMPLIFIED_ROOT_TITLE.equals(title) || "book.alexscaves.root".equals(title);
    }

    public static void updateButton(boolean shown, boolean clickable, float leanX, float leanY) {
        buttonShown = shown;
        float pageX = (leanX - RIGHT_PAGE_LEAN_X) / LEAN_PER_UNIT_X;
        float pageY = (leanY - PAGE_LEAN_Y) / LEAN_PER_UNIT_Y;
        buttonHovered = shown && clickable && !isTransitioning() && pageX >= BUTTON_X && pageX <= BUTTON_X + BUTTON_SIZE && pageY >= BUTTON_Y && pageY <= BUTTON_Y + BUTTON_SIZE;
        buttonScale += ((buttonHovered ? BUTTON_HOVER_SCALE : 1.0F) - buttonScale) * 0.3F;
    }

    public static boolean isButtonShown() {
        return buttonShown;
    }

    public static boolean isButtonHovered() {
        return buttonHovered;
    }

    public static void renderButton(PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        float half = BUTTON_SIZE / 2.0F;
        poseStack.pushPose();
        poseStack.translate(BUTTON_X + half, BUTTON_Y + half, 0.0F);
        poseStack.scale(buttonScale, buttonScale, 1.0F);
        Matrix4f pose = poseStack.last().pose();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.text(BUTTON_TEXTURE));
        consumer.vertex(pose, -half, -half, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
        consumer.vertex(pose, -half, half, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
        consumer.vertex(pose, half, half, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(light).endVertex();
        consumer.vertex(pose, half, -half, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
        poseStack.popPose();
    }

    public static boolean highlights(String title) {
        return exemplified && EXEMPLIFIED_ROOT_TITLE.equals(title);
    }

    public static void drawLine(Font font, String line, float y, int color, boolean highlight, Matrix4f pose, MultiBufferSource bufferSource, int light) {
        int start = highlight ? line.indexOf(HIGHLIGHTED_WORD) : -1;
        if (start < 0) {
            font.drawInBatch(line, 0.0F, y, color, false, pose, bufferSource, Font.DisplayMode.NORMAL, 0, light);
            return;
        }
        int end = Math.min(line.length(), start + HIGHLIGHTED_WORD.length());
        String before = line.substring(0, start);
        font.drawInBatch(before, 0.0F, y, color, false, pose, bufferSource, Font.DisplayMode.NORMAL, 0, light);
        float wordX = font.width(before);
        drawFancyWord(font, line.substring(start, end), wordX, y, pose, bufferSource, light);
        if (end < line.length()) {
            font.drawInBatch(line.substring(end), wordX + font.width(HIGHLIGHTED_WORD.substring(0, end - start)), y, color, false, pose, bufferSource, Font.DisplayMode.NORMAL, 0, light);
        }
    }

    public static void drawFancyWord(Font font, String word, float x, float y, Matrix4f pose, MultiBufferSource bufferSource, int light) {
        double time = System.nanoTime() / 1.0E9D;
        float cursorX = x + Math.round((float) (Math.sin(time * 0.9D) * 1.0D));
        float baseY = y + Math.round((float) (Math.cos(time * 1.3D) * 0.75D));
        for (int i = 0; i < word.length(); i++) {
            String character = String.valueOf(word.charAt(i));
            float wave = (float) Math.sin(time * 2.0D + i * 0.6D);
            float hue = WORD_HUE + WORD_HUE_SWAY * (float) Math.sin(time * 1.5D + i * 0.6D);
            int color = Mth.hsvToRgb(hue, 0.9F, 0.6F + 0.25F * (0.5F + 0.5F * wave));
            font.drawInBatch(character, cursorX, baseY + Math.round(wave), color, false, pose, bufferSource, Font.DisplayMode.NORMAL, 0, light);
            cursorX += font.width(character);
        }
    }

    public static String directory() {
        return exemplified ? EXEMPLIFIED_DIRECTORY : BASE_DIRECTORY;
    }

    public static ResourceLocation counterpart(ResourceLocation entry) {
        String from = exemplified ? BASE_DIRECTORY : EXEMPLIFIED_DIRECTORY;
        String to = exemplified ? EXEMPLIFIED_DIRECTORY : BASE_DIRECTORY;
        String path = entry == null ? "" : entry.toString();
        ResourceLocation target = new ResourceLocation(to + (path.startsWith(from) ? path.substring(from.length()) : "root.json"));
        return Minecraft.getInstance().getResourceManager().getResource(target).isPresent() ? target : new ResourceLocation(to + "root.json");
    }

    public static void start(int lines) {
        phase = Phase.ERASE;
        phaseTicks = 0;
        phaseLines = Math.max(1, lines);
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 0.8F));
    }

    public static boolean tick() {
        if (phase == Phase.NONE)
            return false;
        phaseTicks++;
        if (phase == Phase.ERASE && phaseTicks >= Mth.ceil(phaseLines * ERASE_LINE_STAGGER + ERASE_LINE_TICKS) + PHASE_PADDING_TICKS) {
            exemplified = !exemplified;
            phase = Phase.WRITE;
            phaseTicks = 0;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1.1F));
            return true;
        }
        if (phase == Phase.WRITE && phaseTicks >= Mth.ceil(phaseLines * WRITE_LINE_STAGGER + WRITE_LINE_TICKS) + PHASE_PADDING_TICKS) {
            phase = Phase.NONE;
            phaseTicks = 0;
        }
        return false;
    }

    public static void setLines(int lines) {
        phaseLines = Math.max(1, lines);
    }

    public static float lineVisibility(int line, float partialTick) {
        return switch (phase) {
            case NONE -> 1.0F;
            case ERASE -> Mth.clamp(1.0F - (phaseTicks + partialTick - line * ERASE_LINE_STAGGER) / ERASE_LINE_TICKS, 0.0F, 1.0F);
            case WRITE -> Mth.clamp((phaseTicks + partialTick - line * WRITE_LINE_STAGGER) / WRITE_LINE_TICKS, 0.0F, 1.0F);
        };
    }

    public static boolean isWriting(float visibility) {
        return phase == Phase.WRITE && visibility > 0.0F && visibility < 1.0F;
    }

    public static String visiblePrefix(String text, float visibility) {
        if (visibility >= 1.0F)
            return text;
        int visibleLength = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '§' && i + 1 < text.length()) {
                i++;
            } else {
                visibleLength++;
            }
        }
        int keep = Math.round(visibleLength * visibility);
        StringBuilder builder = new StringBuilder();
        int counted = 0;
        for (int i = 0; i < text.length() && counted < keep; i++) {
            char character = text.charAt(i);
            builder.append(character);
            if (character == '§' && i + 1 < text.length()) {
                builder.append(text.charAt(++i));
            } else {
                counted++;
            }
        }
        return builder.toString();
    }

    public static String toPlainString(FormattedCharSequence sequence) {
        StringBuilder builder = new StringBuilder();
        sequence.accept((index, style, codePoint) -> {
            builder.appendCodePoint(codePoint);
            return true;
        });
        return builder.toString();
    }

    public static void drawFancyTitle(Font font, String text, float x, float y, int outlineColor, Matrix4f pose, MultiBufferSource bufferSource, int light) {
        double time = System.nanoTime() / 1.0E9D;
        float cursorX = x;
        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));
            float wave = (float) Math.sin(time * TITLE_WAVE_SPEED + i * 0.5D);
            float hue = TITLE_HUE + TITLE_HUE_SWAY * (float) Math.sin(time * 1.5D + i * 0.6D);
            int color = Mth.hsvToRgb(hue, 0.7F, 0.85F + 0.15F * (0.5F + 0.5F * wave));
            Matrix4f charPose = new Matrix4f(pose).translate(0.0F, wave * TITLE_WAVE_HEIGHT - TITLE_RAISE, 0.0F);
            font.drawInBatch8xOutline(Component.literal(character).getVisualOrderText(), cursorX, y, color, outlineColor, charPose, bufferSource, light);
            cursorX += font.width(character);
        }
    }
}
