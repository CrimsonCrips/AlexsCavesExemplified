package org.crimsoncrips.alexscavesexemplified.mixins.misc;




import net.minecraft.world.item.alchemy.PotionUtils;
import com.mojang.datafixers.util.Pair;
import org.crimsoncrips.alexscavesexemplified.backport.ACExItemData;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.SackOfSatingItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.food.FoodProperties;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Mixin(SackOfSatingItem.class)
public abstract class ACExSackOfSatingMixin extends Item {

    public ACExSackOfSatingMixin(Properties pProperties) {
        super(pProperties);
    }

    @Unique
    private static final int JELLYBEAN_HUNGER_COST = 15;
    @Unique
    private static final int JELLYBEAN_DURATION_DIVISOR = 10;

    @Inject(method = "overrideOtherStackedOnMe", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/item/SackOfSatingItem;setHunger(Lnet/minecraft/world/item/ItemStack;I)V", shift = At.Shift.AFTER))
    private void alexsCavesExemplified$overrideOtherStackedOnMe(ItemStack sackStack, ItemStack foodStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, CallbackInfoReturnable<Boolean> cir) {
        if (player.level().isClientSide || !AlexsCavesExemplified.COMMON_CONFIG.TUNED_SATING_ENABLED.get())
            return;
        FoodProperties food = foodStack.getFoodProperties(player);
        if (food == null || food.getEffects().isEmpty())
            return;
        List<MobEffectInstance> effects = new ArrayList<>();
        for (Pair<MobEffectInstance, Float> possible : food.getEffects()) {
            MobEffectInstance original = possible.getFirst();
            effects.add(new MobEffectInstance(original.getEffect(), original.getDuration() / JELLYBEAN_DURATION_DIVISOR, original.getAmplifier()));
        }
        int cost = player.isCreative() ? 0 : JELLYBEAN_HUNGER_COST;
        for (int i = 0; i < foodStack.getCount(); i++) {
            int hunger = SackOfSatingItem.getHunger(sackStack);
            if (hunger < cost)
                break;
            SackOfSatingItem.setHunger(sackStack, hunger - cost);
            ItemStack jellybean = new ItemStack(ACItemRegistry.JELLY_BEAN.get());
            PotionUtils.setCustomEffects(jellybean, effects);
            ACExItemData.update(jellybean, tag -> tag.putBoolean("Rainbow", true));
            if (!player.addItem(jellybean)) {
                player.drop(jellybean, false);
            }
        }
    }
}
