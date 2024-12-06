package org.crimsoncrips.alexscavesexemplified.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACEDamageTypes {

    public static final ResourceKey<DamageType> RABIAL_WATER = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(AlexsCavesExemplified.MODID, "rabial_water"));
    public static final ResourceKey<DamageType> RABIAL_END = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(AlexsCavesExemplified.MODID, "rabial_end"));

    public static DamageSource causeRabialWaterDamage(RegistryAccess registryAccess) {
        return new DamageSourceRandomMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(RABIAL_WATER), 1);
    }
    public static DamageSource causeEndRabialDamage(RegistryAccess registryAccess) {
        return new DamageSourceRandomMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(RABIAL_END), 1);
    }

    private static class DamageSourceRandomMessages extends DamageSource {

        private int messageCount;

        public DamageSourceRandomMessages(Holder.Reference<DamageType> message, int messageCount) {
            super(message);
            this.messageCount = messageCount;
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity attacked) {
            int type = attacked.getRandom().nextInt(this.messageCount);
            String s = "death.attack." + this.getMsgId() + "_" + type;
            Entity entity = this.getDirectEntity() == null ? this.getEntity() : this.getDirectEntity();
            if (entity != null) {
                return Component.translatable(s + ".entity", attacked.getDisplayName(), entity.getDisplayName());
            }else{
                return Component.translatable(s, attacked.getDisplayName());
            }
        }
    }
}