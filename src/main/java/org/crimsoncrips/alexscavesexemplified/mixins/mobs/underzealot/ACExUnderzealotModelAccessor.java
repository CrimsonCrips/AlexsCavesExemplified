package org.crimsoncrips.alexscavesexemplified.mixins.mobs.underzealot;

import com.github.alexmodguy.alexscaves.client.model.UnderzealotModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = UnderzealotModel.class, remap = false)
public interface ACExUnderzealotModelAccessor {

    @Accessor("root")
    AdvancedModelBox alexsCavesExemplified$getRoot();

    @Accessor("body")
    AdvancedModelBox alexsCavesExemplified$getBody();

    @Accessor("rarm")
    AdvancedModelBox alexsCavesExemplified$getRightArm();
}
