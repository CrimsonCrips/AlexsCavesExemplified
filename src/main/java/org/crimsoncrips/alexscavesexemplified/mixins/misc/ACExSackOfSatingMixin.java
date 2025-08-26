package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.SackOfSatingItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExItemTagGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Mixin(SackOfSatingItem.class)
public abstract class ACExSackOfSatingMixin extends Item {


    


    public ACExSackOfSatingMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/item/SackOfSatingItem;setHunger(Lnet/minecraft/world/item/ItemStack;I)V"))
    private void alexsCavesExemplified$overrideOtherStackedOnMe(ItemStack sackStack, ItemStack foodStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, CallbackInfoReturnable<Boolean> cir) {
        for (int stackAmount = 0; stackAmount < foodStack.getCount(); stackAmount++){
            List<Pair<MobEffectInstance, Float>> test = Objects.requireNonNull(foodStack.getFoodProperties(player)).getEffects();
            if (!test.isEmpty()) {
                Inventory inv = player.getInventory();
                Collection<MobEffectInstance> mobEffects = new ArrayList<>();
                for (int e = 0; e < test.size(); e++) {
                    MobEffectInstance original = test.get(e).getFirst();
                    MobEffectInstance adjustedMobEffect = new MobEffectInstance(original.getEffect(), original.getDuration() / 10, original.getAmplifier());
                    mobEffects.add(adjustedMobEffect);
                }
                ItemStack itemStack1 = PotionUtils.setCustomEffects(new ItemStack(ACItemRegistry.JELLY_BEAN.get()), mobEffects);
                for (int i = 0; i < inv.getContainerSize(); i++) {
                    ItemStack current = inv.getItem(i);
                    if ((current.is(ACExItemTagGenerator.GELATIN) && current.getCount() >= 10) || player.isCreative()) {
                        itemStack1.getTag().putBoolean("Rainbow", true);
                        if (!player.isCreative()) {
                            current.shrink(10);
                        }
                        if (!player.addItem(itemStack1)) {
                            player.drop(itemStack1, false);
                        }
                        break;
                    }
                }


            }
        }
    }



}
