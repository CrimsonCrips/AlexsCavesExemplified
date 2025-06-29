package org.crimsoncrips.alexscavesexemplified.mixins.mobs.base_deep_entity;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;


@Mixin(DeepOneBaseEntity.class)
public abstract class ACEBaseDeepEntityMixin extends PathfinderMob {

    @Shadow public abstract boolean isSummoned();

    @Shadow private UUID summonerUUID;

    protected ACEBaseDeepEntityMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.DEEP_WEAKENED_ENABLED.get()){
            if (!this.hasEffect(MobEffects.WATER_BREATHING) && (!this.level().getBiome(this.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM) || !this.isInWaterRainOrBubble())) {
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
                this.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
            }
        }
    }

    @ModifyArg(method = "hurt", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/DeepOneBaseEntity;addReputation(Ljava/util/UUID;I)V"))
    private UUID alexsCavesExemplified$hurt(UUID playerUUID) {
        if (true && this.isSummoned()){
            return this.summonerUUID;
        }
        return playerUUID;
    }
}
