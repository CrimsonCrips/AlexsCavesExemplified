package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.GloomothEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(GloomothEntity.class)
public abstract class ACExGloomothMixin extends PathfinderMob {


    protected ACExGloomothMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return super.canBeLeashed(pPlayer) && !isPassenger();
    }


}
