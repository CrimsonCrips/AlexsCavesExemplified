package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tesla_bulb;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.TeslaBulbBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.TeslaCharge;
import org.crimsoncrips.alexscavesexemplified.client.ACESoundRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TeslaBulbBlockEntity.class)
public abstract class ACETeslaBulbEntityMixin extends BlockEntity implements TeslaCharge {

    @Unique
    private int charge;

    public ACETeslaBulbEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private static void alexsCavesExemplified$tick1(Level level, BlockPos blockPos, BlockState state, TeslaBulbBlockEntity entity, CallbackInfo ci) {
        if(ACExemplifiedConfig.TESLA_COILED_ENABLED){
            level.playLocalSound(blockPos, ACESoundRegistry.TESLA_FIRE.get(), SoundSource.BLOCKS, 2, 1, false);
        }
    }


    public int getCharge(){
        return charge;
    }

    public void setCharge(int num){
        charge = num;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        if (packet != null && packet.getTag() != null) {
            charge = packet.getTag().getInt("TeslaCharge");
        }
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        charge = tag.getInt("TeslaCharge");
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("TeslaCharge", charge);
    }


    @Inject(method = "tick", at = @At(value = "HEAD"),remap = false)
    private static void alexsCavesExemplified$tick2(Level level, BlockPos blockPos, BlockState state, TeslaBulbBlockEntity entity, CallbackInfo ci) {
        if(ACExemplifiedConfig.SHOCKING_THERAPY_ENABLED){
            TeslaCharge tickAccesor = (TeslaCharge)(Object)entity;

            LivingEntity target = null;
            for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, new AABB(blockPos.offset(-5, -5, -5), blockPos.offset(5, 5, 5)))) {
                target = livingEntity;
            }

            if (target != null && !target.getType().is(ACTagRegistry.MAGNETIC_ENTITIES)) {

                if (target instanceof Player player && player.isCreative())
                    return;
                if (tickAccesor.getCharge() > 0) {
                    ParticleUtils.spawnParticlesAlongAxis(Direction.UP.getAxis(), level, blockPos, 0.125D, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
                }


                tickAccesor.setCharge(tickAccesor.getCharge() + 1);
                if (tickAccesor.getCharge() == 5 && ACExemplifiedConfig.TESLA_COILED_ENABLED){
                    level.playLocalSound(blockPos, ACESoundRegistry.TESLA_POWERUP.get(), SoundSource.AMBIENT, 2, 1, false);
                }
                if (tickAccesor.getCharge() > 30 && tickAccesor.getCharge() < 35){
                    Vec3 vec3 = findTargetPos(blockPos, target);
                    Vec3 from = Vec3.atCenterOf(blockPos);
                    if (!level.isClientSide) {
                        ((ServerLevel) level).sendParticles(ACParticleRegistry.TESLA_BULB_LIGHTNING.get(), from.x, from.y, from.z, 0, -vec3.x, -vec3.y + 1, -vec3.z, 1.3D);
                    }
                    if (!level.isClientSide) {
                        LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                        lightningBolt.setDamage(7);
                        target.thunderHit((ServerLevel) level, lightningBolt);
                        target.setRemainingFireTicks(30);
                    }
                }
                if (tickAccesor.getCharge() == 33 && ACExemplifiedConfig.TESLA_COILED_ENABLED){
                    target.playSound( ACESoundRegistry.TESLA_FIRE.get());
                }
                if (tickAccesor.getCharge() > 45) {
                    tickAccesor.setCharge(-300);
                }
            } else tickAccesor.setCharge(-20);



        }
    }

    @Unique
    private static Vec3 findTargetPos(BlockPos blockPos, LivingEntity livingEntity) {
        Vec3 center = Vec3.atCenterOf(blockPos);
        return new Vec3(center.x - livingEntity.getX(),center.y - livingEntity.getY(), center.z - livingEntity.getZ());
    }
}
