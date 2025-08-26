//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron;

import com.github.alexmodguy.alexscaves.server.item.HazmatArmorItem;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class AcidCauldronBlock extends ACExCauldron {


    public AcidCauldronBlock(BlockBehaviour.Properties p_153498_) {
        super(p_153498_, ACExCauldronInteraction.ACID);
    }

    protected double getContentHeight(BlockState p_153500_) {
        return 0.9375D;
    }



    public void entityInside(BlockState p_153506_, Level p_153507_, BlockPos p_153508_, Entity p_153509_) {
        if (this.isEntityInsideContent(p_153506_, p_153508_, p_153509_)) {
            boolean armor = false;
            boolean hurtSound = false;
            float dmgMultiplier = 1.0F;
            if (p_153509_ instanceof LivingEntity living) {
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot.isArmor()) {
                        ItemStack item = living.getItemBySlot(slot);
                        if (item != null && item.isDamageableItem() && !(item.getItem() instanceof HazmatArmorItem)) {
                            armor = true;
                            if (living.getRandom().nextFloat() < 0.05F && !(p_153509_ instanceof Player player && player.isCreative())) {
                                item.hurtAndBreak(1, living, e -> e.broadcastBreakEvent(slot));
                            }
                        }
                    }
                }
                dmgMultiplier = 1.0F - (HazmatArmorItem.getWornAmount(living) / 4F);
            }
            if (armor) {
                ACAdvancementTriggerRegistry.ENTER_ACID_WITH_ARMOR.triggerForEntity(p_153509_);
            }
            if (p_153507_.random.nextFloat() < dmgMultiplier) {
                float golemAddition = p_153509_.getType().is(ACTagRegistry.WEAK_TO_ACID) ? 10.0F : 0.0F;
                hurtSound = p_153509_.hurt(ACDamageTypes.causeAcidDamage(p_153507_.registryAccess()), dmgMultiplier * (float) (armor ? 0.01D : 1.0D) + golemAddition);
            }
            if (hurtSound) {
                p_153509_.playSound(ACSoundRegistry.ACID_BURN.get());
            }
        }

    }

    public int getAnalogOutputSignal(BlockState p_153502_, Level p_153503_, BlockPos p_153504_) {
        return 3;
    }



}
