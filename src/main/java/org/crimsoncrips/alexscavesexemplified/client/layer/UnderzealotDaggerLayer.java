package org.crimsoncrips.alexscavesexemplified.client.layer;

import com.github.alexmodguy.alexscaves.client.model.UnderzealotModel;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.crimsoncrips.alexscavesexemplified.mixins.mobs.underzealot.ACExUnderzealotModelAccessor;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

@OnlyIn(Dist.CLIENT)
public class UnderzealotDaggerLayer extends RenderLayer<UnderzealotEntity, UnderzealotModel> {

    private ItemStack dagger = ItemStack.EMPTY;

    public UnderzealotDaggerLayer(RenderLayerParent<UnderzealotEntity, UnderzealotModel> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, UnderzealotEntity underzealot, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!underzealot.getData(ACExAttachments.ARMED) || underzealot.isInvisible())
            return;
        if (this.dagger.isEmpty()) {
            this.dagger = new ItemStack(ACItemRegistry.DESOLATE_DAGGER.get());
        }
        ACExUnderzealotModelAccessor model = (ACExUnderzealotModelAccessor) this.getParentModel();
        poseStack.pushPose();
        model.alexsCavesExemplified$getRoot().translateAndRotate(poseStack);
        model.alexsCavesExemplified$getBody().translateAndRotate(poseStack);
        model.alexsCavesExemplified$getRightArm().translateAndRotate(poseStack);
        poseStack.translate(-1.75F / 16.0F, 10.0F / 16.0F, -1.0F / 16.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        Minecraft.getInstance().getItemRenderer().renderStatic(underzealot, this.dagger, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, poseStack, buffer, underzealot.level(), packedLight, 0, underzealot.getId());
        poseStack.popPose();
    }
}
