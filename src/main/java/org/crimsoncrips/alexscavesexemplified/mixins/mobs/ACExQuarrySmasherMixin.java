package org.crimsoncrips.alexscavesexemplified.mixins.mobs;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.server.entity.item.QuarrySmasherEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.misc.ACExQuarryUpgrades;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(QuarrySmasherEntity.class)
public abstract class ACExQuarrySmasherMixin extends Entity {

    @Unique
    private static final float MAX_HEAD_GROUND_PROGRESS = 5.0F;
    @Unique
    private static final int MIN_PULL_TICKS = 6;
    @Unique
    private static final double MAX_SLAM_SPEED = 1.0D;

    @Shadow
    private float headGroundProgress;

    @Shadow
    public abstract BlockPos getQuarryPos();

    @Shadow
    public abstract boolean isSlamming();

    public ACExQuarrySmasherMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Unique
    private float speed() {
        return ACExData.get(this, ACExAttachments.SMASHER_SPEED);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            ACExAttachments.set(this, ACExAttachments.SMASHER_SPEED, ACExQuarryUpgrades.speedMultiplier(this.level(), this.getQuarryPos()));
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick1(CallbackInfo ci) {
        float extra = this.speed() - 1.0F;
        if (extra <= 0.0F)
            return;
        if (this.isSlamming()) {
            this.headGroundProgress = Math.min(MAX_HEAD_GROUND_PROGRESS, this.headGroundProgress + extra);
        } else {
            this.headGroundProgress = Math.max(0.0F, this.headGroundProgress - extra);
        }
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 35))
    private int alexsCavesExemplified$tick2(int cooldown) {
        return Math.max(1, Math.round(cooldown / this.speed()));
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/QuarrySmasherEntity;setPullingItemsFor(I)V", ordinal = 0))
    private int alexsCavesExemplified$tick4(int pullTicks) {
        return Math.max(MIN_PULL_TICKS, Math.round(pullTicks / this.speed()));
    }

    @ModifyArg(method = "tickMultipart", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;"))
    private double alexsCavesExemplified$tickMultipart(double headSpeed) {
        return this.isSlamming() ? Math.min(MAX_SLAM_SPEED, headSpeed * this.speed()) : headSpeed;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;destroyBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
    private boolean alexsCavesExemplified$tick5(Level level, BlockPos pos, boolean drop, Operation<Boolean> original) {
        int fortune = ACExQuarryUpgrades.fortune(level, this.getQuarryPos());
        BlockState state = level.getBlockState(pos);
        if (!drop || fortune <= 0 || !(level instanceof ServerLevel) || state.isAir())
            return original.call(level, pos, drop);
        Block.dropResources(state, level, pos, level.getBlockEntity(pos), this, ACExQuarryUpgrades.fortunePickaxe(level, fortune));
        return original.call(level, pos, false);
    }
}
