package org.crimsoncrips.alexscavesexemplified.client.model;

import com.github.alexmodguy.alexscaves.server.entity.item.TephraEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import org.crimsoncrips.alexscavesexemplified.server.entity.GammaBlock;

public class GammaBlockModel extends AdvancedEntityModel<GammaBlock> {
    private final AdvancedModelBox main;

    public GammaBlockModel() {
        texWidth = 64;
        texHeight = 64;

        main = new AdvancedModelBox(this);
        main.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.setTextureOffset(0, 0).addBox(-7.0F, -7.0F, -7.0F, 14.0F, 14.0F, 14.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(main);
    }

    @Override
    public void setupAnim(GammaBlock tephraEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        main.rotateAngleZ += ageInTicks * 0.2F;
        main.rotateAngleX += ageInTicks * 0.4F;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(main);
    }

}
