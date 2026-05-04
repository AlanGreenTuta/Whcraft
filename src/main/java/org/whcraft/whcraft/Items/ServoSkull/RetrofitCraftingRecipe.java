package org.whcraft.whcraft.Items.ServoSkull;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.whcraft.whcraft.WhcraftItems;

public class RetrofitCraftingRecipe implements CraftingRecipe {
    private final Identifier id;
    private final String group;
    private final CraftingRecipeCategory category;
    private final String retrofitId;
    private final DefaultedList<Ingredient> inputs;
    private final ItemStack output;

    public RetrofitCraftingRecipe(Identifier id, String group, CraftingRecipeCategory category,
                                  String retrofitId, DefaultedList<Ingredient> inputs) {
        this.id = id;
        this.group = group;
        this.category = category;
        this.retrofitId = retrofitId;
        this.inputs = inputs;

        // 生成带 NBT 的示例输出
        this.output = new ItemStack(WhcraftItems.SERVO_SKULL_RETROFIT);
        ServoSkullRetrofit.setServoSkullRetrofit(this.output, retrofitId);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        // 收集所有非空输入槽
        java.util.ArrayList<ItemStack> stacks = new java.util.ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }

        // 如果数量不匹配，直接失败
        if (stacks.size() != inputs.size()) {
            return false;
        }

        // 为每个原料寻找一个匹配的物品
        boolean[] used = new boolean[stacks.size()];
        for (Ingredient ingredient : inputs) {
            boolean found = false;
            for (int i = 0; i < stacks.size(); i++) {
                if (!used[i] && ingredient.test(stacks.get(i))) {
                    used[i] = true;
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack result = new ItemStack(WhcraftItems.SERVO_SKULL_RETROFIT);
        ServoSkullRetrofit.setServoSkullRetrofit(result, retrofitId);
        return result;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= inputs.size();
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return inputs;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RetrofitCraftingRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return category;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    /**
     * 暴露 retrofitId
     * @return retrofitId
     */
    public String getRetrofitId() {
        return retrofitId;
    }
}