package org.crimsoncrips.alexscavesexemplified.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.jetbrains.annotations.Nullable;

public class ACExDamageTypes {
    public static final ResourceKey<DamageType> DEPTH_CRUSH = create("depth_crush");
    public static final ResourceKey<DamageType> RABIAL_END = create("rabial_end");
    public static final ResourceKey<DamageType> STOMACH_DAMAGE = create("stomach_damage");
    public static final ResourceKey<DamageType> SUGAR_CRASH = create("sugar_crash");
    public static final ResourceKey<DamageType> SWEET_PUNISH = create("sweet_punish");


    public static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, AlexsCavesExemplified.prefix(name));
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type, EntityType<?>... toIgnore) {
        return getEntityDamageSource(level, type, null, toIgnore);
    }

    public static DamageSource getEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, EntityType<?>... toIgnore) {
        return getIndirectEntityDamageSource(level, type, attacker, attacker, toIgnore);
    }

    public static DamageSource getIndirectEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, @Nullable Entity indirectAttacker, EntityType<?>... toIgnore) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), attacker, indirectAttacker);
    }
            
    public static void bootstrap(BootstapContext<DamageType> context) {

        context.register(DEPTH_CRUSH, new DamageType("depth_crush", 0.0F));
        context.register(RABIAL_END, new DamageType("rabial_end", 0.0F));
        context.register(STOMACH_DAMAGE, new DamageType("stomach_damage", 1F));
        context.register(SWEET_PUNISH, new DamageType("sweet_punish", 0.0F));
        context.register(SUGAR_CRASH, new DamageType("sugar_crash", 0.0F));
    }
}
