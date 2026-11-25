package org.crimsoncrips.alexscavesexemplified.client.render;

import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Function;

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

    private static final Function<ResourceLocation, RenderType> DARK_MONOLITH_GLOW = Util.memoize(texture -> create("alexscavesexemplified_dark_monolith_glow", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder()
            .setShaderState(new ShaderStateShard(ACExShaders::darkMonolithGlow))
            .setCullState(NO_CULL)
            .setTextureState(new TextureStateShard(texture, false, false))
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .setDepthTestState(LEQUAL_DEPTH_TEST)
            .setOutputState(IRRADIATED_OUTPUT)
            .createCompositeState(true)));

    private static final Function<ResourceLocation, RenderType> DARK_MONOLITH_MASK = Util.memoize(texture -> create("alexscavesexemplified_dark_monolith_mask", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
            .setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER)
            .setCullState(NO_CULL)
            .setTextureState(new TextureStateShard(texture, false, false))
            .setTransparencyState(NO_TRANSPARENCY)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .setDepthTestState(LEQUAL_DEPTH_TEST)
            .setWriteMaskState(COLOR_WRITE)
            .setOutputState(IRRADIATED_OUTPUT)
            .createCompositeState(false)));

    private static final Function<ResourceLocation, RenderType> DARK_MONOLITH_OUTLINE = Util.memoize(texture -> create("alexscavesexemplified_dark_monolith_outline", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 1536, false, false, CompositeState.builder()
            .setShaderState(new ShaderStateShard(ACExShaders::darkMonolithOutline))
            .setTextureState(new TextureStateShard(texture, false, false))
            .setCullState(NO_CULL)
            .setDepthTestState(NO_DEPTH_TEST)
            .setOutputState(OUTLINE_TARGET)
            .createCompositeState(false)));

    public ACExRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType getIrradiatedItem() {
        return IRRADIATED_ITEM;
    }

    public static RenderType getGammaItem() {
        return GAMMA_ITEM;
    }

    public static RenderType getDarkMonolithGlow(ResourceLocation texture) {
        return DARK_MONOLITH_GLOW.apply(texture);
    }

    public static RenderType getDarkMonolithMask(ResourceLocation texture) {
        return DARK_MONOLITH_MASK.apply(texture);
    }

    public static RenderType getDarkMonolithOutline(ResourceLocation texture) {
        return DARK_MONOLITH_OUTLINE.apply(texture);
    }
}
