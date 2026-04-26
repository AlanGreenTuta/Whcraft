package org.whcraft.whcraft.Items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.whcraft.whcraft.WhcraftItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrokenServoSkullItem extends Item {

    private static final String NAME_KEY = "ServoSkullName";

    private static final TagKey<Item> ALL_PLASTEELS = TagKey.of(
            RegistryKeys.ITEM,
            new Identifier("whcraft", "all_plasteels")
    );

    public BrokenServoSkullItem(Settings settings) {
        super(settings);
    }

    /**
     * 写入损坏时伺服颅骨的名称
     */
    public static void writeName(ItemStack stack, Text name) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putString(NAME_KEY, Text.Serializer.toJson(name));
    }

    /**
     * 将损坏颅骨的名称 NBT 转移到修复后的颅骨上
     */
    public static void transferName(ItemStack broken, ItemStack target) {
        NbtCompound nbt = broken.getNbt();
        if (nbt != null && nbt.contains(NAME_KEY, NbtElement.STRING_TYPE)) {
            target.getOrCreateNbt().putString(NAME_KEY, nbt.getString(NAME_KEY));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack brokenStack = user.getStackInHand(hand);
        if (world.isClient) {
            return TypedActionResult.pass(brokenStack);
        }

        if (!hasItem(user, stack -> stack.isOf(WhcraftItems.CIRCUITRY), 1)) {
            return TypedActionResult.fail(brokenStack);
        }

        if (!hasItem(user, stack -> stack.isIn(ALL_PLASTEELS), 1)) {
            return TypedActionResult.fail(brokenStack);
        }

        consumeItem(user, stack -> stack.isOf(WhcraftItems.CIRCUITRY), 1);

        consumeRandomTagItem(user, ALL_PLASTEELS, 1);

        ItemStack skullStack = new ItemStack(WhcraftItems.SERVO_SKULL);
        transferName(brokenStack, skullStack);

        user.setStackInHand(hand, skullStack);
        return TypedActionResult.success(skullStack);
    }

    /**
     * 检查玩家物品栏（主背包 + 副手）中是否存在至少指定数量的符合谓词的物品。
     */
    private boolean hasItem(PlayerEntity player, java.util.function.Predicate<ItemStack> predicate, int count) {
        int found = 0;
        // 检查副手
        if (predicate.test(player.getOffHandStack())) {
            found += player.getOffHandStack().getCount();
        }
        // 检查物品栏
        for (int i = 0; i < player.getInventory().main.size(); i++) {
            ItemStack stack = player.getInventory().main.get(i);
            if (predicate.test(stack)) {
                found += stack.getCount();
                if (found >= count) return true;
            }
        }
        return found >= count;
    }

    /**
     * 从玩家物品栏（副手优先）中消耗指定数量的符合谓词的物品。
     * 当一堆不够时会自动拆分消耗多堆。
     */
    private void consumeItem(PlayerEntity player, java.util.function.Predicate<ItemStack> predicate, int count) {
        int remaining = count;

        // 先消耗副手
        ItemStack offhand = player.getOffHandStack();
        if (predicate.test(offhand)) {
            int take = Math.min(remaining, offhand.getCount());
            offhand.decrement(take);
            remaining -= take;
            if (remaining <= 0) return;
        }

        // 再消耗主背包
        for (int i = 0; i < player.getInventory().main.size(); i++) {
            if (remaining <= 0) break;
            ItemStack stack = player.getInventory().main.get(i);
            if (predicate.test(stack)) {
                int take = Math.min(remaining, stack.getCount());
                stack.decrement(take);
                remaining -= take;
            }
        }
    }

    /**
     * 随机消耗玩家物品栏中 1 个属于给定标签的物品。
     * 从所有符合的堆叠中随机选 1 个消耗。
     */
    private void consumeRandomTagItem(PlayerEntity player, TagKey<Item> tag, int count) {
        List<ItemStack> candidates = new ArrayList<>();

        // 副手
        ItemStack offhand = player.getOffHandStack();
        if (offhand.isIn(tag) && offhand.getCount() > 0) {
            candidates.add(offhand);
        }

        for (int i = 0; i < player.getInventory().main.size(); i++) {
            ItemStack stack = player.getInventory().main.get(i);
            if (stack.isIn(tag) && stack.getCount() > 0) {
                candidates.add(stack);
            }
        }

        if (candidates.isEmpty()) return;

        // 随机选取
        Random random = new Random();
        ItemStack chosen = candidates.get(random.nextInt(candidates.size()));
        chosen.decrement(count);
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