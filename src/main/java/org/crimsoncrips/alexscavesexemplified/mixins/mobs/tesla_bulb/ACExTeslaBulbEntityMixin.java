package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tesla_bulb;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.TeslaBulbBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.ACExSoundRegistry;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TeslaBulbBlockEntity.class)
public abstract class ACExTeslaBulbEntityMixin extends BlockEntity {

    public ACExTeslaBulbEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private static void alexsCavesExemplified$tick1(Level level, BlockPos blockPos, BlockState state, TeslaBulbBlockEntity entity, CallbackInfo ci) {
        if(AlexsCavesExemplified.COMMON_CONFIG.SHOCKING_THERAPY_ENABLED.get()){
            level.playLocalSound(blockPos, ACExSoundRegistry.TESLA_FIRE.get(), SoundSource.BLOCKS, 2, 1, false);
        }
    }


    @Inject(method = "tick", at = @At(value = "HEAD"),remap = false)
    private static void alexsCavesExemplified$tick2(Level level, BlockPos blockPos, BlockState state, TeslaBulbBlockEntity entity, CallbackInfo ci) {
        if(AlexsCavesExemplified.COMMON_CONFIG.SHOCKING_THERAPY_ENABLED.get()){
            MutableInt charge = entity.getData(ACExAttachments.TESLA_CHARGE);

            Player target = null;
            for (Player livingEntity : level.getEntitiesOfClass(Player.class, new AABB(Vec3.atLowerCornerOf(blockPos.offset(-5, -5, -5)), Vec3.atLowerCornerOf(blockPos.offset(5, 5, 5))))) {
                target = livingEntity;
            }

            if (target != null && !target.isCreative()) {

                if (charge.intValue() > 0) {
                    ParticleUtils.spawnParticlesAlongAxis(Direction.UP.getAxis(), level, blockPos, 0.125D, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
                }
                charge.increment();
                if (charge.intValue() == 5 && AlexsCavesExemplified.COMMON_CONFIG.SHOCKING_THERAPY_ENABLED.get()){
                    level.playLocalSound(blockPos, ACExSoundRegistry.TESLA_POWERUP.get(), SoundSource.AMBIENT, 2, 1, false);
                }
                if (charge.intValue() == 32){
                    Vec3 vec3 = findTargetPos(blockPos, target);
                    Vec3 from = Vec3.atCenterOf(blockPos);
                    if (!level.isClientSide) {
                        ((ServerLevel) level).sendParticles(ACParticleRegistry.TESLA_BULB_LIGHTNING.get(), from.x, from.y, from.z, 0, -vec3.x, -vec3.y + 1, -vec3.z, 1.3D);
                        LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                        lightningBolt.setDamage(7);
                        target.thunderHit((ServerLevel) level, lightningBolt);
                        target.setRemainingFireTicks(ACExUtils.seconds(1.5F));
                        ACExUtils.awardAdvancement(target,"tesla_shock","shock");
                    }
                    target.playSound( ACExSoundRegistry.TESLA_FIRE.get());
                    charge.setValue(-30);
                }
            } else {
                charge.setValue(-30);
            }



        }
    }

    @Unique
    private static Vec3 findTargetPos(BlockPos blockPos, LivingEntity livingEntity) {
        Vec3 center = Vec3.atCenterOf(blockPos);
        return new Vec3(center.x - livingEntity.getX(),center.y - livingEntity.getY(), center.z - livingEntity.getZ());
    }
}
