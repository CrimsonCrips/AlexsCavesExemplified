package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.client.model.SugarStaffModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SugarStaffModel.class)
public interface ACExSugarStaffModelAccessor {

    @Accessor("mint")
    AdvancedModelBox getMint();
}
