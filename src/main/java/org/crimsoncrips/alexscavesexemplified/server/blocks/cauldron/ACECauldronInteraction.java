package org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACEBlockRegistry;

public interface ACECauldronInteraction {
    Map<Item, ACECauldronInteraction> EMPTY = newInteractionMap();
    Map<Item, ACECauldronInteraction> ACID = newInteractionMap();
    Map<Item, ACECauldronInteraction> PURPLE_SODA = newInteractionMap();



    ACECauldronInteraction FILL_ACID = (p_175676_, p_175677_, p_175678_, p_175679_, p_175680_, p_175681_) -> emptyBucket(p_175677_, p_175678_, p_175679_, p_175680_, p_175681_, ACEBlockRegistry.ACID_CAULDRON.get().defaultBlockState(), ACSoundRegistry.ACID_CORROSION.get());
    ACECauldronInteraction FILL_SODA = (p_175676_, p_175677_, p_175678_, p_175679_, p_175680_, p_175681_) -> emptyBucket(p_175677_, p_175678_, p_175679_, p_175680_, p_175681_, ACEBlockRegistry.PURPLE_SODA_CAULDRON.get().defaultBlockState(), ACSoundRegistry.PURPLE_SODA_SWIM.get());


    static Object2ObjectOpenHashMap<Item, ACECauldronInteraction> newInteractionMap() {
        return Util.make(new Object2ObjectOpenHashMap<>(), (p_175646_) -> {
            p_175646_.defaultReturnValue((p_175739_, p_175740_, p_175741_, p_175742_, p_175743_, p_175744_) -> {
                return InteractionResult.PASS;
            });
        });
    }

    InteractionResult interact(BlockState pBlockState, Level pLevel, BlockPos pBlockPos, Player pPlayer, InteractionHand pHand, ItemStack pStack);

    static void bootStrap() {
        addDefaultInteractions(EMPTY);
        if (AlexsCavesExemplified.COMMON_CONFIG.LIQUID_REPLICATION_ENABLED.get()) {
            EMPTY.put(ACItemRegistry.PURPLE_SODA_BOTTLE.get(), (p_175732_, p_175733_, p_175734_, p_175735_, p_175736_, p_175737_) -> {
                if (!p_175733_.isClientSide) {
                    p_175735_.setItemInHand(p_175736_, ItemUtils.createFilledResult(p_175737_, p_175735_, new ItemStack(Items.GLASS_BOTTLE)));
                    p_175735_.awardStat(Stats.USE_CAULDRON);
                    p_175735_.awardStat(Stats.ITEM_USED.get(p_175737_.getItem()));
                    ACEUtils.awardAdvancement(p_175735_,"soda_replication","replicate");
                    p_175733_.setBlockAndUpdate(p_175734_, ACEBlockRegistry.PURPLE_SODA_CAULDRON.get().defaultBlockState());
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

    static void addDefaultInteractions(Map<Item, ACECauldronInteraction> pInteractionsMap) {
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
                pLevel.setBlockAndUpdate(pPos, ACEBlockRegistry.METAL_CAULDRON.get().defaultBlockState());
                pLevel.playSound((Player)null, pPos, pFillSound, SoundSource.BLOCKS, 1.0F, 1.0F);
                pLevel.gameEvent((Entity)null, GameEvent.FLUID_PICKUP, pPos);
            }

            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }
    }

    static InteractionResult emptyBucket(Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, ItemStack pFilledStack, BlockState pState, SoundEvent pEmptySound) {
        if (!pLevel.isClientSide) {
            Item item = pFilledStack.getItem();
            pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pFilledStack, pPlayer, new ItemStack(Items.BUCKET)));
            pPlayer.awardStat(Stats.FILL_CAULDRON);
            pPlayer.awardStat(Stats.ITEM_USED.get(item));
            pLevel.setBlockAndUpdate(pPos, pState);
            pLevel.playSound((Player)null, pPos, pEmptySound, SoundSource.BLOCKS, 1.0F, 1.0F);
            pLevel.gameEvent((Entity)null, GameEvent.FLUID_PLACE, pPos);
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }
}