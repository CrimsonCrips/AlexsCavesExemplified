package org.crimsoncrips.alexscavesexemplified.server.blocks;


import net.minecraft.world.level.BlockGetter;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.item.PreservedAmberItem;

import java.util.List;

public class PreservedAmberBlock extends AbstractGlassBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<PreservedCreature> CREATURE = EnumProperty.create("creature", PreservedCreature.class);
    private static final int RELEASE_SLOWNESS = ACExUtils.seconds(300.0F);

    public PreservedAmberBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(CREATURE, PreservedCreature.TEMPERATE_FROG));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CREATURE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacent, Direction direction) {
        return adjacent.is(ACBlockRegistry.AMBER.get()) || super.skipRendering(state, adjacent, direction);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if (!state.getValue(CREATURE).isAvailable())
            return ACBlockRegistry.AMBER.get().defaultBlockState().getDrops(params);
        return super.getDrops(state, params);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return PreservedAmberItem.withCreature(super.getCloneItemStack(level, pos, state), state.getValue(CREATURE));
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level instanceof ServerLevel serverLevel && (player.isCreative() || !hasSilkTouch(serverLevel, player.getMainHandItem()) || !state.canHarvestBlock(level, pos, player))) {
            release(serverLevel, pos, state);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, tool, dropExperience);
        if (tool.isEmpty()) {
            release(level, pos, state);
        }
    }

    private static boolean hasSilkTouch(ServerLevel level, ItemStack tool) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;
    }

    public static void release(ServerLevel level, BlockPos pos, BlockState state) {
        Mob mob = state.getValue(CREATURE).create(level);
        if (mob == null)
            return;
        float rotation = state.getValue(FACING).toYRot();
        mob.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, rotation, 0.0F);
        mob.setYBodyRot(rotation);
        mob.setYHeadRot(rotation);
        mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, RELEASE_SLOWNESS, 1, false, false));
        level.addFreshEntity(mob);
    }
}
