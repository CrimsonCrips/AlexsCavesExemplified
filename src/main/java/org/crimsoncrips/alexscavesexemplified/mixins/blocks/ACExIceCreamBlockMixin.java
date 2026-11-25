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
import org.crimsoncrips.alexscavesexemplified.server.item.IceCreamConeItem;
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
        if (level.isClientSide || !AlexsCavesExemplified.COMMON_CONFIG.ICE_CREAM_CONE_ENABLED.get())
            return;
        IceCreamConeItem.Flavor top = IceCreamConeItem.Flavor.of(level.getBlockState(blockPos).getBlock());
        IceCreamConeItem.Flavor middle = IceCreamConeItem.Flavor.of(level.getBlockState(blockPos.below()).getBlock());
        IceCreamConeItem.Flavor bottom = IceCreamConeItem.Flavor.of(level.getBlockState(blockPos.below(2)).getBlock());
        if (top == null || middle == null || bottom == null)
            return;
        if (!level.getBlockState(blockPos.below(3)).is(ACBlockRegistry.WAFER_COOKIE_BLOCK.get()) || !level.getBlockState(blockPos.below(4)).is(ACBlockRegistry.WAFER_COOKIE_WALL.get()))
            return;
        for (int i = 0; i < 5; i++) {
            level.setBlock(blockPos.below(i), Blocks.AIR.defaultBlockState(), 3);
        }
        IceCreamConeItem.Formation formation = new IceCreamConeItem.Formation(top, middle, bottom);
        ItemEntity itementity = new ItemEntity(level, blockPos.getX() + 0.5D, blockPos.getY() - 4, blockPos.getZ() + 0.5D, IceCreamConeItem.withFormation(ACExItemRegistry.ICE_CREAM_CONE.get().getDefaultInstance(), formation));
        itementity.setDefaultPickUpDelay();
        level.addFreshEntity(itementity);
        for (Player player : level.getEntitiesOfClass(Player.class, new AABB(blockPos).inflate(12))) {
            ACExUtils.awardAdvancement(player,"ice_cream","made");
        }
        level.playSound(null, blockPos, ACSoundRegistry.FROSTMINT_SPEAR_HIT.get(), SoundSource.AMBIENT, 2, 1);
    }
}
