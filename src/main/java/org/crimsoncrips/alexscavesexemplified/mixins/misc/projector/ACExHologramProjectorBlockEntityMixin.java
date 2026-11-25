package org.crimsoncrips.alexscavesexemplified.mixins.misc.projector;

import com.github.alexmodguy.alexscaves.server.block.blockentity.HologramProjectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HologramProjectorBlockEntity.class)
public abstract class ACExHologramProjectorBlockEntityMixin extends BlockEntity {

    @Unique
    private static final float PLAYER_WIDTH = 0.6F;
    @Unique
    private static final float PLAYER_HEIGHT = 1.8F;
    @Unique
    private static final float BEAM_TOP = 1.5F;

    public ACExHologramProjectorBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        HologramProjectorBlockEntity projector = (HologramProjectorBlockEntity) (Object) this;
        float scale = AlexsCavesExemplified.COMMON_CONFIG.SCALABLE_HOLOGRAM_ENABLED.get() ? Math.max(1, ACExData.get(projector, ACExAttachments.PROJECTION_SCALE)) : 1.0F;
        Entity holoEntity = projector.isPlayerRender() || this.level == null ? null : projector.getDisplayEntity(this.level);
        float width = holoEntity == null ? PLAYER_WIDTH : holoEntity.getBbWidth();
        float height = holoEntity == null ? PLAYER_HEIGHT : holoEntity.getBbHeight();
        double horizontal = Math.max(width, 1.0F) * scale;
        BlockPos pos = projector.getBlockPos();
        return new AABB(pos.getX() + 0.5D - horizontal, pos.getY() - 0.5D, pos.getZ() + 0.5D - horizontal, pos.getX() + 0.5D + horizontal, pos.getY() + 1.0D + (BEAM_TOP + height) * scale, pos.getZ() + 0.5D + horizontal);
    }
}
