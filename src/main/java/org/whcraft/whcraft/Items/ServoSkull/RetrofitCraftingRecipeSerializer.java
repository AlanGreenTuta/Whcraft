package org.whcraft.whcraft.Items.ServoSkull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import org.whcraft.whcraft.WhcraftItems;

public class RetrofitCraftingRecipeSerializer implements RecipeSerializer<RetrofitCraftingRecipe> {
    public static final RetrofitCraftingRecipeSerializer INSTANCE = new RetrofitCraftingRecipeSerializer();
    public static final Identifier ID = new Identifier("whcraft", "retrofit_crafting");

    @Override
    public RetrofitCraftingRecipe read(Identifier id, JsonObject json) {
        String group = JsonHelper.getString(json, "group", "");
        CraftingRecipeCategory category;
        if (JsonHelper.hasString(json, "category")) {
            String catStr = JsonHelper.getString(json, "category").toUpperCase();
            try {
                category = CraftingRecipeCategory.valueOf(catStr);
            } catch (IllegalArgumentException e) {
                category = CraftingRecipeCategory.MISC;
            }
        } else {
            category = CraftingRecipeCategory.MISC;
        }
        String retrofitId = JsonHelper.getString(json, "retrofit");
        JsonArray ingredientsArray = JsonHelper.getArray(json, "ingredients");

        DefaultedList<Ingredient> inputs = DefaultedList.ofSize(ingredientsArray.size(), Ingredient.EMPTY);
        for (int i = 0; i < ingredientsArray.size(); i++) {
            inputs.set(i, Ingredient.fromJson(ingredientsArray.get(i)));
        }

        return new RetrofitCraftingRecipe(id, group, category, retrofitId, inputs);
    }

    @Override
    public RetrofitCraftingRecipe read(Identifier id, PacketByteBuf buf) {
        String group = buf.readString();
        CraftingRecipeCategory category = buf.readEnumConstant(CraftingRecipeCategory.class);
        String retrofitId = buf.readString();
        int size = buf.readVarInt();
        DefaultedList<Ingredient> inputs = DefaultedList.ofSize(size, Ingredient.EMPTY);
        for (int i = 0; i < size; i++) {
            inputs.set(i, Ingredient.fromPacket(buf));
        }
        return new RetrofitCraftingRecipe(id, group, category, retrofitId, inputs);
    }

    @Override
    public void write(PacketByteBuf buf, RetrofitCraftingRecipe recipe) {
        buf.writeString(recipe.getGroup());
        buf.writeEnumConstant(recipe.getCategory());
        buf.writeString(recipe.getRetrofitId());
        buf.writeVarInt(recipe.getIngredients().size());
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.write(buf);
        }
    }
}