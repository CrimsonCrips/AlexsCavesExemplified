package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.FrostmintSpearEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.simibubi.create.AllFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.CreateCompat;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static org.crimsoncrips.alexscavesexemplified.compat.BOPCompat.getBOPBlock;


@Mixin(FrostmintSpearEntity.class)
public abstract class ACEFrostmintSpear extends AbstractArrow {


    @Shadow private boolean exploded;

    @Shadow @Nullable public abstract Entity getOwner();

    protected ACEFrostmintSpear(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean ignoreExplosion() {
        return AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get() && this.getPersistentData().getBoolean("FrostRadiant");
    }


    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        Level level = this.level();
        if (this.isInFluidType()){
            if (AlexsCavesExemplified.COMMON_CONFIG.AMPLIFIED_FROSTMINT_ENABLED.get() && this.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get()) && !level.isClientSide) {
                FrostmintExplosion explosion = new FrostmintExplosion(level, this, this.getX() + 0.5F, this.getY() + 0.5F, this.getZ() + 0.5F, 4.0F, Explosion.BlockInteraction.DESTROY_WITH_DECAY, false);
                explosion.explode();
                explosion.finalizeExplosion(true);
                if (this.getOwner() instanceof Player) {
                    ACAdvancementTriggerRegistry.FROSTMINT_EXPLOSION.triggerForEntity(this.getOwner());
                }
                this.discard();
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get() && this.getPersistentData().getBoolean("FrostRadiant") && this.tickCount > 100){
            this.discard();
        }
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
            if (CreateCompat.presenceCheck(true)) {
                solidifyBlock(CreateCompat.createBlockRegistry(1), ACBlockRegistry.BLOCK_OF_CHOCOLATE.get(), level, blockPos);
            }
            if (CreateCompat.presenceCheck(false)) {
                solidifyBlock(CreateCompat.createBlockRegistry(2), Blocks.HONEY_BLOCK, level,blockPos);
            }

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
                        level.scheduleTick(icePos, blockState.getBlock(), 2);
                        this.discard();
                        ACEUtils.awardAdvancement(this.getOwner(),"frostmint_freeze","freeze");
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
