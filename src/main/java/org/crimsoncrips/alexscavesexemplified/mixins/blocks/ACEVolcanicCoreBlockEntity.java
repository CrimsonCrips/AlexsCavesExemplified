package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.VolcanicCoreBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.TephraEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.ACEReflectionUtil;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;


@Mixin(VolcanicCoreBlockEntity.class)
public abstract class ACEVolcanicCoreBlockEntity extends BlockEntity{



    public ACEVolcanicCoreBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    private static final Predicate<ItemEntity> recharge = item -> item.getItem().is(ACBlockRegistry.ATLATITAN_EGG.get().asItem());



    @Inject(method = "tick", at = @At("HEAD"),remap = false)
    private static void tick(Level level, BlockPos blockPos, BlockState state, VolcanicCoreBlockEntity entity, CallbackInfo ci) {

        int bossCooldown = (int) ACEReflectionUtil.getField(entity, "bossSpawnCooldown");


        if (bossCooldown > 0 && AlexsCavesExemplified.COMMON_CONFIG.VOLCANIC_SACRIFICE_ENABLED.get()) {

            Vec3 vec3 = Vec3.atCenterOf(blockPos);
            AABB itemAABB = new AABB(vec3.subtract(20, 100, 20), vec3.add(20, 100, 20));
            double maxDist = 100;
            for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, itemAABB, recharge)) {

                double dist = Mth.sqrt((float) item.distanceToSqr(vec3));
                if (dist < maxDist) {
                    item.getPersistentData().putBoolean("DraggedProtection", true);
                    item.setGlowingTag(true);
                    Vec3 sub = vec3.subtract(item.position()).normalize().scale(0.2F);
                    Vec3 delta = item.getDeltaMovement().scale(0.8F);
                    item.setDeltaMovement(sub.add(delta));
                }
                if (dist < 1F) {
                    item.getItem().shrink(1);
                    if (level.getRandom().nextDouble() < 1) {
                        spawnTephra(level, entity);
                        ACEReflectionUtil.setField(entity, "bossSpawnCooldown", 0);
                        ACEUtils.awardAdvancement(item.getOwner(),"egg_sacrifice","egg_sacrifice");
                    }
                }
            }


            for (AtlatitanEntity atlatitan : level.getEntitiesOfClass(AtlatitanEntity.class, new AABB(blockPos.offset(-12, -12, -12), blockPos.offset(12, 32, 12)))) {
                if (atlatitan.isBaby()){
                    double dist = Mth.sqrt((float) atlatitan.distanceToSqr(vec3));
                    if (dist < maxDist) {
                        Vec3 sub = vec3.subtract(atlatitan.position()).normalize().scale(0.2F);
                        Vec3 delta = atlatitan.getDeltaMovement().scale(1F);
                        atlatitan.setDeltaMovement(sub.add(delta));
                    }
                    if (dist < 0.66F) {
                        atlatitan.kill();
                        if (level.getRandom().nextDouble() < 1) {
                            spawnTephra(level, entity);
                            for (Player player : level.getEntitiesOfClass(Player.class, atlatitan.getBoundingBox().inflate(40))) {
                                ACEUtils.awardAdvancement(player, "volcanic_sacrifice", "live_sacrifice");
                            }
                            ACEReflectionUtil.setField(entity, "bossSpawnCooldown", 0);
                        }
                    }
                }
            }
        }
    }

    private static BlockPos.MutableBlockPos getTopOfVolcano(BlockPos posIn,Level level) {
        BlockPos.MutableBlockPos volcanoTop = new BlockPos.MutableBlockPos();
        volcanoTop.set(posIn);

        while(level.getBlockState(volcanoTop).is(ACTagRegistry.VOLCANO_BLOCKS) && volcanoTop.getY() < level.getMaxBuildHeight()) {
            volcanoTop.move(0, 1, 0);
        }

        volcanoTop.move(0, -1, 0);
        return volcanoTop;
    }

    private static void spawnTephra(Level level, VolcanicCoreBlockEntity entity){
        BlockPos volcanoTop = getTopOfVolcano(entity.getBlockPos(),level).immutable();
        if (level.getBlockState(volcanoTop).is(ACBlockRegistry.VOLCANIC_CORE.get()) || level.getBlockState(volcanoTop).is(ACBlockRegistry.PRIMAL_MAGMA.get()) || level.getBlockState(volcanoTop).is(ACBlockRegistry.FISSURE_PRIMAL_MAGMA.get()) || level.getFluidState(volcanoTop).is(FluidTags.LAVA)) {
            Vec3 volcanoVec = Vec3.upFromBottomCenterOf(volcanoTop, 3F);
            Player nearestPlayer = level.getNearestPlayer(volcanoVec.x, volcanoVec.y, volcanoVec.z, 400D, true);
            if (true) {
                TephraEntity bigTephra = ACEntityRegistry.TEPHRA.get().create(level);
                bigTephra.setPos(volcanoVec);
                bigTephra.setMaxScale(2F + level.random.nextFloat());
                Vec3 targetVec;
                if (nearestPlayer == null) {
                    targetVec = new Vec3(level.random.nextFloat() - 0.5F, 0, level.random.nextFloat() - 0.5F).normalize().scale(level.random.nextInt(50) + 20);
                } else {
                    targetVec = nearestPlayer.position().subtract(volcanoVec);
                    bigTephra.setArcingTowards(nearestPlayer.getUUID());
                }
                double d4 = Math.sqrt(targetVec.x * targetVec.x + targetVec.z * targetVec.z);
                double d5 = nearestPlayer == null ? level.random.nextFloat() : 0;
                bigTephra.shoot(targetVec.x, targetVec.y + 0.5F + d4 * 0.75F + d5, targetVec.z,  (float) (d4 * 0.1F + d5), 1 + level.random.nextFloat() * 0.5F);
                level.addFreshEntity(bigTephra);
            }
            for(int smalls = 0; smalls < 3 + level.random.nextInt(3); smalls++){
                TephraEntity smallTephra = ACEntityRegistry.TEPHRA.get().create(level);
                smallTephra.setPos(volcanoVec);
                smallTephra.setMaxScale(0.6F + 0.6F * level.random.nextFloat());
                Vec3 targetVec = new Vec3(level.random.nextFloat() - 0.5F, 0, level.random.nextFloat() - 0.5F).normalize().scale(level.random.nextInt(30) + 30);
                double d4 = Math.sqrt(targetVec.x * targetVec.x + targetVec.z * targetVec.z);
                smallTephra.shoot(targetVec.x, targetVec.y + 0.5F + d4 * 0.75F + level.random.nextFloat(), targetVec.z, (float) (d4 * 0.1F + level.random.nextFloat()), 1);
                level.addFreshEntity(smallTephra);
            }
        }
    }

}
