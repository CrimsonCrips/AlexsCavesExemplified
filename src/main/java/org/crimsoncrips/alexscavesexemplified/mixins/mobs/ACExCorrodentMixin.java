package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.core.component.DataComponents;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.TargetsDroppedItems;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExItemTagGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;


@Mixin(CorrodentEntity.class)
public abstract class ACExCorrodentMixin extends Monster implements UnderzealotSacrifice, TargetsDroppedItems {


    private int sacrificeTime = 0;
    private boolean isBeingSacrificed = false;


    protected ACExCorrodentMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public void triggerSacrificeIn(int time) {
        isBeingSacrificed = true;
        sacrificeTime = time;
    }

    @Override
    public boolean isValidSacrifice(int distanceFromGround) {
        return AlexsCavesExemplified.COMMON_CONFIG.CORRODENT_CONVERSION_ENABLED.get() && !isLeashed();
    }

    @Override
    public boolean canBeLeashed() {
        return super.canBeLeashed() || (AlexsCavesExemplified.COMMON_CONFIG.DARK_OFFERING_ENABLED.get() && !isPassenger());
    }


    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (isBeingSacrificed && this.isPassenger() && !level().isClientSide && AlexsCavesExemplified.COMMON_CONFIG.CORRODENT_CONVERSION_ENABLED.get()) {
            sacrificeTime--;
            if (sacrificeTime < ACExUtils.seconds(0.5F)) {
                this.level().broadcastEntityEvent(this, (byte) 61);
            }
            if (sacrificeTime < 0) {
                if (this.isPassenger() && this.getVehicle() instanceof UnderzealotEntity underzealot) {
                    underzealot.postSacrifice(this);
                    underzealot.triggerIdleDigging();
                }
                this.stopRiding();
                UnderzealotEntity underzealot1 = this.convertTo(ACEntityRegistry.UNDERZEALOT.get(), true);
                if (underzealot1 != null) {
                    this.playSound(ACSoundRegistry.CORRODENT_HURT.get(), 8.0F, 1.0F);
                    net.neoforged.neoforge.event.EventHooks.onLivingConvert(this, underzealot1);
                    underzealot1.triggerIdleDigging();
                    underzealot1.stopRiding();
                }
            }
        }

    }


    @Override
    public boolean canTargetItem(ItemStack itemStack) {
        return  (itemStack.has(DataComponents.FOOD) || itemStack.is(ACExItemTagGenerator.KNAWING));
    }

    public void onGetItem(ItemEntity itemEntity) {
        Item item = itemEntity.getItem().getItem();
        if (itemEntity.getItem().has(DataComponents.FOOD)) {
            this.heal(5);
            List<FoodProperties.PossibleEffect> test = Objects.requireNonNull(itemEntity.getItem().getFoodProperties(this)).effects();
            if (!test.isEmpty()){
                for (int i = 0; i < test.size(); i++){
                    this.addEffect(new MobEffectInstance(test.get(i).effect()));
                }
            }
            itemEntity.getItem().shrink(1);
        } else {
            itemEntity.getItem().shrink(1);
            this.heal(1);
        }
    }

}
