package org.crimsoncrips.alexscavesexemplified.client.render;

import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ACExRenderTypes extends ACRenderTypes {

    private static final RenderType IRRADIATED_ITEM = create("alexscavesexemplified_irradiated_item", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder()
            .setShaderState(RENDERTYPE_IRRADIATED_SHADER)
            .setTextureState(new TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setCullState(NO_CULL)
            .setDepthTestState(LEQUAL_DEPTH_TEST)
            .setWriteMaskState(COLOR_WRITE)
            .createCompositeState(false));

    private static final RenderType GAMMA_ITEM = create("alexscavesexemplified_gamma_item", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder()
            .setShaderState(RENDERTYPE_BLUE_IRRADIATED_SHADER)
            .setTextureState(new TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setCullState(NO_CULL)
            .setDepthTestState(LEQUAL_DEPTH_TEST)
            .setWriteMaskState(COLOR_WRITE)
            .createCompositeState(false));

    public ACExRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType getIrradiatedItem() {
        return IRRADIATED_ITEM;
    }

    public static RenderType getGammaItem() {
        return GAMMA_ITEM;
    }
}
