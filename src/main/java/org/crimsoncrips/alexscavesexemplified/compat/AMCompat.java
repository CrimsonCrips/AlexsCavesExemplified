package org.crimsoncrips.alexscavesexemplified.compat;

import org.crimsoncrips.alexscavesexemplified.server.goals.ACExFlyToTrap;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.*;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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

    public static void vineLassoTo(LivingEntity lassoer,LivingEntity lassoed) {
        VineLassoUtil.lassoTo(lassoer, lassoed);
    }
    public static Mob createPreserved(Level level, boolean fly, boolean baby){
        AgeableMob mob = fly ? AMEntityRegistry.FLY.get().create(level) : AMEntityRegistry.COCKROACH.get().create(level);
        if (mob != null) {
            mob.setBaby(baby);
        }
        return mob;
    }
    public static boolean isLeashed(LivingEntity lassoed,Player holder){
        return VineLassoUtil.getLassoedTo(lassoed) == holder;
    }

    public static boolean fly (LivingEntity livingEntity){
        return livingEntity instanceof EntityFly;
    }
    public static Item amItemRegistry(int num){
        return switch (num) {
            case 1 -> AMItemRegistry.VINE_LASSO.get();

            default -> throw new IllegalStateException("Unexpected value: " + num);
        };
    }

    public static MobEffect exsanguination(){
        return AMEffectRegistry.EXSANGUINATION.get();
    }

    public static void addFlyGoals(Entity entity){
        if (entity instanceof EntityFly fly) {
            fly.goalSelector.addGoal(4, new ACExFlyToTrap(fly, 0.7, 10));
        }
    }

}
