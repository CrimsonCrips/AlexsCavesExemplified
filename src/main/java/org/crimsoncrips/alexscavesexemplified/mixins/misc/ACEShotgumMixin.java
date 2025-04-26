package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;
import com.github.alexmodguy.alexscaves.server.item.ShotGumItem;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShotGumItem.class)
public abstract class ACEShotgumMixin extends Item {

    public ACEShotgumMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void alexsCavesExemplified$s(ItemStack stack, Level level, Entity entity, int i, boolean held, CallbackInfo ci) {

    }


    @ModifyArg(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private Entity alexsCavesExemplified$inventoryTick(Entity par1,@Local Level level,@Local Entity living) {
        if (AlexsCavesExemplified.COMMON_CONFIG.SHOTNUKE_ENABLED.get()) {
            NuclearBombEntity bomb = (NuclearBombEntity)((EntityType) ACEntityRegistry.NUCLEAR_BOMB.get()).create(level);
            if (bomb != null){
                bomb.setPos(living.getPosition(1));
                return bomb;
            }
        }
        return par1;
    }



}