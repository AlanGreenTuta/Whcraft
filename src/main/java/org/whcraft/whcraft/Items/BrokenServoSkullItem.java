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

package org.whcraft.whcraft.Items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrokenServoSkullItem extends Item {

    private static final String NAME_KEY = "ServoSkullName";

    public BrokenServoSkullItem(Settings settings) {
        super(settings);
    }

    /**
     * 写入损坏时伺服颅骨的名称。
     */
    public static void writeName(ItemStack stack, Text name) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putString(NAME_KEY, Text.Serializer.toJson(name));
    }

    /**
     * 修复时将名称传入 ServoSkullItem。
     */
    public void applyNameToSkullItem(ItemStack brokenStack, ItemStack skullStack) {
        NbtCompound brokenNbt = brokenStack.getNbt();
        if (brokenNbt != null && brokenNbt.contains(NAME_KEY, NbtElement.STRING_TYPE)) {
            NbtCompound skullNbt = skullStack.getOrCreateNbt();
            skullNbt.putString(NAME_KEY, brokenNbt.getString(NAME_KEY));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(NAME_KEY, NbtElement.STRING_TYPE)) {
            Text name = Text.Serializer.fromJson(nbt.getString(NAME_KEY));
            tooltip.add(Text.translatable("item.whcraft.broken_servo_skull.tooltip.name", name));
        } else {
            tooltip.add(Text.translatable("item.whcraft.broken_servo_skull.tooltip.no_name"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean isFireproof() {
        return true;
    }
}