package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.item.MarineSnowItem;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(MarineSnowItem.class)
public class ACEMarineSnowMixin extends Item {

    public ACEMarineSnowMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/context/UseOnContext;getLevel()Lnet/minecraft/world/level/Level;",ordinal = 8))
    private void registerGoals(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get() && context.getLevel().getBiome(context.getClickedPos()).is(ACBiomeRegistry.ABYSSAL_CHASM)) {
            if(context.getPlayer() != null){
                ACEUtils.deepReputation(context.getPlayer(), 1);
            }
        }
    }

}
