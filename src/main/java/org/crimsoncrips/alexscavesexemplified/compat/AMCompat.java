package org.crimsoncrips.alexscavesexemplified.compat;

import com.github.alexthe666.alexsmobs.entity.*;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class AMCompat {
    public static void amberReset(LivingEntity entity) {
        if (entity instanceof EntityCockroach roach && roach.isNoAi()) {
            roach.setNoAi(false);
            roach.setInvulnerable(false);
            roach.setSilent(false);
            roach.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 1, false, false));
        }
        if (entity instanceof EntityFly fly && fly.isNoAi()) {
            fly.setNoAi(false);
            fly.setInvulnerable(false);
            fly.setSilent(false);
            fly.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 1, false, false));
        }
    }
    public static LivingEntity createAmberAM(Level level){
        if (level.random.nextBoolean()){
            EntityCockroach cockroach = AMEntityRegistry.COCKROACH.get().create(level);
            if (cockroach != null) {
                cockroach.setNoAi(true);
                cockroach.setBaby(level.random.nextDouble() < 0.5);
            }
            return cockroach;
        } else {
            EntityFly fly = AMEntityRegistry.FLY.get().create(level);
            if (fly != null) {
                fly.setNoAi(true);
                fly.setBaby(level.random.nextDouble() < 0.5);
            }
            return fly;
        }
    }
    public static boolean isLeashed(LivingEntity lassoed,Player holder){
        return VineLassoUtil.getLassoedTo(lassoed) == holder;
    }

    public static Class amMob(int num){
        return switch (num) {
            case 1 -> EntityRaccoon.class;
            case 2 -> EntityBlueJay.class;
            case 3 -> EntityFly.class;
            case 4 -> EntityTiger.class;
            case 5 -> EntityCockroach.class;
            default -> LivingEntity.class;
        };
    }
    public static boolean fly (LivingEntity livingEntity){
        return livingEntity instanceof EntityFly;
    }
    public static boolean tiger (LivingEntity livingEntity){
        return livingEntity instanceof EntityTiger;
    }
    public static boolean cockroach (LivingEntity livingEntity){
        return livingEntity instanceof EntityCockroach;
    }

    public static Class<EntityRaccoon> entityRaccoon(){return EntityRaccoon.class;}

    public static boolean gumbeeperCheck(LivingEntity living){
        return living instanceof EntityRaccoon entityRaccoon && entityRaccoon.isRigby();
    }

}