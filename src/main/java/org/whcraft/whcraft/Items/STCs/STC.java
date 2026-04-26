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

package org.whcraft.whcraft.Items.STCs;

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
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class STC extends Item {
    private static final String MODULES_KEY = "STCModules";

    /**
     * 所有 STC 模块 ID 列表
     */
    public static final Set<String> STC_MODULE_IDS = Set.of(
            "servo_skull", // 伺服头骨
            "circuitry", // 电路系统
            "cogitator" // 沉思者
    );

    public STC(Settings settings) {
        super(settings);
    }

    public static Set<String> getAllStcModuleIds() {
        return STC_MODULE_IDS;
    }

    /**
     * 从 STC 中获取包含的模块。
     * @param stack 指定 STC
     * @return STC 模块
     */
    public static Set<String> getStcMods(ItemStack stack) {
        Set<String> mods = new HashSet<>();
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(MODULES_KEY, NbtElement.LIST_TYPE)) {
            NbtList list = nbt.getList(MODULES_KEY, NbtElement.STRING_TYPE);
            for (NbtElement elem : list) {
                mods.add(elem.asString());
            }
        }
        return mods;
    }

    /**
     * 检测 STC 是否含有指定模块。
     * @param stack 指定 STC
     * @param modId 指定模块的 ID
     * @return true:有 or false:无
     */
    public static boolean hasStcMod(ItemStack stack, String modId) {
        return getStcMods(stack).contains(modId);
    }

    /**
     * 为 STC 添加 模块。
     * @param stack 指定 STC
     * @param modIds 指定模块的 ID 集合
     */
    public static void addStcMods(ItemStack stack, Collection<String> modIds) {
        Set<String> current = getStcMods(stack);
        current.addAll(modIds);
        setStcMods(stack, current);
    }

    /**
     * 从 STC 移除指定模块。
     * @param stack 指定 STC
     * @param modIds 指定模块的 ID 集合
     */
    public static void removeStcMods(ItemStack stack, Collection<String> modIds) {
        Set<String> current = getStcMods(stack);
        current.removeAll(modIds);
        setStcMods(stack, current);
    }

    /**
     * 从 STC 移除所有模块。
     * @param stack 指定 STC
     */
    public static void removeAllStcMods(ItemStack stack) {
        stack.removeSubNbt(MODULES_KEY);
    }

    /**
     * 设置 STC 的所有模块，覆盖原有模块。
     * @param stack 指定 STC
     * @param modIds 指定模块集合
     */
    public static void setStcMods(ItemStack stack, Set<String> modIds) {
        NbtList list = new NbtList();
        for (String modId : modIds) {
            list.add(NbtString.of(modId));
        }
        stack.getOrCreateNbt().put(MODULES_KEY, list);
    }

    /**
     * 工具提示
     * @param stack 默认参数
     * @param world 默认参数
     * @param tooltip 默认参数
     * @param context 默认参数
     */
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Set<String> mods = getStcMods(stack);
        if (mods.isEmpty()) {
            tooltip.add(Text.translatable("item.whcraft.stc.empty").formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("item.whcraft.stc.modules").formatted(Formatting.WHITE));
            for (String modId : mods) {
                // 使用翻译键： "stc.module.whcraft." + modId
                tooltip.add(Text.literal(" - ").append(Text.translatable("stc.module.whcraft." + modId))
                        .formatted(Formatting.DARK_GREEN));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    /**
     * 设置合成后不消耗
     * @return true
     */
    @Override
    public boolean hasRecipeRemainder() {
        return true;
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        // 合成后原样返回 STC
        return stack.copy();
    }
}