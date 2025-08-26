package org.crimsoncrips.alexscavesexemplified.compat;

import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import com.github.alexthe666.alexsmobs.entity.EntityTiger;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import net.hellomouse.alexscavesenriched.ACEEntityRegistry;
import net.hellomouse.alexscavesenriched.entity.BlackHoleBombEntity;
import net.hellomouse.alexscavesenriched.entity.MiniNukeEntity;
import net.hellomouse.alexscavesenriched.entity.NeutronBombEntity;
import net.hellomouse.alexscavesenriched.entity.NuclearExplosion2Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class ACEnrichedCompat {

    public static NuclearExplosion2Entity nuclearExplosion2(Level level){
        return (NuclearExplosion2Entity) ((EntityType<?>) ACEEntityRegistry.NUCLEAR_EXPLOSION2.get()).create(level);
    }

    public static void enrichedBomb(LivingEntity entity,int fuse,int type){
        switch (type) {
            case 1 -> {
                MiniNukeEntity bomb = ACEEntityRegistry.MINI_NUKE.get().create(entity.level());
                if (bomb == null)
                    return;
                bomb.setPos(entity.getPosition(1).add(0,-2,0));
                bomb.level().addFreshEntity(bomb);
                bomb.setFuse(fuse);
            }

            case 2 -> {
                NeutronBombEntity bomb = ACEEntityRegistry.NEUTRON_BOMB.get().create(entity.level());
                if (bomb == null)
                    return;
                bomb.setPos(entity.getPosition(1).add(0,-2,0));
                bomb.level().addFreshEntity(bomb);
                bomb.setFuse(fuse);
            }

            case 3 -> {
                BlackHoleBombEntity bomb = ACEEntityRegistry.BLACK_HOLE_BOMB.get().create(entity.level());
                if (bomb == null)
                    return;
                bomb.setPos(entity.getPosition(1).add(0,-2,0));
                bomb.level().addFreshEntity(bomb);
                bomb.setFuse(fuse);
            }

            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

}