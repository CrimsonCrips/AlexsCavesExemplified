package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.PingPongSpongeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(PingPongSpongeBlock.class)
public class ACExPingPongSpongeMixin extends BushBlock implements BonemealableBlock {

    public ACExPingPongSpongeMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        return pLevel.getBlockState(pPos.above()).is(Blocks.WATER) && pState.getValue(PingPongSpongeBlock.TOP);
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return AlexsCavesExemplified.COMMON_CONFIG.CAVIAL_BONEMEAL_ENABLED.get();
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        pLevel.setBlockAndUpdate(pPos,ACBlockRegistry.PING_PONG_SPONGE.get().defaultBlockState().setValue(PingPongSpongeBlock.TOP,false));
        pLevel.setBlockAndUpdate(pPos.above(),ACBlockRegistry.PING_PONG_SPONGE.get().defaultBlockState().setValue(PingPongSpongeBlock.TOP,true));
    }
}
