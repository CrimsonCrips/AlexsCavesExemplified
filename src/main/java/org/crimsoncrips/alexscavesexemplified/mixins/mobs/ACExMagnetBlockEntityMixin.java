package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.blockentity.MagnetBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MagnetBlockEntity.class)
public class ACExMagnetBlockEntityMixin extends BlockEntity {


    public ACExMagnetBlockEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "pushEntity", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$pushEntity(Entity entity, CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.MAGNERIP_ENABLED.get() && entity instanceof Player player) {
            if (AlexsCavesExemplified.COMMON_CONFIG.HARDCORE_MAGNERIP_ENABLED.get()) {
                Inventory inv = player.getInventory();
                for (int i = 0; i < inv.getContainerSize(); i++) {
                    ItemStack current = inv.getItem(i);
                    if (current.is(ACTagRegistry.MAGNETIC_ITEMS)) {
                        player.playSound(ACSoundRegistry.MAGNETRON_HURT.get(),0.5f,1);
                        ACExUtils.dropMagneticItem(player,current);
                        inv.setItem(i, ItemStack.EMPTY);
                    }
                }
            } else {
                EquipmentSlot equipmentSlot =  player.getRandom().nextBoolean() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                ItemStack current = player.getItemBySlot(equipmentSlot);
                if (current.is(ACTagRegistry.MAGNETIC_ITEMS) && player.hasEffect(MobEffects.WEAKNESS)) {
                    player.playSound(ACSoundRegistry.MAGNETRON_HURT.get(),0.5f,1);
                    ACExUtils.dropMagneticItem(player,current);
                    player.setItemSlot(equipmentSlot, ItemStack.EMPTY);
                }
            }
        }
    }

}
