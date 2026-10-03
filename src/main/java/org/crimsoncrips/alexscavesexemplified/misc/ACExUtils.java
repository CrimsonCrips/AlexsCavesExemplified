package org.crimsoncrips.alexscavesexemplified.misc;

import net.minecraft.world.entity.ai.attributes.Attributes;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import net.minecraft.network.chat.Component;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import org.crimsoncrips.alexscavesexemplified.networking.ACExToastPacket;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.CreateCompat;
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

    public static int getDivingAmount(LivingEntity entity) {
        int i = 0;
        if (entity.getItemBySlot(EquipmentSlot.HEAD).is((Item)ACItemRegistry.DIVING_HELMET.get())) {
            i = i + 1;
        }
        if (entity.getItemBySlot(EquipmentSlot.CHEST).is((Item)ACItemRegistry.DIVING_CHESTPLATE.get())) {
            i = i + 2;
        }
        if (entity.getItemBySlot(EquipmentSlot.LEGS).is((Item)ACItemRegistry.DIVING_LEGGINGS.get())) {
            i = i + 1;
        }
        if (entity.getItemBySlot(EquipmentSlot.FEET).is((Item)ACItemRegistry.DIVING_BOOTS.get())) {
            i = i + 1;
        }
        if(ModList.get().isLoaded("create")){
            i = i + CreateCompat.createDivingSuit(entity);
        }
        return i;
    }

    public static void awardAdvancement(Entity entity, String advancementName, String criteria){
        if(entity instanceof ServerPlayer serverPlayer){
            AdvancementHolder advancement = serverPlayer.serverLevel().getServer().getAdvancements().get(ResourceLocation.fromNamespaceAndPath(AlexsCavesExemplified.MODID, advancementName));
            if (advancement != null) {
                serverPlayer.getAdvancements().award(advancement, criteria);
            }
        }
    }

    public static void awardOutsideAdvancement(Entity entity, String advancementName, String criteria,String modId){
        if(entity instanceof ServerPlayer serverPlayer){
            System.out.println(modId);
            AdvancementHolder advancement = serverPlayer.serverLevel().getServer().getAdvancements().get(ResourceLocation.fromNamespaceAndPath(modId, advancementName));
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
                ObjectArrayList<ItemStack> rewards = entity.level().getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, location)).getRandomItems(ctx);
                if (!rewards.isEmpty()) {
                    rewards.forEach(stack -> BehaviorUtils.throwItem(entity, rewards.get(0), owner.position().add(0.0D, 1.0D, 0.0D)));
                }
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

    public static void ripMagnetronHeart(Entity magnetron, int value){
        magnetron.setData(ACExAttachments.RIPPED_HEART_DELAY, 15);
        ACExAttachments.set(magnetron, ACExAttachments.RIPPED_HEART, value);
    }

    public static void addSweets(Player player, int value){
        if (AlexsCavesExemplified.COMMON_CONFIG.SWEET_PUNISHMENT_ENABLED.get()) {
            int sweets = player.getData(ACExAttachments.SWEETS) + value;
            player.setData(ACExAttachments.SWEETS, sweets);
            if (sweets == 8 && !player.level().isClientSide) {
                if (player.getRandom().nextBoolean()) {
                    player.displayClientMessage(Component.translatable("misc.alexscavesexemplified.candy_warn_0"), true);
                } else player.displayClientMessage(Component.nullToEmpty("misc.alexscavesexemplified.candy_warn_1"), true);
            }
        }
    }

    public static void deepReputation(Entity entity,int amount){
        for (DeepOneBaseEntity deepOne : entity.level().getEntitiesOfClass(DeepOneBaseEntity.class, entity.getBoundingBox().inflate(40))) {
            if (entity instanceof Player player) {
                deepOne.addReputation(player.getUUID(),amount);
                ACExUtils.awardAdvancement(player,"ecological_reputation","affect");
            }
        }
    }

    public static CompoundTag nbt(String name, Tag value){
        CompoundTag nbt = new CompoundTag();
        nbt.put(name,value);
        return nbt;
    }



    public static void irradiationWash(LivingEntity entity,int amount){
        if (AlexsCavesExemplified.COMMON_CONFIG.IRRADIATION_WASHOFF_ENABLED.get()){
            MobEffectInstance irradiated = entity.getEffect(ACEffectRegistry.IRRADIATED);
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
            return player.getData(ACExAttachments.RADIANT_METER);
        }
        ItemStack offhand = player.getOffhandItem();
        return offhand.is(ACItemRegistry.RADIANT_ESSENCE.get()) ? offhand.getCount() : 0;
    }

    public static void refundRadiantCharge(Player player, int amount) {
        if (amount <= 0)
            return;
        if (AlexsCavesExemplified.COMMON_CONFIG.RADIANT_CONSUMPTION_ENABLED.get()) {
            player.setData(ACExAttachments.RADIANT_METER, player.getData(ACExAttachments.RADIANT_METER) + amount);
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
            player.setData(ACExAttachments.RADIANT_METER, Math.max(0, player.getData(ACExAttachments.RADIANT_METER) - amount));
        } else {
            player.getOffhandItem().shrink(amount);
        }
    }
}
