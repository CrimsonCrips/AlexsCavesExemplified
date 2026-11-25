package org.crimsoncrips.alexscavesexemplified.mixins.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.backport.ACEnchantmentHelper;
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
import org.crimsoncrips.alexscavesexemplified.server.enchantment.ACExEnchants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.github.alexmodguy.alexscaves.server.item.ResistorShieldItem.isScarlet;


@Mixin(ResistorShieldItem.class)
public abstract class ACExResistorShieldMixin extends ShieldItem {


    public ACExResistorShieldMixin(Properties pProperties) {
        super(pProperties);
    }

    @Unique
    private static final int SLAM_TICKS = ACExUtils.seconds(0.5F);
    @Unique
    private static final int SLAM_INTERVAL = ACExUtils.seconds(0.25F);
    @Unique
    private static final int ARROW_DURABILITY_COST = 3;
    @Unique
    private static final double SCARLET_WEAR_CHANCE = 0.005D;
    @Unique
    private static final int SPIN_SPEED = 10;

    @Inject(method = "onUseTick", at = @At("TAIL"))
    private void alexsCavesExemplified$onUseTick(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.MAGNETICISM_ENABLED.get() || ACEnchantmentHelper.getEnchantmentLevelFromStack(ACExEnchants.MAGNETICISM, stack) <= 0)
            return;
        int i = this.getUseDuration(stack) - timeUsing;
        boolean scarlet = isScarlet(stack);
        boolean slam = i >= SLAM_TICKS && i % SLAM_INTERVAL == 0;
        if (slam) {
            for (ItemEntity entity : level.getEntitiesOfClass(ItemEntity.class, living.getBoundingBox().inflate(5, 1, 5))) {
                if (entity.distanceTo(living) <= 4 && entity.getItem().is(ACTagRegistry.MAGNETIC_ITEMS)) {
                    Vec3 arcVec = living.position().add(0, 0.65F * living.getBbHeight(), 0).subtract(entity.position());
                    if (arcVec.length() > living.getBbWidth()) {
                        float direction = scarlet ? 1.0F : -1.0F;
                        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.3F * direction).add(arcVec.normalize().scale(0.7F * direction)));
                    }
                }
            }
        }
        if (!(living instanceof Player player))
            return;
        List<SeekingArrowEntity> arrows = level.getEntitiesOfClass(SeekingArrowEntity.class, living.getBoundingBox().inflate(5, 2, 5));
        if (scarlet && i >= SLAM_TICKS) {
            for (SeekingArrowEntity arrow : arrows) {
                if (isHeld(arrow, living)) {
                    orbit(arrow, living);
                }
            }
        }
        if (!slam || level.isClientSide)
            return;
        Entity target = scarlet ? null : ACExUtils.getLookingAtEntity(player);
        int caught = 0;
        int orbiting = 0;
        for (SeekingArrowEntity arrow : arrows) {
            if (isHeld(arrow, living)) {
                orbiting++;
                continue;
            }
            catchArrow(arrow, living);
            caught++;
            if (!scarlet) {
                arrow.setPos(arrow.position().add(0, 2, 0));
                arrow.setDeltaMovement(0, 1, 0);
                if (target != null && arrow.canHitEntity(target)) {
                    if (living.getRandom().nextDouble() < 0.2) {
                        arrow.setCritArrow(true);
                    }
                    ACExAttachments.set(arrow, ACExAttachments.LAUNCHED_TARGET_ID, target.getId());
                }
            } else {
                orbiting++;
            }
        }
        if (player.isCreative())
            return;
        if (!scarlet && caught > 0) {
            wearShield(stack, living, caught);
            player.getCooldowns().addCooldown(stack.getItem(), caught);
        } else if (scarlet && orbiting > 0 && player.getRandom().nextDouble() < SCARLET_WEAR_CHANCE * orbiting) {
            wearShield(stack, living, orbiting);
        }
    }

    @Unique
    private static boolean isHeld(SeekingArrowEntity arrow, LivingEntity living) {
        return !arrow.inGround && ACExData.get(arrow, ACExAttachments.SHIELD_HOLDER) == living.getId();
    }

    @Unique
    private static void catchArrow(SeekingArrowEntity arrow, LivingEntity living) {
        arrow.life = 0;
        arrow.inGround = false;
        arrow.setOwner(living);
        arrow.pickup = SeekingArrowEntity.Pickup.CREATIVE_ONLY;
        ACExData.set(arrow, ACExAttachments.STOP_SEEKING, true);
        ACExAttachments.set(arrow, ACExAttachments.SHIELD_HOLDER, living.getId());
    }

    @Unique
    private static void orbit(SeekingArrowEntity arrow, LivingEntity living) {
        arrow.life = 0;
        arrow.move(MoverType.SELF, arrow.getDeltaMovement());
        float radius = Math.min(1.0F, arrow.tickCount / 30F) * 3;
        float angle = ACExData.get(arrow, ACExAttachments.SPIN_ANGLE);
        Vec3 encircle = living.position().add(new Vec3(0, 0, radius).yRot((float) -Math.toRadians(angle))).add(0, 1, 0);
        arrow.setDeltaMovement(encircle.subtract(arrow.position()).scale(0.05 * SPIN_SPEED));
        ACExAttachments.set(arrow, ACExAttachments.SPIN_ANGLE, angle + SPIN_SPEED);
    }

    @Unique
    private void wearShield(ItemStack stack, LivingEntity living, int arrows) {
        int remaining = getMaxDamage(stack) - getDamage(stack) - 1;
        int damage = Math.min(arrows * ARROW_DURABILITY_COST, remaining);
        if (damage > 0) {
            stack.hurtAndBreak(damage, living, breaker -> breaker.broadcastBreakEvent(living.getUsedItemHand()));
        }
    }
}
