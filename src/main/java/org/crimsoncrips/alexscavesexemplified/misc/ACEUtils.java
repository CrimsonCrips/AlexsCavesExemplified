package org.crimsoncrips.alexscavesexemplified.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.CreateCompat;

import static com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil.getEntityMagneticDelta;
import static com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil.setEntityMagneticDelta;

public class ACEUtils {
    public static void dropMagneticItem(Player player, ItemStack item){
        ItemEntity itementity = new ItemEntity(player.level(), player.getX(), player.getEyeY(), player.getZ(), item);
        itementity.setPickUpDelay(60);
        itementity.setDeltaMovement(Vec3.ZERO);
        player.level().addFreshEntity(itementity);
        setEntityMagneticDelta(itementity,getEntityMagneticDelta(player));
        setEntityMagneticDelta(player,Vec3.ZERO);
        itementity.setGlowingTag(true);
        if (player instanceof ServerPlayer serverPlayer){
            ACEUtils.awardAdvancement(serverPlayer,"magnerip","ripped");
        }
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

    public static void awardAdvancement(Entity entity, String advancementName, String criteria){
        if(entity instanceof ServerPlayer serverPlayer){
            Advancement advancement = serverPlayer.serverLevel().getServer().getAdvancements().getAdvancement(new ResourceLocation(AlexsCavesExemplified.MODID, advancementName));
            if (advancement != null) {
                serverPlayer.getAdvancements().award(advancement, criteria);
            }
        }
    }

    public static void deepReputation(Entity entity,int amount){
        for (DeepOneBaseEntity deepOne : entity.level().getEntitiesOfClass(DeepOneBaseEntity.class, entity.getBoundingBox().inflate(40))) {
            if (entity instanceof Player player) {
                deepOne.addReputation(player.getUUID(),amount);
                ACEUtils.awardAdvancement(player,"ecological_reputation","affect");
            }
        }
    }

    public static void irradiationWash(LivingEntity entity,int amount){
        if (AlexsCavesExemplified.COMMON_CONFIG.IRRADIATION_WASHOFF_ENABLED.get()){
            MobEffectInstance irradiated = entity.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null) {
                entity.removeEffect(irradiated.getEffect());
                if (irradiated.getDuration() > amount) {
                    entity.addEffect(new MobEffectInstance(irradiated.getEffect(), irradiated.getDuration() - amount, irradiated.getAmplifier()));
                }
                ACEUtils.awardAdvancement(entity,"washing_radiation","wash");
            }
        }
    }

}
