package org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron;

import java.util.Map;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;

public class ACExCauldron extends Block {
    private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), BooleanOp.ONLY_FIRST);
    private final Map<Item, ACExCauldronInteraction> interactions;

    public ACExCauldron(BlockBehaviour.Properties pProperties, Map<Item, ACExCauldronInteraction> pInteractions) {
        super(pProperties.randomTicks().requiresCorrectToolForDrops().strength(5F));
        this.interactions = pInteractions;
    }


    protected double getContentHeight(BlockState pState) {
        return 0.0D;
    }

    protected boolean isEntityInsideContent(BlockState pState, BlockPos pPos, Entity pEntity) {
        return pEntity.getY() < (double)pPos.getY() + this.getContentHeight(pState) && pEntity.getBoundingBox().maxY > (double)pPos.getY() + 0.25D;
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        ACExCauldronInteraction cauldroninteraction = this.interactions.get(itemstack.getItem());
        return cauldroninteraction.interact(pState, pLevel, pPos, pPlayer, pHand, itemstack);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.LIQUID_REPLICATION_ENABLED.get())
            return;
        BlockPos blockAbove = pPos.above();
        while (pLevel.getBlockState(blockAbove).isAir() && blockAbove.getY() < pLevel.getMaxBuildHeight()) {
            blockAbove = blockAbove.above();
        }
        if (pLevel.getBlockState(blockAbove).is(ACBlockRegistry.ACIDIC_RADROCK.get()) && pRandom.nextDouble() < 0.5) {
            pLevel.setBlockAndUpdate(pPos, ACExBlockRegistry.ACID_CAULDRON.get().defaultBlockState());
            for (Player player : pLevel.getEntitiesOfClass(Player.class, new AABB(pPos.offset(-12, -12, -12), pPos.offset(12, 12, 12)))) {
                ACExUtils.awardAdvancement(player,"acidic_replication","replicate");
            }
        }
    }

    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return INSIDE;
    }


    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }
}