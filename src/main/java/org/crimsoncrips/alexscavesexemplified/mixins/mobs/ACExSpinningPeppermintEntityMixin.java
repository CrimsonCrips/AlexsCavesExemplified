package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.item.SpinningPeppermintEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(SpinningPeppermintEntity.class)
public abstract class ACExSpinningPeppermintEntityMixin extends Entity {

    @Shadow public abstract boolean isStraight();

    @Shadow protected abstract void hurtEntities();

    @Shadow public abstract void setSpinAroundPosition(@Nullable Vec3 vec3);

    @Shadow private float spinAngle;

    @Shadow public abstract float getSpinSpeed();

    @Shadow public abstract float getSpinRadius();

    @Shadow public abstract float getStartAngle();

    @Shadow public abstract void setSpinRadius(float spinRadius);

    @Shadow public abstract int getSeekingEntityId();

    @Shadow @javax.annotation.Nullable public abstract LivingEntity getOwner();

    @Shadow private int lSteps;

    @Shadow private double ly;

    @Shadow private double lx;

    @Shadow private double lz;

    @Shadow private double lyr;

    @Shadow private double lxr;

    @Shadow public abstract void setSpinSpeed(float spinSpeed);

    @Shadow public abstract @Nullable Vec3 getSpinAroundPosition();

    @Shadow private int despawnsIn;

    @Shadow private int prevDespawnsIn;

    @Shadow public abstract int getLifespan();

    @Shadow @javax.annotation.Nullable private LivingEntity owner;

    public ACExSpinningPeppermintEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void tick() {
        super.tick();
        if (this.despawnsIn == -1) {
            this.despawnsIn = this.getLifespan();
        }

        this.prevDespawnsIn = this.despawnsIn;
        if (this.despawnsIn > 0) {
            --this.despawnsIn;
        } else if (!this.level().isClientSide) {
            this.discard();
        }

        Vec3 encirclePos = this.getSpinAroundPosition();
        if (this.level().isClientSide) {
            if (this.lSteps > 0) {
                double d5 = this.getX() + (this.lx - this.getX()) / this.lSteps;
                double d6 = this.getY() + (this.ly - this.getY()) / this.lSteps;
                double d7 = this.getZ() + (this.lz - this.getZ()) / this.lSteps;
                this.setYRot(Mth.wrapDegrees((float)this.lyr));
                this.setXRot(this.getXRot() + (float)(this.lxr - this.getXRot()) / (float)this.lSteps);
                --this.lSteps;
                this.setPos(d5, d6, d7);
            } else {
                this.reapplyPosition();
            }
        } else {
            this.reapplyPosition();
            this.setRot(this.getYRot(), this.getXRot());
            Entity owner = this.getOwner();

            if (owner instanceof Player player && this.getPersistentData().getBoolean("PepperRadiant") && AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get()){
                Entity seeking = this.getSeekingEntityId() == -1 ? null : this.level().getEntity(this.getSeekingEntityId());
                Vec3 playerPos = player.position().add(0.0F, (player.getBbHeight() * 0.45F), 0.0F);

                if (seeking != null) {
                    Vec3 targetPos = seeking.position().add(0.0F, (seeking.getBbHeight() * 0.45F), 0.0F);
                    this.setSpinAroundPosition(targetPos);
                    if (this.tickCount == 50){
                        this.setSpinRadius(getSpinRadius() / 3);
                    }
                } else {
                    this.setSpinAroundPosition(playerPos);
                }

            } else {
                if (owner instanceof Mob) {
                    Mob mob = (Mob)owner;
                    LivingEntity target = mob.getTarget();
                    if (target != null && encirclePos != null) {
                        Vec3 add = target.getEyePosition().subtract(encirclePos);
                        if (add.length() > 1.0F) {
                            add = add.normalize();
                        }

                        this.setSpinAroundPosition(encirclePos.add(add.scale(0.05F)));
                    }
                } else if (owner instanceof Player) {
                    Player player = (Player)owner;
                    Vec3 playerPos = player.position().add(0.0F, (player.getBbHeight() * 0.45F), 0.0F);
                    Entity seeking = this.getSeekingEntityId() == -1 ? null : this.level().getEntity(this.getSeekingEntityId());
                    if (seeking != null) {
                        Vec3 add = seeking.getEyePosition().subtract(this.position());
                        if (add.length() > 1.0F) {
                            add = add.normalize();
                        }

                        this.setSpinRadius(4.0F - 4.0F * Math.min(1.0F, (float)this.tickCount / 30.0F));
                        this.setSpinAroundPosition(this.position().add(add));
                    } else {
                        this.setSpinAroundPosition(playerPos);
                    }
                }
            }

        }

        if (owner != null && this.getPersistentData().getBoolean("PepperRadiant") && AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get()) {
            if (this.isStraight()) {
                if (despawnsIn > 150) {
                    this.setYRot(180 + this.owner.getYHeadRot());
                    if (encirclePos == null) {
                        this.setSpinAroundPosition(this.position());
                    } else if (!level().isClientSide) {
                        this.move(MoverType.SELF, getDeltaMovement());
                        float f = Math.min(0.5F, tickCount / 30F);
                        Vec3 angle = new Vec3(0, 0, f * this.getSpinRadius()).yRot((float) -Math.toRadians(this.getStartAngle() + spinAngle));
                        Vec3 encircle = encirclePos.add(angle);
                        Vec3 newDelta = encircle.subtract(this.position());
                        this.setDeltaMovement(newDelta.scale(0.05 * getSpinSpeed()));
                        spinAngle += getSpinSpeed();
                    }
                } else {
                    if (!level().isClientSide) {
                        this.setSpinSpeed(7);
                        Vec3 vec3 = new Vec3(0, 0, -0.01F * this.getSpinSpeed()).yRot((float) -Math.toRadians(this.getYRot()));
                        this.setDeltaMovement(this.getDeltaMovement().add(vec3));
                        if (!this.isNoGravity()) {
                            this.setDeltaMovement(this.getDeltaMovement().scale(0.9F).add(0.0D, -0.08D, 0.0D));
                        }
                        if (this.verticalCollision) {
                            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.9F, 0).multiply(0.4D, 1.0D, 0.4D));
                        }
                        this.move(MoverType.SELF, getDeltaMovement());
                    }
                }
            } else {
                if (encirclePos == null) {
                    this.setSpinAroundPosition(this.position());
                } else if (!level().isClientSide) {
                    this.move(MoverType.SELF, getDeltaMovement());
                    float f = Math.min(1.0F, tickCount / 30F);
                    Vec3 angle = new Vec3(0, 0, f * this.getSpinRadius()).yRot((float) -Math.toRadians(this.getStartAngle() + spinAngle));
                    Vec3 encircle = encirclePos.add(angle);
                    Vec3 newDelta = encircle.subtract(this.position());
                    this.setDeltaMovement(newDelta.scale(0.05 * getSpinSpeed()));
                    spinAngle += getSpinSpeed();
                }
            }
        } else {
            if (this.isStraight()) {
                if (!level().isClientSide) {
                    Vec3 vec3 = new Vec3(0, 0, -0.01F * this.getSpinSpeed()).yRot((float) -Math.toRadians(this.getYRot()));
                    this.setDeltaMovement(this.getDeltaMovement().add(vec3));
                    if (!this.isNoGravity()) {
                        this.setDeltaMovement(this.getDeltaMovement().scale(0.9F).add(0.0D, -0.08D, 0.0D));
                    }
                    if (this.verticalCollision) {
                        this.setDeltaMovement(this.getDeltaMovement().add(0, 0.9F, 0).multiply(0.4D, 1.0D, 0.4D));
                    }
                    this.move(MoverType.SELF, getDeltaMovement());
                }
            } else {
                if (encirclePos == null) {
                    this.setSpinAroundPosition(this.position());
                } else if (!level().isClientSide) {
                    this.move(MoverType.SELF, getDeltaMovement());
                    float f = Math.min(1.0F, tickCount / 30F);
                    Vec3 angle = new Vec3(0, 0, f * this.getSpinRadius()).yRot((float) -Math.toRadians(this.getStartAngle() + spinAngle));
                    Vec3 encircle = encirclePos.add(angle);
                    Vec3 newDelta = encircle.subtract(this.position());
                    this.setDeltaMovement(newDelta.scale(0.05 * getSpinSpeed()));
                    spinAngle += getSpinSpeed();
                }
            }
        }

        if (!this.getPersistentData().contains("PepperRadiant") || this.tickCount > 50){
            this.hurtEntities();
        }

    }

}
