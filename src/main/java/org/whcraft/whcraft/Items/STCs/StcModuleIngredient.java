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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.whcraft.whcraft.WhcraftItems;

import java.util.*;

public class StcModuleIngredient implements CustomIngredient {
    private final Set<String> requiredModules;

    public StcModuleIngredient(Set<String> requiredModules) {
        this.requiredModules = requiredModules;
    }

    public Set<String> getRequiredModules() {
        return requiredModules;
    }

    @Override
    public boolean test(ItemStack stack) {
        // 必须是 STC 物品
        if (!stack.isOf(WhcraftItems.STC)) {
            return false;
        }
        // 必须包含所有 requiredModules
        Set<String> stackMods = STC.getStcMods(stack);
        return stackMods.containsAll(requiredModules);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        // 返回一个示例 STC 物品，包含所有 requiredModules（用于配方书展示）
        ItemStack example = new ItemStack(WhcraftItems.STC);
        STC.setStcMods(example, requiredModules);
        return List.of(example);
    }

    @Override
    public boolean requiresTesting() {
        return true; // 始终需要实际测试 NBT
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    /**
     * 序列化器
     */
    public static class Serializer implements CustomIngredientSerializer<StcModuleIngredient> {
        public static final Serializer INSTANCE = new Serializer();
        public static final Identifier ID = new Identifier("whcraft", "stc_module");

        @Override
        public Identifier getIdentifier() {
            return ID;
        }

        @Override
        public StcModuleIngredient read(JsonObject json) {
            JsonArray array = JsonHelper.getArray(json, "modules");
            Set<String> modules = new HashSet<>();
            array.forEach(elem -> modules.add(elem.getAsString()));
            return new StcModuleIngredient(modules);
        }

        @Override
        public void write(JsonObject json, StcModuleIngredient ingredient) {
            JsonArray array = new JsonArray();
            ingredient.requiredModules.forEach(array::add);
            json.add("modules", array);
        }

        @Override
        public StcModuleIngredient read(PacketByteBuf buf) {
            int count = buf.readVarInt();
            Set<String> modules = new HashSet<>();
            for (int i = 0; i < count; i++) {
                modules.add(buf.readString());
            }
            return new StcModuleIngredient(modules);
        }

        @Override
        public void write(PacketByteBuf buf, StcModuleIngredient ingredient) {
            buf.writeVarInt(ingredient.requiredModules.size());
            for (String mod : ingredient.requiredModules) {
                buf.writeString(mod);
            }
        }
    }
}