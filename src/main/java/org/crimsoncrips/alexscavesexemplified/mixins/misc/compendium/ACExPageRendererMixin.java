package org.crimsoncrips.alexscavesexemplified.mixins.misc.compendium;

import com.github.alexmodguy.alexscaves.client.gui.book.BookEntry;
import com.github.alexmodguy.alexscaves.client.gui.book.BookLink;
import com.github.alexmodguy.alexscaves.client.gui.book.CaveBookScreen;
import com.github.alexmodguy.alexscaves.client.gui.book.PageRenderer;
import com.github.alexmodguy.alexscaves.client.gui.book.widget.BookWidget;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.crimsoncrips.alexscavesexemplified.client.compendium.ACExCompendium;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PageRenderer.class)
public abstract class ACExPageRendererMixin {

    @Unique
    private static final String WRITING_CURSOR = "_";

    @Shadow
    private BookEntry entry;
    @Shadow
    private int entryPageNumber;

    @Shadow
    public abstract int getDisplayPageNumber();

    @Unique
    private boolean hasTitle() {
        return this.entryPageNumber == 0 && this.entry != null && !this.entry.getTranslatableTitle().isEmpty();
    }

    @Unique
    private static float partialTick() {
        return Minecraft.getInstance().getFrameTime();
    }

    @WrapOperation(method = "renderPage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch8xOutline(Lnet/minecraft/util/FormattedCharSequence;FFIILorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void alexsCavesExemplified$renderPage(Font font, FormattedCharSequence text, float x, float y, int color, int outlineColor, Matrix4f pose, MultiBufferSource bufferSource, int light, Operation<Void> original) {
        boolean fancy = ACExCompendium.enabled() && ACExCompendium.isExemplified() && ACExCompendium.EXEMPLIFIED_ROOT_TITLE.equals(this.entry.getTranslatableTitle());
        if (!fancy && !ACExCompendium.isTransitioning()) {
            original.call(font, text, x, y, color, outlineColor, pose, bufferSource, light);
            return;
        }
        String title = ACExCompendium.visiblePrefix(ACExCompendium.toPlainString(text), ACExCompendium.lineVisibility(0, partialTick()));
        if (title.isEmpty())
            return;
        if (fancy) {
            ACExCompendium.drawFancyTitle(font, title, x, y, outlineColor, pose, bufferSource, light);
        } else {
            original.call(font, Component.literal(title).getVisualOrderText(), x, y, color, outlineColor, pose, bufferSource, light);
        }
    }

    @WrapWithCondition(method = "renderPage", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/gui/book/widget/BookWidget;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;FZ)V"), remap = false)
    private boolean alexsCavesExemplified$renderPage1(BookWidget widget, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float partialTicks, boolean onFlippingPage) {
        return ACExCompendium.showWidgets();
    }

    @Inject(method = "renderPage", at = @At("TAIL"), remap = false)
    private void alexsCavesExemplified$renderPage2(CaveBookScreen screen, PoseStack poseStack, int mouseX, int mouseY, float partialTicks, boolean onFlippingPage, CallbackInfo ci) {
        if (ACExCompendium.isButtonShown() && this.entry != null && this.getDisplayPageNumber() == ACExCompendium.BUTTON_PAGE && ACExCompendium.isRootTitle(this.entry.getTranslatableTitle())) {
            ACExCompendium.renderButton(poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), 15728880);
        }
    }

    @Inject(method = "printLinesFromEntry", at = @At("HEAD"), cancellable = true, remap = false)
    private void alexsCavesExemplified$printLinesFromEntry(Font font, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, BookEntry bookEntry, int startReadingAt, CallbackInfo ci) {
        boolean highlight = ACExCompendium.enabled() && ACExCompendium.highlights(bookEntry.getTranslatableTitle());
        if ((!ACExCompendium.isTransitioning() && !highlight) || startReadingAt < 0)
            return;
        ci.cancel();
        int pageSide = (this.getDisplayPageNumber() - 1) % 2;
        int firstLine = (this.hasTitle() ? 1 : 0) + pageSide * ACExCompendium.LINES_PER_PAGE;
        float partialTick = partialTick();
        for (int i = startReadingAt; i < startReadingAt + ACExCompendium.LINES_PER_PAGE && i < bookEntry.getEntryText().size(); i++) {
            int row = i - startReadingAt;
            float visibility = ACExCompendium.isTransitioning() ? ACExCompendium.lineVisibility(firstLine + row, partialTick) : 1.0F;
            if (visibility <= 0.0F)
                continue;
            String line = ACExCompendium.visiblePrefix(bookEntry.getEntryText().get(i), visibility);
            float y = row * 10;
            ACExCompendium.drawLine(font, line, y, CaveBookScreen.TEXT_COLOR, highlight, poseStack.last().pose(), bufferSource, 15728880);
            for (BookLink link : bookEntry.getEntryLinks()) {
                int shown = Math.min(link.getDisplayText().length(), line.length() - link.getCharacterStartsAt());
                if (link.getLineNumber() != i || shown <= 0)
                    continue;
                float linkX = font.width(line.substring(0, link.getCharacterStartsAt()));
                int linkColor = link.isEnabled() ? (link.isHovered() ? CaveBookScreen.TEXT_LINK_HOVER_COLOR : CaveBookScreen.TEXT_LINK_COLOR) : CaveBookScreen.TEXT_LINK_LOCKED_COLOR;
                font.drawInBatch(Component.literal(link.getDisplayText().substring(0, shown)).withStyle(ChatFormatting.UNDERLINE), linkX, y, linkColor, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            }
            if (ACExCompendium.isWriting(visibility)) {
                font.drawInBatch(WRITING_CURSOR, font.width(line), y, CaveBookScreen.TEXT_COLOR, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            }
        }
    }
}
