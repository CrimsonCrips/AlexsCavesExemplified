package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;



import net.minecraft.world.item.DyeableLeatherItem;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.SackOfSatingItem;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;


import static com.github.alexmodguy.alexscaves.server.item.SackOfSatingItem.*;


@Mixin(ItemEntity.class)
public abstract class ACExItemEntityMixin extends Entity {


    @Shadow public abstract ItemStack getItem();

    @Shadow @Nullable public abstract Entity getOwner();

    public ACExItemEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @WrapOperation(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean alexsCavesExemplified$playerTouch(Inventory inventory, ItemStack itemstack, Operation<Boolean> original, @Local(argsOnly = true) Player player) {
        if (original.call(inventory, itemstack))
            return true;
        if (!AlexsCavesExemplified.COMMON_CONFIG.TUNED_SATING_ENABLED.get() || !itemstack.isEdible() || itemstack.is(ACTagRegistry.RESTRICTED_FROM_SACK_OF_SATING))
            return false;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack sack = inventory.getItem(i);
            if (sack.getItem() instanceof SackOfSatingItem) {
                if (itemstack.is(ACTagRegistry.EXPLODES_SACK_OF_SATING)) {
                    setExploding(sack, true);
                }
                setChewTimestamp(sack, player.level().getGameTime());
                setHunger(sack, getHunger(sack) + calculateWholeStackHungerValue(itemstack, player));
                ACExUtils.awardAdvancement(player, "full_consumption", "full_consumed");
                itemstack.setCount(0);
                this.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                return true;
            }
        }
        return false;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        Level level = this.level();
        ItemStack item = this.getItem();

        if (AlexsCavesExemplified.COMMON_CONFIG.PURPLE_LEATHERED_ENABLED.get() && item.getItem() instanceof DyeableLeatherItem dyeableLeatherItem && this.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get())) {
            if (!dyeableLeatherItem.hasCustomColor(item)){
                dyeableLeatherItem.setColor(item, 0Xb839e6);
                ACExUtils.awardAdvancement(this.getOwner(), "purple_coloring", "colored");
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.AMPLIFIED_FROSTMINT_ENABLED.get()){
            if (item.is(ACItemRegistry.FROSTMINT_SPEAR.get()) || item.is(ACBlockRegistry.FROSTMINT.get().asItem())) {
                if (this.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get()) && !level.isClientSide) {
                    FrostmintExplosion explosion = new FrostmintExplosion(level, this, this.getX() + 0.5F, this.getY() + 0.5F, this.getZ() + 0.5F, 4.0F, Explosion.BlockInteraction.DESTROY_WITH_DECAY, false);
                    explosion.explode();
                    explosion.finalizeExplosion(true);

                    ACAdvancementTriggerRegistry.FROSTMINT_EXPLOSION.triggerForEntity(this.getOwner());
                    ACExUtils.awardAdvancement(this.getOwner(),"frostmint_explode","explode");

                    this.discard();
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.TUNED_SATING_ENABLED.get()){
            ItemStack stack = this.getItem();
            if (stack.getItem() instanceof SackOfSatingItem){
                for (ItemEntity itemEntity : this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(1))) {
                    ItemStack nearItemStack = itemEntity.getItem();
                    if (!nearItemStack.isEmpty() && nearItemStack.isEdible() && !nearItemStack.is(ACTagRegistry.RESTRICTED_FROM_SACK_OF_SATING) && getOwner() instanceof Player player) {

                        if(nearItemStack.is(ACTagRegistry.EXPLODES_SACK_OF_SATING)){
                            setExploding(stack, true);
                        }

                        setHunger(stack, getHunger(stack) + calculateWholeStackHungerValue(nearItemStack, player));
                        ACExUtils.awardAdvancement(player,"dropped_consumption","consumed");
                        nearItemStack.setCount(0);
                        this.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    }
                }
            }
        }
    }

    public boolean fireImmune() {
        return super.fireImmune() || ACExData.get(this, ACExAttachments.DRAGGED_PROTECTION);
    }


}
