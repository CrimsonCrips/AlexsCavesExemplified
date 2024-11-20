package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.DarkArrowEntity;
import com.github.alexmodguy.alexscaves.server.item.DreadbowItem;
import com.github.alexmodguy.alexscaves.server.item.ResistorShieldItem;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.DarknessIncarnateEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.alexmodguy.alexscaves.server.item.DreadbowItem.*;
import static com.github.alexmodguy.alexscaves.server.item.ResistorShieldItem.isScarlet;


@Mixin(ResistorShieldItem.class)
public abstract class ACEResistorShield extends ShieldItem {


    public ACEResistorShield(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "onUseTick", at = @At("TAIL"),remap = false)
    private void getMaxLoadTime(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci) {
        boolean scarlet = isScarlet(stack);
        AABB bashBox = living.getBoundingBox().inflate(5, 1, 5);
        for (ItemEntity entity : living.level().getEntitiesOfClass(ItemEntity.class, bashBox)) {
            if (entity.distanceTo(living) <= 4 && entity.getItem().is(ACTagRegistry.MAGNETIC_ITEMS)) {
                Vec3 arcVec = living.position().add(0, 0.65F * living.getBbHeight(), 0).subtract(entity.position());
                if (scarlet) {
                    if(arcVec.length() > living.getBbWidth()){
                        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.3F).add(arcVec.normalize().scale(0.7F)));
                    }
                } else {
                    if(arcVec.length() > living.getBbWidth()){
                        entity.setDeltaMovement(entity.getDeltaMovement().scale(-0.3F).add(arcVec.normalize().scale(-0.7F)));
                    }
                }
            }
        }
    }
}