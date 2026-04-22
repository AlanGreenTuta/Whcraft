/**
 * WhCraft - A Minecraft Mod about Warhammer 40,000.
 * Copyright (C) 2026 ShuShu, 42
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.whcraft.whcraft.Entities.ServoSkull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.whcraft.whcraft.WhcraftScreenHandlers;

public class ServoSkullScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final ServoSkullEntity skull;

    public ServoSkullScreenHandler(int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
        this(syncId, playerInv, getSkullFromBuf(playerInv, buf));
    }

    private static ServoSkullEntity getSkullFromBuf(PlayerInventory playerInv, PacketByteBuf buf) {
        int entityId = buf.readInt();
        if (playerInv.player.getWorld().getEntityById(entityId) instanceof ServoSkullEntity skull) {
            return skull;
        }
        // 实体不存在，返回 null，后续构造器会处理
        return null;
    }

    public ServoSkullScreenHandler(int syncId, PlayerInventory playerInv, ServoSkullEntity skull) {
        super(WhcraftScreenHandlers.SERVO_SKULL, syncId);
        this.skull = skull;
        this.inventory = (skull != null) ? skull.getInventory() : null;

        if (inventory != null) {
            inventory.onOpen(playerInv.player);
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 9; col++) {
                    this.addSlot(new Slot(inventory, col + row * 9, 8 + col * 18, 18 + row * 18));
                }
            }
        }

        // 玩家物品栏槽位（即使 skull 不存在也显示，防止界面异常）
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 103 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 161));
        }
    }

    public ServoSkullEntity getSkull() {
        return skull;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        // 若实体已移除或不存在，关闭界面
        return this.inventory != null && this.inventory.canPlayerUse(player) && !this.skull.isRemoved();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        if (inventory == null) return ItemStack.EMPTY;
        ItemStack newStack = ItemStack.EMPTY;
        Slot slotObj = this.slots.get(slot);
        if (slotObj != null && slotObj.hasStack()) {
            ItemStack originalStack = slotObj.getStack();
            newStack = originalStack.copy();
            if (slot < 45) {
                if (!this.insertItem(originalStack, 45, 81, true)) return ItemStack.EMPTY;
            } else {
                if (!this.insertItem(originalStack, 0, 45, false)) return ItemStack.EMPTY;
            }
            if (originalStack.isEmpty()) slotObj.setStack(ItemStack.EMPTY);
            else slotObj.markDirty();
            if (originalStack.getCount() == newStack.getCount()) return ItemStack.EMPTY;
            slotObj.onTakeItem(player, originalStack);
        }
        return newStack;
    }
}