package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.blockentity.BeholderBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.ACExReflectionUtil;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(BeholderBlockEntity.class)
public class ACExBeholderMixin extends BlockEntity {


    @Shadow private float eyeXRot;

    public ACExBeholderMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/BeholderBlockEntity;getUsingEntity()Lnet/minecraft/world/entity/Entity;"), cancellable = true,remap = false)
    private static void alexsCavesExemplified$tick(Level level, BlockPos blockPos, BlockState state, BeholderBlockEntity entity, CallbackInfo ci) {

        if (AlexsCavesExemplified.COMMON_CONFIG.BEHOLDENT_STALKING_ENABLED.get()) {
            ci.cancel();

            Vec3 center = Vec3.atCenterOf(blockPos);
            Player looking = null;
            for (Player player : level.getEntitiesOfClass(Player.class, new AABB(Vec3.atLowerCornerOf(blockPos.offset(-5, -5, -5)), Vec3.atLowerCornerOf(blockPos.offset(5, 5, 5))))) {
                if (looking == null || player.distanceToSqr(center) < looking.distanceToSqr(center)) {
                    looking = player;
                }
            }
            if(entity.getUsingEntity() == null) {
                if (looking != null) {
                    Vec3 toEye = looking.getEyePosition().subtract(center);
                    float pitch = (float) -Math.toDegrees(Math.atan2(toEye.y, toEye.horizontalDistance()));
                    ACExReflectionUtil.setField(entity, "eyeYRot", (float) (Math.atan2((looking.getZ() - (blockPos.getZ() + 0.5)), (looking.getX() - (blockPos.getX() + 0.5))) * 180 / Math.PI) + 270);
                    ACExReflectionUtil.setField(entity, "eyeXRot", Mth.approach((float) ACExReflectionUtil.getField(entity, "eyeXRot"), pitch, 10F));
                } else {
                    ACExReflectionUtil.setField(entity, "eyeXRot", Mth.approach((float) ACExReflectionUtil.getField(entity, "eyeXRot"), 0, 10F));
                    ACExReflectionUtil.setField(entity, "eyeYRot", (float) ACExReflectionUtil.getField(entity, "eyeYRot") + 1);
                }
            }else{
                ACExReflectionUtil.setField(entity, "eyeXRot", Mth.approach((float) ACExReflectionUtil.getField(entity, "eyeXRot"), entity.getUsingEntity().getXRot(), 10F));
                ACExReflectionUtil.setField(entity, "eyeYRot", Mth.approach((float) ACExReflectionUtil.getField(entity, "eyeYRot"), entity.getUsingEntity().getYRot(), 10F));
            }

            if(entity.soundCooldown-- <= 0){
                entity.soundCooldown = level.random.nextInt(100) + 100;
                Vec3 vec3 = entity.getBlockPos().getCenter();
                level.playSound((Player)null, vec3.x, vec3.y, vec3.z, (int) ACExReflectionUtil.getField(entity, "currentlyUsingEntityId") == -1 ? ACSoundRegistry.BEHOLDER_IDLE.get() : ACSoundRegistry.BEHOLDER_VIEW_IDLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            if(!level.isClientSide){
                if((int) ACExReflectionUtil.getField(entity, "prevUsingEntityId") != (int) ACExReflectionUtil.getField(entity, "currentlyUsingEntityId")){
                    level.sendBlockUpdated(entity.getBlockPos(), entity.getBlockState(), entity.getBlockState(), 2);
                    ACExReflectionUtil.setField(entity, "prevUsingEntityId", ACExReflectionUtil.getField(entity, "currentlyUsingEntityId"));
                }
            }
        }
    }



}
