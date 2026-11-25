package org.crimsoncrips.alexscavesexemplified.server;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.ACExDamageTypes;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExEntityTagGenerator;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;

public class ACExDepthCrush {

    public static final int CRUSH_HEIGHT = 64;
    private static final int CRUSH_INTERVAL = ACExUtils.seconds(1.0F);
    private static final float BASE_DAMAGE = 1.0F;
    private static final float DAMAGE_PER_BLOCK = 0.05F;
    private static final float AIR_PER_BLOCK = 0.5F;
    private static final int MIN_AIR = -20;

    public static void tick(Player player) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.ABYSSAL_CRUSH_ENABLED.get() || player.level().isClientSide || player.tickCount % CRUSH_INTERVAL != 0 || player.isCreative() || player.isSpectator()) {
            return;
        }
        Entity vehicle = player.getVehicle();
        if (vehicle != null && vehicle.getType().is(ACExEntityTagGenerator.CRUSH_IMMUNITY)) {
            return;
        }
        if (!player.isEyeInFluid(FluidTags.WATER)) {
            return;
        }
        Level level = player.level();
        BlockPos eyePos = BlockPos.containing(player.getEyePosition());
        if (!level.getBiome(eyePos).is(ACBiomeRegistry.ABYSSAL_CHASM)) {
            return;
        }
        int excess = crushHeight(player) - (eyePos.getY() - level.getMinBuildHeight());
        if (excess <= 0) {
            return;
        }
        player.hurt(ACExDamageTypes.getDamageSource(level, ACExDamageTypes.DEPTH_CRUSH), BASE_DAMAGE + excess * DAMAGE_PER_BLOCK);
        player.setAirSupply(Math.max(player.getAirSupply() - Mth.ceil(excess * AIR_PER_BLOCK), MIN_AIR));
    }

    public static int crushHeight(LivingEntity entity) {
        int height = CRUSH_HEIGHT;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor()) {
                ACExDataMaps.DepthResistance resistance = ACExDataMaps.DEPTH_RESISTANCE.get(entity.getItemBySlot(slot).getItem());
                if (resistance != null) {
                    height -= resistance.depth();
                }
            }
        }
        return height;
    }
}
