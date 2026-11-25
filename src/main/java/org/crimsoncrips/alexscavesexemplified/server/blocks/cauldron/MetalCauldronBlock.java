package org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;

import javax.annotation.Nullable;

public class MetalCauldronBlock extends ACExCauldron {

    private static final float ACID_DRIP_CHANCE = 0.05859375F;
    private static final int MAX_DRIP_DISTANCE = 11;
    private static final int DRIP_DELAY = ACExUtils.seconds(2.5F);
    private static final double ADVANCEMENT_RANGE = 12.0D;

    public MetalCauldronBlock(BlockBehaviour.Properties p_51403_) {
        super(p_51403_, CauldronInteraction.EMPTY);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.LIQUID_REPLICATION_ENABLED.get() || random.nextFloat() >= ACID_DRIP_CHANCE)
            return;
        BlockPos radrock = acidicRadrockAbove(level, pos);
        if (radrock != null) {
            level.scheduleTick(pos, this, DRIP_DELAY + radrock.getY() - pos.getY());
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        if (!AlexsCavesExemplified.COMMON_CONFIG.LIQUID_REPLICATION_ENABLED.get() || acidicRadrockAbove(level, pos) == null)
            return;
        BlockState acid = ACExBlockRegistry.ACID_CAULDRON.get().defaultBlockState();
        level.setBlockAndUpdate(pos, acid);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(acid));
        level.playSound(null, pos, SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON, SoundSource.BLOCKS, 2.0F, 0.9F + random.nextFloat() * 0.2F);
        for (Player player : level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(ADVANCEMENT_RANGE))) {
            ACExUtils.awardAdvancement(player, "acidic_replication", "replicate");
        }
    }

    @Nullable
    private static BlockPos acidicRadrockAbove(BlockGetter level, BlockPos cauldron) {
        BlockPos.MutableBlockPos pos = cauldron.mutable();
        for (int i = 0; i < MAX_DRIP_DISTANCE; i++) {
            pos.move(0, 1, 0);
            BlockState state = level.getBlockState(pos);
            if (state.is(ACBlockRegistry.ACIDIC_RADROCK.get()))
                return pos.immutable();
            if (!canDripThrough(level, pos, state))
                return null;
        }
        return null;
    }

    private static boolean canDripThrough(BlockGetter level, BlockPos pos, BlockState state) {
        return state.isAir() || !state.isSolidRender(level, pos) && state.getFluidState().isEmpty() && state.getCollisionShape(level, pos).isEmpty();
    }
}
