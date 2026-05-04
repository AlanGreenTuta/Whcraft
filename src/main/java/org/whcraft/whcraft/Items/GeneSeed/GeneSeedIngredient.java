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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.whcraft.whcraft.Component.Astartes.ImplantType;
import org.whcraft.whcraft.WhcraftItems;

import java.util.*;

public class GeneSeedIngredient implements CustomIngredient {
    private final Set<ImplantType> requiredImplants;

    public GeneSeedIngredient(Set<ImplantType> requiredImplants) {
        this.requiredImplants = requiredImplants;
    }

    public Set<ImplantType> getRequiredImplants() {
        return requiredImplants;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (!stack.isOf(WhcraftItems.GENE_SEED)) return false;
        Set<ImplantType> stackImplants = GeneSeed.getImplantsFromStack(stack);
        return stackImplants.containsAll(requiredImplants);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        ItemStack example = GeneSeed.createWithImplants(requiredImplants);
        return List.of(example);
    }

    @Override
    public boolean requiresTesting() {
        return true; // 始终需要检查 NBT
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    /**
     * 序列化器
     */
    public static class Serializer implements CustomIngredientSerializer<GeneSeedIngredient> {
        public static final Serializer INSTANCE = new Serializer();
        public static final Identifier ID = new Identifier("whcraft", "gene_seed_implant");

        @Override
        public Identifier getIdentifier() {
            return ID;
        }

        @Override
        public GeneSeedIngredient read(JsonObject json) {
            JsonArray array = JsonHelper.getArray(json, "implants");
            Set<ImplantType> modules = new HashSet<>();
            array.forEach(elem -> modules.add(ImplantType.valueOf(elem.getAsString())));
            return new GeneSeedIngredient(modules);
        }

        @Override
        public void write(JsonObject json, GeneSeedIngredient ingredient) {
            JsonArray array = new JsonArray();
            ingredient.requiredImplants.forEach(implant -> array.add(implant.name()));
            json.add("implants", array);
        }

        @Override
        public GeneSeedIngredient read(PacketByteBuf buf) {
            int count = buf.readVarInt();
            Set<ImplantType> implants = new HashSet<>();
            for (int i = 0; i < count; i++) {
                implants.add(ImplantType.valueOf(buf.readString()));
            }
            return new GeneSeedIngredient(implants);
        }

        @Override
        public void write(PacketByteBuf buf, GeneSeedIngredient ingredient) {
            buf.writeVarInt(ingredient.requiredImplants.size());
            for (ImplantType type : ingredient.requiredImplants) {
                buf.writeString(type.name());
            }
        }
    }
}