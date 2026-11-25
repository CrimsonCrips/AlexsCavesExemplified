package org.crimsoncrips.alexscavesexemplified.server.entity;

import org.crimsoncrips.alexscavesexemplified.mixins.backport.ACExGumWormAccessor;

import com.github.alexmodguy.alexscaves.server.entity.living.GumWormEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GumWormSegmentEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;

import javax.annotation.Nullable;

public class GumwirmSegmentEntity extends GumWormSegmentEntity {

    public GumwirmSegmentEntity(EntityType<? extends GumwirmSegmentEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public Vec3 getIdealPosition(@Nullable Entity parent) {
        Entity head = this.getHeadEntity();
        Entity front = parent == null ? this.getFrontEntity() : parent;
        if (front == null) {
            return this.position();
        }
        float backStretch = -2.5F;
        float sideSwing = 0.0F;
        if (head != null) {
            float headDelta = Mth.clamp((float) head.getDeltaMovement().length(), 0.0F, 1.0F);
            if (front == head) {
                backStretch -= head instanceof GumWormEntity gumWorm && gumWorm.isLeaping() ? 0.7F : 1.2F;
            }
            backStretch *= 1.0F - headDelta * 0.3F;
            sideSwing = (0.5F + this.getIndex() * 0.05F) * (float) Math.sin(head.tickCount * 0.2F - this.getIndex());
        }
        Vec3 offsetFromParent = new Vec3(sideSwing, 0.0, backStretch)
                .scale(GumwirmEntity.SCALE)
                .xRot(-((float) Math.toRadians(front.xRotO)))
                .yRot(-((float) Math.toRadians(front.yRotO)));
        return front.position().add(offsetFromParent);
    }

    @Override
    public void spawnDustParticles(boolean surface) {
        if (this.getHeadEntity() instanceof GumWormEntity gumWorm) {
            GumwirmEntity.spawnTinyDust(this, ((ACExGumWormAccessor) gumWorm).getSurfaceY());
        }
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return false;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ACExItemRegistry.GUMWIRM_SPAWN_EGG.get());
    }
}
