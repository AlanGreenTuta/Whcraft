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

package org.whcraft.whcraft.Items.GeneSeed;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.whcraft.whcraft.Component.Astartes.ImplantType;
import org.whcraft.whcraft.WhcraftItems;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GeneSeed extends Item {

    private static final String IMPLANTS_KEY = "WhCraftImplants";

    public GeneSeed(Settings settings) {
        super(settings);
    }

    /**
     * 将由器官集合构造的 NBT 写入新基因种子物品。
     */
    public static ItemStack createWithImplants(Set<ImplantType> implants) {
        ItemStack stack = new ItemStack(WhcraftItems.GENE_SEED);
        NbtCompound nbt = stack.getOrCreateNbt();
        NbtList list = new NbtList();
        for (ImplantType type : implants) {
            list.add(NbtString.of(type.name()));
        }
        nbt.put(IMPLANTS_KEY, list);
        return stack;
    }

    /**
     * 从物品 NBT 中读取包含的器官集合。
     */
    public static Set<ImplantType> getImplantsFromStack(ItemStack stack) {
        Set<ImplantType> implants = new HashSet<>();
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(IMPLANTS_KEY, NbtElement.LIST_TYPE)) {
            NbtList list = nbt.getList(IMPLANTS_KEY, NbtElement.STRING_TYPE);
            for (NbtElement elem : list) {
                try {
                    implants.add(ImplantType.valueOf(elem.asString()));
                } catch (IllegalArgumentException ignored) {}
            }
        }
        return implants;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        Set<ImplantType> implants = getImplantsFromStack(stack);
        if (implants.isEmpty()) {
            tooltip.add(Text.translatable("item.whcraft.gene_seed.empty").formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("item.whcraft.gene_seed.contains").formatted(Formatting.WHITE));
            for (ImplantType type : implants) {
                tooltip.add(Text.literal(" - ").append(type.displayName).formatted(Formatting.DARK_GREEN));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}