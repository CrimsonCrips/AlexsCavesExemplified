package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.SeekingArrowEntity;
import com.github.alexmodguy.alexscaves.server.item.ResistorShieldItem;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.LaunchedSeeking;
import org.crimsoncrips.alexscavesexemplified.server.enchantment.ACExEnchants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.alexmodguy.alexscaves.server.item.ResistorShieldItem.isScarlet;


@Mixin(ResistorShieldItem.class)
public abstract class ACExResistorShieldMixin extends ShieldItem {


    public ACExResistorShieldMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "onUseTick", at = @At("TAIL"))
    private void alexsCavesExemplified$onUseTick(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.MAGNETICISM_ENABLED.get()) {
            int i = this.getUseDuration(stack) - timeUsing;
            boolean scarlet = isScarlet(stack);
            for (ItemEntity entity : living.level().getEntitiesOfClass(ItemEntity.class, living.getBoundingBox().inflate(5, 1, 5))) {
                if (entity.distanceTo(living) <= 4 && (i >= 10 && i % 5 == 0) && (entity.getItem().is(ACTagRegistry.MAGNETIC_ITEMS) && stack.getEnchantmentLevel(ACExEnchants.MAGNETICISM.get()) > 0)) {
                    Vec3 arcVec = living.position().add(0, 0.65F * living.getBbHeight(), 0).subtract(entity.position());
                    if (scarlet) {
                        if (arcVec.length() > living.getBbWidth()) {
                            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.3F).add(arcVec.normalize().scale(0.7F)));
                        }
                    } else {
                        if (arcVec.length() > living.getBbWidth()) {
                            entity.setDeltaMovement(entity.getDeltaMovement().scale(-0.3F).add(arcVec.normalize().scale(-0.7F)));
                        }
                    }
                }
            }

            if (living instanceof Player player && stack.getEnchantmentLevel(ACExEnchants.MAGNETICISM.get()) > 0) {
                int seekingAmount = 0;
                for (SeekingArrowEntity seekingArrowEntity : living.level().getEntitiesOfClass(SeekingArrowEntity.class, living.getBoundingBox().inflate(5, 2, 5))) {
                    LaunchedSeeking accesor = (LaunchedSeeking) seekingArrowEntity;
                    Entity entityLook = ACExUtils.getLookingAtEntity(player);
                    seekingArrowEntity.life = 0;
                    seekingArrowEntity.inGround = false;
                    seekingArrowEntity.setOwner(living);
                    seekingArrowEntity.pickup = SeekingArrowEntity.Pickup.CREATIVE_ONLY;
                    accesor.setStopSeeking(true);
                    if (!isScarlet(stack) && i == 10){
                        seekingArrowEntity.setPos(seekingArrowEntity.getPosition(1).add(0, 2, 0));
                        seekingArrowEntity.setDeltaMovement(0, 1, 0);
                        seekingAmount++;
                        if (entityLook != null && seekingArrowEntity.canHitEntity(entityLook)) {
                            if (living.getRandom().nextDouble() < 0.2) {
                                seekingArrowEntity.setCritArrow(true);
                            }
                            accesor.setLaunchedTargetID(entityLook.getId());
                        }
                    } else if (isScarlet(stack) && i >= 10) {
                        seekingAmount++;

                        int spinSpeed = 10;
                        seekingArrowEntity.move(MoverType.SELF, seekingArrowEntity.getDeltaMovement());
                        float f = Math.min(1.0F, seekingArrowEntity.tickCount / 30F);
                        Vec3 angle = new Vec3(0, 0, f * 3).yRot((float) -Math.toRadians(accesor.getSpinAngle()));
                        Vec3 encircle = living.position().add(angle).add(0,1,0);
                        Vec3 newDelta = encircle.subtract(seekingArrowEntity.position());
                        seekingArrowEntity.setDeltaMovement(newDelta.scale(0.05 * spinSpeed));
                        accesor.setSpinAngle(accesor.getSpinAngle() + spinSpeed);
                    }
                }
                if (seekingAmount > 0 && !player.isCreative() && i == 10){
                    int damageAmount = seekingAmount * 3;
                    if(!isScarlet(stack)){
                        stack.hurtAndBreak((damageAmount > (getMaxDamage(stack) - getDamage(stack))) ? getMaxDamage(stack) - getDamage(stack) - 1 : damageAmount, living, livingEntity -> {
                        });
                        player.getCooldowns().addCooldown(stack.getItem(), seekingAmount);
                    } else if (player.getRandom().nextDouble() < (0.001 * seekingAmount)) {
                        stack.hurtAndBreak((damageAmount > (getMaxDamage(stack) - getDamage(stack))) ? getMaxDamage(stack) - getDamage(stack) - 1 : damageAmount, living, livingEntity -> {
                        });
                    }
                }
            }

        }
    }
}