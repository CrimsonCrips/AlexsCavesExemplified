package org.crimsoncrips.alexscavesexemplified.compat;

import com.github.alexthe666.alexsmobs.entity.*;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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

    public static EntityType amEntityType(int num){
        return switch (num) {
            case 1 -> AMEntityRegistry.FLYING_FISH.get();
            case 2 -> AMEntityRegistry.BLOBFISH.get();
            case 3 -> AMEntityRegistry.COSMIC_COD.get();
            case 4 -> AMEntityRegistry.DEVILS_HOLE_PUPFISH.get();
            case 5 -> AMEntityRegistry.CATFISH.get();
            case 6 -> AMEntityRegistry.ANTEATER.get();
            case 7 -> AMEntityRegistry.BISON.get();
            case 8 -> AMEntityRegistry.CACHALOT_WHALE.get();
            case 9 -> AMEntityRegistry.CAPUCHIN_MONKEY.get();
            case 10 -> AMEntityRegistry.DROPBEAR.get();
            case 11 -> AMEntityRegistry.ELEPHANT.get();
            case 12 -> AMEntityRegistry.ENDERGRADE.get();
            case 13 -> AMEntityRegistry.FROSTSTALKER.get();
            case 14 -> AMEntityRegistry.GAZELLE.get();
            case 15 -> AMEntityRegistry.GELADA_MONKEY.get();
            case 16 -> AMEntityRegistry.GORILLA.get();
            case 17 -> AMEntityRegistry.GRIZZLY_BEAR.get();
            case 18 -> AMEntityRegistry.JERBOA.get();
            case 19 -> AMEntityRegistry.KANGAROO.get();
            case 20 -> AMEntityRegistry.MANED_WOLF.get();
            case 21 -> AMEntityRegistry.MOOSE.get();
            case 22 -> AMEntityRegistry.BUNFUNGUS.get();
            case 23 -> AMEntityRegistry.MURMUR.get();
            case 24 -> AMEntityRegistry.ORCA.get();
            case 25 -> AMEntityRegistry.PLATYPUS.get();
            case 26 -> AMEntityRegistry.RACCOON.get();
            case 27 -> AMEntityRegistry.RHINOCEROS.get();
            case 28 -> AMEntityRegistry.SEA_BEAR.get();
            case 29 -> AMEntityRegistry.SEAL.get();
            case 30 -> AMEntityRegistry.SKUNK.get();
            case 31 -> AMEntityRegistry.MANED_WOLF.get();
            case 32 -> AMEntityRegistry.SNOW_LEOPARD.get();
            case 33 -> AMEntityRegistry.SUGAR_GLIDER.get();
            case 34 -> AMEntityRegistry.TASMANIAN_DEVIL.get();
            case 35 -> AMEntityRegistry.TIGER.get();
            case 36 -> AMEntityRegistry.TUSKLIN.get();
            case 37 -> AMEntityRegistry.FLY.get();
            case 38 -> AMEntityRegistry.COCKROACH.get();


            default -> throw new IllegalStateException("Unexpected value: " + num);
        };
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

    public static Item amItemRegistry(int num){
        return switch (num) {
            case 1 -> AMItemRegistry.SKELEWAG_SWORD.get();
            case 2 -> AMItemRegistry.FISH_BONES.get();
            case 3 -> AMItemRegistry.VINE_LASSO.get();

            default -> throw new IllegalStateException("Unexpected value: " + num);
        };
    }

    public static boolean gumbeeperCheck(LivingEntity living){
        return living instanceof EntityRaccoon entityRaccoon && entityRaccoon.isRigby();
    }

}