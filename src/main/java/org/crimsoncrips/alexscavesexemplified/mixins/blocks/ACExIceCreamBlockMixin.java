package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.IceCreamBlock;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(IceCreamBlock.class)
public class ACExIceCreamBlockMixin extends Block {


    public ACExIceCreamBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        attemptIcecreamSpawn(pLevel,pPos);
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    public void attemptIcecreamSpawn(Level level, BlockPos blockPos){
        if (level.getBlockState(blockPos).getBlock() instanceof IceCreamBlock && AlexsCavesExemplified.COMMON_CONFIG.ICE_CREAM_CONE_ENABLED.get()){
            Block topIcecream = level.getBlockState(blockPos).getBlock();

            if (level.getBlockState(blockPos.below(1)).is(topIcecream))
                return;
            Block middleIcecream = level.getBlockState(blockPos.below()).getBlock();

            if (level.getBlockState(blockPos.below(2)).is(topIcecream))
                return;
            if (level.getBlockState(blockPos.below(2)).is(middleIcecream))
                return;

            if (level.getBlockState(blockPos.below(3)).is(ACBlockRegistry.WAFER_COOKIE_BLOCK.get()) && level.getBlockState(blockPos.below(4)).is(ACBlockRegistry.WAFER_COOKIE_WALL.get())){
                for (int i = 0;i < 5;i++){
                    level.setBlock(blockPos.below(i), Blocks.AIR.defaultBlockState(),3);
                }
                ItemEntity itementity = new ItemEntity(level, blockPos.getX(), blockPos.getY() - 4, blockPos.getZ(), ACExItemRegistry.ICE_CREAM_CONE.get().getDefaultInstance());
                itementity.setDefaultPickUpDelay();

                level.addFreshEntity(itementity);
                for (Player player : level.getEntitiesOfClass(Player.class, new AABB(blockPos.offset(-12, -12, -12), blockPos.offset(12, 12, 12)))) {
                    ACExUtils.awardAdvancement(player,"ice_cream","made");
                }
                level.playLocalSound(blockPos, ACSoundRegistry.FROSTMINT_SPEAR_HIT.get(), SoundSource.AMBIENT, 2, 1, false);

            }

        }
    }
}
