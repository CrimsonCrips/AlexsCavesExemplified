package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.FrostmintSpearEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.neoforged.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.CreateCompat;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static org.crimsoncrips.alexscavesexemplified.compat.BOPCompat.getBOPBlock;


@Mixin(FrostmintSpearEntity.class)
public abstract class ACExFrostmintSpearMixin extends AbstractArrow {


    @Shadow private boolean exploded;

    @Shadow @Nullable public abstract Entity getOwner();

    protected ACExFrostmintSpearMixin(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get() && this.getData(ACExAttachments.FROST_RADIANT);
    }


    @Inject(method = "onHit", at = @At("HEAD"))
    private void alexsCavesExemplified$onHit(HitResult hitResult, CallbackInfo ci) {
        ACExSugarStaffAbilities.SPEAR_HITTING.set(this.getData(ACExAttachments.STAFF_SPEAR));
    }

    @Inject(method = "onHit", at = @At("RETURN"))
    private void alexsCavesExemplified$onHit1(HitResult hitResult, CallbackInfo ci) {
        ACExSugarStaffAbilities.SPEAR_HITTING.set(false);
    }

    @Inject(method = "onHit", at = @At("HEAD"))
    private void hit(HitResult hitResult, CallbackInfo ci) {
        FrostmintSpearEntity frostmintSpear = (FrostmintSpearEntity)(Object)this;
        BlockPos blockPos = new BlockPos(frostmintSpear.getBlockX(), frostmintSpear.getBlockY() - 1, frostmintSpear.getBlockZ());

        Level level = this.level();


        if (!this.exploded && AlexsCavesExemplified.COMMON_CONFIG.AMPLIFIED_FROSTMINT_ENABLED.get()) {
            solidifyBlock(Blocks.WATER,Blocks.ICE,level,blockPos);
            solidifyBlock(Blocks.LAVA,Blocks.BASALT,level,blockPos);
            solidifyBlock(ACBlockRegistry.PURPLE_SODA.get(), ACBlockRegistry.SUGAR_GLASS.get(), level,blockPos);
            solidifyBlock(ACBlockRegistry.ACID.get(), ACBlockRegistry.RADROCK.get(), level,blockPos);

            if (ModList.get().isLoaded("create")) {
                CreateCompat.solidifyCreateLiquid(frostmintSpear,level,blockPos);
            }
            if (ModList.get().isLoaded("biomesoplenty")) {
                solidifyBlock(getBOPBlock(true),getBOPBlock(false), level, blockPos);
            }
        }




    }

    @Unique
    public void solidifyBlock(Block block, Block output, Level level, BlockPos blockPos){
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    BlockPos icePos = new BlockPos(blockPos.getX() + x, blockPos.getY() + y , blockPos.getZ() + z);
                    BlockState blockState = level.getBlockState(icePos);
                    if (blockState.is(block) && level.setBlock(icePos, output.defaultBlockState(), 3)) {
                        level.scheduleTick(icePos, blockState.getBlock(), ACExUtils.seconds(0.1F));
                        this.discard();
                        ACExUtils.awardAdvancement(this.getOwner(),"frostmint_freeze","freeze");
                        explode();
                    }

                }
            }
        }
    }


    private void explode() {
        FrostmintExplosion explosion = new FrostmintExplosion(level(), this.getOwner(), this.getX(), this.getY(0.5), this.getZ(), 2.0F, Explosion.BlockInteraction.KEEP, true);
        explosion.explode();
        explosion.finalizeExplosion(true);
    }

}
