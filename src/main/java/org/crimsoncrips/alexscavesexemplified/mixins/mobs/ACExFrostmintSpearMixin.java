package org.crimsoncrips.alexscavesexemplified.mixins.mobs;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.server.entity.item.FrostmintSpearEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.server.ACExDataMaps;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;



@Mixin(FrostmintSpearEntity.class)
public abstract class ACExFrostmintSpearMixin extends AbstractArrow {


    @Shadow private boolean exploded;

    @Shadow @Nullable public abstract Entity getOwner();

    protected ACExFrostmintSpearMixin(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean ignoreExplosion() {
        return AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get() && ACExData.get(this, ACExAttachments.FROST_RADIANT);
    }


    @Inject(method = "onHit", at = @At("HEAD"))
    private void alexsCavesExemplified$onHit(HitResult hitResult, CallbackInfo ci) {
        ACExSugarStaffAbilities.SPEAR_HITTING.set(ACExData.get(this, ACExAttachments.STAFF_SPEAR));
        ACExSugarStaffAbilities.SPEAR_FREEZE.set(ACExData.get(this, ACExAttachments.SPEAR_FREEZE));
    }

    @Inject(method = "onHit", at = @At("RETURN"))
    private void alexsCavesExemplified$onHit1(HitResult hitResult, CallbackInfo ci) {
        ACExSugarStaffAbilities.SPEAR_HITTING.set(false);
        ACExSugarStaffAbilities.SPEAR_FREEZE.set(1.0F);
    }

    @Inject(method = "onHit", at = @At("HEAD"))
    private void alexsCavesExemplified$onHit2(HitResult hitResult, CallbackInfo ci) {
        FrostmintSpearEntity frostmintSpear = (FrostmintSpearEntity)(Object)this;
        BlockPos blockPos = new BlockPos(frostmintSpear.getBlockX(), frostmintSpear.getBlockY() - 1, frostmintSpear.getBlockZ());

        Level level = this.level();


        if (!this.exploded && AlexsCavesExemplified.COMMON_CONFIG.AMPLIFIED_FROSTMINT_ENABLED.get() && freezeLiquids(level, blockPos)) {
            this.discard();
            ACExUtils.awardAdvancement(this.getOwner(), "frostmint_freeze", "freeze");
            explode();
        }
    }

    @Unique
    private static boolean freezeLiquids(Level level, BlockPos center) {
        boolean froze = false;
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-1, -1, -1), center.offset(1, 1, 1))) {
            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof LiquidBlock))
                continue;
            Block frozen = frozenResult(state.getFluidState().getType());
            if (frozen != null && level.setBlock(pos, frozen.defaultBlockState(), 3)) {
                level.scheduleTick(pos.immutable(), state.getBlock(), ACExUtils.seconds(0.1F));
                froze = true;
            }
        }
        return froze;
    }

    @Unique
    @Nullable
    private static Block frozenResult(Fluid fluid) {
        ResourceLocation id = ACExDataMaps.FROSTMINT_FREEZE.get(fluid);
        if (id == null && fluid instanceof FlowingFluid flowing) {
            id = ACExDataMaps.FROSTMINT_FREEZE.get(flowing.getSource());
        }
        return id == null ? null : BuiltInRegistries.BLOCK.getOptional(id).orElse(null);
    }

    private void explode() {
        FrostmintExplosion explosion = new FrostmintExplosion(level(), this.getOwner(), this.getX(), this.getY(0.5), this.getZ(), 2.0F, Explosion.BlockInteraction.KEEP, true);
        explosion.explode();
        explosion.finalizeExplosion(true);
    }

}
