package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.item.HazmatArmorItem;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.neoforged.neoforge.entity.PartEntity;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExGammaAttacks;
import org.crimsoncrips.alexscavesexemplified.server.entity.ToxicBarrelEntity;
import org.spongepowered.asm.mixin.Unique;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACExParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;


@Mixin(TremorzillaEntity.class)
public abstract class ACExTremorzillaMixin extends DinosaurEntity implements PlayerRideableJumping {

    @Shadow public abstract void setMaxBeamBreakLength(float f);

    @Shadow public abstract Animation getAnimation();

    @Shadow public abstract void setAnimation(Animation animation);

    @Shadow public abstract int getAnimationTick();

    @Shadow public Vec3 beamServerTarget;

    @Shadow protected abstract Vec3 createInitialBeamVec();


    @Shadow @Final public static Animation ANIMATION_ROAR_2;


    @Shadow private int beamTime;

    @Shadow private int maxBeamTime;

    @Shadow public abstract void setCharge(int charge);

    @Shadow public abstract void setBeamEndPosition(@Nullable Vec3 vec3);


    @Shadow public abstract void setFiring(boolean firing);


    @Shadow public abstract boolean isTremorzillaSwimming();

    @Shadow @Final private static EntityDimensions SWIMMING_SIZE;

    @Shadow public abstract boolean isFiring();

    @Shadow public abstract int getCharge();

    @Shadow public abstract Vec3 getBeamEndPosition();

    boolean sound;

    @Unique
    private static final float GAMMA_SCALE = 1.4F;
    @Unique
    private static final double GAMMA_STEP_HEIGHT = 1.6D * GAMMA_SCALE;
    @Unique
    private static final int GAMMA_BLOCK_BREAK_INTERVAL = ACExUtils.seconds(0.25F);
    @Unique
    private static final float STOMP_ATTACK_SIZE = 2.0F;
    @Unique
    private static final float PHASE_TRANSITION_DAMAGE_TAKEN = 0.1F;
    @Unique
    private static final double GAMMA_MAX_HEALTH = 750.0D;
    @Unique
    private static final int LEAP_LIFTOFF = ACExUtils.seconds(0.5F);
    @Unique
    private static final double RIDER_BEAM_REACH = 100.0D;
    @Unique
    private static final float SPIKE_BEAM_BREAK_CHANCE = 0.05F;
    @Unique
    private boolean alexsCavesExemplified$reducedBreaking;
    @Unique
    private static final double BASE_ARMOR = 10.0D;
    @Unique
    private static final double BASE_ATTACK_DAMAGE = 30.0D;
    @Unique
    private static final double GAMMA_ARMOR = 18.0D;
    @Unique
    private static final double GAMMA_ATTACK_DAMAGE = 50.0D;

    @Unique
    private boolean alexsCavesExemplified$lastGamma;
    @Unique
    private boolean alexsCavesExemplified$spitting;
    @Unique
    private int alexsCavesExemplified$laserTicks = -1;
    @Unique
    private Vec3 alexsCavesExemplified$laserOrigin;
    @Unique
    private Vec3 alexsCavesExemplified$laserPoint;
    @Unique
    private int alexsCavesExemplified$savedCharge;
    @Unique
    private int alexsCavesExemplified$barrelTicks = -1;
    @Unique
    private ToxicBarrelEntity alexsCavesExemplified$barrel;
    @Unique
    private LivingEntity alexsCavesExemplified$barrelTarget;
    @Unique
    private boolean alexsCavesExemplified$barrelFiring;
    @Unique
    private int alexsCavesExemplified$nukeBeamTicks = -1;
    @Unique
    private Vec3 alexsCavesExemplified$nukeBeamOrigin;
    @Unique
    private LivingEntity alexsCavesExemplified$nukeBeamTarget;
    @Unique
    private int alexsCavesExemplified$leapTicks = -1;
    @Unique
    private Vec3 alexsCavesExemplified$leapVelocity;
    @Unique
    private int alexsCavesExemplified$leapDuration = ACExGammaAttacks.LEAP_TIME;
    @Unique
    private int alexsCavesExemplified$spikeStormTicks = -1;
    @Unique
    private Vec3 alexsCavesExemplified$spikeStormOrigin;
    @Unique
    private boolean alexsCavesExemplified$controlledBeam;
    @Unique
    private boolean alexsCavesExemplified$controlledFired;
    @Unique
    private Vec3 alexsCavesExemplified$controlledBeamEnd;
    @Unique
    private Player alexsCavesExemplified$attackRider;
    @Unique
    private Vec3 alexsCavesExemplified$aimPos;
    @Unique
    private LivingEntity alexsCavesExemplified$aimEntity;
    @Unique
    private LivingEntity alexsCavesExemplified$phaseTarget;
    @Unique
    private ServerBossEvent alexsCavesExemplified$bossEvent;
    @Unique
    private int alexsCavesExemplified$bossBarGrace;
    @Unique
    private static final int BOSS_BAR_GRACE = ACExUtils.seconds(5.0F);
    @Unique
    private static final ResourceLocation AC_TREMORZILLA_EGG = ResourceLocation.fromNamespaceAndPath("alexscaves", "spawn_egg_tremorzilla");

    protected ACExTremorzillaMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    public void die(DamageSource pCause) {
        if(this.getData(ACExAttachments.GAMMA) && !isTame() && !isBaby() && pCause.getEntity() instanceof Player){
            ACExUtils.awardAdvancement(pCause.getEntity(), "gamma_tremorzilla_kill", "gamma_kill");
        }
        super.die(pCause);
    }

    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            amount *= 0.35F;
        }

        if (this.getData(ACExAttachments.ANIMATION_BEAMING)) {
            amount *= PHASE_TRANSITION_DAMAGE_TAKEN;
        }

        float determiner = this.getMaxHealth() / 3;
        float healthBefore = this.getHealth();
        boolean hurt = super.hurt(source, amount);

        if (hurt && this.isAlive() && healthBefore > determiner && this.getHealth() <= determiner && !isBaby() && !this.getData(ACExAttachments.SECOND_PHASE) && this.getData(ACExAttachments.GAMMA) && AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && source.getEntity() != null){
            this.alexsCavesExemplified$phaseTarget = source.getEntity() instanceof LivingEntity attacker ? attacker : this.getTarget();
            ACExAttachments.set(this, ACExAttachments.ANIMATION_BEAMING, true);
            this.beamServerTarget = createInitialBeamVec();
            this.setMaxBeamBreakLength(180F);
            setAnimation(ANIMATION_ROAR_2);
            setTarget(null);
            setLastHurtByMob(null);
            ACExAttachments.set(this, ACExAttachments.SECOND_PHASE, true);
            getAttribute(Attributes.ARMOR).setBaseValue(12F);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(75F);
        }
        return hurt;
    }

    @ModifyReturnValue(method = "getScale", at = @At("RETURN"))
    private float alexsCavesExemplified$getScale(float original) {
        return (this.isBaby() ? 0.15F : 1.0F) * gammaScale();
    }

    @ModifyReturnValue(method = "getBeamShootFrom", at = @At("RETURN"),remap = false)
    private Vec3 alexsCavesExemplified$getBeamShootFrom(Vec3 original, @Local(argsOnly = true) float partialTicks) {
        return this.getData(ACExAttachments.GAMMA) ? ACExGammaAttacks.beamOrigin((TremorzillaEntity) (Object) this, partialTicks) : original;
    }

    @ModifyExpressionValue(method = "tickBeamTargeting", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getViewVector(F)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 alexsCavesExemplified$tickBeamTargeting(Vec3 view, @Local Player player) {
        if (!this.getData(ACExAttachments.GAMMA))
            return view;
        TremorzillaEntity self = (TremorzillaEntity) (Object) this;
        return ACExGammaAttacks.riderCrosshair(self, player).subtract(self.getBeamShootFrom(1.0F)).scale(1.0D / RIDER_BEAM_REACH);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/DinosaurEntity;tick()V"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        boolean gamma = this.getData(ACExAttachments.GAMMA);
        if (this.level().isClientSide) {
            tickClientLeap();
        }
        if (gamma != this.alexsCavesExemplified$lastGamma) {
            this.alexsCavesExemplified$lastGamma = gamma;
            AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
            if (gamma && !this.level().isClientSide && maxHealth != null && maxHealth.getBaseValue() < GAMMA_MAX_HEALTH) {
                ACExUtils.setGamma((TremorzillaEntity) (Object) this, true);
                this.setHealth(this.getMaxHealth());
            }
            this.refreshDimensions();
            for (PartEntity<?> part : this.getParts()) {
                part.refreshDimensions();
            }
        }
        if (!this.level().isClientSide) {
            tickBossBar(gamma);
            setBase(Attributes.STEP_HEIGHT, gamma ? GAMMA_STEP_HEIGHT : Attributes.STEP_HEIGHT.value().getDefaultValue());
        }
        if (AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get()){
            Level level = this.level();
            if (this.getHealth() == this.getMaxHealth()){
                ACExAttachments.set(this, ACExAttachments.SECOND_PHASE, false);
                setBase(Attributes.ARMOR, gamma ? GAMMA_ARMOR : BASE_ARMOR);
                setBase(Attributes.ATTACK_DAMAGE, gamma ? GAMMA_ATTACK_DAMAGE : BASE_ATTACK_DAMAGE);
            }
            if (gamma && !level.isClientSide) {
                tickGammaAttacks();
            }

            if (getAnimation() == ANIMATION_ROAR_2 && getAnimationTick() >= 20 && getAnimationTick() <= 45 && this.getData(ACExAttachments.ANIMATION_BEAMING)){
                setFiring(true);
            } else if (this.getData(ACExAttachments.ANIMATION_BEAMING) && getAnimation() == ANIMATION_ROAR_2 && getAnimationTick() >= 45){
                sound = false;
                setFiring(false);
                this.playSound(ACSoundRegistry.TREMORZILLA_BEAM_END.get(), 8.0F, 1.0F);
                if (getAnimationTick() >= 55) {
                    if (this.alexsCavesExemplified$phaseTarget != null && this.alexsCavesExemplified$phaseTarget.isAlive()) {
                        setTarget(this.alexsCavesExemplified$phaseTarget);
                    }
                    this.alexsCavesExemplified$phaseTarget = null;
                    beamTime = 0;
                    this.beamServerTarget = null;
                    this.setBeamEndPosition(null);
                    ACExAttachments.set(this, ACExAttachments.ANIMATION_BEAMING, false);
                    this.setCharge(0);
                }
            }

            if (!sound && getAnimation() == ANIMATION_ROAR_2 && getAnimationTick() == 15 && this.getData(ACExAttachments.ANIMATION_BEAMING)){
                this.playSound(ACSoundRegistry.TREMORZILLA_ROAR.get(), 8.0F, isBaby() ? 2F : 1F);
                sound = true;
            }

        }

        if (!AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && this.getData(ACExAttachments.GAMMA)) {
            ACExUtils.setGamma((TremorzillaEntity)(Object)this, false);
            ACExAttachments.set(this, ACExAttachments.SECOND_PHASE, false);
        }
    }

    @Unique
    private void setBase(Holder<Attribute> attribute, double value) {
        AttributeInstance instance = this.getAttribute(attribute);
        if (instance != null && instance.getBaseValue() != value) {
            instance.setBaseValue(value);
        }
    }

    @Unique
    private void tickBossBar(boolean gamma) {
        boolean active = gamma && AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && this.isAlive() && !this.isTame();
        ServerPlayer targetPlayer = this.getTarget() instanceof ServerPlayer player && player.isAlive() ? player : null;
        if (active && targetPlayer != null) {
            this.alexsCavesExemplified$bossBarGrace = BOSS_BAR_GRACE;
        } else if (this.alexsCavesExemplified$bossBarGrace > 0) {
            this.alexsCavesExemplified$bossBarGrace--;
        }
        if (!active || this.alexsCavesExemplified$bossBarGrace <= 0) {
            if (this.alexsCavesExemplified$bossEvent != null) {
                this.alexsCavesExemplified$bossEvent.removeAllPlayers();
            }
            return;
        }
        if (this.alexsCavesExemplified$bossEvent == null) {
            this.alexsCavesExemplified$bossEvent = new ServerBossEvent(Component.empty(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_10);
        }
        ServerBossEvent bossEvent = this.alexsCavesExemplified$bossEvent;
        bossEvent.setName(this.hasCustomName() ? this.getDisplayName() : Component.translatable("entity.alexscavesexemplified.gamma_tremorzilla"));
        bossEvent.setColor(this.getData(ACExAttachments.SECOND_PHASE) ? BossEvent.BossBarColor.PURPLE : BossEvent.BossBarColor.BLUE);
        bossEvent.setProgress(Mth.clamp(this.getHealth() / this.getMaxHealth(), 0.0F, 1.0F));
        if (targetPlayer != null && !bossEvent.getPlayers().contains(targetPlayer)) {
            bossEvent.addPlayer(targetPlayer);
        }
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void alexsCavesExemplified$remove(RemovalReason removalReason, CallbackInfo ci) {
        if (this.alexsCavesExemplified$bossEvent != null) {
            this.alexsCavesExemplified$bossEvent.removeAllPlayers();
        }
    }

    @Inject(method = "calculateEntityAnimation", at = @At("HEAD"), cancellable = true)
    private void alexsCavesExemplified$calculateEntityAnimation(boolean flying, CallbackInfo ci) {
        if (this.alexsCavesExemplified$leapTicks >= 0 || this.getData(ACExAttachments.GAMMA_LEAPING)) {
            this.walkAnimation.update(0.0F, 0.4F);
            ci.cancel();
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return this.alexsCavesExemplified$leapTicks < 0 && super.causeFallDamage(fallDistance, multiplier, source);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(this.getData(ACExAttachments.GAMMA) ? ACExItemRegistry.GAMMA_TREMORZILLA_SPAWN_EGG.get() : BuiltInRegistries.ITEM.get(AC_TREMORZILLA_EGG));
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        if (this.alexsCavesExemplified$bossEvent != null) {
            this.alexsCavesExemplified$bossEvent.removePlayer(player);
        }
    }

    @Unique
    private float gammaScale() {
        return this.getData(ACExAttachments.GAMMA) ? GAMMA_SCALE : 1.0F;
    }

    @Unique
    private void tickGammaAttacks() {
        TremorzillaEntity self = (TremorzillaEntity) (Object) this;
        int request = this.getData(ACExAttachments.GAMMA_RIDER_REQUEST);
        if (request >= 0) {
            this.setData(ACExAttachments.GAMMA_RIDER_REQUEST, -1);
            startRiderAbility(self, request);
        }
        if (this.alexsCavesExemplified$controlledBeam) {
            tickControlledBeam(self);
        }
        if (this.alexsCavesExemplified$leapTicks >= 0) {
            tickLeap(self);
            return;
        }
        if (this.alexsCavesExemplified$spikeStormTicks >= 0) {
            tickSpikeStorm(self);
            return;
        }
        if (this.alexsCavesExemplified$laserTicks >= 0) {
            tickSpikeLaser(self);
            return;
        }
        if (this.alexsCavesExemplified$nukeBeamTicks >= 0) {
            tickNukeBeam(self);
            return;
        }
        if (this.alexsCavesExemplified$barrelTicks >= 0) {
            tickBarrel(self);
            return;
        }
        if (this.alexsCavesExemplified$spitting) {
            if (this.getAnimation() != TremorzillaEntity.ANIMATION_SPEAK) {
                this.alexsCavesExemplified$spitting = false;
                clearAim();
            } else if (this.getAnimationTick() == ACExGammaAttacks.WASTE_SPIT_TICK) {
                if (updateAim(self, this.getTarget())) {
                    ACExGammaAttacks.spitWaste(self, this.alexsCavesExemplified$aimPos, this.alexsCavesExemplified$aimEntity);
                }
                this.alexsCavesExemplified$spitting = false;
                finish(self, ACExGammaAttacks.WASTE_SPIT);
            }
            return;
        }
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive() || this.isVehicle() || this.isBaby() || busy())
            return;
        double distance = this.distanceTo(target);
        boolean spikeReady = ACExGammaAttacks.cooldownRemaining(self, ACExGammaAttacks.SPIKE_LASER) <= 0;
        boolean advancedUnlocked = this.isTame() || this.getData(ACExAttachments.SECOND_PHASE);
        if (ACExGammaAttacks.cooldownRemaining(self, ACExGammaAttacks.SPIKE_STORM) > 0 && ACExGammaAttacks.cooldownRemaining(self, ACExGammaAttacks.LEAP) <= 0 && distance <= this.getBbWidth() * 0.5D + ACExGammaAttacks.LEAP_TRIGGER_RANGE && this.random.nextInt(ACExGammaAttacks.LEAP_CHANCE) == 0) {
            startLeap(ACExGammaAttacks.leapLanding(self, target), ACExGammaAttacks.LEAP_TIME);
        } else if (advancedUnlocked && ACExGammaAttacks.cooldownRemaining(self, ACExGammaAttacks.SPIKE_STORM) <= 0 && distance <= ACExGammaAttacks.SPIKE_STORM_RANGE && this.random.nextInt(ACExGammaAttacks.SPIKE_STORM_CHANCE) == 0) {
            startAbility(ACExGammaAttacks.SPIKE_STORM, target);
        } else if (spikeReady && distance <= this.getBbWidth() * 0.5D + ACExGammaAttacks.SPIKE_NEAR_RANGE) {
            startAbility(ACExGammaAttacks.SPIKE_LASER, target);
        } else if (advancedUnlocked && ACExGammaAttacks.cooldownRemaining(self, ACExGammaAttacks.NUKE_BEAM) <= 0 && distance >= ACExGammaAttacks.NUKE_BEAM_MIN_RANGE && distance <= ACExGammaAttacks.NUKE_BEAM_MAX_RANGE && this.random.nextInt(ACExGammaAttacks.NUKE_BEAM_CHANCE) == 0) {
            startAbility(ACExGammaAttacks.NUKE_BEAM, target);
        } else if (advancedUnlocked && ACExGammaAttacks.cooldownRemaining(self, ACExGammaAttacks.TOXIC_BARREL) <= 0 && distance >= ACExGammaAttacks.BARREL_MIN_RANGE && distance <= ACExGammaAttacks.BARREL_MAX_RANGE && this.random.nextInt(ACExGammaAttacks.BARREL_CHANCE) == 0) {
            startAbility(ACExGammaAttacks.TOXIC_BARREL, target);
        } else if (spikeReady && distance <= ACExGammaAttacks.SPIKE_TRIGGER_RANGE && this.random.nextInt(ACExGammaAttacks.SPIKE_CHANCE) == 0) {
            startAbility(ACExGammaAttacks.SPIKE_LASER, target);
        } else if (ACExGammaAttacks.cooldownRemaining(self, ACExGammaAttacks.WASTE_SPIT) <= 0 && distance >= ACExGammaAttacks.WASTE_MIN_RANGE && distance <= ACExGammaAttacks.WASTE_MAX_RANGE && this.random.nextInt(ACExGammaAttacks.WASTE_CHANCE) == 0) {
            startAbility(ACExGammaAttacks.WASTE_SPIT, target);
        }
    }

    @Unique
    private boolean busy() {
        return this.alexsCavesExemplified$spitting || this.alexsCavesExemplified$leapTicks >= 0 || this.alexsCavesExemplified$spikeStormTicks >= 0 || ((TremorzillaEntity) (Object) this).wantsToUseBeamFromServer || this.alexsCavesExemplified$laserTicks >= 0 || this.alexsCavesExemplified$barrelTicks >= 0 || this.alexsCavesExemplified$nukeBeamTicks >= 0 || this.isFiring() || this.getData(ACExAttachments.ANIMATION_BEAMING) || (this.getAnimation() != null && this.getAnimation() != IAnimatedEntity.NO_ANIMATION);
    }

    @Unique
    private void startRiderAbility(TremorzillaEntity self, int ability) {
        if (ability < 0 || ability >= ACExGammaAttacks.ABILITY_COUNT || !(this.getControllingPassenger() instanceof Player rider) || !ACExGammaAttacks.canRiderUse(self, rider))
            return;
        if (busy()) {
            rider.displayClientMessage(Component.translatable("misc.alexscavesexemplified.gamma_busy"), true);
            return;
        }
        if (ACExGammaAttacks.cooldownRemaining(self, ability) > 0) {
            rider.displayClientMessage(Component.translatable("misc.alexscavesexemplified.sugar_staff_cooldown"), true);
            return;
        }
        if (ability == ACExGammaAttacks.CONTROLLED_LASER && !ACExGammaAttacks.beamCharged(self)) {
            rider.displayClientMessage(Component.translatable("misc.alexscavesexemplified.gamma_not_charged"), true);
            return;
        }
        float yaw = rider.getYHeadRot();
        this.yBodyRot = yaw;
        this.setYRot(yaw);
        this.setYHeadRot(yaw);
        this.alexsCavesExemplified$attackRider = rider;
        startAbility(ability, null);
    }

    @Unique
    private void startAbility(int ability, @Nullable LivingEntity target) {
        switch (ability) {
            case ACExGammaAttacks.NUKE_BEAM -> {
                this.alexsCavesExemplified$nukeBeamTicks = 0;
                this.alexsCavesExemplified$nukeBeamTarget = target;
                this.alexsCavesExemplified$nukeBeamOrigin = this.position();
                this.setAnimation(TremorzillaEntity.ANIMATION_PREPARE_BREATH);
                this.playSound(ACSoundRegistry.TREMORZILLA_BEAM_START.get(), 8.0F, 0.8F);
            }
            case ACExGammaAttacks.TOXIC_BARREL -> {
                this.alexsCavesExemplified$barrelTicks = 0;
                this.alexsCavesExemplified$barrelTarget = target;
                this.setAnimation(TremorzillaEntity.ANIMATION_SPEAK);
            }
            case ACExGammaAttacks.SPIKE_LASER -> {
                this.alexsCavesExemplified$laserTicks = 0;
                this.alexsCavesExemplified$laserOrigin = this.position();
                this.setAnimation(TremorzillaEntity.ANIMATION_PREPARE_BREATH);
                this.playSound(ACSoundRegistry.TREMORZILLA_BEAM_START.get(), 8.0F, 1.2F);
            }
            case ACExGammaAttacks.SPIKE_STORM -> {
                this.alexsCavesExemplified$spikeStormTicks = 0;
                this.alexsCavesExemplified$spikeStormOrigin = this.position();
                this.setAnimation(TremorzillaEntity.ANIMATION_PREPARE_BREATH);
                this.playSound(ACSoundRegistry.TREMORZILLA_BEAM_START.get(), 8.0F, 0.6F);
            }
            case ACExGammaAttacks.CONTROLLED_LASER -> {
                ((TremorzillaEntity) (Object) this).wantsToUseBeamFromServer = true;
                this.alexsCavesExemplified$controlledBeam = true;
                this.alexsCavesExemplified$controlledFired = false;
                this.alexsCavesExemplified$controlledBeamEnd = null;
                this.maxBeamTime = ACExGammaAttacks.CONTROLLED_LASER_TIME;
                clearAim();
            }
            case ACExGammaAttacks.WASTE_SPIT -> {
                this.setAnimation(TremorzillaEntity.ANIMATION_SPEAK);
                this.alexsCavesExemplified$spitting = true;
            }
            default -> {
            }
        }
    }

    @Unique
    private boolean updateAim(TremorzillaEntity self, @Nullable LivingEntity aiTarget) {
        Player rider = this.alexsCavesExemplified$attackRider;
        if (rider != null) {
            if (rider.isAlive() && rider.getVehicle() == self) {
                this.alexsCavesExemplified$aimEntity = ACExGammaAttacks.riderTarget(self, rider);
                this.alexsCavesExemplified$aimPos = ACExGammaAttacks.riderAim(self, rider, this.alexsCavesExemplified$aimEntity);
            } else {
                this.alexsCavesExemplified$aimEntity = null;
            }
            return this.alexsCavesExemplified$aimPos != null;
        }
        if (aiTarget != null && aiTarget.isAlive()) {
            this.alexsCavesExemplified$aimEntity = aiTarget;
            this.alexsCavesExemplified$aimPos = aiTarget.position();
            return true;
        }
        return false;
    }

    @Unique
    private void clearAim() {
        this.alexsCavesExemplified$attackRider = null;
        this.alexsCavesExemplified$aimPos = null;
        this.alexsCavesExemplified$aimEntity = null;
    }

    @Unique
    private void finish(TremorzillaEntity self, int ability) {
        ACExGammaAttacks.startCooldown(self, ability);
        clearAim();
    }

    @Unique
    private void tickControlledBeam(TremorzillaEntity self) {
        if (this.isFiring()) {
            if (!this.alexsCavesExemplified$controlledFired) {
                ACExGammaAttacks.startBeamMerge(self, ACExGammaAttacks.CONTROLLED_LASER_TIME);
            }
            this.alexsCavesExemplified$controlledFired = true;
            Vec3 end = this.getBeamEndPosition();
            if (end != null) {
                this.alexsCavesExemplified$controlledBeamEnd = end;
            }
            return;
        }
        if (!this.alexsCavesExemplified$controlledFired && self.wantsToUseBeamFromServer)
            return;
        if (this.alexsCavesExemplified$controlledFired && this.alexsCavesExemplified$controlledBeamEnd != null) {
            ACExGammaAttacks.miniNuke(self, this.alexsCavesExemplified$controlledBeamEnd, ACExGammaAttacks.CONTROLLED_NUKE_SIZE, ACExGammaAttacks.CONTROLLED_NUKE_DURATION);
        }
        ACExGammaAttacks.stopBeamMerge(self);
        this.alexsCavesExemplified$controlledBeam = false;
        this.alexsCavesExemplified$controlledFired = false;
        this.alexsCavesExemplified$controlledBeamEnd = null;
    }

    @Unique
    private void startLeap(@Nullable Vec3 landing, int duration) {
        this.alexsCavesExemplified$leapTicks = 0;
        ACExAttachments.set(this, ACExAttachments.GAMMA_LEAPING, true);
        this.alexsCavesExemplified$leapDuration = duration;
        this.alexsCavesExemplified$leapVelocity = landing == null ? null : ACExGammaAttacks.leapVelocity(this.position(), landing, duration);
        this.getNavigation().stop();
        this.playSound(ACSoundRegistry.TREMORZILLA_STOMP.get(), 8.0F, 0.6F);
    }

    @Unique
    private void tickLeap(TremorzillaEntity self) {
        int age = this.alexsCavesExemplified$leapTicks++;
        this.getNavigation().stop();
        this.fallDistance = 0.0F;
        Vec3 velocity = this.alexsCavesExemplified$leapVelocity;
        int duration = this.alexsCavesExemplified$leapDuration;
        if (age < duration && velocity != null) {
            this.setDeltaMovement(velocity.x, velocity.y - ACExGammaAttacks.leapGravity() * age, velocity.z);
            this.hasImpulse = true;
            return;
        }
        boolean landed = this.onGround() && (velocity != null || age > LEAP_LIFTOFF);
        if (landed || age >= duration + ACExGammaAttacks.LEAP_LANDING_TIMEOUT) {
            this.playSound(ACSoundRegistry.TREMORZILLA_STOMP.get(), 8.0F, 0.5F);
            this.alexsCavesExemplified$leapTicks = -1;
            this.alexsCavesExemplified$leapVelocity = null;
            ACExAttachments.set(this, ACExAttachments.GAMMA_LEAPING, false);
            if (this.alexsCavesExemplified$attackRider != null) {
                clearAim();
            } else {
                finish(self, ACExGammaAttacks.LEAP);
            }
        }
    }

    @Unique
    private void tickClientLeap() {
        if (this.alexsCavesExemplified$leapTicks < 0 || this.alexsCavesExemplified$leapVelocity == null)
            return;
        int age = this.alexsCavesExemplified$leapTicks++;
        Vec3 velocity = this.alexsCavesExemplified$leapVelocity;
        if (age < this.alexsCavesExemplified$leapDuration && this.isControlledByLocalInstance()) {
            this.setDeltaMovement(velocity.x, velocity.y - ACExGammaAttacks.leapGravity() * age, velocity.z);
            return;
        }
        this.alexsCavesExemplified$leapTicks = -1;
        this.alexsCavesExemplified$leapVelocity = null;
    }

    @Override
    public void onPlayerJump(int power) {
        if (this.level().isClientSide && power > 0 && this.getControllingPassenger() instanceof Player rider) {
            TremorzillaEntity self = (TremorzillaEntity) (Object) this;
            int duration = ACExGammaAttacks.leapTime(ACExGammaAttacks.riderLeapHeight(power));
            this.alexsCavesExemplified$leapTicks = 0;
            this.alexsCavesExemplified$leapDuration = duration;
            this.alexsCavesExemplified$leapVelocity = ACExGammaAttacks.leapVelocity(this.position(), ACExGammaAttacks.riderLeapLanding(self, rider, power), duration);
        }
    }

    @Override
    public boolean canJump() {
        TremorzillaEntity self = (TremorzillaEntity) (Object) this;
        return this.alexsCavesExemplified$leapTicks < 0 && this.getControllingPassenger() instanceof Player rider && ACExGammaAttacks.canRiderUse(self, rider) && ACExGammaAttacks.canRiderLeap(self);
    }

    @Override
    public void handleStartJump(int power) {
        if (!this.level().isClientSide && this.getControllingPassenger() instanceof Player rider) {
            this.alexsCavesExemplified$attackRider = rider;
            startLeap(null, ACExGammaAttacks.leapTime(ACExGammaAttacks.riderLeapHeight(power)));
        }
    }

    @Override
    public void handleStopJump() {
    }

    @Unique
    private void tickSpikeStorm(TremorzillaEntity self) {
        int age = this.alexsCavesExemplified$spikeStormTicks++;
        this.getNavigation().stop();
        boolean aborted = this.getData(ACExAttachments.ANIMATION_BEAMING);
        if (age < ACExGammaAttacks.LASER_WINDUP && !aborted)
            return;
        if (age == ACExGammaAttacks.LASER_WINDUP && !aborted) {
            this.alexsCavesExemplified$savedCharge = this.getCharge();
            this.setFiring(true);
            ACExGammaAttacks.spawnSpikeStorm(self, this.alexsCavesExemplified$spikeStormOrigin);
        }
        int firing = age - ACExGammaAttacks.LASER_WINDUP;
        if (!aborted) {
            Vec3 point = ACExGammaAttacks.spikeStormLaserPoint(self, this.alexsCavesExemplified$spikeStormOrigin, firing);
            this.beamTime = 0;
            this.beamServerTarget = point;
            this.setBeamEndPosition(point);
        }
        if (aborted || firing >= ACExGammaAttacks.SPIKE_STORM_DURATION) {
            if (firing >= 0) {
                this.setFiring(false);
                this.setBeamEndPosition(null);
                this.beamServerTarget = null;
                this.beamTime = 0;
                this.setCharge(this.alexsCavesExemplified$savedCharge);
                this.playSound(ACSoundRegistry.TREMORZILLA_BEAM_END.get(), 8.0F, 1.0F);
            }
            this.alexsCavesExemplified$spikeStormTicks = -1;
            this.alexsCavesExemplified$spikeStormOrigin = null;
            finish(self, ACExGammaAttacks.SPIKE_STORM);
        }
    }

    @Unique
    private void tickSpikeLaser(TremorzillaEntity self) {
        if (this.getData(ACExAttachments.ANIMATION_BEAMING)) {
            this.alexsCavesExemplified$laserTicks = -1;
            finish(self, ACExGammaAttacks.SPIKE_LASER);
            return;
        }
        int age = this.alexsCavesExemplified$laserTicks++;
        if (age < ACExGammaAttacks.LASER_WINDUP)
            return;
        if (age == ACExGammaAttacks.LASER_WINDUP) {
            this.alexsCavesExemplified$savedCharge = this.getCharge();
            this.alexsCavesExemplified$laserPoint = ACExGammaAttacks.laserPoint(self, this.alexsCavesExemplified$laserOrigin);
            this.beamServerTarget = this.alexsCavesExemplified$laserPoint;
            this.setFiring(true);
        }
        int sinceFire = age - ACExGammaAttacks.LASER_WINDUP;
        this.beamTime = 0;
        this.setBeamEndPosition(this.alexsCavesExemplified$laserPoint);
        if (sinceFire % ACExGammaAttacks.RING_INTERVAL == 0 && sinceFire / ACExGammaAttacks.RING_INTERVAL < ACExGammaAttacks.RING_COUNT) {
            ACExGammaAttacks.spawnSpikeRing(self, this.alexsCavesExemplified$laserOrigin, sinceFire / ACExGammaAttacks.RING_INTERVAL);
        }
        if (sinceFire >= ACExGammaAttacks.LASER_DURATION) {
            this.setFiring(false);
            this.setBeamEndPosition(null);
            this.beamServerTarget = null;
            this.beamTime = 0;
            this.setCharge(this.alexsCavesExemplified$savedCharge);
            this.playSound(ACSoundRegistry.TREMORZILLA_BEAM_END.get(), 8.0F, 1.0F);
            this.alexsCavesExemplified$laserTicks = -1;
            finish(self, ACExGammaAttacks.SPIKE_LASER);
        }
    }

    @Unique
    private void tickBarrel(TremorzillaEntity self) {
        int age = this.alexsCavesExemplified$barrelTicks++;
        ToxicBarrelEntity barrel = this.alexsCavesExemplified$barrel;
        if (this.getData(ACExAttachments.ANIMATION_BEAMING)) {
            if (barrel != null && barrel.isHeld()) {
                barrel.burst();
            }
            endBarrel(self);
            return;
        }
        int spitTick = ACExGammaAttacks.WASTE_SPIT_TICK;
        int holdTick = spitTick + ACExGammaAttacks.BARREL_RISE;
        if (age == spitTick) {
            barrel = updateAim(self, this.alexsCavesExemplified$barrelTarget) ? ACExGammaAttacks.spitBarrel(self, this.alexsCavesExemplified$aimPos, this.alexsCavesExemplified$aimEntity, this.alexsCavesExemplified$attackRider != null) : null;
            this.alexsCavesExemplified$barrel = barrel;
            if (barrel == null) {
                endBarrel(self);
                return;
            }
        }
        if (age <= spitTick)
            return;
        if (barrel == null || barrel.isRemoved()) {
            endBarrel(self);
            return;
        }
        if (updateAim(self, this.alexsCavesExemplified$barrelTarget)) {
            barrel.aimAt(this.alexsCavesExemplified$aimPos, this.alexsCavesExemplified$aimEntity);
        }
        if (age == holdTick) {
            this.alexsCavesExemplified$savedCharge = this.getCharge();
            this.alexsCavesExemplified$barrelFiring = true;
            this.setFiring(true);
            barrel.setHeld(true);
        }
        if (age >= holdTick) {
            Vec3 center = barrel.getBoundingBox().getCenter();
            this.beamTime = 0;
            this.beamServerTarget = center;
            this.setBeamEndPosition(center);
        }
        if (age >= holdTick + ACExGammaAttacks.BARREL_FUSE) {
            barrel.burst();
            endBarrel(self);
        }
    }

    @Unique
    private void endBarrel(TremorzillaEntity self) {
        if (this.alexsCavesExemplified$barrelFiring) {
            this.setFiring(false);
            this.setBeamEndPosition(null);
            this.beamServerTarget = null;
            this.beamTime = 0;
            this.setCharge(this.alexsCavesExemplified$savedCharge);
            this.playSound(ACSoundRegistry.TREMORZILLA_BEAM_END.get(), 8.0F, 1.0F);
        }
        this.alexsCavesExemplified$barrelFiring = false;
        this.alexsCavesExemplified$barrelTicks = -1;
        this.alexsCavesExemplified$barrel = null;
        this.alexsCavesExemplified$barrelTarget = null;
        finish(self, ACExGammaAttacks.TOXIC_BARREL);
    }

    @Unique
    private void tickNukeBeam(TremorzillaEntity self) {
        boolean aborted = this.getData(ACExAttachments.ANIMATION_BEAMING) || !updateAim(self, this.alexsCavesExemplified$nukeBeamTarget);
        int age = this.alexsCavesExemplified$nukeBeamTicks++;
        if (aborted && age < ACExGammaAttacks.LASER_WINDUP) {
            this.alexsCavesExemplified$nukeBeamTicks = -1;
            this.alexsCavesExemplified$nukeBeamTarget = null;
            clearAim();
            return;
        }
        if (age < ACExGammaAttacks.LASER_WINDUP)
            return;
        if (age == ACExGammaAttacks.LASER_WINDUP) {
            this.alexsCavesExemplified$savedCharge = this.getCharge();
            this.setFiring(true);
            ACExGammaAttacks.startBeamMerge(self, ACExGammaAttacks.NUKE_BEAM_SWEEP);
        }
        int sweep = age - ACExGammaAttacks.LASER_WINDUP;
        Vec3 point = aborted ? this.getBeamEndPosition() : ACExGammaAttacks.nukeBeamPoint(self, this.alexsCavesExemplified$nukeBeamOrigin, this.alexsCavesExemplified$aimPos, Math.min(1.0F, sweep / (float) ACExGammaAttacks.NUKE_BEAM_SWEEP));
        if (point != null) {
            this.beamTime = 0;
            this.beamServerTarget = point;
            this.setBeamEndPosition(point);
            if (sweep % ACExGammaAttacks.NUKE_BEAM_HURT_INTERVAL == 0) {
                self.hurtEntitiesAround(point, ACExGammaAttacks.NUKE_BEAM_HURT_RADIUS, ACExGammaAttacks.NUKE_BEAM_DAMAGE, 1.0F, true, true, false);
            }
        }
        if (aborted || sweep >= ACExGammaAttacks.NUKE_BEAM_SWEEP) {
            if (!aborted && point != null) {
                ACExGammaAttacks.miniNuke(self, point);
            }
            this.setFiring(false);
            this.setBeamEndPosition(null);
            this.beamServerTarget = null;
            this.beamTime = 0;
            this.setCharge(this.alexsCavesExemplified$savedCharge);
            this.playSound(ACSoundRegistry.TREMORZILLA_BEAM_END.get(), 8.0F, 1.0F);
            this.alexsCavesExemplified$nukeBeamTicks = -1;
            this.alexsCavesExemplified$nukeBeamTarget = null;
            ACExGammaAttacks.stopBeamMerge(self);
            finish(self, ACExGammaAttacks.NUKE_BEAM);
        }
    }

    @WrapOperation(method = "tickBreath", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;breakBlocksAround(Lnet/minecraft/world/phys/Vec3;FZZF)Z"), remap = false)
    private boolean alexsCavesExemplified$tickBreathBreak(TremorzillaEntity instance, Vec3 center, float radius, boolean square, boolean triggerExplosions, float dropChance, Operation<Boolean> original) {
        if (this.alexsCavesExemplified$barrelFiring)
            return false;
        this.alexsCavesExemplified$reducedBreaking = this.alexsCavesExemplified$spikeStormTicks >= 0 || this.alexsCavesExemplified$laserTicks >= 0;
        try {
            return original.call(instance, center, radius, square, triggerExplosions, dropChance);
        } finally {
            this.alexsCavesExemplified$reducedBreaking = false;
        }
    }

    @ModifyExpressionValue(method = "breakBlocksAround", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;blocksMotion()Z"), remap = false)
    private boolean alexsCavesExemplified$breakBlocksAround(boolean blocksMotion) {
        return blocksMotion && (!this.alexsCavesExemplified$reducedBreaking || this.random.nextFloat() < SPIKE_BEAM_BREAK_CHANCE);
    }

    @Inject(method = "onKeyPacket", at = @At("HEAD"), cancellable = true, remap = false)
    private void alexsCavesExemplified$onKeyPacket(Entity keyPresser, int type, CallbackInfo ci) {
        if (type == 2 && this.getData(ACExAttachments.GAMMA) && AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get()) {
            ci.cancel();
        }
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;tickBeamTargeting()V"))
    private boolean alexsCavesExemplified$tickBeamTargeting(TremorzillaEntity instance) {
        return this.alexsCavesExemplified$laserTicks < 0 && this.alexsCavesExemplified$nukeBeamTicks < 0 && this.alexsCavesExemplified$spikeStormTicks < 0 && !this.alexsCavesExemplified$barrelFiring;
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;hurtEntitiesAround(Lnet/minecraft/world/phys/Vec3;FFFZZZ)Z"), index = 1)
    private float alexsCavesExemplified$tickHurtRadius(float radius) {
        return radius * gammaScale();
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;stompEffect(ZFFFF)V"), index = 2)
    private float alexsCavesExemplified$tickStompHurtSize(float hurtSize) {
        return hurtSize * gammaScale();
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;stompEffect(ZFFFF)V"), index = 3)
    private float alexsCavesExemplified$tickStompForwards(float forwards) {
        return forwards * gammaScale();
    }

    @Inject(method = "stompEffect", at = @At("TAIL"))
    private void alexsCavesExemplified$stompEffectSpikes(boolean left, float size, float hurtSize, float forwards, float damage, CallbackInfo ci) {
        if (this.level().isClientSide || size < STOMP_ATTACK_SIZE || !this.getData(ACExAttachments.GAMMA) || !AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() || (this.getAnimation() != TremorzillaEntity.ANIMATION_LEFT_STOMP && this.getAnimation() != TremorzillaEntity.ANIMATION_RIGHT_STOMP))
            return;
        double side = (left ? 2.2F : -2.2F) * gammaScale();
        Vec3 foot = this.position().add(new Vec3(side, 0.0D, forwards).yRot(-this.yBodyRot * Mth.DEG_TO_RAD));
        ACExGammaAttacks.spawnStompSpikes((TremorzillaEntity) (Object) this, foot);
    }

    @ModifyConstant(method = "stompEffect", constant = @Constant(doubleValue = (double) 2.2F))
    private double alexsCavesExemplified$stompEffect(double offset) {
        return offset * gammaScale();
    }

    @ModifyConstant(method = "stompEffect", constant = @Constant(doubleValue = (double) -2.2F))
    private double alexsCavesExemplified$stompEffect1(double offset) {
        return offset * gammaScale();
    }

    @ModifyConstant(method = "breakBlocksInBoundingBox", constant = @Constant(doubleValue = 2.0D))
    private double alexsCavesExemplified$breakBlocksInBoundingBox(double inflate) {
        return inflate * gammaScale();
    }

    @ModifyConstant(method = "aiStep", constant = @Constant(intValue = 10))
    private int alexsCavesExemplified$aiStep(int interval) {
        return this.getData(ACExAttachments.GAMMA) ? GAMMA_BLOCK_BREAK_INTERVAL : interval;
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;breakBlocksAround(Lnet/minecraft/world/phys/Vec3;FZZF)Z"),index = 1)
    private float alexsCavesExemplified$tick1(float radius) {
        return radius * gammaScale();
    }

    @Inject(method = "positionRider", at = @At("HEAD"), cancellable = true)
    private void alexsCavesExemplified$positionRider(Entity passenger, Entity.MoveFunction moveFunction, CallbackInfo ci) {
        if (!this.getData(ACExAttachments.GAMMA) || !this.isPassengerOfSameVehicle(passenger) || !(passenger instanceof LivingEntity living) || this.touchingUnloadedChunk())
            return;
        ci.cancel();
        Vec3 eye = ACExGammaAttacks.eyePosition((TremorzillaEntity) (Object) this, this.getSwimAmount(1.0F));
        passenger.setYBodyRot(this.yBodyRot);
        passenger.fallDistance = 0.0F;
        if (!this.isFiring()) {
            this.clampRotation(living, 105.0F);
        }
        moveFunction.accept(passenger, eye.x, eye.y + ACExGammaAttacks.RIDER_LIFT - passenger.getEyeHeight(), eye.z);
    }

    @ModifyArg(method = "tickBreath", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;setBeamEndPosition(Lnet/minecraft/world/phys/Vec3;)V"), remap = false)
    private Vec3 alexsCavesExemplified$tickBreathBeamEnd(Vec3 end) {
        if (this.beamServerTarget == null || (this.alexsCavesExemplified$laserTicks < 0 && this.alexsCavesExemplified$nukeBeamTicks < 0 && this.alexsCavesExemplified$spikeStormTicks < 0 && !this.alexsCavesExemplified$barrelFiring))
            return end;
        Vec3 from = ((TremorzillaEntity) (Object) this).getBeamShootFrom(1.0F);
        return this.level().clip(new ClipContext(from, this.beamServerTarget, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getLocation();
    }

    @ModifyConstant(method = "tickBreath",constant = @Constant(floatValue = 5.0F),remap = false)
    private float alexsCavesExemplified$tickBreath1(float amount) {
        return this.getData(ACExAttachments.GAMMA) ? 10 : amount;
    }

    @ModifyArg(method = "tickBreath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V",ordinal = 2))
    private ParticleOptions alexsCavesExemplified$tickBreath2(ParticleOptions pParticleData) {
        return (AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && this.getData(ACExAttachments.GAMMA) ? ACExParticleRegistry.TREMORZILLA_GAMMA_PROTON.get() : this.getAltSkin() == 2 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_TECTONIC_PROTON.get() : (this.getAltSkin() == 1 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_RETRO_PROTON.get() : (ParticleOptions)ACParticleRegistry.TREMORZILLA_PROTON.get()));
    }

    @ModifyArg(method = "tickBreath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V",ordinal = 0))
    private ParticleOptions alexsCavesExemplified$tickBreath3(ParticleOptions pParticleData) {
        return (AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && this.getData(ACExAttachments.GAMMA) ? ACExParticleRegistry.TREMORZILLA_GAMMA_EXPLOSION.get() : this.getAltSkin() == 2 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_TECTONIC_EXPLOSION.get() : (this.getAltSkin() == 1 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_RETRO_EXPLOSION.get() : (ParticleOptions)ACParticleRegistry.TREMORZILLA_EXPLOSION.get()));
    }

    @ModifyArg(method = "tickBreath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V",ordinal = 1))
    private ParticleOptions alexsCavesExemplified$tickBreath4(ParticleOptions pParticleData) {
        return (AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && this.getData(ACExAttachments.GAMMA) ? ACExParticleRegistry.TREMORZILLA_GAMMA_LIGHTNING.get() : this.getAltSkin() == 2 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_TECTONIC_LIGHTNING.get() : (this.getAltSkin() == 1 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_RETRO_LIGHTNING.get() : (ParticleOptions)ACParticleRegistry.TREMORZILLA_LIGHTNING.get()));
    }

    @ModifyConstant(method = "tick",constant = @Constant(intValue = 100,ordinal = 1))
    private int alexsCavesExemplified$tick(int amount) {
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && this.getData(ACExAttachments.GAMMA) ? ACExUtils.seconds(12.5F) : amount;
    }

    @ModifyConstant(method = "tick",constant = @Constant(floatValue = 100.0F,ordinal = 0))
    private float alexsCavesExemplified$tick(float constant) {
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && this.getData(ACExAttachments.GAMMA) ? 180 : constant;
    }

    @ModifyConstant(method = "hurtEntitiesAround",constant = @Constant(intValue = 2),remap = false)
    private int alexsCavesExemplified$hurtEntitiesAround(int amount, @Local LivingEntity living) {
        int hazmatReduction = AlexsCavesExemplified.COMMON_CONFIG.ARMORED_LIQUIDATORS_ENABLED.get() ? HazmatArmorItem.getWornAmount(living) : 0;
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && this.getData(ACExAttachments.GAMMA) ? (6 - hazmatReduction > 0 ? 6 - hazmatReduction : 1) : (amount - hazmatReduction > 0 ? amount - hazmatReduction : 1);
    }

    @Inject(method = "hurtEntitiesAround", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;knockbackTarget(Lnet/minecraft/world/entity/Entity;DDDZ)V"),locals = LocalCapture.CAPTURE_FAILSOFT,remap = false)
    private void alexsCavesExemplified$hurtEntitiesAround1(Vec3 center, float radius, float damageAmount, float knockbackAmount, boolean radioactive, boolean hurtsOtherKaiju, boolean stretchY, CallbackInfoReturnable<Boolean> cir, AABB aabb, boolean flag, DamageSource damageSource, Iterator var11, LivingEntity living) {
        if (AlexsCavesExemplified.COMMON_CONFIG.GAMMA_TREMORZILLA_ENABLED.get() && this.getData(ACExAttachments.GAMMA)){
            living.setRemainingFireTicks(ACExUtils.seconds(50.0F));
        }
    }


}
