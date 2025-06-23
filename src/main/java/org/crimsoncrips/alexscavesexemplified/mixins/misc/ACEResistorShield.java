package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.SeekingArrowEntity;
import com.github.alexmodguy.alexscaves.server.item.ResistorShieldItem;
import com.github.alexmodguy.alexscaves.server.item.SeekingArrowItem;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.ACEReflectionUtil;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.server.enchantment.ACEEnchants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.alexmodguy.alexscaves.server.item.ResistorShieldItem.isScarlet;


@Mixin(ResistorShieldItem.class)
public class ACEResistorShield extends ShieldItem {


    public ACEResistorShield(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "onUseTick", at = @At("TAIL"))
    private void getMaxLoadTime(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.MAGNETICISM_ENABLED.get()) {
            int i = this.getUseDuration(stack) - timeUsing;
            boolean scarlet = isScarlet(stack);
            for (ItemEntity entity : living.level().getEntitiesOfClass(ItemEntity.class, living.getBoundingBox().inflate(5, 1, 5))) {
                if (entity.distanceTo(living) <= 4 && (i >= 10 && i % 5 == 0) && (entity.getItem().is(ACTagRegistry.MAGNETIC_ITEMS) && stack.getEnchantmentLevel(ACEEnchants.MAGNETICISM.get()) > 0)) {
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

            for (SeekingArrowEntity seekingArrowEntity : living.level().getEntitiesOfClass(SeekingArrowEntity.class, living.getBoundingBox().inflate(5, 8, 5))) {
                if (living instanceof Player player && seekingArrowEntity.getOwner() != living && stack.getEnchantmentLevel(ACEEnchants.MAGNETICISM.get()) > 0) {
                    Entity entityLook = ACEUtils.getLookingAtEntity(player);
                    if (entityLook == null){
                        return;
                    }

                    System.out.println("test");

                    seekingArrowEntity.inGround = false;
                    seekingArrowEntity.setOwner(living);
                    seekingArrowEntity.setDeltaMovement(0,1,0);
                    EntityDataAccessor<Integer> arc_id = (EntityDataAccessor<Integer>) ACEReflectionUtil.getField(seekingArrowEntity, "ARC_TOWARDS_ENTITY_ID");
                    seekingArrowEntity.getEntityData().set(arc_id,entityLook.getId());

                }
            }
        }
    }
}