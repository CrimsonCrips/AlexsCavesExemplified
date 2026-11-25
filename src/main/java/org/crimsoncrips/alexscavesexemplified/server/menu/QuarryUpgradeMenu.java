package org.crimsoncrips.alexscavesexemplified.server.menu;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.crimsoncrips.alexscavesexemplified.misc.ACExQuarryUpgrades;

public class QuarryUpgradeMenu extends AbstractContainerMenu {

    public static final int FIRST_SLOT_X = 62;
    public static final int SLOT_Y = 22;
    public static final int INVENTORY_Y = 58;
    public static final int HOTBAR_Y = 116;

    private final ContainerLevelAccess access;
    private final ACExQuarryUpgrades upgrades;

    public QuarryUpgradeMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new ACExQuarryUpgrades(null), ContainerLevelAccess.NULL);
    }

    public QuarryUpgradeMenu(int containerId, Inventory playerInventory, ACExQuarryUpgrades upgrades, ContainerLevelAccess access) {
        super(ACExMenuRegistry.QUARRY_UPGRADES.get(), containerId);
        this.access = access;
        this.upgrades = upgrades;
        for (int i = 0; i < ACExQuarryUpgrades.SLOTS; i++) {
            this.addSlot(new SlotItemHandler(upgrades, i, FIRST_SLOT_X + i * 18, SLOT_Y));
        }
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, INVENTORY_Y + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInventory, column, 8 + column * 18, HOTBAR_Y));
        }
    }

    public ACExQuarryUpgrades getUpgrades() {
        return this.upgrades;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ACBlockRegistry.QUARRY.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem())
            return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < ACExQuarryUpgrades.SLOTS) {
            if (!this.moveItemStackTo(stack, ACExQuarryUpgrades.SLOTS, this.slots.size(), true))
                return ItemStack.EMPTY;
        } else if (!ACExQuarryUpgrades.isUpgrade(stack) || !this.moveItemStackTo(stack, 0, ACExQuarryUpgrades.SLOTS, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return original;
    }
}
