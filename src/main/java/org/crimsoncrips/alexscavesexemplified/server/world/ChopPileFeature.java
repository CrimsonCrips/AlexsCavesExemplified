package org.crimsoncrips.alexscavesexemplified.server.world;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.DinosaurChopBlock;
import com.github.alexmodguy.alexscaves.server.block.ThinBoneBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import javax.annotation.Nullable;

public class ChopPileFeature extends Feature<ChopPileConfiguration> {
    private static final int FLOOR_SEARCH = 3;
    private static final float UPRIGHT_BONE_CHANCE = 0.2F;

    public ChopPileFeature() {
        super(ChopPileConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<ChopPileConfiguration> context) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.SCAVENGING_ENABLED.get())
            return false;
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        ChopPileConfiguration config = context.config();
        int spread = config.spread();
        int count = config.count().sample(random);
        boolean placed = false;
        for (int i = 0; i < count; i++) {
            BlockPos floor = findFloor(level, context.origin().offset(random.nextInt(spread * 2 + 1) - spread, 0, random.nextInt(spread * 2 + 1) - spread));
            if (floor != null) {
                level.setBlock(floor, random.nextFloat() < config.boneChance() ? bone(random) : chop(random, config.maxBites()), 2);
                placed = true;
            }
        }
        return placed;
    }

    @Nullable
    private static BlockPos findFloor(WorldGenLevel level, BlockPos column) {
        for (int dy = FLOOR_SEARCH; dy >= -FLOOR_SEARCH; dy--) {
            BlockPos pos = column.above(dy);
            BlockPos below = pos.below();
            if (level.getBlockState(pos).isAir() && level.getBlockState(below).isFaceSturdy(level, below, Direction.UP))
                return pos;
        }
        return null;
    }

    private static BlockState chop(RandomSource random, int maxBites) {
        return ACBlockRegistry.DINOSAUR_CHOP.get().defaultBlockState()
                .setValue(DinosaurChopBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random))
                .setValue(DinosaurChopBlock.BITES, maxBites == 0 ? 0 : random.nextInt(maxBites + 1));
    }

    private static BlockState bone(RandomSource random) {
        Direction.Axis axis = random.nextFloat() < UPRIGHT_BONE_CHANCE ? Direction.Axis.Y : Direction.Plane.HORIZONTAL.getRandomAxis(random);
        return ACBlockRegistry.THIN_BONE.get().defaultBlockState().setValue(ThinBoneBlock.AXIS, axis);
    }
}
