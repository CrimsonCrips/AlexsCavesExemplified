package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.TephraEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

import static net.minecraft.world.entity.EntityType.LIGHTNING_BOLT;

@Debug(export=true)
@Mixin(TremorzillaEntity.class)
public abstract class ACETremorzillaMixin extends DinosaurEntity implements Gammafied {

    @Shadow public abstract void setMaxBeamBreakLength(float f);

    @Shadow public abstract Animation getAnimation();

    @Shadow public abstract void setAnimation(Animation animation);

    @Shadow public abstract int getAnimationTick();

    @Shadow public Vec3 beamServerTarget;

    @Shadow protected abstract Vec3 createInitialBeamVec();


    @Shadow @Final public static Animation ANIMATION_ROAR_2;


    @Shadow private int beamTime;

    @Shadow public abstract void setCharge(int charge);

    @Shadow public abstract void setBeamEndPosition(@Nullable Vec3 vec3);


    @Shadow public abstract void setFiring(boolean firing);


    @Shadow public abstract Vec3 getBeamShootFrom(float partialTicks);

    @Shadow private float beamProgress;


    boolean sound;

    protected ACETremorzillaMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final EntityDataAccessor<Boolean> GAMMA = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ANIMATION_BEAMING = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SECOND_PHASE = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean isGamma() {
        return this.entityData.get(GAMMA);
    }

    public void setGamma(boolean val) {
        this.entityData.set(GAMMA, Boolean.valueOf(val));
    }

    public boolean is2ndPhase() {
        return this.entityData.get(SECOND_PHASE);
    }

    public void set2ndPhase(boolean val) {
        this.entityData.set(SECOND_PHASE, Boolean.valueOf(val));
    }

    public boolean isAnimationBeaming() {
        return this.entityData.get(ANIMATION_BEAMING);
    }

    public void setAnimationBeaming(boolean variant) {
        this.entityData.set(ANIMATION_BEAMING, Boolean.valueOf(variant));
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void define(CallbackInfo ci) {
        this.entityData.define(GAMMA, false);
        this.entityData.define(SECOND_PHASE, false);
        this.entityData.define(ANIMATION_BEAMING, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void add(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Gamma", this.isGamma());
        compound.putBoolean("2ndPhase", this.is2ndPhase());
        compound.putBoolean("AnimationBeaming", this.isAnimationBeaming());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void read(CompoundTag compound, CallbackInfo ci) {
        this.setGamma(compound.getBoolean("Gamma"));
        this.set2ndPhase(compound.getBoolean("2ndPhase"));
        this.setAnimationBeaming(compound.getBoolean("AnimationBeaming"));
    }

    @Override
    public void die(DamageSource pCause) {
        if(isGamma() && !isTame() && !isBaby() && pCause.getEntity() instanceof Player){
            ACEUtils.awardAdvancement(pCause.getEntity(), "gamma_tremorzilla_kill", "gamma_kill");
        }
        super.die(pCause);
    }

    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            amount *= 0.35F;
        }

        float determiner = this.getMaxHealth() / 3;

        if (amount > determiner && this.getHealth() > determiner && !is2ndPhase() && isGamma() && AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get()){
            setAnimationBeaming(true);
            this.beamServerTarget = createInitialBeamVec();
            this.setMaxBeamBreakLength(180F);
            setAnimation(ANIMATION_ROAR_2);
            setTarget(null);
            setLastHurtByMob(null);
            set2ndPhase(true);
            return super.hurt(source, this.getHealth() - determiner);
        }
        return super.hurt(source, amount);
    }

    int spitDesire = 0;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/DinosaurEntity;tick()V"))
    private void tick(CallbackInfo ci) {

        if (AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get()){
            LivingEntity prevTarget = null;

            if (prevTarget != getTarget()){
                prevTarget = getTarget();
            }


            Level level = this.level();
            if (this.getHealth() == this.getMaxHealth()){
                this.set2ndPhase(false);
            }

            LivingEntity living = getTarget();
            if (living != null && spitDesire >= 200 && !isAnimationBeaming() && beamProgress == 0 && is2ndPhase()) {


                if (getAnimation() == TremorzillaEntity.NO_ANIMATION){
                    setAnimation(TremorzillaEntity.ANIMATION_SPEAK);
                }

                if (getAnimation() == TremorzillaEntity.ANIMATION_SPEAK && getAnimationTick() == 10){
                    for (int repeat = 0; repeat < 3; repeat++) {
                        TephraEntity tephra = ACEntityRegistry.TEPHRA.get().create(level);
                        tephra.setPos(getBeamShootFrom(1).add(this.getLookAngle()));
                        tephra.setMaxScale(0.5F + level.random.nextFloat());
                        Vec3 targetVec = living.position().subtract(getBeamShootFrom(1));
                        tephra.setArcingTowards(living.getUUID());
                        double d4 = Math.sqrt(targetVec.x * targetVec.x + targetVec.z * targetVec.z);
                        double d5 = 0;
                        tephra.shoot(targetVec.x, targetVec.y + 0.5F + d4 * 0.75F + d5, targetVec.z, (float) (d4 * 0.1F + d5), 1 + level.random.nextFloat() * 0.5F);
                        level.addFreshEntity(tephra);
                    }
                    spitDesire = 0;
                }
            }

            if (getAnimation() == ANIMATION_ROAR_2 && getAnimationTick() >= 20 && getAnimationTick() <= 45 && isAnimationBeaming()){
                setFiring(true);
            } else if (isAnimationBeaming() && getAnimation() == ANIMATION_ROAR_2 && getAnimationTick() >= 45){
                sound = false;
                setFiring(false);
                this.playSound(ACSoundRegistry.TREMORZILLA_BEAM_END.get(), 8.0F, 1.0F);
                if (getAnimationTick() >= 55) {
                    setTarget(prevTarget);
                    int rotate = random.nextInt(0, 361);
                    for (int i = 0;i < 7;i++) {
                        rotate = rotate + 51;
                        Vec3 vec3 = new Vec3(this.getX() + Math.cos(rotate * 10) * 18, this.getY(), this.getZ() + Math.sin(rotate * 10) * 18);

                        LightningBolt lightningBolt = LIGHTNING_BOLT.create(level);
                        if (lightningBolt != null) {
                            lightningBolt.setPos(vec3);
                            level.addFreshEntity(lightningBolt);
                        }

                    }
                    beamTime = 0;
                    this.beamServerTarget = null;
                    this.setBeamEndPosition(null);
                    setAnimationBeaming(false);
                    this.setCharge(0);
                }
            }

            if (!sound && getAnimation() == ANIMATION_ROAR_2 && getAnimationTick() == 15 && isAnimationBeaming()){
                this.playSound(ACSoundRegistry.TREMORZILLA_ROAR.get(), 8.0F, 1.0F);
                sound = true;
            }

            spitDesire++;
        }

        if (!AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && isGamma()) {
            this.setGamma(false);
            this.set2ndPhase(false);
        }
    }


    @ModifyArg(method = "tickBreath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V",ordinal = 2))
    private ParticleOptions protonAddition(ParticleOptions pParticleData) {
        return (AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && isGamma() ? ACEParticleRegistry.TREMORZILLA_GAMMA_PROTON.get() : this.getAltSkin() == 2 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_TECTONIC_PROTON.get() : (this.getAltSkin() == 1 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_RETRO_PROTON.get() : (ParticleOptions)ACParticleRegistry.TREMORZILLA_PROTON.get()));
    }

    @ModifyArg(method = "tickBreath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V",ordinal = 0))
    private ParticleOptions explosionAddition(ParticleOptions pParticleData) {
        return (AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && isGamma() ? ACEParticleRegistry.TREMORZILLA_GAMMA_EXPLOSION.get() : this.getAltSkin() == 2 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_TECTONIC_EXPLOSION.get() : (this.getAltSkin() == 1 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_RETRO_EXPLOSION.get() : (ParticleOptions)ACParticleRegistry.TREMORZILLA_EXPLOSION.get()));
    }

    @ModifyArg(method = "tickBreath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V",ordinal = 1))
    private ParticleOptions lightningAddition(ParticleOptions pParticleData) {
        return (AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && isGamma() ? ACEParticleRegistry.TREMORZILLA_GAMMA_LIGHTNING.get() : this.getAltSkin() == 2 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_TECTONIC_LIGHTNING.get() : (this.getAltSkin() == 1 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_RETRO_LIGHTNING.get() : (ParticleOptions)ACParticleRegistry.TREMORZILLA_LIGHTNING.get()));
    }

    @ModifyConstant(method = "tick",constant = @Constant(intValue = 100,ordinal = 1))
    private int beamTime(int amount) {
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && isGamma() ? 250 : amount;
    }

    @ModifyConstant(method = "tick",constant = @Constant(floatValue = 100.0F,ordinal = 0))
    private float beamLength(float constant) {
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && isGamma() ? 180 : constant;
    }

    @ModifyConstant(method = "hurtEntitiesAround",constant = @Constant(intValue = 2),remap = false)
    private int modifyIrradiation(int amount) {
        return AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && isGamma() ? 6 : amount;
    }

    @Inject(method = "hurtEntitiesAround", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;knockbackTarget(Lnet/minecraft/world/entity/Entity;DDDZ)V"),locals = LocalCapture.CAPTURE_FAILSOFT,remap = false)
    private void mobInteract(Vec3 center, float radius, float damageAmount, float knockbackAmount, boolean radioactive, boolean hurtsOtherKaiju, boolean stretchY, CallbackInfoReturnable<Boolean> cir, AABB aabb, boolean flag, DamageSource damageSource, Iterator var11, LivingEntity living) {
        if (AlexsCavesExemplified.COMMON_CONFIG.GAMMARATED_TREMORZILLA_ENABLED.get() && isGamma()){
            living.setRemainingFireTicks(1000);
        }
    }









}
