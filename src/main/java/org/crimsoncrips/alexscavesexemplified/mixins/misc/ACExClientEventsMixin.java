package org.crimsoncrips.alexscavesexemplified.mixins.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.spongepowered.asm.mixin.Unique;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.client.event.ClientEvents;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.RidingMeterMount;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.client.sectumsempra.ACExSectumsempraClient;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(ClientEvents.class)
public class ACExClientEventsMixin {

    @Unique
    private static final ResourceLocation GAMMA_HUD_OVERLAYS = new ResourceLocation(AlexsCavesExemplified.MODID, "textures/gui/gamma_hud_overlays.png");


    @ModifyArg(method = "onPostRenderGuiOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V",ordinal = 0),index = 0)
    private ResourceLocation alexsCavesExemplified$onPostRenderGuiOverlay1(ResourceLocation pAtlasLocation, @Local RidingMeterMount ridingMeterMount) {
        if (ridingMeterMount instanceof TremorzillaEntity tremorzilla && ACExData.get(tremorzilla, ACExAttachments.GAMMA)) {
            return GAMMA_HUD_OVERLAYS;
        }
        return pAtlasLocation;
    }

    @ModifyArg(method = "onPostRenderGuiOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V",ordinal = 1),index = 0)
    private ResourceLocation alexsCavesExemplified$onPostRenderGuiOverlay2(ResourceLocation pAtlasLocation, @Local RidingMeterMount ridingMeterMount) {
        if (ridingMeterMount instanceof TremorzillaEntity tremorzilla && ACExData.get(tremorzilla, ACExAttachments.GAMMA)) {
            return GAMMA_HUD_OVERLAYS;

        }
        return pAtlasLocation;
    }

    @ModifyArg(method = "onPostRenderGuiOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V",ordinal = 2),index = 1)
    private int alexsCavesExemplified$onPostRenderGuiOverlay3(int x) {
        return this.darknessMeterX(x);
    }

    @ModifyArg(method = "onPostRenderGuiOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V",ordinal = 3),index = 1)
    private int alexsCavesExemplified$onPostRenderGuiOverlay4(int x) {
        return this.darknessMeterX(x);
    }

    @Unique
    private int darknessMeterX(int x) {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && ACExSectumsempraClient.isMeterShown(player) ? x - ACExSectumsempraClient.METER_SPACING : x;
    }
}
