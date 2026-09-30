package org.crimsoncrips.alexscavesexemplified.mixins.mobs.mine_guardian;

import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;


@Mixin(MineGuardianEntity.class)
public abstract class ACExMineGuardianMixin extends Monster {

    @Shadow public abstract boolean isExploding();

    @Shadow private float explodeProgress;

    protected ACExMineGuardianMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }



    @ModifyReturnValue(method = "isValidTarget", at = @At("RETURN"),remap = false)
    private boolean alexsMobsInteraction$isValidTarget(boolean original, @Local Entity entity) {
        if(AlexsCavesExemplified.COMMON_CONFIG.REMINEDING_ENABLED.get() && entity instanceof Player player){
            return canAttack(player) && !Objects.equals(player.getUUID().toString(), this.getData(ACExAttachments.MINE_GUARDIAN_OWNER));
        }
        return original;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/MineGuardianEntity;isExploding()Z",ordinal = 2))
    private boolean alexsCavesExemplified$tick(MineGuardianEntity instance, Operation<Boolean> original) {
        return original.call(instance) && this.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) < 1;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick1(CallbackInfo ci) {
        if(this.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) > 0){
            if (this.isExploding()) {
                if (this.explodeProgress >= 10.0F) {
                    this.remove(RemovalReason.KILLED);
                    this.nukeExplode();
                }

                this.setDeltaMovement(this.getDeltaMovement().multiply((double)0.3F, (double)1.0F, (double)0.3F));
            }
        }
    }

    private void nukeExplode() {
        NuclearExplosionEntity explosion = (NuclearExplosionEntity)((EntityType) ACEntityRegistry.NUCLEAR_EXPLOSION.get()).create(this.level());
        explosion.copyPosition(this);
        explosion.setSize((float) (AlexsCaves.COMMON_CONFIG.nukeExplosionSizeModifier.get().floatValue() * 0.8));
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            explosion.setNoGriefing(true);
        }

        this.level().addFreshEntity(explosion);
    }



}
