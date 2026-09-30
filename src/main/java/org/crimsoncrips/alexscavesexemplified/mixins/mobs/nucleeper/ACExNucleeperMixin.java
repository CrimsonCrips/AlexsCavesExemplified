package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nucleeper;

import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(NucleeperEntity.class)
public abstract class ACExNucleeperMixin extends Monster {

    protected ACExNucleeperMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Inject(method = "explode", at = @At("HEAD"),remap = false)
    private void alexsCavesExemplified$explode(CallbackInfo ci) {
        Level level = this.level();
        int amount = 0;
        for (NucleeperEntity nucleepers : level.getEntitiesOfClass(NucleeperEntity.class, this.getBoundingBox().inflate(13))) {
            if (AlexsCavesExemplified.COMMON_CONFIG.NUCLEAR_CHAIN_ENABLED.get() && !nucleepers.getData(ACExAttachments.DEFUSED)){
                amount++;
                NuclearExplosionEntity explosion = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(level);
                explosion.copyPosition(nucleepers);
                explosion.setSize(nucleepers.isCharged() ? 1.75F : 1F);
                if(!level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)){
                    explosion.setNoGriefing(true);
                }
                level.addFreshEntity(explosion);

            }
        }
        for (Player players : level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(50))) {
            if (amount >= 10){
                ACExUtils.awardAdvancement(players,"nucleeper_annhilation","nuke_chained");
            }
            if (amount != 0){
                ACExUtils.awardAdvancement(players,"chain_reaction","chain");
            }
        }
    }

    @WrapWithCondition(method = "mobInteract", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/NucleeperEntity;setTriggered(Z)V"))
    private boolean alexsCavesExemplified$tick(NucleeperEntity instance, boolean triggered) {
        return !this.getData(ACExAttachments.DEFUSED);
    }

}
