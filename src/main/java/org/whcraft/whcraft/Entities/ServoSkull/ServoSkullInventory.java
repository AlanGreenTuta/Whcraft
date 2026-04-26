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

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class ServoSkullInventory extends SimpleInventory {
    private final ServoSkullEntity entity;

    public ServoSkullInventory(ServoSkullEntity entity, int size) {
        super(size);
        this.entity = entity;
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    public void readNbt(NbtCompound nbt) {
        this.clear();
        NbtList invList = nbt.getList("Inventory", 10);
        for (int i = 0; i < invList.size(); i++) {
            NbtCompound slotTag = invList.getCompound(i);
            int slot = slotTag.getInt("Slot");
            ItemStack stack = ItemStack.fromNbt(slotTag);
            if (slot >= 0 && slot < this.size()) {
                this.setStack(slot, stack);
            }
        }
        this.markDirty();
    }

    public void writeNbt(NbtCompound nbt) {
        NbtList invList = new NbtList();
        for (int i = 0; i < this.size(); i++) {
            ItemStack stack = this.getStack(i);
            if (!stack.isEmpty()) {
                NbtCompound slotTag = new NbtCompound();
                slotTag.putInt("Slot", i);
                stack.writeNbt(slotTag);
                invList.add(slotTag);
            }
        }
        if (!invList.isEmpty()) {
            nbt.put("Inventory", invList);
        }
    }
}