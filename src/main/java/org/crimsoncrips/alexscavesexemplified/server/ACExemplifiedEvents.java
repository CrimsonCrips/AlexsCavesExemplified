package org.crimsoncrips.alexscavesexemplified.server;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.*;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.entity.item.MeltedCaramelEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.HazmatArmorItem;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.crimsoncrips.alexscavesexemplified.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.compat.CreateCompat;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;
import org.crimsoncrips.alexscavesexemplified.misc.ACEDamageTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.*;

import static com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity.ANIMATION_DRINK_BARREL;
import static net.minecraft.world.entity.EntityType.*;


@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ACExemplifiedEvents {

    private static final AttributeModifier FAST_FALLING = new AttributeModifier(UUID.randomUUID(), "Fast falling acceleration reduction", 0.1, AttributeModifier.Operation.ADDITION); // Add -0.07 to 0.08 so we get the vanilla default of 0.01


    @SubscribeEvent
    public void onEntityFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        final var entity = event.getEntity();

        if (entity instanceof GumbeeperEntity gumbeeper){
            if (entity.getRandom().nextDouble() < ACExemplifiedConfig.CHARGED_CAVE_CREEPER_CHANCE){
                gumbeeper.setCharged(true);
            }
        }
        if (entity instanceof NucleeperEntity nucleeper){
            if (entity.getRandom().nextDouble() < ACExemplifiedConfig.CHARGED_CAVE_CREEPER_CHANCE){
                nucleeper.setCharged(true);
            }
        }
        if (ACExemplifiedConfig.RABIES_ENABLED && entity.getRandom().nextDouble() < 0.05){
            if (entity instanceof CorrodentEntity || entity instanceof UnderzealotEntity || entity instanceof VesperEntity){
                entity.addEffect(new MobEffectInstance(ACEEffects.RABIAL.get(), 140000, 0));
            }
        }

    }

    @SubscribeEvent
    public void onLevelJoin(EntityJoinLevelEvent event) {
        final var entity = event.getEntity();

        if (entity.getType().is(ACExexmplifiedTagRegistry.CAN_RABIES) && entity instanceof Mob mob && ACExemplifiedConfig.RABIES_ENABLED){
            mob.targetSelector.addGoal(2, new MobTarget3DGoal(mob, LivingEntity.class, false,10, livingEntity -> {
                return livingEntity.getType() != entity.getType();
            }){
                @Override
                public boolean canUse() {
                    return super.canUse() && mob.hasEffect(ACEEffects.RABIAL.get());
                }
            });
        }

    }


    @SubscribeEvent
    public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockState blockState = event.getEntity().level().getBlockState(event.getPos());
        BlockPos pos = event.getPos();
        Level worldIn = event.getLevel();
        RandomSource random = event.getEntity().getRandom();
        Player player = event.getEntity();

        if (ACExemplifiedConfig.GLUTTONY_ENABLED) {
            if (blockState.is(ACExexmplifiedTagRegistry.CONSUMABLE_BLOCKS)) {
                ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, blockState);

                if (player.isCrouching()) {
                    MobEffectInstance hunger = player.getEffect(MobEffects.HUNGER);
                    if (hunger != null) {
                        if (!hunger.isInfiniteDuration()) {
                            player.removeEffect(MobEffects.HUNGER);
                            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, hunger.getDuration() - 60, hunger.getAmplifier()));
                        }
                        worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 1);
                        player.getFoodData().eat(1, 1);
                        player.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                        for (int i = 0; i <= 15; i++) {
                            Vec3 lookAngle = player.getLookAngle();
                            worldIn.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 1.5, lookAngle.y * 2, lookAngle.z * 1.5);
                        }
                        if (random.nextDouble() < 0.01)
                            player.addEffect(new MobEffectInstance(ACEffectRegistry.SUGAR_RUSH.get(), 100, 0));


                    } else if (player.getFoodData().needsFood()) {
                        player.getFoodData().eat(2, 2);
                        player.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                        for (int i = 0; i <= 15; i++) {
                            Vec3 lookAngle = player.getLookAngle();
                            worldIn.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 1.5, lookAngle.y * 2, lookAngle.z * 1.5);
                        }
                        if (random.nextDouble() < 0.01)
                            player.addEffect(new MobEffectInstance(ACEffectRegistry.SUGAR_RUSH.get(), 100, 0));

                    }
                }
            }



        }

    }

    @SubscribeEvent
    public void onInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        RandomSource random = player.getRandom();
        if (event.getTarget() instanceof GingerbreadManEntity gingerbreadMan) {

            if (itemStack.getItem() instanceof AxeItem && ACExemplifiedConfig.AMPUTATION_ENABLED) {
                if (gingerbreadMan.hasBothLegs()) {
                    gingerbreadMan.hurt(player.damageSources().mobAttack(player), 0.5F);
                    gingerbreadMan.setLostLimb(gingerbreadMan.getRandom().nextBoolean(), false, true);
                    player.swing(player.getUsedItemHand());
                } else if (gingerbreadMan.getRandom().nextInt(2) == 0) {
                    player.swing(player.getUsedItemHand());
                    gingerbreadMan.hurt(player.damageSources().mobAttack(player), 0.5F);
                    gingerbreadMan.setLostLimb(gingerbreadMan.getRandom().nextBoolean(), true, true);
                }

            }
            if (ACExemplifiedConfig.GLUTTONY_ENABLED && player.isCrouching()) {
                MobEffectInstance hunger = player.getEffect(MobEffects.HUNGER);
                if (hunger != null) {
                    event.getTarget().discard();
                    player.playSound(SoundEvents.GENERIC_EAT, 1.0F, -2F);
                }
                player.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            }

        }

        if(event.getTarget() instanceof Parrot parrot && ACExemplifiedConfig.COOKIE_CRUMBLE_ENABLED){
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }

            parrot.addEffect(new MobEffectInstance(MobEffects.POISON, 900));
            parrot.hurt(player.damageSources().playerAttack(player), Float.MAX_VALUE);
            parrot.level().explode(player,parrot.getX(),parrot.getY(),parrot.getZ(),3, Level.ExplosionInteraction.MOB);
        }

        if (event.getTarget() instanceof CandicornEntity candicornEntity) {
            if (itemStack.is(ACItemRegistry.CARAMEL_APPLE.get()) && ACExemplifiedConfig.AMPUTATION_ENABLED) {
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                candicornEntity.heal(4);
                player.swing(event.getHand());
            }
        }

    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent deathEvent) {
        LivingEntity died = deathEvent.getEntity();
        Level level = died.level();

        if (died instanceof NucleeperEntity nucleeper && ACExemplifiedConfig.NUCLEAR_CHAIN_ENABLED){
            if (deathEvent.getSource().is(DamageTypes.PLAYER_EXPLOSION) || deathEvent.getSource().is(DamageTypes.EXPLOSION) || deathEvent.getSource().is(ACDamageTypes.NUKE) || deathEvent.getSource().is(ACDamageTypes.TREMORZILLA_BEAM)){
                NuclearExplosionEntity explosion = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(nucleeper.level());
                explosion.copyPosition(nucleeper);
                explosion.setSize(nucleeper.isCharged() ? 1.75F : 1F);
                if(!nucleeper.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)){
                    explosion.setNoGriefing(true);
                }
                nucleeper.level().addFreshEntity(explosion);
            }
        }

        if (died instanceof Player player && ACExemplifiedConfig.MUTATED_DEATH_ENABLED) {
            MobEffectInstance irradiated = player.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && irradiated.getAmplifier() >= 2) {
                ACEntityRegistry.BRAINIAC.get().spawn((ServerLevel) level, BlockPos.containing(player.getX(), player.getY(), player.getZ()), MobSpawnType.MOB_SUMMONED);
            }
        }

        if (ACExemplifiedConfig.FISH_MUTATION_ENABLED && died.getFeetBlockState().is(ACBlockRegistry.ACID.get()) && died.getType().is(ACExexmplifiedTagRegistry.FISH)  && !died.level().isClientSide() && died.getRandom().nextDouble() < 1){
            ACEntityRegistry.RADGILL.get().spawn((ServerLevel) level, BlockPos.containing(died.getX(), died.getY(), died.getZ()), MobSpawnType.MOB_SUMMONED);
            died.discard();
        }
        if (ACExemplifiedConfig.CAT_MUTATION_ENABLED && died.getFeetBlockState().is(ACBlockRegistry.ACID.get()) && died.getType().is(ACExexmplifiedTagRegistry.CAT)  && !died.level().isClientSide() && died.getRandom().nextDouble() < 1){
            ACEntityRegistry.RAYCAT.get().spawn((ServerLevel) level, BlockPos.containing(died.getX(), died.getY(), died.getZ()), MobSpawnType.MOB_SUMMONED);
            died.discard();
        }

    }



    @SubscribeEvent
    public void blockBreak(BlockEvent.BreakEvent breakEvent){
        BlockState blockState = breakEvent.getState();
        Level level = (Level) breakEvent.getLevel();
        Player player = breakEvent.getPlayer();
        if (ACExemplifiedConfig.BURST_OUT_ENABLED) {
            if (blockState.is(ACExexmplifiedTagRegistry.BURST_BLOCKS) && breakEvent.getLevel().getRandom().nextDouble() < 0.02) {
                if (level.getBiome(breakEvent.getPos()).is(ACBiomeRegistry.FORLORN_HOLLOWS)){
                    if (level.getRandom().nextBoolean()) {
                        ACEntityRegistry.UNDERZEALOT.get().spawn((ServerLevel) level, BlockPos.containing(breakEvent.getPos().getX(), breakEvent.getPos().getY(), breakEvent.getPos().getZ()), MobSpawnType.MOB_SUMMONED);
                    } else {
                        ACEntityRegistry.CORRODENT.get().spawn((ServerLevel) level, BlockPos.containing(breakEvent.getPos().getX(), breakEvent.getPos().getY(), breakEvent.getPos().getZ()), MobSpawnType.MOB_SUMMONED);
                    }
                }

            }
            if (blockState.is(ACBlockRegistry.PEERING_COPROLITH.get()) && breakEvent.getLevel().getRandom().nextDouble() < 0.4){
                if (level.getBiome(breakEvent.getPos()).is(ACBiomeRegistry.FORLORN_HOLLOWS)){
                    ACEntityRegistry.CORRODENT.get().spawn((ServerLevel) level, BlockPos.containing(breakEvent.getPos().getX(), breakEvent.getPos().getY(), breakEvent.getPos().getZ()), MobSpawnType.MOB_SUMMONED);
                }
            }
        }

        if (ACExemplifiedConfig.ECOLOGICAL_REPUTATION_ENABLED) {
            if (blockState.is(ACExexmplifiedTagRegistry.ABYSSAL_ECOSYSTEM) && level.getBiome(breakEvent.getPos()).is(ACBiomeRegistry.ABYSSAL_CHASM)) {
                for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(30))) {
                    if (entity instanceof DeepOneBaseEntity deepOneBaseEntity) {
                        deepOneBaseEntity.addReputation(player.getUUID(),-1);
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public void mobBreathe(LivingBreatheEvent livingTickEvent) {
        LivingEntity livingEntity = livingTickEvent.getEntity();
        Level level = livingEntity.level();
        if (ACExemplifiedConfig.PRIMORDIAL_OXYGEN_ENABLED && livingEntity instanceof Player player && level.getBiome(player.getOnPos()).is(ACBiomeRegistry.PRIMORDIAL_CAVES)){
            livingTickEvent.setConsumeAirAmount(livingTickEvent.getConsumeAirAmount() + 2);
        }

        if (ACExemplifiedConfig.ABYSSAL_CRUSH_ENABLED && livingEntity instanceof Player player && level.getBiome(player.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM)){
            int aboveWater = 0;
            BlockPos pos = new BlockPos(player.getBlockX(), (int) (player.getBlockY() + 2),player.getBlockZ());
            while (level.getBlockState(pos).is(Blocks.WATER)){
                pos = pos.above();
                aboveWater++;
            }

            int diving = getDivingAmount(livingEntity);
            if (level.random.nextDouble() < (0.1 - (0.020 * diving))){
                if (aboveWater > 50 && diving < 10){
                    livingTickEvent.setConsumeAirAmount(livingTickEvent.getConsumeAirAmount() + (int) (0.025 * (aboveWater - 40)));
                }
            }

        }


    }

    @SubscribeEvent
    public void mobTickEvents(LivingEvent.LivingTickEvent livingTickEvent){
        LivingEntity livingEntity = livingTickEvent.getEntity();
        Level level = livingEntity.level();

        if (livingEntity.getPersistentData().getBoolean("WastePowered")){
            //taken from Dreadbow's particle making
            Vec3 particlePos = livingEntity.position().add((level.random.nextFloat() - 0.5F) * 2.5F, 0F, (level.random.nextFloat() - 0.5F) * 2.5F);
            level.addParticle(ACParticleRegistry.PROTON.get(), particlePos.x, particlePos.y, particlePos.z, livingEntity.getX(), livingEntity.getY(0.5F), livingEntity.getZ());
        }

        if (livingEntity instanceof SeaPigEntity seaPigEntity && level.random.nextDouble() < 0.01 && ACExemplifiedConfig.POISONOUS_SKIN_ENABLED) {
            for (LivingEntity entity : seaPigEntity.level().getEntitiesOfClass(LivingEntity.class, seaPigEntity.getBoundingBox().inflate(1.2))) {
                if (entity != seaPigEntity && entity.getBbHeight() <= 3.5F && !(entity instanceof SeaPigEntity)) {
                    entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
                }
            }
        }

        if (livingEntity instanceof Player player && level.getBiome(player.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM) && ACExemplifiedConfig.ABYSSAL_CRUSH_ENABLED){
            int aboveWater = 0;
            BlockPos pos = new BlockPos(player.getBlockX(), (int) (player.getBlockY() + 2),player.getBlockZ());
            while (level.getBlockState(pos).is(Blocks.WATER)){
                pos = pos.above();
                aboveWater++;
            }
            int diving = getDivingAmount(livingEntity);

            if (level.random.nextDouble() < (0.1 - (0.020 * diving))){
                if (aboveWater > 50 && diving < 10){
                    player.hurt(ACEDamageTypes.causeDepthDamage(level.registryAccess()), (float) (0.025 * (aboveWater - 40)));
                }
            }
        }


        if (livingEntity instanceof Player player) {
            for (int x = -1; x < 2; x++) {
                for (int y = 0; y < 3; y++) {
                    for (int z = -1; z < 2; z++) {
                        BlockPos pickedBlock = new BlockPos(player.getBlockX() + x, player.getBlockY() + y , player.getBlockZ() + z);
                        BlockState blockState = level.getBlockState(pickedBlock);
                        if (ACExemplifiedConfig.PEERING_TRIGGER_ENABLED && blockState.is(ACBlockRegistry.PEERING_COPROLITH.get()) && (livingEntity.isHolding(Ingredient.of(ACExexmplifiedTagRegistry.LIGHT)) || curiosLight(player)) && level.random.nextDouble() < 0.1) {
                            if (player.getRandom().nextDouble() < 0.9) {
                                level.setBlock(pickedBlock, ACBlockRegistry.POROUS_COPROLITH.get().defaultBlockState(), 3);
                            } else if (!level.isClientSide) {
                                level.destroyBlock(pickedBlock, true, player);
                                ACEntityRegistry.CORRODENT.get().spawn((ServerLevel) level, pickedBlock, MobSpawnType.MOB_SUMMONED);
                            }
                        }
                        if (ACExemplifiedConfig.RADIOACTIVE_AWARENESS_ENABLED && blockState.is(ACExexmplifiedTagRegistry.RADIOACTIVE) && HazmatArmorItem.getWornAmount(player) < 4 && level.random.nextDouble() < 0.5) {
                            player.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 200, 0));
                        }
                        if (blockState.getBlock() instanceof ActivatedByAltar && livingEntity.isHolding(ACItemRegistry.PEARL.get()) && level.random.nextDouble() < 0.5) {
                            level.setBlockAndUpdate(pickedBlock, blockState.setValue(ActivatedByAltar.ACTIVE, true));
                            if (level.random.nextDouble() < 0.05){
                                level.playLocalSound(pickedBlock, ACSoundRegistry.ABYSSMARINE_GLOW_ON.get(), SoundSource.BLOCKS, 1.5F, level.random.nextFloat() * 0.4F + 0.8F, false);
                            }
                        }
                    }
                }
            }

        }

        if (ACExemplifiedConfig.HEAVY_GRAVITY_ENABLED){
            AttributeInstance gravity = livingEntity.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
            boolean flag = livingEntity.isHolding(ACItemRegistry.HEAVYWEIGHT.get());

            if (flag) {
                if (!gravity.hasModifier(FAST_FALLING)) gravity.addTransientModifier(FAST_FALLING);
            } else if (gravity.hasModifier(FAST_FALLING)) {
                gravity.removeModifier(FAST_FALLING);
            }
        }


        if (ACExemplifiedConfig.DISORIENTED_ENABLED && !level.isClientSide && livingEntity instanceof WatcherEntity watcherEntity){
            Entity possessedEntity = watcherEntity.getPossessedEntity();
            if (possessedEntity != null && possessedEntity.isAlive()) {
                if (possessedEntity.getId() != watcherEntity.getId() && possessedEntity instanceof Player player) {
                    player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0));
                }
            }
        }

        if (ACExemplifiedConfig.GUM_TRAMPLE_ENABLED && livingEntity instanceof GumWormEntity gumWorm && gumWorm.isVehicle() && gumWorm.getFirstPassenger() instanceof Player){
            for (LivingEntity entity : gumWorm.level().getEntitiesOfClass(LivingEntity.class, gumWorm.getBoundingBox().inflate(1.2))) {
                if (entity != gumWorm && entity.getBbHeight() <= 3.5F) {
                    entity.hurt(gumWorm.damageSources().mobAttack(gumWorm), 1.0F);
                }
            }
        }

        if (ACExemplifiedConfig.WASTE_POWERUP_ENABLED && livingEntity instanceof BrainiacEntity brainiac){
            if (!level.isClientSide && brainiac.hasBarrel()) {
                if (brainiac.getAnimation() == ANIMATION_DRINK_BARREL && brainiac.getAnimationTick() >= 60) {
                    brainiac.getPersistentData().putBoolean("WastePowered", true);
                }
            }
        }


        if (ACExemplifiedConfig.PRESSURED_HOOKS_ENABLED){
            if (livingEntity instanceof Player player) {
                boolean trueMainhand = livingEntity.getMainHandItem().is(ACItemRegistry.CANDY_CANE_HOOK.get());
                boolean trueOffhand = livingEntity.getOffhandItem().is(ACItemRegistry.CANDY_CANE_HOOK.get());
                if (livingEntity.getVehicle() instanceof GumWormSegmentEntity && trueMainhand) {
                    if (player.isCreative())
                        return;
                    if (!(player.getRandom().nextDouble() < 0.05))
                        return;
                    player.getMainHandItem().hurtAndBreak(1, player, (p_233654_0_) -> {
                    });
                }
                if (livingEntity.getVehicle() instanceof GumWormSegmentEntity && trueOffhand) {
                    if (player.isCreative())
                        return;
                    if (!(player.getRandom().nextDouble() < 0.05))
                        return;
                    player.getOffhandItem().hurtAndBreak(1, player, (p_233654_0_) -> {
                    });
                }

                if ((!trueMainhand || !trueOffhand) && livingEntity.getVehicle() instanceof GumWormSegmentEntity && ACExemplifiedConfig.LOGICAL_RIDING_ENABLED) {
                    livingEntity.removeVehicle();
                }
            }
        }

        if (ACExemplifiedConfig.AMBER_HEAL_ENABLED && livingEntity.getFeetBlockState().is(ACBlockRegistry.AMBER.get()) && livingEntity.getRandom().nextDouble() < 0.01){
            if (livingEntity.getMobType() == MobType.UNDEAD){
                livingEntity.hurt(livingEntity.damageSources().generic(),2);
            } else livingEntity.heal(2);
        }

        if (ACExemplifiedConfig.GUANO_SLOW_ENABLED && (livingEntity.getFeetBlockState().is(ACBlockRegistry.GUANO_BLOCK.get()) || livingEntity.getFeetBlockState().is(ACBlockRegistry.GUANO_LAYER.get()))){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 0));
        }

        if (ACExemplifiedConfig.SOLIDIFIED_WATCHER_ENABLED && livingEntity instanceof WatcherEntity watcherEntity){
            if (watcherEntity.tickCount > 12000){
                BlockPos blockPos = watcherEntity.getOnPos().above();

                for (int i = 0; i < 4; i++){
                    BlockPos blockPlacing = new BlockPos(blockPos.getX(),blockPos.getY() + i,blockPos.getZ());
                    level.setBlock(blockPlacing,ACBlockRegistry.THORNWOOD_WOOD.get().defaultBlockState(),2);
                    if (i == 3){
                        if (watcherEntity.getDirection() == Direction.WEST || watcherEntity.getDirection() == Direction.EAST){
                            level.setBlock(blockPlacing.north(),ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING,Direction.NORTH),2);
                            level.setBlock(blockPlacing.south(),ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING,Direction.SOUTH),2);
                        } else {
                            level.setBlock(blockPlacing.east(),ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING,Direction.EAST),2);
                            level.setBlock(blockPlacing.west(),ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING,Direction.WEST),2);
                        }
                    }
                    if (!(level.random.nextDouble() < 1))
                        return;
                    level.setBlock(new BlockPos(blockPlacing.above()),ACBlockRegistry.BEHOLDER.get().defaultBlockState(),2);


                }
                watcherEntity.playSound(ACSoundRegistry.WATCHER_DEATH.get(), 6F, -5F);
                watcherEntity.discard();

            }
        }
        


        if (ACExemplifiedConfig.STICKY_SODA_ENABLED && livingEntity.getFeetBlockState().is(ACBlockRegistry.PURPLE_SODA.get()) && !livingEntity.getType().is(ACTagRegistry.CANDY_MOBS)){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 90, 0));
        }

        if (ACExemplifiedConfig.PURPLE_LEATHERED_ENABLED) {
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.HEAD),level,livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.FEET),level,livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.CHEST),level,livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.LEGS),level,livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.MAINHAND),level,livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.OFFHAND),level,livingEntity);
        }

        if (ACExemplifiedConfig.CRUMBY_RAGE_ENABLED){
            Iterator<GingerbreadManEntity> var4 = level.getEntitiesOfClass(GingerbreadManEntity.class, livingEntity.getBoundingBox().inflate(10, 5, 10)).iterator();
            while (var4.hasNext()) {
                LivingEntity entity = var4.next();
                if (entity instanceof GingerbreadManEntity gingerbreadMan && !gingerbreadMan.isOvenSpawned() && livingEntity.getUseItem().is(ACItemRegistry.GINGERBREAD_CRUMBS.get())) {
                    gingerbreadMan.setTarget(livingEntity);
                }
            }
        }

        if (ACExemplifiedConfig.FLY_TRAP_ENABLED  && ModList.get().isLoaded("alexsmobs")){
            BlockState blockState = livingEntity.getFeetBlockState();
            BlockPos blockPos = new BlockPos(livingEntity.getBlockX(),livingEntity.getBlockY(),livingEntity.getBlockZ());
            if (blockState.is(ACBlockRegistry.FLYTRAP.get())&& AMCompat.fly(livingEntity)){
                 if (blockState.getValue(PottedFlytrapBlock.OPEN)){
                     livingEntity.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                     livingEntity.captureDrops();
                     livingEntity.kill();
                     livingEntity.level().setBlock(blockPos, blockState.setValue(PottedFlytrapBlock.OPEN, false), 2);
                 }
            }
        }

        if(ACExemplifiedConfig.IRRADIATION_WASHOFF_ENABLED){
            MobEffectInstance irradiated = livingEntity.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && livingEntity.getRandom().nextDouble() < 0.05 && (livingEntity.isInWater() || livingEntity.getBlockStateOn().is(Blocks.WATER_CAULDRON) || livingEntity.isInWaterRainOrBubble())) {
                livingEntity.removeEffect(irradiated.getEffect());
                livingEntity.addEffect(new MobEffectInstance(irradiated.getEffect(), irradiated.getDuration() - 100, irradiated.getAmplifier()));
            }
        }

        BlockState blockState = livingEntity.getBlockStateOn();
        if (blockState.getBlock() instanceof GeothermalVentBlock){
            if (ACExemplifiedConfig.GEOTHERMAL_EFFECTS_ENABLED){
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 1){
                    if(ACExemplifiedConfig.IRRADIATION_WASHOFF_ENABLED){
                        MobEffectInstance irradiated = livingEntity.getEffect(ACEffectRegistry.IRRADIATED.get());
                        if (irradiated != null && livingEntity.getRandom().nextDouble() < 0.05) {
                            livingEntity.removeEffect(irradiated.getEffect());
                            livingEntity.addEffect(new MobEffectInstance(irradiated.getEffect(), irradiated.getDuration() - 100, irradiated.getAmplifier()));
                        }
                    }
                    if (livingEntity.isOnFire()) livingEntity.setRemainingFireTicks(livingEntity.getRemainingFireTicks() - 30);
                }
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 2){
                    livingEntity.setSecondsOnFire(5);
                }
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 3){
                    if(!livingEntity.hasEffect(ACEffectRegistry.IRRADIATED.get())){
                        livingEntity.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 400, 0));
                    }
                }
            }
        }

        if(ACExemplifiedConfig.EXEMPLIFIED_IRRADIATION_AMOUNT > 0){
            MobEffectInstance irradiated = livingEntity.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && irradiated.getAmplifier() >= ACExemplifiedConfig.EXEMPLIFIED_IRRADIATION_AMOUNT - 1) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));

                if (ModList.get().isLoaded("alexsmobs"))
                    livingEntity.addEffect(new MobEffectInstance(AMEffectRegistry.EXSANGUINATION.get(), 60, 0));
            }
        }

        if (ACExemplifiedConfig.RABIES_ENABLED && !level.isClientSide && livingEntity.isInWaterRainOrBubble() && livingEntity.hasEffect(ACEEffects.RABIAL.get())) {
            livingEntity.hurt(ACEDamageTypes.causeRabialWaterDamage(level.registryAccess()), 1.0F);
        }
    }

    @SubscribeEvent
    public void rightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level level = event.getLevel();

        ItemStack offHand = player.getOffhandItem();
        ItemStack mainHand = player.getMainHandItem();

        if(ACExemplifiedConfig.IRRADIATION_WASHOFF_ENABLED && ModList.get().isLoaded("supplementaries")){
            MobEffectInstance irradiated = player.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && mainHand.is(ModRegistry.SOAP.get()) && (player.isInWater() || player.getBlockStateOn().is(Blocks.WATER_CAULDRON))) {
                player.gameEvent(GameEvent.ITEM_INTERACT_START);
                player.removeEffect(irradiated.getEffect());
                player.addEffect(new MobEffectInstance(irradiated.getEffect(), irradiated.getDuration() - 1000, irradiated.getAmplifier()));

                for (int i = 0; i < 10; i++){
                    double d1 = player.getRandom().nextGaussian() * 0.02;
                    double d2 = player.getRandom().nextGaussian() * 0.02;
                    double d3 = player.getRandom().nextGaussian() * 0.02;

                    level.addParticle(ModParticles.SUDS_PARTICLE.get(), player.getX(), player.getY() + 0.5, player.getZ(), d1 * 2, d2 * 2, d3 * 2);
                }
            }
        }

        if (ACExemplifiedConfig.KIROV_REPORTING_ENABLED && player.isFallFlying() && (offHand.is(Items.FLINT_AND_STEEL) || offHand.is(Items.FIRE_CHARGE))){
            boolean clientSide = level.isClientSide;
            if (mainHand.is(Items.TNT)){
                if (clientSide){
                    player.getMainHandItem().shrink(player.isCreative() ? 0 : 1);
                    player.getCooldowns().addCooldown(player.getOffhandItem().getItem(), 60);
                    player.swing(InteractionHand.MAIN_HAND);
                } else {
                    mainHand.shrink(player.isCreative() ? 0 : 1);
                    player.getCooldowns().addCooldown(offHand.getItem(), 60);
                    TNT.spawn((ServerLevel) level, BlockPos.containing(player.getX(), player.getY(), player.getZ()), MobSpawnType.MOB_SUMMONED);
                }
            } else if (mainHand.is(Items.TNT_MINECART)){
                if (clientSide){
                    player.getMainHandItem().shrink(player.isCreative() ? 0 : 1);
                    player.getCooldowns().addCooldown(player.getOffhandItem().getItem(), 60);
                    player.swing(InteractionHand.MAIN_HAND);
                } else {
                    mainHand.shrink(player.isCreative() ? 0 : 1);
                    player.getCooldowns().addCooldown(offHand.getItem(), 60);
                    TNT_MINECART.spawn((ServerLevel) level, BlockPos.containing(player.getX(), player.getY(), player.getZ()), MobSpawnType.MOB_SUMMONED);
                }
            } else if (mainHand.is(ACBlockRegistry.NUCLEAR_BOMB.get().asItem())) {
                if (clientSide){
                    player.getMainHandItem().shrink(player.isCreative() ? 0 : 1);
                    player.getCooldowns().addCooldown(player.getOffhandItem().getItem(), 60);
                    player.swing(InteractionHand.MAIN_HAND);
                } else {
                    mainHand.shrink(player.isCreative() ? 0 : 1);
                    player.getCooldowns().addCooldown(offHand.getItem(), 60);
                    ACEntityRegistry.NUCLEAR_BOMB.get().spawn((ServerLevel) level, BlockPos.containing(player.getX(), player.getY(), player.getZ()), MobSpawnType.MOB_SUMMONED);
                }
            }

        }
    }

    @SubscribeEvent
    public void livingDamage(LivingDamageEvent livingDamageEvent) {
        Entity damager = livingDamageEvent.getSource().getEntity();
        LivingEntity damaged = livingDamageEvent.getEntity();


        if(ACExemplifiedConfig.RABIES_ENABLED && damager instanceof LivingEntity living && living.hasEffect(ACEEffects.RABIAL.get()) && damaged.getType().is(ACExexmplifiedTagRegistry.CAN_RABIES) && !damaged.hasEffect(MobEffects.DAMAGE_RESISTANCE)){
            damaged.addEffect(new MobEffectInstance(ACEEffects.RABIAL.get(), 72000, 0));
        }

        if(ACExemplifiedConfig.STICKY_CARAMEL_ENABLED && damager instanceof CaramelCubeEntity caramelCubeEntity && caramelCubeEntity.getRandom().nextDouble() < 0.5){
            MeltedCaramelEntity meltedCaramel = ACEntityRegistry.MELTED_CARAMEL.get().create(caramelCubeEntity.level());
            if (meltedCaramel == null)
                return;
            meltedCaramel.setPos(damaged.getPosition(1));
            meltedCaramel.setDespawnsIn(40 + ((1 + caramelCubeEntity.getSlimeSize()) - 1) * 40);
            meltedCaramel.setDeltaMovement(caramelCubeEntity.getDeltaMovement().multiply(-1.0F, 0.0F, -1.0F));
            caramelCubeEntity.level().addFreshEntity(meltedCaramel);
        }
    }

//   Future use

    @SubscribeEvent
    public void bonemealEvent(BonemealEvent bonemealEvent) {
        Entity entity = bonemealEvent.getEntity();
        Level level = entity.level();
        BlockPos blockPos = bonemealEvent.getPos();
        BlockState blockState = level.getBlockState(blockPos);
        RandomSource random = level.getRandom();
        BlockState grass = Blocks.GRASS.defaultBlockState();

        if (ACExemplifiedConfig.ECOLOGICAL_REPUTATION_ENABLED) {
            if (blockState.is(ACBlockRegistry.PING_PONG_SPONGE.get()) && level.getBiome(blockPos).is(ACBiomeRegistry.ABYSSAL_CHASM)) {
                for (LivingEntity deepOne : entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(30))) {
                    if (deepOne instanceof DeepOneBaseEntity deepOneBaseEntity && entity instanceof Player player) {
                        deepOneBaseEntity.addReputation(player.getUUID(),2);
                    }
                }
            }
        }

//        Optional<Holder.Reference<PlacedFeature>> optional = level.registryAccess().registryOrThrow(Registries.PLACED_FEATURE).getHolder(ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation("alexscaves", "primordial_caves_grass")));
//        label49:
//        for(int i = 0; i < 128; ++i) {
//            for(int $$9 = 0; $$9 < i / 16; ++$$9) {
//                blockAbove = blockAbove.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
//                if (!(level.getBlockState(blockAbove.below()).is(Blocks.GRASS_BLOCK) || level.getBlockState(blockAbove).isCollisionShapeFullBlock(level, blockAbove))) {
//                    continue label49;
//                }
//            }
//            BlockState blockState = level.getBlockState(blockAbove);
//            if (blockState.is(grass.getBlock()) && random.nextInt(10) == 0) {
//                ((BonemealableBlock)grass.getBlock()).performBonemeal((ServerLevel) level, random, blockAbove, blockState);
//            }
//            if (blockState.isAir()) {
//                Holder<PlacedFeature> $$12;
//                if (random.nextInt(8) == 0) {
//                    List<ConfiguredFeature<?, ?>> $$11 = (level.getBiome(blockAbove).value()).getGenerationSettings().getFlowerFeatures();
//                    if ($$11.isEmpty()) {
//                        continue;
//                    }
//                    $$12 = ((RandomPatchConfiguration)((ConfiguredFeature)$$11.get(0)).config()).feature();
//                } else {
//                    if (!optional.isPresent()) {
//                        continue;
//                    }
//                    $$12 = (Holder)optional.get();
//                }
//                ServerLevel serverLevel = (ServerLevel) level;
//                ($$12.value()).place(serverLevel, serverLevel.getChunkSource().getGenerator(), random, blockAbove);
//            }
//        }
    }

    private void checkLeatherArmor(ItemStack item, Level level, LivingEntity living){
        if (item.getItem() instanceof DyeableLeatherItem dyeableLeatherItem && living.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get())) {
            dyeableLeatherItem.setColor(item, 0Xb839e6);
        }
    }

    public boolean curiosLight(Player player){
        if (ModList.get().isLoaded("curiouslanterns")) {
            ICuriosItemHandler handler = CuriosApi.getCuriosInventory(player).orElseThrow(() -> new IllegalStateException("Player " + player.getName() + " has no curios inventory!"));
            return handler.getStacksHandler("belt").orElseThrow().getStacks().getStackInSlot(0).is(ACExexmplifiedTagRegistry.LIGHT);
        } else return false;
    }

    public static int getDivingAmount(LivingEntity entity) {
        int i = 0;
        if (entity.getItemBySlot(EquipmentSlot.HEAD).is((Item)ACItemRegistry.DIVING_HELMET.get())) {
            ++i;
        }
        if (entity.getItemBySlot(EquipmentSlot.CHEST).is((Item)ACItemRegistry.DIVING_CHESTPLATE.get())) {
            ++i;
        }
        if (entity.getItemBySlot(EquipmentSlot.LEGS).is((Item)ACItemRegistry.DIVING_LEGGINGS.get())) {
            ++i;
        }
        if (entity.getItemBySlot(EquipmentSlot.FEET).is((Item)ACItemRegistry.DIVING_BOOTS.get())) {
            ++i;
        }
        i = i + CreateCompat.createDivingSuit(entity);
        return i;
    }


}
