package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.EnigmaticEngineBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExBlockTagGenerator;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;


@Mixin(EnigmaticEngineBlockEntity.class)
public abstract class ACExEnigmaticEngineMixin extends BlockEntity {



    public ACExEnigmaticEngineMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/EnigmaticEngineBlockEntity;attemptAssembly()Z"),remap = false)
    private static void tick(Level level, BlockPos blockPos, BlockState state, EnigmaticEngineBlockEntity entity, CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.REMINEDING_ENABLED.get()){
            attemptMineAssembly(entity);
        }
    }








    @Unique
    private static void attemptMineAssembly(EnigmaticEngineBlockEntity entity) {
        Direction assembleIn = null;
        Level level = entity.getLevel();

        BlockPos blockPos = entity.getBlockPos();
        for (Direction direction : ACMath.HORIZONTAL_DIRECTIONS) {
            if (assemblingCheck(direction,blockPos,entity)) {
                assembleIn = direction;
                break;
            }
        }
        if (assembleIn != null) {
            for (BlockPos pos : BlockPos.betweenClosed(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ() - 1, blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1)) {
                if (level.getBlockState(pos).isCollisionShapeFullBlock(level,pos)){
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
            }
            Player owner = null;
            for (Player player : level.getEntitiesOfClass(Player.class, new AABB(blockPos.offset(-6, -6, -6), blockPos.offset(6, 6, 6)))) {
                owner = player;
                break;
            }
            if(!level.isClientSide){
                MineGuardianEntity mineGuardian = ACEntityRegistry.MINE_GUARDIAN.get().create(level);
                Vec3 vec31 = Vec3.atCenterOf(blockPos).add(0, -1, 0);
                mineGuardian.setYRot(assembleIn.toYRot());
                mineGuardian.setPos(vec31.x, vec31.y, vec31.z);
                if (owner != null) {
                    ACExUtils.awardAdvancement(owner,"mine_ownership","own");
                    ((MineGuardianXtra) mineGuardian).alexsCavesExemplified$setOwner(owner.getUUID().toString());
                }
                level.addFreshEntity(mineGuardian);
            }
        }
    }

    @Unique
    private static boolean assemblingCheck(Direction direction, BlockPos blockPos,EnigmaticEngineBlockEntity entity) {
        Level level = entity.getLevel();
        List<BlockPos> scrap = new ArrayList<>();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    BlockPos blockCheck = new BlockPos(blockPos.getX() + x,blockPos.getY() + y,blockPos.getZ() + z);
                    if (y == 0){
                        if (level.getBlockState(blockCheck).is(ACExBlockTagGenerator.REMINEDING_MATERIAL)) {
                            scrap.add(blockCheck);
                        }
                    } else if (((x == 0 && z == 0) || (x + z == 1 || x + z == -1))){
                        if (level.getBlockState(blockCheck).is(ACExBlockTagGenerator.REMINEDING_MATERIAL)) {
                            scrap.add(blockCheck);
                        }
                    } else if (level.getBlockState(blockCheck).isCollisionShapeFullBlock(level,blockCheck)) {
                        scrap.clear();
                    }

                }
            }
        }
        return scrap.size() == 17 && level.getBlockState(blockPos.relative(direction)).is(ACBlockRegistry.DEPTH_GLASS.get());
    }
}
