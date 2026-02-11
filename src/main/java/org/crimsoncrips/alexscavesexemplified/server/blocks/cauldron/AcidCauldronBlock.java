//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompileR.asItem())
//

package org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.HazmatArmorItem;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.HashMap;
import java.util.LinkedHashMap;

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

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayeR, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack heldItem = pPlayeR.getItemInHand(pHand);
        Item convertItem = corrosionConversion(heldItem.getItem());
        if (convertItem != null){
            ItemStack itemstack1 = ItemUtils.createFilledResult(heldItem, pPlayeR, convertItem.getDefaultInstance());
            pPlayeR.setItemInHand(pHand, itemstack1);
            pPlayeR.swing(pHand);
            pPlayeR.playSound(ACSoundRegistry.ACID_BURN.get());
            return InteractionResult.SUCCESS;
        } else {
            return super.use(pState, pLevel, pPos, pPlayeR, pHand, pHit);
        }

    }

    private static Item corrosionConversion(Item item) {
        HashMap<Item, Item> hashMap = new LinkedHashMap<>();
        hashMap.put(Blocks.COPPER_BLOCK.asItem(), Blocks.WEATHERED_COPPER.asItem());
        hashMap.put(Blocks.WEATHERED_COPPER.asItem(), Blocks.EXPOSED_COPPER.asItem());
        hashMap.put(Blocks.EXPOSED_COPPER.asItem(), Blocks.OXIDIZED_COPPER.asItem());
        hashMap.put(Blocks.CUT_COPPER.asItem(), Blocks.WEATHERED_CUT_COPPER.asItem());
        hashMap.put(Blocks.WEATHERED_CUT_COPPER.asItem(), Blocks.EXPOSED_CUT_COPPER.asItem());
        hashMap.put(Blocks.EXPOSED_CUT_COPPER.asItem(), Blocks.OXIDIZED_CUT_COPPER.asItem());
        hashMap.put(Blocks.CUT_COPPER_SLAB.asItem(), Blocks.WEATHERED_CUT_COPPER_SLAB.asItem());
        hashMap.put(Blocks.WEATHERED_CUT_COPPER_SLAB.asItem(), Blocks.EXPOSED_CUT_COPPER_SLAB.asItem());
        hashMap.put(Blocks.EXPOSED_CUT_COPPER_SLAB.asItem(), Blocks.OXIDIZED_CUT_COPPER_SLAB.asItem());
        hashMap.put(Blocks.CUT_COPPER_STAIRS.asItem(), Blocks.WEATHERED_CUT_COPPER_STAIRS.asItem());
        hashMap.put(Blocks.WEATHERED_CUT_COPPER_STAIRS.asItem(), Blocks.EXPOSED_CUT_COPPER_STAIRS.asItem());
        hashMap.put(Blocks.EXPOSED_CUT_COPPER_STAIRS.asItem(), Blocks.OXIDIZED_CUT_COPPER_STAIRS.asItem());
        hashMap.put(ACBlockRegistry.SCRAP_METAL.get().asItem(), ACBlockRegistry.RUSTY_SCRAP_METAL.get().asItem());
        hashMap.put(ACBlockRegistry.SCRAP_METAL_PLATE.get().asItem(), ACBlockRegistry.RUSTY_SCRAP_METAL_PLATE.get().asItem());
        hashMap.put(ACBlockRegistry.METAL_BARREL.get().asItem(), ACBlockRegistry.RUSTY_BARREL.get().asItem());
        hashMap.put(ACBlockRegistry.METAL_SCAFFOLDING.get().asItem(), ACBlockRegistry.RUSTY_SCAFFOLDING.get().asItem());
        hashMap.put(ACBlockRegistry.METAL_REBAR.get().asItem(), ACBlockRegistry.RUSTY_REBAR.get().asItem());
        return hashMap.getOrDefault(item, null);
    }

    public int getAnalogOutputSignal(BlockState p_153502_, Level p_153503_, BlockPos p_153504_) {
        return 3;
    }



}
