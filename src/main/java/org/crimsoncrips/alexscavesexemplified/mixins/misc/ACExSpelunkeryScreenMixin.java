package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.gui.SpelunkeryTableScreen;
import com.github.alexmodguy.alexscaves.client.gui.SpelunkeryTableWordButton;
import com.github.alexmodguy.alexscaves.server.inventory.SpelunkeryTableMenu;
import com.github.alexmodguy.alexscaves.server.message.SpelunkeryTableChangeMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;


@Mixin(SpelunkeryTableScreen.class)
public abstract class ACExSpelunkeryScreenMixin extends AbstractContainerScreen<SpelunkeryTableMenu> {

    @Shadow private int highlightColor;

    @Shadow private int level;

    @Shadow private boolean doneWithTutorial;

    @Shadow public abstract void fullResetWords();

    @Shadow private boolean finishedLevel;

    @Shadow private float passLevelProgress;

    @Shadow private int attemptsLeft;

    @Shadow public abstract boolean hasTablet();

    @Shadow private List<SpelunkeryTableWordButton> wordButtons;

    @Shadow protected abstract void clearWordWidgets();

    @Shadow private int tutorialStep;

    @Shadow public abstract boolean hasPaper();

    @Shadow private boolean hasClickedLens;

    @Shadow protected abstract void generateWords(ResourceLocation file);

    @Shadow private ResourceLocation prevWordsFile;

    public ACExSpelunkeryScreenMixin(SpelunkeryTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Inject(method = "containerTick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/inventory/SpelunkeryTableMenu;getHighlightColor(Lnet/minecraft/world/level/Level;)I"), cancellable = true,locals = LocalCapture.CAPTURE_FAILHARD)
    private void drawOrb(CallbackInfo ci, int targetMagnifyX, int targetMagnifyY, int maxDistance, Vec3 vec3, boolean resetTabletFromWin) {
        ci.cancel();

        int currentColor = ((SpelunkeryTableMenu)this.menu).getHighlightColor(Minecraft.getInstance().level);

        if (currentColor != -1) {
            this.highlightColor = currentColor;
        }

        if (resetTabletFromWin && this.level >= 3) {
            this.doneWithTutorial = true;
            SpelunkeryTableMenu var10000 = (SpelunkeryTableMenu)this.menu;
            SpelunkeryTableMenu.setTutorialComplete(Minecraft.getInstance().player, true);
            AlexsCaves.NETWORK_WRAPPER.sendToServer(new SpelunkeryTableChangeMessage(true));
            this.level = 0;
            this.fullResetWords();
        } else if (this.finishedLevel && this.passLevelProgress >= 10.0F && this.attemptsLeft <= 0) {
            if (AlexsCavesExemplified.COMMON_CONFIG.FORGIVING_SPELUKING_ENABLED.get()) {
                if (level <= 0) {
                    AlexsCaves.NETWORK_WRAPPER.sendToServer(new SpelunkeryTableChangeMessage(false));
                    fullResetWords();
                    Minecraft.getInstance().setScreen(null);
                } else {
                    passLevelProgress = 0;
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(level >= 3 ? ACSoundRegistry.SPELUNKERY_TABLE_SUCCESS_COMPLETE.get() : ACSoundRegistry.SPELUNKERY_TABLE_SUCCESS.get(), 1.0F));
                    clearWordWidgets();
                    generateWords(prevWordsFile);
                    finishedLevel = false;
                    level--;
                }
            } else {
                level = 0;
                AlexsCaves.NETWORK_WRAPPER.sendToServer(new SpelunkeryTableChangeMessage(false));
                fullResetWords();
                Minecraft.getInstance().setScreen(null);
            }
        }

        if (!this.hasTablet() && !this.wordButtons.isEmpty()) {
            this.clearWordWidgets();
        }

        if (this.doneWithTutorial) {
            this.tutorialStep = 6;
        } else if (!this.hasTablet()) {
            this.tutorialStep = 0;
        } else if (!this.hasPaper()) {
            this.tutorialStep = 1;
        } else if (this.attemptsLeft == 5 && this.level == 0) {
            this.tutorialStep = 2;
        } else if (!this.hasClickedLens) {
            this.tutorialStep = 3;
        } else if (this.level == 0) {
            this.tutorialStep = 4;
        } else {
            this.tutorialStep = 5;
        }
    }

    @ModifyReturnValue(method = "getRevealWordsAmount", at = @At("RETURN"),remap = false)
    private float revealWords(float original) {
        if (AlexsCavesExemplified.COMMON_CONFIG.BRAINDEAD_MODE_ENABLED.get()) {
            return 1;
        } else {
            return original;
        }
    }



    @Shadow protected abstract boolean hasClickedAnyWord();


    @ModifyConstant(method = "generateWords",constant = @Constant(intValue = 5),remap = false)
    private int modifyAmount(int amount) {
        return AlexsCavesExemplified.COMMON_CONFIG.SPELUNKY_ATTEMPTS_AMOUNT.get();
    }

    @Override
    public void onClose() {
        if (AlexsCavesExemplified.COMMON_CONFIG.REDOABLE_SPELUNKY_ENABLED.get()) {
            super.onClose();
        } else {
            if (hasPaper() && hasTablet() && hasClickedAnyWord() && level < 3) {
                AlexsCaves.NETWORK_WRAPPER.sendToServer(new SpelunkeryTableChangeMessage(false));
            }
        }

    }


}
