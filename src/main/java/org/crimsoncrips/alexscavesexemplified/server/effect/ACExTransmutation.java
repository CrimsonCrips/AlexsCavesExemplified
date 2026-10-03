package org.crimsoncrips.alexscavesexemplified.server.effect;

import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExEntityTagGenerator;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.event.EventHooks;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ACExTransmutation extends InstantenousMobEffect {

    private static final List<Supplier<? extends EntityType<? extends Mob>>> LAND_CANDY = List.of(
            ACEntityRegistry.GUMBEEPER, ACEntityRegistry.CANDICORN, ACEntityRegistry.CARAMEL_CUBE, ACEntityRegistry.GUMMY_BEAR, ACEntityRegistry.GINGERBREAD_MAN);

    public ACExTransmutation() {
        super(MobEffectCategory.HARMFUL, 0XE84FD1);
    }

    public String getDescriptionId() {
        return "effect.alexscavesexemplified.transmutation.title";
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity living, int amplifier, double health) {
        transmute(living);
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        transmute(living);
        return true;
    }

    private static boolean canTransmute(LivingEntity living) {
        return living instanceof Mob && living.isAlive() && living.getType().is(ACExEntityTagGenerator.TRANSMUTABLE);
    }

    public static void transmute(LivingEntity living) {
        if (!(living.level() instanceof ServerLevel serverLevel) || !AlexsCavesExemplified.COMMON_CONFIG.LICOWITCH_TRANSMUTATION_ENABLED.get() || !canTransmute(living))
            return;
        Mob mob = (Mob) living;
        EntityType<? extends Mob> candy = mob.isInWater()
                ? ACEntityRegistry.SWEETISH_FISH.get()
                : LAND_CANDY.get(mob.getRandom().nextInt(LAND_CANDY.size())).get();
        if (!EventHooks.canLivingConvert(mob, candy, timer -> {}))
            return;
        Mob converted = mob.convertTo(candy, false);
        if (converted != null) {
            EventHooks.onLivingConvert(mob, converted);
            serverLevel.sendParticles(ParticleTypes.WITCH, converted.getX(), converted.getY(0.5D), converted.getZ(), 20, 0.3D, 0.4D, 0.3D, 0.05D);
            converted.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.4F);
        }
    }
}
