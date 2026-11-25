package org.crimsoncrips.alexscavesexemplified.mixins.blocks;



import net.minecraft.world.InteractionHand;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.server.block.QuarryBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.QuarryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExQuarryUpgrades;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.menu.QuarryUpgradeMenu;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(QuarryBlock.class)
public abstract class ACExQuarryBlockMixin extends BaseEntityBlock {

    protected ACExQuarryBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.QUARRY_UPGRADES_ENABLED.get() || !(level.getBlockEntity(pos) instanceof QuarryBlockEntity quarry))
            return super.use(state, level, pos, player, hand, hitResult);
        if (!level.isClientSide) {
            ACExQuarryUpgrades upgrades = ACExData.get(quarry, ACExAttachments.QUARRY_UPGRADES);
            player.openMenu(new SimpleMenuProvider((containerId, inventory, menuPlayer) -> new QuarryUpgradeMenu(containerId, inventory, upgrades, ContainerLevelAccess.create(level, pos)), this.getName()));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof QuarryBlockEntity quarry && ACExData.has(quarry, ACExAttachments.QUARRY_UPGRADES)) {
            ACExQuarryUpgrades upgrades = ACExData.get(quarry, ACExAttachments.QUARRY_UPGRADES);
            for (int i = 0; i < upgrades.getSlots(); i++) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), upgrades.getStackInSlot(i).copy());
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
