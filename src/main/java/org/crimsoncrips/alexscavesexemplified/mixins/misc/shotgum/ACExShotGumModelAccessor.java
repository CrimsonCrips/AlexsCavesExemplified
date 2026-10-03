package org.crimsoncrips.alexscavesexemplified.mixins.misc.shotgum;

import com.github.alexmodguy.alexscaves.client.model.ShotGumModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ShotGumModel.class, remap = false)
public interface ACExShotGumModelAccessor {

    @Accessor("shot_gum")
    AdvancedModelBox alexsCavesExemplified$getBody();

    @Accessor("barrels")
    AdvancedModelBox alexsCavesExemplified$getBarrels();

    @Accessor("crank")
    AdvancedModelBox alexsCavesExemplified$getCrank();
}
