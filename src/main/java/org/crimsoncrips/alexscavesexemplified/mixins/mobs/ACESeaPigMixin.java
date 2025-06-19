package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SeaPigEntity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACEEntityTagGenerator;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEDinosaurEggAttack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(SeaPigEntity.class)
public abstract class ACESeaPigMixin extends WaterAnimal implements Bucketable {


    protected ACESeaPigMixin(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/SeaPigEntity;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"))
    private void alexsCavesExemplified$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if(AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get()){
            ACEUtils.deepReputation(player,1);
        }
    }



}
