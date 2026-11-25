package org.crimsoncrips.alexscavesexemplified.misc;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.server.block.blockentity.QuarryBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.crimsoncrips.alexscavesexemplified.backport.IAttachmentHolder;
import net.minecraftforge.items.ItemStackHandler;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

import javax.annotation.Nullable;

public class ACExQuarryUpgrades extends ItemStackHandler {

    public static final int SLOTS = 3;
    private static final float SPEED_PER_AZURE = 1.0F;

    @Nullable
    private final IAttachmentHolder holder;

    public ACExQuarryUpgrades(@Nullable IAttachmentHolder holder) {
        super(SLOTS);
        this.holder = holder;
    }

    public static boolean isUpgrade(ItemStack stack) {
        return stack.is(ACItemRegistry.AZURE_NEODYMIUM_INGOT.get()) || stack.is(ACItemRegistry.SCARLET_NEODYMIUM_INGOT.get());
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return isUpgrade(stack);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (this.holder instanceof BlockEntity blockEntity) {
            blockEntity.setChanged();
        }
    }

    public int count(ItemStack upgrade) {
        int count = 0;
        for (int i = 0; i < this.getSlots(); i++) {
            if (ItemStack.isSameItem(this.getStackInSlot(i), upgrade)) {
                count++;
            }
        }
        return count;
    }

    public int azure() {
        return this.count(new ItemStack(ACItemRegistry.AZURE_NEODYMIUM_INGOT.get()));
    }

    public int scarlet() {
        return this.count(new ItemStack(ACItemRegistry.SCARLET_NEODYMIUM_INGOT.get()));
    }

    public static float speedMultiplier(int azure) {
        return 1.0F + SPEED_PER_AZURE * azure;
    }

    @Nullable
    public static ACExQuarryUpgrades of(Level level, @Nullable BlockPos quarryPos) {
        if (quarryPos == null || !AlexsCavesExemplified.COMMON_CONFIG.QUARRY_UPGRADES_ENABLED.get())
            return null;
        return level.getBlockEntity(quarryPos) instanceof QuarryBlockEntity quarry ? ACExData.get(quarry, ACExAttachments.QUARRY_UPGRADES) : null;
    }

    public static float speedMultiplier(Level level, @Nullable BlockPos quarryPos) {
        ACExQuarryUpgrades upgrades = of(level, quarryPos);
        return upgrades == null ? 1.0F : speedMultiplier(upgrades.azure());
    }

    public static int fortune(Level level, @Nullable BlockPos quarryPos) {
        ACExQuarryUpgrades upgrades = of(level, quarryPos);
        return upgrades == null ? 0 : upgrades.scarlet();
    }

    public static ItemStack fortunePickaxe(Level level, int fortune) {
        ItemStack pickaxe = new ItemStack(Items.NETHERITE_PICKAXE);
        pickaxe.enchant(Enchantments.BLOCK_FORTUNE, fortune);
        return pickaxe;
    }
}
