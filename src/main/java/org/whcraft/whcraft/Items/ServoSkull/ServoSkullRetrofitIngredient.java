package org.whcraft.whcraft.Items.ServoSkull;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.whcraft.whcraft.WhcraftItems;

import java.util.*;

public class ServoSkullRetrofitIngredient implements CustomIngredient {
    private final Set<String> allowedIds;

    public ServoSkullRetrofitIngredient(Set<String> allowedIds) {
        this.allowedIds = allowedIds;
    }

    public Set<String> getAllowedIds() {
        return allowedIds;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (!stack.isOf(WhcraftItems.SERVO_SKULL_RETROFIT)) {
            return false;
        }
        String currentId = ServoSkullRetrofit.getServoSkullRetrofit(stack);
        return allowedIds.contains(currentId);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        List<ItemStack> examples = new ArrayList<>();
        for (String id : allowedIds) {
            ItemStack stack = new ItemStack(WhcraftItems.SERVO_SKULL_RETROFIT);
            ServoSkullRetrofit.setServoSkullRetrofit(stack, id);
            examples.add(stack);
        }
        return examples;
    }

    @Override
    public boolean requiresTesting() {
        return true; // 需要实际检查 NBT
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements CustomIngredientSerializer<ServoSkullRetrofitIngredient> {
        public static final Serializer INSTANCE = new Serializer();
        public static final Identifier ID = new Identifier("whcraft", "servo_skull_retrofit");

        @Override
        public Identifier getIdentifier() {
            return ID;
        }

        @Override
        public ServoSkullRetrofitIngredient read(JsonObject json) {
            Set<String> ids = new HashSet<>();
            JsonHelper.getArray(json, "ids").forEach(elem -> ids.add(elem.getAsString()));
            return new ServoSkullRetrofitIngredient(ids);
        }

        @Override
        public void write(JsonObject json, ServoSkullRetrofitIngredient ingredient) {
            com.google.gson.JsonArray array = new com.google.gson.JsonArray();
            ingredient.allowedIds.forEach(array::add);
            json.add("ids", array);
        }

        @Override
        public ServoSkullRetrofitIngredient read(PacketByteBuf buf) {
            int count = buf.readVarInt();
            Set<String> ids = new HashSet<>();
            for (int i = 0; i < count; i++) {
                ids.add(buf.readString());
            }
            return new ServoSkullRetrofitIngredient(ids);
        }

        @Override
        public void write(PacketByteBuf buf, ServoSkullRetrofitIngredient ingredient) {
            buf.writeVarInt(ingredient.allowedIds.size());
            for (String id : ingredient.allowedIds) {
                buf.writeString(id);
            }
        }
    }
}