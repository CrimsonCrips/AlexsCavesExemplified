package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.SugarStaffHexEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(SugarStaffHexEntity.class)
public abstract class ACExSugarStaffHexMixin extends Entity {


    public ACExSugarStaffHexMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean ignoreExplosion() {
        return AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get();
    }

}
