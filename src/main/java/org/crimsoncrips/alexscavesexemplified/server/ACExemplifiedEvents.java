package org.crimsoncrips.alexscavesexemplified.server;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.*;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.entity.item.MeltedCaramelEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.HazmatArmorItem;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.crimsoncrips.alexscavesexemplified.compat.SupplementariesCompat;
import org.crimsoncrips.alexscavesexemplified.datagen.ACEDamageTypes;
import org.crimsoncrips.alexscavesexemplified.datagen.ACEFeatures;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACEBlockTagGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACEEntityTagGenerator;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEBaseInterface;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.NucleeperXtra;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACEBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.*;

import static com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity.ANIMATION_DRINK_BARREL;
import static net.minecraft.world.entity.EntityType.*;


@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ACExemplifiedEvents {

    private static final AttributeModifier FAST_FALLING = new AttributeModifier(UUID.fromString("A5B6CF2A-2F7C-31EF-9022-7C3E7D5E6ABA"), "Fast falling acceleration reduction", 0.2, AttributeModifier.Operation.ADDITION); // Add -0.07 to 0.08 so we get the vanilla default of 0.01

    @SubscribeEvent
    public void onEntityFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        final var entity = event.getEntity();

        Double chargedChance = AlexsCavesExemplified.COMMON_CONFIG.CHARGED_CAVE_CREEPER_CHANCE.get();
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
                entity.addEffect(new MobEffectInstance(ACEEffects.RABIAL.get(), 140000, 0));
            }
        }

    }

    @SubscribeEvent
    public void onLevelJoin(EntityJoinLevelEvent event) {
        final var entity = event.getEntity();

        if (entity.getType().is(ACEEntityTagGenerator.CAN_RABIES) && entity instanceof Mob mob && AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get()){
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
        ItemStack itemStack = event.getItemStack();

        if (AlexsCavesExemplified.COMMON_CONFIG.GLUTTONY_ENABLED.get() && blockState.is(ACEBlockTagGenerator.CONSUMABLE_BLOCKS)) {
            ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, blockState);
            if (player.isCrouching()) {
                MobEffectInstance hunger = player.getEffect(MobEffects.HUNGER);
                if (hunger != null) {
                    ((ACEBaseInterface) player).addSweets(1);

                    if (!hunger.isInfiniteDuration()) {
                        player.removeEffect(MobEffects.HUNGER);
                        player.addEffect(new MobEffectInstance(MobEffects.HUNGER, hunger.getDuration() - 60, hunger.getAmplifier()));
                    }
                    worldIn.destroyBlock(pos, true,player);
                    player.getFoodData().eat(1, 1);
                    player.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    for (int i = 0; i <= 15; i++) {
                        Vec3 lookAngle = player.getLookAngle();
                        worldIn.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 1.5, lookAngle.y * 2, lookAngle.z * 1.5);
                    }
                    ACEUtils.awardAdvancement(player,"gluttony","eat");
                    if (random.nextDouble() < 0.01)
                        player.addEffect(new MobEffectInstance(ACEffectRegistry.SUGAR_RUSH.get(), 100, 0));


                } else if (player.getFoodData().needsFood()) {
                    ((ACEBaseInterface) player).addSweets(1);

                    player.getFoodData().eat(2, 2);
                    player.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    for (int i = 0; i <= 15; i++) {
                        Vec3 lookAngle = player.getLookAngle();
                        worldIn.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 1.5, lookAngle.y * 2, lookAngle.z * 1.5);
                    }
                    ACEUtils.awardAdvancement(player,"gluttony","eat");
                    if (random.nextDouble() < 0.01)
                        player.addEffect(new MobEffectInstance(ACEffectRegistry.SUGAR_RUSH.get(), 100, 0));

                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.LIQUID_REPLICATION_ENABLED.get() && blockState.is(Blocks.CAULDRON) && (itemStack.is(ACBlockRegistry.SCRAP_METAL.get().asItem()) || itemStack.is(ACBlockRegistry.SCRAP_METAL_PLATE.get().asItem())) && player.isCrouching()){
            event.getLevel().setBlock(pos, ACEBlockRegistry.METAL_CAULDRON.get().defaultBlockState(),3);
            player.swing(event.getHand());
            player.playSound(SoundEvents.SMITHING_TABLE_USE, 1F, 1F);
            ACEUtils.awardAdvancement(player,"metal_cauldron","metal");
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
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0,false,false));
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

        if(target instanceof SeaPigEntity && AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get() && level.getBiome(target.getOnPos()).is(ACBiomeRegistry.ABYSSAL_CHASM) && player.getItemInHand(player.getUsedItemHand()).is(ACBlockRegistry.MUCK.get().asItem())){
            ACEUtils.deepReputation(player,1);
        }


        if(event.getTarget() instanceof Parrot parrot && AlexsCavesExemplified.COMMON_CONFIG.COOKIE_CRUMBLE_ENABLED.get() && itemStack.is(ACBlockRegistry.COOKIE_BLOCK.get().asItem())){
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }

            parrot.addEffect(new MobEffectInstance(MobEffects.POISON, 900));
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

        if (died instanceof NucleeperEntity nucleeper && AlexsCavesExemplified.COMMON_CONFIG.NUCLEAR_CHAIN_ENABLED.get() && !((NucleeperXtra)nucleeper).alexsCavesExemplified$isDefused()){
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
                    if (AlexsCavesExemplified.COMMON_CONFIG.NUCLEAR_CHAIN_ENABLED.get() && !((NucleeperXtra)nucleepers).alexsCavesExemplified$isDefused()){
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
                        ACEUtils.awardAdvancement(players,"nucleeper_annhilation","nuke_chained");
                    }
                    if (amount != 0){
                        ACEUtils.awardAdvancement(players,"chain_reaction","chain");
                    }
                }
            }
        }

        if (died instanceof Player player && AlexsCavesExemplified.COMMON_CONFIG.MUTATED_DEATH_ENABLED.get()) {
            MobEffectInstance irradiated = player.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && irradiated.getAmplifier() >= 2 && player.getRandom().nextDouble() < 0.2) {
                ACEntityRegistry.BRAINIAC.get().spawn((ServerLevel) level, BlockPos.containing(player.getX(), player.getY(), player.getZ()), MobSpawnType.MOB_SUMMONED);
            }
        }

        if (died instanceof MineGuardianEntity mineGuardianEntity && ((MineGuardianXtra)mineGuardianEntity).alexsCavesExemplified$getVariant() >= 1) {
            ACEUtils.awardAdvancement(killer,"nuclear_kill","killed");
        }

        if (killer instanceof Player player && AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get()) {
            if (died instanceof SeaPigEntity){
                ACEUtils.deepReputation(player,-1);
            } else if (died instanceof GossamerWormEntity){
                ACEUtils.deepReputation(player,-2);
            } else if (died instanceof LanternfishEntity && player.getRandom().nextDouble() < 0.3){
                ACEUtils.deepReputation(player,-1);
            } else if (died instanceof TripodfishEntity){
                ACEUtils.deepReputation(player,-1);
            } else if (died instanceof HullbreakerEntity){
                ACEUtils.awardAdvancement(player,"hullbreaker_reputation","killed");
                ACEUtils.deepReputation(player,-10);
            } else if (died instanceof MineGuardianEntity){
                ACEUtils.deepReputation(player,2);
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.FISH_MUTATION_ENABLED.get() && died.getFeetBlockState().is(ACBlockRegistry.ACID.get()) && died.getType().is(ACEEntityTagGenerator.ACID_TO_FISH)  && !died.level().isClientSide() && died.getRandom().nextDouble() < 1){
            ACEntityRegistry.RADGILL.get().spawn((ServerLevel) level, BlockPos.containing(died.getX(), died.getY(), died.getZ()), MobSpawnType.MOB_SUMMONED);
            for (Player players : level.getEntitiesOfClass(Player.class, died.getBoundingBox().inflate(8))) {
                ACEUtils.awardAdvancement(players,"convert_fish","convert");
            }
            died.discard();
        }
        if (AlexsCavesExemplified.COMMON_CONFIG.CAT_MUTATION_ENABLED.get() && died.getFeetBlockState().is(ACBlockRegistry.ACID.get()) && died.getType().is(ACEEntityTagGenerator.ACID_TO_CAT)  && !died.level().isClientSide() && died.getRandom().nextDouble() < 1){
            ACEntityRegistry.RAYCAT.get().spawn((ServerLevel) level, BlockPos.containing(died.getX(), died.getY(), died.getZ()), MobSpawnType.MOB_SUMMONED);
            for (Player players : level.getEntitiesOfClass(Player.class, died.getBoundingBox().inflate(8))) {
                ACEUtils.awardAdvancement(players,"convert_cat","convert");
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
            if (blockState.is(ACEBlockTagGenerator.BURST_BLOCKS) && breakEvent.getLevel().getRandom().nextDouble() < 0.02) {
                if (level.getBiome(breakEvent.getPos()).is(ACBiomeRegistry.FORLORN_HOLLOWS)){
                    if (level.getRandom().nextBoolean()) {
                        ACEntityRegistry.UNDERZEALOT.get().spawn((ServerLevel) level, BlockPos.containing(breakEvent.getPos().getX(), breakEvent.getPos().getY(), breakEvent.getPos().getZ()), MobSpawnType.MOB_SUMMONED);
                    } else {
                        ACEntityRegistry.CORRODENT.get().spawn((ServerLevel) level, BlockPos.containing(breakEvent.getPos().getX(), breakEvent.getPos().getY(), breakEvent.getPos().getZ()), MobSpawnType.MOB_SUMMONED);
                    }
                    ACEUtils.awardAdvancement(player,"burst_out","burst");

                }

            }
            if (blockState.is(ACBlockRegistry.PEERING_COPROLITH.get()) && breakEvent.getLevel().getRandom().nextDouble() < 0.4){
                if (level.getBiome(breakEvent.getPos()).is(ACBiomeRegistry.FORLORN_HOLLOWS)){
                    ACEntityRegistry.CORRODENT.get().spawn((ServerLevel) level, BlockPos.containing(breakEvent.getPos().getX(), breakEvent.getPos().getY(), breakEvent.getPos().getZ()), MobSpawnType.MOB_SUMMONED);
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get()) {
            ACEUtils.deepReputation(player,-1);
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



        if (livingEntity instanceof SeaPigEntity seaPigEntity && level.random.nextDouble() < 0.01 && AlexsCavesExemplified.COMMON_CONFIG.POISONOUS_SKIN_ENABLED.get()) {
            for (LivingEntity entity : seaPigEntity.level().getEntitiesOfClass(LivingEntity.class, seaPigEntity.getBoundingBox().inflate(0.5))) {
                if (entity != seaPigEntity && entity.getBbHeight() <= 3.5F && !(entity instanceof SeaPigEntity)) {
                    entity.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
                    ACEUtils.awardAdvancement(entity,"poisonous_skin","touched");
                }
            }
        }

        if (livingEntity instanceof Player player && level.getBiome(player.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM) && AlexsCavesExemplified.COMMON_CONFIG.ABYSSAL_CRUSH_ENABLED.get()){
            int aboveWater = 0;
            BlockPos pos = new BlockPos(player.getBlockX(), (int) (player.getBlockY() + 2),player.getBlockZ());
            while (level.getBlockState(pos).is(Blocks.WATER)){
                pos = pos.above();
                aboveWater++;
            }
            int diving = ACEUtils.getDivingAmount(livingEntity);

            if (level.random.nextDouble() < (0.1 - (0.020 * diving)) && !(player.getVehicle() instanceof SubmarineEntity)){
                if (aboveWater > 50 && diving < 10){
                    player.hurt(ACEDamageTypes.getDamageSource(player.level(),ACEDamageTypes.DEPTH_CRUSH), (float) (0.025 * (aboveWater - 40)));
                    player.setAirSupply(player.getAirSupply() + (int) (0.025 * (aboveWater - 100)));
                }
            }
        }


        if (livingEntity instanceof Player player) {
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    for (int z = -1; z < 2; z++) {
                        BlockPos pickedBlock = new BlockPos(player.getBlockX() + x, player.getBlockY() + y , player.getBlockZ() + z);
                        BlockState blockState = level.getBlockState(pickedBlock);
                        if (AlexsCavesExemplified.COMMON_CONFIG.PEERING_TRIGGER_ENABLED.get() && blockState.is(ACBlockRegistry.PEERING_COPROLITH.get()) && (CuriosCompat.hasLight(livingEntity)) && level.random.nextDouble() < 0.1) {
                            if (player.getRandom().nextDouble() < 0.9) {
                                level.setBlock(pickedBlock, ACBlockRegistry.POROUS_COPROLITH.get().defaultBlockState(), 3);
                            } else if (!level.isClientSide) {
                                level.destroyBlock(pickedBlock, true, player);
                                ACEntityRegistry.CORRODENT.get().spawn((ServerLevel) level, pickedBlock, MobSpawnType.MOB_SUMMONED);
                            }
                        }
                        if (AlexsCavesExemplified.COMMON_CONFIG.RADIOACTIVE_AWARENESS_ENABLED.get() && blockState.is(ACEBlockTagGenerator.RADIOACTIVE) && HazmatArmorItem.getWornAmount(player) < 4 && level.random.nextDouble() < 0.5) {
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


        if (AlexsCavesExemplified.COMMON_CONFIG.DISORIENTED_ENABLED.get() && !level.isClientSide && livingEntity instanceof WatcherEntity watcherEntity){
            if (watcherEntity.getPossessedEntity() instanceof Player player) {
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0));
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.GUM_TRAMPLE_ENABLED.get() && livingEntity instanceof GumWormEntity gumWorm && gumWorm.isVehicle() && gumWorm.getFirstPassenger() instanceof Player){
            for (LivingEntity entity : gumWorm.level().getEntitiesOfClass(LivingEntity.class, gumWorm.getBoundingBox().inflate(1.2))) {
                if (entity != gumWorm && entity.getBbHeight() <= 3.5F) {
                    entity.hurt(gumWorm.damageSources().mobAttack(gumWorm), 1.0F);
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.WASTE_POWERUP_ENABLED.get() && livingEntity instanceof BrainiacEntity brainiac){
            if (!level.isClientSide && brainiac.hasBarrel()) {
                if (brainiac.getAnimation() == ANIMATION_DRINK_BARREL && brainiac.getAnimationTick() >= 60) {
                    brainiac.getPersistentData().putBoolean("WastePowered", true);
                }
            }
        }


        if (AlexsCavesExemplified.COMMON_CONFIG.PRESSURED_HOOKS_ENABLED.get()){
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

                if ((!trueMainhand || !trueOffhand) && livingEntity.getVehicle() instanceof GumWormSegmentEntity && AlexsCavesExemplified.COMMON_CONFIG.LOGICAL_RIDING_ENABLED.get()) {
                    livingEntity.removeVehicle();
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.AMBER_HEAL_ENABLED.get() && livingEntity.getFeetBlockState().is(ACBlockRegistry.AMBER.get()) && livingEntity.getRandom().nextDouble() < 0.01){
            if (livingEntity.getMobType() == MobType.UNDEAD){
                livingEntity.hurt(livingEntity.damageSources().generic(),2);
            } else livingEntity.heal(2);
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.GUANO_SLOW_ENABLED.get() && (livingEntity.getFeetBlockState().is(ACBlockRegistry.GUANO_BLOCK.get()) || livingEntity.getFeetBlockState().is(ACBlockRegistry.GUANO_LAYER.get()))){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 0));
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.SOLIDIFIED_WATCHER_ENABLED.get() && livingEntity instanceof WatcherEntity watcherEntity){
            if (watcherEntity.tickCount > 3000 && watcherEntity.getVehicle() == null && !watcherEntity.isVehicle()){
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
                for (Player player : level.getEntitiesOfClass(Player.class, watcherEntity.getBoundingBox().inflate(60))) {
                    ACEUtils.awardAdvancement(player,"solidified","solid");
                }
                watcherEntity.playSound(ACSoundRegistry.WATCHER_DEATH.get(), 6F, -5F);
                watcherEntity.discard();

            }
        }



        if (AlexsCavesExemplified.COMMON_CONFIG.STICKY_SODA_ENABLED.get() && livingEntity.getFeetBlockState().is(ACBlockRegistry.PURPLE_SODA.get()) && !livingEntity.getType().is(ACTagRegistry.CANDY_MOBS)){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 90, 0));
            ACEUtils.awardAdvancement(livingEntity,"sticky_soda","stick");
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.PURPLE_LEATHERED_ENABLED.get()) {
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.HEAD),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.FEET),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.CHEST),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.LEGS),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.MAINHAND),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.OFFHAND),livingEntity);
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.CRUMBY_RAGE_ENABLED.get()){
            Iterator<GingerbreadManEntity> var4 = level.getEntitiesOfClass(GingerbreadManEntity.class, livingEntity.getBoundingBox().inflate(10, 5, 10)).iterator();
            while (var4.hasNext()) {
                LivingEntity entity = var4.next();
                if (entity instanceof GingerbreadManEntity gingerbreadMan && livingEntity.getUseItem().is(ACItemRegistry.GINGERBREAD_CRUMBS.get())) {
                    gingerbreadMan.setTarget(livingEntity);
                    ACEUtils.awardAdvancement(livingEntity,"crumby_rage","raged");
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.FLY_TRAP_ENABLED.get()  && ModList.get().isLoaded("alexsmobs")){
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

        if(livingEntity.getRandom().nextDouble() < 0.05 && AlexsCavesExemplified.COMMON_CONFIG.IRRADIATION_WASHOFF_ENABLED.get() && (livingEntity.isInWater() || livingEntity.getBlockStateOn().is(Blocks.WATER_CAULDRON) || livingEntity.isInWaterRainOrBubble())){
            ACEUtils.irradiationWash(livingEntity,50);
        }

        BlockState blockState = livingEntity.getBlockStateOn();
        if (blockState.getBlock() instanceof GeothermalVentBlock){
            if (AlexsCavesExemplified.COMMON_CONFIG.GEOTHERMAL_EFFECTS_ENABLED.get()){
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 1 && livingEntity.getRandom().nextDouble() < 0.05){
                    ACEUtils.irradiationWash(livingEntity,50);
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

        int irradiationAmmount = AlexsCavesExemplified.COMMON_CONFIG.EXEMPLIFIED_IRRADIATION_AMOUNT.get();
        if(irradiationAmmount > 0){
            MobEffectInstance irradiated = livingEntity.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && irradiated.getAmplifier() >= irradiationAmmount - 1) {
                ACEUtils.awardAdvancement(livingEntity,"deathly_radiation","radiate");
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));

                if (ModList.get().isLoaded("alexsmobs"))
                    livingEntity.addEffect(new MobEffectInstance(AMEffectRegistry.EXSANGUINATION.get(), 60, 0));
            }
        }


    }

    @SubscribeEvent
    public void rightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level level = event.getLevel();

        ItemStack offHand = player.getOffhandItem();
        ItemStack mainHand = player.getMainHandItem();

        if(AlexsCavesExemplified.COMMON_CONFIG.IRRADIATION_WASHOFF_ENABLED.get() && ModList.get().isLoaded("supplementaries")){
            MobEffectInstance irradiated = player.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && SupplementariesCompat.isSoap(mainHand.getItem().getDefaultInstance()) && (player.isInWater() || player.getBlockStateOn().is(Blocks.WATER_CAULDRON))) {
                ACEUtils.irradiationWash(player,100);

                for (int i = 0; i < 10; i++){
                    double d1 = player.getRandom().nextGaussian() * 0.02;
                    double d2 = player.getRandom().nextGaussian() * 0.02;
                    double d3 = player.getRandom().nextGaussian() * 0.02;

                    level.addParticle(ModParticles.SUDS_PARTICLE.get(), player.getX(), player.getY() + 0.5, player.getZ(), d1 * 2, d2 * 2, d3 * 2);
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.KIROV_REPORTING_ENABLED.get() && player.isFallFlying() && (offHand.is(Items.FLINT_AND_STEEL) || offHand.is(Items.FIRE_CHARGE))){
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
                    ACEUtils.awardAdvancement(player,"kirov_reporting","kirov");
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
                    ACEUtils.awardAdvancement(player,"kirov_reporting","kirov");
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
                    ACEUtils.awardAdvancement(player,"kirov_reporting","kirov");
                }
            }

        }

    }

    @SubscribeEvent
    public void livingDamage(LivingDamageEvent livingDamageEvent) {
        Entity damager = livingDamageEvent.getSource().getEntity();
        LivingEntity damaged = livingDamageEvent.getEntity();


        if(AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get() && damager instanceof LivingEntity living && living.hasEffect(ACEEffects.RABIAL.get()) && damaged.getType().is(ACEEntityTagGenerator.CAN_RABIES) && !damaged.hasEffect(MobEffects.DAMAGE_RESISTANCE)){
            damaged.addEffect(new MobEffectInstance(ACEEffects.RABIAL.get(), 72000, 0));
            ACEUtils.awardAdvancement(damager,"rabial_spread","spread");
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
        Entity entity = bonemealEvent.getEntity();
        Level level = bonemealEvent.getLevel();
        BlockPos blockPos = bonemealEvent.getPos();
        BlockState blockState = level.getBlockState(blockPos);

        if (AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get() && blockState.is(ACBlockRegistry.PING_PONG_SPONGE.get()) && level.getBiome(blockPos).is(ACBiomeRegistry.ABYSSAL_CHASM)) {
            ACEUtils.deepReputation(entity,1);
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

        if (AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get() && player.getRandom().nextDouble() < 0.01 && player.hasEffect(ACEEffects.RABIAL.get())){
            serverChatEvent.setMessage(Component.nullToEmpty("rrRRRrrrAgh!... " + message));
        }
    }


    private void checkLeatherArmor(ItemStack item, LivingEntity living){
        if (item.getItem() instanceof DyeableLeatherItem dyeableLeatherItem && living.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get()) && !dyeableLeatherItem.hasCustomColor(item)) {
            dyeableLeatherItem.setColor(item, 0Xb839e6);
            ACEUtils.awardAdvancement(living,"purple_coloring","colored");
        }
    }

//    @SubscribeEvent
//    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
//        Player player = event.getEntity();
//        CompoundTag playerData = event.getEntity().getPersistentData();
//        CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
//
//        ItemStack book = new ItemStack(PatchouliItems.BOOK);
//        book.getOrCreateTag().putString("patchouli:book","alexscavesexemplified:ace_exemplified_wiki");
//
//        if (!data.getBoolean("ace_book") && AlexsCavesExemplified.COMMON_CONFIG.ACE_WIKI_ENABLED.get()) {
//            player.addItem(book);
//            data.putBoolean("ace_book", true);
//            playerData.put(Player.PERSISTED_NBT_TAG, data);
//        }
//    }






}

