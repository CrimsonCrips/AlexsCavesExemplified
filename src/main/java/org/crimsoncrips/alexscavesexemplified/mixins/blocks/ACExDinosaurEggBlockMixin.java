package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.DinosaurEggBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(DinosaurEggBlock.class)
public class ACExDinosaurEggBlockMixin {


    @Shadow @Final private RegistryObject<EntityType> births;

    @Inject(method = "playerDestroy", at = @At("TAIL"))
    private void playerBreak(Level worldIn, Player player, BlockPos pos, BlockState state, BlockEntity te, ItemStack stack, CallbackInfo ci) {
        if (!worldIn.isClientSide && AlexsCavesExemplified.COMMON_CONFIG.EGG_ANGER_ENABLED.get()) {
            AABB bb = (new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)).inflate(10.0F, 10.0F, 10.0F);
            if (player.isCreative())
                return;

            for(Mob living : worldIn.getEntitiesOfClass(Mob.class, bb, (livingx) -> livingx.isAlive() && livingx.getType() == this.births.get())) {
                if (!(living instanceof TamableAnimal) || !((TamableAnimal)living).isTame() || !((TamableAnimal)living).isOwnedBy(player)) {
                    living.setTarget(player);
                }
            }
        }
    }


}
