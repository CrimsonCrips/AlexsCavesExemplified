package org.crimsoncrips.alexscavesexemplified.mixins.blocks;


import net.minecraftforge.eventbus.api.Event;
import com.github.alexmodguy.alexscaves.server.block.blockentity.AmberMonolithBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.misc.ACExAmberMonolith;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AmberMonolithBlockEntity.class)
public abstract class ACExAmberMonolithBlockEntityMixin {

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/AmberMonolithBlockEntity;spawnMobs()Z"), remap = false)
    private static boolean alexsCavesExemplified$tick(AmberMonolithBlockEntity monolith, Operation<Boolean> original) {
        return ACExAmberMonolith.summon(monolith, () -> original.call(monolith));
    }

    @Inject(method = "tick", at = @At("TAIL"), remap = false)
    private static void alexsCavesExemplified$tick1(Level level, BlockPos pos, BlockState state, AmberMonolithBlockEntity monolith, CallbackInfo ci) {
        if (!level.isClientSide) {
            ACExAmberMonolith.tickRitual(monolith);
        }
    }

    @Inject(method = "generateSpawnData", at = @At("TAIL"), remap = false)
    private void alexsCavesExemplified$generateSpawnData(CallbackInfo ci) {
        ACExAmberMonolith.keepDarkSummon((AmberMonolithBlockEntity) (Object) this);
    }

    @WrapOperation(method = "spawnMobs", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;checkSpawnPosition(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z"), remap = false)
    private boolean alexsCavesExemplified$spawnMobs2(Mob mob, ServerLevelAccessor level, MobSpawnType spawnType, Operation<Boolean> original) {
        if (!ACExAmberMonolith.isDark((AmberMonolithBlockEntity) (Object) this))
            return original.call(mob, level, spawnType);
        MobSpawnEvent.PositionCheck event = new MobSpawnEvent.PositionCheck(mob, level, spawnType, null);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Event.Result.DEFAULT)
            return mob.checkSpawnObstruction(level);
        return event.getResult() == Event.Result.ALLOW;
    }

    @ModifyArg(method = "spawnMobs", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"), index = 0)
    private ParticleOptions alexsCavesExemplified$spawnMobs(ParticleOptions particle) {
        return ACExAmberMonolith.summonParticle((AmberMonolithBlockEntity) (Object) this, particle);
    }
}
