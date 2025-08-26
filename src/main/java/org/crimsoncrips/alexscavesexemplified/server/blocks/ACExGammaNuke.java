package org.crimsoncrips.alexscavesexemplified.server.blocks;

import com.github.alexmodguy.alexscaves.server.block.NuclearBombBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;
import org.crimsoncrips.alexscavesexemplified.server.entity.GammaNuclearBombEntity;

import javax.annotation.Nullable;

public class ACExGammaNuke extends NuclearBombBlock {

    public void onCaughtFire(BlockState state, Level level, BlockPos blockPos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!level.isClientSide) {
            GammaNuclearBombEntity bomb = ACExEntityRegistry.GAMMA_NUCLEAR_BOMB.get().create(level);
            bomb.setPos((double)blockPos.getX() + (double)0.5F, (double)blockPos.getY(), (double)blockPos.getZ() + (double)0.5F);
            level.addFreshEntity(bomb);
            level.playSound((Player)null, bomb.getX(), bomb.getY(), bomb.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(igniter, GameEvent.PRIME_FUSE, blockPos);
        }
    }

}