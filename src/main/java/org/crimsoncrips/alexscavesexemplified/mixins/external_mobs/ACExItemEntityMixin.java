package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.SackOfSatingItem;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExBlockTagGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExItemTagGenerator;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import java.util.UUID;

import static com.github.alexmodguy.alexscaves.server.item.SackOfSatingItem.*;


@Mixin(ItemEntity.class)
public abstract class ACExItemEntityMixin extends Entity {


    @Shadow public abstract ItemStack getItem();

    @Shadow @Nullable public abstract Entity getOwner();

    @Shadow private int pickupDelay;

    @Shadow @Nullable private UUID target;

    public ACExItemEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    int timeToCook = 0;

    @Inject(method = "playerTouch", at = @At("HEAD"))
    private void playerTouch(Player pEntity, CallbackInfo ci){
        ItemEntity itemEntity = (ItemEntity)(Object)this;
        if (!this.level().isClientSide && AlexsCavesExemplified.COMMON_CONFIG.TUNED_SATING_ENABLED.get()) {
            ItemStack itemstack = itemEntity.getItem();
            int hook = net.minecraftforge.event.ForgeEventFactory.onItemPickup(itemEntity, pEntity);
            if (this.pickupDelay <= 0 && (hook == 0 || itemstack.getCount() <= 0) && itemstack.isEdible() && !pEntity.getInventory().add(itemstack)) {
                Inventory inv = pEntity.getInventory();
                for (int i = 0; i < inv.getContainerSize(); i++) {
                    ItemStack current = inv.getItem(i);
                    if (current.getItem() instanceof SackOfSatingItem) {
                        if(itemstack.is(ACTagRegistry.EXPLODES_SACK_OF_SATING)){
                            setExploding(current, true);
                        }

                        int foodAmount;

                        FoodProperties foodProperties = itemstack.getFoodProperties(pEntity);
                        if(foodProperties != null && !itemstack.is(ACTagRegistry.RESTRICTED_FROM_SACK_OF_SATING)){
                            foodAmount = foodProperties.getNutrition() * itemstack.getCount();
                        } else foodAmount = 0;

                        setChewTimestamp(current, pEntity.level().getGameTime());
                        setHunger(current, getHunger(current) + foodAmount);
                        ACExUtils.awardAdvancement(pEntity,"full_consumption","full_consumed");
                        itemstack.setCount(0);
                        this.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    }
                }
            }


        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        BlockState blockState = this.getBlockStateOn();
        Level level = this.level();
        ItemStack item = this.getItem();

        if (AlexsCavesExemplified.COMMON_CONFIG.PURPLE_LEATHERED_ENABLED.get() && item.getItem() instanceof DyeableLeatherItem dyeableLeatherItem && this.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get())) {
            if (!dyeableLeatherItem.hasCustomColor(item)){
                dyeableLeatherItem.setColor(item, 0Xb839e6);
                ACExUtils.awardAdvancement(this.getOwner(), "purple_coloring", "colored");
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.BREAKING_CANDY_ENABLED.get()) {
            if (level.getBlockState(this.blockPosition()).is(Blocks.WATER_CAULDRON) && item.is(ACExItemTagGenerator.GELATINABLE) && level.getBlockState(this.blockPosition().below()).is(ACExBlockTagGenerator.GELATIN_FIRE)) {
                for (ItemEntity itemEntity : this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(0.2))) {
                    ItemStack nearBone = itemEntity.getItem();
                    if (nearBone.getItem() instanceof DyeItem && timeToCook >= 50) {
                        ItemStack gelatinColor = switch (checkDye(nearBone)) {
                            case 2 -> ACItemRegistry.GELATIN_PINK.get().asItem().getDefaultInstance();
                            case 3 -> ACItemRegistry.GELATIN_GREEN.get().asItem().getDefaultInstance();
                            case 4 -> ACItemRegistry.GELATIN_YELLOW.get().asItem().getDefaultInstance();
                            case 5 -> ACItemRegistry.GELATIN_BLUE.get().asItem().getDefaultInstance();
                            default -> ACItemRegistry.GELATIN_RED.get().asItem().getDefaultInstance();
                        };
                        this.getItem().shrink(1);

                        newGelatin(gelatinColor.getItem(),nearBone,item);

                        level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY() + 1, this.getZ(), 0,0,0);

                        ACExUtils.awardAdvancement(this.getOwner(),"breaking_candy","bake");
                    } else timeToCook++;
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.AMPLIFIED_FROSTMINT_ENABLED.get()){
            if (item.is(ACItemRegistry.FROSTMINT_SPEAR.get()) || item.is(ACBlockRegistry.FROSTMINT.get().asItem())) {
                if (blockState.getFluidState().getFluidType() == ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get() && !level.isClientSide) {
                    FrostmintExplosion explosion = new FrostmintExplosion(level, this, this.getX() + 0.5F, this.getY() + 0.5F, this.getZ() + 0.5F, 4.0F, Explosion.BlockInteraction.DESTROY_WITH_DECAY, false);
                    explosion.explode();
                    explosion.finalizeExplosion(true);

                    ACAdvancementTriggerRegistry.FROSTMINT_EXPLOSION.triggerForEntity(this.getOwner());

                    item.shrink(1);
                }
                ACExUtils.awardAdvancement(this.getOwner(),"frostmint_explode","explode");
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.TUNED_SATING_ENABLED.get()){
            ItemStack stack = this.getItem();
            if (stack.getItem() instanceof SackOfSatingItem){
                for (ItemEntity itemEntity : this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(1))) {
                    ItemStack nearItemStack = itemEntity.getItem();
                    if (!nearItemStack.isEmpty() && nearItemStack.isEdible() && getOwner() instanceof Player player) {

                        if(nearItemStack.is(ACTagRegistry.EXPLODES_SACK_OF_SATING)){
                            setExploding(stack, true);
                        }

                        int foodAmount;

                        FoodProperties foodProperties = nearItemStack.getFoodProperties(player);
                        if(foodProperties != null && !nearItemStack.is(ACTagRegistry.RESTRICTED_FROM_SACK_OF_SATING)){
                            foodAmount = foodProperties.getNutrition() * nearItemStack.getCount();
                        } else foodAmount = 0;
                        setHunger(stack, getHunger(stack) + foodAmount);
                        ACExUtils.awardAdvancement(player,"dropped_consumption","consumed");
                        nearItemStack.setCount(0);
                        this.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    }
                }
            }
        }
    }

    public boolean fireImmune() {
        return super.fireImmune() || this.getPersistentData().getBoolean("DraggedProtection");
    }

    public int checkDye(ItemStack possibleDye){
        int dyeDeterminer = 0;
        if (possibleDye.is(Items.LIME_DYE) || possibleDye.is(Items.GREEN_DYE)){
            dyeDeterminer = dyeDeterminer + 3;
        }
        if (possibleDye.is(Items.YELLOW_DYE) || possibleDye.is(Items.ORANGE_DYE)){
            dyeDeterminer = dyeDeterminer + 4;
        }
        if (possibleDye.is(Items.BLUE_DYE) || possibleDye.is(Items.LIGHT_BLUE_DYE) || possibleDye.is(Items.CYAN_DYE)){
            dyeDeterminer = dyeDeterminer + 5;
        }
        if (possibleDye.is(Items.PINK_DYE) || possibleDye.is(Items.PURPLE_DYE) || possibleDye.is(Items.MAGENTA_DYE)){
            dyeDeterminer = dyeDeterminer + 2;
        }


        return dyeDeterminer;


    }

    public void newGelatin(Item gelatinColor,ItemStack bone, ItemStack item){
        if (random.nextDouble() < 0.08){
            bone.shrink(1);
        }
        if (random.nextDouble() < 0.03){
            item.shrink(1);
        }
        timeToCook = 0;
        ItemEntity gelatin = new ItemEntity(this.level(),this.getX(),this.getY() + 0.5,this.getZ(), gelatinColor.getDefaultInstance());
        this.level().addFreshEntity(gelatin);
        gelatin.setDeltaMovement(random.nextInt(-1,2) * 0.07, 0.4, random.nextInt(-1,2) * 0.07);
    }


}
