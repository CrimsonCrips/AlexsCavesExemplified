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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.server.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static com.github.alexmodguy.alexscaves.server.item.SackOfSatingItem.*;


@Mixin(ItemEntity.class)
public abstract class ACEItemEntity extends Entity {


    @Shadow public abstract ItemStack getItem();

    @Shadow @Nullable public abstract Entity getOwner();

    public ACEItemEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    int timeToCook = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        BlockState blockState = this.getBlockStateOn();
        Level level = this.level();
        ItemStack item = this.getItem();

        if (ACExemplifiedConfig.PURPLE_LEATHERED_ENABLED) {
            if (item.getItem() instanceof DyeableLeatherItem dyeableLeatherItem && this.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get())) {
                dyeableLeatherItem.setColor(item, 0Xb839e6);
            }
        }

        if (ACExemplifiedConfig.BREAKING_CANDY_ENABLED) {
            if (level.getBlockState(this.blockPosition()).is(Blocks.WATER_CAULDRON) && item.is(ACExexmplifiedTagRegistry.GELATINABLE) && level.getBlockState(this.blockPosition().below()).is(ACExexmplifiedTagRegistry.GELATIN_FIRE)) {
                for (ItemEntity itemEntity : this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(0.1))) {
                    ItemStack nearBone = itemEntity.getItem();
                    if (!nearBone.isEmpty() && timeToCook >= 200) {
                        ItemStack gelatinColor = switch (checkDye(nearBone)) {
                            case 1 -> ACItemRegistry.GELATIN_GREEN.get().asItem().getDefaultInstance();
                            case 2 -> ACItemRegistry.GELATIN_BLUE.get().asItem().getDefaultInstance();
                            case 3 -> ACItemRegistry.GELATIN_YELLOW.get().asItem().getDefaultInstance();
                            case 4 -> ACItemRegistry.GELATIN_PINK.get().asItem().getDefaultInstance();
                            default -> ACItemRegistry.GELATIN_RED.get().asItem().getDefaultInstance();
                        };

                        newGelatin(gelatinColor.getItem(),nearBone,item);

                        level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY() + 1, this.getZ(), 0,0,0);

                    } else timeToCook++;
                }
            }
        }

        if (ACExemplifiedConfig.AMPLIFIED_FROSTMINT_ENABLED){
            if (item.is(ACItemRegistry.FROSTMINT_SPEAR.get()) || item.is(ACBlockRegistry.FROSTMINT.get().asItem())) {
                if (blockState.getFluidState().getFluidType() == ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get() && !level.isClientSide) {
                    FrostmintExplosion explosion = new FrostmintExplosion(level, this, this.getX() + 0.5F, this.getY() + 0.5F, this.getZ() + 0.5F, 4.0F, Explosion.BlockInteraction.DESTROY_WITH_DECAY, false);
                    explosion.explode();
                    explosion.finalizeExplosion(true);

                    ACAdvancementTriggerRegistry.FROSTMINT_EXPLOSION.triggerForEntity(this.getOwner());

                    this.discard();
                }
            }
        }

        if (ACExemplifiedConfig.DROPPED_SATING_ENABLED){
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

                        nearItemStack.setCount(0);
                        this.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    }
                }
            }
        }
    }

    public boolean fireImmune() {
        return this.getItem().getItem().isFireResistant() || super.fireImmune() || this.getPersistentData().getBoolean("DraggedProtection");
    }

    public int checkDye(ItemStack possibleDye){
        int dyeDeterminer = 0;
        if (possibleDye.is(Items.RED_DYE)){
            dyeDeterminer = dyeDeterminer + 1;
        }
        if (possibleDye.is(Items.LIME_DYE)){
            dyeDeterminer = dyeDeterminer + 2;
        }
        if (possibleDye.is(Items.YELLOW_DYE)){
            dyeDeterminer = dyeDeterminer + 3;
        }
        if (possibleDye.is(Items.LIGHT_BLUE_DYE)){
            dyeDeterminer = dyeDeterminer + 4;
        }
        if (possibleDye.is(Items.PINK_DYE)){
            dyeDeterminer = dyeDeterminer + 5;
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
