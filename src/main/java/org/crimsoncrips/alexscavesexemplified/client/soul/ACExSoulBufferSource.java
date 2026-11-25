package org.crimsoncrips.alexscavesexemplified.client.soul;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.crimsoncrips.alexscavesexemplified.mixins.misc.render.ACExCompositeRenderTypeAccessor;
import org.crimsoncrips.alexscavesexemplified.mixins.misc.render.ACExCompositeStateAccessor;
import org.crimsoncrips.alexscavesexemplified.mixins.misc.render.ACExTextureStateShardAccessor;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class ACExSoulBufferSource implements MultiBufferSource {

    private static final float RED = 1.0F;
    private static final float GREEN = 0.3F;
    private static final float BLUE = 0.3F;
    private static final float ALPHA = 0.55F;

    private final MultiBufferSource delegate;

    public ACExSoulBufferSource(MultiBufferSource delegate) {
        this.delegate = delegate;
    }

    private static Optional<ResourceLocation> texture(RenderType type) {
        if (!(type instanceof ACExCompositeRenderTypeAccessor composite))
            return Optional.empty();
        Object state = composite.invokeState();
        Object shard = ((ACExCompositeStateAccessor) state).getTextureState();
        return ((ACExTextureStateShardAccessor) shard).invokeCutoutTexture();
    }

    @Override
    public VertexConsumer getBuffer(RenderType type) {
        Optional<ResourceLocation> texture = texture(type);
        if (texture.isEmpty())
            return Discard.INSTANCE;
        return new Ghost(this.delegate.getBuffer(RenderType.entityTranslucentEmissive(texture.get())));
    }

    private record Ghost(VertexConsumer delegate) implements VertexConsumer {
        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            this.delegate.vertex(x, y, z);
            return this;
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha) {
            this.delegate.color((int) (red * RED), (int) (green * GREEN), (int) (blue * BLUE), (int) (alpha * ALPHA));
            return this;
        }

        @Override
        public VertexConsumer uv(float u, float v) {
            this.delegate.uv(u, v);
            return this;
        }

        @Override
        public VertexConsumer overlayCoords(int u, int v) {
            this.delegate.overlayCoords(OverlayTexture.NO_OVERLAY);
            return this;
        }

        @Override
        public VertexConsumer uv2(int u, int v) {
            this.delegate.uv2(LightTexture.FULL_BRIGHT);
            return this;
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            this.delegate.normal(x, y, z);
            return this;
        }

        @Override
        public void endVertex() {
            this.delegate.endVertex();
        }

        @Override
        public void defaultColor(int red, int green, int blue, int alpha) {
            this.delegate.defaultColor((int) (red * RED), (int) (green * GREEN), (int) (blue * BLUE), (int) (alpha * ALPHA));
        }

        @Override
        public void unsetDefaultColor() {
            this.delegate.unsetDefaultColor();
        }
    }

    private enum Discard implements VertexConsumer {
        INSTANCE;

        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            return this;
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha) {
            return this;
        }

        @Override
        public VertexConsumer uv(float u, float v) {
            return this;
        }

        @Override
        public VertexConsumer overlayCoords(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer uv2(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            return this;
        }

        @Override
        public void endVertex() {
        }

        @Override
        public void defaultColor(int red, int green, int blue, int alpha) {
        }

        @Override
        public void unsetDefaultColor() {
        }
    }
}
