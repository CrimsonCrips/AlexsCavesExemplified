package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.util.DeepOneReaction;
import com.github.alexmodguy.alexscaves.server.item.MagicConchItem;
import com.github.alexmodguy.alexscaves.server.level.storage.ACWorldData;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagicConchItem.class)
public abstract class ACExMagicConchMixin extends Item {

    @Unique
    private static final int CONCH_TOLL = 10;
    @Unique
    private static final int CALL_TIME = ACExUtils.seconds(1.25F);

    public ACExMagicConchMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "releaseUsing", at = @At("HEAD"), cancellable = true)
    private void alexsCavesExemplified$releaseUsing(ItemStack stack, Level level, LivingEntity living, int useTimeLeft, CallbackInfo ci) {
        if (level.isClientSide || !(living instanceof Player player) || !AlexsCavesExemplified.COMMON_CONFIG.CONCHIOUSNESS_ENABLED.get())
            return;
        if (this.getUseDuration(stack) - useTimeLeft <= CALL_TIME || !refused(player))
            return;
        ci.cancel();
        level.playSound(null, player, ACSoundRegistry.MAGIC_CONCH_CAST.get(), SoundSource.RECORDS, 16.0F, 0.5F);
        player.getCooldowns().removeCooldown(this);
        player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.magic_conch_refused"), true);
    }

    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"))
    private void alexsCavesExemplified$releaseUsing1(ItemStack stack, Level level, LivingEntity living, int useTimeLeft, CallbackInfo ci) {
        if (!level.isClientSide && living instanceof Player player && AlexsCavesExemplified.COMMON_CONFIG.CONCHIOUSNESS_ENABLED.get()) {
            ACExUtils.changeDeepReputation(player, -CONCH_TOLL);
        }
    }

    @Unique
    private static boolean refused(Player player) {
        ACWorldData worldData = ACWorldData.get(player.level());
        return worldData != null && DeepOneReaction.fromReputation(worldData.getDeepOneReputation(player.getUUID())) == DeepOneReaction.AGGRESSIVE;
    }
}
