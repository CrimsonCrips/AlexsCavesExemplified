package org.crimsoncrips.alexscavesexemplified.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import java.util.Deque;
import java.util.ArrayDeque;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.github.alexmodguy.alexscaves.server.level.storage.ACWorldData;
import com.github.alexmodguy.alexscaves.server.entity.util.DeepOneReaction;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import java.util.Locale;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import net.minecraft.network.chat.Component;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.Advancement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import org.crimsoncrips.alexscavesexemplified.backport.PacketDistributor;
import org.crimsoncrips.alexscavesexemplified.networking.ACExToastPacket;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

import java.util.Random;

import static com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil.getEntityMagneticDelta;
import static com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil.setEntityMagneticDelta;

public class ACExUtils {
    public static int seconds(float seconds) {
        return Math.round(seconds * 20.0F);
    }

    public static long secondsInMillis(float seconds) {
        return Math.round(seconds * 1000.0D);
    }

    public static void sendToast(Player player, Component message) {
        sendToast(player, message, ACExToastPacket.DISPLAY_TIME_MS);
    }

    public static void sendToast(Player player, Component message, long displayTimeMs) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new ACExToastPacket(message, displayTimeMs));
        }
    }

    public static void dropMagneticItem(Player player, ItemStack item){
        ItemEntity itementity = new ItemEntity(player.level(), player.getX(), player.getEyeY(), player.getZ(), item);
        itementity.setPickUpDelay(seconds(3.0F));
        itementity.setDeltaMovement(Vec3.ZERO);
        player.level().addFreshEntity(itementity);
        setEntityMagneticDelta(itementity,getEntityMagneticDelta(player));
        setEntityMagneticDelta(player,Vec3.ZERO);
        itementity.setGlowingTag(true);
        if (player instanceof ServerPlayer serverPlayer){
            ACExUtils.awardAdvancement(serverPlayer,"magnerip","ripped");
        }
    }

    public boolean chanceTrue(int level,int max) {
        if(level >= max) return true;   // 10 = 100%
        Random r = new Random();
        int roll = r.nextInt(max) + 1;  // 1-10
        return roll <= level;
    }

    public static void awardAdvancement(Entity entity, String advancementName, String criteria){
        if(entity instanceof ServerPlayer serverPlayer){
            Advancement advancement = serverPlayer.serverLevel().getServer().getAdvancements().getAdvancement(new ResourceLocation(AlexsCavesExemplified.MODID, advancementName));
            if (advancement != null) {
                serverPlayer.getAdvancements().award(advancement, criteria);
            }
        }
    }

    public static void awardOutsideAdvancement(Entity entity, String advancementName, String criteria,String modId){
        if(entity instanceof ServerPlayer serverPlayer){
            Advancement advancement = serverPlayer.serverLevel().getServer().getAdvancements().getAdvancement(new ResourceLocation(modId, advancementName));
            if (advancement != null) {
                serverPlayer.getAdvancements().award(advancement, criteria);
            }
        }
    }

    public static Entity getLookingAtEntity(Player player) {
        Entity closestValid = null;
        HitResult hitresult = ProjectileUtil.getHitResultOnViewVector(player, Entity::isAlive, 50);
        if (hitresult instanceof EntityHitResult entityHitResult) {
            Entity entity = entityHitResult.getEntity();
            if (!entity.equals(player) && player.hasLineOfSight(entity)) {
                closestValid = entity;
            }
        }
        return closestValid;
    }

//    public static Block getLookingAtBlock(Player player) {
//        Block closestValid = null;
//        HitResult hitresult = ProjectileUtil.getHitResultOnViewVector(player, Entity::isAlive, 32);
//        if (hitresult instanceof BlockHitResult blockHitResult) {
//            BlockState blockState = player.level().getBlockState(blockHitResult.getBlockPos());
//            closestValid = blockState.getBlock();
//        }
//        return closestValid;
//    }

    public static void spawnLoot (ResourceLocation location, LivingEntity entity, Entity owner, int loop){
        if (!entity.level().isClientSide){
            for (int i = 0; i <= loop; i++) {
                LootParams ctx = new LootParams.Builder((ServerLevel) entity.level()).withParameter(LootContextParams.THIS_ENTITY, entity).create(LootContextParamSets.EMPTY);
                ObjectArrayList<ItemStack> rewards = entity.level().getServer().getLootData().getLootTable(location).getRandomItems(ctx);
                rewards.forEach(stack -> BehaviorUtils.throwItem(entity, stack, owner.position().add(0.0D, 1.0D, 0.0D)));
            }
        }
    }

    public static void setGamma(TremorzillaEntity tremorzilla, boolean gamma){
        if (gamma) {
            tremorzilla.getAttribute(Attributes.MAX_HEALTH).setBaseValue(750F);
            tremorzilla.getAttribute(Attributes.ARMOR).setBaseValue(18F);
            tremorzilla.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(50F);
        }
        ACExAttachments.set(tremorzilla, ACExAttachments.GAMMA, gamma);
    }

    public static void setGamma(Entity entity, boolean gamma){
        if (entity instanceof TremorzillaEntity tremorzilla) {
            setGamma(tremorzilla, gamma);
        } else {
            ACExAttachments.set(entity, ACExAttachments.GAMMA, gamma);
        }
    }

    public static final int HEART_RIP_TICKS = seconds(5.0F);
    private static final int HEART_RIP_DECAY_DELAY = seconds(0.75F);

    public static boolean canStyleMineGuardian(MineGuardianEntity mineGuardian, Player player) {
        String owner = ACExData.get(mineGuardian, ACExAttachments.MINE_GUARDIAN_OWNER);
        return owner.equals("-1") || owner.equals(player.getStringUUID());
    }

    public static boolean restyleMineGuardian(MineGuardianEntity mineGuardian, Player player) {
        int variant = ACExData.get(mineGuardian, ACExAttachments.MINE_GUARDIAN_VARIANT);
        if ((variant != 1 && variant != 2) || !canStyleMineGuardian(mineGuardian, player))
            return false;
        if (!mineGuardian.level().isClientSide) {
            ACExAttachments.set(mineGuardian, ACExAttachments.MINE_GUARDIAN_VARIANT, variant == 1 ? 2 : 1);
            mineGuardian.playSound(SoundEvents.SMITHING_TABLE_USE, 1.0F, 1.2F);
        }
        return true;
    }

    public static boolean feedNoonGuardian(MineGuardianEntity mineGuardian, Player player, ItemStack pufferfish) {
        if (ACExData.get(mineGuardian, ACExAttachments.MINE_GUARDIAN_VARIANT) != 0 || !canStyleMineGuardian(mineGuardian, player))
            return false;
        if (!mineGuardian.level().isClientSide) {
            ACExAttachments.set(mineGuardian, ACExAttachments.MINE_GUARDIAN_VARIANT, -1);
            mineGuardian.playSound(SoundEvents.PUFFER_FISH_BLOW_UP, 1.0F, 1.0F);
            if (!player.isCreative()) {
                pufferfish.shrink(1);
            }
            awardAdvancement(player, "noon_guardian", "noon");
        }
        return true;
    }

    public static void ripMagnetronHeart(Entity magnetron, int value){
        ACExData.set(magnetron, ACExAttachments.RIPPED_HEART_DELAY, HEART_RIP_DECAY_DELAY);
        ACExAttachments.set(magnetron, ACExAttachments.RIPPED_HEART, value);
    }

    private static final double CHAIN_RANGE = 13.0D;
    private static final double CHAIN_ADVANCEMENT_RANGE = 50.0D;
    private static final int ANNIHILATION_CHAIN = 10;

    public static void spawnNucleeperNuke(Level level, NucleeperEntity nucleeper) {
        NuclearExplosionEntity explosion = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(level);
        if (explosion == null)
            return;
        explosion.copyPosition(nucleeper);
        explosion.setSize(nucleeper.isCharged() ? 1.75F : 1.0F);
        if (!level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            explosion.setNoGriefing(true);
        }
        level.addFreshEntity(explosion);
    }

    public static void chainDetonate(Level level, NucleeperEntity origin) {
        if (level.isClientSide || !AlexsCavesExemplified.COMMON_CONFIG.NUCLEAR_CHAIN_ENABLED.get())
            return;
        ACExData.set(origin, ACExAttachments.CHAIN_DETONATED, true);
        Deque<NucleeperEntity> pending = new ArrayDeque<>();
        pending.add(origin);
        int chained = 0;
        while (!pending.isEmpty()) {
            NucleeperEntity source = pending.poll();
            for (NucleeperEntity other : level.getEntitiesOfClass(NucleeperEntity.class, source.getBoundingBox().inflate(CHAIN_RANGE), other -> !ACExData.get(other, ACExAttachments.DEFUSED) && !ACExData.get(other, ACExAttachments.CHAIN_DETONATED))) {
                ACExData.set(other, ACExAttachments.CHAIN_DETONATED, true);
                spawnNucleeperNuke(level, other);
                other.discard();
                pending.add(other);
                chained++;
            }
        }
        if (chained > 0) {
            for (Player player : level.getEntitiesOfClass(Player.class, origin.getBoundingBox().inflate(CHAIN_ADVANCEMENT_RANGE))) {
                awardAdvancement(player, "chain_reaction", "chain");
                if (chained >= ANNIHILATION_CHAIN) {
                    awardAdvancement(player, "nucleeper_annhilation", "nuke_chained");
                }
            }
        }
    }

    public static void deepReputation(Entity entity, int amount) {
        if (entity instanceof Player player && changeDeepReputation(player, amount)) {
            ACExUtils.awardAdvancement(player, "ecological_reputation", "affect");
        }
    }

    public static boolean changeDeepReputation(Player player, int amount) {
        if (player.level().isClientSide)
            return false;
        ACWorldData worldData = ACWorldData.get(player.level());
        if (worldData == null)
            return false;
        int previous = worldData.getDeepOneReputation(player.getUUID());
        worldData.setDeepOneReputation(player.getUUID(), previous + amount);
        DeepOneReaction before = DeepOneReaction.fromReputation(previous);
        DeepOneReaction after = DeepOneReaction.fromReputation(worldData.getDeepOneReputation(player.getUUID()));
        if (before != after) {
            if (after == DeepOneReaction.NEUTRAL) {
                ACAdvancementTriggerRegistry.DEEP_ONE_NEUTRAL.triggerForEntity(player);
            }
            if (after == DeepOneReaction.HELPFUL) {
                ACAdvancementTriggerRegistry.DEEP_ONE_HELPFUL.triggerForEntity(player);
            }
            player.displayClientMessage(Component.translatable("entity.alexscaves.deep_one.reaction_" + after.toString().toLowerCase(Locale.ROOT)), true);
        }
        return true;
    }

    public static CompoundTag nbt(String name, Tag value){
        CompoundTag nbt = new CompoundTag();
        nbt.put(name,value);
        return nbt;
    }



    public static void irradiationWash(LivingEntity entity,int amount){
        if (AlexsCavesExemplified.COMMON_CONFIG.IRRADIATION_WASHOFF_ENABLED.get()){
            MobEffectInstance irradiated = entity.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null) {
                entity.removeEffect(irradiated.getEffect());
                if (irradiated.getDuration() > amount) {
                    entity.addEffect(new MobEffectInstance(irradiated.getEffect(), irradiated.getDuration() - amount, irradiated.getAmplifier()));
                }
                ACExUtils.awardAdvancement(entity,"washing_radiation","wash");
            }
        }
    }



    //give it up for reimnop again for the help cus im too retarded to know what the fuck to do
    static void yaw(PoseStack poseStack, float value) {
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(value, 0.0F, 1.0F, 0.0F)));
    }

    static void pitch(PoseStack poseStack, float value) {
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(value, 1.0F, 0.0F, 0.0F)));
    }

    static void roll(PoseStack poseStack, float value) {
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(value, 0.0F, 0.0F, 1.0F)));
    }


    public static boolean hasRadiantCharge(Player player) {
        return getRadiantCharges(player) > 0;
    }

    public static int getRadiantCharges(Player player) {
        if (AlexsCavesExemplified.COMMON_CONFIG.RADIANT_CONSUMPTION_ENABLED.get()) {
            return ACExData.get(player, ACExAttachments.RADIANT_METER);
        }
        ItemStack offhand = player.getOffhandItem();
        return offhand.is(ACItemRegistry.RADIANT_ESSENCE.get()) ? offhand.getCount() : 0;
    }

    public static void refundRadiantCharge(Player player, int amount) {
        if (amount <= 0)
            return;
        if (AlexsCavesExemplified.COMMON_CONFIG.RADIANT_CONSUMPTION_ENABLED.get()) {
            ACExData.set(player, ACExAttachments.RADIANT_METER, ACExData.get(player, ACExAttachments.RADIANT_METER) + amount);
        } else {
            ItemStack refund = new ItemStack(ACItemRegistry.RADIANT_ESSENCE.get(), amount);
            if (!player.getInventory().add(refund)) {
                player.drop(refund, false);
            }
        }
    }

    public static void useRadiantCharge(Player player) {
        useRadiantCharges(player, 1);
    }

    public static void useRadiantCharges(Player player, int amount) {
        if (AlexsCavesExemplified.COMMON_CONFIG.RADIANT_CONSUMPTION_ENABLED.get()) {
            ACExData.set(player, ACExAttachments.RADIANT_METER, Math.max(0, ACExData.get(player, ACExAttachments.RADIANT_METER) - amount));
        } else {
            player.getOffhandItem().shrink(amount);
        }
    }
}
