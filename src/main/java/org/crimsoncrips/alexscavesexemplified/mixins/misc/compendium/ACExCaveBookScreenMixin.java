package org.crimsoncrips.alexscavesexemplified.mixins.misc.compendium;



import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.github.alexmodguy.alexscaves.client.gui.book.BookEntry;
import com.github.alexmodguy.alexscaves.client.gui.book.CaveBookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.compendium.ACExCompendium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CaveBookScreen.class)
public abstract class ACExCaveBookScreenMixin extends Screen {

    @Unique
    private static final float BOOK_SCALE = 221.0F;


    @Shadow
    protected ResourceLocation currentEntryJSON;
    @Shadow
    private BookEntry currentEntry;
    @Shadow
    private int entryPageNumber;
    @Shadow
    private boolean incrementingPage;
    @Shadow
    private boolean decrementingPage;
    @Shadow
    private float openBookProgress;
    @Shadow
    private float flipProgress;


    protected ACExCaveBookScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    public abstract void resetEntry();

    @Shadow
    public abstract void updatePageRenderers();

    @WrapOperation(method = "<init>(Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/gui/book/CaveBookScreen;resetEntry()V"), remap = false)
    private void alexsCavesExemplified$init(CaveBookScreen screen, Operation<Void> original) {
        ACExCompendium.reset();
        original.call(screen);
    }

    @Override
    public void removed() {
        super.removed();
        ACExCompendium.reset();
    }

    @Inject(method = "getBookFileDirectory", at = @At("HEAD"), cancellable = true, remap = false)
    private static void alexsCavesExemplified$getBookFileDirectory(CallbackInfoReturnable<String> cir) {
        if (ACExCompendium.isExemplified()) {
            cir.setReturnValue(ACExCompendium.EXEMPLIFIED_DIRECTORY);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (!ACExCompendium.tick())
            return;
        int page = this.entryPageNumber;
        this.currentEntryJSON = ACExCompendium.counterpart(this.currentEntryJSON);
        this.resetEntry();
        if (this.currentEntry != null) {
            this.entryPageNumber = Math.max(0, Math.min(page, this.currentEntry.getPageCount() - 1));
            this.updatePageRenderers();
        }
        ACExCompendium.setLines(this.visibleLines());
    }

    @Inject(method = {"canGoLeft", "canGoRight"}, at = @At("HEAD"), cancellable = true, remap = false)
    private void alexsCavesExemplified$canGoLeft(CallbackInfoReturnable<Boolean> cir) {
        if (ACExCompendium.isTransitioning()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void alexsCavesExemplified$mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!ACExCompendium.enabled())
            return;
        if (ACExCompendium.isTransitioning()) {
            cir.setReturnValue(true);
            return;
        }
        if (ACExCompendium.isButtonHovered()) {
            ACExCompendium.start(this.visibleLines());
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void alexsCavesExemplified$render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        ACExCompendium.updateButton(this.buttonVisible(), this.buttonClickable(), (mouseX - this.width / 2.0F) / BOOK_SCALE, (mouseY - this.height / 2.0F) / BOOK_SCALE);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void alexsCavesExemplified$render1(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (ACExCompendium.isButtonHovered()) {
            Component tooltip = Component.translatable(ACExCompendium.isExemplified() ? "book." + AlexsCavesExemplified.MODID + ".switch_base" : "book." + AlexsCavesExemplified.MODID + ".switch_exemplified");
            guiGraphics.renderTooltip(Minecraft.getInstance().font, tooltip, mouseX, mouseY);
        }
    }

    @Unique
    private boolean buttonVisible() {
        return ACExCompendium.enabled() && this.currentEntry != null && ACExCompendium.isRootTitle(this.currentEntry.getTranslatableTitle());
    }

    @Unique
    private boolean buttonClickable() {
        return this.openBookProgress >= 1.0F && this.flipProgress <= 0.0F && !this.incrementingPage && !this.decrementingPage && this.entryPageNumber == 0;
    }

    @Unique
    private int visibleLines() {
        if (this.currentEntry == null)
            return 1;
        boolean title = this.entryPageNumber == 0 && !this.currentEntry.getTranslatableTitle().isEmpty();
        int remaining = this.currentEntry.getEntryText().size() - this.entryPageNumber * ACExCompendium.LINES_PER_PAGE * 2;
        return (title ? 1 : 0) + Math.max(0, Math.min(ACExCompendium.LINES_PER_PAGE * 2, remaining));
    }
}
