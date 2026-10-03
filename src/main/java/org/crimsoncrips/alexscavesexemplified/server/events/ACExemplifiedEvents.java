package org.crimsoncrips.alexscavesexemplified.server.events;

import org.crimsoncrips.alexscavesexemplified.misc.ACExArmedUnderzealot;
import org.crimsoncrips.alexscavesexemplified.misc.ACExSubmarineRemote;
import net.neoforged.neoforge.network.PacketDistributor;
import org.crimsoncrips.alexscavesexemplified.networking.ACExRadiantConsumePacket;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Unit;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExDataComponents;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;
import com.github.alexmodguy.alexscaves.server.block.*;
import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearSirenBlockEntity;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.FrostmintSpearEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.MagneticWeaponEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.MeltedCaramelEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
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
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.crimsoncrips.alexscavesexemplified.compat.SupplementariesCompat;
import org.crimsoncrips.alexscavesexemplified.datagen.ACExDamageTypes;
import org.crimsoncrips.alexscavesexemplified.datagen.loottables.ACExLootTables;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExBlockTagGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExEntityTagGenerator;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACExEffects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;

import java.util.Optional;

import static net.minecraft.world.entity.EntityType.*;


public class ACExemplifiedEvents {

    private static final double ARMED_UNDERZEALOT_CHANCE = 0.25D;
    private static final int IRRADIATED_CROP_TICKS = ACExUtils.seconds(10.0F);

    @SubscribeEvent
    public void onEntityFinalizeSpawn(FinalizeSpawnEvent event) {
        final var entity = event.getEntity();

        Double chargedChance = AlexsCavesExemplified.COMMON_CONFIG.CHARGED_CAVE_CREEPER_CHANCE.get();

        if (entity instanceof UnderzealotEntity underzealot && AlexsCavesExemplified.COMMON_CONFIG.ARMED_UNDERZEALOT_ENABLED.get() && underzealot.getRandom().nextDouble() < ARMED_UNDERZEALOT_CHANCE) {
            underzealot.setData(ACExAttachments.ARMED, true);
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
                entity.addEffect(new MobEffectInstance(ACExEffects.RABIAL, ACExUtils.seconds(7000.0F), 0));
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


        if (AlexsCavesExemplified.COMMON_CONFIG.GLUTTONY_ENABLED.get() && blockState.is(ACExBlockTagGenerator.CONSUMABLE_BLOCKS)) {
            ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, blockState);
            if (player.isCrouching()) {
                MobEffectInstance hunger = player.getEffect(MobEffects.HUNGER);
                if (hunger != null) {
                    ACExUtils.addSweets(player, 1);

                    if (!hunger.isInfiniteDuration()) {
                        player.removeEffect(MobEffects.HUNGER);
                        player.addEffect(new MobEffectInstance(MobEffects.HUNGER, hunger.getDuration() - ACExUtils.seconds(3.0F), hunger.getAmplifier()));
                    }
                    worldIn.destroyBlock(pos, true,player);
                    player.getFoodData().eat(1, 1);
                    player.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    for (int i = 0; i <= 15; i++) {
                        Vec3 lookAngle = player.getLookAngle();
                        worldIn.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 1.5, lookAngle.y * 2, lookAngle.z * 1.5);
                    }
                    ACExUtils.awardAdvancement(player,"gluttony","eat");
                    if (random.nextDouble() < 0.01)
                        player.addEffect(new MobEffectInstance(ACEffectRegistry.SUGAR_RUSH, ACExUtils.seconds(5.0F), 0));


                } else if (player.getFoodData().needsFood()) {
                    ACExUtils.addSweets(player, 1);

                    player.getFoodData().eat(2, 2);
                    player.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    for (int i = 0; i <= 15; i++) {
                        Vec3 lookAngle = player.getLookAngle();
                        worldIn.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 1.5, lookAngle.y * 2, lookAngle.z * 1.5);
                    }
                    ACExUtils.awardAdvancement(player,"gluttony","eat");
                    if (random.nextDouble() < 0.01)
                        player.addEffect(new MobEffectInstance(ACEffectRegistry.SUGAR_RUSH, ACExUtils.seconds(5.0F), 0));

                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.LIQUID_REPLICATION_ENABLED.get() && blockState.is(Blocks.CAULDRON) && (itemStack.is(ACBlockRegistry.SCRAP_METAL.get().asItem()) || itemStack.is(ACBlockRegistry.SCRAP_METAL_PLATE.get().asItem())) && player.isCrouching()){
            event.getLevel().setBlock(pos, ACExBlockRegistry.METAL_CAULDRON.get().defaultBlockState(),3);
            player.swing(event.getHand());
            player.playSound(SoundEvents.SMITHING_TABLE_USE, 1F, 1F);
            ACExUtils.awardAdvancement(player,"metal_cauldron","metal");
            event.setCanceled(true);
        }




    }

    @SubscribeEvent
    public void onInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        Level level = event.getLevel();
        Entity target = event.getTarget();


        if (AlexsCavesExemplified.COMMON_CONFIG.GLUTTONY_ENABLED.get() && player.isCrouching() && player.getEffect(MobEffects.HUNGER) != null) {

            if (target instanceof GingerbreadManEntity gingerbread && !gingerbread.isOvenSpawned()) {
                ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, ACItemRegistry.GINGERBREAD_CRUMBS.get().asItem().getDefaultInstance());
                Vec3 lookAngle = player.getLookAngle();
                level.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 0.2, lookAngle.y * 0.6, lookAngle.z * 0.2);


                for (GingerbreadManEntity entity : level.getEntitiesOfClass(GingerbreadManEntity.class, gingerbread.getBoundingBox().inflate(10, 5, 10))) {
                    if (!entity.isOvenSpawned() && !player.isCreative()) {
                        entity.setTarget(player);
                    }
                }
                event.getTarget().discard();
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 2);
                player.playSound(SoundEvents.GENERIC_EAT, 1.0F, -2F);

            }

            if (target instanceof CaramelCubeEntity caramelCube && caramelCube.getSlimeSize() <= 0) {
                ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, ACItemRegistry.CARAMEL.get().asItem().getDefaultInstance());
                Vec3 lookAngle = player.getLookAngle();
                level.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 0.2, lookAngle.y * 0.6, lookAngle.z * 0.2);

                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 3);
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ACExUtils.seconds(3.0F), 0,false,false));
                event.getTarget().discard();
                player.playSound(SoundEvents.GENERIC_EAT, 1.0F, -2F);

            }

            if (target instanceof GummyBearEntity gummyBearEntity && gummyBearEntity.isBaby()) {
                Vec3 lookAngle = player.getLookAngle();

                ItemStack gelatinColor = switch (gummyBearEntity.getGummyColor()) {
                    case GREEN -> ACItemRegistry.GELATIN_GREEN.get().asItem().getDefaultInstance();
                    case BLUE -> ACItemRegistry.GELATIN_BLUE.get().asItem().getDefaultInstance();
                    case YELLOW -> ACItemRegistry.GELATIN_YELLOW.get().asItem().getDefaultInstance();
                    case PINK -> ACItemRegistry.GELATIN_PINK.get().asItem().getDefaultInstance();
                    default -> ACItemRegistry.GELATIN_RED.get().asItem().getDefaultInstance();
                };

                ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, gelatinColor.getItem().getDefaultInstance());

                for (int i = 0; i <= 15; i++) {
                    level.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 0.2, lookAngle.y * 0.6, lookAngle.z * 0.2);
                }

                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 5);
                for (GummyBearEntity entity : level.getEntitiesOfClass(GummyBearEntity.class, player.getBoundingBox().inflate(10, 5, 10))) {
                    if (!player.isCreative()) {
                        entity.setTarget(player);
                    }
                }
                target.discard();
                player.playSound(SoundEvents.GENERIC_EAT, 1.0F, -2F);
                player.playSound(ACSoundRegistry.GUMMY_BEAR_DEATH.get(), 0.4F, 2F);

            }
        }


        if(event.getTarget() instanceof Parrot parrot && AlexsCavesExemplified.COMMON_CONFIG.COOKIE_CRUMBLE_ENABLED.get() && itemStack.is(ACBlockRegistry.COOKIE_BLOCK.get().asItem())){
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }

            parrot.addEffect(new MobEffectInstance(MobEffects.POISON, ACExUtils.seconds(45.0F)));
            parrot.hurt(player.damageSources().playerAttack(player), Float.MAX_VALUE);
            parrot.level().explode(player,parrot.getX(),parrot.getY(),parrot.getZ(),3, Level.ExplosionInteraction.MOB);
        }

        if (event.getTarget() instanceof CandicornEntity candicornEntity) {
            if (itemStack.is(ACItemRegistry.CARAMEL_APPLE.get()) && AlexsCavesExemplified.COMMON_CONFIG.CANDICORN_HEAL_ENABLED.get()) {
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
        Entity killer = deathEvent.getSource().getEntity();
        Level level = died.level();

        if (died instanceof Player player) {
            player.setData(ACExAttachments.SWEETS, 0);
        }

        if (died instanceof MagnetronEntity magnetron && magnetron.getData(ACExAttachments.RIPPED_HEART) >= 100 && AlexsCavesExemplified.COMMON_CONFIG.MAGNETICISM_ENABLED.get()){
            float range = 5F;
            RandomSource random = magnetron.getRandom();
            Vec3 particlesFrom = magnetron.position().add(0, 0.2, 0);
            float particleMax = 10 + random.nextInt(5);
            for (int particles = 0; particles < particleMax; particles++) {
                Vec3 vec3 = new Vec3((random.nextFloat() - 0.5) * 0.3F, (random.nextFloat() - 0.5) * 0.3F, range * 0.5F + range * 0.5F * random.nextFloat()).yRot((float) ((particles / particleMax) * Math.PI * 2)).add(particlesFrom);
                if (random.nextBoolean()) {
                    level.addParticle(ACParticleRegistry.SCARLET_SHIELD_LIGHTNING.get(), vec3.x, vec3.y, vec3.z, particlesFrom.x, particlesFrom.y, particlesFrom.z);
                } else {
                    level.addParticle(ACParticleRegistry.AZURE_SHIELD_LIGHTNING.get(), particlesFrom.x, particlesFrom.y, particlesFrom.z, vec3.x, vec3.y, vec3.z);
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

        if (died instanceof NucleeperEntity nucleeper && AlexsCavesExemplified.COMMON_CONFIG.NUCLEAR_CHAIN_ENABLED.get() && !nucleeper.getData(ACExAttachments.DEFUSED)){
            if (deathEvent.getSource().is(DamageTypes.PLAYER_EXPLOSION) || deathEvent.getSource().is(DamageTypes.EXPLOSION) || deathEvent.getSource().is(ACDamageTypes.NUKE) || deathEvent.getSource().is(ACDamageTypes.TREMORZILLA_BEAM)){
                NuclearExplosionEntity explosionMain = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(nucleeper.level());
                explosionMain.copyPosition(nucleeper);
                explosionMain.setSize(nucleeper.isCharged() ? 1.75F : 1F);
                if(!nucleeper.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)){
                    explosionMain.setNoGriefing(true);
                }
                nucleeper.level().addFreshEntity(explosionMain);


                int amount = 0;
                for (NucleeperEntity nucleepers : level.getEntitiesOfClass(NucleeperEntity.class, nucleeper.getBoundingBox().inflate(13))) {
                    if (AlexsCavesExemplified.COMMON_CONFIG.NUCLEAR_CHAIN_ENABLED.get() && !nucleepers.getData(ACExAttachments.DEFUSED)){
                        amount++;
                        NuclearExplosionEntity explosion = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(level);
                        explosion.copyPosition(nucleepers);
                        explosion.setSize(nucleepers.isCharged() ? 1.75F : 1F);
                        if(!level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)){
                            explosion.setNoGriefing(true);
                        }
                        level.addFreshEntity(explosion);

                    }
                }
                for (Player players : level.getEntitiesOfClass(Player.class, nucleeper.getBoundingBox().inflate(50))) {
                    if (amount >= 10){
                        ACExUtils.awardAdvancement(players,"nucleeper_annhilation","nuke_chained");
                    }
                    if (amount != 0){
                        ACExUtils.awardAdvancement(players,"chain_reaction","chain");
                    }
                }
            }
        }

        if (died instanceof MineGuardianEntity mineGuardianEntity && mineGuardianEntity.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) >= 1) {
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

        if (blockState.is(ACExBlockTagGenerator.ABYSSAL_ECOSYSTEM) && player.level().getBiome(player.getOnPos()).is(ACBiomeRegistry.ABYSSAL_CHASM) && player.getRandom().nextDouble() > 0.25){
            ACExUtils.deepReputation(player,-1);
        }

    }

    @SubscribeEvent
    public void mobTickEvents(EntityTickEvent.Pre entityTickEvent){
        if (!(entityTickEvent.getEntity() instanceof LivingEntity livingEntity)) {
            return;
        }
        Level level = livingEntity.level();

        if (livingEntity instanceof NucleeperEntity nucleeper && !AlexsCavesExemplified.COMMON_CONFIG.DEFUSION_ENABLED.get() && nucleeper.getData(ACExAttachments.DEFUSED)){
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

        if (livingEntity instanceof Player player){
            Entity vehicle = player.getVehicle();
            if (AlexsCavesExemplified.COMMON_CONFIG.ABYSSAL_CRUSH_ENABLED.get() && vehicle != null && !(vehicle.getType().is(ACExEntityTagGenerator.CRUSH_IMMUNITY))){
                if(level.getBiome(player.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM)){
                    int aboveWater = 0;
                    BlockPos pos = new BlockPos(player.getBlockX(), (int) (player.getBlockY() + 2), player.getBlockZ());
                    while (level.getBlockState(pos).is(Blocks.WATER)) {
                        pos = pos.above();
                        aboveWater++;
                    }
                    int diving = ACExUtils.getDivingAmount(livingEntity);

                    if (level.random.nextDouble() < (0.1 - (0.030 * diving))) {
                        if (aboveWater > 50 && diving <= 10) {
                            player.hurt(ACExDamageTypes.getDamageSource(player.level(), ACExDamageTypes.DEPTH_CRUSH), (float) (0.025 * (aboveWater - 40)));
                            player.setAirSupply(player.getAirSupply() + (int) (0.025 * (aboveWater - 100)));
                        }
                    }
                }
            }

            if (AlexsCavesExemplified.COMMON_CONFIG.PRESSURED_HOOKS_ENABLED.get()){
                boolean trueMainhand = livingEntity.getMainHandItem().is(ACItemRegistry.CANDY_CANE_HOOK.get());
                boolean trueOffhand = livingEntity.getOffhandItem().is(ACItemRegistry.CANDY_CANE_HOOK.get());
                if (livingEntity.getVehicle() instanceof GumWormSegmentEntity && trueMainhand) {
                    if (player.isCreative())
                        return;
                    if (!(player.getRandom().nextDouble() < 0.05))
                        return;
                    player.getMainHandItem().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }
                if (livingEntity.getVehicle() instanceof GumWormSegmentEntity && trueOffhand) {
                    if (player.isCreative())
                        return;
                    if (!(player.getRandom().nextDouble() < 0.05))
                        return;
                    player.getOffhandItem().hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
                }
            }
        }




        if (AlexsCavesExemplified.COMMON_CONFIG.GUASLOWPOKE_ENABLED.get() && livingEntity.getType().is(ACExEntityTagGenerator.GUANO_IMMUNITY) && (livingEntity.getInBlockState().is(ACBlockRegistry.GUANO_BLOCK.get()) || livingEntity.getInBlockState().is(ACBlockRegistry.GUANO_LAYER.get()))){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ACExUtils.seconds(1.5F), 0));
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.SOLIDIFIED_ENABLED.get() && livingEntity instanceof WatcherEntity watcherEntity){
            if (watcherEntity.tickCount > ACExUtils.seconds(150.0F) && watcherEntity.getVehicle() == null && !watcherEntity.isVehicle()){
                BlockPos blockPos = watcherEntity.getOnPos().above();

                for (int i = 0; i < 3; i++){
                    BlockPos blockPlacing = new BlockPos(blockPos.getX(),blockPos.getY() + i,blockPos.getZ());
                    if (!level.getBlockState(blockPlacing).isCollisionShapeFullBlock(level,blockPlacing)){
                        level.setBlock(blockPlacing, ACBlockRegistry.THORNWOOD_WOOD.get().defaultBlockState(), 2);
                        if (i == 2) {
                            if (watcherEntity.getDirection() == Direction.WEST || watcherEntity.getDirection() == Direction.EAST) {
                                level.setBlock(blockPlacing.north(), ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING, Direction.NORTH), 2);
                                level.setBlock(blockPlacing.south(), ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING, Direction.SOUTH), 2);
                            } else {
                                level.setBlock(blockPlacing.east(), ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING, Direction.EAST), 2);
                                level.setBlock(blockPlacing.west(), ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING, Direction.WEST), 2);
                            }
                        }
                        if (level.random.nextDouble() < 0.03) {
                            level.setBlock(new BlockPos(blockPlacing.above()), ACBlockRegistry.BEHOLDER.get().defaultBlockState(), 2);
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
            if(livingEntity.getInBlockState().is(ACBlockRegistry.PURPLE_SODA.get())){
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
            MobEffectInstance irradiated = livingEntity.getEffect(ACEffectRegistry.IRRADIATED);
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
                    livingEntity.igniteForSeconds(5);
                }
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 3){
                    if(!livingEntity.hasEffect(ACEffectRegistry.IRRADIATED)){
                        livingEntity.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED, ACExUtils.seconds(20.0F), 0));
                    }
                }
            }
        }


    }

    @SubscribeEvent
    public void mobTickPostEvents(EntityTickEvent.Post entityTickEvent){
        Entity tickingEntity = entityTickEvent.getEntity();
        Level level = tickingEntity.level();

        if (tickingEntity instanceof FrostmintSpearEntity frostmintSpear) {
            if (frostmintSpear.isInFluidType() && AlexsCavesExemplified.COMMON_CONFIG.AMPLIFIED_FROSTMINT_ENABLED.get() && frostmintSpear.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get()) && !level.isClientSide) {
                FrostmintExplosion explosion = new FrostmintExplosion(level, frostmintSpear, frostmintSpear.getX() + 0.5F, frostmintSpear.getY() + 0.5F, frostmintSpear.getZ() + 0.5F, 4.0F, Explosion.BlockInteraction.DESTROY_WITH_DECAY, false);
                ACExSugarStaffAbilities.SPEAR_HITTING.set(frostmintSpear.getData(ACExAttachments.STAFF_SPEAR));
                explosion.explode();
                explosion.finalizeExplosion(true);
                ACExSugarStaffAbilities.SPEAR_HITTING.set(false);
                if (frostmintSpear.getOwner() instanceof Player) {
                    ACAdvancementTriggerRegistry.FROSTMINT_EXPLOSION.get().triggerForEntity(frostmintSpear.getOwner());
                }
                frostmintSpear.discard();
            }
            if (AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get() && frostmintSpear.getData(ACExAttachments.FROST_RADIANT) && frostmintSpear.tickCount > ACExUtils.seconds(5.0F)){
                frostmintSpear.discard();
            }
        }

        if (!(tickingEntity instanceof LivingEntity livingEntity)) {
            return;
        }

        //Props to Drullkus for assistance
        if (level.getBlockState(livingEntity.blockPosition()).getBlock() != ACBlockRegistry.AMBER.get()) {
            if (ModList.get().isLoaded("alexsmobs")) {
                AMCompat.amberReset(livingEntity);
            }
            if ((livingEntity instanceof Frog || livingEntity instanceof Tadpole) && livingEntity instanceof Mob mob && mob.isNoAi()) {
                mob.setNoAi(false);
                mob.setInvulnerable(false);
                mob.setSilent(false);
                mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ACExUtils.seconds(300.0F), 1,false,false));
            }
        }

        if ((livingEntity instanceof UnderzealotEntity || livingEntity instanceof VesperEntity || livingEntity instanceof CorrodentEntity) && livingEntity instanceof Mob mob && AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get()) {
            LivingEntity target = mob.getTarget();
            if (target != null && target != mob.getLastHurtByMob() && CuriosCompat.hasLight(target)) {
                mob.setTarget(null);
                ACExUtils.awardAdvancement(target,"light_repel","repelled");
            }
        }

        if (livingEntity instanceof MagnetronEntity magnetron) {
            int delay = magnetron.getData(ACExAttachments.RIPPED_HEART_DELAY) - 1;
            magnetron.setData(ACExAttachments.RIPPED_HEART_DELAY, delay);
            if (delay <= 0 && magnetron.getData(ACExAttachments.RIPPED_HEART) > 0) {
                ACExUtils.ripMagnetronHeart(magnetron, 0);
            }
        }

        if (livingEntity instanceof TeletorEntity teletor && AlexsCavesExemplified.COMMON_CONFIG.TELETOR_ARMORY_ENABLED.get() && !teletor.isDeadOrDying()){
            for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, teletor.getBoundingBox().inflate(8))) {
                if (item.getItem().is(ACTagRegistry.TELETOR_SPAWNS_WITH) && teletor.getWeapon() == null) {
                    ItemStack stolen = item.getItem();
                    MagneticWeaponEntity magneticWeapon = ACEntityRegistry.MAGNETIC_WEAPON.get().create(level);
                    CustomData.update(DataComponents.CUSTOM_DATA, stolen, tag -> tag.putBoolean("Stolen", true));
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
            int destructTime = notor.getData(ACExAttachments.SELF_DESTRUCT_TIME);
            if (destructTime > 0) {
                //Thank you Reimnop and ChatGPT for the math, shame im too retarded to know how shit works
                ACExAttachments.set(notor, ACExAttachments.SELF_DESTRUCT_TIME, destructTime - Math.min((destructTime / 5) + 1, Math.max(1, (int) Math.pow(2, Math.log(ACExUtils.seconds(10.0F) / destructTime) / Math.log(2)))));
            }
            if (destructTime == 1){
                ACExUtils.awardAdvancement(notor.getLastHurtByMob(),"self_destruct","explode");
                level.explode(notor,notor.getX(),notor.getY(),notor.getZ(),2, Level.ExplosionInteraction.NONE);
                notor.discard();
            }
        }

        if (livingEntity instanceof MineGuardianEntity mineGuardian) {
            String name = mineGuardian.getName().getString();
            if (mineGuardian.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) >= 1){
                if (name.equals("Ae")){
                    ACExAttachments.set(mineGuardian, ACExAttachments.MINE_GUARDIAN_VARIANT, 1);
                }
                if (name.equals("Jesse")){
                    ACExAttachments.set(mineGuardian, ACExAttachments.MINE_GUARDIAN_VARIANT, 2);
                }
            }
            if (mineGuardian.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) > 0){
                RandomSource random = mineGuardian.getRandom();
                if (random.nextInt(80) == 0) {
                    level.playLocalSound(mineGuardian.getX() + 0.5D, mineGuardian.getY() + 0.5D, mineGuardian.getZ() + 0.5D, ACSoundRegistry.URANIUM_HUM.get(), SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
                }
                if (random.nextInt(10) == 0) {
                    level.addParticle(ACParticleRegistry.PROTON.get(), mineGuardian.getX(), mineGuardian.getY() + 0.5, mineGuardian.getZ(), mineGuardian.getX(), mineGuardian.getY(), mineGuardian.getZ());
                }
            }
            if (AlexsCavesExemplified.COMMON_CONFIG.NOON_GUARDIAN_ENABLED.get() && name.equals("Noon") && mineGuardian.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) == 0){
                ACExAttachments.set(mineGuardian, ACExAttachments.MINE_GUARDIAN_VARIANT, -1);
                for (Player deepOne : level.getEntitiesOfClass(Player.class, mineGuardian.getBoundingBox().inflate(10))) {
                    ACExUtils.awardAdvancement(deepOne,"noon_guardian","noon");
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

        if (target instanceof NucleeperEntity nucleeper && itemStack.is(Items.SHEARS) && !nucleeper.getData(ACExAttachments.DEFUSED) && AlexsCavesExemplified.COMMON_CONFIG.DEFUSION_ENABLED.get()){
            ACExUtils.awardAdvancement(player,"defusing","defuse");
            nucleeper.setTriggered(false);
            ACExUtils.spawnLoot(ACExLootTables.NUCLEEPER_DEFUSION,nucleeper,player,1);
            nucleeper.playSound(ACSoundRegistry.NUCLEAR_BOMB_DEFUSE.get());
            ACExAttachments.set(nucleeper, ACExAttachments.DEFUSED, true);
            player.swing(hand);
            if (!player.isCreative()) {
                itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
        }

        if (target instanceof MineGuardianEntity mineGuardian && AlexsCavesExemplified.COMMON_CONFIG.NAVAL_NUCLEARITY_ENABLED.get() && itemStack.is(ACBlockRegistry.NUCLEAR_BOMB.get().asItem()) && mineGuardian.getData(ACExAttachments.MINE_GUARDIAN_VARIANT) == 0 && !mineGuardian.getData(ACExAttachments.MINE_GUARDIAN_OWNER).equals("-1")){
            ACExAttachments.set(mineGuardian, ACExAttachments.MINE_GUARDIAN_VARIANT, mineGuardian.getRandom().nextBoolean() ? 1 : 2);
            if (!player.isCreative()){
                itemStack.shrink(1);
            }
            mineGuardian.playSound(SoundEvents.SMITHING_TABLE_USE, 8.0F, 1.0F);
            ACExUtils.awardAdvancement(player,"nuclear_mines","nuke_mine");
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }

        if (target instanceof UnderzealotEntity underzealot && underzealot.getPassengers().isEmpty() && !underzealot.isPraying() && AlexsCavesExemplified.COMMON_CONFIG.DARK_OFFERING_ENABLED.get()) {
            boolean respect = (player.getItemBySlot(EquipmentSlot.CHEST).is(ACItemRegistry.CLOAK_OF_DARKNESS.get()) && player.getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.HOOD_OF_DARKNESS.get()));
            for (Mob leashedEntities : level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(10))) {
                boolean lasso = ModList.get().isLoaded("alexsmobs") && AMCompat.isLeashed(leashedEntities,player);
                if ((leashedEntities.getLeashHolder() == player || lasso)  && leashedEntities instanceof UnderzealotSacrifice && underzealot.getTarget() != player && !leashedEntities.getData(ACExAttachments.SACRIFICE_GIVEN)) {
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
                    leashedEntities.setData(ACExAttachments.SACRIFICE_GIVEN, true);
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
                submarine.playSound(SoundEvents.NOTE_BLOCK_BIT.value(), 1.0F, 1.6F);
            }
            event.getEntity().swing(event.getHand());
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void blockDrops(BlockDropsEvent dropsEvent) {
        BlockState state = dropsEvent.getState();
        if (!AlexsCavesExemplified.COMMON_CONFIG.IRRADIATED_CROPS_ENABLED.get() || !(state.getBlock() instanceof CropBlock || state.is(BlockTags.CROPS)))
            return;
        if (!dropsEvent.getLevel().getBiome(dropsEvent.getPos()).is(ACBiomeRegistry.TOXIC_CAVES))
            return;
        for (ItemEntity drop : dropsEvent.getDrops()) {
            ItemStack stack = drop.getItem();
            if (stack.has(DataComponents.FOOD)) {
                stack.set(ACExDataComponents.IRRADIATED, Unit.INSTANCE);
            }
        }
    }

    @SubscribeEvent
    public void finishUsingItem(LivingEntityUseItemEvent.Finish useEvent) {
        if (useEvent.getItem().has(ACExDataComponents.IRRADIATED) && useEvent.getItem().has(DataComponents.FOOD) && !useEvent.getEntity().level().isClientSide) {
            useEvent.getEntity().addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED, IRRADIATED_CROP_TICKS, 0));
        }
    }

    @SubscribeEvent
    public void livingIncomingDamage(LivingIncomingDamageEvent incomingDamageEvent) {
        LivingEntity damaged = incomingDamageEvent.getEntity();
        DamageSource source = incomingDamageEvent.getSource();

        if (ACExArmedUnderzealot.armedAttacker(source) != null) {
            incomingDamageEvent.setAmount(ACExArmedUnderzealot.daggerDamage());
        }

        if (source.getEntity() instanceof LivingEntity && damaged.hasEffect(ACExEffects.SERENED)){
            MobEffectInstance serene = damaged.getEffect(ACExEffects.SERENED);
            if (serene != null) {
                damaged.removeEffect(serene.getEffect());
                if (serene.getDuration() > ACExUtils.seconds(5.0F)) {
                    damaged.addEffect(new MobEffectInstance(serene.getEffect(), serene.getDuration() - ACExUtils.seconds(50.0F), serene.getAmplifier()));
                }
            }
        }

        if (damaged instanceof NotorEntity notor && AlexsCavesExemplified.COMMON_CONFIG.SELF_DESTRUCT_ENABLED.get() && notor.getData(ACExAttachments.SELF_DESTRUCT_TIME) <= 0) {
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
                player.setData(ACExAttachments.RADIANT_METER, player.getData(ACExAttachments.RADIANT_METER) + 1);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 1.0F, 1.2F);
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new ACExRadiantConsumePacket(player.getId()));
            }
            player.swing(hand);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }


        if(AlexsCavesExemplified.COMMON_CONFIG.IRRADIATION_WASHOFF_ENABLED.get() && ModList.get().isLoaded("supplementaries")){
            MobEffectInstance irradiated = player.getEffect(ACEffectRegistry.IRRADIATED);
            if (irradiated != null && SupplementariesCompat.isSoap(player.getItemInHand(hand)) && (player.isInWater() || player.getBlockStateOn().is(Blocks.WATER_CAULDRON))) {
                ACExUtils.irradiationWash(player,player.getRandom().nextInt(200,500));
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

            String bombLocation = switch (otherStack.getItem().toString()) {
                case "tnt" -> "minecraft:tnt";
                case "nuclear_bomb" -> "alexscaves:nuclear_bomb";
                case "gamma_nuclear_bomb" -> "alexscavesexemplified:gamma_nuclear_bomb";
                default -> null;
            };


            if (bombLocation == null)
                return;
            Optional<EntityType<?>> test = EntityType.byString(bombLocation);

            if (test.isPresent() && !player.level().isClientSide && !player.getCooldowns().isOnCooldown(otherStack.getItem())){
                EntityType bomb2 = test.get();
                player.level().gameEvent(player, GameEvent.PRIME_FUSE, player.blockPosition());
                player.level().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
                player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 60);
                player.swing(hand);
                bomb2.spawn((ServerLevel) level, BlockPos.containing(player.getX(), player.getY() - 0.5, player.getZ()), MobSpawnType.MOB_SUMMONED);
                if (!player.isCreative()) {
                    otherStack.shrink(1);
                }
                ACExUtils.awardAdvancement(player, "kirov_reporting", "kirov");
            }

        }

    }

    @SubscribeEvent
    public void livingDamage(LivingDamageEvent.Post livingDamageEvent) {
        Entity damager = livingDamageEvent.getSource().getEntity();
        LivingEntity damaged = livingDamageEvent.getEntity();

        UnderzealotEntity armedUnderzealot = ACExArmedUnderzealot.armedAttacker(livingDamageEvent.getSource());
        if (armedUnderzealot != null && damaged.isAlive()) {
            ACExArmedUnderzealot.summonBlade(armedUnderzealot, damaged);
        }


        if(AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get() && damager instanceof LivingEntity living && living.hasEffect(ACExEffects.RABIAL) && damaged.getType().is(ACExEntityTagGenerator.CAN_RABIES) && !damaged.hasEffect(MobEffects.DAMAGE_RESISTANCE)){
            damaged.addEffect(new MobEffectInstance(ACExEffects.RABIAL, ACExUtils.seconds(3600.0F), 0));
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
        Player entity = bonemealEvent.getPlayer();
        Level level = bonemealEvent.getLevel();
        BlockPos blockPos = bonemealEvent.getPos();
        BlockState blockState = level.getBlockState(blockPos);

        if (AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get() && entity != null && blockState.is(ACBlockRegistry.PING_PONG_SPONGE.get()) && level.getBiome(blockPos).is(ACBiomeRegistry.ABYSSAL_CHASM)) {
            ACExUtils.deepReputation(entity,1);
        }

    }

    @SubscribeEvent
    public void talkEvent(ServerChatEvent serverChatEvent){
        Player player = serverChatEvent.getPlayer();
        String message = serverChatEvent.getMessage().getString();

        if (message.contains("pspspsps") && AlexsCavesExemplified.COMMON_CONFIG.CATTASTROPHE_ENABLED.get()){

            int delay = 0;
            while (delay < 200) {
                delay++;
                if (delay >= 200){
                    for (LivingEntity cats : player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(4, 4, 4))) {
                        if (cats instanceof Cat || cats instanceof Ocelot || cats instanceof RaycatEntity || AMCompat.tiger(cats)) {
                            NuclearExplosionEntity explosion = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(cats.level());
                            explosion.copyPosition(cats);
                            explosion.setSize(1.75F);

                            cats.level().addFreshEntity(explosion);
                            cats.discard();
                        }
                    }
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get() && player.getRandom().nextDouble() < 0.01 && player.hasEffect(ACExEffects.RABIAL)){
            serverChatEvent.setMessage(Component.nullToEmpty("rrRRRrrrAgh!... " + message));
        }
    }


    private void checkLeatherArmor(ItemStack item, LivingEntity living){
        if (item.is(ItemTags.DYEABLE) && !item.has(DataComponents.DYED_COLOR)) {
            item.set(DataComponents.DYED_COLOR, new DyedItemColor(0Xb839e6, true));
            ACExUtils.awardAdvancement(living,"purple_coloring","colored");
        }
    }

}
