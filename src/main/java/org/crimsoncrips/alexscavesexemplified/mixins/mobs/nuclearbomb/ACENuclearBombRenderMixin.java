package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nuclearbomb;

import com.github.alexmodguy.alexscaves.client.render.entity.NuclearBombRenderer;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.ACEFeatures;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(NuclearBombRenderer.class)
public abstract class ACENuclearBombRenderMixin extends EntityRenderer<NuclearBombEntity> {


    protected ACENuclearBombRenderMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @ModifyArg(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/item/NuclearBombEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/render/entity/NuclearBombRenderer;renderModel(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/resources/model/BakedModel;FFFIILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V"),index = 4,remap = false)
    private float alexsCavesExemplified$render(float p_111072_,@Local NuclearBombEntity entity,@Local (ordinal = 2) float progress) {
        return ((Gammafied)entity).isGamma() ? 1.0F - progress : p_111072_;
    }

    @ModifyArg(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/item/NuclearBombEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/client/render/entity/NuclearBombRenderer;renderModel(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/resources/model/BakedModel;FFFIILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V"),index = 6,remap = false)
    private float alexsCavesExemplified$render1(float p_111072_,@Local NuclearBombEntity entity,@Local (ordinal = 2) float progress) {
        return ((Gammafied)entity).isGamma() ? 1.0F + progress : p_111072_;
    }

}
