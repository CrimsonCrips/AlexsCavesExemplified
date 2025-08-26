package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.NuclearBombBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(NuclearBombBlock.class)
public abstract class ACExNuclearBombMixin extends Block {


    public ACExNuclearBombMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return (AlexsCavesExemplified.COMMON_CONFIG.NUCLEAR_PISTONATION_ENABLED.get()) ? PushReaction.NORMAL : PushReaction.DESTROY;
    }
}
