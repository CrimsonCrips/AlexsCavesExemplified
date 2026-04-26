package org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;

public interface ACExCauldronInteraction extends CauldronInteraction {
    Map<Item, CauldronInteraction> ACID = CauldronInteraction.newInteractionMap();
    Map<Item, CauldronInteraction> PURPLE_SODA = CauldronInteraction.newInteractionMap();

    CauldronInteraction FILL_ACID = (p_175676_, p_175677_, p_175678_, p_175679_, p_175680_, p_175681_) -> CauldronInteraction.emptyBucket(p_175677_, p_175678_, p_175679_, p_175680_, p_175681_, ACExBlockRegistry.ACID_CAULDRON.get().defaultBlockState(), ACSoundRegistry.ACID_CORROSION.get());
    CauldronInteraction FILL_SODA = (p_175676_, p_175677_, p_175678_, p_175679_, p_175680_, p_175681_) -> CauldronInteraction.emptyBucket(p_175677_, p_175678_, p_175679_, p_175680_, p_175681_, ACExBlockRegistry.PURPLE_SODA_CAULDRON.get().defaultBlockState(), ACSoundRegistry.PURPLE_SODA_SWIM.get());

    static void bootStrap() {
        addDefaultInteractions(EMPTY);
        if (AlexsCavesExemplified.COMMON_CONFIG.LIQUID_REPLICATION_ENABLED.get()) {
            EMPTY.put(ACItemRegistry.PURPLE_SODA_BOTTLE.get(), (p_175732_, p_175733_, p_175734_, p_175735_, p_175736_, p_175737_) -> {
                if (!p_175733_.isClientSide) {
                    p_175735_.setItemInHand(p_175736_, ItemUtils.createFilledResult(p_175737_, p_175735_, new ItemStack(Items.GLASS_BOTTLE)));
                    p_175735_.awardStat(Stats.USE_CAULDRON);
                    p_175735_.awardStat(Stats.ITEM_USED.get(p_175737_.getItem()));
                    ACExUtils.awardAdvancement(p_175735_,"soda_replication","replicate");
                    p_175733_.setBlockAndUpdate(p_175734_, ACExBlockRegistry.PURPLE_SODA_CAULDRON.get().defaultBlockState());
                    p_175733_.playSound((Player) null, p_175734_, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    p_175733_.gameEvent((Entity) null, GameEvent.FLUID_PLACE, p_175734_);
                }
                return InteractionResult.sidedSuccess(p_175733_.isClientSide);
            });
        }
        ACID.put(Items.BUCKET, (p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_) -> {
            return fillBucket(p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_, new ItemStack(ACItemRegistry.ACID_BUCKET.get()), (p_175651_) -> {
                return true;
            }, ACSoundRegistry.ACID_SUBMERGE.get());
        });
        addDefaultInteractions(ACID);

        PURPLE_SODA.put(Items.BUCKET, (p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_) -> {
            return fillBucket(p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_, new ItemStack(ACItemRegistry.PURPLE_SODA_BUCKET.get()), (p_175651_) -> {
                return true;
            }, ACSoundRegistry.PURPLE_SODA_SUBMERGE.get());
        });
        PURPLE_SODA.put(Items.GLASS_BOTTLE, (p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_) -> {
            return fillBucket(p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_, new ItemStack(ACItemRegistry.PURPLE_SODA_BOTTLE.get()), (p_175651_) -> {
                return true;
            }, ACSoundRegistry.PURPLE_SODA_SUBMERGE.get());
        });
        addDefaultInteractions(PURPLE_SODA);
    }

    static void addDefaultInteractions(Map<Item, CauldronInteraction> pInteractionsMap) {
        pInteractionsMap.put(ACItemRegistry.ACID_BUCKET.get(), FILL_ACID);
        pInteractionsMap.put(ACItemRegistry.PURPLE_SODA_BUCKET.get(), FILL_SODA);
    }

    static InteractionResult fillBucket(BlockState pBlockState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, ItemStack pEmptyStack, ItemStack pFilledStack, Predicate<BlockState> pStatePredicate, SoundEvent pFillSound) {
        if (!pStatePredicate.test(pBlockState)) {
            return InteractionResult.PASS;
        } else {
            if (!pLevel.isClientSide) {
                Item item = pEmptyStack.getItem();
                pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pEmptyStack, pPlayer, pFilledStack));
                pPlayer.awardStat(Stats.USE_CAULDRON);
                pPlayer.awardStat(Stats.ITEM_USED.get(item));
                pLevel.setBlockAndUpdate(pPos, ACExBlockRegistry.METAL_CAULDRON.get().defaultBlockState());
                pLevel.playSound((Player)null, pPos, pFillSound, SoundSource.BLOCKS, 1.0F, 1.0F);
                pLevel.gameEvent((Entity)null, GameEvent.FLUID_PICKUP, pPos);
            }

            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }
    }

}