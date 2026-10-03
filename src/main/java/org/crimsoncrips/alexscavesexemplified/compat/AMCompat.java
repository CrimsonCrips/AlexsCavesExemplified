package org.crimsoncrips.alexscavesexemplified.compat;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACExFlyToTrap;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.*;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import net.minecraft.util.RandomSource;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class AMCompat {
    public static Entity multipartHead(Entity entity) {
        Entity current = entity;
        for (int i = 0; i < 256; i++) {
            Entity parent = current instanceof EntityAnacondaPart part ? part.getParent() : current instanceof EntityBoneSerpentPart part ? part.getParent() : current instanceof EntityVoidWormPart part ? part.getParent() : null;
            if (parent == null || parent == current)
                return current;
            current = parent;
        }
        return current;
    }

    public static void amberReset(LivingEntity entity) {
        if (entity instanceof EntityCockroach roach && roach.isNoAi()) {
            roach.setNoAi(false);
            roach.setInvulnerable(false);
            roach.setSilent(false);
            roach.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ACExUtils.seconds(300.0F), 1, false, false));
        }
        if (entity instanceof EntityFly fly && fly.isNoAi()) {
            fly.setNoAi(false);
            fly.setInvulnerable(false);
            fly.setSilent(false);
            fly.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ACExUtils.seconds(300.0F), 1, false, false));
        }
    }
    public static void vineLassoTo(LivingEntity lassoer,LivingEntity lassoed) {
        VineLassoUtil.lassoTo(lassoer, lassoed);
    }
    public static LivingEntity createAmberAM(Level level, RandomSource randomSource){
        if (randomSource.nextBoolean()){
            EntityCockroach cockroach = AMEntityRegistry.COCKROACH.get().create(level);
            if (cockroach != null) {
                cockroach.setNoAi(true);
                cockroach.setBaby(randomSource.nextDouble() < 0.5);
            }
            return cockroach;
        } else {
            EntityFly fly = AMEntityRegistry.FLY.get().create(level);
            if (fly != null) {
                fly.setNoAi(true);
                fly.setBaby(randomSource.nextDouble() < 0.5);
            }
            return fly;
        }
    }
    public static boolean isLeashed(LivingEntity lassoed,Player holder){
        return VineLassoUtil.getLassoedTo(lassoed) == holder;
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


    public static Item amItemRegistry(int num){
        return switch (num) {
            case 1 -> AMItemRegistry.VINE_LASSO.get();

            default -> throw new IllegalStateException("Unexpected value: " + num);
        };
    }

    public static Holder<MobEffect> exsanguination(){
        return AMEffectRegistry.EXSANGUINATION;
    }

    public static void addFlyGoals(Entity entity){
        if (entity instanceof EntityFly fly) {
            fly.goalSelector.addGoal(4, new ACExFlyToTrap(fly, 0.7, 10));
        }
    }

}
