package org.crimsoncrips.alexscavesexemplified.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import java.io.IOException;
import java.io.UncheckedIOException;

@OnlyIn(Dist.CLIENT)
public final class ACExShaders {

    private static ShaderInstance radiantAura;
    private static ShaderInstance sweetReleaseGlow;
    private static ShaderInstance sweetReleasePulse;
    private static ShaderInstance sweetReleaseElectric;
    private static ShaderInstance darkMonolithGlow;
    private static ShaderInstance darkMonolithOutline;

    private ACExShaders() {
    }

    public static void register(RegisterShadersEvent event) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), AlexsCavesExemplified.prefix("wheel/radiant_aura"), DefaultVertexFormat.POSITION_TEX), shader -> radiantAura = shader);
            event.registerShader(new ShaderInstance(event.getResourceProvider(), AlexsCavesExemplified.prefix("wheel/lockdown_glow"), DefaultVertexFormat.POSITION_TEX), shader -> sweetReleaseGlow = shader);
            event.registerShader(new ShaderInstance(event.getResourceProvider(), AlexsCavesExemplified.prefix("wheel/lockdown_pulse"), DefaultVertexFormat.POSITION_TEX), shader -> sweetReleasePulse = shader);
            event.registerShader(new ShaderInstance(event.getResourceProvider(), AlexsCavesExemplified.prefix("wheel/lockdown_electric"), DefaultVertexFormat.POSITION_TEX), shader -> sweetReleaseElectric = shader);
            event.registerShader(new ShaderInstance(event.getResourceProvider(), AlexsCavesExemplified.prefix("dark_monolith_glow"), DefaultVertexFormat.NEW_ENTITY), shader -> darkMonolithGlow = shader);
            event.registerShader(new ShaderInstance(event.getResourceProvider(), AlexsCavesExemplified.prefix("dark_monolith_outline"), DefaultVertexFormat.POSITION_TEX_COLOR), shader -> darkMonolithOutline = shader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static ShaderInstance radiantAura() {
        return radiantAura;
    }

    public static ShaderInstance sweetReleaseGlow() {
        return sweetReleaseGlow;
    }

    public static ShaderInstance sweetReleasePulse() {
        return sweetReleasePulse;
    }

    public static ShaderInstance sweetReleaseElectric() {
        return sweetReleaseElectric;
    }

    public static ShaderInstance darkMonolithGlow() {
        return darkMonolithGlow;
    }

    public static ShaderInstance darkMonolithOutline() {
        return darkMonolithOutline;
    }
}
