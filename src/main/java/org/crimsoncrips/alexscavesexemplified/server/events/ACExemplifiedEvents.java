package org.crimsoncrips.alexscavesexemplified.server.events;






import java.util.List;
import org.crimsoncrips.alexscavesexemplified.backport.ACExCompat;
import net.minecraft.world.item.DyeableLeatherItem;
import org.crimsoncrips.alexscavesexemplified.backport.ACExItemData;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineHunt;
import net.minecraft.world.entity.animal.WaterAnimal;
import org.crimsoncrips.alexscavesexemplified.misc.ACExScheduler;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExRabial;
import java.util.UUID;
import org.crimsoncrips.alexscavesexemplified.mixins.mobs.ACExDeepOneAccessor;
import org.crimsoncrips.alexscavesexemplified.misc.ACExArmedUnderzealot;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;
import org.crimsoncrips.alexscavesexemplified.backport.PacketDistributor;
import org.crimsoncrips.alexscavesexemplified.networking.ACExRadiantConsumePacket;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Unit;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExDataComponents;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;
import com.github.alexmodguy.alexscaves.server.block.*;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.FrostmintSpearEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.MagneticWeaponEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.MeltedCaramelEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import org.crimsoncrips.alexscavesexemplified.backport.EntityTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.crimsoncrips.alexscavesexemplified.compat.SupplementariesCompat;
import org.crimsoncrips.alexscavesexemplified.datagen.ACExDamageTypes;
import org.crimsoncrips.alexscavesexemplified.server.ACExDepthCrush;
import org.crimsoncrips.alexscavesexemplified.datagen.loottables.ACExLootTables;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExBlockTagGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExEntityTagGenerator;
import org.crimsoncrips.alexscavesexemplified.misc.ACExMagnerip;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExSelfDestruct;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExSerened;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;


import static net.minecraft.world.entity.EntityType.*;


public class ACExemplifiedEvents {

    private static final double ARMED_UNDERZEALOT_CHANCE = 0.25D;
    private static final int IRRADIATED_CROP_TICKS = ACExUtils.seconds(10.0F);
    private static final int SERENE_HIT_COST = ACExUtils.seconds(5.0F);
    private static final double SUGAR_CRASH_CHANCE = 0.3D;
    private static final int GLUTTONY_HUNGER_CUT = ACExUtils.seconds(3.0F);
    private static final int KIROV_COOLDOWN = ACExUtils.seconds(3.0F);

    @SubscribeEvent
    public void onEntityFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        final var entity = event.getEntity();

        Double chargedChance = AlexsCavesExemplified.COMMON_CONFIG.CHARGED_CAVE_CREEPER_CHANCE.get();

        if (entity instanceof UnderzealotEntity underzealot && AlexsCavesExemplified.COMMON_CONFIG.ARMED_UNDERZEALOT_ENABLED.get() && underzealot.getRandom().nextDouble() < ARMED_UNDERZEALOT_CHANCE) {
            ACExData.set(underzealot, ACExAttachments.ARMED, true);
        }
        if (entity instanceof GumbeeperEntity gumbeeper){
            if (entity.getRandom().nextDouble() < chargedChance){
                gumbeeper.setCharged(true);
            }
        }
        if (entity instanceof NucleeperEntity nucleeper){
            if (entity.getRandom().nextDouble() < chargedChance){
                nucleeper.setCharged(true);
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get() && entity.getRandom().nextDouble() < 0.05){
            if (entity instanceof CorrodentEntity || entity instanceof UnderzealotEntity || entity instanceof VesperEntity){
                entity.addEffect(new MobEffectInstance(ACExEffects.RABIAL.get(), ACExUtils.seconds(7000.0F), 0));
            }
        }

    }



    @SubscribeEvent
    public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockState blockState = event.getEntity().level().getBlockState(event.getPos());
        BlockPos pos = event.getPos();
        Level worldIn = event.getLevel();
        RandomSource random = event.getEntity().getRandom();
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();


        if (AlexsCavesExemplified.COMMON_CONFIG.GLUTTONY_ENABLED.get() && blockState.is(ACExBlockTagGenerator.CONSUMABLE_BLOCKS) && player.isCrouching()) {
            MobEffectInstance hunger = player.getEffect(MobEffects.HUNGER);
            if (hunger != null || player.getFoodData().needsFood()) {
                player.swing(event.getHand());
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
                if (worldIn.isClientSide)
                    return;
                if (hunger != null) {
                    if (!hunger.isInfiniteDuration()) {
                        player.removeEffect(MobEffects.HUNGER);
                        if (hunger.getDuration() > GLUTTONY_HUNGER_CUT) {
                            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, hunger.getDuration() - GLUTTONY_HUNGER_CUT, hunger.getAmplifier()));
                        }
                    }
                    player.getFoodData().eat(1, 1);
                } else {
                    player.getFoodData().eat(2, 2);
                }
                worldIn.destroyBlock(pos, true, player);
                worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (worldIn instanceof ServerLevel serverLevel) {
                    Vec3 lookAngle = player.getLookAngle();
                    for (int i = 0; i <= 15; i++) {
                        serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState), player.getX(), player.getY() + 1.5, player.getZ(), 0, lookAngle.x * 1.5, lookAngle.y * 2, lookAngle.z * 1.5, 1.0D);
                    }
                }
                ACExUtils.awardAdvancement(player,"gluttony","eat");
                if (random.nextDouble() < 0.01)
                    player.addEffect(new MobEffectInstance(ACEffectRegistry.SUGAR_RUSH.get(), ACExUtils.seconds(5.0F), 0));
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.LIQUID_REPLICATION_ENABLED.get() && blockState.is(Blocks.CAULDRON) && (itemStack.is(ACBlockRegistry.SCRAP_METAL.get().asItem()) || itemStack.is(ACBlockRegistry.SCRAP_METAL_PLATE.get().asItem())) && player.isCrouching()){
            if (!worldIn.isClientSide) {
                worldIn.setBlock(pos, ACExBlockRegistry.METAL_CAULDRON.get().defaultBlockState(),3);
                ACExCompat.consume(itemStack, 1, player);
                worldIn.playSound(null, pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 1F, 1F);
                ACExUtils.awardAdvancement(player,"metal_cauldron","metal");
            }
            player.swing(event.getHand());
            event.setCancellationResult(InteractionResult.sidedSuccess(worldIn.isClientSide));
            event.setCanceled(true);
        }




    }

    @SubscribeEvent
    public void onInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        Level level = event.getLevel();
        Entity target = event.getTarget();


        if (AlexsCavesExemplified.COMMON_CONFIG.GLUTTONY_ENABLED.get() && level instanceof ServerLevel serverLevel && player.isCrouching() && player.getEffect(MobEffects.HUNGER) != null) {
            if (target instanceof GingerbreadManEntity gingerbread && !gingerbread.isOvenSpawned()) {
                eatSpray(serverLevel, player, new ItemParticleOption(ParticleTypes.ITEM, ACItemRegistry.GINGERBREAD_CRUMBS.get().getDefaultInstance()), 1);
                for (GingerbreadManEntity entity : level.getEntitiesOfClass(GingerbreadManEntity.class, gingerbread.getBoundingBox().inflate(10, 5, 10))) {
                    if (!entity.isOvenSpawned() && !player.isCreative()) {
                        entity.setTarget(player);
                    }
                }
                target.discard();
                player.getFoodData().eat(2, 0.0F);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 0.5F);
            }

            if (target instanceof CaramelCubeEntity caramelCube && caramelCube.getSlimeSize() <= 0) {
                eatSpray(serverLevel, player, new ItemParticleOption(ParticleTypes.ITEM, ACItemRegistry.CARAMEL.get().getDefaultInstance()), 1);
                player.getFoodData().eat(3, 0.0F);
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ACExUtils.seconds(3.0F), 0,false,false));
                target.discard();
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 0.5F);
            }

            if (target instanceof GummyBearEntity gummyBearEntity && gummyBearEntity.isBaby()) {
                ItemStack gelatinColor = switch (gummyBearEntity.getGummyColor()) {
                    case GREEN -> ACItemRegistry.GELATIN_GREEN.get().getDefaultInstance();
                    case BLUE -> ACItemRegistry.GELATIN_BLUE.get().getDefaultInstance();
                    case YELLOW -> ACItemRegistry.GELATIN_YELLOW.get().getDefaultInstance();
                    case PINK -> ACItemRegistry.GELATIN_PINK.get().getDefaultInstance();
                    default -> ACItemRegistry.GELATIN_RED.get().getDefaultInstance();
                };
                eatSpray(serverLevel, player, new ItemParticleOption(ParticleTypes.ITEM, gelatinColor), 16);
                player.getFoodData().eat(5, 0.0F);
                for (GummyBearEntity entity : level.getEntitiesOfClass(GummyBearEntity.class, player.getBoundingBox().inflate(10, 5, 10))) {
                    if (!player.isCreative()) {
                        entity.setTarget(player);
                    }
                }
                target.discard();
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 0.5F);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), ACSoundRegistry.GUMMY_BEAR_DEATH.get(), SoundSource.PLAYERS, 0.4F, 2.0F);
            }
        }


        if (event.getTarget() instanceof CandicornEntity candicornEntity && candicornEntity.isTame() && candicornEntity.getHealth() < candicornEntity.getMaxHealth() && itemStack.is(ACItemRegistry.CARAMEL_APPLE.get()) && AlexsCavesExemplified.COMMON_CONFIG.CANDICORN_HEAL_ENABLED.get()) {
            if (!level.isClientSide) {
                ACExCompat.consume(itemStack, 1, player);
                candicornEntity.heal(4);
            }
            player.swing(event.getHand());
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent deathEvent) {
        LivingEntity died = deathEvent.getEntity();
        Entity killer = deathEvent.getSource().getEntity();
        Level level = died.level();

        if (died instanceof MagnetronEntity magnetron && ACExData.get(magnetron, ACExAttachments.RIPPED_HEART) >= ACExUtils.HEART_RIP_TICKS && AlexsCavesExemplified.COMMON_CONFIG.MAGNETICISM_ENABLED.get()){
            float range = 5F;
            RandomSource random = magnetron.getRandom();
            Vec3 particlesFrom = magnetron.position().add(0, 0.2, 0);
            float particleMax = 10 + random.nextInt(5);
            if (level instanceof ServerLevel serverLevel) {
                for (int particles = 0; particles < particleMax; particles++) {
                    Vec3 vec3 = new Vec3((random.nextFloat() - 0.5) * 0.3F, (random.nextFloat() - 0.5) * 0.3F, range * 0.5F + range * 0.5F * random.nextFloat()).yRot((float) ((particles / particleMax) * Math.PI * 2)).add(particlesFrom);
                    if (random.nextBoolean()) {
                        serverLevel.sendParticles(ACParticleRegistry.SCARLET_SHIELD_LIGHTNING.get(), vec3.x, vec3.y, vec3.z, 0, particlesFrom.x, particlesFrom.y, particlesFrom.z, 1.0D);
                    } else {
                        serverLevel.sendParticles(ACParticleRegistry.AZURE_SHIELD_LIGHTNING.get(), particlesFrom.x, particlesFrom.y, particlesFrom.z, 0, vec3.x, vec3.y, vec3.z, 1.0D);
                    }
                }
            }
            level.explode(null, magnetron.blockPosition().getX() + 0.5D, magnetron.blockPosition().getY() + 0.5D, magnetron.blockPosition().getZ() + 0.5D, 1, false, Level.ExplosionInteraction.NONE);
            if (magnetron.isFormed()) {
                for (MagnetronPartEntity part : magnetron.allParts) {
                    if (part.getBlockState() != null && level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        BlockPos placeAt = part.blockPosition();
                        while (!level.getBlockState(placeAt).isAir() && placeAt.getY() < level.getMaxBuildHeight()) {
                            placeAt = placeAt.above();
                        }
                        FallingBlockEntity.fall(level, placeAt, part.getBlockState());
                        part.setBlockState(null);
                    }
                }
            }
        }

        if (died instanceof NucleeperEntity nucleeper && !level.isClientSide && AlexsCavesExemplified.COMMON_CONFIG.NUCLEAR_CHAIN_ENABLED.get() && !ACExData.get(nucleeper, ACExAttachments.DEFUSED) && !ACExData.get(nucleeper, ACExAttachments.CHAIN_DETONATED)){
            DamageSource source = deathEvent.getSource();
            if (source.is(DamageTypes.PLAYER_EXPLOSION) || source.is(DamageTypes.EXPLOSION) || source.is(ACDamageTypes.NUKE) || source.is(ACDamageTypes.TREMORZILLA_BEAM)){
                ACExUtils.spawnNucleeperNuke(level, nucleeper);
                ACExUtils.chainDetonate(level, nucleeper);
            }
        }

        if (died instanceof MineGuardianEntity mineGuardianEntity && ACExData.get(mineGuardianEntity, ACExAttachments.MINE_GUARDIAN_VARIANT) >= 1) {
            ACExUtils.awardAdvancement(killer,"nuclear_kill","killed");
        }

        if (killer instanceof Player player && AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get()) {
            if (died instanceof SeaPigEntity){
                ACExUtils.deepReputation(player,-1);
            } else if (died instanceof GossamerWormEntity){
                ACExUtils.deepReputation(player,-2);
            } else if (died instanceof LanternfishEntity && player.getRandom().nextDouble() < 0.3){
                ACExUtils.deepReputation(player,-1);
            } else if (died instanceof TripodfishEntity){
                ACExUtils.deepReputation(player,-1);
            } else if (died instanceof HullbreakerEntity){
                ACExUtils.awardAdvancement(player,"hullbreaker_reputation","killed");
                ACExUtils.deepReputation(player,-20);
            } else if (died instanceof MineGuardianEntity){
                ACExUtils.deepReputation(player,2);
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.FISH_MUTATION_ENABLED.get() && deathEvent.getSource().is(ACDamageTypes.ACID) && died.getType().is(ACExEntityTagGenerator.ACID_TO_FISH) && !died.level().isClientSide() && died.getRandom().nextDouble() < 0.2){
            ACEntityRegistry.RADGILL.get().spawn((ServerLevel) level, BlockPos.containing(died.getX(), died.getY(), died.getZ()), MobSpawnType.MOB_SUMMONED);
            for (Player players : level.getEntitiesOfClass(Player.class, died.getBoundingBox().inflate(8))) {
                ACExUtils.awardAdvancement(players,"convert_fish","convert");
            }
            died.discard();
        }


        if (AlexsCavesExemplified.COMMON_CONFIG.CAT_MUTATION_ENABLED.get() && deathEvent.getSource().is(ACDamageTypes.ACID) && died.getType().is(ACExEntityTagGenerator.ACID_TO_CAT)  && !died.level().isClientSide() && died.getRandom().nextDouble() < 0.2){
            ACEntityRegistry.RAYCAT.get().spawn((ServerLevel) level, BlockPos.containing(died.getX(), died.getY(), died.getZ()), MobSpawnType.MOB_SUMMONED);
            for (Player players : level.getEntitiesOfClass(Player.class, died.getBoundingBox().inflate(8))) {
                ACExUtils.awardAdvancement(players,"convert_cat","convert");
            }
            died.discard();
        }

    }

    @SubscribeEvent
    public void blockBreak(BlockEvent.BreakEvent breakEvent){
        BlockState blockState = breakEvent.getState();
        Level level = (Level) breakEvent.getLevel();
        Player player = breakEvent.getPlayer();
        if (AlexsCavesExemplified.COMMON_CONFIG.BURST_OUT_ENABLED.get()) {
            if (blockState.is(ACExBlockTagGenerator.BURST_BLOCKS) && breakEvent.getLevel().getRandom().nextDouble() < 0.02) {
                if (level.getBiome(breakEvent.getPos()).is(ACBiomeRegistry.FORLORN_HOLLOWS)){
                    if (level.getRandom().nextBoolean()) {
                        ACEntityRegistry.UNDERZEALOT.get().spawn((ServerLevel) level, BlockPos.containing(breakEvent.getPos().getX(), breakEvent.getPos().getY(), breakEvent.getPos().getZ()), MobSpawnType.MOB_SUMMONED);
                    } else {
                        ACEntityRegistry.CORRODENT.get().spawn((ServerLevel) level, BlockPos.containing(breakEvent.getPos().getX(), breakEvent.getPos().getY(), breakEvent.getPos().getZ()), MobSpawnType.MOB_SUMMONED);
                    }
                    ACExUtils.awardAdvancement(player,"burst_out","burst");

                }

            }
            if (blockState.is(ACBlockRegistry.PEERING_COPROLITH.get()) && breakEvent.getLevel().getRandom().nextDouble() < 0.4){
                if (level.getBiome(breakEvent.getPos()).is(ACBiomeRegistry.FORLORN_HOLLOWS)){
                    ACEntityRegistry.CORRODENT.get().spawn((ServerLevel) level, BlockPos.containing(breakEvent.getPos().getX(), breakEvent.getPos().getY(), breakEvent.getPos().getZ()), MobSpawnType.MOB_SUMMONED);
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get() && blockState.is(ACExBlockTagGenerator.ABYSSAL_ECOSYSTEM) && player.level().getBiome(player.getOnPos()).is(ACBiomeRegistry.ABYSSAL_CHASM) && player.getRandom().nextDouble() > 0.25){
            ACExUtils.deepReputation(player,-1);
        }

    }

    @SubscribeEvent
    public void mobTickEvents(EntityTickEvent.Pre entityTickEvent){
        if (!(entityTickEvent.getEntity() instanceof LivingEntity livingEntity)) {
            return;
        }
        Level level = livingEntity.level();
        if (level.isClientSide)
            return;

        if ((livingEntity instanceof HullbreakerEntity || livingEntity instanceof DeepOneBaseEntity) && livingEntity instanceof Mob hunter) {
            ACExSubmarineHunt.tick(hunter);
        }

        if (livingEntity instanceof NucleeperEntity nucleeper && !AlexsCavesExemplified.COMMON_CONFIG.DEFUSION_ENABLED.get() && ACExData.get(nucleeper, ACExAttachments.DEFUSED)){
            ACExAttachments.set(nucleeper, ACExAttachments.DEFUSED, false);
        }

        if (livingEntity instanceof SeaPigEntity seaPigEntity && AlexsCavesExemplified.COMMON_CONFIG.POISONOUS_SKIN_ENABLED.get()) {
            for (LivingEntity entity : seaPigEntity.level().getEntitiesOfClass(LivingEntity.class, seaPigEntity.getBoundingBox().inflate(0.5))) {
                if (entity != seaPigEntity && entity.getBbHeight() <= 3.5F && !(entity instanceof SeaPigEntity) && level.random.nextDouble() < 0.01) {
                    entity.addEffect(new MobEffectInstance(MobEffects.POISON, ACExUtils.seconds(3.0F), 0));
                    ACExUtils.awardAdvancement(entity,"poisonous_skin","touched");
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get() && livingEntity.hasEffect(ACExEffects.RABIAL.get()) && !(livingEntity instanceof WaterAnimal) && livingEntity.isInWaterRainOrBubble()) {
            float damage = Math.min(ACExRabial.WATER_DAMAGE, livingEntity.getHealth() - 1.0F);
            if (damage > 0.0F) {
                livingEntity.hurt(livingEntity.damageSources().drown(), damage);
            }
        }

        if (livingEntity instanceof Player player){
            ACExDepthCrush.tick(player);

            if (AlexsCavesExemplified.COMMON_CONFIG.PRESSURED_HOOKS_ENABLED.get() && !player.isCreative() && player.getVehicle() instanceof GumWormSegmentEntity) {
                for (InteractionHand hand : InteractionHand.values()) {
                    if (player.getItemInHand(hand).is(ACItemRegistry.CANDY_CANE_HOOK.get()) && player.getRandom().nextDouble() < 0.05) {
                        player.getItemInHand(hand).hurtAndBreak(1, player, breaker -> breaker.broadcastBreakEvent(hand));
                    }
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.GUASLOWPOKE_ENABLED.get() && !livingEntity.getType().is(ACExEntityTagGenerator.GUANO_IMMUNITY) && (livingEntity.getFeetBlockState().is(ACBlockRegistry.GUANO_BLOCK.get()) || livingEntity.getFeetBlockState().is(ACBlockRegistry.GUANO_LAYER.get()))){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ACExUtils.seconds(1.5F), 0));
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.SOLIDIFIED_ENABLED.get() && livingEntity instanceof WatcherEntity watcherEntity){
            if (watcherEntity.tickCount > ACExUtils.seconds(150.0F) && watcherEntity.getVehicle() == null && !watcherEntity.isVehicle()){
                BlockPos blockPos = watcherEntity.getOnPos().above();

                for (int i = 0; i < 3; i++){
                    BlockPos blockPlacing = blockPos.above(i);
                    if (level.getBlockState(blockPlacing).canBeReplaced()){
                        level.setBlock(blockPlacing, ACBlockRegistry.THORNWOOD_WOOD.get().defaultBlockState(), 2);
                        if (i == 2) {
                            boolean eastWest = watcherEntity.getDirection() == Direction.WEST || watcherEntity.getDirection() == Direction.EAST;
                            placeBranch(level, blockPlacing, eastWest ? Direction.NORTH : Direction.EAST);
                            placeBranch(level, blockPlacing, eastWest ? Direction.SOUTH : Direction.WEST);
                        }
                        if (level.random.nextDouble() < 0.03 && level.getBlockState(blockPlacing.above()).canBeReplaced()) {
                            level.setBlock(blockPlacing.above(), ACBlockRegistry.BEHOLDER.get().defaultBlockState(), 2);
                        }
                    }
                }
                for (Player player : level.getEntitiesOfClass(Player.class, watcherEntity.getBoundingBox().inflate(40))) {
                    ACExUtils.awardAdvancement(player,"solidified","solid");
                }
                watcherEntity.playSound(ACSoundRegistry.WATCHER_DEATH.get(), 6F, -5F);
                watcherEntity.discard();

            }
        }



        if (AlexsCavesExemplified.COMMON_CONFIG.STICKY_SODA_ENABLED.get() && !livingEntity.getType().is(ACTagRegistry.CANDY_MOBS)){
            if(livingEntity.getFeetBlockState().is(ACBlockRegistry.PURPLE_SODA.get())){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ACExUtils.seconds(4.5F), 0));
                ACExUtils.awardAdvancement(livingEntity, "sticky_soda", "stick");
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.PURPLE_LEATHERED_ENABLED.get() && livingEntity.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get())) {
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.HEAD),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.FEET),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.CHEST),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.LEGS),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.MAINHAND),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.OFFHAND),livingEntity);
        }

        if(livingEntity.getRandom().nextDouble() < 0.05 && AlexsCavesExemplified.COMMON_CONFIG.IRRADIATION_WASHOFF_ENABLED.get() && (livingEntity.isInWater() || livingEntity.getBlockStateOn().is(Blocks.WATER_CAULDRON) || livingEntity.isInWaterRainOrBubble())){
            ACExUtils.irradiationWash(livingEntity,50);
        }



        int irradiationAmmount = AlexsCavesExemplified.COMMON_CONFIG.EXEMPLIFIED_IRRADIATION_AMOUNT.get();
        if(irradiationAmmount > 0 && level.random.nextDouble() < 0.1){
            MobEffectInstance irradiated = livingEntity.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && irradiated.getAmplifier() >= irradiationAmmount - 1) {
                ACExUtils.awardAdvancement(livingEntity,"deathly_radiation","radiate");
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, ACExUtils.seconds(3.0F), 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, ACExUtils.seconds(3.0F), 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, ACExUtils.seconds(3.0F), 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, ACExUtils.seconds(3.0F), 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ACExUtils.seconds(3.0F), 0));

                if (ModList.get().isLoaded("alexsmobs")) {
                    livingEntity.addEffect(new MobEffectInstance(AMCompat.exsanguination(), ACExUtils.seconds(3.0F), 0));
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.GEOTHERMAL_EFFECTS_ENABLED.get()){
            BlockState blockState = livingEntity.getBlockStateOn();
            if (blockState.getBlock() instanceof GeothermalVentBlock){
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 1 && livingEntity.getRandom().nextDouble() < 0.05){
                    ACExUtils.irradiationWash(livingEntity,50);
                    if (livingEntity.isOnFire()) {
                        livingEntity.extinguishFire();
                    }
                }
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 2){
                    livingEntity.setSecondsOnFire(5);
                }
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 3){
                    if(!livingEntity.hasEffect(ACEffectRegistry.IRRADIATED.get())){
                        livingEntity.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), ACExUtils.seconds(20.0F), 0));
                    }
                }
            }
        }


    }

    @SubscribeEvent
    public void mobTickPostEvents(EntityTickEvent.Post entityTickEvent){
        Entity tickingEntity = entityTickEvent.getEntity();
        Level level = tickingEntity.level();

        if (tickingEntity instanceof Player player) {
            ACExMagnerip.tick(player);
        }

        if (tickingEntity instanceof FrostmintSpearEntity frostmintSpear) {
            if (frostmintSpear.isInFluidType() && AlexsCavesExemplified.COMMON_CONFIG.AMPLIFIED_FROSTMINT_ENABLED.get() && frostmintSpear.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get()) && !level.isClientSide) {
                FrostmintExplosion explosion = new FrostmintExplosion(level, frostmintSpear, frostmintSpear.getX() + 0.5F, frostmintSpear.getY() + 0.5F, frostmintSpear.getZ() + 0.5F, 4.0F, Explosion.BlockInteraction.DESTROY_WITH_DECAY, false);
                ACExSugarStaffAbilities.SPEAR_HITTING.set(ACExData.get(frostmintSpear, ACExAttachments.STAFF_SPEAR));
                explosion.explode();
                explosion.finalizeExplosion(true);
                ACExSugarStaffAbilities.SPEAR_HITTING.set(false);
                if (frostmintSpear.getOwner() instanceof Player) {
                    ACAdvancementTriggerRegistry.FROSTMINT_EXPLOSION.triggerForEntity(frostmintSpear.getOwner());
                }
                frostmintSpear.discard();
            }
            if (AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get() && ACExData.get(frostmintSpear, ACExAttachments.FROST_RADIANT) && frostmintSpear.tickCount > ACExUtils.seconds(5.0F)){
                frostmintSpear.discard();
            }
        }

        if (!(tickingEntity instanceof LivingEntity livingEntity)) {
            return;
        }

        if ((livingEntity instanceof UnderzealotEntity || livingEntity instanceof VesperEntity || livingEntity instanceof CorrodentEntity) && livingEntity instanceof Mob mob) {
            LivingEntity target = mob.getTarget();
            if (target != null && CuriosCompat.repelledByLight(mob, target)) {
                mob.setTarget(null);
                ACExUtils.awardAdvancement(target,"light_repel","repelled");
            }
        }

        if (livingEntity instanceof MineGuardianEntity mineGuardian && level.isClientSide && ACExData.get(mineGuardian, ACExAttachments.MINE_GUARDIAN_VARIANT) > 0) {
            RandomSource random = mineGuardian.getRandom();
            if (random.nextInt(80) == 0) {
                level.playLocalSound(mineGuardian.getX() + 0.5D, mineGuardian.getY() + 0.5D, mineGuardian.getZ() + 0.5D, ACSoundRegistry.URANIUM_HUM.get(), SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
            }
            if (random.nextInt(10) == 0) {
                level.addParticle(ACParticleRegistry.PROTON.get(), mineGuardian.getX(), mineGuardian.getY() + 0.5, mineGuardian.getZ(), mineGuardian.getX(), mineGuardian.getY(), mineGuardian.getZ());
            }
        }

        if (level.isClientSide)
            return;

        if (livingEntity instanceof MagnetronEntity magnetron) {
            int delay = ACExData.get(magnetron, ACExAttachments.RIPPED_HEART_DELAY) - 1;
            ACExData.set(magnetron, ACExAttachments.RIPPED_HEART_DELAY, delay);
            if (delay <= 0 && ACExData.get(magnetron, ACExAttachments.RIPPED_HEART) > 0) {
                ACExUtils.ripMagnetronHeart(magnetron, 0);
            }
        }

        if (livingEntity instanceof TeletorEntity teletor && AlexsCavesExemplified.COMMON_CONFIG.TELETOR_ARMORY_ENABLED.get() && !teletor.isDeadOrDying()){
            for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, teletor.getBoundingBox().inflate(8))) {
                if (item.getItem().is(ACTagRegistry.TELETOR_SPAWNS_WITH) && teletor.getWeapon() == null) {
                    ItemStack stolen = item.getItem();
                    MagneticWeaponEntity magneticWeapon = ACEntityRegistry.MAGNETIC_WEAPON.get().create(level);
                    ACExItemData.update(stolen, tag -> tag.putBoolean("Stolen", true));
                    magneticWeapon.setItemStack(stolen);
                    magneticWeapon.setControllerUUID(teletor.getUUID());
                    teletor.setWeaponUUID(magneticWeapon.getUUID());
                    level.addFreshEntity(magneticWeapon);
                    magneticWeapon.setPos(item.position());
                    item.discard();
                    ACExUtils.awardAdvancement(item.getOwner(),"teletor_rearm","rearm");
                }
            }
        }

        if (livingEntity instanceof NotorEntity notor) {
            int destructTime = ACExData.get(notor, ACExAttachments.SELF_DESTRUCT_TIME);
            if (destructTime > 0) {
                //Thank you Reimnop and ChatGPT for the math, shame im too retarded to know how shit works
                ACExAttachments.set(notor, ACExAttachments.SELF_DESTRUCT_TIME, destructTime - Math.min((destructTime / 5) + 1, Math.max(1, (int) Math.pow(2, Math.log(ACExUtils.seconds(10.0F) / destructTime) / Math.log(2)))));
            }
            if (destructTime == 1){
                ACExUtils.awardAdvancement(notor.getLastHurtByMob(),"self_destruct","explode");
                if (level instanceof ServerLevel serverLevel) {
                    ACExSelfDestruct.shatter(serverLevel, notor);
                }
            }
        }

    }

    @SubscribeEvent
    public void mobInteractEvents(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        InteractionHand hand = event.getHand();
        Level level = event.getLevel();
        Entity target = event.getTarget();

        if (!target.isAlive())
            return;

        if (target instanceof NucleeperEntity nucleeper && itemStack.is(Items.SHEARS) && !ACExData.get(nucleeper, ACExAttachments.DEFUSED) && AlexsCavesExemplified.COMMON_CONFIG.DEFUSION_ENABLED.get()){
            if (!level.isClientSide) {
                ACExUtils.awardAdvancement(player,"defusing","defuse");
                nucleeper.setTriggered(false);
                ACExUtils.spawnLoot(ACExLootTables.NUCLEEPER_DEFUSION,nucleeper,player,1);
                nucleeper.playSound(ACSoundRegistry.NUCLEAR_BOMB_DEFUSE.get());
                ACExAttachments.set(nucleeper, ACExAttachments.DEFUSED, true);
                itemStack.hurtAndBreak(1, player, breaker -> breaker.broadcastBreakEvent(hand));
            }
            player.swing(hand);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }

        if (target instanceof MineGuardianEntity mineGuardian && (itemStack.is(ItemTags.PICKAXES) && ACExUtils.restyleMineGuardian(mineGuardian, player) || itemStack.is(Items.PUFFERFISH) && ACExUtils.feedNoonGuardian(mineGuardian, player, itemStack))) {
            player.swing(hand);
            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
            event.setCanceled(true);
            return;
        }

        if (target instanceof MineGuardianEntity mineGuardian && AlexsCavesExemplified.COMMON_CONFIG.NAVAL_NUCLEARITY_ENABLED.get() && itemStack.is(ACBlockRegistry.NUCLEAR_BOMB.get().asItem()) && ACExData.get(mineGuardian, ACExAttachments.MINE_GUARDIAN_VARIANT) == 0 && !ACExData.get(mineGuardian, ACExAttachments.MINE_GUARDIAN_OWNER).equals("-1")){
            if (!level.isClientSide) {
                ACExAttachments.set(mineGuardian, ACExAttachments.MINE_GUARDIAN_VARIANT, mineGuardian.getRandom().nextBoolean() ? 1 : 2);
                if (!player.isCreative()){
                    itemStack.shrink(1);
                }
                mineGuardian.playSound(SoundEvents.SMITHING_TABLE_USE, 8.0F, 1.0F);
                ACExUtils.awardAdvancement(player,"nuclear_mines","nuke_mine");
            }
            player.swing(hand);
            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
            event.setCanceled(true);
            return;
        }

        if (target instanceof UnderzealotEntity underzealot && underzealot.getPassengers().isEmpty() && !underzealot.isPraying() && AlexsCavesExemplified.COMMON_CONFIG.DARK_OFFERING_ENABLED.get()) {
            boolean respect = (player.getItemBySlot(EquipmentSlot.CHEST).is(ACItemRegistry.CLOAK_OF_DARKNESS.get()) && player.getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.HOOD_OF_DARKNESS.get()));
            for (Mob leashedEntities : level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(10))) {
                boolean lasso = ModList.get().isLoaded("alexsmobs") && AMCompat.isLeashed(leashedEntities,player);
                if ((leashedEntities.getLeashHolder() == player || lasso)  && leashedEntities instanceof UnderzealotSacrifice && underzealot.getTarget() != player && !ACExData.get(leashedEntities, ACExAttachments.SACRIFICE_GIVEN)) {
                    int tradeDeterminer = 0;
                    if (leashedEntities instanceof GloomothEntity) {
                        tradeDeterminer = 1;
                        ACExUtils.awardAdvancement(player, "gloomoth_trade", "gloomoth");
                    } else if (leashedEntities instanceof CorrodentEntity) {
                        tradeDeterminer = 2;
                        ACExUtils.awardAdvancement(player, "corrodent_trade", "corrodent");
                    } else if (leashedEntities instanceof VesperEntity) {
                        tradeDeterminer = 3;
                        ACExUtils.awardAdvancement(player, "vesper_trade", "vesper");
                    }
                    if (lasso){
                        leashedEntities.spawnAtLocation(new ItemStack(AMCompat.amItemRegistry(1)));
                        AMCompat.vineLassoTo(null,leashedEntities);
                    } else {
                        leashedEntities.dropLeash(true,true);
                    }
                    ACExData.set(leashedEntities, ACExAttachments.SACRIFICE_GIVEN, true);
                    if (!leashedEntities.level().isClientSide()) {
                        leashedEntities.startRiding(underzealot);
                    }
                    boolean happy;
                    if (AlexsCavesExemplified.COMMON_CONFIG.UNDERZEALOT_RESPECT_ENABLED.get() && respect){
                        ACExUtils.awardAdvancement(player,"dark_respect","respect");
                        ResourceLocation resourceLocation = switch (tradeDeterminer) {
                            case 1 -> ACExLootTables.GLOOMOTH_TRADE;
                            case 2 -> ACExLootTables.CORRODENT_TRADE;
                            default -> ACExLootTables.VESPER_TRADE;
                        };
                        ACExUtils.spawnLoot(resourceLocation,underzealot,player,0);
                        happy = true;
                    } else {
                        happy = false;
                    }
                    for(int i = 0; i < 5; ++i) {
                        double d0 = underzealot.getRandom().nextGaussian() * 0.02D;
                        double d1 = underzealot.getRandom().nextGaussian() * 0.02D;
                        double d2 = underzealot.getRandom().nextGaussian() * 0.02D;
                        level.addParticle((happy ? ParticleTypes.HAPPY_VILLAGER : ParticleTypes.ANGRY_VILLAGER), underzealot.getRandomX(1.0D), underzealot.getRandomY() + 1.0D, underzealot.getRandomZ(1.0D), d0, d1, d2);
                    }
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void linkSubmarine(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        if (event.getTarget() instanceof SubmarineEntity submarine && stack.is(ACItemRegistry.GAME_CONTROLLER.get()) && ACExSubmarineRemote.enabled()) {
            if (!event.getLevel().isClientSide) {
                ACExSubmarineRemote.link(stack, submarine);
                event.getEntity().displayClientMessage(Component.translatable("misc.alexscavesexemplified.submarine_linked"), true);
                submarine.playSound(SoundEvents.NOTE_BLOCK_BIT.get(), 1.0F, 1.6F);
            }
            event.getEntity().swing(event.getHand());
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    public static void blockDrops(BlockState state, Level level, BlockPos pos, List<ItemStack> drops) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.IRRADIATED_CROPS_ENABLED.get() || !(state.getBlock() instanceof CropBlock || state.is(BlockTags.CROPS)))
            return;
        if (!level.getBiome(pos).is(ACBiomeRegistry.TOXIC_CAVES))
            return;
        for (ItemStack stack : drops) {
            if (stack.isEdible()) {
                ACExDataComponents.IRRADIATED.set(stack, Unit.INSTANCE);
            }
        }
    }

    @SubscribeEvent
    public void finishUsingItem(LivingEntityUseItemEvent.Finish useEvent) {
        if (AlexsCavesExemplified.COMMON_CONFIG.IRRADIATED_CROPS_ENABLED.get() && ACExDataComponents.IRRADIATED.has(useEvent.getItem()) && useEvent.getItem().isEdible() && !useEvent.getEntity().level().isClientSide) {
            useEvent.getEntity().addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), IRRADIATED_CROP_TICKS, 0));
        }
    }

    private static void eatSpray(ServerLevel level, Player player, ParticleOptions particle, int count) {
        Vec3 lookAngle = player.getLookAngle();
        for (int i = 0; i < count; i++) {
            level.sendParticles(particle, player.getX(), player.getY() + 1.5, player.getZ(), 0, lookAngle.x * 0.2, lookAngle.y * 0.6, lookAngle.z * 0.2, 1.0D);
        }
    }

    @SubscribeEvent
    public void livingChangeTarget(LivingChangeTargetEvent event) {
        if (ACExSerened.calms(event.getEntity(), event.getNewTarget())) {
            event.setNewTarget(null);
        }
        if (AlexsCavesExemplified.COMMON_CONFIG.REMINEDING_ENABLED.get() && event.getEntity() instanceof DeepOneBaseEntity deepOne && deepOne.isSummoned() && event.getNewTarget() instanceof MineGuardianEntity guardian) {
            UUID summoner = ((ACExDeepOneAccessor) deepOne).getSummonerUUID();
            if (summoner != null && summoner.toString().equals(ACExData.get(guardian, ACExAttachments.MINE_GUARDIAN_OWNER))) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void livingIncomingDamage(LivingHurtEvent incomingDamageEvent) {
        LivingEntity damaged = incomingDamageEvent.getEntity();
        DamageSource source = incomingDamageEvent.getSource();
        incomingDamageEvent.setAmount(ACExSelfDestruct.weakenedDamage(source, incomingDamageEvent.getAmount()));

        if (ACExArmedUnderzealot.armedAttacker(source) != null) {
            incomingDamageEvent.setAmount(ACExArmedUnderzealot.daggerDamage());
        }

        if (source.getEntity() instanceof LivingEntity && damaged.hasEffect(ACExEffects.SERENED.get())){
            MobEffectInstance serene = damaged.getEffect(ACExEffects.SERENED.get());
            if (serene != null) {
                damaged.removeEffect(serene.getEffect());
                if (serene.getDuration() > SERENE_HIT_COST) {
                    damaged.addEffect(new MobEffectInstance(serene.getEffect(), serene.getDuration() - SERENE_HIT_COST, serene.getAmplifier()));
                }
            }
        }

        if (damaged instanceof NotorEntity notor && AlexsCavesExemplified.COMMON_CONFIG.SELF_DESTRUCT_ENABLED.get() && ACExData.get(notor, ACExAttachments.SELF_DESTRUCT_TIME) <= 0) {
            ACExAttachments.set(notor, ACExAttachments.SELF_DESTRUCT_TIME, ACExUtils.seconds(10.0F));
        }

        if (damaged instanceof VesperEntity vesper && source.is(DamageTypes.ARROW) && AlexsCavesExemplified.COMMON_CONFIG.VESPER_SHOTDOWN_ENABLED.get()){
            vesper.setHanging(false);
            vesper.groundedFor = ACExUtils.seconds(50.0F);
            ACExUtils.awardAdvancement(source.getEntity(),"shot_down","shot");
        }
    }

    @SubscribeEvent
    public void rightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();

        if (event.getItemStack().is(ACItemRegistry.RADIANT_ESSENCE.get()) && AlexsCavesExemplified.COMMON_CONFIG.RADIANT_CONSUMPTION_ENABLED.get()) {
            if (!level.isClientSide) {
                if (!player.isCreative()) {
                    event.getItemStack().shrink(1);
                }
                ACExData.set(player, ACExAttachments.RADIANT_METER, ACExData.get(player, ACExAttachments.RADIANT_METER) + 1);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 1.0F, 1.2F);
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new ACExRadiantConsumePacket(player.getId()));
            }
            player.swing(hand);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }


        if(AlexsCavesExemplified.COMMON_CONFIG.IRRADIATION_WASHOFF_ENABLED.get() && ModList.get().isLoaded("supplementaries")){
            MobEffectInstance irradiated = player.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && SupplementariesCompat.isSoap(player.getItemInHand(hand)) && (player.isInWater() || player.getBlockStateOn().is(Blocks.WATER_CAULDRON))) {
                if (!level.isClientSide) {
                    ACExUtils.irradiationWash(player,player.getRandom().nextInt(200,500));
                }
                player.swing(hand);

                for (int i = 0; i < 10; i++){
                    double d1 = player.getRandom().nextGaussian() * 0.02;
                    double d2 = player.getRandom().nextGaussian() * 0.02;
                    double d3 = player.getRandom().nextGaussian() * 0.02;

                    level.addParticle(SupplementariesCompat.suds(), player.getX(), player.getY() + 0.5, player.getZ(), d1 * 2, d2 * 2, d3 * 2);
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.KIROV_REPORTING_ENABLED.get() && player.isFallFlying() && (player.getItemInHand(hand).is(Items.FLINT_AND_STEEL) || player.getItemInHand(hand).is(Items.FIRE_CHARGE))){

            InteractionHand otherHand = hand.equals(InteractionHand.MAIN_HAND) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            ItemStack otherStack = player.getItemInHand(otherHand);

            EntityType<?> bomb;
            if (otherStack.is(Items.TNT)) {
                bomb = EntityType.TNT;
            } else if (otherStack.is(ACBlockRegistry.NUCLEAR_BOMB.get().asItem())) {
                bomb = ACEntityRegistry.NUCLEAR_BOMB.get();
            } else if (otherStack.is(ACExBlockRegistry.GAMMA_NUCLEAR_BOMB.get().asItem())) {
                bomb = ACExEntityRegistry.GAMMA_NUCLEAR_BOMB.get();
            } else {
                return;
            }

            if (!player.level().isClientSide && !player.getCooldowns().isOnCooldown(player.getItemInHand(hand).getItem())){
                player.level().gameEvent(player, GameEvent.PRIME_FUSE, player.blockPosition());
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
                player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), KIROV_COOLDOWN);
                player.swing(hand, true);
                bomb.spawn((ServerLevel) level, BlockPos.containing(player.getX(), player.getY() - 0.5, player.getZ()), MobSpawnType.MOB_SUMMONED);
                if (!player.isCreative()) {
                    otherStack.shrink(1);
                }
                ACExUtils.awardAdvancement(player, "kirov_reporting", "kirov");
            }

        }

    }

    @SubscribeEvent
    public void effectExpired(MobEffectEvent.Expired event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();
        if (instance != null && instance.getEffect() == ACEffectRegistry.SUGAR_RUSH.get() && entity.level() instanceof ServerLevel sugarLevel && AlexsCavesExemplified.COMMON_CONFIG.GLUTTONY_ENABLED.get() && (entity.getRandom().nextDouble() < SUGAR_CRASH_CHANCE || instance.getAmplifier() >= 2)) {
            int amplifier = instance.getAmplifier();
            ACExScheduler.schedule(sugarLevel, 1, () -> {
                if (!entity.isAlive())
                    return;
                int crashLevel = amplifier + 1;
                entity.addEffect(new MobEffectInstance(ACExEffects.SUGAR_CRASH.get(), ACExUtils.seconds(20.0F), amplifier));
                entity.hurt(ACExDamageTypes.getDamageSource(sugarLevel, ACExDamageTypes.SUGAR_CRASH), crashLevel * 2);
                if (crashLevel > 3) {
                    sugarLevel.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 2, Level.ExplosionInteraction.MOB);
                }
                ACExUtils.awardAdvancement(entity, "sugar_crashed", "crashed");
            });
        }
        if (instance != null && instance.getEffect() == ACExEffects.RABIAL.get() && entity.level() instanceof ServerLevel serverLevel && AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get()) {
            float damage = (instance.getAmplifier() + 1) * ACExRabial.END_DAMAGE;
            ACExScheduler.schedule(serverLevel, 1, () -> {
                if (entity.isAlive()) {
                    entity.hurt(ACExDamageTypes.getDamageSource(serverLevel, ACExDamageTypes.RABIAL_END), damage);
                }
            });
        }
    }

    @SubscribeEvent
    public void livingDamage(LivingDamageEvent livingDamageEvent) {
        Entity damager = livingDamageEvent.getSource().getEntity();
        LivingEntity damaged = livingDamageEvent.getEntity();

        UnderzealotEntity armedUnderzealot = ACExArmedUnderzealot.armedAttacker(livingDamageEvent.getSource());
        if (armedUnderzealot != null && damaged.isAlive() && damaged.getHealth() > livingDamageEvent.getAmount()) {
            ACExArmedUnderzealot.summonBlade(armedUnderzealot, damaged);
        }


        if(AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get() && damager instanceof LivingEntity living && damager == livingDamageEvent.getSource().getDirectEntity() && living.hasEffect(ACExEffects.RABIAL.get()) && damaged.getType().is(ACExEntityTagGenerator.CAN_RABIES) && !damaged.hasEffect(MobEffects.DAMAGE_RESISTANCE)){
            damaged.addEffect(new MobEffectInstance(ACExEffects.RABIAL.get(), ACExUtils.seconds(3600.0F), 0));
            ACExUtils.awardAdvancement(damager,"rabial_spread","spread");
        }

        if(AlexsCavesExemplified.COMMON_CONFIG.STICKY_CARAMEL_ENABLED.get() && damager instanceof CaramelCubeEntity caramelCubeEntity && caramelCubeEntity.getRandom().nextDouble() < 0.5){
            MeltedCaramelEntity meltedCaramel = ACEntityRegistry.MELTED_CARAMEL.get().create(caramelCubeEntity.level());
            if (meltedCaramel == null)
                return;
            meltedCaramel.setPos(damaged.getPosition(1));
            meltedCaramel.setDespawnsIn(40 + ((1 + caramelCubeEntity.getSlimeSize()) - 1) * 40);
            meltedCaramel.setDeltaMovement(caramelCubeEntity.getDeltaMovement().multiply(-1.0F, 0.0F, -1.0F));
            caramelCubeEntity.level().addFreshEntity(meltedCaramel);
        }
    }
    

    @SubscribeEvent
    public void bonemealEvent(BonemealEvent bonemealEvent) {
        Player entity = bonemealEvent.getEntity();
        Level level = bonemealEvent.getLevel();
        BlockPos blockPos = bonemealEvent.getPos();
        BlockState blockState = level.getBlockState(blockPos);

        if (AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get() && !level.isClientSide && entity != null && blockState.getBlock() instanceof BonemealableBlock bonemealable && bonemealable.isValidBonemealTarget(level, blockPos, blockState, level.isClientSide) && blockState.is(ACBlockRegistry.PING_PONG_SPONGE.get()) && level.getBiome(blockPos).is(ACBiomeRegistry.ABYSSAL_CHASM)) {
            ACExUtils.deepReputation(entity,1);
        }

    }

    @SubscribeEvent
    public void talkEvent(ServerChatEvent serverChatEvent){
        Player player = serverChatEvent.getPlayer();
        if (AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get() && player.getRandom().nextDouble() < 0.01 && player.hasEffect(ACExEffects.RABIAL.get())){
            serverChatEvent.setMessage(Component.literal("rrRRRrrrAgh!... ").append(serverChatEvent.getMessage()));
        }
    }


    private static void placeBranch(Level level, BlockPos trunk, Direction direction) {
        BlockPos branch = trunk.relative(direction);
        if (level.getBlockState(branch).canBeReplaced()) {
            level.setBlock(branch, ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING, direction), 2);
        }
    }

    private void checkLeatherArmor(ItemStack item, LivingEntity living){
        if (item.getItem() instanceof DyeableLeatherItem dyeableLeatherItem && !dyeableLeatherItem.hasCustomColor(item)) {
            dyeableLeatherItem.setColor(item, 0Xb839e6);
            ACExUtils.awardAdvancement(living,"purple_coloring","colored");
        }
    }

}
