package org.crimsoncrips.alexscavesexemplified.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.backport.ACEnchantmentHelper;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.item.RaygunItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import javax.annotation.Nullable;

public class ACExRaygunBlasting {

    private static final int WARMUP = ACExUtils.seconds(0.75F);
    private static final float RANGE = 25.0F;
    private static final float FIRE_CHANCE = 0.05F;
    private static final float HARDNESS_PER_SECOND = 3.0F;
    private static final float BREAK_PER_TICK = HARDNESS_PER_SECOND / ACExUtils.seconds(1.0F);
    private static final float MIN_HARDNESS = 0.2F;

    public static void tick(Level level, LivingEntity shooter, ItemStack stack, int useTicks) {
        if (level.isClientSide)
            return;
        if (!AlexsCavesExemplified.COMMON_CONFIG.REARAYNGEMENT_ENABLED.get() || !RaygunItem.hasCharge(stack) || ACEnchantmentHelper.getEnchantmentLevel(level, ACEnchantmentRegistry.X_RAY, stack) > 0) {
            stop(level, shooter);
            return;
        }
        if (useTicks < WARMUP)
            return;
        HitResult hit = ProjectileUtil.getHitResultOnViewVector(shooter, Entity::canBeHitByProjectile, RANGE);
        if (hit.getType() != HitResult.Type.BLOCK || !(hit instanceof BlockHitResult blockHit)) {
            stop(level, shooter);
            return;
        }
        if (ACEnchantmentHelper.getEnchantmentLevel(level, ACEnchantmentRegistry.GAMMA_RAY, stack) > 0) {
            disintegrate(level, shooter, blockHit);
        } else {
            stop(level, shooter);
            ignite(level, shooter, blockHit);
        }
    }

    public static void stop(Level level, LivingEntity shooter) {
        if (level.isClientSide || !ACExData.has(shooter, ACExAttachments.RAYGUN_BREAK))
            return;
        clearCracks(level, shooter, ACExData.get(shooter, ACExAttachments.RAYGUN_BREAK));
        ACExData.remove(shooter, ACExAttachments.RAYGUN_BREAK);
    }

    private static void disintegrate(Level level, LivingEntity shooter, BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        BreakProgress progress = ACExData.get(shooter, ACExAttachments.RAYGUN_BREAK);
        if (!pos.equals(progress.pos)) {
            clearCracks(level, shooter, progress);
            progress.pos = pos.immutable();
        }
        float hardness = state.getDestroySpeed(level, pos);
        if (state.isAir() || hardness < 0.0F || state.is(BlockTags.WITHER_IMMUNE) || !canAffect(level, shooter, pos)) {
            clearCracks(level, shooter, progress);
            return;
        }
        progress.amount += BREAK_PER_TICK / Math.max(hardness, MIN_HARDNESS);
        if (progress.amount < 1.0F) {
            level.destroyBlockProgress(shooter.getId(), pos, Math.min((int) (progress.amount * 10.0F), 9));
            return;
        }
        clearCracks(level, shooter, progress);
        if (shooter instanceof ServerPlayer player && ForgeHooks.onBlockBreakEvent(level, player.gameMode.getGameModeForPlayer(), player, pos) == -1)
            return;
        level.destroyBlock(pos, false, shooter);
    }

    private static void ignite(Level level, LivingEntity shooter, BlockHitResult hit) {
        if (level.random.nextFloat() >= FIRE_CHANCE)
            return;
        BlockPos firePos = hit.getBlockPos().relative(hit.getDirection());
        if (!level.getBlockState(firePos).isAir() || !BaseFireBlock.canBePlacedAt(level, firePos, hit.getDirection()) || !canAffect(level, shooter, firePos))
            return;
        level.setBlock(firePos, BaseFireBlock.getState(level, firePos), 11);
        level.playSound(null, firePos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.5F, 1.4F + level.random.nextFloat() * 0.2F);
        level.gameEvent(shooter, GameEvent.BLOCK_PLACE, firePos);
    }

    private static boolean canAffect(Level level, LivingEntity shooter, BlockPos pos) {
        if (shooter instanceof Player player)
            return player.mayBuild() && level.mayInteract(player, pos);
        return ForgeEventFactory.getMobGriefingEvent(level, shooter);
    }

    private static void clearCracks(Level level, LivingEntity shooter, BreakProgress progress) {
        if (progress.pos != null && progress.amount > 0.0F) {
            level.destroyBlockProgress(shooter.getId(), progress.pos, -1);
        }
        progress.amount = 0.0F;
    }

    public static class BreakProgress {
        @Nullable
        private BlockPos pos;
        private float amount;
    }
}
