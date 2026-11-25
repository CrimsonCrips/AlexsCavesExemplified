package org.crimsoncrips.alexscavesexemplified.server.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExEntityRegistry {

    public static final DeferredRegister<EntityType<?>> DEF_REG = DeferredRegister.create(Registries.ENTITY_TYPE, AlexsCavesExemplified.MODID);

    public static final RegistryObject<EntityType<GammaNuclearBombEntity>> GAMMA_NUCLEAR_BOMB = DEF_REG.register("gamma_nuclear_bomb", () ->
            EntityType.Builder.<GammaNuclearBombEntity>of(GammaNuclearBombEntity::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .setUpdateInterval(1).setShouldReceiveVelocityUpdates(true)
                    .updateInterval(10).clientTrackingRange(20).build("gamma_nuclear_bomb"));

    public static final RegistryObject<EntityType<SiphonedSoulEntity>> SIPHONED_SOUL = DEF_REG.register("siphoned_soul", () ->
            EntityType.Builder.<SiphonedSoulEntity>of(SiphonedSoulEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(8).updateInterval(1)
                    .fireImmune().noSummon().build("siphoned_soul"));

    public static final RegistryObject<EntityType<CuriosityOfferingEntity>> CURIOSITY_OFFERING = DEF_REG.register("curiosity_offering", () ->
            EntityType.Builder.<CuriosityOfferingEntity>of(CuriosityOfferingEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.75F)
                    .clientTrackingRange(10).updateInterval(20)
                    .fireImmune().noSummon().build("curiosity_offering"));

    public static final RegistryObject<EntityType<DarkRitualEntity>> DARK_RITUAL = DEF_REG.register("dark_ritual", () ->
            EntityType.Builder.<DarkRitualEntity>of(DarkRitualEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(10).updateInterval(20)
                    .fireImmune().noSummon().build("dark_ritual"));

    public static final RegistryObject<EntityType<SpikesEntity>> SPIKES = DEF_REG.register("spikes", () ->
            EntityType.Builder.<SpikesEntity>of(SpikesEntity::new, MobCategory.MISC)
                    .sized(1.2F, 2.0F)
                    .clientTrackingRange(10).updateInterval(20)
                    .fireImmune().build("spikes"));

    public static final RegistryObject<EntityType<ToxicWasteEntity>> TOXIC_WASTE = DEF_REG.register("toxic_waste", () ->
            EntityType.Builder.<ToxicWasteEntity>of(ToxicWasteEntity::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10).updateInterval(1)
                    .fireImmune().build("toxic_waste"));

    public static final RegistryObject<EntityType<ToxicBarrelEntity>> TOXIC_BARREL = DEF_REG.register("toxic_barrel", () ->
            EntityType.Builder.<ToxicBarrelEntity>of(ToxicBarrelEntity::new, MobCategory.MISC)
                    .sized(0.98F * ToxicBarrelEntity.SCALE, 0.98F * ToxicBarrelEntity.SCALE)
                    .clientTrackingRange(12).updateInterval(1)
                    .fireImmune().build("toxic_barrel"));

    public static final RegistryObject<EntityType<GumwirmEntity>> GUMWIRM = DEF_REG.register("gumwirm", () ->
            EntityType.Builder.<GumwirmEntity>of(GumwirmEntity::new, MobCategory.MONSTER)
                    .sized(3.5F, 2.7F)
                    .setTrackingRange(14).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1)
                    .fireImmune().build("gumwirm"));

    public static final RegistryObject<EntityType<GumwirmSegmentEntity>> GUMWIRM_SEGMENT = DEF_REG.register("gumwirm_segment", () ->
            EntityType.Builder.<GumwirmSegmentEntity>of(GumwirmSegmentEntity::new, MobCategory.MISC)
                    .sized(0.2F, 0.2F)
                    .setTrackingRange(14).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true)
                    .fireImmune().build("gumwirm_segment"));

}

