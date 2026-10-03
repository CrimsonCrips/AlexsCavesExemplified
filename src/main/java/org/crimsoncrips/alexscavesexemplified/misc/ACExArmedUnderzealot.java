package org.crimsoncrips.alexscavesexemplified.misc;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.DesolateDaggerEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import javax.annotation.Nullable;

public class ACExArmedUnderzealot {

    private static final double HAND_DAMAGE = 1.0D;

    @Nullable
    public static UnderzealotEntity armedAttacker(DamageSource source) {
        if (source.is(DamageTypes.MOB_ATTACK) && source.getEntity() instanceof UnderzealotEntity underzealot && underzealot.getData(ACExAttachments.ARMED)
                && AlexsCavesExemplified.COMMON_CONFIG.ARMED_UNDERZEALOT_ENABLED.get()) {
            return underzealot;
        }
        return null;
    }

    public static ItemStack dagger() {
        return new ItemStack(ACItemRegistry.DESOLATE_DAGGER.get());
    }

    public static float daggerDamage() {
        double damage = HAND_DAMAGE;
        for (var entry : dagger().getAttributeModifiers().modifiers()) {
            if (entry.attribute().is(Attributes.ATTACK_DAMAGE) && entry.modifier().operation() == AttributeModifier.Operation.ADD_VALUE) {
                damage += entry.modifier().amount();
            }
        }
        return (float) damage;
    }

    public static void summonBlade(UnderzealotEntity underzealot, LivingEntity target) {
        DesolateDaggerEntity blade = ACEntityRegistry.DESOLATE_DAGGER.get().create(underzealot.level());
        if (blade == null)
            return;
        blade.setTargetId(target.getId());
        blade.setPlayerId(underzealot.getId());
        blade.copyPosition(underzealot);
        blade.setItemStack(dagger());
        blade.orbitFor = ACExUtils.seconds(1.0F) + underzealot.getRandom().nextInt(ACExUtils.seconds(0.5F));
        underzealot.level().addFreshEntity(blade);
    }
}
