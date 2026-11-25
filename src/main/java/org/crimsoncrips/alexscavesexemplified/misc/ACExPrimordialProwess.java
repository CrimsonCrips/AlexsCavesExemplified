package org.crimsoncrips.alexscavesexemplified.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExCompat;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.PrimordialArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExPrimordialProwess {

    private static final ResourceLocation PROWESS = AlexsCavesExemplified.prefix("primordial_prowess");
    private static final float BONUS_PER_PIECE = 0.1F;
    private static final EquipmentSlot[] ARMOR_SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();
        AttributeInstance attackDamage = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage == null)
            return;
        attackDamage.removeModifier(ACExCompat.uuid(PROWESS));
        double bonus = meleeBonus(entity);
        if (bonus > 0.0D) {
            attackDamage.addTransientModifier(ACExCompat.modifier(PROWESS, bonus, AttributeModifier.Operation.ADDITION));
        }
    }

    private static double meleeBonus(LivingEntity entity) {
        ItemStack weapon = entity.getMainHandItem();
        if (!AlexsCavesExemplified.COMMON_CONFIG.PRIMORDIAL_PROWESS_ENABLED.get() || !isPrimordialWeapon(weapon))
            return 0.0D;
        int pieces = primordialPieces(entity);
        if (pieces == 0)
            return 0.0D;
        double[] damage = {entity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE)};
        for (AttributeModifier modifier : weapon.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE)) {
            if (modifier.getOperation() == AttributeModifier.Operation.ADDITION) {
                damage[0] += modifier.getAmount();
            }
        }
        return damage[0] * BONUS_PER_PIECE * pieces;
    }

    public static float boostedDamage(Entity wielder, float damage) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.PRIMORDIAL_PROWESS_ENABLED.get() || !(wielder instanceof LivingEntity living))
            return damage;
        return damage + damage * BONUS_PER_PIECE * primordialPieces(living);
    }

    private static boolean isPrimordialWeapon(ItemStack stack) {
        return stack.is(ACItemRegistry.LIMESTONE_SPEAR.get()) || stack.is(ACItemRegistry.PRIMITIVE_CLUB.get()) || stack.is(ACItemRegistry.EXTINCTION_SPEAR.get());
    }

    private static int primordialPieces(LivingEntity entity) {
        int pieces = 0;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            if (entity.getItemBySlot(slot).getItem() instanceof PrimordialArmorItem) {
                pieces++;
            }
        }
        return pieces;
    }
}
