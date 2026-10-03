package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.item.SugarStaffItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.networking.ACExSugarStaffWheelPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(SugarStaffItem.class)
public abstract class ACExSugarStaffMixin extends Item {

    public ACExSugarStaffMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    private void alexsCavesExemplified$use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get())
            return;
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new ACExSugarStaffWheelPacket());
        }
        cir.setReturnValue(InteractionResultHolder.consume(player.getItemInHand(hand)));
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player player && ACExUtils.hasRadiantCharge(player)){
            pTarget.addEffect(new MobEffectInstance(MobEffects.HUNGER,ACExUtils.seconds(75.0F),9));
            if (!player.isCreative()) {
                ACExUtils.useRadiantCharge(player);
                player.getCooldowns().addCooldown(this, ACExUtils.seconds(20.0F));
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
