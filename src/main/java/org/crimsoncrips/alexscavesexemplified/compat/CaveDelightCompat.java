package org.crimsoncrips.alexscavesexemplified.compat;

import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexthe666.alexsmobs.entity.*;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import net.thevaliantsquidward.cavedelight.entity.custom.RoastedGrottoceratopsEntity;
import net.thevaliantsquidward.cavedelight.item.ModItems;

public class CaveDelightCompat {

    public static boolean roastedCheck(TremorsaurusEntity tremorsaurus, BlockPos blockPos){
        if (ModList.get().isLoaded("cavedelight")) {
            for (RoastedGrottoceratopsEntity food : tremorsaurus.level().getEntitiesOfClass(RoastedGrottoceratopsEntity.class, new AABB(blockPos.offset(0, 1, 0), blockPos.offset(0, 2, 0)))) {
                if (food.getConsumptionStage() != 12) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void roastedConsume(TremorsaurusEntity tremorsaurus, BlockPos blockPos){
        for (Entity food : tremorsaurus.level().getEntitiesOfClass(Entity.class, new AABB(blockPos.offset(0, 1, 0), blockPos.offset(0, 2, 0)))) {
            if (food instanceof RoastedGrottoceratopsEntity roastedGrottoceratopsEntity && ModList.get().isLoaded("cavedelight")) {

                int consumptionStage = roastedGrottoceratopsEntity.getConsumptionStage();
                roastedGrottoceratopsEntity.setConsumptionStage(Math.min(12, consumptionStage + 3));
            }
        }
    }

}