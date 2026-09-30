package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(VesperEntity.class)
public abstract class ACExVesperMixin extends Monster {


    protected ACExVesperMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    public boolean canBeLeashed() {
        return super.canBeLeashed() || (AlexsCavesExemplified.COMMON_CONFIG.DARK_OFFERING_ENABLED.get() && !isPassenger());
    }


}
