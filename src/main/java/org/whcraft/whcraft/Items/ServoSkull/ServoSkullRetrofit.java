package org.whcraft.whcraft.Items.ServoSkull;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class ServoSkullRetrofit extends Item {

    private static final String RETROFIT_KEY = "ServoSkullRetrofit";

    /**
     * 所有可能的改装项 ID。
     */
    public static final Set<String> SERVO_SKULL_RETROFIT_IDS = Set.of(
            "null",           // 无改装
            "weaponry",
            "healthcare"
    );

    public ServoSkullRetrofit(Settings settings) {
        super(settings);
    }

    /**
     * 返回所有改装项 ID。
     */
    public static Set<String> getAllServoSkullRetrofitIds() {
        return SERVO_SKULL_RETROFIT_IDS;
    }

    /**
     * 从物品 NBT 中获取当前存储的改装项 ID。<br/>
     * 若不存在或无效则返回 "null"。
     */
    public static String getServoSkullRetrofit(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(RETROFIT_KEY, NbtCompound.STRING_TYPE)) {
            String id = nbt.getString(RETROFIT_KEY);
            if (SERVO_SKULL_RETROFIT_IDS.contains(id)) {
                return id;
            }
        }
        return "null";
    }

    /**
     * 设置改装项 ID（必须属于预定义集合，否则不修改）。
     */
    public static void setServoSkullRetrofit(ItemStack stack, String retrofitId) {
        if (!SERVO_SKULL_RETROFIT_IDS.contains(retrofitId)) {
            return; // 不合法 ID
        }
        stack.getOrCreateNbt().putString(RETROFIT_KEY, retrofitId);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        String currentId = getServoSkullRetrofit(stack);
        // 翻译键 "servoskull.retrofit.<id>"
        Text name = Text.translatable("servoskull.retrofit." + currentId);
        tooltip.add(Text.translatable("item.whcraft.servo_skull_retrofit.tooltip", name)
                .formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean hasRecipeRemainder() {
        return true;
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return stack.copy();
    }
}