package org.crimsoncrips.alexscavesexemplified.server.entity;

import com.github.alexmodguy.alexscaves.server.block.blockentity.AmberMonolithBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.misc.ACExAmberMonolith;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;

public class CuriosityOfferingEntity extends Entity {

    private static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(CuriosityOfferingEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final int GROW_TICKS = ACExUtils.seconds(0.2F);
    private static final int RISE_TICKS = ACExUtils.seconds(0.5F);
    private static final int HOVER_TICKS = ACExUtils.seconds(1.0F);
    private static final int DIVE_TICKS = ACExUtils.seconds(0.25F);
    private static final int ARRIVE_TICKS = RISE_TICKS + HOVER_TICKS + DIVE_TICKS;
    private static final float HOVER_HEIGHT = 0.6F;
    private static final float BOB_HEIGHT = 0.05F;
    private static final float BOB_SPEED = 0.25F;
    private static final float DIVE_DEPTH = -0.5F;
    private static final float SPIN_SPEED = 6.0F;
    private static final float DIVE_SPIN = 540.0F;
    private static final float DIVE_SHRINK = 0.4F;

    public CuriosityOfferingEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public CuriosityOfferingEntity(Level level, BlockPos monolith, ItemStack stack) {
        this(ACExEntityRegistry.CURIOSITY_OFFERING.get(), level);
        this.setPos(monolith.getX() + 0.5D, monolith.getY() + 1.0D, monolith.getZ() + 0.5D);
        this.setItem(stack);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ITEM, ItemStack.EMPTY);
    }

    public ItemStack getItem() {
        return this.entityData.get(ITEM);
    }

    public void setItem(ItemStack stack) {
        this.entityData.set(ITEM, stack);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.tickCount >= ARRIVE_TICKS) {
            this.arrive();
        }
    }

    private void arrive() {
        if (this.level().getBlockEntity(this.getMonolithPos()) instanceof AmberMonolithBlockEntity monolith) {
            ACExAmberMonolith.cutTimer(monolith);
        } else {
            this.spawnAtLocation(this.getItem());
        }
        this.discard();
    }

    private BlockPos getMonolithPos() {
        return BlockPos.containing(this.getX(), this.getY() - 0.5D, this.getZ());
    }

    private static float getDive(float age) {
        return Mth.clamp((age - RISE_TICKS - HOVER_TICKS) / DIVE_TICKS, 0.0F, 1.0F);
    }

    public float getLift(float age) {
        if (age < RISE_TICKS) {
            float rise = 1.0F - age / RISE_TICKS;
            return HOVER_HEIGHT * (1.0F - rise * rise);
        }
        float hover = HOVER_HEIGHT + BOB_HEIGHT * Mth.sin((age - RISE_TICKS) * BOB_SPEED);
        float dive = getDive(age);
        return Mth.lerp(dive * dive, hover, DIVE_DEPTH);
    }

    public float getSpin(float age) {
        float dive = getDive(age);
        return age * SPIN_SPEED + dive * dive * DIVE_SPIN;
    }

    public float getScale(float age) {
        return Mth.clamp(age / GROW_TICKS, 0.0F, 1.0F) * (1.0F - getDive(age) * DIVE_SHRINK);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setItem(ItemStack.of(tag.getCompound("Item")));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (!this.getItem().isEmpty()) {
            tag.put("Item", this.getItem().save(new CompoundTag()));
        }
    }
}
