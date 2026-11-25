package org.crimsoncrips.alexscavesexemplified.server.entity;

import com.github.alexmodguy.alexscaves.server.block.blockentity.AmberMonolithBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExAmberMonolith;

public class DarkRitualEntity extends Entity implements UnderzealotSacrifice {

    private static final EntityDataAccessor<Long> START_TIME = SynchedEntityData.defineId(DarkRitualEntity.class, EntityDataSerializers.LONG);
    public static final double RITUAL_HEIGHT = 0.75D;

    public DarkRitualEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public DarkRitualEntity(Level level, BlockPos monolith, long startTime) {
        this(ACExEntityRegistry.DARK_RITUAL.get(), level);
        this.setPos(monolith.getX() + 0.5D, monolith.getY() + RITUAL_HEIGHT, monolith.getZ() + 0.5D);
        this.entityData.set(START_TIME, startTime);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(START_TIME, 0L);
    }

    public int getRitualAge() {
        return (int) Math.max(0L, this.level().getGameTime() - this.entityData.get(START_TIME));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            AlexsCavesExemplified.PROXY.tickDarkRitual(this);
        } else if (!(this.level().getBlockEntity(this.blockPosition()) instanceof AmberMonolithBlockEntity monolith) || !ACExAmberMonolith.isRitualActive(monolith)) {
            this.discard();
        }
    }

    @Override
    public void triggerSacrificeIn(int time) {
    }

    @Override
    public boolean isValidSacrifice(int distanceFromGround) {
        return false;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }
}
