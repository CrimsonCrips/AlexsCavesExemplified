package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.PrimalMagmaBlock;
import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static com.github.alexmodguy.alexscaves.server.block.PrimalMagmaBlock.SINK_SHAPE;


@Mixin(PrimalMagmaBlock.class)
public abstract class ACExPrimalMagmaMixin extends Block {

    @Shadow @Final public static BooleanProperty ACTIVE;

    public ACExPrimalMagmaMixin(Properties pProperties) {
        super(pProperties);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        if (state.getValue(ACTIVE) && context instanceof EntityCollisionContext entityCollisionContext) {
            boolean aceThrough = entityCollisionContext.getEntity() instanceof AtlatitanEntity && AlexsCavesExemplified.COMMON_CONFIG.VOLCANIC_SACRIFICE_ENABLED.get();
            if (!(entityCollisionContext.getEntity() instanceof LuxtructosaurusEntity)) {
                return entityCollisionContext.getEntity() instanceof ItemEntity || aceThrough  ? Shapes.empty() : SINK_SHAPE;
            }
        }

        return super.getCollisionShape(state, level, blockPos, context);
    }

}
